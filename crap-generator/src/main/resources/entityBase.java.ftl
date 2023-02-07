package ${package.Entity};


<#list table.importPackages as pkg>
import ${pkg};
</#list>
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import java.sql.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonProperty;
import work.sajor.crap.core.mybatis.support.TableCode;
import work.sajor.crap.core.mybatis.handler.AESEncryptHandler;
import ${baseEntity};
<#list table.fields as field>
    <#if field.comment?starts_with("enum") && !field.comment?starts_with("enum(")>
import ${package.ServiceImpl}.${table.serviceImplName}.${field.propertyName?cap_first}Enum;
    </#if>
    <#if field.comment?starts_with("json") && !field.comment?starts_with("json(")>
import ${package.ServiceImpl}.${table.serviceImplName}.${field.propertyName?cap_first}Bean;
    </#if>
</#list>
<#if swagger2>
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
</#if>
<#if entityLombokModel>
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
</#if>

/**
 * ${table.comment!}
 *
 * 数据表实体, 与数据库保持同步, 不可修改
 *
 * @author ${author}
 * @since ${date}
 */
<#if entityLombokModel>
@Data
    <#if superEntityClass??>
@EqualsAndHashCode(callSuper = true)
    <#else>
@EqualsAndHashCode(callSuper = false)
    </#if>
</#if>
@TableName(value = "${table.name}", autoResultMap = true)
<#if swagger2>
@ApiModel(value="${entity}对象", description="${table.comment!}")
</#if>
<#if superEntityClass??>
public class ${entity} extends ${superEntityClass}<#if activeRecord><${entity}></#if> {
<#elseif activeRecord>
public class ${entity} extends Model<${entity}> {
<#else>
public class ${entity} implements Serializable, ${baseEntity} {
</#if>

<#if entitySerialVersionUID>
    private static final long serialVersionUID = 1L;
</#if>
<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>

    <#-- 逻辑删除字段加上前缀, 防止连表时重名 -->
    <#if (logicDeleteFieldName!"") == field.name>
        <#assign fieldName="${table.name}.`${field.name}`">
    <#else>
        <#assign fieldName="`${field.name}`">
    </#if>
    <#if field.keyFlag>
        <#assign keyPropertyName="${field.propertyName}"/>
        <#assign keyPropertyType="${field.propertyType}"/>
    </#if>
    <#-- 检查是否有 JSON -->
    <#assign isJson = field.comment?starts_with("json")>
    <#-- 检查是否需要加密 -->
    <#assign isEncrypt = field.comment?starts_with("encrypt")>
    <#if field.comment!?length gt 0>
    /**
     * ${field.comment}
     */
        <#if swagger2>
    @ApiModelProperty(value = "${field.comment}")
        <#else>
    /**
     * ${field.comment}
     */
        </#if>
    </#if>
    <#if field.keyFlag>
        <#-- 主键 -->
        <#if field.keyIdentityFlag>
    @Id
    @TableId(value = "${field.name}", type = IdType.AUTO)
        <#elseif idType??>
    @Id
    @TableId(value = "${field.name}", type = IdType.${idType})
        <#elseif field.convert>
    @Id
    @TableId("${field.name}")
        </#if>
    <#-- 普通字段 -->
    <#elseif field.fill??>
    <#-- -----   存在字段填充设置   ----->
        <#if field.convert>
    @TableField(value = "${field.name}", fill = FieldFill.${field.fill})
        <#else>
    @TableField(value = "${field.name}", fill = FieldFill.${field.fill})
        </#if>
    <#elseif field.convert>
    @TableField("`${field.name}`")
    <#elseif isJson>
    @TableField(value = "`${field.name}`", typeHandler = JacksonTypeHandler.class)
    <#elseif isEncrypt>
    @TableField(value = "`${field.name}`", typeHandler = AESEncryptHandler.class)
    <#else>
    @TableField("${fieldName}")
    </#if>
    <#-- 在 code 字段上标注自动填充前缀 -->
    <#if field.name=="code">
    <#if field.comment?starts_with("code(")>
    @TableCode("${field.comment?keep_after("(")?keep_before(")")}")
    <#else>
    @TableCode()
    </#if>
    </#if>
    <#if field.comment?starts_with("prop(")>
    @JsonProperty("${field.comment?keep_after("(")?keep_before(")")?replace("([a-z])([A-Z]+)","$1_$2","r")?lower_case}")
    <#elseif field.comment?starts_with("ignore")>
    @JsonProperty(value = "${field.comment?keep_after("(")?keep_before(")")?replace("([a-z])([A-Z]+)","$1_$2","r")?lower_case}", access = JsonProperty.Access.WRITE_ONLY)
    <#else>
    @JsonProperty("${field.name}")
    </#if>
    <#-- 乐观锁注解 -->
    <#if (versionFieldName!"") == field.name>
    @Version
    </#if>
    <#-- 逻辑删除注解 -->
    <#if (logicDeleteFieldName!"") == field.name>
    @TableLogic
    </#if>
    <#-- 注释以 enum(xxx) 开头, 生成为枚举值 -->
    <#if field.comment?starts_with("enum(")>
    <#assign res = field.comment?matches("\\([\\w\\.]+\\)")>
    protected ${res[0]?replace("(","")?replace(")","")} ${field.propertyName};
    <#-- 注释以 enum 开头, 生成为内部枚举值 -->
    <#elseif field.comment?starts_with("enum")>
    protected ${field.propertyName?cap_first}Enum ${field.propertyName};
    <#-- 注释以 json(xxx) 开头, 生成为 bean -->
    <#elseif field.comment?starts_with("json(")>
    protected ${field.comment?keep_after("(")?keep_before(")")} ${field.propertyName};
    <#-- 注释以 json 开头, 生成为内部 bean -->
    <#elseif field.comment?starts_with("json")>
    protected ${field.propertyName?cap_first}Bean ${field.propertyName};
    <#-- 注释以 type(xxx) 开头, 指定类型 -->
    <#elseif field.comment?starts_with("type(")>
    protected ${field.comment?keep_after("(")?keep_before(")")} ${field.propertyName};
    <#elseif field.comment?starts_with("prop(")>
    protected ${field.propertyType} ${field.comment?keep_after("(")?keep_before(")")};
    <#-- bit 类型, 生成为布尔值, 以 0/1 前端输出 -->
    <#elseif field.type?starts_with("bit")>
    @JsonFormat(shape = Shape.NUMBER)
    protected Boolean ${field.propertyName};
    <#-- datetime 类型, 标记前端输出格式 -->
    <#elseif field.propertyType=="LocalDateTime">
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime ${field.propertyName};
    <#-- date 类型, 标记前端输出格式 -->
    <#elseif field.propertyType=="LocalDate">
    @JsonFormat(pattern = "yyyy-MM-dd")
    protected LocalDate ${field.propertyName};
    <#-- 非 bit 类型, 不转换成布尔值 -->
    <#elseif field.propertyType=="Boolean">
    protected Integer ${field.propertyName};
    <#-- bigint 类型, 标记前端输出为字符型, 防止 js 数字溢出导致丢失精度 -->
    <#elseif field.propertyType=="Long">
    @JsonFormat(shape = Shape.STRING)
    protected ${field.propertyType} ${field.propertyName};
    <#else>
    protected ${field.propertyType} ${field.propertyName};
    </#if>
</#list>
<#------------  END 字段循环遍历  ---------->

<#if !entityLombokModel>
    <#list table.fields as field>
        <#if field.propertyType == "boolean">
            <#assign getprefix="is"/>
        <#else>
            <#assign getprefix="get"/>
        </#if>
    public ${field.propertyType} ${getprefix}${field.capitalName}() {
        return ${field.propertyName};
    }

