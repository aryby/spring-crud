package io.aryby.spring_boot_crud.generator.implimentations;

import io.aryby.spring_boot_crud.custom_table_attributes.CustomTableAttributeDTO;
import io.aryby.spring_boot_crud.generator.jpa_generator.IJpaGetById;
import io.aryby.spring_boot_crud.util.MyHelpper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JpaGetByIdImpl implements IJpaGetById {
    @Override
    public StringBuilder getById(String DTO_MODAL, String ENTITY_MODAL, String REPO_DI_LOWER, List<CustomTableAttributeDTO> attributes ) {
        StringBuilder sb = new StringBuilder();

        sb.append("    public ");
        sb.append(DTO_MODAL);
        sb.append(" getById");
        sb.append(ENTITY_MODAL);
        sb.append("(Long id) {\n");
        sb.append("         return  this.");
        sb.append(REPO_DI_LOWER);
        sb.append(".findById(id).map(");
        sb.append(MyHelpper.lowerCaseFirstLetter(ENTITY_MODAL));
        sb.append("-> new ");
        sb.append(DTO_MODAL);
        sb.append("(\n");

        sb. append("                   "+ENTITY_MODAL.toLowerCase()).append(".getId(),\n");
        for (CustomTableAttributeDTO attr : attributes) {
            sb.append("   ").
                append("                "+ENTITY_MODAL.toLowerCase()).append(".");
            if (attr.getNameTypeModifier().equalsIgnoreCase("boolean")) {
                sb.append(MyHelpper.lowerCaseFirstLetter(attr.getNameAttribute())).append("(),\n");
            }else {
                sb.append("get"+ MyHelpper.capitalizeFirstLetter(attr.getNameAttribute())).append("(),\n");
            }
        }
        sb. append("                   "+ENTITY_MODAL.toLowerCase()).append(".getLastUpdated())\n");
        sb.append("               ).orElseThrow(() -> new RuntimeException(" +
            "\" " +
            ENTITY_MODAL +
            " not found\"));\n" +
            "     }\n ");

        return sb;
    }
}
