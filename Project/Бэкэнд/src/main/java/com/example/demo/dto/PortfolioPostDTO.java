package com.example.demo.dto;

public class PortfolioPostDTO {
    String proffesion;
    String description;
    byte[] photos;

    public PortfolioPostDTO(String proffesion, String description, byte[] photos) {
        super();
        this.proffesion = proffesion;
        this.description = description;
        this.photos = photos;
    }

    public String getProffesion() {
        return proffesion;
    }

    public void setProffesion(String proffesion) {
        this.proffesion = proffesion;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getPhotos() {
        return photos;
    }

    public void setPhotos(byte[] photos) {
        this.photos = photos;
    }
}
