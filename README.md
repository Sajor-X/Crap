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

### 生成方式

继承CrapGenerator类，调用generator方法即可

``` java
/**
 * 代码生成
 *
 * @param packageName 包名
 * @param packagePath 路径
 * @param prefix      可通过前缀设定生成范围
 * @param trimPrefix  清除前缀
 * @param tables      包含的表, 与 prefix 指定的范围合并
 * @param excludes    排除的表, 在 prefix 指定的范围中排除
 * @param override    是否覆盖
 * @param modelName   模块名称
 */
public void generate(String packageName, String packagePath, String prefix, String trimPrefix, String[] tables, String[] excludes, boolean override, String modelName);
```

## Url 配置

格式 : /route/module/controller/method/id/action

- route : 路由标识, 用于前端机做流量转发, 版本号, tid 等
- module : 模块名, 对应前端 view 目录
- controller: 控制器名, 对应前端 view 目录
- method : 方法名, 对应前端 view 文件
- id : 数据 id
- action : 辅助标识, 用于标记页面动作, 如动态查询字典, 导入解析等
- /module/controller/method 为权限标识, 不包含 id 和 action 部分, 也就是说菜单的 uri 只能包括这三级, 不能含有其他参数

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