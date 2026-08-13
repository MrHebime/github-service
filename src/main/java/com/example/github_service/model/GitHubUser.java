package com.example.github_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GitHubUser {
    String login;
    String name;
    String avatarUrl;
    String location;
    String email;
    String url;
    String createdAt;
}
