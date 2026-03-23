package UrlShortenerApp.dto;

import UrlShortenerApp.entity.Url;

public class UrlResponse {

    private Long id;
    private String originalUrl;
    private String shortCode;
    private String shortUrl;
    private String createdAt;
    private int clickCount;
    private String lastAccessedAt;

    public static UrlResponse from(Url url, String baseUrl) {
        UrlResponse r = new UrlResponse();
        r.id = url.getId();
        r.originalUrl = url.getOriginalUrl();
        r.shortCode = url.getShortCode();
        r.shortUrl = baseUrl + "/" + url.getShortCode();
        r.createdAt = url.getCreatedAt() != null ? url.getCreatedAt().toString() : null;
        r.clickCount = url.getClickCount();
        r.lastAccessedAt = url.getLastAccessedAt() != null
                ? url.getLastAccessedAt().toString() : null;
        return r;
    }

    public Long getId() { return id; }
    public String getOriginalUrl() { return originalUrl; }
    public String getShortCode() { return shortCode; }
    public String getShortUrl() { return shortUrl; }
    public String getCreatedAt() { return createdAt; }
    public int getClickCount() { return clickCount; }
    public String getLastAccessedAt() { return lastAccessedAt; }
}
