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

    private final IMvcModule mvcModule;
    private final IMvcRoutes mvcRoutes;
    private final IMvcComponentList mvcComponentList;
    private final IMvcComponentCreate mvcComponentCreateHtml;
    private final IMvcComponentCreateTs mvcComponentCreateTs;

    public IGenerateFrontendZipImpl(ProjectSettingsRepository projectSettingRepository,
                                    GeneralSettingsRepository generalSettingsRepository,
                                    CustomTableRepository customTableRepository,
                                    IAngularAppModuleTs angularAppModule,
                                    IAngularAppComponent angularAppComponent,
                                    IAngularAppModuleRoutingTs appModuleRoutingTs, IAngularServiceGenerator angularServiceGenerator, IAngularComponentsGenerator angularComponentsGenerator, IModelGenerator modelGenerator, IMvcModule mvcModule, IMvcRoutes mvcRoutes, IMvcComponentList mvcComponentList, IMvcComponentCreate mvcComponentCreateHtml, IMvcComponentCreateTs mvcComponentCreateTs) {
        this.projectSettingRepository = projectSettingRepository;
        this.generalSettingsRepository = generalSettingsRepository;
        this.customTableRepository = customTableRepository;
        this.angularAppModule = angularAppModule;
        this.angularAppComponent = angularAppComponent;
        this.appModuleRoutingTs = appModuleRoutingTs;
        this.angularServiceGenerator = angularServiceGenerator;
        this.angularComponentsGenerator = angularComponentsGenerator;
        this.modelGenerator = modelGenerator;
        this.mvcModule = mvcModule;
        this.mvcRoutes = mvcRoutes;
        this.mvcComponentList = mvcComponentList;
        this.mvcComponentCreateHtml = mvcComponentCreateHtml;
        this.mvcComponentCreateTs = mvcComponentCreateTs;
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

        // generate app.module.ts
        String MODULL_APP = angularAppModule.generateAngularAppModuleTs(projectId);
        logger.info("generate MODULE APP");
        ZipEntry MODULE_PATH = new ZipEntry(angularSrcApp+ "app.module.ts");
        zipOut.putNextEntry(MODULE_PATH);
        zipOut.write(MODULL_APP.getBytes());
        zipOut.closeEntry();

        // generate mvc Module
        String MODUL_MVC = mvcModule.generate(projectId);
        logger.info("generate MVC MODULE APP");
        ZipEntry MODULE_PATH_MVC = new ZipEntry(angularSrcAppMvc+ "mvc.module.ts");
        zipOut.putNextEntry(MODULE_PATH_MVC);
        zipOut.write(MODUL_MVC.getBytes());
        zipOut.closeEntry();

        // generate mvc Routes
        String ROUTES_MVC = mvcRoutes.generate(projectId);
        logger.info("generate MVC ROUTES APP");
        ZipEntry MVC_ROUTS = new ZipEntry(angularSrcAppMvc+ "mvc.routes.ts");
        zipOut.putNextEntry(MVC_ROUTS);
        zipOut.write(ROUTES_MVC.getBytes());
        zipOut.closeEntry();

        // generate list ts component
        for (CustomTable table : tables) {
            String listComponentTs  = angularComponentsGenerator.generateAngularComponentsTs(table, projectId);
            ZipEntry entry = new ZipEntry(angularSrcAppMvc + "components/" +
                table.getName().toLowerCase() +"/"+ table.getName().toLowerCase() + "-list.component.ts");

            zipOut.putNextEntry(entry);
            zipOut.write(listComponentTs.getBytes());
            zipOut.closeEntry();
        }

        // generate list html component
        for (CustomTable table : tables) {
           String listComponentHtml  = mvcComponentList.generate(table, projectId);
            ZipEntry entryhtml = new ZipEntry(angularSrcAppMvc + "components/" +
                table.getName().toLowerCase() +"/"+ table.getName().toLowerCase() + "-list.component.html");

            zipOut.putNextEntry(entryhtml);
            zipOut.write(listComponentHtml.getBytes());
            zipOut.closeEntry();
        }

        // generate Add html component
        for (CustomTable table : tables) {
            String addComponentHtml  = mvcComponentCreateHtml.generate(table, projectId);
            ZipEntry entryhtml = new ZipEntry(angularSrcAppMvc + "components/" +
                table.getName().toLowerCase() +"/"+ table.getName().toLowerCase() + "-add.component.html");

            zipOut.putNextEntry(entryhtml);
            zipOut.write(addComponentHtml.getBytes());
            zipOut.closeEntry();
        }

        // generate Add ts component
        for (CustomTable table : tables) {
            String addComponentTs  = mvcComponentCreateTs.generate(table, projectId);
            ZipEntry entry = new ZipEntry(angularSrcAppMvc + "components/" +
                table.getName().toLowerCase() +"/"+ table.getName().toLowerCase() + "-add.component.ts");

            zipOut.putNextEntry(entry);
            zipOut.write(addComponentTs.getBytes());
            zipOut.closeEntry();
        }

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

        String folderToZipPath = "src/main/resources/static/";

        File folderToZip = new File(folderToZipPath);
        if (folderToZip.exists() && folderToZip.isDirectory()) {
            zipDirectory(folderToZip, angularRoot, zipOut);
        }

        zipOut.close();
        return byteArrayOutputStream.toByteArray();
    }

}
