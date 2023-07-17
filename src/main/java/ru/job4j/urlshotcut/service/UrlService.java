package ru.job4j.urlshotcut.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.urlshotcut.domain.Url;
import ru.job4j.urlshotcut.dto.UrlStatistic;
import ru.job4j.urlshotcut.repository.UrlRepository;
import java.util.List;

@Service
@AllArgsConstructor
public class UrlService {

    private final UrlRepository urlRepository;

    public Url findByUrl(String url) {
        return urlRepository.findByUrl(url);
    }

    public Url findByCode(String code) {
        return urlRepository.findByCode(code);
    }

    public List<Url> findAll() {
        return urlRepository.findAll();
    }

    public List<UrlStatistic> getUrlsResponse() {
     List<Url> urls = urlRepository.findAll();
     return urls.stream().map(
             u -> new UrlStatistic(u.getUrl(), u.getTotal())).toList();
    }

    public Url save(Url url) {
        return urlRepository.save(url);
    }

    public void incrementTotal(Url url) {
        url.setTotal(url.getTotal() + 1);
        urlRepository.save(url);
    }
}
