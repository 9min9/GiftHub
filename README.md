# GiftHub


# GiftHub Web Page v2.0
> **KOSTA 266기 최종 프로젝트** <br/> **개발기간: 2023.11 ~ 2023.12**


## 프로젝트 소개
- 기프티콘을 서비스에 등록하여 통합관리를 해주는 서비스. 바코드로 결제가 가능한 모바일 상품권을 대상으로 함. 3사(카카오톡, kt기프티쇼, sk기프티콘)로 나뉘어진 '기프티콘'들의 유효기간등을 통합관리하고자 하였음.


#### Service for registering and centrally managing gift certificates in an integrated manner. 
- Targeting mobile gift cards that support barcode payments, the platform focuses on unifying the management of gift certificates from three major providers (KakaoTalk, KT Gift Show, SK Gifticon). The goal is to streamline the management of validity periods and other aspects of these 'gifticons'.


#### 


## Stacks 🐈

### Environment
![intellij](https://img.shields.io/badge/intellij-000000?style=for-the-badge&logo=intellijidea&logoColor=white)
![Visual Studio Code](https://img.shields.io/badge/Visual%20Studio%20Code-007ACC?style=for-the-badge&logo=Visual%20Studio%20Code&logoColor=white)
![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=Git&logoColor=white)
![Github](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=GitHub&logoColor=white)             


[//]: # (### Config)

[//]: # (![npm]&#40;https://img.shields.io/badge/npm-CB3837?style=for-the-badge&logo=npm&logoColor=white&#41;        )


[//]: # (### Front-end)

[//]: # (![HTML]&#40;https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=HTML5&logoColor=white&#41;)

[//]: # (![Bootstrap]&#40;https://img.shields.io/badge/Bootstrap-7952B3?style=for-the-badge&logo=Bootstrap&logoColor=white&#41;)

[//]: # (![JavaScript]&#40;https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=Javascript&logoColor=white&#41;)

[//]: # (![React]&#40;https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB&#41;)

[//]: # (![Thymeleaf]&#40;https://img.shields.io/badge/Thymeleaf-005E86?style=for-the-badge&#41;)

[//]: # (![jQuery]&#40;https://img.shields.io/badge/jQuery-0769AD?style=for-the-badge&logo=jQuery&logoColor=white&#41;)


### Back-end
![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=Java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1.5.RELEASE-green?style=for-the-badge&logo=Spring&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-3.1.5.RELEASE-green?style=for-the-badge&logo=Spring&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-3.1.5.RELEASE-green?style=for-the-badge&logo=Spring&logoColor=white)
![QueryDSL](https://img.shields.io/badge/QueryDSL-5.0-green?style=for-the-badge&logo=Java&logoColor=white)

### DataBase

![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15.3-336791?style=for-the-badge&logo=PostgreSQL&logoColor=white)



### Communication
![Notion](https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=Notion&logoColor=white)
![Discord](https://img.shields.io/badge/Discord-5865F2?style=for-the-badge&logo=Discord&logoColor=white)

### Server
![AWS](https://img.shields.io/badge/AWS-232F3E?style=for-the-badge&logo=amazonaws&logoColor=white)
![AWS](https://img.shields.io/badge/AWS_S3-569A31?style=for-the-badge&logo=amazons3&logoColor=white)
![AWS](https://img.shields.io/badge/AWS_RDS-527FFF?style=for-the-badge&logo=amazonrds&logoColor=white)
![AWS](https://img.shields.io/badge/AWS_EC2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white)

AWS EC2 S3 


---
## 주요 기능 🎁

### 🛒 소셜 회원가입 및 Jwt토큰을 사용하여 보안강화
- 소셜 로그인 시 API를 통해 받은 토큰을 사용하여 프로젝트 자체의 JWT를 구현
- 소셜 로그인의 인증 토큰을 사용하지 않으므로 보안부담 감소

### 🛒 기프티콘 등록 및 거래, 선물
- 카카오 챗봇, 파일로 등록시 Naver Cloud Ocr을 호출하여 db의 상품과 mapping
- 기프티콘 등록 시 DB의 Product 테이블에 존재하는 상품이면 바로 기프티콘 등록, 존재하지 않는 상품이면 관리자의 검수를 통해 등록 처리
- 등록 처리된 기프티콘은 판매 가능
- 카카오 페이를 통해 포인트 충전. 충전된 포인트로 기프티콘을 구매

### 🛒 기프티콘 목록 및 사용
- 기프티콘으로 등록되면 입력했던 사진은 보안상 이유로 서버에서 삭제
- 사용 시 기프티콘 바코드 이미지를 생성하고 SMS를 통해 전송

### 관리자
- 검수 상태의 기프티콘을 확인하여 검수 완료 및 거절

---
## 아키텍쳐

### 디렉토리 구조
```bash
├── README.md
├── package-lock.json
├── package.json
├── strapi-backend : 
│   ├── README.md
│   ├── api : db model, api 관련 정보 폴더
│   │   ├── about
│   │   ├── course
│   │   └── lecture
│   ├── config : 서버, 데이터베이스 관련 정보 폴더
│   │   ├── database.js
│   │   ├── env : 배포 환경(NODE_ENV = production) 일 때 설정 정보 폴더
│   │   ├── functions : 프로젝트에서 실행되는 함수 관련 정보 폴더
│   │   └── server.js
│   ├── extensions
│   │   └── users-permissions : 권한 정보
│   ├── favicon.ico
│   ├── package-lock.json
│   ├── package.json
│   └── public
│       ├── robots.txt
│       └── uploads : 강의 별 사진
└── voluntain-app : 프론트엔드
    ├── README.md
    ├── components
    │   ├── CourseCard.js
    │   ├── Footer.js
    │   ├── LectureCards.js
    │   ├── MainBanner.js : 메인 페이지에 있는 남색 배너 컴포넌트, 커뮤니티 이름과 슬로건을 포함.
    │   ├── MainCard.js
    │   ├── MainCookieCard.js
    │   ├── NavigationBar.js : 네비게이션 바 컴포넌트, _app.js에서 공통으로 전체 페이지에 포함됨.
    │   ├── RecentLecture.js
    │   └── useWindowSize.js
    ├── config
    │   └── next.config.js
    ├── lib
    │   ├── context.js
    │   └── ga
    ├── next.config.js
    ├── package-lock.json
    ├── package.json
    ├── pages
    │   ├── _app.js
    │   ├── _document.js
    │   ├── about.js
    │   ├── course
    │   ├── index.js
    │   ├── lecture
    │   ├── newcourse
    │   ├── question.js
    │   └── setting.js
    ├── public
    │   ├── favicon.ico
    │   └── logo_about.png
    └── styles
        └── Home.module.css

```

<!--
```bash
├── README.md : 리드미 파일
│
├── strapi-backend/ : 백엔드
│   ├── api/ : db model, api 관련 정보 폴더
│   │   └── [table 이름] : database table 별로 분리되는 api 폴더 (table 구조, 해당 table 관련 api 정보 저장)
│   │       ├── Config/routes.json : api 설정 파일 (api request에 따른 handler 지정)
│   │       ├── Controllers/ [table 이름].js : api controller 커스텀 파일
│   │       ├── Models : db model 관련 정보 폴더
│   │       │   ├── [table 이름].js : (사용 X) api 커스텀 파일
│   │       │   └── [table 이름].settings.json : model 정보 파일 (field 정보)
│   │       └─── Services/ course.js : (사용 X) api 커스텀 파일
│   │ 
│   ├── config/ : 서버, 데이터베이스 관련 정보 폴더
│   │   ├── Env/production : 배포 환경(NODE_ENV = production) 일 때 설정 정보 폴더
│   │   │   └── database.js : production 환경에서 database 설정 파일
│   │   ├── Functions : 프로젝트에서 실행되는 함수 관련 정보 폴더
│   │   │   │   ├── responses : (사용 X) 커스텀한 응답 저장 폴더
│   │   │   │   ├── bootstrap.js : 어플리케이션 시작 시 실행되는 코드 파일
│   │   │   │   └── cron.js : (사용 X) cron task 관련 파일
│   │   ├── database.js : 기본 개발 환경(NODE_ENV = development)에서 database 설정 파일
│   │   └── server.js : 서버 설정 정보 파일
│   │  
│   ├── extensions/
│   │   └── users-permissions/config/ : 권한 정보
│   │ 
│   └── public/
│       └── uploads/ : 강의 별 사진
│
└── voluntain-app/ : 프론트엔드
    ├── components/
    │   ├── NavigationBar.js : 네비게이션 바 컴포넌트, _app.js에서 공통으로 전체 페이지에 포함됨.
    │   ├── MainBanner.js : 메인 페이지에 있는 남색 배너 컴포넌트, 커뮤니티 이름과 슬로건을 포함.
    │   ├── RecentLecture.js : 사용자가 시청 정보(쿠키)에 따라, 현재/다음 강의를 나타내는 컴포넌트 [호출: MainCookieCard]
    │   ├── MainCookieCard.js : 상위 RecentLecture 컴포넌트에서 전달받은 props를 나타내는 레이아웃 컴포넌트.
    │   ├── MainCard.js : 현재 등록된 course 정보를 백엔드에서 받아서 카드로 나타내는 컴포넌트 [호출: CourseCard]
    │   └── CourseCard.js : 상위 MainCard 컴포넌트에서 전달받은 props를 나타내는 레이아웃 컴포넌트
    │
    ├── config/
    │   └── next.config.js
    │
    ├── lib/
    │   └── ga/
    │   │   └── index.js
    │   └── context.js
    │
    ├── pages/
    │   ├── courses/
    │   │   └── [id].js : 강의 페이지
    │   ├── _app.js : Next.js에서 전체 컴포넌트 구조를 결정, 공통 컴포넌트(navbar, footer)가 선언되도록 customizing 됨.
    │   ├── _document.js : Next.js에서 전체 html 문서의 구조를 결정, lang 속성과 meta tag가 customizing 됨.
    │   ├── about.js : 단체 소개 페이지
    │   ├── index.js : 메인 페이지
    │   ├── question.js : Q&A 페이지
    │   └── setting.js : 쿠키, 구글 애널리틱스 정보 수집 정책 페이지
    │
    ├── public/
    │   ├── favicon.ico : 네비게이션바 이미지
    │   └── logo_about.png : about 페이지 로고 이미지
    │
    └── styles/
        └── Home.module.css

```
-->

---
## 화면 구성 📺
| 메인 페이지  |  소개 페이지   |
| :-------------------------------------------: | :------------: |
|  <img width="329" src=""/> |  <img width="329" src=""/>|  
| 강좌 소개 페이지   |  강의 영상 페이지   |  
| <img width="329" src=""/>   |  <img width="329" src=""/>     |

---
