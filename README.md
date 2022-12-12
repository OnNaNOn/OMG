# 온앤온 (ONO, On NaN On)


# 프로젝트 기획
## 프로젝트 설명
> 👍 대규모 트래픽에서 빠른 검색 및 안정적인 주문 서비스를 제공하는 쇼핑 플랫폼
![image](https://user-images.githubusercontent.com/102216495/206985430-ad323a45-8445-417c-8049-cfe57a52b150.png)

## 설계 고려사항
📌 시나리오

이벤트 상품뿐만 아니라 일반 상품에 대해서도 몇 건의 주문 요청이 들어와도 동시성 문제가 발생하지 않고 주문이 정상적으로 이루어져야 한다.

<details>
<summary>📌 데이터 기준</summary>
<div markdown="1">
1. 상품 데이터 수 : 1,700만 개
 <br/>
KOSIS(국가통계포털)의 온라인 쇼핑몰 취급상품 범위를 보았을 때, 종합몰 기준 취급 상품 수에 맞춰 결정
<br>
  <br>
  
- 종합몰 : **약 1,700만개**
- 전문몰: **약 1,100만개**

![image](https://user-images.githubusercontent.com/102216495/206987598-b38daece-85fc-4b05-bc9a-1ae4ecf0d560.png)

2. 주문 데이터 수 : 약 600만 개
  <br>
  
**3,000,000** (SSG몰 추정멤버수) * **2** (SSG몰 월 평균 고객 결제횟수) = **약 600만**

SSG몰 추정 멤버수 : **300만명**

![image](https://user-images.githubusercontent.com/102216495/206987916-4f2ef5e6-43f5-4d2d-95d8-cb5e619d29b5.png)

<br>

SSG몰 고객 1인당 월 평균 결제횟수 : **2회**

![image](https://user-images.githubusercontent.com/102216495/206987943-fe99a76d-31ab-4409-8df6-f4b88d5c7d7e.png)

</div>
</details>

</div>
</details>

<details>
<summary>📌 Latency, Throughput 목표</summary>
<div markdown="1">
<br/>
	
 1. Latency 목표값 설정  
 
  ```
 📢 KISSmetrics는 고객의 47%가 2초 이내의 시간에 로딩이 되는 웹 페이지를 원하고 있으며, 40%는 로딩에 3초 이상 걸리는 페이지를 바로 떠난다고 설명했습니다.
  ```
  
   * 일반적인 경우 : 0.05~0.1초
   * 복잡한 트랜잭션이 필요한 경우 : 2초이내	
	
	
 2. Throughput 목표값 설정
 
  ```
 📢 News1 자료(2021년 기준)에서 쇼핑 플랫폼별 MAU(Monthly Active User, 월간 순수 이용자) 추이는 평균 약 400만 명이다.
  ```
  
   * MAU : 400만(단위 : 명)
   * DAU : 15만(단위 : 명)
   * 안전계수 : 2.5
   * 1일 평균 접속 수에 대한 최대 피크 때 배율 : 2배<br/><br/>
   ![Untitled (1)](https://user-images.githubusercontent.com/59110017/190335545-856adc4a-17e7-4aaf-8322-dcf580414d5a.png)
   * 1명당 평균 접속 수 : 15회<br/>
   &nbsp; ⇒ 130,000(명) * 15(회) / 86,400(초) * 2.5(안전계수) * 2(1일 평균 접속 수에 대한 최대 피크 때 배율) = 약 130 rps

  	
</div>
</details>

<details>
<summary>📌 동시성 제어 기준</summary>
<br/>

 ```
 📢 MUSINSA는 직매입한 인기 제품들을 최대 60% 할인하는 ‘무신사 라이브' 행사에서 1초당 최대 동시 접속자 수가 6400명을 기록하였다고 하였으며 이는 약 30여 분 만에 품절이 되었다고 하였습니다.
 ```
 
 1. 1초당 최대 동시 접속자 수 : 6400명
 	
	* 인기 온라인 패션 스토어의 특가 할인 케이스를 참고하여, 해당 케이스의 70%정도인 약 4,000명대로 동시 접속자 수 시나리오 설정
 
 2. 시간 당 처리량 : 가용성이 보장되는 범위의 최대치
 
 	* 앞선 Latency의 내용을 참고하여 고객은 가능한 빠른 응답을 원하고 있음
 
 
<div markdown="1">
</div>
</details> 

## 아키텍처
![image](https://user-images.githubusercontent.com/102216495/206994203-ab9eb4b7-0ebb-40a7-98f8-be4aa99ea0cf.png)


## 핵심 기능
### 🧐 검색(필터 및 정렬)
> * Query DSL과 Full Text Search를 활용한 상품 검색 기능 구현


### 🤡 상품 주문
> * 락 기능을 활용한 동시성 제어

### 🧑🏻사용자 & 판매자 페이지
> 사용자가 가진 권한에 따라 특정 페이지 접근 제한 (STANDARD/ADMIN)

* 모든 사용자 > 상품 검색 및 조회
* 로그인한 사용자 (STANDARD) > 상품 주문
* 관리자 권한을 가진 사용자 (ADMIN) > 이벤트 상품 등록

## 트러블슈팅 및 기술적 의사결정
### 👨‍👩‍👧‍👦 동시성 제어
> 최종해결 방안
* Pessimistic Lock 으로 동시성 제어

> 단계별 문제 정의

> 한 개 상품에 대해 10명의 사용자가 1000개를 동시에 주문한다고 가정
![image](https://user-images.githubusercontent.com/102216495/207009325-c5a538bd-49be-4c05-a2e2-a607cd02ddce.png)

1. 하나의 상품에 대해 여러 명의 사용자가 동시에 주문을 할 경우 Race Condition이 발생하여 동시성 이슈 발생
2. 상품 주문 API가 동작이 되면 상품의 재고량이 1씩 감소되어 처리되도록 구현되어 있으나, 여러 명의 사용자가 요청을 보내게 될 경우 재고량을 감소시키고 Commit 되기전의 데이터를 읽게 되면서 데이터 정합성 문제 발생
3. 타임 특가 이벤트 특성상 여러 명의 사용자가 동시에 주문할 경우가 발생하므로 동시성 제어의 필요성을 느낌

<details>
<summary>📈 기술적 의사 결정</summary>
<div markdown="1">

### 해결 방법 수집
### 1. `synchronized`

- `@Transactional` 이 있다면 완전한 해결을 하지 못함
    - `@Transational` 은 사용하려는 클래스를 wrapping한 클래스를 생성하게 됨
    - wrapping한 클래스에서 메소드 사용시 메소드 사용 전후로 **startTransaction**과 **endTransaction**을 실행함
    - 메소드 자체에는 `synchronized`를 통해 한개의 쓰레드만 접근가능하더라도 **해당 메소드 종료 후와 endTransaction 사이**에 다시 동시에 여러 쓰레드가 접근 가능
- 하나의 프로세스 안에서만 보장됨
    - 여러 서버에서 동시에 데이터 접근시 해결 불가

**→ 실제 운영 환경은 여러 서버를 사용하므로 해당 해결 방식 미사용**

### 2. Mysql

### 1. `Pessimistic Lock`

- **exclusive lock**을 통해 해제 전에 다른 트랜잭션에서는 데이터를 가져갈 수 없게 함
- **데드락**을 주의해야함
- 충돌이 빈번하다면 Optimistic lock보다 성능이 좋음
    - optimistic은 충돌시 다시 데이터를 읽어와야함

**→ [문제 정의] 에서의 상황은 충돌이 빈번한 기능이기에, Pessimistic Lock을 해결 방법에 추가**

### 2. `Optimistic Lock`

- lock이 아닌 **버전**을 이용하여 정합성을 맞춤
- 데이터를 읽은 후에 update를 수행할 때 현재 내가 읽은 버전이 맞는지 확인하여 업데이트

**→ [문제 정의] 에서의 상황은 충돌이 빈번한 기능이기에, 충돌감지 기능이 주를 이루는 Optimistic Lock 해결 방식 미사용**

### 3. Redis

### 1. `Lettuce`

- **스핀락** 방식으로 반복적으로 lock을 점유할 수 있는지 확인
    - redis 서버에 부하를 줄 수 있음
- `setnx` 명령어를 통해서 lock의 점유 여부를 확인하고 획득할 수 있음
- 세션 관리가 필요 없음

**→ 락 획득 실패 시, 바로 재진입하여 락 획득을 시도하는 스핀락 방식의 특성으로 인해, 레디스에 계속해서 요청을 보낼 것임. 
그로 인해 부담을 줄 수 있으므로 Lettuce 해결 방식 미사용**

### 2. `Redisson`

- in-memory db 형태이므로, 속도면에서 더욱 빨라질 것으로 예상
- pub-sub 기반으로 Lock 구현 제공
- 쓰레드가 lock을 점유하고 있다가 돌려주면 그것을 락을 획득하고자하는 다른 쓰레드에게 알려줌
- 스핀락 형태가 아닌 lock 해제 되었다는 정보를 획득했을때 lock 점유를 시도함으로 redis 부하가 적음
- lock 관련 클래스를 제공해줌으로 따로 repository를 구현할 필요 없음

**→ Lettuce와 동일한 역할을 하나, 보다 더 안정적으로 동시성 제어를 할 수 있기에 Redisson을 해결 방법에 추가**

## **[결과 관찰]**

**1,000개의 재고**에 대해서 **1,000개의 요청 환경**에서 진행

### 1. `Pessimistic Lock`

처리 시간 >> **5.416s**

### 2. `Redisson`

처리 시간 >> **15.665s**

→ pub/sub을 통한 lock 획득 시도 과정에서 network latency가 overhead로 발생 추정, 

→ **속도 측면 고려하여, 최종 Pessimistic lock 적용**


</div>
</details>

<br>

### 📈 검색 성능 개선
> 최종해결 방안
* No-Offset 도입 검색 성능 향상

> 단계별 문제 정의
![image](https://user-images.githubusercontent.com/102216495/207009839-5ef2e276-fe30-44c5-b63e-bde1278bc079.png)

1. 상품 데이터가 100만개로 증가하면서 메인페이지 로딩 시간이 최대 4.8초까지 증가
2. KISSmetrics에 따르면 로딩시간 3초 이상 시 고객의 40% 이탈율 발생
3. 페이지 로딩 시간 2초 이내를 목표로 문제를 해결해야함

<details>
<summary>📈 기술적 의사 결정</summary>
<div markdown="1">

### 진행단계


</div>
</details>

<br>

### 📶 대규모 트래픽 분산
> 최종해결 방안
* DB Main-Replication 적용으로 서버 요청처리 시간 (Processing Time) 단축  ▶ TPS 개선

> 단계별 문제 정의
1. 상품에 대해 **1초당 1,000명**의 사용자가 주문을 할 경우 **EC2 서버(t2.micro)의 CPU 사용률이 약 100%에 도달하게 되면서 서버가 요청을 받을 수 없는 상태를 확인.** 뿐만 아니라, RDS도 **CPU 사용률 100%** 에 도달하게 되면서 트래픽 분산에 대한 필요성을 느낌

2. 쇼핑몰 서비스의 특성상 실시간 요청이 계속해서 이루어지는 상황에서 서버가 부하되어 요청을 받을 수 없는 상태라면 서비스 사용자의 이탈율이 많을 것이라고 생각되어 해결 방안이 필요함.

<br>

> RDS(db.t3.micro) CPU 사용률(%) / EC2(t2.micro) CPU 사용률(%) / 에러발생 성능테스트 지표
> ![image](https://user-images.githubusercontent.com/102216495/207015577-530bc3fb-83f3-467b-80df-239afbcb24bf.png)

<details>
<summary>📈 기술적 의사 결정</summary>
<div markdown="1">

### 해결 방법 수집
### 1. `서버 Scale-out`
* 주 연산에 해당하는 읽기(Write) 연산을 병렬로 처리가 가능함
* 서버 1대가 10 TPS의 성능을 가진다고 할 때, 2대라면 20 TPS, 3대면 30 TPS의 성능을 가짐 (10 TPS * 서버 N대)

**→ 해당 작업으로 인해 포기해야하는 기술적 장점은 발생하지 않으므로 해결 방법에 추가**

### 2. `서버 Scale-up`
* 처리량이 개선된 Scale-up 버전 사용

**→ 금액적인 문제가 있으므로, 필요성이 커질 경우 추후 해결 방법에 추가할 예정**

### 3. `DB 다중화 (DB Replication - Main-Replication 패턴)`
* 추가(Insert) / 수정(Update) / 삭제(Delete) 기능은 Master DB가 처리하도록 하고, 조회(Select) 기능은 Slave DB로 처리하게 함으로써 요청에 대한 처리를 병렬적으로 처리하도록 하여 **처리시간 (Processing Time)을** 단축

**→ DB 부하를 분산시켜야 함으로, 해결 방법에 추가**


## **[결과 관찰]**

### 1. `DB Replication (부하 분산)`

    3초 동안 1,000회 요청을 10회 반복으로 공통테스트 진행

Throughput      → **5~6% 증가**

Response Time → **전체적 감소**

| 종류 | 단일 DB 주문 | 분산 DB 주문 | 단일 DB 조회 | 분산 DB 조회 |
| --- | --- | --- | --- | --- |
| Throughput | 124.9/sec | 132.3/sec | 130.9/sec | 137.4/sec |

![image](https://user-images.githubusercontent.com/102216495/207014648-b2e3b55f-37a1-4aa2-a534-66189f716050.png)

</div>
</details>

## 프로젝트 관리
<details>
<summary>지속적인 배포(CD)</summary>
<div markdown="1">

   * 지속적인 배포의 필요성
     * 기능이 추가될 때마다 배포해야하는 불편함이 있어 배포 자동화의 필요성 인식
   * 대안
   
     |Jenkins|Github Actions|
     |------|------|
     |무료|일정 사용량 이상 시 유료|
     |작업 또는 작업이 동기화되어 제품을 시장에 배포하는데 더 많은 시간이 소요|클라우드가 있으므로, 별도 설치 필요 없음|
     |계정 및 트리거를 기반으로하며 Github 이벤트를 준수하지 않는 빌드를 중심으로 함|모든 Github 이벤트에 대한 작업을 제공하고 다양한 언어와 프레임워크를 지원|
     |전 세계 많은 사람들이 이용해 문서가 다양|젠킨스에 비해 문서가 없음|
     |캐싱 메커니즘을 지원하기 위해 플러그인 사용 가능|캐싱이 필요한 경우 자체 캐싱 메커니즘을 작성해야함|
     
   * 선택
     * Jenkins가 Github Actions에 비해 별도의 서버 설치가 필요하는 등 인프라 세팅이 까다롭긴 하나 무료이고 다양한 기능들을 제공하기 때문에 선택했음
	
</div>
</details>

<details>
<summary>코딩 컨벤션</summary>
<div markdown="1">
<br/>

   * 코딩 컨벤션의 필요성
     * 정해진 규칙없이 협업을 하다보니 팀원 들의 코드를 이해하기 어려웠고 Git에서 Merge할 때 어려움이 있어서 코딩 컨벤션의 필요성 인식
   * 코딩 컨벤션 장점
     1. 정해진 규칙이 있기 때문에 명칭이나 구조를 빠르고 정확하게 정할 수 있다.
     2. 통일된 규약이 있기 때문에 모든 사람들이 코드를 이해하기 쉽고 편리하다.
     3. 유지보수 비용을 줄일 수 있다.

</div>
</details>
<details>
<summary>Git</summary>
<div markdown="1">
<br/>

   * Git Commit 메시지 컨벤션의 필요성
     * commit된 코드가 어떤 내용을 작성 했는 지 파악하려면 commit을 확인해야 한다.
     * 프로젝트 진행 중에는 수 많은 코드가 commit되기 때문에 일일이 내용을 확인하기 힘들기 때문에 
메시지 컨벤션을 통해서 제목이나 description을 통해서 commit의 정보를 전달한다.
   * Git Commit 메시지 컨벤션 전략
   
   ```
  - ⭐Feat : 새로운 기능 추가
  - 🐛Fix : 버그 수정
  - 📝Docs : 문서 수정
  - 🎨Design: CSS 등 사용자 UI 디자인 변경
  - 🗃️Style : 코드 포맷팅, 세미콜론 누락, 코드 변경이 없는 경우
  - 🔨Refactor : 코드 리펙토링
  - 🤝Test : 테스트 코드, 리펙토링 테스트 코드 추가
  - 🧐!BREAKING CHANGE: 커다란 API 변경의 경우
  - 🚨!HOTFIX: 급하게 치명적인 버그를 고쳐야하는 경우
  - 🔧Rename: 파일 혹은 폴더명을 수정하거나 옮기는 작업만인 경우
  - ➖Remove:파일을 삭제하는 작업만 수행한 경우
  - 📌Comment: 필요한 주석 추가 및 변경
  - 🎉Begin: 프로젝트 초기 설정
   ```
   
</div>
</details>

## MEMBER
- 이승우 님 BE
- 전선영 님 BE
- 안성재 님 BE

</br>

## 기술 스택
### **Application**

- JAVA 11
- Spring Boot 2.7.5
- spring data jpa 2.7.5
- spring security5 3.0.4
- JPA
- redisson 3.18.0
- Swagger 3.0.0 
- jwt 0.11.2 
- Query DSL 5.0.0
- Full Text Search

### **Data**

- AWS RDS - MySQL 8.028
- AWS S3
- Faker (faker_15.2.0)
- h2 1.4.200

### **CI/CD**

- Github Action
- AWS EC2
- AWS CodeDeploy

### **Monitoring**

- Cloud Watch
- Prometheus 1.9.5
- grafana
- Logback 1.2.11
- Slf4j 1.7.3
- Log4j 2.17.2

### TestCode

- Junit 5
- Mock

### Front
- thymeleaf 2.7.5


</br>

## ERD

![Copy of ONO COM (8)](https://user-images.githubusercontent.com/102216495/207005916-9bd7478a-e443-4c92-b273-caf0aecea96c.png)
</br>

## API
https://www.notion.so/4d8247417fb94ed1b69460821da962e7?v=a11f85efb4714bcb988a0d510266985a

</br>

## GIT CONVENTION
- ⭐Feat : 새로운 기능 추가
- 🐛Fix : 버그 수정
- 📝Docs : 문서 수정
- 🎨Design: CSS 등 사용자 UI 디자인 변경
- 🗃️Style : 코드 포맷팅, 세미콜론 누락, 코드 변경이 없는 경우
- 🔨Refactor : 코드 리펙토링
- 🤝Test : 테스트 코드, 리펙토링 테스트 코드 추가
- 🧐!BREAKING CHANGE: 커다란 API 변경의 경우
- 🚨!HOTFIX: 급하게 치명적인 버그를 고쳐야하는 경우
- 🔧Rename: 파일 혹은 폴더명을 수정하거나 옮기는 작업만인 경우
- ➖Remove:파일을 삭제하는 작업만 수행한 경우
- 📌Comment: 필요한 주석 추가 및 변경
- 🎉Begin: 프로젝트 초기 설정
