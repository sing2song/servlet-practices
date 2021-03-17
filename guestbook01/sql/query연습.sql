desc guestbook;

-- insert
insert into guestbook
values (null, '쏭','1234','테스트입니다',now());

-- select 
select no, name, date_format(reg_date, '%Y년 %m월 %d일 %H시%i분%s초'), contents
from guestbook
order by reg_date desc;

-- delete
delete from guestbook where no=1 and password=1234;