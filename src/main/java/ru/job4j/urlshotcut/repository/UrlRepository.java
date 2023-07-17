package ru.job4j.urlshotcut.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.urlshotcut.domain.Url;

import java.util.List;

@Repository
public interface UrlRepository extends CrudRepository<Url, Integer> {
    Url findByUrl(String url);

    Url findByCode(String code);

    @Override
    List<Url> findAll();
}
