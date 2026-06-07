package uz.dev_nodirbek.pluton.service;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import org.kohsuke.github.*;
import org.springframework.stereotype.Service;
import uz.dev_nodirbek.pluton.dto.response.EntityMetadataResponse;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GitHubService {
    private GitHub buildClient(String accessToken) throws IOException {
        return new GitHubBuilder()
                .withOAuthToken(accessToken)
                .build();
    }

    public List<String> getEntityFiles(String repoFullName, String entityPackagePath, String accessToken) throws IOException {
        GitHub gitHub = buildClient(accessToken);

        GHRepository repository = gitHub.getRepository(repoFullName);
        GHTree tree = repository.getTreeRecursive("main", 1);

        return tree.getTree().stream()
                .map(GHTreeEntry::getPath)
                .filter(path -> path.startsWith(entityPackagePath) && path.endsWith(".java"))
                .collect(Collectors.toList());
    }

    public String getFileContent(String repoFullName, String filePath, String accessToken) throws IOException {
        GitHub gitHub = buildClient(accessToken);

        GHRepository repository = gitHub.getRepository(repoFullName);
        GHContent content = repository.getFileContent(filePath);
        return content.getContent();
    }

    public EntityMetadataResponse parseEntity(String fileContent) {
        CompilationUnit cu = StaticJavaParser.parse(fileContent);

        ClassOrInterfaceDeclaration classDecl = cu.findFirst(ClassOrInterfaceDeclaration.class)
                .orElseThrow(() -> new RuntimeException("Class topilmadi"));

        String className = classDecl.getNameAsString();
        String packageName = cu.getPackageDeclaration()
                .map(pd -> pd.getNameAsString())
                .orElse("");

        List<EntityMetadataResponse.FieldMetadata> fields = classDecl.getFields().stream()
                .flatMap(field -> field.getVariables().stream()
                        .map(var -> EntityMetadataResponse.FieldMetadata.builder()
                                .name(var.getNameAsString())
                                .type(field.getElementType().asString())
                                .annotations(field.getAnnotations().stream()
                                        .map(a -> a.getNameAsString())
                                        .collect(Collectors.toList()))
                                .build()))
                .collect(Collectors.toList());

        return EntityMetadataResponse.builder()
                .className(className)
                .packageName(packageName)
                .fields(fields)
                .build();
    }
}