package io.aryby.spring_boot_crud.generator.implimentations;

import io.aryby.spring_boot_crud.custom_table.CustomTable;
import io.aryby.spring_boot_crud.custom_table.CustomTableRepository;
import io.aryby.spring_boot_crud.general_settings.GeneralSettings;
import io.aryby.spring_boot_crud.general_settings.GeneralSettingsRepository;
import io.aryby.spring_boot_crud.generator.IGenerateStaticContentsZip;
import io.aryby.spring_boot_crud.project_settings.ProjectSettingService;
import io.aryby.spring_boot_crud.project_settings.ProjectSettings;
import io.aryby.spring_boot_crud.project_settings.ProjectSettingsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static io.aryby.spring_boot_crud.util.MyHelpper.zipDirectory;

public class GenerateStaticContentsZipImpl implements IGenerateStaticContentsZip {
    private Logger logger = LoggerFactory.getLogger(ProjectSettingService.class);
    private final ProjectSettingsRepository projectSettingRepository;
    private final GeneralSettingsRepository generalSettingsRepository;
    private final CustomTableRepository customTableRepository;


    public GenerateStaticContentsZipImpl(ProjectSettingsRepository projectSettingRepository,
                                         GeneralSettingsRepository generalSettingsRepository,
                                         CustomTableRepository customTableRepository) {
        this.projectSettingRepository = projectSettingRepository;
        this.generalSettingsRepository = generalSettingsRepository;
        this.customTableRepository = customTableRepository;

    }

    @Override
    public byte[] generate(Long projectId) throws IOException {
        logger.info("generate {}", projectId);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOut = new ZipOutputStream(byteArrayOutputStream);

        ProjectSettings projectSettings = projectSettingRepository.findById(projectId).orElseThrow(() ->
            new RuntimeException("Project settings not found"));

        List<CustomTable> tables = customTableRepository.findAllByprojectSetting(projectId);

        // Create directory structure
        logger.info("projectSettings.getGeneralSettings() =  {} ", projectSettings.getGeneralSettings());
        Optional<GeneralSettings> generalSgs = generalSettingsRepository.findById(projectSettings.getGeneralSettings());


        String myFile = "src/";
        String folderToZipPath = "src/static/";


        // environments
        String myStyle = """
            h1{
            color:#fff;
            }

            """;
        ZipEntry zipEntryenvironments = new ZipEntry(myFile + "environments/environment.ts");
        zipOut.putNextEntry(zipEntryenvironments);
        zipOut.write(myStyle.getBytes());
        zipOut.closeEntry();


        File folderToZip = new File(folderToZipPath);
        if (folderToZip.exists() && folderToZip.isDirectory()) {
            zipDirectory(folderToZip, myFile, zipOut);
        }


        zipOut.close();
        return byteArrayOutputStream.toByteArray();
    }



}
