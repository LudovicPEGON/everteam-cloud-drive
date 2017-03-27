package com.everteam.storage.common;

public class FileMetadata {
    
    private String name;
    private String description;
    private String mimeType;
    private Long size;
    
    
    public FileMetadata(String name, String description, String mimeType, Long size) {
        this.name = name;
        this.description = description;
        this.mimeType = mimeType;
        this.size = size;
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
    
    
}
