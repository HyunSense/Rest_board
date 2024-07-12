create table member
(
    id           bigint auto_increment
        primary key,
    login_id     varchar(50)            not null,
    password     varchar(100)           not null,
    name         varchar(50)            not null,
    email        varchar(100)           null,
    role         varchar(50)            not null,
    created_date date default curdate() not null
);

create table board
(
    id           bigint auto_increment
        primary key,
    user_id      bigint        not null,
    writer_id    varchar(50)   not null,
    title        varchar(500)  not null,
    content      varchar(5000) null,
    view_count   bigint        not null,
    created_date datetime      not null,
    updated_date timestamp     not null,
    is_deleted   tinyint       not null,
    constraint board_user_id_fk
        foreign key (user_id) references member (id)
);

create table comment
(
    id         bigint auto_increment
        primary key,
    user_id    bigint       not null,
    board_id   bigint       not null,
    writer_id  varchar(50)  not null,
    content    varchar(500) not null,
    is_deleted tinyint      not null,
    constraint comment_board_id_fk
        foreign key (board_id) references board (id),
    constraint comment_user_id_fk
        foreign key (user_id) references member (id)
);