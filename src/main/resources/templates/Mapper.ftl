package ${packageName}.mapper;

import ${packageName}.entity.${className};
import ${packageName}.dto.request.${className}Request;
import ${packageName}.dto.response.${className}Response;
import org.springframework.stereotype.Component;

@Component
public class ${className}Mapper {

public ${className} toEntity(${className}Request request) {
if (request == null) return null;
${className} entity = new ${className}();
<#list fields as field>
    entity.set${field.name?cap_first}(request.get${field.name?cap_first}());
</#list>
return entity;
}

public ${className}Response toResponse(${className} entity) {
if (entity == null) return null;
return ${className}Response.builder()
<#list fields as field>
    .${field.name}(entity.get${field.name?cap_first}())
</#list>
.build();
}
}