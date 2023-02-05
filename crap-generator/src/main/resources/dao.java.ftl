package ${package.Service};

import ${superServiceClassPackage};
import ${package.ServiceImpl}.${table.serviceImplName};
import ${package.Mapper}.${table.serviceImplName}Mapper;
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
    @CacheConfig(cacheNames = "${table.serviceImplName}", keyGenerator = "cacheKeyGenerator")
    public class ${table.serviceName} extends ${superServiceClass}
    <${table.serviceImplName}Mapper, ${table.serviceImplName}> {

    }
</#if>
