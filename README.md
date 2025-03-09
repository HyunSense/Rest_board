## V2 주요 변경내역
### MyBatis -> JPA Migration
- Mybatis Entity -> JPA Entity로 수정
- 기존 요청별 ResponseDto -> ResponseDto, DataResponseDto 통합
- Post, Comment 에 대한 Dto추가 (PostDto, PostListDto, BoardSearchDto..)
- Service 계층 기능 분리, 기존 BoardService -> BoardService, CommentService, LikesService로 변경
- Service 계층 반환타입 ResponseEntity<ResponseDto> -> ResponseDto(계층별 명확한 분리를 위해 변경)
- 기존 에러(글 존재여부, 중복된 Content)를 예외처리 방식으로 변경 (ExceptionHandler로 통합)
- Interface ResponseCode, Message -> Enum ResponseCode로 변경


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



### <a href="https://drive.google.com/file/d/1yTKePs1NsfInhDzOVriECa9wDw2JmVK-/view?usp=sharing">API 명세서 PDF</a>
### <a href="https://hyunsense.notion.site/REST-API-f59958d679ac45adbe96fb923c4eafb4?pvs=4">API 명세서 노션</a>

## ERD
![erd_capture](https://github.com/user-attachments/assets/dc73745c-3bbb-4ada-9023-cdbd7fa29483)

## 시퀀스 다이어그램

### 회원가입 시퀀스
![joinSequence](https://github.com/user-attachments/assets/25f07f79-de71-4a6d-9759-ab978c7f5e3e)

### 로그인 시퀀스
![loginSequence](https://github.com/user-attachments/assets/118500d6-9476-42af-8aae-64f94321fdb2)

### 게시글 작성 시퀀스
![postBoardSequence](https://github.com/user-attachments/assets/56d9e9ca-5723-47af-86f9-0b353debecb9)

### 게시글 조회 시퀀스
![getBoardSequence](https://github.com/user-attachments/assets/4ec95d7a-91b4-4fba-a5c0-1152b20a8153)

### 게시글 수정 시퀀스
![patchBoardSequence](https://github.com/user-attachments/assets/e3173ab2-5bc8-441d-b1cf-39d22b6042e8)

### 게시글 삭제 시퀀스
![deleteBoardSequence](https://github.com/user-attachments/assets/cb9c6376-d312-4e51-9403-59f21d4f5499)

## 클래스 다이어그램(수정중)
![RestBoardClassDiagram](https://github.com/user-attachments/assets/fb5b5e12-ee58-4193-941e-b77a2cb887ef)

## 테스트 커버리지
![codeCoverage](https://github.com/user-attachments/assets/2d8d8730-6b50-4b7a-ad4d-cbf68c3a7e88)

### 회고
1. **테스트의 중요성**
    - 프로젝트 초기부터 **Given-When-Then 패턴**을 적용해 단위 테스트를 작성했습니다. 예를 들어, 서비스 계층에서 테스트 시 `given()`에서 Mock 객체를 활용해 DB 의존성을 제거하고, `when()`에서 서비스를 호출한 뒤 `then()`에서 응답 코드와 메세지를 검증하는 방식으로 진행하였습니다. 반면 Controller에서는 `@SpringBootTest`와 `MockMvc`를 활용해 통합 테스트를 실행하여 실제 요청 흐름을 검증 하였습니다.
    - **의존성 문제 개선**: 초기에는 `Service` 계층이 `Repository`와 강하게 결합되어 있어 테스트가 어려웠습니다. 이를 해결하기 위해 **Mockito의 `@Mock`과 `@InjectMocks`** 를 활용해 의존성을 제어하였습니다.
    - **예상치 못한 버그 발견**: 테스트 과정에서 눈으로 확인하기 어려운 문제들을 사전에 잡아냈습니다. 예를 들어, 사용자 권한 처리, 응답 메세지의 필드 누락 등 눈으로 확인하기 어려운 문제들을 사전에 식별할 수 있었습니다. 테스트가 실제 상황에서 생길 수 있는 문제를 미리 알려주고 코드의 질을 끌어올리는 데 꼭 필요하다는 걸 느꼈고, 앞으로도 테스트 주도 개발을 계속해야겠다고 다짐하였습니다.
    - **코드 커버리지 90%:** `JUnit5`와 `Mockito`를 활용해 단위 테스트와 통합 테스트를 꼼꼼히 작성했습니다. 이를 통해 **코드 커버리지 90%**를 달성했으며, 코드의 안정성을 크게 향상시킬 수 있었습니다.
    - **테스트 코드의 유지보수성**: 테스트 코드 역시 유지보수가 필요하다고 생각합니다. 초기에는 테스트 코드가 중복되고 복잡했으나, **`@BeforeEach`**와 같은 메서드를 적용해 중복을 제거하고 가독성을 높였습니다. 테스트 코드를 수정하거나 확장할 때도 비용을 크게 줄이는것이 중요하다 느꼈습니다.
    
2. **REST API 문서화**
    - **응답 형식 표준화**: API를 처음부터 직접 문서화하며 클라이언트가 사용할 응답 형식을 정리하였습니다. 예를 들어, `{ code: "SU", message: "Success", data: {...} }`처럼 성공 응답 형식을 표준화하고, `@ControllerAdvice`로 예외 상황마다 `{ code: "DU", message: "Duplicate username" }` 처럼 표준화하여 작성했습니다.이 과정에서 API가 어떻게 동작하는지 다시 점검하며 응답의 일관성을 유지하려 노력하였습니다.
    - **사용자 입장 고려**: 문서를 작성하면서 응답 데이터를 받을 사용자를 생각하게 되었습니다. 필요한 정보만 전달하려 노력하였고, 응답 코드와 메시지를 명확히 정의해야 한다는 점을 깨달았습니다. 단순히 코드를 기록하는 게 아니라, 누군가 사용할 수 있게 만드는 게 중요하다 생각하게 되었습니다.
    - **수동 문서화의 한계**: 직접 문서화를 하다 보니 코드가 바뀔 때마다 문서와 달라지는 부분이 생겼습니다. 이를 맞추느라 시간이 꽤 걸렸고, 문서화의 정확성과 최신 상태를 유지하는 게 쉽지 않다는 걸 배웠습니다. 기회가 된다면 **Swagger** 같은 도구로 문서를 자동화하여 코드와 문서가 늘 일치하도록 할 계획입니다.
