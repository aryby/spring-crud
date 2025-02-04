package io.aryby.spring_boot_crud.project_settings;

import io.aryby.spring_boot_crud.custom_table.CustomTable;
import io.aryby.spring_boot_crud.custom_table.CustomTableRepository;
import io.aryby.spring_boot_crud.custom_table_attributes.CustomTableAttributesDTO;
import io.aryby.spring_boot_crud.custom_table_attributes.CustomTableAttributesService;
import io.aryby.spring_boot_crud.database_settings.DatabaseSettings;
import io.aryby.spring_boot_crud.database_settings.DatabaseSettingsRepository;
import io.aryby.spring_boot_crud.developer_preferences.DeveloperPreferences;
import io.aryby.spring_boot_crud.developer_preferences.DeveloperPreferencesRepository;
import io.aryby.spring_boot_crud.general_settings.GeneralSettings;
import io.aryby.spring_boot_crud.general_settings.GeneralSettingsRepository;
import io.aryby.spring_boot_crud.util.NotFoundException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import io.aryby.spring_boot_crud.util.UuidGenerator;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ProjectSettingService {

    private final ProjectSettingsRepository projectSettingRepository;
    private final GeneralSettingsRepository generalSettingsRepository;
    private final DatabaseSettingsRepository databaseSettingsRepository;
    private final DeveloperPreferencesRepository developerPreferencesRepository;
    private final CustomTableRepository customTableRepository;
    private final CustomTableAttributesService customTableAttributesService;


    public ProjectSettingService(final ProjectSettingsRepository projectSettingRepository,
                                  final GeneralSettingsRepository generalSettingsRepository,
                                  final DatabaseSettingsRepository databaseSettingsRepository,
                                  final DeveloperPreferencesRepository developerPreferencesRepository,
                                  final CustomTableRepository customTableRepository,
                                  final CustomTableAttributesService customTableAttributesService) {
        this.projectSettingRepository = projectSettingRepository;
        this.customTableAttributesService = customTableAttributesService;
        this.generalSettingsRepository = generalSettingsRepository;
        this.databaseSettingsRepository = databaseSettingsRepository;
        this.developerPreferencesRepository = developerPreferencesRepository;
        this.customTableRepository = customTableRepository;
    }

    public List<ProjectSettingsDTO> findAll() {
        final List<ProjectSettings> projectSettinges = projectSettingRepository.findAll(Sort.by("id"));
        return projectSettinges.stream()
            .map(projectSettings -> mapToDTO(projectSettings, new ProjectSettingsDTO()))
            .toList();
    }

    public ProjectSettingsDTO getBySlug(final String slug) {
        return projectSettingRepository.findFirstBySlug(slug)
            .map(projectSetting -> mapToDTO(projectSetting, new ProjectSettingsDTO()))
            .orElseThrow(NotFoundException::new);
    }

    public ProjectSettingsDTO get(final Long id) {
        return projectSettingRepository.findById(id)
            .map(projectSetting -> mapToDTO(projectSetting, new ProjectSettingsDTO()))
            .orElseThrow(NotFoundException::new);
    }

    public ProjectSettings create(final ProjectSettingsDTO projectSettingDTO) {
        final ProjectSettings projectSetting = new ProjectSettings();
        projectSettingDTO.setSlug(UuidGenerator.generateUID());

        mapToEntity(projectSettingDTO, projectSetting);
        return projectSettingRepository.save(projectSetting);
    }

    public void update(final Long id, final ProjectSettingsDTO projectSettingDTO) {
        final ProjectSettings projectSetting = projectSettingRepository.findById(id)
            .orElseThrow(NotFoundException::new);
        mapToEntity(projectSettingDTO, projectSetting);
        projectSettingRepository.save(projectSetting);
    }

    public void delete(final Long id) {
        projectSettingRepository.deleteById(id);
    }

    private ProjectSettingsDTO mapToDTO(final ProjectSettings projectSetting,
                                        final ProjectSettingsDTO projectSettingDTO) {
        projectSettingDTO.setId(projectSetting.getId());
        projectSettingDTO.setSlug(projectSetting.getSlug());
        projectSettingDTO.setGeneralSettings(projectSetting.getGeneralSettings() == null ? null : projectSetting.getGeneralSettings());
        projectSettingDTO.setDatabaseSettings(projectSetting.getDatabaseSettings() == null ? null : projectSetting.getDatabaseSettings());
        projectSettingDTO.setDeveloperPreferences(projectSetting.getDeveloperPreferences() == null ? null : projectSetting.getDeveloperPreferences());
        return projectSettingDTO;
    }

    private ProjectSettings mapToEntity(final ProjectSettingsDTO projectSettingDTO,
                                        final ProjectSettings projectSetting) {
        projectSetting.setId(projectSettingDTO.getId());
        projectSetting.setSlug(projectSettingDTO.getSlug());
        final GeneralSettings generalSettings = projectSettingDTO.getGeneralSettings() == null ? null : generalSettingsRepository.findById(projectSettingDTO.getGeneralSettings())
            .orElseThrow(() -> new NotFoundException("generalSettings not found"));
        projectSetting.setGeneralSettings(generalSettings.getId());
        final DatabaseSettings databaseSettings = projectSettingDTO.getDatabaseSettings() == null ? null : databaseSettingsRepository.findById(projectSettingDTO.getDatabaseSettings())
            .orElseThrow(() -> new NotFoundException("databaseSettings not found"));
        projectSetting.setDatabaseSettings(databaseSettings.getId());
        final DeveloperPreferences developerPreferences = projectSettingDTO.getDeveloperPreferences() == null ? null : developerPreferencesRepository.findById(projectSettingDTO.getDeveloperPreferences())
            .orElseThrow(() -> new NotFoundException("developerPreferences not found"));
        projectSetting.setDeveloperPreferences(developerPreferences.getId());
        return projectSetting;
    }


    public byte[] generateZip(Long projectId) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOut = new ZipOutputStream(byteArrayOutputStream);

        ProjectSettings projectSettings = projectSettingRepository.findById(projectId).orElseThrow(() ->
            new RuntimeException("Project settings not found"));

        List<CustomTable> tables = customTableRepository.findAllByprojectSetting(projectId);

        // Create directory structure
        GeneralSettings generalSettings = generalSettingsRepository.findById(projectSettings.getGeneralSettings())
            .get();

        String basePackage = generalSettings.getGroupId().replace(".", "/")
            + "/" + generalSettings.getArtifactId().replace(".", "/");

        String srcMainJava = "src/main/java/" + basePackage + "/";
        String srcMainResources = "src/main/resources/";

        String appColnt=generateSpringBootMain(projectId); // main class spring boot
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
        ProjectSettings projectSetting = projectSettingRepository.findById(projectId).get();
        GeneralSettings generalSettings = generalSettingsRepository.findById(projectSetting.getGeneralSettings()).get();


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

    private String generatePomXml(ProjectSettings projectSetting) {
        GeneralSettings generalSettings = generalSettingsRepository.findById(projectSetting.getGeneralSettings()).get();
        //DatabaseSettings databaseSettings = databaseSettingsRepository.findById(projectSetting.getDatabaseSettings()).get();
        DeveloperPreferences developerPreferences = developerPreferencesRepository.findById(projectSetting.getDeveloperPreferences()).get();

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
        ProjectSettings projectSetting = projectSettingRepository.findById(projectId).get();
        GeneralSettings generalSettings = generalSettingsRepository.findById(projectSetting.getGeneralSettings()).get();

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
