package com.example.github_service.controller;

import com.example.github_service.dto.GitHubUserDataDto;
import com.example.github_service.service.GitHubDataService;

import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("github")
@Validated
public class GitHubController {

    private final GitHubDataService gitHubDataService;

    @Autowired
    public GitHubController(GitHubDataService gitHubDataService) {
        this.gitHubDataService = gitHubDataService;
    }

    @GetMapping("/users/{username}/data")
    public ResponseEntity<GitHubUserDataDto> getUserData(@PathVariable("username") @NotBlank String username) {
        GitHubUserDataDto gitHubUserDataDto = gitHubDataService.getUserData(username);
        return ResponseEntity.ok(gitHubUserDataDto);
    }
}
