package uz.dev_nodirbek.pluton.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @GetMapping("/me")
    public ResponseEntity<?> me(@AuthenticationPrincipal OAuth2User user) {
        return ResponseEntity.ok(user.getAttributes());
    }

    @GetMapping("/token")
    public ResponseEntity<?> token(OAuth2AuthenticationToken authentication) {
        OAuth2AuthorizedClient client = authorizedClientService
                .loadAuthorizedClient(
                        authentication.getAuthorizedClientRegistrationId(),
                        authentication.getName()
                );
        return ResponseEntity.ok(Map.of("token", client.getAccessToken().getTokenValue()));
    }

    private final OAuth2AuthorizedClientService authorizedClientService;

    public AuthController(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }
}