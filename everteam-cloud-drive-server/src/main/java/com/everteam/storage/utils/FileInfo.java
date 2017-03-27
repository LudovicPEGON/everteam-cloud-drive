package com.everteam.storage.utils;

import java.io.InputStream;

public class FileInfo {
    
    private String name;
    private String description;
    private String mimeType;
    private Long size;
    private InputStream inputStream;
    
    public FileInfo(String name, String description, String mimeType, Long size, InputStream inputStream) {
        this.name = name;
        this.description = description;
        this.mimeType = mimeType;
        this.size = size;
        this.inputStream = inputStream;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getMimeType() {
        return mimeType;
    }
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
    public Long getSize() {
        return size;
    }
    public void setSize(Long size) {
        this.size = size;
    }
    public InputStream getInputStream() {
        return inputStream;
    }
    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
    
    
}
