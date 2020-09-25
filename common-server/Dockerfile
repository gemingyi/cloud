# 基于哪个镜像
FROM java:8

# 创建工作目录
RUN mkdir -p /user/src/local/service
WORKDIR /user/src/local/service

# 拷贝包到容器
ADD ./target/common-server.jar common-server.jar

# 设置jar启动参数
ENV JAVA_OPTS="\
-server -Xms1024m -Xmx1024m"

# 配置容器启动后执行的命令
#ENTRYPOINT java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar ./common-server.jar
ENTRYPOINT ["java", "-server", "-Xms1024m", "-Xmx1024m", "-Djava.security.egd=file:/dev/./urandom", "-jar", "./common-server.jar"]