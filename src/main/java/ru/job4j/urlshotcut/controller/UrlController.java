package ru.job4j.urlshotcut.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;
import ru.job4j.urlshotcut.domain.Site;
import ru.job4j.urlshotcut.domain.Url;
import ru.job4j.urlshotcut.dto.CodeResponse;
import ru.job4j.urlshotcut.service.SiteService;
import ru.job4j.urlshotcut.service.UrlService;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/urls")
public class UrlController {

    private final UrlService urlService;
    private final SiteService siteService;
    private final ObjectMapper objectMapper;

    @PostMapping("/convert")
    public ResponseEntity<CodeResponse> convert(
            @Valid @RequestBody Url url,
            @CurrentSecurityContext(expression = "authentication") Authentication authentication)
                throws AuthenticationException {
        Optional<Url> foundUrl = urlService.findByUrl(url.getUrl());
        if (foundUrl.isPresent()) {
            return new ResponseEntity<>(
                    new CodeResponse(foundUrl.get().getCode()),
                    HttpStatus.FOUND
            );
        }
        String login = authentication.getPrincipal().toString();
        Optional<Site> site = siteService.findByLogin(login);
        if (site.isEmpty()) {
            throw new AuthenticationException(String.format("Login %s not found", login));
        }
        Url newUrl = urlService.save(url, site.get());
        return new ResponseEntity<>(
                new CodeResponse(newUrl.getCode()),
                HttpStatus.CREATED
        );
    }

    @GetMapping("redirect/{code}")
    public ResponseEntity<String> redirect(@PathVariable String code) {
        Optional<Url> foundUrlOptional = urlService.findByCode(code);
        if (foundUrlOptional.isEmpty()) {
            return new ResponseEntity<>(
                    String.format("Redirect url not found on Code=%s", code),
                    HttpStatus.NOT_FOUND
            );
        }
        Url foundUrl = foundUrlOptional.get();
        urlService.incrementTotal(foundUrl.getId());
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

    @ExceptionHandler(value = { AuthenticationException.class })
    public void exceptionHandler(Exception e, HttpServletRequest request,
                                 HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(new HashMap<>() { {
            put("message", e.getMessage());
        }}));
    }
}
