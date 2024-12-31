package com.dev.memberblog.files.upload;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Service
public class FilesStorageServiceImpl implements FilesStorageService{
    @Value("${path.upload.file}")
    private String folderRoot;

    private Path root;

    @Override
    public String uploadImage(String base64Image) {
        try {
            String[] base64Parts = base64Image.split(",");
            String base64Data = base64Parts[1];
            byte[] imageBytes = Base64.getDecoder().decode(base64Data);

            root = Path.of(folderRoot);
            if(!Files.exists(root)){
                Files.createDirectories(root);
            }
            String fileName = getFileName(base64Image);
            Files.copy(new ByteArrayInputStream(imageBytes),root.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean save(MultipartFile file) {
        try {
            root = Path.of(folderRoot);
            if(!Files.exists(root)){
                Files.createDirectories(root);
            }
            Files.copy(file.getInputStream(),root.resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
            return true;
        }catch (Exception e){
            System.out.println("Error" + e.getLocalizedMessage());
        }
        return false;
    }



    @Override
    public Resource load(String filename) {
        try {
            root = Path.of(folderRoot);
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                return null;
            }
        } catch (MalformedURLException e) {
            return null;
        }
    }

    private static String getFileName(String base64Data){
        String extension = getExtensionFromBase64(base64Data);

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "image_" + timeStamp + "." + extension;

        return fileName;
    }
    private static String getExtensionFromBase64(String base64Data) {
        String[] parts = base64Data.split(";");
        String mimeType = parts[0].split(":")[1];
        String extension = mimeType.split("/")[1];
        return extension;
    }
    @Override
    public boolean delete(String filename) {
        try {
            root = Path.of(folderRoot);
            Path fileToDelete = root.resolve(filename);

            if (Files.exists(fileToDelete)) {
                Files.delete(fileToDelete);
                return true;
            } else{
                System.out.println("delete file failure");
                return false;
            }

        } catch (Exception e) {
            System.out.println("Error deleting file: " + e.getLocalizedMessage());
            return false;
        }
    }
}
