package io.aryby.spring_boot_crud.generator.implimentations;

import io.aryby.spring_boot_crud.generator.jpa_generator.IJpaDelete;
import org.springframework.stereotype.Service;

@Service
public class JpaDeleteImpl implements IJpaDelete {

    @Override
    public StringBuilder deleteById(String REPO_DI_LOWER) {
        StringBuilder sb = new StringBuilder();

        sb.append("\n    @Transactional\n    public void deleteById(Long id) {\n");
        sb.append("        this." + REPO_DI_LOWER + ".deleteById(id);\n");
        sb.append("    }\n");
        return sb;
    }
}
