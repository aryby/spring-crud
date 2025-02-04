package io.aryby.spring_boot_crud.util;

import java.util.UUID;

public class UuidGenerator {
    public static String generateUID() {
        UUID uid = UUID.randomUUID();
        return uid.toString();
    }
}
