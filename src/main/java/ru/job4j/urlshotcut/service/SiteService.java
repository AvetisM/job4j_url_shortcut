package ru.job4j.urlshotcut.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.job4j.urlshotcut.domain.Site;
import ru.job4j.urlshotcut.dto.SiteRegistration;
import ru.job4j.urlshotcut.repository.SiteRepository;

import java.util.Optional;
import java.util.UUID;

import static java.util.Collections.emptyList;

@Service
@AllArgsConstructor
public class SiteService implements UserDetailsService {

    private final SiteRepository siteRepository;
    private BCryptPasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        Optional<Site> siteOptional = findByUrl(name);
        if (siteOptional.isEmpty()) {
            throw new UsernameNotFoundException(name);
        }
        return new User(siteOptional.get().getLogin(),
                siteOptional.get().getPassword(), emptyList());
    }

    public Optional<Site> findByUrl(String url) {
        return siteRepository.findByUrl(url);
    }

    public Optional<Site> findByLogin(String login) {
        return siteRepository.findByLogin(login);
    }

    public SiteRegistration getSiteRegistration(Site site) {
        String password = UUID.randomUUID().toString();
        site.setLogin(UUID.randomUUID().toString());
        site.setPassword(encoder.encode(password));
        Site newSite = save(site);
        return new SiteRegistration(!newSite.getLogin().isEmpty(),
                newSite.getLogin(), password);
    }

    public Site save(Site site) {
        return siteRepository.save(site);
    }

}