    <#if entityBuilderModel>
    public ${entity} set${field.capitalName}(${field.propertyType} ${field.propertyName}) {
    <#else>
    public void set${field.capitalName}(${field.propertyType} ${field.propertyName}) {
    </#if>
        this.${field.propertyName} = ${field.propertyName};
        <#if entityBuilderModel>
        return this;
        </#if>
    }
    </#list>
</#if>
    // ****************************** Db Field ******************************

    public static final String Table = "${table.name}";

    public static class Fields {
    <#list table.fields as field>
        public static final String ${field.propertyName} = "${field.name?replace("`","")}";
    </#list>
    }

    public static class Alias {
    <#list table.fields as field>
        public static final String ${field.propertyName} = "${table.name}.${field.name?replace("`","")}";
    </#list>
    }

    // ****************************** Entity ******************************

    @Override
<#--    public <#if keyPropertyType??>${keyPropertyType}<#else>Serializable</#if> pkVal() {-->
    public Long getId() {
    <#if keyPropertyName??>
        return this.${keyPropertyName};
    <#else>
        return null;
    </#if>
    }

<#if !entityLombokModel>
    @Override
    public String toString() {
        return "${entity}{" +
    <#list table.fields as field>
        <#if field_index==0>
            "${field.propertyName}=" + ${field.propertyName} +
        <#else>
            ", ${field.propertyName}=" + ${field.propertyName} +
        </#if>
    </#list>
        "}";
    }
</#if>
<#-- ----------  字段名方法  ---------->
<#--    // ****************************** 字段名 ******************************-->

<#--<#list table.fields as field>-->
<#--    <#if field.keyFlag>-->
<#--        <#assign keyPropertyName="${field.propertyName}"/>-->
<#--    </#if>-->
<#--    public static String ${field.propertyName}Field() { return "${table.name}.${field.name?replace("`","")}"; }-->

<#--</#list>-->
}
