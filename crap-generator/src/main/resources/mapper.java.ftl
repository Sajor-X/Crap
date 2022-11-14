package ${package.Mapper};

import ${package.Controller}.${table.controllerName};
import ${superMapperClassPackage};

/**
 * ${table.comment!} Mapper 接口
 *
 * @author ${author}
 * @since ${date}
 */
<#if kotlin>
interface ${table.mapperName} : ${superMapperClass}<${table.controllerName}>
<#else>
public interface ${table.mapperName} extends ${superMapperClass}<${table.controllerName}> {

}
</#if>
