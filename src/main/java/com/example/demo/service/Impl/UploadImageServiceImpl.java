package com.example.demo.service.Impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.example.demo.payload.ReqUploadImage;
import com.example.demo.payload.ResUploadImage;
import com.example.demo.service.UploadImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UploadImageServiceImpl implements UploadImageService {

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public ResUploadImage uploadImage(ReqUploadImage request) throws Exception {
        MultipartFile file = request.getFile();

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is required");
        }

        // Validate file type
        if (!isValidImageFile(file)) {
            throw new IllegalArgumentException("Only image files are allowed");
        }

        // Validate file size (max 10MB)
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new IllegalArgumentException("File size must be less than 10MB");
        }

        try {
            Map<String, Object> uploadParams = ObjectUtils.asMap(
                    "folder", request.getFolder(),
                    "use_filename", request.isUseFilename(),
                    "unique_filename", request.isUniqueFilename(),
                    "resource_type", "auto",
                    "quality", "auto",
                    "fetch_format", "auto"
            );

            if (request.getPublicId() != null && !request.getPublicId().isEmpty()) {
                uploadParams.put("public_id", request.getPublicId());
            }

            Map<String, Object> uploadResult = cloudinary.uploader()
                    .upload(file.getBytes(), uploadParams);

            return mapToResponse(uploadResult, file.getOriginalFilename());

        } catch (IOException e) {
            throw new Exception("Failed to upload image to Cloudinary: " + e.getMessage());
        }
    }

    @Override
    public ResUploadImage uploadMultipleImages(ReqUploadImage request) throws Exception {
        MultipartFile[] files = request.getFiles();

        if (files == null || files.length == 0) {
            throw new IllegalArgumentException("Files are required");
        }

        List<ResUploadImage.ImageInfo> imageInfos = new ArrayList<>();

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                // Validate each file
                if (!isValidImageFile(file)) {
                    throw new IllegalArgumentException("Only image files are allowed: " + file.getOriginalFilename());
                }

                if (file.getSize() > 10 * 1024 * 1024) {
                    throw new IllegalArgumentException("File size must be less than 10MB: " + file.getOriginalFilename());
                }

                try {
                    Map<String, Object> uploadParams = ObjectUtils.asMap(
                            "folder", request.getFolder(),
                            "use_filename", request.isUseFilename(),
                            "unique_filename", request.isUniqueFilename(),
                            "resource_type", "auto",
                            "quality", "auto",
                            "fetch_format", "auto"
                    );

                    Map<String, Object> uploadResult = cloudinary.uploader()
                            .upload(file.getBytes(), uploadParams);

                    ResUploadImage.ImageInfo imageInfo = new ResUploadImage.ImageInfo();
                    imageInfo.setPublicId((String) uploadResult.get("public_id"));
                    imageInfo.setUrl((String) uploadResult.get("url"));
                    imageInfo.setSecureUrl((String) uploadResult.get("secure_url"));
                    imageInfo.setOriginalFilename(file.getOriginalFilename());
                    imageInfo.setBytes(((Number) uploadResult.get("bytes")).longValue());
                    imageInfo.setWidth((Integer) uploadResult.get("width"));
                    imageInfo.setHeight((Integer) uploadResult.get("height"));

                    imageInfos.add(imageInfo);

                } catch (IOException e) {
                    throw new Exception("Failed to upload image " + file.getOriginalFilename() + ": " + e.getMessage());
                }
            }
        }

        ResUploadImage response = new ResUploadImage();
        response.setImages(imageInfos);
        response.setMessage("Successfully uploaded " + imageInfos.size() + " images");

        return response;
    }

    @Override
    public ResUploadImage deleteImage(String publicId) throws Exception {
        if (publicId == null || publicId.isEmpty()) {
            throw new IllegalArgumentException("Public ID is required");
        }

        try {
            Map<String, Object> deleteResult = cloudinary.uploader()
                    .destroy(publicId, ObjectUtils.emptyMap());

            ResUploadImage response = new ResUploadImage();
            String result = (String) deleteResult.get("result");

            if ("ok".equals(result)) {
                response.setMessage("Image deleted successfully");
                response.setPublicId(publicId);
            } else {
                response.setSuccess(false);
                response.setMessage("Failed to delete image: " + result);
            }

            return response;

        } catch (IOException e) {
            throw new Exception("Failed to delete image from Cloudinary: " + e.getMessage());
        }
    }

    @Override
    public String getImageUrl(String publicId) {
        return cloudinary.url().generate(publicId);
    }

    @Override
    public String getTransformedImageUrl(String publicId, int width, int height) {
        return cloudinary.url()
                .transformation(new Transformation()
                        .width(width).height(height).crop("fill"))
                .generate(publicId);
    }

    private boolean isValidImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && (
                contentType.equals("image/jpeg") ||
                        contentType.equals("image/jpg") ||
                        contentType.equals("image/png") ||
                        contentType.equals("image/gif") ||
                        contentType.equals("image/webp")
        );
    }

    private ResUploadImage mapToResponse(Map<String, Object> uploadResult, String originalFilename) {
        ResUploadImage response = new ResUploadImage();
        response.setPublicId((String) uploadResult.get("public_id"));
        response.setUrl((String) uploadResult.get("url"));
        response.setSecureUrl((String) uploadResult.get("secure_url"));
        response.setFormat((String) uploadResult.get("format"));
        response.setResourceType((String) uploadResult.get("resource_type"));
        response.setBytes(((Number) uploadResult.get("bytes")).longValue());
        response.setWidth((Integer) uploadResult.get("width"));
        response.setHeight((Integer) uploadResult.get("height"));
        response.setFolder((String) uploadResult.get("folder"));
        response.setOriginalFilename(originalFilename);
        return response;
    }
}