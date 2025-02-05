package io.aryby.spring_boot_crud.generator.implimentations;

import io.aryby.spring_boot_crud.developer_preferences.DeveloperPreferences;
import io.aryby.spring_boot_crud.developer_preferences.DeveloperPreferencesRepository;
import io.aryby.spring_boot_crud.general_settings.GeneralSettings;
import io.aryby.spring_boot_crud.general_settings.GeneralSettingsRepository;
import io.aryby.spring_boot_crud.generator.IPomGenerator;
import io.aryby.spring_boot_crud.project_settings.ProjectSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PomGeneratorImpl implements IPomGenerator {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final DeveloperPreferencesRepository developerPreferencesRepository;
    private final GeneralSettingsRepository generalSettingsRepository;

    public PomGeneratorImpl(DeveloperPreferencesRepository developerPreferencesRepository, GeneralSettingsRepository generalSettingsRepository) {
        this.developerPreferencesRepository = developerPreferencesRepository;
        this.generalSettingsRepository = generalSettingsRepository;
    }

    @Override
    public String generatePomXml(ProjectSettings projectSetting) {
        logger.info("Generate Pom file {} ", projectSetting);
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
            "        </dependency>\n" +
            "        <dependency>\n" +
            "            <groupId>org.projectlombok</groupId>\n" +
            "            <artifactId>lombok</artifactId>\n" +
            "            <optional>true</optional>\n" +
            "        </dependency>\n" +
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
            "        </dependency>\n" +
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
            "    </build>\n" +
            "</project>";
    }

}
