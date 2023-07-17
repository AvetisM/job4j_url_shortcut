package ru.job4j.urlshotcut.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.job4j.urlshotcut.domain.Site;
import ru.job4j.urlshotcut.repository.SiteRepository;
import static java.util.Collections.emptyList;

@Service
@AllArgsConstructor
public class SiteService implements UserDetailsService {

    private final SiteRepository siteRepository;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        Site site = findByUrl(name);
        if (site == null) {
            throw new UsernameNotFoundException(name);
        }
        return new User(site.getLogin(), site.getPassword(), emptyList());
    }

    public Site findByUrl(String url) {
        return siteRepository.findByUrl(url);
    }

    public Site save(Site site) {
        return siteRepository.save(site);
    }

}
