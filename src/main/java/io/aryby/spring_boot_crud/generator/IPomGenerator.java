package io.aryby.spring_boot_crud.generator;

import io.aryby.spring_boot_crud.project_settings.ProjectSettings;

public interface IPomGenerator {
    String generatePomXml(ProjectSettings projectSetting);
}
