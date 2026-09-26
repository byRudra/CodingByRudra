# 577. Employee Bonus

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/employee-bonus/)

`Database`

## Approach

Accepted easy solution in MySQL.
Relevant topics: Database.

## Complexity

- **Time:** _not analysed_
- **Space:** _not analysed_

## Solution (MySQL)

```sql
# Write your MySQL query statement below
select E.name, B.bonus
from Employee as E
Left JOIN Bonus as B
ON E.empId = B.empId
where B.bonus < 1000 or B.bonus IS NULL; 
```

---

**Runtime** 960 ms (beats 88.5%) · **Memory** 0B (beats 100.0%)

<sub>Synced by AILeetHub on 2026-09-26.</sub>
