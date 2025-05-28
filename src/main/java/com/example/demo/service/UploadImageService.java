package com.example.demo.service;

import com.example.demo.payload.ReqUploadImage;
import com.example.demo.payload.ResUploadImage;

public interface UploadImageService {
    ResUploadImage uploadImage(ReqUploadImage request) throws Exception;
    ResUploadImage uploadMultipleImages(ReqUploadImage request) throws Exception;
    ResUploadImage deleteImage(String publicId) throws Exception;
    String getImageUrl(String publicId);
    String getTransformedImageUrl(String publicId, int width, int height);
}