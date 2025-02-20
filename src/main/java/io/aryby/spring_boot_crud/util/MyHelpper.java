package io.aryby.spring_boot_crud.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class MyHelpper {
    public static String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        char firstChar = Character.toUpperCase(str.charAt(0));
        return firstChar + str.substring(1);
    }


    public static String lowerCaseFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        char firstChar = Character.toLowerCase(str.charAt(0));
        return firstChar + str.substring(1);
    }

    // Helper method to zip an entire directory
    public static void zipDirectory(File folder, String parentFolder, ZipOutputStream zipOut) throws IOException {
        for (File file : folder.listFiles()) {
            String zipEntryName = parentFolder + file.getName();
            if (file.isDirectory()) {
                zipDirectory(file, zipEntryName + "/", zipOut);
            } else {
                FileInputStream fis = new FileInputStream(file);
                ZipEntry zipEntry = new ZipEntry(zipEntryName);
                zipOut.putNextEntry(zipEntry);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) >= 0) {
                    zipOut.write(buffer, 0, length);
                }

                zipOut.closeEntry();
                fis.close();
            }
        }
    }
}
