package ${packageName}.service;

import ${packageName}.entity.${className};
import ${packageName}.repository.${className}Repository;
import ${packageName}.dto.request.${className}Request;
import ${packageName}.dto.response.${className}Response;
import ${packageName}.mapper.${className}Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ${className}Service {

private final ${className}Repository ${className?uncap_first}Repository;
private final ${className}Mapper ${className?uncap_first}Mapper;

public List
<${className}Response> getAll() {
    return ${className?uncap_first}Repository.findAll()
    .stream()
    .map(${className?uncap_first}Mapper::toResponse)
    .toList();
    }

    public ${className}Response getById(Long id) {
    ${className} ${className?uncap_first} = ${className?uncap_first}Repository.findById(id)
    .orElseThrow(() -> new RuntimeException("${className} not found"));
    return ${className?uncap_first}Mapper.toResponse(${className?uncap_first});
    }

    public ${className}Response create(${className}Request request) {
    ${className} ${className?uncap_first} = ${className?uncap_first}Mapper.toEntity(request);
    return ${className?uncap_first}Mapper.toResponse(${className?uncap_first}Repository.save(${className?uncap_first}));
    }

    public ${className}Response update(Long id, ${className}Request request) {
    ${className?uncap_first}Repository.findById(id)
    .orElseThrow(() -> new RuntimeException("${className} not found"));
    ${className} ${className?uncap_first} = ${className?uncap_first}Mapper.toEntity(request);
    ${className?uncap_first}.setId(id);
    return ${className?uncap_first}Mapper.toResponse(${className?uncap_first}Repository.save(${className?uncap_first}));
    }

    public void delete(Long id) {
    ${className?uncap_first}Repository.findById(id)
    .orElseThrow(() -> new RuntimeException("${className} not found"));
    ${className?uncap_first}Repository.deleteById(id);
    }
    }