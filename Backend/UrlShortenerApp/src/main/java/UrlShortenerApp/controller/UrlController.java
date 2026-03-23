package UrlShortenerApp.controller;

import UrlShortenerApp.dto.*;
import UrlShortenerApp.entity.ClickEvent;
import UrlShortenerApp.entity.Url;
import UrlShortenerApp.service.IUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class UrlController {

    @Autowired
    private IUrlService urlService;

    @PostMapping("/urls")
    public ResponseEntity<UrlResponse> create(@RequestBody CreateUrlRequest request) {
        Url url = urlService.createShortUrl(request.getOriginalUrl());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UrlResponse.from(url, getBaseUrl()));
    }

    @GetMapping("/urls")
    public UrlsPageResponse getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(name = "per_page", defaultValue = "10") int perPage) {

        Page<Url> result = urlService.getAll(page, perPage);
        String baseUrl = getBaseUrl();

        List<UrlResponse> urls = result.getContent().stream()
                .map(u -> UrlResponse.from(u, baseUrl))
                .collect(Collectors.toList());

        return new UrlsPageResponse(
                urls,
                result.getNumber() + 1,
                result.getTotalPages(),
                result.getTotalElements()
        );
    }

    @GetMapping("/urls/{id}/analytics")
    public AnalyticsResponse getAnalytics(@PathVariable Long id) {
        Url url = urlService.getById(id);
        List<ClickEvent> clicks = urlService.getClicksByUrlId(id);

        String baseUrl = getBaseUrl();
        AnalyticsResponse response = new AnalyticsResponse();
        response.setId(url.getId());
        response.setOriginalUrl(url.getOriginalUrl());
        response.setShortCode(url.getShortCode());
        response.setShortUrl(baseUrl + "/" + url.getShortCode());
        response.setCreatedAt(url.getCreatedAt() != null ? url.getCreatedAt().toString() : null);
        response.setClickCount(url.getClickCount());
        response.setLastAccessedAt(
                url.getLastAccessedAt() != null ? url.getLastAccessedAt().toString() : null
        );
        response.setClicks(
                clicks.stream()
                        .map(c -> new AnalyticsResponse.ClickRecord(
                                c.getId(),
                                c.getClickedAt().toString()
                        ))
                        .collect(Collectors.toList())
        );
        return response;
    }

    @GetMapping("/stats")
    public List<StatsEntry> getStats() {
        return urlService.getStats();
    }

    private String getBaseUrl() {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .build().toUriString();
    }
}
