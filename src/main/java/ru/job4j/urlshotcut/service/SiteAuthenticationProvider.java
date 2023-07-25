package ru.job4j.urlshotcut.service;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.job4j.urlshotcut.domain.Site;
import ru.job4j.urlshotcut.repository.SiteRepository;

import java.util.Optional;

import static java.util.Collections.emptyList;

@Service
@AllArgsConstructor
public class SiteAuthenticationProvider implements AuthenticationProvider {

    private final SiteRepository siteRepository;
    private BCryptPasswordEncoder encoder;

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        String userName = authentication.getName();
        String password = authentication.getCredentials().toString();

        Optional<Site> siteOptional = siteRepository.findByLogin(userName);
        if (siteOptional.isEmpty()) {
            throw new BadCredentialsException(userName);
        }
        Site site = siteOptional.get();
        if (!encoder.matches(password, site.getPassword())) {
            throw new BadCredentialsException(password);
        }
        UserDetails user = new User(site.getLogin(), site.getPassword(), emptyList());
        return new UsernamePasswordAuthenticationToken(
                user, password, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
