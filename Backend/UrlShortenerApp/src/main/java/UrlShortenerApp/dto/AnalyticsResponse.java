package UrlShortenerApp.dto;

import java.util.List;

public class AnalyticsResponse {

    private Long id;
    private String originalUrl;
    private String shortCode;
    private String shortUrl;
    private String createdAt;
    private int clickCount;
    private String lastAccessedAt;
    private List<ClickRecord> clicks;

    public static class ClickRecord {
        private Long id;
        private String clickedAt;

        public ClickRecord(Long id, String clickedAt) {
            this.id = id;
            this.clickedAt = clickedAt;
        }

        public Long getId() { return id; }
        public String getClickedAt() { return clickedAt; }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOriginalUrl() { return originalUrl; }
    public void setOriginalUrl(String originalUrl) { this.originalUrl = originalUrl; }
    public String getShortCode() { return shortCode; }
    public void setShortCode(String shortCode) { this.shortCode = shortCode; }
    public String getShortUrl() { return shortUrl; }
    public void setShortUrl(String shortUrl) { this.shortUrl = shortUrl; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public int getClickCount() { return clickCount; }
    public void setClickCount(int clickCount) { this.clickCount = clickCount; }
    public String getLastAccessedAt() { return lastAccessedAt; }
    public void setLastAccessedAt(String lastAccessedAt) { this.lastAccessedAt = lastAccessedAt; }
    public List<ClickRecord> getClicks() { return clicks; }
    public void setClicks(List<ClickRecord> clicks) { this.clicks = clicks; }
}
