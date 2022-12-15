## 온앤온 (ONO, On NaN On)

<img src="https://user-images.githubusercontent.com/64416833/207515418-6cd01a4c-d4ed-4255-857a-be91b11f77b4.png">

---

<br/>

### 👋 프로젝트 참여 인원 👋
BE: 안성재, 이승우, 전성영

<br/>

### 🛒 프로젝트 설명 🛒
대규모 트래픽에서 빠른 검색 및 안정적인 주문 서비스를 제공하는 쇼핑 플랫폼

✔ [온앤온닷컴 이용해보기 Click!](http://3.39.234.126/omg) <br>
✔ [온앤온닷컴 팀 노션 Click!](https://www.notion.so/ONO-COM-88223335fb8d4276a89788118b843438#37aba69ac67a41ad92e8ffc835719bcd)

![image](https://user-images.githubusercontent.com/102216495/206985430-ad323a45-8445-417c-8049-cfe57a52b150.png)

<br/>

### 📋 API 📋

[API 명세서](https://www.notion.so/4d8247417fb94ed1b69460821da962e7?v=a11f85efb4714bcb988a0d510266985a)

<br/>

### 🤝 ERD 🤝

![image](https://user-images.githubusercontent.com/64416833/207512412-35dfc732-c5bf-49ec-9f1e-231c69bf19f8.png)

<br/>

### 🏰 아키텍처 🏰

![image](https://user-images.githubusercontent.com/64416833/207512562-c901470f-56b7-4109-830a-4cba32cba8fb.png)

<br/>

### ⚒ 기술 스택 ⚒

- **Application**
    - JAVA 11
    - Spring Boot 2.7.5
    - Spring Data jpa 2.7.5
    - QueryDSL 5.0.0
    - JPA
    - Swagger 3.0.0
    - JWT 0.11.2
- **Data**
    - AWS RDS - MySQL 8.0.28
    - AWS S3
    - Faker (faker 15.2.0)
    - h2 1.4.200
- **CI/CD**
    - Github Actions
    - AWS CodeDeploy

- **Monitoring**
    - Grafana 9.3.1
    - Prometheus 2.37.4
    - loki & promtail 2.6.1
    - Logback 1.2.11
    - Log4j 2.17.2
    - Slf4j 1.7.3
- **Server**
    - AWS ALB
    - AWS Auto-Scaling
- **TestCode**
    - Junit 5
    - mockito 4.8.0
    - jacoco 0.8.8
- **Front**
    - Thymeleaf 3.0.15

<br/>

### ⛳ 시나리오 ⛳

이벤트 상품뿐만 아니라 일반 상품에 대해서도 몇 건의 주문 요청이 들어와도 동시성 문제가 발생하지 않고 주문이 정상적으로 이루어져야 한다.

<details>
<summary>📌 데이터 기준</summary>
<div markdown="1">

1. 상품 데이터 수 : 1,300만 개
 <br/>

[KOSIS(국가통계포털)](https://kosis.kr/statHtml/statHtml.do?orgId=101&tblId=DT_1KE10041) 의 온라인 쇼핑몰 취급상품 범위를 보았을 때, 종합몰 기준 취급 상품 수에 맞춰 결정
<br>

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
 
 1. 1초당 최대 동시 접속자 수 : 1,000명
 	
	* 인기 온라인 패션 스토어의 특가 할인 케이스를 참고하여, 해당 케이스의 20%정도인 약 1,000명대로 동시 접속자 수 시나리오 설정
 
 2. 시간 당 처리량 : 가용성이 보장되는 범위의 최대치
 
 	* 앞선 Latency의 내용을 참고하여 고객은 가능한 빠른 응답을 원하고 있음
 
 
<div markdown="1">
</div>
</details> 

<br/><br/>

### ⚡ 핵심 기능 ⚡
#### 🧐 검색(필터 및 정렬)
Query DSL과 NoOffset 방식을 활용한 상품 검색 기능 구현


#### 🤡 상품 주문
락 기능을 활용한 동시성 제어

#### 🧑🏻사용자 & 판매자 페이지
사용자가 가진 권한에 따라 특정 페이지 접근 제한 (STANDARD/ADMIN)

- 모든 사용자 > 상품 검색 및 조회
- 로그인한 사용자 (STANDARD) > 상품 주문
- 관리자 권한을 가진 사용자 (ADMIN) > 이벤트 상품 등록

<br/>

### ❌ 트러블슈팅 및 기술적 의사결정 과정 ⭕

[📒노션으로 이동📒](https://www.notion.so/ONO-COM-88223335fb8d4276a89788118b843438#37aba69ac67a41ad92e8ffc835719bcd)

### 👨‍👩‍👧‍👦 동시성 제어

#### [**최종 해결방안]**

**Pessimistic Lock 으로 동시성 제어**

#### **[문제 정의]**

> 한 개의 상품에 대해서 10명의 사용자가 1000개 재고가 감소될 때까지 동시에 주문한다고 가정
![image](https://user-images.githubusercontent.com/102216495/207778342-8afa49f3-49d1-40cd-ba16-b856625538cc.png)

하나의 상품에 대해 여러 명의 사용자가 동시에 주문을 할 경우 **Race Condition이 발생하여 동시성 이슈 발생**

상품 주문 API가 동작이 되면 상품의 재고량이 1씩 감소되어 처리되도록 구현되어 있으나, 여러 명의 사용자가 요청을 보내게 될 경우 **재고량을 감소시키고 Commit 되기전의 데이터를 읽게 되면서 데이터 정합성 문제 발생**

타임 특가 이벤트 특성상 여러 명의 사용자가 동시에 주문할 경우가 발생하므로 동시성 제어의 필요성을 느낌

<br>


<details>
<summary> 기술적 의사결정 과정 </summary>
<div markdown="1">

## **[해결 방법 수집]**

### 1. `synchronized`

synchronized는 **하나의 스레드만 접근할 수 있도록 하여 스레드의 불규칙적인 자원 공유(Race Condition)를 막기 위해 사용**한다.

그러나, `synchronized`와 `Transactional`을 함께 사용한다면 원하는 대로 동작하지 않을 수도 있다.

트랜잭션의 begin과 commit은 synchronized 메소드에 포함되지 않는데 그 결과 커밋이 날라가 심사의 수정된 상태가 DB에 반영되기 전에 그 다음 요청을 담당한 스레드가 synchronized 메소드에 진입하여 아직 수정이 반영되지 않은 심사 값을 읽게 되고 예외 처리에 걸리지 않게 된다.

뿐만 아니라, 서버가 여러 대 있다고 가정한다면 synchronized의 사용만으로는 **각 서버마다 요청하는 쓰레드에 대해 정합성이 맞지 않는 문제를 해결할 수 없다.**

그렇다고 해서 `Transactional`을 사용하지 않는다면, 재고는 감소되었지만 주문을 생성하는 과정에서 에러가 발생하게 되면 감소시킨 상품에 대해 재고를 채워놔야 하는 문제점이 생기게 된다. 그래서 주문 처리의 로직이 정상적으로 처리가 되면 `Commit`을 시켜주고, 처리 중 에러가 발생하게 되면 자동으로 `Rollback`을 시켜주는 `Transaction`을 붙여주어야 한다. 

해당 문제는 데이터베이스의 락을 통해 `Race Condition`문제를 해결해야한다.

<br>




### 2. `Redis`

Redis는 Single Thread 기반이기도 하고, 락 정보가 간단한 휘발성 데이터에 가깝기 때문에 가장 좋은 선택지이다. 

또한, Redis에서 분산 락을 구현한 알고리즘으로 redlock이라는 것을 제공하는데 한다.

Redis의 분산 락 구현체로 자바에서는 **Lettuce**와 **redisson**를 제공하게 되는데, 두 Redis Client는 **Netty를 사용하여 non-blocking I/O 로 동작**된다.

<br>


### 2.1 `Lettuce`

Lettuce는 Lock의 타임 아웃이 지정되어 있지 않아서 tryLock을 성공했는데 어떤 오류때문에 애플리케이션이 종료되어버린다면 다른 어플리케이션에서 영원히 락을 획득하지 못한 채 락이 해제되기만을 기다리는 무한정 대기상태가 되어 전체 서비스 장애가 발생하게 된다. 또한, 무한정으로 락 획득 시도를 할 경우에도 레디스에 부하를 줄 수 있기 때문에 도입을 배제했다.

<br>


### 2.2 `Redisson`

Redisson은 여러 독립된 프로세스에서 하나의 자원을 공유해야 할 때, 데이터에 결함이 발생하지 않도록 하기 위해서 **분산 락**을 제공한다.

**Redisson은 락을 잡는 방식이 스핀락 방식이 아니고, `expire time`도 적용할 수 있다**는 특징을 갖는다. 

또한, Redisson은 **pub/sub 방식을 사용**하여 락이 해제될 때마다 subscribe하는 클라이언트들에게 "이제 락 획득을 시도해도 좋다"는 알림을 주는 구조라 스핀락보다 훨씬 적은 부담을 준다.

그러나 Redis의 락 방식을 도입하지 않은 이유는 **lock 획득 시도 과정에서 발생하는 네트워크 지연으로 인해 비관적 락에 비해 속도가 늦었고, 현재 Redis를 도입하지 않았을 뿐더러 Redisson 도입만을 위해 Redis를 적용해야 한다는 점에서 오버 엔지니어링, 러닝 커브 등을 고려했을 때 효율적이지 못하다고 판단되어 비관적 락 처리하도록 구현함.**

<br>


### 3. `Mysql`

MySQL에서 제공하는 Lock의 종류는 크게 Optimistic Lock, Pessimistic Lock이 존재한다.

<br>



### 3.1 `Optimistic Lock`

lock이 아닌 **버전**을 이용하여 정합성을 맞추는 방법으로 데이터를 읽은 후에 update를 수행할 때 현재 내가 읽은 버전이 맞는지 확인하고 맞다면 하는

타임 이벤트 특성상 **충돌이 빈번하게 일어날 것이라고 가정을 했기 때문에 롤백하는 비용이 크다고 판단되어 Optimistic Lock 미적용**

<br>


### 3.2 `Pessimistic Lock` ✅

exclusive lock을 통해 해제 전에 다른 트랜잭션에서는 데이터가 점유할 수 있도록 하는 락 방식으로 **데드락**을 주의해야 한다는 단점이 존재한다.

그러나, 충돌이 빈번하게 일어날 것이라고 예상될 뿐더러 **도입 비용**과 **규모**를 생각했을 때 **가장 적절하다고 판단되어 도입**
	
<br>


## **[결과 관찰]**

**1,000개의 재고**에 대해서 **1,000개의 요청 환경**에서 진행

### 1. `Pessimistic Lock`

처리 시간 >> **5.416s**

### 2. `Redisson`

처리 시간 >> **15.665s**

lock 획득 시도 과정에서 network latency가 overhead로 발생 추정

</div>
</details>

<br>


### 📈 검색 성능 개선

#### [**최종 해결방안]**

**No-Offset 도입 검색 성능 향상**

#### [문제 정의]
- 상품 데이터가 100만개로 증가하면서 **메인페이지 로딩 시간이 최대 4.8초까지 증가**

⇒ KISSmetrics에 따르면 로딩시간 3초 이상 시 고객의 40% 이탈율 발생

⇒ 목표 : 페이지 로딩 시간 **2초 이내**

![image](https://user-images.githubusercontent.com/102216495/207778713-29826e0a-776b-4435-befc-f6dc3c8e3ff9.png)


<br>


<details>
<summary> 기술적 의사결정 과정 </summary>
<div markdown="1">

### `1. Offset 방식 & MySQL Full Text Search`

- **적용 계기**

상품 데이터 1200만건 기준으로 단순 SQL 쿼리만으로 저조한 응답 시간을 확인함에 따라 개선이 필요하다고 느꼈고, 이를 개선하기 위해 Full Text Search를 도입

- **결과 분석**

Full Text Search를 도입하고 검색에 대한 성능적 개선은 이루어짐

- **문제점**

검색 성능에 대한 개선은 이루어졌으나 **Offset 방식의 페이지네이션**과 **카운트 쿼리**에 대한 성능적 문제는 여전히 남아있는 상태라 구조적인 개편의 필요성을 느낌

<br>
	
### `2. No Offset 방식`

- **적용 계기**

Full Text Search를 도입하고 검색 성능 개선은 이루어졌으나 Offset 방식이 가지던 구조적인 문제로 인해 별도의 차선책이 필요함을 느꼈었고 그에 따른 해결책으로 No Offset 방식을 고려하게 됨

NoOffset 방식은 페이징 버튼이 아닌 더보기 버튼을 사용해야 하기 때문에 순차적인 페이지 이동만 가능하다는 것이 단점이긴 하나 사용자 경험을 고려했을 때 순차적인 페이지 이동이 이루어지더라도 빠른 응답을 받는 것이 적절하다고 생각되어 도입하게 됨

- **결과 분석**

동일한 키워드로 아무런 기술을 적용하지 않은 QueryDSL-JPA와 비교했을 때 약 747%의 성능 개선이 이루어짐.


![image](https://user-images.githubusercontent.com/102216495/207779314-436b115d-c28d-4ede-8f64-fa3a7d2fb62d.png)

![image](https://user-images.githubusercontent.com/102216495/207779335-746a42fb-9213-4da1-a8a1-5950622457d0.png)
<br>

### `Offset이 성능 저하를 발생시키는 이유`

Offset 방식은 데이터베이스의 offset쿼리를 이용해서 페이지 단위로 구분하여 요청/응답하는 방식으로 모든 데이터를 읽어온 후에 순서를 부여해서 offset부터 limit수로 자르는 방법이다.

때문에 페이지 단위가 커질수록 데이터베이스에서 읽어와야 하는 데이터가 많아지기 때문에 속도가 느리다. 

그렇다고 해서 읽어온 모든 데이터를 사용하는 것이 아닌 필요한 페이지의 데이터만큼만 사용하고 나머지는 버리게 된다.

예를 들어 offset 10,000이고 limit 20이라 하면 최종적으로 10,020개의 행을 읽어야 하고 이 중 앞 10,000개 행은 버리게 된다.

즉, 뒤로 갈수록 버려야 하지만 읽어야 할 행의 개수가 많아 점점 뒤로 갈수록 느려지게 된다.

<br>

</div>
</details>

<br>


### 📶 대규모 트래픽 분산

#### **[최종 해결방안]**

**DB Main-Replication 적용으로 서버 부하상황 개선 및 서버 요청처리 시간 (Processing Time) 단축**  ▶ **TPS 개선**

#### **[문제 정의]**

> 상품에 대해 **1초당 1,000명**의 사용자가 주문을 할 경우 **EC2 서버(t2.micro)의 CPU 사용률이 약 100%에 도달하게 되면서 서버가 요청을 받을 수 없는 상태를 확인.** 뿐만 아니라, RDS도 CPU 사용률 100%에 도달하게 되면서 트래픽 분산에 대한 필요성을 느낌

> 쇼핑몰 서비스의 특성상 실시간 요청이 계속해서 이루어지는 상황에서 서버가 부하되어 요청을 받을 수 없는 상태라면 서비스 사용자의 이탈율이 많을 것이라고 생각됨.

> ![image](https://user-images.githubusercontent.com/102216495/207779760-c2b62747-0029-4dcb-b0d1-55213d77b366.png)
> ![image](https://user-images.githubusercontent.com/102216495/207779770-1a896b2c-4a9b-40a5-89e3-dde7c3f6182b.png)
> ![image](https://user-images.githubusercontent.com/102216495/207779777-70afdc3c-7b55-4d90-8200-58eaf4e7f9aa.png)

<br>


<details>
<summary> 기술적 의사결정 과정  </summary>
<div markdown="1">

## **[해결 방법 수집]**

### 1. `서버 Scale-out`

- 서버 1대가 10 TPS의 성능을 가진다고 할 때, 2대라면 20 TPS, 3대면 30 TPS의 성능을 가짐 (10 TPS * 서버 N대)
- 용량, 성능 확장에 한계가 없다. 이로 인해 병렬 처리를 실천하기 쉬워진다.
- 여러 대의 서버를 두어 분산처리를 하기에, 장애 가능성이 감소한다.

**→ 해당 작업으로 인해 포기해야하는 기술적 장점은 발생하지 않으므로 해결 방법에 추가**

<br>


### 2. `서버 Scale-up`

- 더 높은 성능의 서버를 교체하는 작업이기에, 구현과 설계가 쉽다.
- 비용대비 효과가 낮다
- 용량, 성능 확장에 한계가 있다.
- 서버 장애 시, 복구될 때까지 서비스를 중단함으로써 큰 비즈니스 손실이 있을 수 있다.

**→ 서버 Scale-out에 비해 써야하는 메리트가 크게 존재하지 않으므로, 서버 Scale-out을 우선적으로 도입한 뒤 추가 문제 발생 시 해당 방법 재검토 (저예산 상황인 점도 고려)**

<br>


### 3. **`DB 다중화(DB Replication - Main-Replication 패턴)`**

- 추가(Insert) / 수정(Update) / 삭제(Delete) 기능은 **Master DB가 처리**하도록 하고, 조회(Select) 기능은 Slave DB로 처리하게 함으로써 요청에 대한 처리를 병렬적으로 처리하도록 하여 **DB부하 분산 및 처리시간 (Processing Time)을 단축**

**→ DB 부하를 분산시켜야하는 상황임으로, 해결 방법에 추가**

<br>

## **[결과 관찰]**

### 1. `DB Replication (부하 분산)`

    **3초 동안 1,000회 요청을 10회 반복으로 공통테스트 진행**

Throughput      → **5~6% 증가**

Response Time → **전체적 감소**

서버 에러율      → **Error율 0%**

| 종류 | 단일 DB 주문 | 분산 DB 주문 | 단일 DB 조회 | 분산 DB 조회 |
| --- | --- | --- | --- | --- |
| Throughput | 124.9/sec | 132.3/sec | 130.9/sec | 137.4/sec |

</div>
</details>

<br>


### 🤖 로깅 & 모니터링


- **로깅 및 모니터링의 필요성**

    - 개발 진행 중 혹은 개발 후에 발생할 수 있는 **애플리케이션의 문제를 빠르게 진단할 수 있다.**
    - 적재된 로그들을 가시성 좋게 파악할 수 있다.
    - 정확한 데이터 분석 결과를 도출할 수 있다.
    - 성능 혹은 서비스 동작 상태에 관한 통계와 정보를 제공할 수 있다.

<br>

- **문제 해결**

    - logback을 사용하여 서버에 쌓이는 로그를 파일 단위로 저장 후 promatil을 사용하여 서버로 전송하고, 로그를 수신할 수 있는 loki를 사용하여 grafana에서 시각화를 진행
    - prometheus로 metric을 수집 후 빠르게 문제점을 파악하기 위해 **grafana로 시각화 진행**
    
<br>

- **결과**

`prometheus & grafana`
![image](https://user-images.githubusercontent.com/102216495/207780801-9cdc1b5b-50f3-4ca1-a24c-b3102ab29012.png)



`promtail & Loki & grafana`
![image](https://user-images.githubusercontent.com/102216495/207780810-6dc97b37-d1a5-4c1a-a637-08c70ed0fee2.png)

<br>


<br/>

### 🔠 Git Commit 메시지 컨벤션 전략 🔠
   
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
