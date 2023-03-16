# gascharge-app-batch

배치 스케줄러, 쿼츠 잡 관련 모듈

* 배치잡 설정 빈, 스케줄 잡과 쿼츠 설정
* 공공 api 호출하는 코드

*module-yml, service-reservation 의존, 독립적으로 실행 가능*

로컬, 원격 메이븐 레포지토리
```groovy
implementation 'com.gascharge.taemin:gascharge-app-batch:0.0.1-SNAPSHOT'
```

멀티 모듈 프로젝트
```groovy
// settings.gradle
includeProject("batch", "app")
```
```groovy
// build.gradle
implementation project(":gascharge-app-batch")
```

YAML 파일 설정은 https://github.com/illinia/gascharge-module-yml 참조