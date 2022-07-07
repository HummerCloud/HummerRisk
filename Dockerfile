FROM registry.cn-beijing.aliyuncs.com/hummerrisk/nuclei:v0.1 as nuclei-env

FROM registry.cn-beijing.aliyuncs.com/hummerrisk/prowler:v0.1 as prowler-env

FROM registry.cn-beijing.aliyuncs.com/hummerrisk/dependency-check:v0.1 as dependency-env

FROM registry.cn-beijing.aliyuncs.com/hummerrisk/xray:v0.1 as xray-env

FROM registry.cn-beijing.aliyuncs.com/hummerrisk/grype:v0.1 as grype-env

FROM registry.cn-beijing.aliyuncs.com/hummerrisk/syft:v0.1 as syft-env

FROM registry.cn-beijing.aliyuncs.com/hummerrisk/cloud-custodian:v0.1

WORKDIR /usr

COPY --from=nuclei-env /usr/local/bin/nuclei /usr/local/bin/nuclei

COPY --from=prowler-env /prowler /prowler

COPY --from=dependency-env /dependency-check /dependency-check

COPY --from=xray-env /opt/hummerrisk/xray/ /opt/hummerrisk/xray/

COPY --from=grype-env /grype /usr/bin/grype

COPY --from=syft-env /syft /usr/bin/syft

RUN mkdir -p /opt/apps

COPY backend/target/backend-1.0.jar /opt/apps

ARG HR_VERSION=dev

ENV JAVA_APP_JAR=/opt/apps/backend-1.0.jar

ENV AB_OFF=true

ENV HR_VERSION=${HR_VERSION}

ENV JAVA_OPTIONS="-Dfile.encoding=utf-8 -Djava.awt.headless=true -Xss5m -XX:+UseParallelGC -XX:GCTimeRatio=4 -XX:AdaptiveSizePolicyWeight=90 -XX:MinHeapFreeRatio=20 -XX:MaxHeapFreeRatio=40 -XX:+ExitOnOutOfMemoryError"

HEALTHCHECK --interval=15s --timeout=5s --retries=20 --start-period=30s CMD curl -f 127.0.0.1:8088

CMD ["/deployments/run-java.sh"]