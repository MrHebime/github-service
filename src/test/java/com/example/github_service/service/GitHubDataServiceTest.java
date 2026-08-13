package com.example.github_service.service;

import com.example.github_service.error.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GitHubDataServiceTest {

    @Mock
    private GitHubService gitHubService;

    @InjectMocks
    private GitHubDataService gitHubDataService;

    @Test
    void getUserData_throwsNotFoundException() {
        // 1. ARRANGE

        // not a real github username
        String username = "octo123cat";

        HttpClientErrorException httpClientErrorException = HttpClientErrorException.create(
                HttpStatus.NOT_FOUND,
                "Not Found",
                null,
                null,
                null
        );

        when(gitHubService.getUser(username)).thenThrow(httpClientErrorException);

        // 2. ACT & ASSERT
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            gitHubDataService.getUserData(username);
        });

        assertEquals("User " + username + " not found", exception.getMessage());
    }
}
