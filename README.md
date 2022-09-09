# AN-backend
## 1. 소개

![image](https://user-images.githubusercontent.com/51109514/189318228-65c3c8e1-624d-4a3d-aaa4-946f0c0d2daa.png)

> 소중한 이웃과 함께하는 이웃사이

이웃사이는 나와 가장 가까운 이웃간의 사소하지만 소중한 가치들을 공유할 수 있는 인프라를 형성하고자 출시한 서비스 입니다.

<br>

## 2. 사용 방법

이웃사이 서버 애플리케이션은 Docker 환경에서 최적화된 동작을 보장합니다.

### **git clone**
```shell
$ git clone https://github.com/among-neighbors/AN-backend.git
```

<br>

### configuration

서버 스펙에 맞게 수정이 필요합니다.

**application.yml**

```yaml
group:
  name: name
  email: email

spring:
  security:
    filter:
      order: 2

  datasource:
    driver-class-name: org.h2.Driver
    url: jdbc:h2:mem:test
    
  redis:
    host: host
    port: port

  jpa:
    hibernate:
      ddl-auto: none

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

springdoc:
  swagger-ui:
    path: swagger.html
    operations-sorter: method
  paths-to-match: /api/**
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

<br>

### **build & execute**

condition
- redis connection
- smtp connection

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

## 3. 기능

> 어디서든 누구나 편하게 이용해요.

이웃사이는 **휴대용 기기와 빌트인 기기 간의 인터페이스** 다각화를 통해서 가장 효과적인 접근 방식을 제공합니다.

휴대용 기기 전용 모드로 집 밖에서도 이웃 사이를 이용하고, 편리하게 글을 작성할 수 있습니다.

집 안에서는 **얼굴 인식 기반 연령 별로 제공되는 맞춤형 인터페이스**로 남녀노소 누구나 편리하게 빌트인 기기를 사용할 수 있습니다.

<br>


> 이웃 사이 조금 더 편하게 이야기 할 수 있어요.

이웃사이는 이웃 간 조금은 더 편하게 다가갈 수 있는 온라인 소통 창구를 제공합니다.

**커뮤니티**를 통해 이웃과 공유할 글을 작성할 수 있고, **댓글**로 소통할 수 있습니다.

**민원** 사항도 관리실에 방문하지 않고 온라인으로 편리하게 요청하고, 처리결과를 알 수 있습니다.

<br>


> 이웃 사이 중요한 정보는 놓치면 안돼요.

이웃사이는 집 안팎에서 언제든지 확인 가능한 통합 공지 시스템을 제공합니다.

언제 어디서든 **공지사항**을 간편하게 확인할 수 있습니다. 세대 별 공지를 골라 볼 수도 있습니다.

<br>


> 이웃 사이 골든타임을 지켜줄 수 있어요.

이웃사이는 사용자가 위급한 상황에서 관리실 및 이웃에게 신속하게 도움을 요청할 수 있는 기능을 제공합니다.

**도움을 요청**하는 즉시 같은 라인의 모든 이웃들에게 전달되고, 요청을 수락한 이웃으로부터 필요한 도움을 받을 수 있습니다.

<br>

## 4. 이웃사이 서버 환경

> 이웃사이 서버는 이렇게 개발하고 배포해요

본 프로젝트를 사용해서 이웃사이가 어떻게 서버를 구성하였는지 설명합니다.

<br>

### **기술 스택**

버전 정보는 **build.gradle** 에서 확인할 수 있습니다.

![image](https://user-images.githubusercontent.com/51109514/189318496-0f313a93-b8c4-4d27-baf1-8ebe6850d1cd.png)

자세한 적용 방식은 [이웃사이 기술포스팅](https://gratis-shape-ac1.notion.site/fa9b8f0cddc047c99bef0dbc126b00d3?v=aa3acf9a163146a7ba342d5a5010fdac)
에서 확인할 수 있습니다.

<br>

### **개발 및 배포 환경**



**CI/CD**

이웃사이 서버에서는 CI/CD 환경을 구축하여 클라이언트에게 실시간으로 변경사항을 반영하고 배포과정에서 발생하는 스트레스를 최소화 합니다.

![image](https://user-images.githubusercontent.com/51109514/189318363-10748188-01f9-430b-b3cd-7bea52525316.png)


<br>

**런타임 애플리케이션**

런타임 환경은 도커 컨테이너를 통해 구축하였습니다.

Redis 는 도커 브릿지 네트워크를 만들어 연결, 데이터베이스는 외부 네트워크를 통해 연결된 상태입니다.


![image](https://user-images.githubusercontent.com/51109514/189318513-0817759e-3e84-4cfd-9bf5-3a52159bad4d.png)

<br>

## **5. Contributor**

### 클라이언트
| Name |GitHub|Email|
|------|---|---|
| 김민지  |[wlwl1011](https://github.com/wlwl1011)|minji001011@naver.com|
| 최윤석  |[Yoonlang](https://github.com/Yoonlang)|cdt9473@gmail.com|

### 서버
| Name |GitHub|Email|
|------|---|---|
| 박상현  |[PPakSang](https://github.com/PPakSang)|sanghyun-dev@naver.com|
| 황아영  |[dkdud9261](https://github.com/dkdud9261)|ayxxng73@gmail.com|




