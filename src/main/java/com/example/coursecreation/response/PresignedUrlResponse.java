package com.example.coursecreation.response;

public class PresignedUrlResponse {
    private String url;

    public PresignedUrlResponse(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
