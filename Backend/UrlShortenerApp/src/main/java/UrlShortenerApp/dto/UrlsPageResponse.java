package UrlShortenerApp.dto;

import java.util.List;

public class UrlsPageResponse {

    private List<UrlResponse> urls;
    private int page;
    private int totalPages;
    private long totalItems;

    public UrlsPageResponse(List<UrlResponse> urls, int page, int totalPages, long totalItems) {
        this.urls = urls;
        this.page = page;
        this.totalPages = totalPages;
        this.totalItems = totalItems;
    }

    public List<UrlResponse> getUrls() { return urls; }
    public int getPage() { return page; }
    public int getTotalPages() { return totalPages; }
    public long getTotalItems() { return totalItems; }
}
