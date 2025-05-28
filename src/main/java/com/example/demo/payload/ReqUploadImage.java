package com.example.demo.payload;

import org.springframework.web.multipart.MultipartFile;

public class ReqUploadImage {
    private MultipartFile file;
    private MultipartFile[] files;
    private String folder;
    private String publicId;
    private boolean useFilename;
    private boolean uniqueFilename;
    private String resourceType;

    public ReqUploadImage() {
        this.folder = "uploads";
        this.useFilename = false;
        this.uniqueFilename = true;
        this.resourceType = "image";
    }

    // Getters and Setters
    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public MultipartFile[] getFiles() {
        return files;
    }

    public void setFiles(MultipartFile[] files) {
        this.files = files;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public boolean isUseFilename() {
        return useFilename;
    }

    public void setUseFilename(boolean useFilename) {
        this.useFilename = useFilename;
    }

    public boolean isUniqueFilename() {
        return uniqueFilename;
    }

    public void setUniqueFilename(boolean uniqueFilename) {
        this.uniqueFilename = uniqueFilename;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }
}