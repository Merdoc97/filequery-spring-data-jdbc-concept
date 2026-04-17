select *
from users as users
         join addresses as addresses on users.id = addresses.user_id
where users.id = :userId;