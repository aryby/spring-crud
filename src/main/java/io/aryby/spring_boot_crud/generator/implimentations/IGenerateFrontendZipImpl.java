package io.aryby.spring_boot_crud.generator.implimentations;

import io.aryby.spring_boot_crud.custom_table.CustomTable;
import io.aryby.spring_boot_crud.custom_table.CustomTableRepository;
import io.aryby.spring_boot_crud.general_settings.GeneralSettings;
import io.aryby.spring_boot_crud.general_settings.GeneralSettingsRepository;
import io.aryby.spring_boot_crud.generator.*;
import io.aryby.spring_boot_crud.generator.frontend.*;
import io.aryby.spring_boot_crud.project_settings.ProjectSettingService;
import io.aryby.spring_boot_crud.project_settings.ProjectSettings;
import io.aryby.spring_boot_crud.project_settings.ProjectSettingsRepository;
import io.aryby.spring_boot_crud.util.MyHelpper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static io.aryby.spring_boot_crud.util.MyHelpper.zipDirectory;

@Service
public class IGenerateFrontendZipImpl implements IGenerateZip {
    private Logger logger = LoggerFactory.getLogger(ProjectSettingService.class);
    private final ProjectSettingsRepository projectSettingRepository;
    private final GeneralSettingsRepository generalSettingsRepository;
    private final CustomTableRepository customTableRepository;

    private final IAngularAppModuleTs angularAppModule;
    private final IAngularAppComponent angularAppComponent;
    private final IAngularAppModuleRoutingTs appModuleRoutingTs;
    private final IAngularServiceGenerator angularServiceGenerator;

    private final IAngularComponentsGenerator angularComponentsGenerator;
    private final IModelGenerator modelGenerator;

