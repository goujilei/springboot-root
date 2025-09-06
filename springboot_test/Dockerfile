# 选择基础镜像（JDK）
FROM eclipse-temurin:8-jdk-alpine
# 设置工作目录
WORKDIR /app

# 拷贝 jar 包到容器中
COPY target/springboot_test-0.0.1-SNAPSHOT.jar app.jar

# 暴露端口（可选）
EXPOSE 8080

# 启动 Spring Boot 应用
ENTRYPOINT ["java", "-jar", "app.jar"]
