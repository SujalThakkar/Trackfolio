package com.trackfolio.util;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class FileHandler {
    public static String saveCertificateFile(String username, File sourceFile) {
        try {
            String dirPath = "certificates/" + username;
            File directory = new File(dirPath);
            if (!directory.exists()) {
                directory.mkdirs();
                System.out.println("Created directory: " + dirPath);
            }

            String fileName = sourceFile.getName();
            String extension = fileName.substring(fileName.lastIndexOf("."));
            String baseName = fileName.substring(0, fileName.lastIndexOf("."));
            File destFile = new File(dirPath + "/" + fileName);
            int counter = 1;
            while (destFile.exists()) {
                destFile = new File(dirPath + "/" + baseName + "_" + counter + extension);
                counter++;
            }

            Files.copy(sourceFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File copied to: " + destFile.getAbsolutePath());
            return destFile.getAbsolutePath();
        } catch (Exception e) {
            System.err.println("Error saving certificate file: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}