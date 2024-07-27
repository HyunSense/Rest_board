create table member
(
    id           bigint auto_increment primary key,
    username     varchar(50) unique      not null,
    password     varchar(100)            not null,
    name         varchar(50)             not null,
    email        varchar(100)            null,
    role         varchar(50)             not null,
    created_at   datetime default current_timestamp not null,
    updated_at   datetime default current_timestamp on update current_timestamp not null
);

create table board
(
    id           bigint auto_increment primary key,
    member_id    bigint                          not null,
    title        varchar(500)                    not null,
    content      varchar(5000)                   null,
    view_count   bigint default 0                not null,
    comment_count int default 0                  not null,
    likes_count   int default 0                  not null,
    created_at   datetime default current_timestamp      not null,
    updated_at   datetime default current_timestamp on update current_timestamp not null,
    is_deleted   tinyint default 0               not null,
    constraint board_user_id_fk
        foreign key (member_id) references member (id)
);

create table comment
(
    id         bigint auto_increment
        primary key,
    member_id    bigint       not null,
    board_id     bigint       not null,
    content      varchar(500) not null,
    created_at   datetime default current_timestamp not null,
    is_deleted   tinyint default 0                  not null,
    constraint comment_board_id_fk
        foreign key (board_id) references board (id),
    constraint comment_user_id_fk
        foreign key (member_id) references member (id)
);

create table likes
(
    member_id bigint not null,
    board_id bigint      not null,
    created_at datetime default current_timestamp not null,
    primary key (member_id, board_id),
    FOREIGN KEY (member_id) REFERENCES member(id),
    FOREIGN KEY (board_id) REFERENCES board(id)
);