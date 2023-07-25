package ru.job4j.urlshotcut.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
public class UrlStatistic {
    @EqualsAndHashCode.Include
    private String url;
    private int total;
}
