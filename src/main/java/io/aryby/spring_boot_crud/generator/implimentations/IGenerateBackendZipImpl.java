package io.aryby.spring_boot_crud.generator.implimentations;

import io.aryby.spring_boot_crud.custom_table.CustomTable;
import io.aryby.spring_boot_crud.custom_table.CustomTableRepository;
import io.aryby.spring_boot_crud.database_settings.DatabaseSettingsRepository;
import io.aryby.spring_boot_crud.developer_preferences.DeveloperPreferencesRepository;
import io.aryby.spring_boot_crud.general_settings.GeneralSettings;
import io.aryby.spring_boot_crud.general_settings.GeneralSettingsRepository;
import io.aryby.spring_boot_crud.generator.*;
import io.aryby.spring_boot_crud.generator.date_defaults_generator.IDateTimeGenerator;
import io.aryby.spring_boot_crud.generator.files_generator.IUploadImage;
import io.aryby.spring_boot_crud.project_settings.ProjectSettingService;
import io.aryby.spring_boot_crud.project_settings.ProjectSettings;
import io.aryby.spring_boot_crud.project_settings.ProjectSettingsRepository;
import io.aryby.spring_boot_crud.util.MyHelpper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class IGenerateBackendZipImpl implements IGenerateZip {
    private Logger logger = LoggerFactory.getLogger(ProjectSettingService.class);
    private final ProjectSettingsRepository projectSettingRepository;
    private final GeneralSettingsRepository generalSettingsRepository;
    private final CustomTableRepository customTableRepository;
    private final IEntityGenerator entityGenerator;
    private final IRepositoryGenerator interfaceGenerator;
    private final IDTOGenerator dtoGenerator;
    private final IServiceGenerator serviceGenerator;
    private final IControllerGenerator controllerGenerator;
    private final IMainGenerator mainGenerator;
    private final IRequestGenerator requestGenerator;
    private final IPomGenerator generatePomXml;
    private final IUploadImage uploadImage;
    private final IDateTimeGenerator dateTimeGenerator;

    public IGenerateBackendZipImpl(
                                    ProjectSettingsRepository projectSettingRepository, GeneralSettingsRepository generalSettingsRepository, DatabaseSettingsRepository databaseSettingsRepository, DeveloperPreferencesRepository developerPreferencesRepository, CustomTableRepository customTableRepository,
                                   IEntityGenerator entityGenerator, IRepositoryGenerator interfaceGenerator,
                                   IDTOGenerator dtoGenerator, IServiceGenerator serviceGenerator,
                                   IControllerGenerator controllerGenerator, IMainGenerator mainGenerator,
                                   IRequestGenerator requestGenerator, IPomGenerator generatePomXml,
                                   IUploadImage uploadImage,
                                   IDateTimeGenerator dateTimeGenerator) {

        this.projectSettingRepository = projectSettingRepository;
        this.generalSettingsRepository = generalSettingsRepository;
        this.customTableRepository = customTableRepository;
        this.entityGenerator = entityGenerator;
        this.interfaceGenerator = interfaceGenerator;
        this.dtoGenerator = dtoGenerator;
        this.serviceGenerator = serviceGenerator;
        this.controllerGenerator = controllerGenerator;
        this.mainGenerator = mainGenerator;
        this.requestGenerator = requestGenerator;
        this.generatePomXml = generatePomXml;
        this.uploadImage = uploadImage;
        this.dateTimeGenerator = dateTimeGenerator;
    }

    @Override
    public byte[] generateZip(Long projectId) throws IOException {
        logger.info("generateZip {}", projectId);
        IRessourcesGenerator ressourcesGenerator = new IRessourcesGenerator() {
            @Override
            public String generateRessources() {
                return IRessourcesGenerator.super.generateRessources();
            }
        };
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOut = new ZipOutputStream(byteArrayOutputStream);

        ProjectSettings projectSettings = projectSettingRepository.findById(projectId).orElseThrow(() ->
            new RuntimeException("Project settings not found"));

        List<CustomTable> tables = customTableRepository.findAllByprojectSetting(projectId);

        // Create directory structure
        logger.info("Create directory structure");
        logger.info("projectSettings.getGeneralSettings() =  {} ", projectSettings.getGeneralSettings());
        Optional<GeneralSettings> generalSgs = generalSettingsRepository.findById(projectSettings.getGeneralSettings());

        String basePackage = "";
        try {
            logger.info("getting general settings : {}", generalSgs);
            GeneralSettings generalSettings = generalSgs.get();
            basePackage = generalSettings.getGroupId().replace(".", "/")
                + "/" + generalSettings.getArtifactId().replace(".", "/");

        } catch (Exception e) {
            basePackage="com.example";
            logger.info("Error general settings : {}", e.getMessage());
        }


        String srcMainJava = "src/main/java/" + basePackage + "/";
        String srcMainResources = "src/main/resources/";


        String STARTER_APP = mainGenerator.generateSpringBootMain(projectId); // main class spring boot
        ZipEntry zipEntry1 = new ZipEntry(srcMainJava + "StarterApp.java");
        zipOut.putNextEntry(zipEntry1);
        zipOut.write(STARTER_APP.getBytes());
        zipOut.closeEntry();

        // adding class upload file to util package
        String PACKAGE_UTILS = (basePackage + ".utils").replace("/",".");
        String utilsUploadFileContent = String.valueOf(uploadImage.upladFile(PACKAGE_UTILS));
        ZipEntry uploadExample = new ZipEntry(srcMainJava + "utils/UploadMyFile.java");
        zipOut.putNextEntry(uploadExample);
        zipOut.write(utilsUploadFileContent.getBytes());
        zipOut.closeEntry();

        // adding class DateTime Default Configutil package
        String PACKAGE_DATE_DEFAULTS = (basePackage).replace("/",".");
        String dateTimeJpaConfig = String.valueOf(dateTimeGenerator.generateNowByDefault(PACKAGE_DATE_DEFAULTS));
        ZipEntry configJpaPath = new ZipEntry(srcMainJava + "utils/DateTimeDefaultsConfig.java");
        zipOut.putNextEntry(configJpaPath);
        zipOut.write(dateTimeJpaConfig.getBytes());
        zipOut.closeEntry();


        // Add Java class files
        for (CustomTable table : tables) {
            String classContent = entityGenerator.generateJavaClass(table, projectId);
            ZipEntry zipEntry = new ZipEntry(srcMainJava + "entities/" + table.getName() + ".java");
            zipOut.putNextEntry(zipEntry);
            zipOut.write(classContent.getBytes());
            zipOut.closeEntry();
        }

        // Add Repository class files
        for (CustomTable table : tables) {
            String classContent = interfaceGenerator.generate(table, projectId);
            ZipEntry zipEntry = new ZipEntry(srcMainJava + "repositories/" + MyHelpper.capitalizeFirstLetter(table.getName()) + "Repository.java");
            zipOut.putNextEntry(zipEntry);
            zipOut.write(classContent.getBytes());
            zipOut.closeEntry();
        }

        // Add DTO class files
        for (CustomTable table : tables) {
            String classContent = dtoGenerator.generateDTOClass(table, projectId);
            ZipEntry zipEntry = new ZipEntry(srcMainJava + "dtos/" + MyHelpper.capitalizeFirstLetter(table.getName()) + "DTO.java");
            zipOut.putNextEntry(zipEntry);
            zipOut.write(classContent.getBytes());
            zipOut.closeEntry();
        }

        // Add SERVIces class files
        for (CustomTable table : tables) {
            String classContent = serviceGenerator.generate(table, projectId);
            ZipEntry zipEntry = new ZipEntry(srcMainJava + "services/" + MyHelpper.capitalizeFirstLetter(table.getName()) + "Service.java");
            zipOut.putNextEntry(zipEntry);
            zipOut.write(classContent.getBytes());
            zipOut.closeEntry();
        }

        // Add Requests class files
        for (CustomTable table : tables) {
            String classContent = requestGenerator.generateRequestClass(table, projectId);
            ZipEntry zipEntry = new ZipEntry(srcMainJava + "requests/" + MyHelpper.capitalizeFirstLetter(table.getName()) + "Request.java");
            zipOut.putNextEntry(zipEntry);
            zipOut.write(classContent.getBytes());
            zipOut.closeEntry();
        }

        // Add Controller class files
        for (CustomTable table : tables) {
            String classContent = controllerGenerator.generate(table, projectId);
            ZipEntry zipEntry = new ZipEntry(srcMainJava + "controllers/" + MyHelpper.capitalizeFirstLetter(table.getName()) + "Controller.java");
            zipOut.putNextEntry(zipEntry);
            zipOut.write(classContent.getBytes());
            zipOut.closeEntry();
        }

        // Add pom.xml
        String pomContent = generatePomXml.generatePomXml(projectSettings);
        ZipEntry pomEntry = new ZipEntry("pom.xml");
        zipOut.putNextEntry(pomEntry);
        zipOut.write(pomContent.getBytes());
        zipOut.closeEntry();

        // add ressources
        String propertiesContent = ressourcesGenerator.generateRessources();
        ZipEntry propertiesEntry = new ZipEntry(srcMainResources + "application.properties");
        zipOut.putNextEntry(propertiesEntry);
        zipOut.write(propertiesContent.getBytes());
        zipOut.closeEntry();


        zipOut.close();
        return byteArrayOutputStream.toByteArray();
    }

}
