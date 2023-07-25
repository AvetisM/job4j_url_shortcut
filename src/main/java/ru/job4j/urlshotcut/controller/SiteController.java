package ru.job4j.urlshotcut.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.urlshotcut.domain.Site;
import ru.job4j.urlshotcut.dto.SiteRegistration;
import ru.job4j.urlshotcut.service.SiteService;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/sites")
public class SiteController {

    private final SiteService siteService;

    @PostMapping("/registration")
    public ResponseEntity<SiteRegistration> create(@Valid @RequestBody Site site) {
        Optional<Site> foundSite = siteService.findByUrl(site.getUrl());
        if (foundSite.isPresent()) {
            return new ResponseEntity<>(
                    new SiteRegistration(),
                    HttpStatus.BAD_REQUEST
            );
        }
        return new ResponseEntity<>(
                siteService.getSiteRegistration(site),
                HttpStatus.CREATED
        );
    }

}
