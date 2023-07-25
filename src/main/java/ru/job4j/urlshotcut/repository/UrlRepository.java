package ru.job4j.urlshotcut.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.urlshotcut.domain.Url;

import java.util.List;
import java.util.Optional;

@Repository
public interface UrlRepository extends CrudRepository<Url, Integer> {
    String INCREMENT_TOTAL =
            "UPDATE urls SET total = total + 1 WHERE id = ?1";

    Optional<Url> findByUrl(String url);

    Optional<Url> findByCode(String code);

    @Override
    List<Url> findAll();

    @Transactional
    @Modifying
    @Query(value = INCREMENT_TOTAL,
            nativeQuery = true)
    void incrementTotal(Integer id);
}
