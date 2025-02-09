package io.aryby.spring_boot_crud.generator.implimentations;

import io.aryby.spring_boot_crud.custom_table_attributes.CustomTableAttributeDTO;
import io.aryby.spring_boot_crud.generator.jpa_generator.IJpaGetAll;
import io.aryby.spring_boot_crud.util.CapitalizeFirstChar;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JpaGetAllImpl implements IJpaGetAll {
    @Override
    public StringBuilder getAll(String DTO_MODAL, String ENTITY_MODAL, String REPO_DI_LOWER,List<CustomTableAttributeDTO> attributes ) {
        StringBuilder sb = new StringBuilder();
        // get all
        sb.append("    public List<");
        sb.append(DTO_MODAL);
        sb.append("> getAll");
        sb.append(ENTITY_MODAL);
        sb.append("s() {\n");
        sb.append("         return  this.");
        sb.append(REPO_DI_LOWER);
        sb.append(".findAll().stream().map(");
        sb.append(CapitalizeFirstChar.lowerCaseFirstLetter(ENTITY_MODAL));
        sb.append("-> new ");
        sb.append(DTO_MODAL);
        sb.append("(\n");

        sb. append("                   "+ENTITY_MODAL.toLowerCase()).append(".getId(),\n");
        for (CustomTableAttributeDTO attr : attributes) {
            sb.append("   ").
                append("                "+ENTITY_MODAL.toLowerCase()).append(".");
            if (attr.getNameTypeModifier().equalsIgnoreCase("boolean")) {
                sb.append(CapitalizeFirstChar.lowerCaseFirstLetter(attr.getNameAttribute())).append("(),\n");
            }else {
                sb.append("get"+CapitalizeFirstChar.capitalizeFirstLetter(attr.getNameAttribute())).append("(),\n");
            }
        }
        sb. append("                   "+ENTITY_MODAL.toLowerCase()).append(".getLastUpdated())");
        sb.append(").collect(Collectors.toList());\n" +
            "     }\n ");

        return sb;
    }
}
