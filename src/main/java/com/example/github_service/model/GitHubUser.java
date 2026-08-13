package com.example.github_service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("avatar_url")
    String avatarUrl;
    String location;
    String email;
    String url;
    @JsonProperty("created_at")
    String createdAt;
}
