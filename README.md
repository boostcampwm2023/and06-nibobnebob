# 🍚 니밥내밥 ( NIBOB-NEBOB )
<br>

![Group 2608558](https://github.com/boostcampwm2023/and06-nibobnebob/assets/82799840/31c93c05-b519-4810-b53e-5d6bfe5cfcba)

![Group 2608569 (1)](https://github.com/boostcampwm2023/and06-nibobnebob/assets/82799840/8323d852-f60b-4202-b39f-5eb0595b3e65)


<br>

[👉🏻 wiki 바로가기](https://github.com/boostcampwm2023/and06-nibobnebob/wiki)






<br><br>


## 🤔 주요 개발 과정과 고민 

|주제|설명|
|--|--|
|[**앱 flow**](https://github.com/boostcampwm2023/and06-nibobnebob/wiki/%E3%80%B0%EF%B8%8F-App-%ED%99%94%EB%A9%B4-flow)|각 페이지별 깊은 depth 로 인해 발생하는 소통 문제 해결을 위해 flow 작성.|
|[**권한 허용 여부에 따른 메인화면 flow**](https://github.com/boostcampwm2023/and06-nibobnebob/wiki/%F0%9F%93%8D-%EC%9C%84%EC%B9%98-%EA%B6%8C%ED%95%9C-%ED%97%88%EC%9A%A9%EC%97%90-%EB%94%B0%EB%A5%B8-%EB%A9%94%EC%9D%B8-%ED%99%94%EB%A9%B4-flow)|지도가 핵심 기능인 만큼, 위치 권한과 GPS ON 여부에 따라 메인 화면의 flow 가 달라진다.|
|[**사용자 경험을 고려한 지도 필터간 전환**](https://github.com/boostcampwm2023/and06-nibobnebob/wiki/%5BK011%5D-%EB%85%B8%EA%B7%A0%EC%9A%B1#%EB%84%A4%EC%9D%B4%EB%B2%84%EC%A7%80%EB%8F%84-%EC%82%AC%EC%9A%A9%EC%9E%90-%EA%B2%BD%ED%97%98-%EA%B0%9C%EC%84%A0%ED%95%98%EA%B8%B0)|필터에 따라 다르게 보여지는 지도와 맛집 마커를 전환하는 과정에서 사용자 경험을 고려했다.|
|[**왜 공공 API를 선택했나?**](https://github.com/boostcampwm2023/and06-nibobnebob/wiki/%E2%9C%92-%5BBE%5D-%EA%B0%9C%EB%B0%9C%EC%9D%BC%EC%A7%80#%EA%B3%B5%EA%B3%B5-api-%EC%84%A0%ED%83%9D-%EC%9D%B4%EC%9C%A0)|근처의 모든 음식점을 가져올 때, 네이버/카카오 api 가 아닌 공공 api 를 선택한 이유?|
|[**왜 이미지를 리사이징 하나?**](https://github.com/boostcampwm2023/and06-nibobnebob/wiki/%E2%9C%92-%5BBE%5D-%EA%B0%9C%EB%B0%9C%EC%9D%BC%EC%A7%80#%EC%9D%B4%EB%AF%B8%EC%A7%80-%EB%A6%AC%EC%82%AC%EC%9D%B4%EC%A7%95)|Object Storage에 이미지를 업로드 할 때, 리사이징 하는 이유?|
|[**어플리케이션 통신 과정**](https://github.com/boostcampwm2023/and06-nibobnebob/wiki/%E2%9C%92-%5BBE%5D-%EA%B0%9C%EB%B0%9C%EC%9D%BC%EC%A7%80#%EC%96%B4%ED%94%8C%EB%A6%AC%EC%BC%80%EC%9D%B4%EC%85%98-%ED%86%B5%EC%8B%A0-%EA%B3%BC%EC%A0%95)|사용자의 요청부터 서버의 응답까지 어플리케이션 통신 과정 설계 이유|

<br>

📍 AOS 기술 선택 이유 바로가기 

📍 [BE 기술 선택 이유 바로가기](https://github.com/boostcampwm2023/and06-nibobnebob/wiki/%E2%9C%92-%5BBE%5D-%EA%B0%9C%EB%B0%9C%EC%9D%BC%EC%A7%80#%EA%B8%B0%EC%88%A0%EC%A0%81-%EB%8F%84%EC%A0%84)

<br><br>

## 📲 주요 기능 동작 화면

### 로그인 / 회원가입
일반 회원가입|네이버 회원가입|
|------|---|
<img src="https://github.com/boostcampwm2023/and06-nibobnebob/assets/82799840/ec04d729-eae4-4135-a6c4-b0868dce0e4e" width="390" heigth="1200">|<img src="https://github.com/boostcampwm2023/and06-nibobnebob/assets/82799840/55210733-f31d-44a4-a5fe-6127ec49b63a" width="400" heigth="1200">

<br>

### 홈화면
홈 지도|위치 트래킹|홈 검색|
|------|---|---|
|<img src="https://github.com/boostcampwm2023/and06-nibobnebob/assets/82799840/eba76f0a-6746-4076-9e8a-5c9a7b1020ca" width="300" heigth="1200">|<img src="https://github.com/boostcampwm2023/and06-nibobnebob/assets/82799840/e2106336-33da-4fe0-af90-b1ac9b2dbc71" width="300" heigth="1200">|<img src="https://github.com/boostcampwm2023/and06-nibobnebob/assets/82799840/f8559be6-3f5d-41ea-82c0-379c00d4ec60" width="300" heigth="1200">|

<br>

### 팔로우

유저 추천 및 팔로우|지역으로 유저 검색|유저 검색 및 유저상세|
|------|---|---|
|<img src="https://github.com/boostcampwm2023/and06-nibobnebob/assets/82799840/c621d20c-a16e-4bdb-b476-815f59f549c3" width="300" heigth="1200">|<img src="https://github.com/boostcampwm2023/and06-nibobnebob/assets/82799840/5090676a-d248-4260-a2c9-891f5d892a7b" width="300" heigth="1200">|<img src="https://github.com/boostcampwm2023/and06-nibobnebob/assets/82799840/2eba3ddb-b718-417e-9699-55f4298034a6" width="300" heigth="1200">|

<br>

### 맛집 등록하기 및 상세보기

맛집 등록하기|맛집 상세보기|위시리스트 추가하기|
|------|---|---|
|<img src="https://github.com/boostcampwm2023/and06-nibobnebob/assets/82799840/21c15f01-b7d1-4183-ad28-07ec79bacf45" width="300" heigth="1200">|<img src="https://github.com/boostcampwm2023/and06-nibobnebob/assets/82799840/a161a1d6-2a53-47a3-b1a2-c6b9d3e9b722" width="300" heigth="1200">|<img src="https://github.com/boostcampwm2023/and06-nibobnebob/assets/82799840/bcf0a4ec-eb0b-4abf-be24-ba836f8886a2" width="300" heigth="1200">|

<br>

### 마이페이지
프로필 수정|나의 위시리스트|내 맛집 리스트
|------|---|---|
|<img src="https://github.com/boostcampwm2023/and06-nibobnebob/assets/82799840/0086ff34-34db-483c-86bd-77a6a098e311" width="300" heigth="1200">|<img src="https://github.com/boostcampwm2023/and06-nibobnebob/assets/82799840/614b9d5e-0e1f-4252-abea-d1393bd2db00" width="300" heigth="1200">|<img src="https://github.com/boostcampwm2023/and06-nibobnebob/assets/82799840/ecbe43e7-8e49-4eb7-96cf-ba1b119022d3" width="260" heigth="1200">|

<br><br>

## 📚 기술 스택
- Android
- Backend

| 분류 | 사용 기술|
|-- | --|
|서버 프레임워크 | NestJS|
|프로그래밍 언어 | TypeScript|
|테스트 | Jest, Docker, Apache Jmeter|
|로깅 | Winston|
|DB | PostgreSQL, TypeORM|
|웹 서버 | NginX|
|클라우드 컴퓨팅 | Naver Cloud Platform|
|이미지 저장 | Object Storage, multer, sharp|
|CI/CD | GitHub Action, Docker|


<br><br>

## 👬 팀 소개

📍 협업 문서

[✔️ 그라운드 룰](https://github.com/boostcampwm2023/and06-nibobnebob/wiki/%F0%9F%93%8D-Team-Ground-Rule)

[✔️ 플로우차트](https://github.com/boostcampwm2023/and06-nibobnebob/wiki/%F0%9F%8F%97%EF%B8%8F-%EA%B8%B0%ED%9A%8D-%EB%B0%8F-%EC%84%A4%EA%B3%84#flowchart)
[✔️ 와이어프레임](https://www.figma.com/file/5R3cLTWCB1L2ukpthS6rgJ?embed_host=notion&kind=&mode=design&node-id=0-1&t=FrunV2RXXGml5ylf-0&type=design&viewer=1)
[✔️ Feature List](https://docs.google.com/spreadsheets/d/1z26deo1rmjP9H2LibTZikfhk6sTTP5gZ1F50FWlACFU/edit#gid=399729850)
[✔️ Back Log](https://docs.google.com/spreadsheets/d/1z26deo1rmjP9H2LibTZikfhk6sTTP5gZ1F50FWlACFU/edit#gid=1396326280)


<br>

📍 팀원

| <img src="https://github.com/BENDENG1.png?size=70" width="70" height="70" alt="user1"/> |<img src="https://github.com/plashdof.png?size=70" width="70" height="70" alt="user2"/> | <img src="https://github.com/yy0ung.png?size=70" width="70" height="70" alt="user3"/> | <img src="https://github.com/LeeTH916.png?size=70" width="70" height="70" alt="user4"/> | <img src="https://github.com/GeunH.png?size=70" width="70" height="70" alt="user5"/> |
|:---:|:---:|:---:|:---:|:---:|
| [K011_노균욱](https://github.com/BENDENG1) | [K015_박진성](https://github.com/plashdof) | [K024_오세영](https://github.com/yy0ung) | [J123_이태훈](https://github.com/LeeTH916) | [J155_최근혁](https://github.com/GeunH) |

<br>

