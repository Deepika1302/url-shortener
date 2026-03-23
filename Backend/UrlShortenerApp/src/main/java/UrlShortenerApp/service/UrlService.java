package UrlShortenerApp.service;

import UrlShortenerApp.dto.StatsEntry;
import UrlShortenerApp.entity.ClickEvent;
import UrlShortenerApp.entity.Url;
import UrlShortenerApp.repository.IClickEventRepository;
import UrlShortenerApp.repository.IUrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class UrlService implements IUrlService {

    @Autowired
    private IUrlRepository urlRepository;

    @Autowired
    private IClickEventRepository clickEventRepository;

    @Override
    public Url createShortUrl(String originalUrl) {
        if (originalUrl == null || originalUrl.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "originalUrl is required");
        }

        String trimmed = originalUrl.trim();
        if (!trimmed.startsWith("http://") && !trimmed.startsWith("https://")) {
            trimmed = "http://" + trimmed;
        }

        Url url = new Url(trimmed, generateShortCode());
        return urlRepository.save(url);
    }

    @Override
    public Page<Url> getAll(int page, int perPage) {
        return urlRepository.findAllByOrderByCreatedAtDesc(
                PageRequest.of(page - 1, perPage)
        );
    }

    @Override
    public Url getById(Long id) {
        return urlRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "URL not found"));
    }

    @Override
    public Url resolveAndTrackClick(String shortCode) {
        Url url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Short URL not found"));

        url.setClickCount(url.getClickCount() + 1);
        url.setLastAccessedAt(LocalDateTime.now());
        urlRepository.save(url);

        ClickEvent click = new ClickEvent(url.getId(), LocalDateTime.now());
        clickEventRepository.save(click);

        return url;
    }

    @Override
    public List<ClickEvent> getClicksByUrlId(Long urlId) {
        return clickEventRepository.findByUrlIdOrderByClickedAtDesc(urlId);
    }

    @Override
    public List<StatsEntry> getStats() {
        List<Object[]> clickRows = clickEventRepository.countClicksByDate();
        List<Object[]> creationRows = urlRepository.countCreationsByDate();

        Map<String, long[]> map = new LinkedHashMap<>();

        for (Object[] row : clickRows) {
            String date = row[0].toString();
            long count = ((Number) row[1]).longValue();
            map.computeIfAbsent(date, k -> new long[2])[0] = count;
        }
        for (Object[] row : creationRows) {
            String date = row[0].toString();
            long count = ((Number) row[1]).longValue();
            map.computeIfAbsent(date, k -> new long[2])[1] = count;
        }

        List<String> sortedDates = new ArrayList<>(map.keySet());
        Collections.sort(sortedDates);

        List<StatsEntry> result = new ArrayList<>();
        for (String date : sortedDates) {
            long[] counts = map.get(date);
            result.add(new StatsEntry(date, counts[0], counts[1]));
        }
        return result;
    }

    private String generateShortCode() {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random r = new Random();
        String code;
        do {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 6; i++) {
                sb.append(chars.charAt(r.nextInt(chars.length())));
            }
            code = sb.toString();
        } while (urlRepository.findByShortCode(code).isPresent());
        return code;
    }
}
