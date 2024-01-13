# GiftHub Backend Server

> **KOSTA 266기 최종 프로젝트** <br/> **개발기간: 2023.11 ~ 2023.12** <br/> 팀원 : 4명

---

## 프로젝트 소개

- 기프티콘을 등록하여 사용하는 통합관리를 서비스. 
- 바코드로 결제가 가능한 모바일 상품권을 대상으로 함. 3사(카카오톡, kt기프티쇼, sk기프티콘)
- 웹 프로젝트의 백엔드 서버

👉 [Frontend Server](https://github.com/9min9/Gifthub-Client)


---


## Stacks 🐈

### Environment

![intellij](https://img.shields.io/badge/intellij-000000?style=for-the-badge&logo=intellijidea&logoColor=white)
![Visual Studio Code](https://img.shields.io/badge/Visual%20Studio%20Code-007ACC?style=for-the-badge&logo=Visual%20Studio%20Code&logoColor=white)
![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=Git&logoColor=white)
![Github](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=GitHub&logoColor=white)

### Back-end

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=Java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1.5.RELEASE-green?style=for-the-badge&logo=Spring&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-3.1.5.RELEASE-green?style=for-the-badge&logo=Spring&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-3.1.5.RELEASE-green?style=for-the-badge&logo=Spring&logoColor=white)
![QueryDSL](https://img.shields.io/badge/QueryDSL-5.0-green?style=for-the-badge&logo=Java&logoColor=white)

### DataBase

![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15.3-336791?style=for-the-badge&logo=PostgreSQL&logoColor=white)

### Server

![AWS](https://img.shields.io/badge/AWS_EC2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white)
![AWS](https://img.shields.io/badge/AWS_S3-569A31?style=for-the-badge&logo=amazons3&logoColor=white)
![AWS](https://img.shields.io/badge/AWS_RDS-527FFF?style=for-the-badge&logo=amazonrds&logoColor=white)


### Api
[![Kakao Login API](https://img.shields.io/badge/Kakao%20Login%20API-FFCD00?style=for-the-badge&logo=kakao&logoColor=black)](https://developers.kakao.com/docs/latest/ko/kakaologin/)
[![Naver Login API](https://img.shields.io/badge/Naver%20Login%20API-1EC800?style=for-the-badge&logo=naver&logoColor=white)](https://developers.naver.com/docs/login/api/)
[![Kakao Pay API](https://img.shields.io/badge/Kakao%20Pay%20API-FFCD00?style=for-the-badge&logo=kakao&logoColor=black)](https://developers.kakao.com/docs/latest/ko/kakaopay)
[![Naver OCR API](https://img.shields.io/badge/Naver%20OCR%20API-1EC800?style=for-the-badge&logo=naver&logoColor=white)](https://www.ncloud.com/product/aiService/ocr)
[![CoolSMS API](https://img.shields.io/badge/CoolSMS%20API-5B9BD5?style=for-the-badge&logo=coolpad&logoColor=white)](https://www.coolsms.co.kr/)





### Communication

![Notion](https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=Notion&logoColor=white)
![Discord](https://img.shields.io/badge/Discord-5865F2?style=for-the-badge&logo=Discord&logoColor=white)

---


## 주요 기능 🎁

### 🛒 소셜 회원가입 및 Jwt 사용
- 소셜 로그인 시 API를 통해 받은 정보를 사용하여 JWT를 구현
- 소셜 로그인의 API의 인증 토큰을 사용하지 않으므로 보안부담 감소

### 🛒 기프티콘 등록 및 사용
- 카카오 챗봇, 파일로 등록시 Naver Cloud Ocr을 사용하여 읽은 값을 DB의 Product 테이블을 통해 검증
- 기프티콘 등록 시 DB의 Product 테이블에 존재하는 상품이면 바로 기프티콘 등록, 존재하지 않는 상품이면 관리자의 검수를 통해 등록 처리
- 기프티콘으로 등록되면 입력했던 사진은 보안상 이유로 서버에서 삭제
- 사용 시 기프티콘 바코드 이미지를 생성하고 SMS를 통해 전송

### 🛒 기프티콘 거래 및 포인트 충전
- 등록 처리된 기프티콘은 판매 가능
- 카카오 페이를 통해 포인트 충전, 충전된 포인트로 기프티콘을 구매

### 관리자
- Product DB을 초기화 기프티콘 API excel 파일을 통해 Product DB에 상품을 등록
- 검수 상태의 기프티콘을 확인하여 검수 완료 및 거절

---

## 기능 구현

### 회원가입 로그인
- 회원가입, 로그인 CRUD 구현
- 동적 입력 검증을 위한 Controller 작성
- Kakao 로그인, Naver 로그인 API를 사용하여 OAuth2 인증
- 로그인 시 JWT를 발급하여 header 방식으로 사용자를 검증

👉 [자세히 보기](https://github.com/9min9/GiftHub/wiki/%ED%9A%8C%EC%9B%90%EA%B0%80%EC%9E%85,-%EB%A1%9C%EA%B7%B8%EC%9D%B8)


### 기프티콘 등록 및 사용
- 이미지 URL 또는 파일을 받아 기프티콘을 등록
- 등록 과정에서 OCR을 통해 기프티콘 이미지의 텍스트를 추출
- 다양한 형식의 기프티콘의 텍스트를 추출하기 위해 기프티콘의 브랜드와 Product DB의 brand와 값을 매칭
- 등록 과정에서 오류는 관리자의 검수를 통해 처리 가능
- 기프티콘을 사용 시 바코드 값을 통해 바코드 이미지를 생성하여 SMS로 전송

👉 [자세히 보기](https://github.com/9min9/GiftHub/wiki/%EA%B8%B0%ED%94%84%ED%8B%B0%EC%BD%98-%EB%93%B1%EB%A1%9D-%EB%B0%8F-%EC%82%AC%EC%9A%A9)


### 기프티콘 거래 및 포인트 충전
- 카카오 페이 API를 사용하여 포인트를 충전
- 판매중인 기프티콘을 구매 시 기프티콘의 소유권을 변경

👉 [자세히 보기](https://github.com/9min9/GiftHub/wiki/%EA%B8%B0%ED%94%84%ED%8B%B0%EC%BD%98-%EA%B1%B0%EB%9E%98-%EB%B0%8F-%ED%8F%AC%EC%9D%B8%ED%8A%B8-%EC%B6%A9%EC%A0%84)

---


## 아키텍쳐


### 에러 처리

- @Valid, Validator를 사용하여 상황에 따른 사용자 입력값을 검증
- 사용자 정의 Exception을 만들어 Exception을 처리
- error.properties를 MessageSource Bean으로 등록하여 에러 메세지를 출력

```java

@Data @Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {
    @NotBlank @Email
    private String email;
    @NotBlank @Pattern(regexp = "^(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,20}$")
    private String password;
    @NotBlank
    private String confirmPassword;
    ...

```

```java

@Component
public class GifticonAppovalValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return GifticonAppovalRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        GifticonAppovalRequest request = (GifticonAppovalRequest) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "storageId", "NotBlank");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "productName", "NotBlank");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "brandName", "NotBlank");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "due", "NotBlank");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "barcode", "NotBlank");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "price", "NotNull");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "category", "NotSelect");
    }
}

```

```java

    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:/errors");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

```

👉 [자세히 보기](https://github.com/9min9/GiftHub/wiki/%EC%97%90%EB%9F%AC%EC%B2%98%EB%A6%AC)


---


### 테이블 관계도
![Table](https://github.com/9min9/GiftHub/assets/130825350/2fa9bd9a-0311-4ffb-a998-4bead43b2210)

<details><summary>프로젝트 구조</summary>

<div markdown="1">

```

src
 ┣ main
 ┃ ┣ generated
 ┃ ┣ java
 ┃ ┃ ┣ com
 ┃ ┃ ┃ ┣ gifthub
 ┃ ┃ ┃ ┃ ┣ admin
 ┃ ┃ ┃ ┃ ┃ ┣ controller
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ AdminController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ AdminPageController.java
 ┃ ┃ ┃ ┃ ┃ ┣ dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ GifticonAppovalRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ StorageAdminListDto.java
 ┃ ┃ ┃ ┃ ┃ ┣ exception
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ validator
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ GifticonAppovalValidator.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ NotSelectConfirmFlagException.java
 ┃ ┃ ┃ ┃ ┃ ┗ service
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ AdminService.java
 ┃ ┃ ┃ ┃ ┣ cart
 ┃ ┃ ┃ ┃ ┃ ┣ controller
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ CartController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ CartPageController.java
 ┃ ┃ ┃ ┃ ┃ ┣ dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ CartDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ CartRequestDto.java
 ┃ ┃ ┃ ┃ ┃ ┣ entity
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ Cart.java
 ┃ ┃ ┃ ┃ ┃ ┣ repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ CartRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ CartRepositoryImpl.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ CartRepositorySupport.java
 ┃ ┃ ┃ ┃ ┃ ┗ service
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ CartService.java
 ┃ ┃ ┃ ┃ ┣ chatbot
 ┃ ┃ ┃ ┃ ┃ ┣ controller
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ ChatbotPageController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ KakaoChatbotController.java
 ┃ ┃ ┃ ┃ ┃ ┗ util
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ JsonConverter.java
 ┃ ┃ ┃ ┃ ┣ config
 ┃ ┃ ┃ ┃ ┃ ┣ ImgServer
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ S3Config.java
 ┃ ┃ ┃ ┃ ┃ ┣ jwt
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ JwtAuthenticationFilter.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ JwtContext.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ KakaoAuthenticationProvider.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ LocalUserAuthenticationProvider.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ NaverAuthenticationProvider.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ SocialAuthenticationToken.java
 ┃ ┃ ┃ ┃ ┃ ┣ security
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ CorsConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ SecurityConfig.java
 ┃ ┃ ┃ ┃ ┃ ┣ Config.java
 ┃ ┃ ┃ ┃ ┃ ┗ MessageConfig.java
 ┃ ┃ ┃ ┃ ┣ event
 ┃ ┃ ┃ ┃ ┃ ┗ attendance
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ controller
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ AttendanceController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ AttendancePageController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ AttendanceDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ entity
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ Attendance.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ exception
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ DuplicateAttendanceException.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ FailedAttendanceException.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ AttendanceRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ AttendanceRepositoryImpl.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ AttendanceRepositorySupport.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ service
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ AttendanceService.java
 ┃ ┃ ┃ ┃ ┣ gifticon
 ┃ ┃ ┃ ┃ ┃ ┣ controller
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ GifticonController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ GifticonPageController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ StorageController.java
 ┃ ┃ ┃ ┃ ┃ ┣ dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ storage
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ GifticonStorageDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ BarcodeImageDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ GifticonDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ GifticonImageDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ GifticonQueryDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ GifticonRegisterRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ GifticonSearchCond.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ GifticonStorageListDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ ImageSaveDto.java
 ┃ ┃ ┃ ┃ ┃ ┣ entity
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ BarcodeImage.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ Gifticon.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ GifticonImage.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ GifticonStorage.java
 ┃ ┃ ┃ ┃ ┃ ┣ enumeration
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ GifticonStatus.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ MovementStatus.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ RegistrationFailureReason.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ StorageStatus.java
 ┃ ┃ ┃ ┃ ┃ ┣ exception
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ NotEmptyBrandNameException.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ NotEmptyDueException.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ NotEmptyPriceException.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ NotExpiredDueException.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ NotFoundProductNameException.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ NotFoundStorageException.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ NotValidFileExtensionException.java
 ┃ ┃ ┃ ┃ ┃ ┣ repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ image
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ BarcodeImageRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ GifticonImageRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ GifticonImageRepositoryImpl.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ GifticonImageRepositorySupport.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ storage
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ GifticonStorageRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ GifticonStorageRepositoryImpl.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ GifticonStorageRepositorySupport.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ GifticonRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ GifticonRepositoryImpl.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ GifticonRepositorySupport.java
 ┃ ┃ ┃ ┃ ┃ ┣ service
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ GifticonImageService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ GifticonService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ GifticonStorageService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ OcrService.java
 ┃ ┃ ┃ ┃ ┃ ┗ util
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ GifticonImageUtil.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ JsonMapper.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ OcrUtil.java
 ┃ ┃ ┃ ┃ ┣ global
 ┃ ┃ ┃ ┃ ┃ ┣ controller
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ ContextController.java
 ┃ ┃ ┃ ┃ ┃ ┣ error
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ ErrorDetail.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ ErrorResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ ErrorResult.java
 ┃ ┃ ┃ ┃ ┃ ┣ exception
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ BaseException.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ ExceptionResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ RequiredFieldException.java
 ┃ ┃ ┃ ┃ ┃ ┣ success
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ SuccessResponse.java
 ┃ ┃ ┃ ┃ ┃ ┣ util
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ CheckUtil.java
 ┃ ┃ ┃ ┃ ┃ ┗ BaseTimeEntity.java
 ┃ ┃ ┃ ┃ ┣ movement
 ┃ ┃ ┃ ┃ ┃ ┣ controller
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ MovementController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ MovementPageController.java
 ┃ ┃ ┃ ┃ ┃ ┣ dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ MovementDto.java
 ┃ ┃ ┃ ┃ ┃ ┣ entity
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ Movement.java
 ┃ ┃ ┃ ┃ ┃ ┣ repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ MovementRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ MovementRepositoryImpl.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ MovementRepositorySupport.java
 ┃ ┃ ┃ ┃ ┃ ┗ service
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ MovementService.java
 ┃ ┃ ┃ ┃ ┣ payment
 ┃ ┃ ┃ ┃ ┃ ┣ controller
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ CheckoutController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ KakaoPayController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ PaymentController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ PaymentPageController.java
 ┃ ┃ ┃ ┃ ┃ ┣ dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ kakao
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ Amount.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ KakaoApproveRequestDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ KakaoPayApproveRequestDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ KakaoPayApproveResponseDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ KakaoPayReadyRequestDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ KakaoPayReadyResponseDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ KakaoPayRequestDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ PaymentDto.java
 ┃ ┃ ┃ ┃ ┃ ┣ entity
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ Payment.java
 ┃ ┃ ┃ ┃ ┃ ┣ enumeration
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ PayMethod.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ PayStatus.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ Site.java
 ┃ ┃ ┃ ┃ ┃ ┣ exception
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ EmptyItemNameException.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ EmptyPgTokenException.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ EmptyTotalAmountException.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ PaidIdMismatchException.java
 ┃ ┃ ┃ ┃ ┃ ┣ repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ PaymentRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ PaymentRepositoryImpl.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ PaymentRepositorySupport.java
 ┃ ┃ ┃ ┃ ┃ ┣ service
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ KakaoPayService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ PaymentService.java
 ┃ ┃ ┃ ┃ ┃ ┗ util
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ DtoToMultiValueMapConverter.java
 ┃ ┃ ┃ ┃ ┣ point
 ┃ ┃ ┃ ┃ ┃ ┣ controller
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ PointController.java
 ┃ ┃ ┃ ┃ ┃ ┣ exception
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ NotEnoughPointException.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ NotFoundGifticonException.java
 ┃ ┃ ┃ ┃ ┃ ┣ service
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ PointService.java
 ┃ ┃ ┃ ┃ ┃ ┗ PointBuyRequestDto.java
 ┃ ┃ ┃ ┃ ┣ product
 ┃ ┃ ┃ ┃ ┃ ┣ controller
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ ProductController.java
 ┃ ┃ ┃ ┃ ┃ ┣ dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ ProductDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ ProductEngCategoryDto.java
 ┃ ┃ ┃ ┃ ┃ ┣ entity
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ Product.java
 ┃ ┃ ┃ ┃ ┃ ┣ enumeration
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ CategoryName.java
 ┃ ┃ ┃ ┃ ┃ ┣ exception
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ NotFoundCategoryException.java
 ┃ ┃ ┃ ┃ ┃ ┣ repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ ProductRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ ProductRepositoryImpl.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ ProductRepositorySupport.java
 ┃ ┃ ┃ ┃ ┃ ┗ service
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ ProductService.java
 ┃ ┃ ┃ ┃ ┣ shop
 ┃ ┃ ┃ ┃ ┃ ┗ controller
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ ShopPageController.java
 ┃ ┃ ┃ ┃ ┣ user
 ┃ ┃ ┃ ┃ ┃ ┣ controller
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ AccountPageController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ KakaoAccountController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ LocalUserAccountController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ MessageController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ NaverAccountConotroller.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ UserController.java
 ┃ ┃ ┃ ┃ ┃ ┣ dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ KakaoUserDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ KakaoUserInfoDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ LocalUserDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ NaverTokenDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ NaverUserDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ SignupRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ TokenInfo.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ UserDto.java
 ┃ ┃ ┃ ┃ ┃ ┣ entity
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ enumeration
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ LoginType.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ UserType.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ KakaoUser.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ LocalUser.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ NaverUser.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ User.java
 ┃ ┃ ┃ ┃ ┃ ┣ exception
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ validator
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ LoginValidator.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ DuplicateEmailException.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ DuplicateNicknameException.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ DuplicateTelException.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ IdMismatchException.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ MismatchPasswordAndConfirmPassword.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ MismatchPasswordException.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ NotFoundUserException.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ NotLoginedException.java
 ┃ ┃ ┃ ┃ ┃ ┣ repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ NaverRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ UserRepository.java
 ┃ ┃ ┃ ┃ ┃ ┣ service
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ CustomUserDetailsService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ KakaoAccountService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ LocalUserService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ NaverAccountService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ UserAccountService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ UserService.java
 ┃ ┃ ┃ ┃ ┃ ┗ UserJwtTokenProvider.java
 ┃ ┃ ┃ ┃ ┣ .DS_Store
 ┃ ┃ ┃ ┃ ┣ Application.java
 ┃ ┃ ┃ ┃ ┗ ServletInitializer.java

 

```
</div>
</details>


---
