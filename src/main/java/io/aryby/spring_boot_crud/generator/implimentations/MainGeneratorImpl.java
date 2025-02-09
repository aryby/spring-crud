package io.aryby.spring_boot_crud.generator.implimentations;

import io.aryby.spring_boot_crud.general_settings.GeneralSettings;
import io.aryby.spring_boot_crud.general_settings.GeneralSettingsRepository;
import io.aryby.spring_boot_crud.generator.IMainGenerator;
import io.aryby.spring_boot_crud.project_settings.ProjectSettings;
import io.aryby.spring_boot_crud.project_settings.ProjectSettingsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MainGeneratorImpl implements IMainGenerator {

    private final ProjectSettingsRepository projectSettingRepository;
    private final GeneralSettingsRepository generalSettingsRepository;
    private final Logger logger = LoggerFactory.getLogger(MainGeneratorImpl.class);
    MainGeneratorImpl(ProjectSettingsRepository projectSettingRepository,
                      GeneralSettingsRepository generalSettingsRepository){
        this.projectSettingRepository = projectSettingRepository;
        this.generalSettingsRepository = generalSettingsRepository;
    }

    @Override
    public String generateSpringBootMain(Long projectId) {
        logger.info("Generate Spring Boot Main Class file {}", projectId);
        ProjectSettings projectSetting = projectSettingRepository.findById(projectId)
            .orElseThrow(() -> new IllegalArgumentException("ProjectSettings not found for ID: " + projectId));
        GeneralSettings generalSettings = generalSettingsRepository.findById(projectSetting.getGeneralSettings())
            .orElseThrow(() -> new IllegalArgumentException("GeneralSettings not found for ID: " + projectId));

        StringBuilder sb = new StringBuilder();

        sb.append("package " + generalSettings.getGroupId() + "." + generalSettings.getArtifactId() + ";\n\n");


        sb.append("""
            import org.springframework.boot.SpringApplication;
            import org.springframework.boot.autoconfigure.SpringBootApplication;


            @SpringBootApplication
            public class StarterApp {

                    public static void main(final String[] args) {
                           SpringApplication.run(StarterApp.class, args);
                    }

            }
            """);

        return sb.toString();
    }
}
