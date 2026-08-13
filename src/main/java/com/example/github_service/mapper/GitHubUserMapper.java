package com.example.github_service.mapper;

import com.example.github_service.dto.GitHubUserDataDto;
import com.example.github_service.model.GitHubUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface GitHubUserMapper {
    @Mapping(source = "login", target = "userName")
    @Mapping(source = "name", target = "displayName")
    @Mapping(source = "avatarUrl", target = "avatar", qualifiedByName = "stringToUri")
    @Mapping(source = "location", target = "geoLocation")
    @Mapping(source = "url", target = "url", qualifiedByName = "stringToUri")
    @Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "stringToOffsetDateTime")
    @Mapping(target = "repos", ignore = true)
    GitHubUserDataDto toDto(GitHubUser user);

    /**
     * Custom null-safe converter from String to URI.
     */
    @Named("stringToUri")
    default URI stringToUri(String url) {
        if (url == null || url.isBlank()) {
            return null;
        }
        try {
            return new URI(url);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid URI syntax: " + url, e);
        }
    }

    @Named("stringToOffsetDateTime")
    default OffsetDateTime stringToOffsetDateTime(String createdAt) {
        if (createdAt == null || createdAt.isBlank()) {
            return null;
        }
        // Custom logic: e.g., using ISO formatter or parsing manually
        return OffsetDateTime.parse(createdAt, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
}
