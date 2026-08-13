package com.example.github_service.service;

import com.example.github_service.model.GitHubRepo;
import com.example.github_service.model.GitHubUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class GitHubService {
    public static final int FIRST_PAGE = 1;
    public static final int MIN_PER_PAGE = 1;
    public static final int MAX_PER_PAGE = 100;
    public static final String GITHUB_BASE_URL = "https://api.github.com";
    public static final String GITHUB_ACCEPT_TYPE = "application/vnd.github+json";
    public static final String GITHUB_PATH_USERS = "/users";
    public static final String GITHUB_PATH_REPOS = "/repos";
    public static final String HEADER_ACCEPT = "Accept";
    public static final String HEADER_LINK = "link";

    private final RestClient restClient;

    @Autowired
    public GitHubService(RestClient.Builder restClientBuilder) {
        restClient = restClientBuilder
                .baseUrl(GITHUB_BASE_URL)
                .defaultHeader(HEADER_ACCEPT, GITHUB_ACCEPT_TYPE)
                .build();
    }


    /**
     * Calls GitHub to get the GitHub user's info
     * See <a href="https://docs.github.com/en/rest/users/users?apiVersion=2026-03-10#get-a-user">...</a>
     * @param username - the GitHub username
     * @return - basic information about the GitHub User
     */
    public ResponseEntity<GitHubUser> getUser(String username) {
        return this.restClient.get()
                .uri(GITHUB_PATH_USERS + "/" + username)
                .retrieve()
                .toEntity(GitHubUser.class);
    }

    /**
     * Calls GitHub to get information about the GitHub user's repositories
     * See <a href="https://docs.github.com/en/rest/repos/repos?apiVersion=2026-03-10#list-repositories-for-a-user">...</a>
     * @param username - the GitHub username
     * @param page - starts at 1
     * @param perPage - between 1 and 100
     * @return a list of GitHub repositories related to the GitHub user
     */
    public ResponseEntity<List<GitHubRepo>> getUserRepos(String username, int page, int perPage) {
        if (page < FIRST_PAGE) {
            throw new IllegalArgumentException("Page must be at least 1");
        }
        if (perPage < MIN_PER_PAGE || perPage > MAX_PER_PAGE) {
            throw new IllegalArgumentException("perPage must be at least 1 and cannot exceed 100");
        }

        ParameterizedTypeReference<List<GitHubRepo>> responseType =
                new ParameterizedTypeReference<>() {
                };

        return this.restClient.get()
                .uri(uriBuilder -> uriBuilder.path(GITHUB_PATH_USERS + "/" + username + GITHUB_PATH_REPOS)
                        .queryParam("page", page)
                        .queryParam("per_page", perPage)
                        .build())
                .retrieve()
                .toEntity(responseType);
    }
}
