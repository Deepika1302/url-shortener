package UrlShortenerApp.dto;

public class StatsEntry {

    private String date;
    private long clicks;
    private long creations;

    public StatsEntry(String date, long clicks, long creations) {
        this.date = date;
        this.clicks = clicks;
        this.creations = creations;
    }

    public String getDate() { return date; }
    public long getClicks() { return clicks; }
    public long getCreations() { return creations; }
}
