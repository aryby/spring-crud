package io.aryby.spring_boot_crud.project_settings;

import io.aryby.spring_boot_crud.custom_table.CustomTable;
import io.aryby.spring_boot_crud.custom_table.CustomTableRepository;
import io.aryby.spring_boot_crud.custom_table_attributes.CustomTableAttributeService;
import io.aryby.spring_boot_crud.database_settings.DatabaseSettings;
import io.aryby.spring_boot_crud.database_settings.DatabaseSettingsRepository;
import io.aryby.spring_boot_crud.developer_preferences.DeveloperPreferences;
import io.aryby.spring_boot_crud.developer_preferences.DeveloperPreferencesRepository;
import io.aryby.spring_boot_crud.general_settings.GeneralSettings;
import io.aryby.spring_boot_crud.general_settings.GeneralSettingsRepository;
import io.aryby.spring_boot_crud.generator.*;
import io.aryby.spring_boot_crud.util.CapitalizeFirstChar;
import io.aryby.spring_boot_crud.util.NotFoundException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import io.aryby.spring_boot_crud.util.UuidGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ProjectSettingService {

    private Logger logger = LoggerFactory.getLogger(ProjectSettingService.class);
    private final ProjectSettingsRepository projectSettingRepository;
    private final GeneralSettingsRepository generalSettingsRepository;
    private final DatabaseSettingsRepository databaseSettingsRepository;
    private final DeveloperPreferencesRepository developerPreferencesRepository;
    private final CustomTableRepository customTableRepository;
    private final CustomTableAttributeService customTableAttributeService;
    private final IEntityGenerator entityGenerator;
    private final IRepositoryGenerator interfaceGenerator;
    private final IControllerGenerator controllerGenerator;
    private final IMainGenerator mainGenerator;
    private final IPomGenerator generatePomXml;

    public ProjectSettingService(final ProjectSettingsRepository projectSettingRepository,
                                 final GeneralSettingsRepository generalSettingsRepository,
                                 final DatabaseSettingsRepository databaseSettingsRepository,
                                 final DeveloperPreferencesRepository developerPreferencesRepository,
                                 final CustomTableRepository customTableRepository,
                                 final CustomTableAttributeService customTableAttributeService,
                                 final IRepositoryGenerator interfaceGenerator,
                                 final IControllerGenerator controllerGenerator,
                                 final IEntityGenerator entityGenerator, IMainGenerator mainGenerator, IPomGenerator generatePomXml) {
        this.projectSettingRepository = projectSettingRepository;
        this.controllerGenerator = controllerGenerator;
        this.customTableAttributeService = customTableAttributeService;
        this.generalSettingsRepository = generalSettingsRepository;
        this.interfaceGenerator = interfaceGenerator;
        this.databaseSettingsRepository = databaseSettingsRepository;
        this.developerPreferencesRepository = developerPreferencesRepository;
        this.customTableRepository = customTableRepository;
        this.entityGenerator = entityGenerator;
        this.mainGenerator = mainGenerator;
        this.generatePomXml = generatePomXml;
    }

    public List<ProjectSettingsDTO> findAll() {
        final List<ProjectSettings> projectSettinges = projectSettingRepository.findAll(Sort.by("id"));
        return projectSettinges.stream()
            .map(projectSettings -> mapToDTO(projectSettings, new ProjectSettingsDTO()))
            .toList();
    }

    public ProjectSettingsDTO getBySlug(final String slug) {
        logger.info("getBySlug {}", slug);
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

        String appColnt = mainGenerator.generateSpringBootMain(projectId); // main class spring boot
        ZipEntry zipEntry1 = new ZipEntry(srcMainJava + "StarterApp.java");
        zipOut.putNextEntry(zipEntry1);
        zipOut.write(appColnt.getBytes());
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
            ZipEntry zipEntry = new ZipEntry(srcMainJava + "repositories/" + CapitalizeFirstChar.capitalizeFirstLetter(table.getName()) + "Repository.java");
            zipOut.putNextEntry(zipEntry);
            zipOut.write(classContent.getBytes());
            zipOut.closeEntry();
        }
        // Add Controller class files
        for (CustomTable table : tables) {
            String classContent = controllerGenerator.generate(table, projectId);
            ZipEntry zipEntry = new ZipEntry(srcMainJava + "controllers/" + CapitalizeFirstChar.capitalizeFirstLetter(table.getName()) + "Controller.java");
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
