package ru.job4j.urlshotcut.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.urlshotcut.domain.Site;
import ru.job4j.urlshotcut.domain.Url;
import ru.job4j.urlshotcut.dto.UrlStatistic;
import ru.job4j.urlshotcut.repository.UrlRepository;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UrlService {

    private final UrlRepository urlRepository;
    private final CodeGeneratorService codeGenerator;

    public Optional<Url> findByUrl(String url) {
        return urlRepository.findByUrl(url);
    }

    public Optional<Url> findByCode(String code) {
        return urlRepository.findByCode(code);
    }

    public List<Url> findAll() {
        return urlRepository.findAll();
    }

    public void incrementTotal(int id) {
        urlRepository.incrementTotal(id);
    }

    public List<UrlStatistic> getUrlsResponse() {
     List<Url> urls = urlRepository.findAll();
     return urls.stream().map(
             u -> new UrlStatistic(u.getUrl(), u.getTotal())).toList();
    }

    public Url save(Url url, Site site) {
        Url newUrl = new Url();
        newUrl.setUrl(url.getUrl());
        newUrl.setCode(codeGenerator.generate());
        newUrl.setSite(site);
        return urlRepository.save(newUrl);
    }

}
