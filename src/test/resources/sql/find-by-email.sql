select *
from users
where email like CONCAT('%', :email, '%');