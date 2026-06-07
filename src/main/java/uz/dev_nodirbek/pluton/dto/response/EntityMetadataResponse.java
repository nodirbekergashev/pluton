package uz.dev_nodirbek.pluton.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EntityMetadataResponse {
    private String className;
    private String packageName;
    private List<FieldMetadata> fields;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FieldMetadata {
        private String name;
        private String type;
        private List<String> annotations;
    }
}