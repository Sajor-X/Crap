package ${package.Service};

import ${superServiceClassPackage};
import ${package.Controller}.${table.controllerName};
import ${package.Mapper}.${table.controllerName}Mapper;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Repository;

/**
 * ${table.comment!} Dao
 *
 * @author ${author}
 * @since ${date}
 */
<#if kotlin>
interface ${table.serviceName} : ${superServiceClass}<${entity}>
<#else>
@Repository
@CacheConfig(cacheNames = "${table.controllerName}", keyGenerator = "cacheKeyGenerator")
public class ${table.serviceName} extends ${superServiceClass}<${table.controllerName}Mapper, ${table.controllerName}> {

}
</#if>
