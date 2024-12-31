package com.dev.memberblog.files.upload;

import com.dev.memberblog.common.helper.ResponseHelper;
import com.dev.memberblog.post.dto.PostDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/uploads")
public class FilesController {

    @Autowired
    private FilesStorageService service;

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = service.load(filename);
        if(file == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"")
                .contentType(MediaType.IMAGE_JPEG)
                .body(file);
    }

    @PostMapping("/uploads/images")
    public Object uploadImage(@RequestBody String image){
        return  ResponseHelper.getResponse(service.uploadImage(image), HttpStatus.OK);
    }
}
