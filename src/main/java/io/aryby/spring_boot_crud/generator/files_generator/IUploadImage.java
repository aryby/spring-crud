package io.aryby.spring_boot_crud.generator.files_generator;




public interface IUploadImage {

    default StringBuilder upladFile(String PAKAGE_UTILS){

        StringBuilder builder = new StringBuilder();
        builder.append("package ").append(PAKAGE_UTILS).append(";\n")
            .append("""

                import java.io.File;
                import java.io.IOException;
                import java.nio.file.Files;
                import java.nio.file.Path;
                import java.nio.file.Paths;

                import org.springframework.web.multipart.MultipartFile;

                public class UploadMyFile {


                    // Method to save the file with a unique name
                    //private static final String UPLOAD_DIRECTORY = "uploads/";

                    public String saveFile(MultipartFile file, String UPLOAD_DIRECTORY) throws IOException {
                        // Define the upload path
                        Path uploadPath = Paths.get(System.getProperty("user.dir") + File.separator + UPLOAD_DIRECTORY);

                        // Ensure the upload directory exists
                        if (!Files.exists(uploadPath)) {
                            Files.createDirectories(uploadPath);
                        }

                        // Rename and save the uploaded file
                        String uniqueFileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

                        // Resolve the file path and save the file
                        Path filePath = uploadPath.resolve(uniqueFileName);
                        file.transferTo(filePath.toFile());

                        // Return the path for storing in the database
                        return uniqueFileName;
                    }
                }



                """);

        return builder;
    }
}
