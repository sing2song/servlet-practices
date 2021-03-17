desc emaillist;
use webdb;

-- insert
insert 
into 
emaillist values (null,'이','송원','sing2song@naver.com');

insert 
into 
emaillist values (null,'김','송원','kimsing@naver.com');

-- delete
delete from emaillist where no=3;

-- list emaillist
select no, first_name, last_name,email 
from emaillist
order by no desc;

