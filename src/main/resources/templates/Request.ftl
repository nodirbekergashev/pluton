package ${packageName}.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ${className}Request {

<#list fields as field>
    <#list field.annotations as annotation>
        <#if annotation == "Email">
            @Email
        </#if>
        <#if annotation == "Min">
            @Min(0)
        </#if>
    </#list>
    private ${field.type} ${field.name};
</#list>
}