# 1741. Find Total Time Spent by Each Employee

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/find-total-time-spent-by-each-employee/)

`Database`

## Intuition  
The only thing that matters for each employee on a given day is the sum of all intervals `out_time - in_time`. Because the table already stores each interval as a separate row, we can obtain the total with a single aggregation: group rows by the pair *(emp_id, event_day)* and add up the differences. The naïve alternative would be to scan the table repeatedly—once per employee or per day—or to build an auxiliary map in application code, both of which add extra passes or memory. The insight that a relational engine can perform this grouping in‑place lets us solve the problem in one declarative statement, i.e. the **group‑by aggregation** pattern.

## Approach  
1. **Project the needed columns.**  
   `SELECT event_day AS day, emp_id, out_time - in_time AS diff` computes the length of each stay directly in the result set.  
2. **Group rows.**  
   `GROUP BY emp_id, event_day` creates a bucket for every distinct employee‑day pair. The invariant is that all rows inside a bucket share the same `emp_id` and `event_day`.  
3. **Aggregate the bucket.**  
   `SUM(diff) AS total_time` adds the `diff` values inside each bucket, yielding the total minutes for that employee on that day.  
4. **Return the final columns.**  
   The `SELECT` clause keeps the grouped keys (`day`, `emp_id`) and the aggregated column (`total_time`).  

Edge cases handled automatically:  
- **Empty table** → the query returns zero rows.  
- **Single‑row day** → the bucket contains one `diff`, and `SUM` returns that value.  
- **Multiple entries per day** → all differences are accumulated because `GROUP BY` does not discriminate on row count.  
- **No need for `<` vs `<=` checks** because the arithmetic `out_time - in_time` is always positive by the schema guarantee.

## Dry Run  

Input `Employees`  

| emp_id | event_day | in_time | out_time |
|--------|-----------|---------|----------|
| 1      | 2020‑11‑28| 4       | 32       |
| 1      | 2020‑11‑28| 55      | 200      |
| 2      | 2020‑11‑28| 3       | 33       |
| 2      | 2020‑12‑09| 47      | 74       |

| Step | emp_id | event_day | diff (`out_time - in_time`) | Bucket after grouping | Action |
|------|--------|-----------|----------------------------|-----------------------|--------|
| 1    | 1      | 2020‑11‑28| 28                         | (1,2020‑11‑28) → 28   | compute diff |
| 2    | 1      | 2020‑11‑28| 145                        | (1,2020‑11‑28) → 28+145| accumulate |
| 3    | 2      | 2020‑11‑28| 30                         | (2,2020‑11‑28) → 30   | new bucket |
| 4    | 2      | 2020‑12‑09| 27                         | (2,2020‑12‑09) → 27   | new bucket |

After the `GROUP BY` finishes, the engine emits one row per bucket:

| day        | emp_id | total_time |
|------------|--------|------------|
| 2020‑11‑28 | 1      | 173        |
| 2020‑11‑28 | 2      | 30         |
| 2020‑12‑09 | 2      | 27         |

The final state matches the required totals because each bucket’s `SUM` precisely added all interval lengths for that employee on that day.

## Complexity  
- **Time:** **O(N)** – the engine scans the `Employees` table once; each row contributes a constant‑time `out_time - in_time` and a constant‑time hash update for its bucket.  
- **Space:** **O(G)** – additional memory holds the aggregation hash table, where *G* is the number of distinct *(emp_id, event_day)* groups; this is independent of the total row count and excludes the output itself.

## Solution (MySQL)

```sql
# Write your MySQL query statement below
SELECT
event_day AS day,
emp_id,
sum(out_time - in_time) AS total_time
FROM Employees
GROUP BY emp_id, event_day
```

---

**Runtime** 532 ms (beats 78.8%) · **Memory** 0B (beats 100.0%)

<sub>Synced by AILeetHub on 2026-10-07.</sub>
