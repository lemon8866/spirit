FROM alpine
ENV TZ=Asia/Shanghai
ARG BUILD_VERSION=2024.05.27
ENV YT_DLP_VERSION=$BUILD_VERSION
RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.ustc.edu.cn/g' /etc/apk/repositories && \
    apk upgrade --update-cache && \
    apk add openjdk8 && \
    apk add ffmpeg && \
    apk add python3 && \
    rm -rf /tmp/* /var/cache/apk/*
RUN wget -O /usr/local/bin/yt-dlp https://github.com/yt-dlp/yt-dlp/releases/download/${YT_DLP_VERSION}/yt-dlp && chmod a+rx /usr/local/bin/yt-dlp
ENV YT_DLP_PATH /usr/local/bin/yt-dlp
VOLUME ["/tmp","/app"]
COPY db /home/app/db/
RUN mkdir -p /app/resources
ADD spirit-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar","--spring.profiles.active=docker"]