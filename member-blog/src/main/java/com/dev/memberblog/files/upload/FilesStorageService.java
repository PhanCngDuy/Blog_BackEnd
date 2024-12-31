package com.dev.memberblog.files.upload;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FilesStorageService {

     String uploadImage(String image);
     boolean save(MultipartFile file);
     boolean delete(String filename);
     Resource load(String filename);


}
