package io.aryby.spring_boot_crud.generator.implimentations;


import io.aryby.spring_boot_crud.custom_table_attributes.CustomTableAttributeDTO;
import io.aryby.spring_boot_crud.generator.jpa_generator.IJpaUpdate;
import io.aryby.spring_boot_crud.util.CapitalizeFirstChar;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class JpaUpdateImpl implements IJpaUpdate {

    @Override
    public StringBuilder update(String REQUEST_MODAL, String DTO_MODAL, String ENTITY_MODAL, String REPO_DI_LOWER, List<CustomTableAttributeDTO> attributes) {
        StringBuilder sb = new StringBuilder();

        sb.append("\n    @Transactional\n    public ");
        sb.append(DTO_MODAL);
        sb.append(" update");
        sb.append(ENTITY_MODAL);
        sb.append("(Long id, " + REQUEST_MODAL + " " + CapitalizeFirstChar.lowerCaseFirstLetter(REQUEST_MODAL) + ") {\n");

        sb.append("        " + ENTITY_MODAL + " " + CapitalizeFirstChar.lowerCaseFirstLetter(ENTITY_MODAL) + " = ")
            .append(REPO_DI_LOWER + ".findById(id).orElseThrow(() -> new RuntimeException(\"Entity not found\"));\n\n");

        for (CustomTableAttributeDTO attr : attributes) {
            sb.append("        " + CapitalizeFirstChar.lowerCaseFirstLetter(ENTITY_MODAL) + ".");
            if (attr.getNameTypeModifier().equalsIgnoreCase("boolean")) {
                sb.append("set" +CapitalizeFirstChar.capitalizeFirstLetter(attr.getNameAttribute().substring(2))).append("(");
            } else {
                sb.append("set" + CapitalizeFirstChar.capitalizeFirstLetter(attr.getNameAttribute())).append("(");
            }
            sb.append(CapitalizeFirstChar.lowerCaseFirstLetter(REQUEST_MODAL)).append(".");
            if (attr.getNameTypeModifier().equalsIgnoreCase("boolean")) {
                sb.append(CapitalizeFirstChar.lowerCaseFirstLetter(attr.getNameAttribute())).append("());\n");
            } else {
                sb.append("get" + CapitalizeFirstChar.capitalizeFirstLetter(attr.getNameAttribute())).append("());\n");
            }
        }

        sb.append("\n        " + CapitalizeFirstChar.lowerCaseFirstLetter(ENTITY_MODAL) + " = " + REPO_DI_LOWER + ".save(" + CapitalizeFirstChar.lowerCaseFirstLetter(ENTITY_MODAL) + ");\n");
        sb.append("        return new " + DTO_MODAL + "(\n");
        sb.append("            " + CapitalizeFirstChar.lowerCaseFirstLetter(ENTITY_MODAL) + ".getId(),\n");

        for (CustomTableAttributeDTO attr : attributes) {
            sb.append("            " + CapitalizeFirstChar.lowerCaseFirstLetter(ENTITY_MODAL) + ".");
            if (attr.getNameTypeModifier().equalsIgnoreCase("boolean")) {
                sb.append(CapitalizeFirstChar.lowerCaseFirstLetter(attr.getNameAttribute())).append("(),\n");
            } else {
                sb.append("get" + CapitalizeFirstChar.capitalizeFirstLetter(attr.getNameAttribute())).append("(),\n");
            }
        }

        sb.append("            " + CapitalizeFirstChar.lowerCaseFirstLetter(ENTITY_MODAL) + ".getLastUpdated()\n");
        sb.append("        );\n    }\n");

        return sb;
    }
}
