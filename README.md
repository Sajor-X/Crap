# Crap

Crap make project easy.


## 代码生成器 crap-generator 

### Comment 特殊前缀, 用于生成实体时控制字段类型

- enum / enum(xx.xx.xx) : 枚举类型, 实现 work.sajor.crap.core.mybatis.facade.FieldEnum 接口
- json / json(xx.xx.xx) : 使用 json 格式的存储对象, 使用 JacksonTypeHandler 转换. 例如 json(java.util.List<String>)
- type(xx.xx.xx)        : 指定类型, 简单类型转换
- prop(xx.xx.xx)        : 指定属性名
- code(xxx)             : 编码序列, 只能用在 code 字段上
- encrypt               : 字段自动加解密

