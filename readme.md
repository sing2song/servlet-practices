# Mysite02

## 테이블 구성

![image-20210317121537438](C:%5CUsers%5C32153256%5CAppData%5CRoaming%5CTypora%5Ctypora-user-images%5Cimage-20210317121537438.png)



## Board SQL


```mysql
-- 게시판
DROP TABLE IF EXISTS `board` RESTRICT;

-- 게시판
CREATE TABLE `board` (
	`no`       INT UNSIGNED NOT NULL COMMENT '번호', -- 번호
	`title`    VARCHAR(200) NOT NULL COMMENT '타이틀', -- 타이틀
	`writer`   VARCHAR(50)  NOT NULL COMMENT '글쓴이', -- 글쓴이
	`email`    VARCHAR(200) NOT NULL COMMENT '이메일', -- 이메일
	`password` VARCHAR(20)  NOT NULL COMMENT '비밀번호', -- 비밀번호
	`hit`      VARCHAR(50)  NOT NULL DEFAULT 0 COMMENT '조회수', -- 조회수
	`contents` TEXT         NOT NULL COMMENT '내용', -- 내용
	`regDate`  DATETIME     NOT NULL COMMENT '작성일', -- 작성일
	`g_no`     VARCHAR(50)  NOT NULL COMMENT '그룹번호', -- 그룹번호
	`o_no`     VARCHAR(50)  NOT NULL COMMENT '그룹내 순서', -- 그룹내 순서
	`depth`    VARCHAR(50)  NOT NULL COMMENT '글의 깊이' -- 글의 깊이
)
COMMENT '게시판';

-- 게시판
ALTER TABLE `board`
	ADD CONSTRAINT `PK_board` -- 게시판 기본키
		PRIMARY KEY (
			`no` -- 번호
		);

ALTER TABLE `board`
	MODIFY COLUMN `no` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '번호';
```





### insert

```mysql
insert into board values(null,'qwer','이송원','ssong@youtube.com','111',0,'qqqqqqqqqqqqqq',now(),(select max(cast(g_no as unsigned))+1 from board as b),1,0);
```

