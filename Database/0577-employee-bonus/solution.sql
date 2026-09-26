# Write your MySQL query statement below
select E.name, B.bonus
from Employee as E
Left JOIN Bonus as B
ON E.empId = B.empId
where B.bonus < 1000 or B.bonus IS NULL; 