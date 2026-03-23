package UrlShortenerApp.repository;

import UrlShortenerApp.entity.ClickEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IClickEventRepository extends JpaRepository<ClickEvent, Long> {

    List<ClickEvent> findByUrlIdOrderByClickedAtDesc(Long urlId);

    @Query(value = "SELECT CAST(clicked_at AS DATE) AS d, COUNT(*) AS c "
            + "FROM click_events GROUP BY CAST(clicked_at AS DATE) ORDER BY d",
            nativeQuery = true)
    List<Object[]> countClicksByDate();
}
