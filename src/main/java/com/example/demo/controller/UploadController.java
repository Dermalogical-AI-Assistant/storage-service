package com.example.demo.controller;

import com.example.demo.payload.ReqUploadImage;
import com.example.demo.payload.ResUploadImage;
import com.example.demo.service.UploadImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
@CrossOrigin(origins = "*")
public class UploadController {

    @Autowired
    private UploadImageService uploadImageService;

    @PostMapping("/image")
    public ResponseEntity<ResUploadImage> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            ReqUploadImage request = new ReqUploadImage();
            request.setFile(file);

            ResUploadImage response = uploadImageService.uploadImage(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResUploadImage errorResponse = new ResUploadImage();
            errorResponse.setSuccess(false);
            errorResponse.setMessage("Upload failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/multiple")
    public ResponseEntity<ResUploadImage> uploadMultipleImages(@RequestParam("files") MultipartFile[] files) {
        try {
            ReqUploadImage request = new ReqUploadImage();
            request.setFiles(files);

            ResUploadImage response = uploadImageService.uploadMultipleImages(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResUploadImage errorResponse = new ResUploadImage();
            errorResponse.setSuccess(false);
            errorResponse.setMessage("Upload failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @DeleteMapping("/image/{publicId}")
    public ResponseEntity<ResUploadImage> deleteImage(@PathVariable String publicId) {
        try {
            ResUploadImage response = uploadImageService.deleteImage(publicId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResUploadImage errorResponse = new ResUploadImage();
            errorResponse.setSuccess(false);
            errorResponse.setMessage("Delete failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}