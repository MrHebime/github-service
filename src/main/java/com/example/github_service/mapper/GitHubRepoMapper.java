package com.example.github_service.mapper;

import com.example.github_service.dto.GitHubRepoDto;
import com.example.github_service.model.GitHubRepo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Mapper(componentModel = "spring")
public interface GitHubRepoMapper {

    @Mapping(source = "url", target = "url", qualifiedByName="stringToUri")
    GitHubRepoDto toDto(GitHubRepo repo);

    List<GitHubRepoDto> toDtoList(List<GitHubRepo> gitHubRepoList);

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

}
