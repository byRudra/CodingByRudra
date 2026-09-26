# 1378. Replace Employee ID With The Unique Identifier

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/replace-employee-id-with-the-unique-identifier/)

`Database`

## Intuition  
The only thing that distinguishes employees with a unique identifier from those without is whether their `id` appears in the `EmployeeUNI` table. If we line‑up the two tables on the common `id` column, every employee will stay in the result and the matching `unique_id` will appear when it exists; otherwise the column stays `NULL`. A naïve solution could scan `Employees`, then for each row perform a separate lookup in `EmployeeUNI`, which would be O(N·M) or require a temporary hash map. The insight that a single **left join** simultaneously preserves all rows from the left side and pulls matching values from the right side eliminates the need for any extra pass or auxiliary structure.

## Approach  
1. **Select the left side** – start from `Employees e`. This guarantees that every employee contributes a row to the final output.  
2. **Left‑join the right side** – `LEFT JOIN EmployeeUNI u ON e.id = u.id`. The join condition matches rows with identical `id`. Because the join is left‑biased, rows from `e` without a partner in `u` are still emitted, and the columns from `u` (here `unique_id`) are filled with `NULL`.  
3. **Project the required columns** – `SELECT e.name, u.unique_id`. The order of columns matches the required output format (`unique_id` first in the problem statement, but the query can return them in any order; the README shows `name, unique_id` as the actual code does).  
4. **Return the result set** – no `ORDER BY` clause is needed because the problem allows any order.

**Edge‑case handling**  
- **Empty `Employees`**: the `FROM` clause yields zero rows, so the query returns an empty set – correct.  
- **Single‑row tables**: the join still works; if the `id` is present in `EmployeeUNI` the `unique_id` appears, otherwise `NULL`.  
- **Duplicate `id` values**: the schema guarantees `id` is a primary key in both tables, so duplicates cannot arise; the join therefore produces at most one match per employee.  

## Dry Run  
Input tables  

| Employees (e.id, e.name) | EmployeeUNI (u.id, u.unique_id) |
|--------------------------|---------------------------------|
| 1, Alice                 | 3, 1                            |
| 2, Bob                   | 1, 100                          |
| 3, Carol                 | 2, 200                          |

| Iteration | e.id | e.name | u.id (matched) | u.unique_id | Output row                | Note                              |
|-----------|------|--------|----------------|-------------|---------------------------|-----------------------------------|
| 1         | 1    | Alice  | NULL           | NULL        | (Alice, NULL)             | No matching `id` in `EmployeeUNI` |
| 2         | 2    | Bob    | NULL           | NULL        | (Bob, NULL)               | No matching `id` in `EmployeeUNI` |
| 3         | 3    | Carol  | 3              | 1           | (Carol, 1)                | Match found, `unique_id` = 1      |

Final result contains three rows: Alice NULL, Bob NULL, Carol 1, which is exactly the required output.

## Complexity  
- **Time:** O(N + M) – the engine scans `Employees` (N rows) once and probes `EmployeeUNI` (M rows) via the join, each row examined at most once.  
- **Space:** O(N + M) for the result set (output storage) plus O(1) auxiliary memory, because the query does not allocate additional data structures beyond the join buffers.

## Solution (MySQL)

```sql
SELECT e.name, u.unique_id
FROM Employees e
LEFT JOIN EmployeeUNI u
ON e.id = u.id;
```

---

**Runtime** 1171 ms (beats 88.8%) · **Memory** 0B (beats 100.0%)

<sub>Synced by AILeetHub on 2026-09-26.</sub>
