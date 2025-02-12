package io.aryby.spring_boot_crud.generator.files_generator.impl;

import io.aryby.spring_boot_crud.generator.files_generator.IUploadImage;
import org.springframework.stereotype.Service;

import java.io.IOException;
@Service
public class IUploadImageImpl implements IUploadImage {
    @Override
    public StringBuilder upladFile(String PAKAGE_UTILS) {
        return IUploadImage.super.upladFile(PAKAGE_UTILS);
    }
}
