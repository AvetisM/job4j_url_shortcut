package ru.job4j.urlshotcut.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.urlshotcut.domain.Site;

@Repository
public interface SiteRepository extends CrudRepository<Site, Integer> {
    Site findByUrl(String url);

    Site findByLogin(String login);

}
