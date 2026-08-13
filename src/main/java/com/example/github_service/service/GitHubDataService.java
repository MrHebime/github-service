package com.example.github_service.service;

import com.example.github_service.dto.GitHubRepoDto;
import com.example.github_service.dto.GitHubUserDataDto;
import com.example.github_service.error.NotFoundException;
import com.example.github_service.mapper.GitHubRepoMapper;
import com.example.github_service.mapper.GitHubUserMapper;
import com.example.github_service.model.GitHubRepo;
import com.example.github_service.model.GitHubUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import static com.example.github_service.service.GitHubService.*;

@Service
public class GitHubDataService {
    private static final Logger logger = LoggerFactory.getLogger(GitHubDataService.class);

    private final GitHubService gitHubService;
    private final GitHubUserMapper gitHubUserMapper;
    private final GitHubRepoMapper gitHubRepoMapper;

    @Autowired
    public GitHubDataService(GitHubService gitHubService,
                             GitHubUserMapper gitHubUserMapper,
                             GitHubRepoMapper gitHubRepoMapper) {
        this.gitHubService = gitHubService;
        this.gitHubUserMapper = gitHubUserMapper;
        this.gitHubRepoMapper = gitHubRepoMapper;

    }

    /**
     * Given the GitHub username, get information about the GitHub user including up to 100 of their GitHub repositories
     * GitHubUserData is cached locally
     *
     * @param username - the GitHub username
     * @return data about the GitHub user including up to 100 of their GitHub repositories
     */
    @Cacheable(value = "githubuserdata")
    public GitHubUserDataDto getUserData(String username) {
        logger.info("Calling GitHub");
        try {
            ResponseEntity<GitHubUser> gitHubUserResponse = gitHubService.getUser(username);

            GitHubUser gitHubUser = gitHubUserResponse.getBody();
            GitHubUserDataDto gitHubUserDataDto = gitHubUserMapper.toDto(gitHubUser);

            ResponseEntity<List<GitHubRepo>> gitHubRepoResponse = gitHubService.getUserRepos(username,
                    FIRST_PAGE, MAX_PER_PAGE);

            List<GitHubRepo> gitHubRepoList = gitHubRepoResponse.getBody();
            List<GitHubRepoDto> gitHubRepoDtoList = gitHubRepoMapper.toDtoList(gitHubRepoList);

            gitHubUserDataDto.setRepos(gitHubRepoDtoList);

            return gitHubUserDataDto;
        } catch (HttpClientErrorException ex) {
            if (HttpStatus.NOT_FOUND.equals(ex.getStatusCode())) {
                throw new NotFoundException("User " + username + " not found");
            } else {
                logger.error("Exception calling GitHub API: ", ex);
            }
        }
        return new GitHubUserDataDto();
    }
}
