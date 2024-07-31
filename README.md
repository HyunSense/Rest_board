
## 프로젝트 주요기능
- RestAPI 설계
- Spring Security 와 JWT Token을 이용한 사용자 인증 방식 구현
- Rest 방식의 사용자 인증 및 게시판 구현
- REST API 문서화 
- 시퀀스 다이어그램을 통해 기능별 문서화

## 기술 및 라이브러리
- JAVA 17
- Spring Boot 3.3
- MariaDB 11.1.2
- h2 database 2.2 (TEST용)
- MyBatis
- Spring Security 6.3
- Spring Validation
- auth0-JWT 4.4
- Junit5 + Mockito
- Lombok

### 목표
- Spring Security 의 흐름과 동작 이해하기
- RestFul 하게 설계하기
- Controller, Service, Dao, DB 의 계층별 관심사 분리를 명확하게 하기
- 테스트 주도개발을 통해 검증된 테스트와 90% 이상 코드 커버리지 달성하기
- REST API 명세서를 작성하기
- 시퀀스 다이어그램 및 클래스 다이어그램을 직접 그려 문서화 하기



### <a href="https://drive.google.com/file/d/1Rp4IJkPxKoVy4Cy0st-oMQTNiL6mtwlO/view?usp=sharing">API 명세서 PDF</a>
### <a href="https://hyunsense.notion.site/REST-API-f59958d679ac45adbe96fb923c4eafb4?pvs=4">API 명세서 노션</a>

## ERD
![erd_capture](https://github.com/user-attachments/assets/01022a45-ff1f-4ca5-9a82-ad8a5141e3b0)

## 시퀀스 다이어그램

### 회원가입 시퀀스
![joinSequence](https://github.com/user-attachments/assets/25f07f79-de71-4a6d-9759-ab978c7f5e3e)

### 로그인 시퀀스
![loginSequence](https://github.com/user-attachments/assets/118500d6-9476-42af-8aae-64f94321fdb2)

### 게시글 작성 시퀀스
![postBoardSequence](https://github.com/user-attachments/assets/af1627f7-2c07-4759-9eda-a072d677f0c8)

### 게시글 조회 시퀀스
![getBoardSequence](https://github.com/user-attachments/assets/e15963e5-5dad-4b01-b3ca-8e0cdcfb890b)

### 게시글 수정 시퀀스
![patchBoardSequence](https://github.com/user-attachments/assets/8672f884-7a54-4361-ba6c-8adf80add8bf)

### 게시글 삭제 시퀀스
![deleteBoardSequence](https://github.com/user-attachments/assets/3a6ae2f2-be1c-49d1-80d0-ef59a6462498)

## 클래스 다이어그램(수정중)
![RestBoardClassDiagram](https://github.com/user-attachments/assets/fb5b5e12-ee58-4193-941e-b77a2cb887ef)

## 테스트 커버리지
![codeCoverage](https://github.com/user-attachments/assets/2d8d8730-6b50-4b7a-ad4d-cbf68c3a7e88)

### 회고

