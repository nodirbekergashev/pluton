package uz.dev_nodirbek.pluton.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class GenerateRequest {
    private String repoFullName;
    private String filePath;
    private List<String> selectedTypes;
}