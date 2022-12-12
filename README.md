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
### 1. synchronized

- synchronized를 사용하는 이유는 하나의 객체에 동시에 접근해 처리하는 것을 막기 위해 사용된다. 
다시 말해, synchronized 키워드가 붙은 메서드 블럭은 하나의 스레드만 접근할 수 있도록 하여 스레드의 불규칙적인 자원 공유(Race Condition)를 막기 위해 사용한다.

그러나, `synchronized`와 `Transactional`을 함께 사용한다면 원하는 대로 동작하지 않을 수도 있다. 
그 이유는 @Transactional은 프록시 객체를 생성하기 위해 AOP로 동작되기 때문이다.

```
TransactionStatus status = transactionManager.getTransaction(..);

try {
	target.logic(); // public synchronized void decrease 메서드 수행
    // logic 수행 이후 트랜잭션 종료 전에 다른 쓰레드가 decrease에 접근!
    transactionManager.commit(status);
} catch (Exception e) {
	transactionManager.rollback(status);
    throw new IllegalStateException(e); 
}
```

`Transaction`과 `synchronized`를 동시에 적용하게 될 경우 한 쓰레드가 `synchronized`가 붙은 메서드를 호출하여 수행한 이후, 트랜잭션이 종료되기 전에 다른 쓰레드가 `synchronized`가 붙은 메서드에 접근을 하여 커밋되지 않는 데이터에 접근하게 되기 때문에 원하는 결과를 얻을 수 없다.

뿐만 아니라, 서버가 여러 대 있다고 가정한다면 synchronized의 사용만으로는 **각 서버마다 요청하는 쓰레드에 대해 정합성이 맞지 않는 문제를 해결할 수 없다.**

그렇다고 해서 `Transactional`을 사용하지 않는다면, 재고는 감소되었지만 주문을 생성하는 과정에서 에러가 발생하게 되면 감소시킨 상품에 대해 재고를 채워놔야 하는 문제점이 생기게 된다. 그래서 주문 처리의 로직이 정상적으로 처리가 되면 `Commit`을 시켜주고, 처리 중 에러가 발생하게 되면 자동으로 `Rollback`을 시켜주는 `Transaction`을 붙여주어야 한다. 

해당 문제는 데이터베이스의 락을 통해 `Race Condition`문제를 해결해야한다.

### 2. Redis
Redis는 Single Thread 기반이기도 하고, 락 정보가 간단한 휘발성 데이터에 가깝기 때문에 가장 좋은 선택지이다. 

또한, Redis에서 분산 락을 구현한 알고리즘으로 redlock이라는 것을 제공하는데 자바에서는 redlock의 구현체로 **Lettuce**와 **redisson**를 제공한다.

Redis의 분산 락 구현체인 두 Redis Client는 **Netty를 사용하여 non-blocking I/O 로 동작**된다.

### 1. `Lettuce`

Lettuce는 기본적으로 락 기능을 제공하고 있지 않기 때문에 락을 잡기 위해 `setnx`명령어를 이용해서 사용자가 직접 스핀 락 형태(Polling 방식)로 구현해야 하는 단점이 존재하는데, 이로인해 락을 잡지 못하면 끊임없이 루프를 돌며 재시도를 하게 되고, 이는 레디스에 부하를 줄 수 있다고 판단했다.

또한, `setnx`명령어는 `expire time`을 지정할 수 없기 때문에 락을 잡고 있는 서버가 다운 되었을 경우 락을 해제하지 못하는 문제점(**DeadLock 발생 가능성**)을 가지고 있다.

### 2. `Redisson`

Redisson은 여러 독립된 프로세스에서 하나의 자원을 공유해야 할 때, 데이터에 결함이 발생하지 않도록 하기 위해서 **분산 락을 제공**해준다. 

**Redisson은 락을 잡는 방식이 스핀락 방식이 아니고, `expire time`도 적용할 수 있다**는 특징을 갖는다. 

또한, Redisson은 **pub/sub 방식을 사용**하여 락이 해제될 때마다 subscribe하는 클라이언트들에게 "이제 락 획득을 시도해도 좋다"는 알림을 주는 구조라 스핀락보다 훨씬 적은 부담을 준다.

