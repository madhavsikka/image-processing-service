package com.github.madhav.uploader.model;

public class FileData {

    private String filename;
    private Integer width;
    private Integer height;
    private Integer quality;

    public FileData(String filename, Integer width, Integer height, Integer quality) {
        this.filename = filename;
        this.width = width;
        this.height = height;
        this.quality = quality;
    }

    public FileData(String filename, Integer width, Integer height) {
        this.filename = filename;
        this.width = width;
        this.height = height;
        this.quality = 100;
    }

    public String getFilename() {
        return filename;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public Integer getQuality() {
        return quality;
    }

    @Override
    public String toString() {
        return "FileData{" +
                "filename='" + filename + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", quality=" + quality +
                '}';
    }
}
