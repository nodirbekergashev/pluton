package uz.dev_nodirbek.pluton.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import uz.dev_nodirbek.pluton.dto.response.EntityMetadataResponse;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GenerationService {

    private final Configuration freemarkerConfig;

    public Map<String, String> generate(EntityMetadataResponse entity, List<String> selectedTypes) {
        Map<String, String> result = new HashMap<>();

        Map<String, Object> model = new HashMap<>();
        model.put("className", entity.getClassName());
        model.put("packageName", entity.getPackageName());
        model.put("fields", entity.getFields());

        for (String type : selectedTypes) {
            try {
                Template template = freemarkerConfig.getTemplate(type + ".ftl");
                StringWriter writer = new StringWriter();
                template.process(model, writer);
                result.put(type, writer.toString());
            } catch (Exception e) {
                throw new RuntimeException("Template error: " + type, e);
            }
        }

        return result;
    }
}