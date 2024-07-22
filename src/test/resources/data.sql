insert into member (username, password, name, email, role)
values
    ('hyunsense', '1234', 'jaehoon', 'jaehoon1022@naver.com', 'ROLE_USER'),
    ('jaehoon1022', '1234', 'hyun', 'hyun@naver.com', 'ROLE_USER');


insert into board (member_id, title, content)
values
    (1, 'test01', '내용1'),
    (2, 'test02', '내용1'),
    (2, '한글테스트용제목1', '내용1'),
    (1, '한글 테스트용제목2', '내용2'),
    (1, '한글 테스트용제목3', '내용3');



--       ('jaehoon1022', '테스트용 제목2', '내용2'),
--       ('jaehoon1022', '테스트용 제목3', '내용3'),
--       ('jaehoon1022', '테스트용 제목4', '내용4'),
--       ('jaehoon1022', '테스트용 제목5', '내용5'),
--       ('jaehoon1022', '테스트용 제목6', '내용6');
