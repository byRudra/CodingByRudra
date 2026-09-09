# 1179. Reformat Department Table

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/reformat-department-table/)

`Database`

## Intuition  
The key observation is that each department’s monthly revenue can be turned into a separate column by aggregating rows conditionally on the month value. Instead of scanning the table once per month (which would be 12 passes) or building a map of (id, month) pairs, we can compute all twelve columns in a single pass: for every row we add its revenue to exactly one of twelve “buckets” determined by a `CASE` expression. This is the classic **conditional aggregation** pattern.

## Approach  
1. **Select the grouping key** – `id` is listed first in the `SELECT` clause; it will become the primary column of the result.  
2. **Create a conditional bucket for each month** – for month *M* we write `SUM(CASE WHEN month = 'M' THEN revenue END) AS M_Revenue`. The `CASE` yields `revenue` only for rows whose `month` matches *M*; otherwise it yields `NULL`, which `SUM` ignores.  
3. **Group rows by department** – `GROUP BY id` collapses all rows sharing the same `id` into a single output row, causing the twelve `SUM` expressions to accumulate the revenues for that department.  
4. **Return the aggregated rows** – the query emits one row per distinct `id` with twelve revenue columns; months without data remain `NULL` because no `revenue` was ever added to that bucket.  

*Edge‑case handling*  
- **Empty table**: `GROUP BY` produces no rows, so the result is empty – correct.  
- **Single‑row department**: only the matching month’s `SUM` receives a value; all others stay `NULL`.  
- **Duplicate (id, month) pairs**: `SUM` adds them together, matching the problem’s “revenue per month” semantics.  
- **Month spelling**: the `CASE` literals are exact strings; any typo would never match and would incorrectly yield `NULL`. The query deliberately uses `=` (exact match) rather than `LIKE` to avoid partial matches.

## Dry Run  
**Input rows**

| id | revenue | month |
|----|---------|-------|
| 1  | 8000    | Jan   |
| 2  | 9000    | Jan   |
| 1  | 7000    | Feb   |
| 1  | 6000    | Mar   |

**Iteration table (state after processing each input row)**  

| Step | Processed row | Partial `Jan_Revenue` | Partial `Feb_Revenue` | Partial `Mar_Revenue` | Note |
|------|----------------|-----------------------|-----------------------|-----------------------|------|
| 1 | (1,8000,Jan) | 8000 (id 1) | NULL | NULL | `CASE` matches Jan, adds 8000 to id 1’s Jan bucket |
| 2 | (2,9000,Jan) | 8000 (id 1), 9000 (id 2) | NULL | NULL | New group id 2 created, Jan bucket gets 9000 |
| 3 | (1,7000,Feb) | 8000 (id 1) | 7000 (id 1) | NULL | Feb bucket for id 1 receives 7000 |
| 4 | (1,6000,Mar) | 8000 (id 1) | 7000 (id 1) | 6000 (id 1) | Mar bucket for id 1 receives 6000 |

After all rows are consumed, the `GROUP BY` collapses the partial sums into final rows:

| id | Jan_Revenue | Feb_Revenue | Mar_Revenue | … |
|----|-------------|-------------|-------------|---|
| 1  | 8000        | 7000        | 6000        | null |
| 2  | 9000        | null        | null        | null |

The final table matches the required format because each month’s revenue appears in its dedicated column, and missing months stay `NULL`.

## Complexity  
- **Time:** **O(n)** – the engine scans the `Department` table once; each row contributes to exactly one of the twelve `SUM` expressions, so the work scales linearly with the number of rows *n*.  
- **Space:** **O(k)** – the grouping hash (or sort) stores one accumulator set per distinct department id, i.e., *k* groups, where *k* ≤ *n*. The output itself is not counted toward auxiliary space.

## Solution (MySQL)

```sql
SELECT
    id,
    SUM(CASE WHEN month = 'Jan' THEN revenue END) AS Jan_Revenue,
    SUM(CASE WHEN month = 'Feb' THEN revenue END) AS Feb_Revenue,
    SUM(CASE WHEN month = 'Mar' THEN revenue END) AS Mar_Revenue,
    SUM(CASE WHEN month = 'Apr' THEN revenue END) AS Apr_Revenue,
    SUM(CASE WHEN month = 'May' THEN revenue END) AS May_Revenue,
    SUM(CASE WHEN month = 'Jun' THEN revenue END) AS Jun_Revenue,
    SUM(CASE WHEN month = 'Jul' THEN revenue END) AS Jul_Revenue,
    SUM(CASE WHEN month = 'Aug' THEN revenue END) AS Aug_Revenue,
    SUM(CASE WHEN month = 'Sep' THEN revenue END) AS Sep_Revenue,
    SUM(CASE WHEN month = 'Oct' THEN revenue END) AS Oct_Revenue,
    SUM(CASE WHEN month = 'Nov' THEN revenue END) AS Nov_Revenue,
    SUM(CASE WHEN month = 'Dec' THEN revenue END) AS Dec_Revenue
FROM Department
GROUP BY id;
```

---

**Runtime** 599 ms (beats 40.3%) · **Memory** 0B (beats 100.0%)

<sub>Synced by AILeetHub on 2026-09-09.</sub>
