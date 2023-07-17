package ru.job4j.urlshotcut.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.job4j.urlshotcut.domain.Site;
import ru.job4j.urlshotcut.domain.Url;
import ru.job4j.urlshotcut.dto.CodeResponse;
import ru.job4j.urlshotcut.dto.SiteRegistration;
import ru.job4j.urlshotcut.service.SiteService;
import ru.job4j.urlshotcut.service.UrlService;
import ru.job4j.urlshotcut.service.CodeGeneratorService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sites")
@AllArgsConstructor
public class SiteController {

    private final SiteService siteService;
    private final UrlService urlService;
    private final CodeGeneratorService codeGenerator;
    private BCryptPasswordEncoder encoder;

    @PostMapping("/registration")
    public ResponseEntity<SiteRegistration> create(@Valid @RequestBody Site site) {
        Site foundSite = siteService.findByUrl(site.getUrl());
        if (foundSite != null) {
            return new ResponseEntity<>(
                    new SiteRegistration(),
                    HttpStatus.BAD_REQUEST
            );
        }
        String password = UUID.randomUUID().toString();
        site.setLogin(UUID.randomUUID().toString());
        site.setPassword(encoder.encode(password));
        Site newSite = siteService.save(site);
        SiteRegistration siteRegistration = new SiteRegistration(!newSite.getLogin().isEmpty(),
                newSite.getLogin(), password);
        return new ResponseEntity<>(
                siteRegistration,
                HttpStatus.CREATED
        );
    }

    @PostMapping("/convert")
    public ResponseEntity<CodeResponse> convert(@Valid @RequestBody Url url) {
        Url foundUrl = urlService.findByUrl(url.getUrl());
        if (foundUrl != null) {
            return new ResponseEntity<>(
                    new CodeResponse(foundUrl.getCode()),
                    HttpStatus.FOUND
            );
        }
        Url newUrl = new Url();
        newUrl.setUrl(url.getUrl());
        newUrl.setCode(codeGenerator.generate());
        urlService.save(newUrl);
        return new ResponseEntity<>(
                new CodeResponse(newUrl.getCode()),
                HttpStatus.CREATED
        );
    }

    @GetMapping("redirect/{code}")
    public ResponseEntity<String> redirect(@PathVariable String code) {
        Url foundUrl = urlService.findByCode(code);
        if (foundUrl == null) {
            return new ResponseEntity<>(
                    String.format("Redirect url not found on Code=%s", code),
                    HttpStatus.NOT_FOUND
            );
        }
        urlService.incrementTotal(foundUrl);
        return ResponseEntity.status(HttpStatus.valueOf(302))
                .header("URL", foundUrl.getUrl())
                .contentType(MediaType.APPLICATION_JSON)
                .contentLength(foundUrl.getUrl().length())
                .body(foundUrl.getUrl());
    }

    @GetMapping("/statistic")
    public ResponseEntity<String> findAll() {
        var body = new HashMap<>() {{
            put("urls", urlService.getUrlsResponse());
        }}.toString();
        return ResponseEntity.status(HttpStatus.OK)
                .header("UrlShotCutHeader", "url_shortcut")
                .contentType(MediaType.APPLICATION_JSON)
                .contentLength(body.length())
                .body(body);
    }

}
