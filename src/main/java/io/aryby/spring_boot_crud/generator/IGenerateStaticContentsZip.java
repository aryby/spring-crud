package io.aryby.spring_boot_crud.generator;

import java.io.IOException;

public interface IGenerateStaticContentsZip {
    public byte[] generate(Long projectId) throws IOException;
}
