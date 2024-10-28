FROM maven:3.9.8-eclipse-temurin-17 AS app-builder
ARG SERVICE_NAME
ARG VERSION
ENV JAR_PATH=/build/${SERVICE_NAME}/target/${SERVICE_NAME}-${VERSION}.jar
WORKDIR /build
COPY . .
RUN ls -la /build
RUN mvn clean install -N
WORKDIR /build/${SERVICE_NAME}
RUN mvn clean package -q -DskipTests
RUN java -Djarmode=tools -jar $JAR_PATH extract --layers --launcher --destination target/layers

FROM eclipse-temurin:17-jdk-alpine AS jre-builder
RUN  jlink --verbose --add-modules ALL-MODULE-PATH --strip-debug --no-man-pages --no-header-files --compress=2 --output /jre

FROM alpine:3.20.3@sha256:beefdbd8a1da6d2915566fde36db9db0b524eb737fc57cd1367effd16dc0d06d
ENV JAVA_HOME=/jre
ENV PATH="$JAVA_HOME/bin:$PATH"
COPY --from=jre-builder /jre $JAVA_HOME
ARG SERVICE_PORT
ARG SERVICE_NAME
WORKDIR /opt/app
RUN adduser -D -s /bin/bash app
ENV LAYER_PATH=/build/${SERVICE_NAME}/target/layers
COPY --from=app-builder --chown=app:app --chmod=500 $LAYER_PATH/dependencies/ ./
COPY --from=app-builder --chown=app:app --chmod=500 $LAYER_PATH/spring-boot-loader/ $LAYER_PATH/snapshot-dependencies $LAYER_PATH/application ./
EXPOSE ${SERVICE_PORT}
LABEL org.opencontainers.image.source=https://github.com/ngodat0103/nt531
LABEL  org.opencontainers.image.description="YAMP - Yet Another Microservice Project"
LABEL  org.opencontainers.image.source=https://github.com/ngodat0103/nt531
LABEL  org.opencontainers.image.title=YAMP
LABEL  org.opencontainers.image.url=https://github.com/ngodat0103/nt531
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]