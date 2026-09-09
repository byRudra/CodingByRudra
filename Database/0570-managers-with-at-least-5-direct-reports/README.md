# 570. Managers with at Least 5 Direct Reports

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/managers-with-at-least-5-direct-reports/)

`Database`

## Intuition  
A manager is exactly an `id` that appears in the `managerId` column of at least five other rows. The naive way would be to examine every employee, look up all rows that reference its `id`, and count them – an O(n²) scan or a series of self‑joins. The key insight is that SQL can count how many times each `managerId` occurs in a single pass using `GROUP BY` and then filter with `HAVING`. Once we have the qualifying manager ids, a simple join retrieves their names. This follows the classic **aggregation‑then‑join** pattern.

## Approach  
1. **Filter out null managers** – `WHERE managerId IS NOT NULL` ensures we only count real references.  
2. **Aggregate counts** – `GROUP BY managerId` builds one row per distinct manager id; the implicit accumulator `COUNT(*)` tallies how many direct reports each manager has.  
3. **Select qualifying managers** – `HAVING COUNT(*) >= 5` keeps only those manager ids with five or more reports. The subquery therefore yields a set `m` of qualifying `managerId`s.  
4. **Join back to Employee** – `JOIN Employee e ON e.id = m.managerId` matches each qualifying id to its employee record, allowing us to output `e.name`.  
5. **Project the name column** – `SELECT e.name` returns just the manager names, as required.  

*Edge handling*:  
- If the table is empty or no manager has five reports, the subquery returns zero rows, so the outer join produces an empty result – the correct answer.  
- Managers with exactly five reports satisfy `>= 5`; the `HAVING` clause uses `>=` rather than `>` to include the boundary case.  
- Duplicate rows are impossible because `id` is unique, so counting `managerId` directly reflects distinct direct reports.

## Dry Run  

**Input**  

| id | name | department | managerId |
|----|------|------------|-----------|
| 1  | Alice| A          | NULL      |
| 2  | Bob  | A          | 1         |
| 3  | Carol| A          | 1         |
| 4  | Dave | B          | 1         |
| 5  | Eve  | B          | 1         |
| 6  | Frank| B          | 1         |
| 7  | Grace| C          | 2         |

**Aggregation steps (subquery)**  

| Iteration | Processed row (managerId) | Count map after iteration | Note |
|-----------|---------------------------|---------------------------|------|
| 1 | 2 → manager 1 | {1:1} | first report for Alice |
| 2 | 3 → manager 1 | {1:2} | second report |
| 3 | 4 → manager 1 | {1:3} | third report |
| 4 | 5 → manager 1 | {1:4} | fourth report |
| 5 | 6 → manager 1 | {1:5} | fifth report (reaches threshold) |
| 6 | 7 → manager 2 | {1:5, 2:1} | report for Bob |

After processing all rows, `HAVING COUNT(*) >= 5` keeps only `managerId = 1`.  

**Join step**  

| e.id | e.name | m.managerId | Join condition | Output |
|------|--------|-------------|----------------|--------|
| 1    | Alice  | 1           | 1 = 1          | Alice  |

Final result: a single row with `Alice`, because she has exactly five direct reports.

## Complexity  
- **Time:** O(n) – the `GROUP BY` scans the `Employee` table once, counting each row, and the subsequent join scans the table a second time, both linear in the number of rows `n`.  
- **Space:** O(k) – extra space holds the aggregation map for `k` distinct `managerId`s (the number of managers that appear at least once), which is bounded by the number of employees. The output list itself is not counted toward auxiliary space.

## Solution (MySQL)

```sql
SELECT e.name
FROM Employee e
JOIN (
    SELECT managerId
    FROM Employee
    WHERE managerId IS NOT NULL
    GROUP BY managerId
    HAVING COUNT(*) >= 5
) m
ON e.id = m.managerId;
```

---

**Runtime** 355 ms (beats 77.6%) · **Memory** 0B (beats 100.0%)

<sub>Synced by AILeetHub on 2026-09-09.</sub>
