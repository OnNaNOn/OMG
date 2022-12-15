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

⛳ 시나리오 ⛳

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
Query DSL과 Full Text Search를 활용한 상품 검색 기능 구현


#### 🤡 상품 주문
락 기능을 활용한 동시성 제어

#### 🧑🏻사용자 & 판매자 페이지
사용자가 가진 권한에 따라 특정 페이지 접근 제한 (STANDARD/ADMIN)

- 모든 사용자 > 상품 검색 및 조회
- 로그인한 사용자 (STANDARD) > 상품 주문
- 관리자 권한을 가진 사용자 (ADMIN) > 이벤트 상품 등록

<br/>

### ❌ 트러블슈팅 및 기술적 의사결정 ⭕

[📒노션으로 이동📒](https://www.notion.so/ONO-COM-88223335fb8d4276a89788118b843438#37aba69ac67a41ad92e8ffc835719bcd)

#### 👨‍👩‍👧‍👦 동시성 제어
#### 📈 검색 성능 개선
#### 📶 대규모 트래픽 분산


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
