package UrlShortenerApp.config;

import UrlShortenerApp.entity.ClickEvent;
import UrlShortenerApp.entity.Url;
import UrlShortenerApp.repository.IClickEventRepository;
import UrlShortenerApp.repository.IUrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Random;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private IUrlRepository urlRepository;

    @Autowired
    private IClickEventRepository clickEventRepository;

    @Override
    public void run(String... args) {
        if (urlRepository.count() > 0) return;

        LocalDateTime now = LocalDateTime.now();
        Random rand = new Random(42);

        Object[][] seeds = {
            {"https://www.codester.com/items/3423/easy-url-shortener-with-analytics-php-mysql", "nOQx7r", 0},
            {"http://www.google.com",     "nOMy4k", 4},
            {"http://t.tc",              "nNnz2p", 6},
            {"https://www.codester.com/items/3423/easy-url-shortener-with-analytics-php-mysql", "nDEw8q", 6},
            {"http://google.com",        "ntDA3m", 7},
            {"http://google.com",        "nMzx1v", 7},
            {"https://www.codester.com/", "URLSI9", 8},
            {"https://db.tt/K4OBP9c2",   "nMzz6w", 8},
            {"https://www.codester.com/", "nMzY5a", 9},
            {"https://www.youtube.com/watch?v=Qn3m0sQNdu8", "nMzb4c", 9},
            {"https://github.com/features",        "gHftr2", 10},
            {"https://stackoverflow.com/questions", "sOqst8", 11},
            {"https://developer.mozilla.org",       "mDnDv3", 12},
            {"https://reactjs.org/docs/getting-started.html", "rJdGs7", 13},
        };

        for (Object[] s : seeds) {
            String originalUrl = (String) s[0];
            String shortCode = (String) s[1];
            int daysAgo = (int) s[2];

            Url url = new Url();
            url.setOriginalUrl(originalUrl);
            url.setShortCode(shortCode);
            url.setCreatedAt(now.minusDays(daysAgo).minusHours(rand.nextInt(12)));
            url.setClickCount(0);
            urlRepository.save(url);

            int numClicks = rand.nextInt(10) + 1;
            url.setClickCount(numClicks);

            int maxDays = Math.max(daysAgo, 1);
            for (int i = 0; i < numClicks; i++) {
                LocalDateTime clickedAt = url.getCreatedAt()
                        .plusDays(rand.nextInt(maxDays))
                        .plusHours(rand.nextInt(24))
                        .plusMinutes(rand.nextInt(60));
                clickEventRepository.save(new ClickEvent(url.getId(), clickedAt));
            }

            url.setLastAccessedAt(now.minusHours(rand.nextInt(48)));
            urlRepository.save(url);
        }

        System.out.println("Seeded " + urlRepository.count() + " URLs with "
                + clickEventRepository.count() + " click events");
    }
}
