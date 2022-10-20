# AN-backend <a target="_blank" rel="noopener noreferrer nofollow" href="https://github.com/among-neighbors/AN-backend/blob/main/LICENSE"><img src="https://camo.githubusercontent.com/624c9e93c3b48d62d41af5687661c2a8a60ce90093685a281cc181bbbe14c6c1/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f4c6963656e73652d417061636865322e302d627269676874677265656e" alt="license" data-canonical-src="https://img.shields.io/badge/License-Apache2.0-brightgreen" style="max-width: 100%;"></a> <a target="_blank" rel="noopener noreferrer nofollow" href="https://github.com/among-neighbors/AN-backend/releases/tag/1.2.0"><img src="https://img.shields.io/badge/Release-1.2.0-ec8034" alt="license" data-canonical-src="https://img.shields.io/badge/Release-1.2.0-ec8034" style="max-width: 100%;"></a>
## 1. 소개

<img width="157" alt="logo" src="https://user-images.githubusercontent.com/67043922/198819170-dbb0ef03-cb85-4220-bfbd-3f1276776cf3.png">

> 소중한 이웃과 함께하는 이웃사이

이웃 간의 단절과 고령 1인 가구 증가로 인한 다양한 사회 문제를 해결하고자 다양한 주거 형태에 적용 가능한 관리 시스템 모델을 제시합니다.<br>
서비스에서 제공되는 편의 기능을 통해 이웃 간의 소통을 증진하고, 긴급 상황에 대처할 수 있는 환경을 조성합니다.

[이웃사이 위키 바로가기](https://github.com/among-neighbors/AN-backend/wiki)

<br>

## 2. 사용 방법

이웃사이 서버 애플리케이션은 Docker 환경에서 최적화된 동작을 보장합니다.

### git clone
```shell
$ git clone https://github.com/among-neighbors/AN-backend.git
```

### configuration

서버 스펙에 맞게 수정이 필요합니다.

**application.yml**

```yaml
gcp-bucket-name: bucket-name
gcp-directory-name: directory-name
gcp-storage-url: storage-url

group:
  name: group-name
  email: group-email

spring:
  security:
    filter:
      order: 2

  datasource:
    driver-class-name: org.h2.Driver
    url: jdbc:h2:mem:test

  h2:
    console:
      enabled: true
      path: /h2-console

  redis:
    host: localhost
    port: 6379

  jpa:
    properties:
      hibernate:
        default_batch_fetch_size: 100

  mail:
    host: host
    port: port
    username: username
    password: password
    properties:
      mail:
        smtp:
          auth: true

  jwt:
    secret: secret

  servlet:
    multipart:
      max-file-size: 30MB
      max-request-size: 40MB

  cloud:
    gcp:
      storage:
        credentials:
          location: storage-secret-key


springdoc:
  swagger-ui:
    path: path
    operations-sorter: method
  #  version: v1
  paths-to-match: /api/**

server:
  ssl:
    key-store: key-store-path
    key-store-type: PKCS12
    key-store-password: 1234
```

**Dockerfile**
```dockerfile
FROM openjdk:11
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
VOLUME /tmp
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
```

### build & execute

condition
- redis connection
- smtp connection
- gcp storage connection
- database connection

프로젝트 루트 경로에서 실행

**1. Docker 환경 x**

```shell
$ ./gradlew clean build -x test
$ cd build/libs
$ java -jar *.jar
```

**2. Docker 환경**
```shell
$ ./gradlew clean build -x test
$ docker build -t tag-name:1.0 .
$ docker run -p 8080:8080 -d --name=app-name tag-name:1.0
```
<br>

## 3. 시스템 구성
### 사용자 관리 모델
<p align="center">
  <img src="https://user-images.githubusercontent.com/67043922/198874796-66042b9e-a224-4317-b36d-5f6214b12071.png" alt="" width="80%"><br>
  < 계정/프로필 모델 >
</p>
가족 구성원을 모델링한 계정/프로필 형태로 사용자를 관리하고 있습니다.<br>
세대별로 하나의 계정을 가지며, 각 계정 내에서 여러 개의 프로필을 생성할 수 있습니다.
<br>

### 사용자 인증
<p align="center">
  <img src="https://user-images.githubusercontent.com/67043922/198875011-5549859b-6e72-43a3-9e10-d894ed32140e.png" alt="" width="80%"/><br>
  < 사용자 인증 로직 >
</p>
사용자 인증은 계정 레벨과 프로필 레벨로 나뉩니다.<br>
따라서 서버에서는 계정과 프로필 토큰을 각각 발급하고 있으며 Spring Security 를 활용하여 각 토큰을 검증합니다.
<br>

### 긴급 호출
<p align="center">
  <img src="https://user-images.githubusercontent.com/67043922/198874882-49a350c1-5298-4943-8438-7feba1527e2f.png" alt="" width="80%"/><br>
  < 긴급 호출 모델 >
</p>
WebSocket 을 통해 사용자를 연결할 수 있습니다. <br>
연결된 사용자 간에는 pub/sub 방식을 통해 실시간으로 긴급 호출 메세지를 전달할 수 있습니다.

#### [ 자세한 내용은 위키에서 확인하실 수 있습니다. ]
<br>

## 4. 관련 저장소
+ [AN-frontend](https://github.com/among-neighbors/AN-frontend)
+ [AN-frontend-builtin](https://github.com/among-neighbors/AN-frontend-builtin)
+ [AN-frontend-manager](https://github.com/among-neighbors/AN-frontend-manager)

<br>

## 5. 오픈소스

[APACHE License](LICENSE)

[Contribution Guideline](CONTRIBUTING.md)


 
