package ${package.Mapper};

import ${package.ServiceImpl}.${table.serviceImplName};
import ${superMapperClassPackage};

/**
 * ${table.comment!} Mapper 接口
 *
 * @author ${author}
 * @since ${date}
 */
<#if kotlin>
interface ${table.mapperName} : ${superMapperClass}<${table.serviceImplName}>
<#else>
public interface ${table.mapperName} extends ${superMapperClass}<${table.serviceImplName}> {

}
</#if>
