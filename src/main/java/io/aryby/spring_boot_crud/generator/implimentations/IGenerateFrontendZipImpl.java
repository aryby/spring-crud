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
import io.aryby.spring_boot_crud.generator.frontend.*;
import io.aryby.spring_boot_crud.project_settings.ProjectSettingService;
import io.aryby.spring_boot_crud.project_settings.ProjectSettings;
import io.aryby.spring_boot_crud.project_settings.ProjectSettingsRepository;
import io.aryby.spring_boot_crud.util.CapitalizeFirstChar;
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
public class IGenerateFrontendZipImpl implements IGenerateZip {
    private Logger logger = LoggerFactory.getLogger(ProjectSettingService.class);
    private final ProjectSettingsRepository projectSettingRepository;
    private final GeneralSettingsRepository generalSettingsRepository;
    private final CustomTableRepository customTableRepository;

    private final IPackageJson packageJson;
    private final IAngularAppModuleTs angularAppModule;
    private final IAngularAppComponent angularAppComponent;
    private final IAngularIndex angularIndex;
    private final IAngularJson angularJson;
    private final IAngularMainTs angularMainTs;

    public IGenerateFrontendZipImpl(ProjectSettingsRepository projectSettingRepository,
                                    GeneralSettingsRepository generalSettingsRepository,
                                    CustomTableRepository customTableRepository, IPackageJson packageJson, IAngularAppModuleTs angularAppModule, IAngularAppComponent angularAppComponent, IAngularIndex angularIndex, IAngularJson angularJson, IAngularMainTs angularMainTs) {
        this.projectSettingRepository = projectSettingRepository;
        this.generalSettingsRepository = generalSettingsRepository;
        this.customTableRepository = customTableRepository;
        this.packageJson = packageJson;
        this.angularAppModule = angularAppModule;
        this.angularAppComponent = angularAppComponent;
        this.angularIndex = angularIndex;
        this.angularJson = angularJson;
        this.angularMainTs = angularMainTs;
    }

    @Override
    public byte[] generateZip(Long projectId) throws IOException {
        logger.info("generateZip {}", projectId);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOut = new ZipOutputStream(byteArrayOutputStream);

        ProjectSettings projectSettings = projectSettingRepository.findById(projectId).orElseThrow(() ->
            new RuntimeException("Project settings not found"));

        List<CustomTable> tables = customTableRepository.findAllByprojectSetting(projectId);

        // Create directory structure
        logger.info("projectSettings.getGeneralSettings() =  {} ", projectSettings.getGeneralSettings());
        Optional<GeneralSettings> generalSgs = generalSettingsRepository.findById(projectSettings.getGeneralSettings());




        String angularRoot = "angular-app/";
        String angularSrcApp = "angular-app/src/app";
        String angularSrc = "angular-app/src/";


        String PACKAGE_JSON = packageJson.generatePackageJson(projectId); // main class spring boot
        ZipEntry zipEntry1 = new ZipEntry(angularRoot + "package.json");
        zipOut.putNextEntry(zipEntry1);
        zipOut.write(PACKAGE_JSON.getBytes());
        zipOut.closeEntry();

        String PACKAGE_ANGULAR = packageJson.generatePackageJson(projectId); // main class spring boot
        ZipEntry zipEntry2 = new ZipEntry(angularRoot + "angular.json");
        zipOut.putNextEntry(zipEntry2);
        zipOut.write(PACKAGE_ANGULAR.getBytes());
        zipOut.closeEntry();

        // Add pom.xml
        String ANGULAR_INDEX = angularIndex.generateAngularIndex(projectId);
        ZipEntry INDEX_PATH = new ZipEntry(angularSrc+"index.html");
        zipOut.putNextEntry(INDEX_PATH);
        zipOut.write(ANGULAR_INDEX.getBytes());
        zipOut.closeEntry();

        // add ressources
        String MAIN_TS = angularMainTs.generateAngularMainTs(projectId);
        ZipEntry MAIN_PATH = new ZipEntry(angularSrc+ "main.ts");
        zipOut.putNextEntry(MAIN_PATH);
        zipOut.write(MAIN_TS.getBytes());
        zipOut.closeEntry();

        String MODULL_APP = angularMainTs.generateAngularMainTs(projectId);
        ZipEntry MODULE_PATH = new ZipEntry(angularSrcApp+ "app.module.ts");
        zipOut.putNextEntry(MODULE_PATH);
        zipOut.write(MODULL_APP.getBytes());
        zipOut.closeEntry();
        String MODULL_APP_ROUTING = angularMainTs.generateAngularMainTs(projectId);
        ZipEntry MODULE_PATH_ROUTING = new ZipEntry(angularSrcApp+ "app-routing.module.ts");
        zipOut.putNextEntry(MODULE_PATH_ROUTING);
        zipOut.write(MODULL_APP_ROUTING.getBytes());
        zipOut.closeEntry();
/*

        // Add Java class files
        for (CustomTable table : tables) {
            String classContent = entityGenerator.generateJavaClass(table, projectId);
            ZipEntry zipEntry = new ZipEntry(angularSrc + "entities/" + table.getName() + ".java");
            zipOut.putNextEntry(zipEntry);
            zipOut.write(classContent.getBytes());
            zipOut.closeEntry();
        }

        // Add Repository class files
        for (CustomTable table : tables) {
            String classContent = interfaceGenerator.generate(table, projectId);
            ZipEntry zipEntry = new ZipEntry(angularSrc + "repositories/" + CapitalizeFirstChar.capitalizeFirstLetter(table.getName()) + "Repository.java");
            zipOut.putNextEntry(zipEntry);
            zipOut.write(classContent.getBytes());
            zipOut.closeEntry();
        }

        // Add DTO class files
        for (CustomTable table : tables) {
            String classContent = dtoGenerator.generateDTOClass(table, projectId);
            ZipEntry zipEntry = new ZipEntry(angularSrc + "dtos/" + CapitalizeFirstChar.capitalizeFirstLetter(table.getName()) + "DTO.java");
            zipOut.putNextEntry(zipEntry);
            zipOut.write(classContent.getBytes());
            zipOut.closeEntry();
        }

        // Add SERVIces class files
        for (CustomTable table : tables) {
            String classContent = serviceGenerator.generate(table, projectId);
            ZipEntry zipEntry = new ZipEntry(angularSrc + "services/" + CapitalizeFirstChar.capitalizeFirstLetter(table.getName()) + "Service.java");
            zipOut.putNextEntry(zipEntry);
            zipOut.write(classContent.getBytes());
            zipOut.closeEntry();
        }

        // Add Requests class files
        for (CustomTable table : tables) {
            String classContent = requestGenerator.generateRequestClass(table, projectId);
            ZipEntry zipEntry = new ZipEntry(angularSrc + "requests/" + CapitalizeFirstChar.capitalizeFirstLetter(table.getName()) + "Request.java");
            zipOut.putNextEntry(zipEntry);
            zipOut.write(classContent.getBytes());
            zipOut.closeEntry();
        }

        // Add Controller class files
        for (CustomTable table : tables) {
            String classContent = controllerGenerator.generate(table, projectId);
            ZipEntry zipEntry = new ZipEntry(angularSrc + "controllers/" + CapitalizeFirstChar.capitalizeFirstLetter(table.getName()) + "Controller.java");
            zipOut.putNextEntry(zipEntry);
            zipOut.write(classContent.getBytes());
            zipOut.closeEntry();
        }
*/




        zipOut.close();
        return byteArrayOutputStream.toByteArray();
    }

}
