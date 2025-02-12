package io.aryby.spring_boot_crud.generator.date_defaults_generator;

import org.springframework.stereotype.Service;

@Service
public class IDateTimeGeneratorImpl implements IDateTimeGenerator {
    @Override
    public StringBuilder generateNowByDefault(String PAKAGE_MAIN) {
        return IDateTimeGenerator.super.generateNowByDefault(PAKAGE_MAIN);
    }
}
