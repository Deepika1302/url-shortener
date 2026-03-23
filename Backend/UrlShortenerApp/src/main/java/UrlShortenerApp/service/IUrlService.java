package UrlShortenerApp.service;

import UrlShortenerApp.dto.StatsEntry;
import UrlShortenerApp.entity.ClickEvent;
import UrlShortenerApp.entity.Url;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IUrlService {

    Url createShortUrl(String originalUrl);

    Page<Url> getAll(int page, int perPage);

    Url getById(Long id);

    Url resolveAndTrackClick(String shortCode);

    List<ClickEvent> getClicksByUrlId(Long urlId);

    List<StatsEntry> getStats();
}
