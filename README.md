# Crap

Crap make project easy.

## 代码生成器 crap-generator

### Comment 特殊前缀, 用于生成实体时控制字段类型

- enum / enum(xx.xx.xx) : 枚举类型, 实现 work.sajor.crap.core.mybatis.facade.FieldEnum 接口
- json / json(xx.xx.xx) : 使用 json 格式的存储对象, 使用 JacksonTypeHandler 转换. 例如 json(java.util.List<String>)
- type(xx.xx.xx)        : 指定类型, 简单类型转换
- prop(xx.xx.xx)        : 指定属性名
- code(xxx)             : 编码序列, 只能用在 code 字段上
- encrypt(xxx)          : 字段自动加解密

## TODO

- [x] 代码生成器，支持注释控制类型 例如List使用typeHandler自动解析
- [x] 字典返回封装（代码中定义的枚举作为字典，自动封装返回，代码启动时扫描所有枚举注册到Map中）
- [x] 数据库字段自动加密（使用代码生成器，在字段注释中使用encrypt前缀）
- [x] 自动脱敏注解（使用自定义注解PrivacyEncrypt实现）
- [x] 统一异常返回
- [x] 基于Rbac权限控制
- [x] 枚举等字典自动封装
- [x] 自定义请求级日志工具注解
- [x] 通用控制器提供增删改查导入导出等基础功能
- [x] 容器化
- [ ] 账户注册
- [ ] 连表join
- [ ] 自定义请求校验注解
- [ ] 审批流
- [ ] 异步线程池
- [ ] 定时任务 PowerJob/QuartZ
- [ ] Elasticsearch
- [ ] excel导出功能
- [ ] 文件上传下载
- [ ] pdf 打印功能
- [ ] 消息队列
- [ ] 发号器
- [ ] 消息中心（支持短信、邮件、微信推送等等）
- [ ] 监控