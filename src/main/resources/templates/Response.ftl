package ${packageName}.dto.response;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ${className}Response {

<#list fields as field>
    private ${field.type} ${field.name};
</#list>
}