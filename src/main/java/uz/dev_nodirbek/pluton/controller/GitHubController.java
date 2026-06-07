package uz.dev_nodirbek.pluton.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;
import uz.dev_nodirbek.pluton.dto.request.GenerateRequest;
import uz.dev_nodirbek.pluton.dto.response.EntityMetadataResponse;
import uz.dev_nodirbek.pluton.service.GenerationService;
import uz.dev_nodirbek.pluton.service.GitHubService;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/github")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class GitHubController {

    private final GitHubService gitHubService;
    private final OAuth2AuthorizedClientService authorizedClientService;
    private final GenerationService generationService;

    @GetMapping("/entities")
    public ResponseEntity<?> getEntities(
            @RequestParam String repoFullName,
            @RequestParam String entityPackagePath,
            OAuth2AuthenticationToken authentication
    ) throws IOException {
        OAuth2AuthorizedClient client = authorizedClientService
                .loadAuthorizedClient(
                        authentication.getAuthorizedClientRegistrationId(),
                        authentication.getName()
                );

        String accessToken = client.getAccessToken().getTokenValue();

        List<String> files = gitHubService.getEntityFiles(repoFullName, entityPackagePath, accessToken);
        return ResponseEntity.ok(files);
    }

    @GetMapping("/entities/metadata")
    public ResponseEntity<?> getEntitiesMetadata(
            @RequestParam String repoFullName,
            @RequestParam String entityPackagePath,
            OAuth2AuthenticationToken authentication
    ) throws IOException {
        OAuth2AuthorizedClient client = authorizedClientService
                .loadAuthorizedClient(
                        authentication.getAuthorizedClientRegistrationId(),
                        authentication.getName()
                );

        String accessToken = client.getAccessToken().getTokenValue();

        List<String> files = gitHubService.getEntityFiles(repoFullName, entityPackagePath, accessToken);

        List<EntityMetadataResponse> metadata = files.stream()
                .filter(f -> !f.contains("/base/"))
                .map(filePath -> {
                    try {
                        String content = gitHubService.getFileContent(repoFullName, filePath, accessToken);
                        return gitHubService.parseEntity(content);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(metadata);
    }

    @PostMapping("/generate")
    public ResponseEntity<?> generate(
            @RequestBody GenerateRequest request,
            OAuth2AuthenticationToken authentication
    ) throws IOException {
        OAuth2AuthorizedClient client = authorizedClientService
                .loadAuthorizedClient(
                        authentication.getAuthorizedClientRegistrationId(),
                        authentication.getName()
                );

        String accessToken = client.getAccessToken().getTokenValue();
        String content = gitHubService.getFileContent(
                request.getRepoFullName(),
                request.getFilePath(),
                accessToken
        );

        EntityMetadataResponse entity = gitHubService.parseEntity(content);
        Map<String, String> generated = generationService.generate(entity, request.getSelectedTypes());

        return ResponseEntity.ok(generated);
    }
}