    public IGenerateFrontendZipImpl(ProjectSettingsRepository projectSettingRepository,
                                    GeneralSettingsRepository generalSettingsRepository,
                                    CustomTableRepository customTableRepository,
                                    IAngularAppModuleTs angularAppModule,
                                    IAngularAppComponent angularAppComponent,
                                    IAngularAppModuleRoutingTs appModuleRoutingTs, IAngularServiceGenerator angularServiceGenerator, IAngularComponentsGenerator angularComponentsGenerator, IModelGenerator modelGenerator) {
        this.projectSettingRepository = projectSettingRepository;
        this.generalSettingsRepository = generalSettingsRepository;
        this.customTableRepository = customTableRepository;
        this.angularAppModule = angularAppModule;
        this.angularAppComponent = angularAppComponent;
        this.appModuleRoutingTs = appModuleRoutingTs;
        this.angularServiceGenerator = angularServiceGenerator;
        this.angularComponentsGenerator = angularComponentsGenerator;
        this.modelGenerator = modelGenerator;
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
        String angularSrcApp = "angular-app/src/app/";
        String angularSrcAppMvc = "angular-app/src/app/mvc/";
        String angularSrc = "angular-app/src/";

        // generate css assets folder
        String textCss = "h1 { color: blue; }";
        ZipEntry zipEntryCSS = new ZipEntry(angularSrc + "assets/style.css");
        zipOut.putNextEntry(zipEntryCSS);
        zipOut.write(textCss.getBytes());
        zipOut.closeEntry();
//        // src/app/app.component.ts
//        String appVomponnt = angularAppComponent.generateAngularAppComponent(projectId);
//        ZipEntry zipappcomp = new ZipEntry(angularSrcApp + "app.component.ts");
//        zipOut.putNextEntry(zipappcomp);
//        zipOut.write(appVomponnt.getBytes());
//        zipOut.closeEntry();
//
//
//        // environments
//        String textenvironments = """
//            export const environment = {
//                production: false,
//                apiPath: 'http://localhost:8080',
//                PROJECT_TYPE: 'V_01',
//            };
//
//            """;
//        ZipEntry zipEntryenvironments = new ZipEntry(angularSrc + "environments/environment.ts");
//        zipOut.putNextEntry(zipEntryenvironments);
//        zipOut.write(textenvironments.getBytes());
//        zipOut.closeEntry();
//
//        // Generate package.json
//        String PACKAGE_JSON = packageJson.generatePackageJson(projectId); // main class spring boot
//        ZipEntry zipEntry1 = new ZipEntry(angularRoot + "package.json");
//        zipOut.putNextEntry(zipEntry1);
//        zipOut.write(PACKAGE_JSON.getBytes());
//        zipOut.closeEntry();
//
//        // Generate angular.json
//        String PACKAGE_ANGULAR = angularJson.generateAngularJson(projectId); // main class spring boot
//        ZipEntry zipEntry2 = new ZipEntry(angularRoot + "angular.json");
//        zipOut.putNextEntry(zipEntry2);
//        zipOut.write(PACKAGE_ANGULAR.getBytes());
//        zipOut.closeEntry();
//
//        // Generate index.html
//        String ANGULAR_INDEX = angularIndex.generateAngularIndex(projectId);
//        ZipEntry INDEX_PATH = new ZipEntry(angularSrc+"index.html");
//        zipOut.putNextEntry(INDEX_PATH);
//        zipOut.write(ANGULAR_INDEX.getBytes());
//        zipOut.closeEntry();
//
//        // Generate main.ts
//        String MAIN_TS = angularMainTs.generateAngularMainTs(projectId);
//        ZipEntry MAIN_PATH = new ZipEntry(angularSrc+ "main.ts");
//        zipOut.putNextEntry(MAIN_PATH);
//        zipOut.write(MAIN_TS.getBytes());
//        zipOut.closeEntry();
//
        // generate app.module.ts
        String MODULL_APP = angularAppModule.generateAngularAppModuleTs(projectId);
        logger.info("generate MODULE APP");
        ZipEntry MODULE_PATH = new ZipEntry(angularSrcApp+ "app.module.ts");
        zipOut.putNextEntry(MODULE_PATH);
        zipOut.write(MODULL_APP.getBytes());
        zipOut.closeEntry();

        // generate list ts component
        for (CustomTable table : tables) {
            String listComponentTs  = angularComponentsGenerator.generateAngularComponentsTs(table, projectId);
            ZipEntry entry = new ZipEntry(angularSrcAppMvc + "components/" + table.getName().toLowerCase() +"/"+ table.getName().toLowerCase() + ".component.ts");

            zipOut.putNextEntry(entry);
            zipOut.write(listComponentTs.getBytes());
            zipOut.closeEntry();
        }

        // generate list html component
        for (CustomTable table : tables) {
           String listComponentHtml  = angularComponentsGenerator.generateAngularComponentsHtml(table, projectId);
            ZipEntry entryhtml = new ZipEntry(angularSrcAppMvc + "components/" + table.getName().toLowerCase() +"/"+ table.getName().toLowerCase() + ".component.html");

            zipOut.putNextEntry(entryhtml);
            zipOut.write(listComponentHtml.getBytes());
            zipOut.closeEntry();
        }

//
//
//
//
//        // generate app-routing.module.ts
//        String MODULL_APP_ROUTING = appModuleRoutingTs.generateAngularAppModuleTs(projectId);
//        ZipEntry MODULE_PATH_ROUTING = new ZipEntry(angularSrcApp+ "app-routing.module.ts");
//        zipOut.putNextEntry(MODULE_PATH_ROUTING);
//        zipOut.write(MODULL_APP_ROUTING.getBytes());
//        zipOut.closeEntry();



        // Add Angular class files
        for (CustomTable table : tables) {
            String classContent = modelGenerator.generateAngularModel(table, projectId);
            ZipEntry zipEntry = new ZipEntry(angularSrcAppMvc + "models/" + table.getName().toLowerCase() + ".entity.ts");
            zipOut.putNextEntry(zipEntry);
            zipOut.write(classContent.getBytes());
            zipOut.closeEntry();
        }

        // Add Service Angular files
        for (CustomTable table : tables) {
            String classContent = angularServiceGenerator.generateAngularService(table, projectId);
            ZipEntry zipEntry = new ZipEntry(angularSrcAppMvc + "services/" + table.getName().toLowerCase() + ".service.ts");
            zipOut.putNextEntry(zipEntry);
            zipOut.write(classContent.getBytes());
            zipOut.closeEntry();
        }
/*
        // Add DTO class files
        for (CustomTable table : tables) {
            String classContent = dtoGenerator.generateDTOClass(table, projectId);
            ZipEntry zipEntry = new ZipEntry(angularSrc + "dtos/" + MyHelpper.capitalizeFirstLetter(table.getName()) + "DTO.java");
            zipOut.putNextEntry(zipEntry);
            zipOut.write(classContent.getBytes());
            zipOut.closeEntry();
        }

        // Add SERVIces class files
        for (CustomTable table : tables) {
            String classContent = serviceGenerator.generate(table, projectId);
            ZipEntry zipEntry = new ZipEntry(angularSrc + "services/" + MyHelpper.capitalizeFirstLetter(table.getName()) + "Service.java");
            zipOut.putNextEntry(zipEntry);
            zipOut.write(classContent.getBytes());
            zipOut.closeEntry();
        }

        // Add Requests class files
        for (CustomTable table : tables) {
            String classContent = requestGenerator.generateRequestClass(table, projectId);
            ZipEntry zipEntry = new ZipEntry(angularSrc + "requests/" + MyHelpper.capitalizeFirstLetter(table.getName()) + "Request.java");
            zipOut.putNextEntry(zipEntry);
            zipOut.write(classContent.getBytes());
            zipOut.closeEntry();
        }

        // Add Controller class files
        for (CustomTable table : tables) {
            String classContent = controllerGenerator.generate(table, projectId);
            ZipEntry zipEntry = new ZipEntry(angularSrc + "controllers/" + MyHelpper.capitalizeFirstLetter(table.getName()) + "Controller.java");
            zipOut.putNextEntry(zipEntry);
            zipOut.write(classContent.getBytes());
            zipOut.closeEntry();
        }
    */

        String folderToZipPath = "src/main/resources/static/";

        File folderToZip = new File(folderToZipPath);
        if (folderToZip.exists() && folderToZip.isDirectory()) {
            zipDirectory(folderToZip, angularRoot, zipOut);
        }

        zipOut.close();
        return byteArrayOutputStream.toByteArray();
    }

}
