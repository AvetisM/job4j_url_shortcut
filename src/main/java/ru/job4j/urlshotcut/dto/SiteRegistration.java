package ru.job4j.urlshotcut.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SiteRegistration {
    private boolean registration;
    private String login;
    private String password;
}
