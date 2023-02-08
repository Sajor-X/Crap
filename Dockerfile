# 选择构建用基础镜像。如需更换，请到[dockerhub官方仓库](https://hub.docker.com/_/java?tab=tags)自行选择后替换。
FROM maven:3.6.0-jdk-11-slim as build

# 指定构建过程中的工作目录
WORKDIR /app

# 将目录下所有文件，拷贝到工作目录中（.gitignore/.dockerignore中文件除外）
ADD . /app/

# 将pom.xml文件，拷贝到工作目录下
#COPY settings.xml pom.xml /app/

# 执行代码编译命令
# 自定义settings.xml, 选用国内镜像源以提高下载速度
RUN mvn -s /app/settings.xml -f /app/pom.xml clean package

# 选择运行时基础镜像
FROM alpine:3.17

# 容器默认时区为UTC，如需使用上海时间请启用以下时区设置命令
RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.tencent.com/g' /etc/apk/repositories \
    && apk add tzdata && cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo Asia/Shanghai > /etc/timezone

# 安装依赖包，如需其他依赖包，请到alpine依赖包管理(https://pkgs.alpinelinux.org/packages?name=php8*imagick*&branch=v3.13)查找。
# 选用国内镜像源以提高下载速度
# fontconfig 增加字体 做验证码时使用
RUN apk add --update --no-cache openjdk11 --repository=http://mirrors.tencent.com/alpine/edge/community \
    && apk add curl \
    && apk add fontconfig \
    && rm -f /var/cache/apk/*

# 指定运行时的工作目录
WORKDIR /app

# 将构建产物jar包拷贝到运行时目录中
COPY --from=build /app/*/target/*.jar ./

# 暴露端口
# 此处端口必须与部署时填写的端口一致，否则会部署失败。
EXPOSE 2727

#健康检查。容器启动时间30s，检查时间间隔30s，超时3s，重试3s
HEALTHCHECK --start-period=30s --interval=30s --timeout=3s --retries=3 CMD curl --silent --fail http://127.0.0.1:2727/api || exit 1

# 执行启动命令.
# 写多行独立的CMD命令是错误写法！只有最后一行CMD命令会被执行，之前的都会被忽略，导致业务报错。
# 请参考[Docker官方文档之CMD命令](https://docs.docker.com/engine/reference/builder/#cmd)
CMD ["java", "-jar", "/app/crap-server-0.0.1.jar"]