**그러나 Redis의 락 방식을 도입하지 않은 이유는 pub/sub을 통한 lock 획득 시도 과정에서 발생하는 네트워크 지연으로 인해 비관적 락에 비해 속도가 늦었고, 현재 Redis를 도입하지 않았을 뿐더러 Redisson 도입만을 위해 Redis를 적용해야 한다는 점에서 오버 엔지니어링, 러닝 커브 등을 고려했을 때 효율적이지 못하다고 판단되어 비관적 락 처리하도록 구현함.**

### 3. Mysql
MySQL에서 제공하는 Lock의 종류는 크게 Optimistic Lock, Pessimistic Lock이 존재한다.

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
<details>
<summary> 1. QueryDSL </summary>
<div markdown="1">

* 적용 계기

향후 필터 또는 정렬 조건이 추가될 것을 고려하여 동적 쿼리 작성을 위해 QueryDSL 도입

- **문제점**

QueryDSL만으로는 필터나 정렬 조건에 대한 처리율과 카운트 쿼리에 대한 처리율이 상당히 저조했음 (**수치는 추후 기입 예정**)
</div>
</details>

<details>
<summary>2. Offset 방식 & MySQL Full Text Search</summary>
<div markdown="1">

* 적용 계기

기존 QueryDSL로 작성한 쿼리만으로는 동적 쿼리에 대한 조건에 대해서 빠른 응답을 받을 수 없었고 이에 대한 해결책으로 MySQL의 Full Text Search를 도입하게 됨

- **결과 분석**
    - **개선된 부분**
        - 동일한 키워드로 QueryDSL과 비교했을 때 약 N%의 성능 개선이 이루어졌다.
    - **추가 개선이 필요한 부분**
        - 상품 1,700만개 기준으로 검색에 대한 응답 시간은 빠르나, 페이지네이션 또는 카운트 쿼리에 대해서는 지연율이 발생함을 발견함
</div>
</details>

<details>
<summary>3. Offset 방식 & MySQL Full Text Search & Covering Index</summary>
<div markdown="1">

* 적용 계기

기존에 가지던 페이지네이션과 카운트 쿼리에 대한 응답율이 저조함을 확인함에 따라 개선이 필요했고 이를 개선할 수 있는 커버링 인덱스를 도입
    
- **결과 분석**
    - 개선된 부분
        - 카운트 쿼리의 도입으로 Offset 방식이 가지는 단점에 대해서는 성능 향상이 이루어짐
    - 추가 개선이 필요한 부분
        - 전체 카운트 쿼리에 대해서 Offset 방식이 가지는 구조적인 문제로 인해 성능 저하를 발생한다는 사실을 알게 됨
    
- **Offset이 성능 저하를 발생시키는 이유**
    
    : offset을 사용하게 되면 offset + limit만큼의 데이터를 읽은 후 offset만큼의 데이터를 버림
    
    ⇒ 마지막 페이지로 갈수록 읽어야하는 데이터 수가 비약적으로 증가
</div>
</details>

<details>
<summary>4. No Offset 방식 </summary>
<div markdown="1">

* 적용 계기

커버링 인덱싱으로 Offset 방식이 가지는 문제는 해결이 되었으나 카운트 쿼리에 대한 문제나 정렬에 대한 문제는 해결되지 않음

- 결과 분석
    - 개선된 부분
        - 기존 Offset 방식이 가지던 정렬, 카운트 쿼리에 대한 문제가 해결됨에 따라 최대 N%의 성능 향상이 이루어짐
    
    NoOffset 방식의 경우 페이징버튼이 아닌 ‘more(더보기)’ 버튼을 사용해야 하기 때문에 순차적인 페이지 이동만 가능하다는 것이 단점이긴 하나 사용자 경험을 고려했을 때 순차적인 페이지 이동이 이루어지더라도 빠른 응답을 받는 것이 적절하다고 생각되어 도입하게 됨
</div>
</details>

* > 결과

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
