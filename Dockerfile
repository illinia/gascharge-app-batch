FROM arm64v8/centos

RUN sed -i 's/mirrorlist/#mirrorlist/g' /etc/yum.repos.d/CentOS-Linux-*
RUN sed -i 's|#baseurl=http://mirror.centos.org|baseurl=http://vault.centos.org|g' /etc/yum.repos.d/CentOS-Linux-*

RUN yum update -y
RUN yum install -y java-17-openjdk.aarch64

COPY ./build/libs/gascharge-app-batch-0.0.1-SNAPSHOT.jar ./gascharge-app-batch-0.0.1-SNAPSHOT.jar
COPY ./application-charge.yml ./application-charge.yml

ENTRYPOINT ["java", "-jar", "-Dspring.server.port=8401", "-Dspring.profiles.active=dev", "-Dspring.config.location=application-charge.yml,classpath:/application.yml", "gascharge-app-batch-0.0.1-SNAPSHOT.jar" ]