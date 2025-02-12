package io.aryby.spring_boot_crud.generator;

import java.io.IOException;

public interface IGenerateZip {
    public byte[] generateZip(Long projectId) throws IOException;
}
