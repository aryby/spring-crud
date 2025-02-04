package io.aryby.spring_boot_crud.project_settings;

import io.aryby.spring_boot_crud.SpringBootCrudApplication;
import io.aryby.spring_boot_crud.custom_table.CustomTable;
import io.aryby.spring_boot_crud.custom_table.CustomTableRepository;
import io.aryby.spring_boot_crud.custom_table_attributes.CustomTableAttributes;
import io.aryby.spring_boot_crud.custom_table_attributes.CustomTableAttributesDTO;
import io.aryby.spring_boot_crud.custom_table_attributes.CustomTableAttributesService;
import io.aryby.spring_boot_crud.database_settings.DatabaseSettings;
import io.aryby.spring_boot_crud.database_settings.DatabaseSettingsRepository;
import io.aryby.spring_boot_crud.developer_preferences.DeveloperPreferences;
import io.aryby.spring_boot_crud.developer_preferences.DeveloperPreferencesRepository;
import io.aryby.spring_boot_crud.general_settings.GeneralSettings;
import io.aryby.spring_boot_crud.general_settings.GeneralSettingsRepository;
import io.aryby.spring_boot_crud.util.NotFoundException;
import io.aryby.spring_boot_crud.util.ReferencedWarning;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.boot.SpringApplication;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ProjectSettingsService {

    private final ProjectSettingsRepository projectSettingsRepository;
    private final GeneralSettingsRepository generalSettingsRepository;
    private final DatabaseSettingsRepository databaseSettingsRepository;
    private final DeveloperPreferencesRepository developerPreferencesRepository;
    private final CustomTableRepository customTableRepository;
    private final CustomTableAttributesService customTableAttributesService;


    public ProjectSettingsService(final ProjectSettingsRepository projectSettingsRepository,
                                  final GeneralSettingsRepository generalSettingsRepository,
                                  final DatabaseSettingsRepository databaseSettingsRepository,
                                  final DeveloperPreferencesRepository developerPreferencesRepository,
                                  final CustomTableRepository customTableRepository,
                                  final CustomTableAttributesService customTableAttributesService) {
        this.projectSettingsRepository = projectSettingsRepository;
        this.customTableAttributesService = customTableAttributesService;
        this.generalSettingsRepository = generalSettingsRepository;
        this.databaseSettingsRepository = databaseSettingsRepository;
        this.developerPreferencesRepository = developerPreferencesRepository;
        this.customTableRepository = customTableRepository;
    }

    public List<ProjectSettingsDTO> findAll() {
        final List<ProjectSettings> projectSettingses = projectSettingsRepository.findAll(Sort.by("id"));
        return projectSettingses.stream()
            .map(projectSettings -> mapToDTO(projectSettings, new ProjectSettingsDTO()))
            .toList();
    }

    public ProjectSettingsDTO get(final Long id) {
        return projectSettingsRepository.findById(id)
            .map(projectSettings -> mapToDTO(projectSettings, new ProjectSettingsDTO()))
            .orElseThrow(NotFoundException::new);
    }

    public Long create(final ProjectSettingsDTO projectSettingsDTO) {
        final ProjectSettings projectSettings = new ProjectSettings();
        mapToEntity(projectSettingsDTO, projectSettings);
        return projectSettingsRepository.save(projectSettings).getId();
    }

    public void update(final Long id, final ProjectSettingsDTO projectSettingsDTO) {
        final ProjectSettings projectSettings = projectSettingsRepository.findById(id)
            .orElseThrow(NotFoundException::new);
        mapToEntity(projectSettingsDTO, projectSettings);
        projectSettingsRepository.save(projectSettings);
    }

    public void delete(final Long id) {
        projectSettingsRepository.deleteById(id);
    }

    private ProjectSettingsDTO mapToDTO(final ProjectSettings projectSettings,
                                        final ProjectSettingsDTO projectSettingsDTO) {
        projectSettingsDTO.setId(projectSettings.getId());
        projectSettingsDTO.setGeneralSettings(projectSettings.getGeneralSettings() == null ? null : projectSettings.getGeneralSettings());
        projectSettingsDTO.setDatabaseSettings(projectSettings.getDatabaseSettings() == null ? null : projectSettings.getDatabaseSettings());
        projectSettingsDTO.setDeveloperPreferences(projectSettings.getDeveloperPreferences() == null ? null : projectSettings.getDeveloperPreferences());
        return projectSettingsDTO;
    }

    private ProjectSettings mapToEntity(final ProjectSettingsDTO projectSettingsDTO,
                                        final ProjectSettings projectSettings) {
        final GeneralSettings generalSettings = projectSettingsDTO.getGeneralSettings() == null ? null : generalSettingsRepository.findById(projectSettingsDTO.getGeneralSettings())
            .orElseThrow(() -> new NotFoundException("generalSettings not found"));
        projectSettings.setGeneralSettings(generalSettings.getId());
        final DatabaseSettings databaseSettings = projectSettingsDTO.getDatabaseSettings() == null ? null : databaseSettingsRepository.findById(projectSettingsDTO.getDatabaseSettings())
            .orElseThrow(() -> new NotFoundException("databaseSettings not found"));
        projectSettings.setDatabaseSettings(databaseSettings.getId());
        final DeveloperPreferences developerPreferences = projectSettingsDTO.getDeveloperPreferences() == null ? null : developerPreferencesRepository.findById(projectSettingsDTO.getDeveloperPreferences())
            .orElseThrow(() -> new NotFoundException("developerPreferences not found"));
        projectSettings.setDeveloperPreferences(developerPreferences.getId());
        return projectSettings;
    }


    public byte[] generateZip(Long projectId) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOut = new ZipOutputStream(byteArrayOutputStream);

        ProjectSettings projectSettings = projectSettingsRepository.findById(projectId).orElseThrow(() ->
            new RuntimeException("Project settings not found"));

        List<CustomTable> tables = customTableRepository.findAllByProjectSettings(projectId);

        // Create directory structure
        GeneralSettings generalSettings = generalSettingsRepository.findById(projectSettings.getGeneralSettings())
            .get();

        String basePackage = generalSettings.getGroupId().replace(".", "/")
            + "/" + generalSettings.getArtifactId().replace(".", "/");

        String srcMainJava = "src/main/java/" + basePackage + "/";
        String srcMainResources = "src/main/resources/";

        String appColnt=generateSpringBootMain(projectId);
        ZipEntry zipEntry1 = new ZipEntry(srcMainJava + "StarterApp.java");
        zipOut.putNextEntry(zipEntry1);
        zipOut.write(appColnt.getBytes());
        zipOut.closeEntry();
        // Add Java class files
        for (CustomTable table : tables) {
            String classContent = generateJavaClass(table, projectId);
            ZipEntry zipEntry = new ZipEntry(srcMainJava + "entities/" + table.getName() + ".java");
            zipOut.putNextEntry(zipEntry);
            zipOut.write(classContent.getBytes());
            zipOut.closeEntry();
        }

        // Add pom.xml
        String pomContent = generatePomXml(projectSettings);
        ZipEntry pomEntry = new ZipEntry("pom.xml");
        zipOut.putNextEntry(pomEntry);
        zipOut.write(pomContent.getBytes());
        zipOut.closeEntry();

        // add ressources
        String propertiesContent = generateRessources();
        ZipEntry propertiesEntry = new ZipEntry(srcMainResources+"application.properties");
        zipOut.putNextEntry(propertiesEntry);
        zipOut.write(propertiesContent.getBytes());
        zipOut.closeEntry();


        zipOut.close();
        return byteArrayOutputStream.toByteArray();
    }

    private String generateJavaClass(CustomTable table, Long projectId) {
        ProjectSettings projectSettings = projectSettingsRepository.findById(projectId).get();
        GeneralSettings generalSettings = generalSettingsRepository.findById(projectSettings.getGeneralSettings()).get();


        StringBuilder sb = new StringBuilder();

        sb.append("package " + generalSettings.getGroupId() + "." + generalSettings.getArtifactId() + ".entities;\n\n");

        sb.append("\n" +
            "\n" +
            "import jakarta.persistence.Column;\n" +
            "import jakarta.persistence.Entity;\n" +
            "import jakarta.persistence.EntityListeners;\n" +
            "import jakarta.persistence.GeneratedValue;\n" +
            "import jakarta.persistence.GenerationType;\n" +
            "import jakarta.persistence.Id;\n" +
            "import jakarta.persistence.Table;\n" +
            "import java.time.OffsetDateTime;\n" +
            "import lombok.Getter;\n" +
            "import lombok.Setter;\n" +
            "import org.springframework.data.annotation.CreatedDate;\n" +
            "import org.springframework.data.annotation.LastModifiedDate;\n" +
            "import org.springframework.data.jpa.domain.support.AuditingEntityListener;\n" +
            "\n" +
            "\n" +
            "@Entity\n" +
            "@Table(name = \""+table.getName().toLowerCase()+"s\")\n" +
            "@EntityListeners(AuditingEntityListener.class)\n" +
            "@Getter\n" +
            "@Setter\n");
        sb.append("public class ").append(table.getName()).append(" {\n");
        sb.append("\n" +
            "    @Id\n" +
            "    @Column(nullable = false, updatable = false)\n" +
            "    @GeneratedValue(strategy = GenerationType.IDENTITY)\n" +
            "    private Long id;" +
            "\n");
        for (CustomTableAttributesDTO attr : customTableAttributesService.findAllByTableId(table.getId())) {

                sb.append("    private  ")
                .append(attr.getNameTypeModifier())
                .append(" ")
                .append(attr.getNameAttribute())
                .append(";\n");


        }

        sb.append("\n" +
            "    @CreatedDate\n" +
            "    @Column(nullable = false, updatable = false)\n" +
            "    private OffsetDateTime dateCreated;\n" +
            "\n" +
            "    @LastModifiedDate\n" +
            "    @Column(nullable = false)\n" +
            "    private OffsetDateTime lastUpdated;\n");
        sb.append("}");
        return sb.toString();
    }

    private String generatePomXml(ProjectSettings projectSettings) {
        GeneralSettings generalSettings = generalSettingsRepository.findById(projectSettings.getGeneralSettings()).get();
        //DatabaseSettings databaseSettings = databaseSettingsRepository.findById(projectSettings.getDatabaseSettings()).get();
        DeveloperPreferences developerPreferences = developerPreferencesRepository.findById(projectSettings.getDeveloperPreferences()).get();

        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            " xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
            " <modelVersion>4.0.0</modelVersion>\n" +
            "\n" +
            "\n" +
            "    <parent>\n" +
            "         <groupId>org.springframework.boot</groupId>\n" +
            "         <artifactId>spring-boot-starter-parent</artifactId>\n" +
            "         <version>3.4.2</version>\n" +
            "         <relativePath />\n" +
            "    </parent>\n" +
            "\n" +
            "    <groupId>" + generalSettings.getGroupId() + "</groupId>\n" +
            "    <artifactId>" + generalSettings.getArtifactId() + "</artifactId>\n" +
            "    <version>1.0-SNAPSHOT</version>\n" +
            "    <name>" + generalSettings.getProjectName() + "</name>" +
            "    <properties>\n" +
            "    <java.version>" + developerPreferences.getJavaVersion() + "</java.version>\n" +
            "    </properties>\n" +
            "\n" +
            "    <dependencies>\n" +
            "        <dependency>\n" +
            "            <groupId>org.springframework.boot</groupId>\n" +
            "            <artifactId>spring-boot-starter-web</artifactId>\n" +
            "        </dependency>\n" +
            "        <dependency>\n" +
            "            <groupId>org.springframework.boot</groupId>\n" +
            "            <artifactId>spring-boot-starter-validation</artifactId>\n" +
            "        </dependency>\n" +
            "        <dependency>\n" +
            "            <groupId>org.springframework.boot</groupId>\n" +
            "            <artifactId>spring-boot-starter-data-jpa</artifactId>\n" +
            "        </dependency>\n" +
            "        <dependency>\n" +
            "            <groupId>com.mysql</groupId>\n" +
            "            <artifactId>mysql-connector-j</artifactId>\n" +
            "            <scope>runtime</scope>\n" +
            "        </dependency>\n"+
            "        <dependency>\n" +
            "            <groupId>org.projectlombok</groupId>\n" +
            "            <artifactId>lombok</artifactId>\n" +
            "            <optional>true</optional>\n" +
            "        </dependency>\n"+
            "        <dependency>\n" +
            "            <groupId>org.springframework.boot</groupId>\n" +
            "            <artifactId>spring-boot-devtools</artifactId>\n" +
            "            <scope>runtime</scope>\n" +
            "            <optional>true</optional>\n" +
            "        </dependency>\n" +
            "        <dependency>\n" +
            "            <groupId>org.springframework.boot</groupId>\n" +
            "            <artifactId>spring-boot-starter-test</artifactId>\n" +
            "            <scope>test</scope>\n" +
            "        </dependency>\n"+
            "   </dependencies>\n" +
            "   <build>\n" +
            "        <plugins>\n" +
            "            <plugin>\n" +
            "                <groupId>org.springframework.boot</groupId>\n" +
            "                <artifactId>spring-boot-maven-plugin</artifactId>\n" +
            "                <configuration>\n" +
            "                    <profiles>\n" +
            "                        <profile>local</profile>\n" +
            "                    </profiles>\n" +
            "                    <excludes>\n" +
            "                        <exclude>\n" +
            "                            <groupId>org.projectlombok</groupId>\n" +
            "                            <artifactId>lombok</artifactId>\n" +
            "                        </exclude>\n" +
            "                    </excludes>\n" +
            "                </configuration>\n" +
            "            </plugin>\n" +
            "        </plugins>\n" +
            "    </build>\n"+
            "</project>";
    }

    private String generateRessources() {
        return "" +
            "spring.datasource.url=${JDBC_DATABASE_URL\\:jdbc\\:mysql\\://localhost\\:3306/spring-boot-crud?serverTimezone\\=UTC}\n" +
            "spring.datasource.username=${JDBC_DATABASE_USERNAME\\:root}\n" +
            "spring.datasource.password=${JDBC_DATABASE_PASSWORD\\:root}\n" +
            "spring.datasource.hikari.connection-timeout=30000\n" +
            "spring.datasource.hikari.maximum-pool-size=10\n" +
            "spring.jpa.hibernate.ddl-auto=create-drop\n" +
            "spring.jpa.open-in-view=false\n" +
            "spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true\n" +
            "spring.jpa.properties.hibernate.id.new_generator_mappings=true\n";
    }


    private String generateSpringBootMain(Long projectId) {
        ProjectSettings projectSettings = projectSettingsRepository.findById(projectId).get();
        GeneralSettings generalSettings = generalSettingsRepository.findById(projectSettings.getGeneralSettings()).get();


        StringBuilder sb = new StringBuilder();

        sb.append("package " + generalSettings.getGroupId() + "." + generalSettings.getArtifactId() + ";\n\n");


        sb.append("import org.springframework.boot.SpringApplication;\n\n");
        sb.append("public class ").
            append("StarterApp").
            append(" {\n");
        sb.append("\n");
        sb.append("public static void main(final String[] args) {")
            .append("\n")
            .append("    SpringApplication.run(StarterApp.class, args);")
            .append("\n")
            .append("}")
            .append(";\n");

        sb.append("}");
        return sb.toString();
    }
}
