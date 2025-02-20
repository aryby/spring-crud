package io.aryby.spring_boot_crud.generator.implimentations;

import io.aryby.spring_boot_crud.custom_table_attributes.CustomTableAttributeDTO;
import io.aryby.spring_boot_crud.generator.jpa_generator.IJpaSave;
import io.aryby.spring_boot_crud.util.MyHelpper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JpaSaveImpl implements IJpaSave {
    @Override
    public StringBuilder create(String REQUEST_MODAL,String DTO_MODAL, String ENTITY_MODAL, String REPO_DI_LOWER, List<CustomTableAttributeDTO> attributes ) {
        StringBuilder sb = new StringBuilder();

        sb.append("\n    @Transactional\n     public ");
        sb.append(DTO_MODAL);
        sb.append(" create");
        sb.append(ENTITY_MODAL);
        sb.append("(" +
            REQUEST_MODAL +
            " " +
            MyHelpper.lowerCaseFirstLetter(REQUEST_MODAL) +
            ") {\n");


        sb.append("         " + ENTITY_MODAL + " "+ MyHelpper.lowerCaseFirstLetter(ENTITY_MODAL)+" = "
            +" "+ENTITY_MODAL+".builder()\n"
        );


        for (CustomTableAttributeDTO attr : attributes) {
            sb.append("   ").
                append("                            ." +
                    MyHelpper.lowerCaseFirstLetter(attr.getNameAttribute()) +
                    "("+ MyHelpper.lowerCaseFirstLetter(REQUEST_MODAL))
                .append(".");
            if (attr.getNameTypeModifier().equalsIgnoreCase("boolean")) {
                sb.append(MyHelpper.lowerCaseFirstLetter(attr.getNameAttribute())).append("())\n");
            }else {
                sb.append("get"+ MyHelpper.capitalizeFirstLetter(attr.getNameAttribute())).append("())\n");
            }
        }
        sb.append("                           " +
                "   .build();\n\n");
       sb.append("        " +
           MyHelpper.lowerCaseFirstLetter(ENTITY_MODAL) +
           " = " +
           REPO_DI_LOWER +
           ".save(" +
           MyHelpper.lowerCaseFirstLetter(ENTITY_MODAL) +
           ");\n" +
           "        return new " +
           DTO_MODAL +
           "(");
        sb.append("\n");
        sb. append("                    "+ENTITY_MODAL.toLowerCase()).append(".getId(),\n");
        for (CustomTableAttributeDTO attr : attributes) {
            sb.append("                     ").
                append(MyHelpper.lowerCaseFirstLetter(ENTITY_MODAL))
                .append(".");

            if (attr.getNameTypeModifier().equalsIgnoreCase("boolean")) {
                sb.append(MyHelpper.lowerCaseFirstLetter(attr.getNameAttribute())).append("(),\n");
            }else {
                sb.append("get"+ MyHelpper.capitalizeFirstLetter(attr.getNameAttribute())).append("(),\n");
            }
        }
        sb. append("                     " +
            MyHelpper.lowerCaseFirstLetter(ENTITY_MODAL) +
            ".getLastUpdated()\n");
        sb.append("         );");
        sb.append("\n     }");

        return sb;
    }
}
