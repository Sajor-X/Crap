package ${package.Controller};

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ${package.Service}.${table.serviceName};
import ${package.Entity}.${table.entityName};

<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>

/**
 * ${table.comment!} controller
 *
 * @author ${author}
 * @since ${date}
 */
@RestController
@Slf4j
-- 需转移至Controller包中，不允许放在dao层
-- url格式: /{route}/module/controller
@RequestMapping("/{route}/<#if package.ModuleName?? && package.ModuleName != "">${package.ModuleName}<#else>${cfg.model}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.serviceImplName}</#if>")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
    <#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass}<${table.serviceName}> {
    <#else>
public class ${table.controllerName} {
    </#if>


}
</#if>