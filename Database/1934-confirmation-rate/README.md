# 1934. Confirmation Rate

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/confirmation-rate/)

`Database`

## Intuition  
The confirmation rate for a user is simply the fraction of their requests that ended with `'confirmed'`. If we treat each request as a numeric value — `1` for a confirmed request and `0` for a timeout — the average of those numbers is exactly that fraction. The only obstacle is users who never appear in **Confirmations**; they must contribute a rate of 0, not a `NULL`. By left‑joining **Signups** to **Confirmations**, every user is present in the result set, and missing rows produce `NULL` for `c.action`. The `CASE` expression maps `NULL` (and any non‑confirmed action) to `0.0`, so the average automatically becomes 0 for users without requests. The pattern used here is a classic *aggregation after a left join*.

## Approach  
1. **Left join** `Signups s` with `Confirmations c` on `s.user_id = c.user_id`.  
   - *Invariant*: After the join, each row represents either a real confirmation request (when `c` is not `NULL`) or a placeholder row for a user with no requests (when `c` is `NULL`).  
2. **Transform each joined row** with `CASE WHEN c.action = 'confirmed' THEN 1.0 ELSE 0.0 END`.  
   - The expression yields `1.0` for confirmed rows, `0.0` for timeout rows, and also `0.0` when `c` is `NULL` because the condition fails.  
3. **Aggregate** by `s.user_id` using `AVG(...)`.  
   - Because the numerator is the sum of the transformed values and the denominator is the count of rows (including the placeholder rows), the average equals `confirmed / total_requests`. For users with no requests the average of all `0.0`s is `0.0`.  
4. **Round** the resulting average to two decimal places with `ROUND(..., 2)`.  
   - The rounding is applied after the aggregation, ensuring the final output matches the required precision.  
5. **Select** `s.user_id` and the rounded average as `confirmation_rate`, and `GROUP BY s.user_id` to produce one row per user.

Edge‑case handling:  
- Empty tables: the `LEFT JOIN` still yields zero rows, so the query returns an empty result set.  
- Single‑user with no confirmations: the join creates one row with `c.* = NULL`; the `CASE` yields `0.0`, `AVG` returns `0.0`, and rounding gives `0.00`.  
- Users with only timeouts: all transformed values are `0.0`, average is `0.0`.  
- Users with mixed actions: the average correctly reflects the proportion of `1.0`s.

## Dry Run  
**Input** (excerpt)

| s.user_id | s.time_stamp          | c.user_id | c.time_stamp          | c.action   |
|-----------|-----------------------|-----------|-----------------------|------------|
| 1         | 2020‑01‑01 00:00:00   | 1         | 2020‑01‑02 08:00:00   | confirmed |
| 1         | 2020‑01‑01 00:00:00   | 1         | 2020‑01‑03 09:30:00   | timeout   |
| 2         | 2020‑02‑01 12:00:00   | NULL      | NULL                  | NULL       |

| Iteration (user) | Transformed value (`CASE`) | Row count for user | Sum of values | AVG (sum/rows) | Rounded |
|------------------|----------------------------|--------------------|---------------|----------------|---------|
| 1                | 1.0                        | 2                  | 1.0           | 0.50           | 0.50    |
| 1                | 0.0                        |                    |               |                |         |
| 2                | 0.0 (c is NULL)            | 1                  | 0.0           | 0.00           | 0.00    |

Final state: user 1 gets `0.50`, user 2 gets `0.00`, matching the definition of confirmation rate.

## Complexity  
- **Time:** O(N + M) – the left join scans the `Signups` table (size N) and the `Confirmations` table (size M) once, and the aggregation touches each joined row exactly once.  
- **Space:** O(U) – only a hash map of size equal to the number of distinct users (`U`) is needed for grouping; the output array is not counted toward extra space.

## Solution (MySQL)

```sql
# Write your MySQL query statement below
SELECT 
    s.user_id,
    ROUND(
        AVG(CASE WHEN c.action = 'confirmed' THEN 1.0 ELSE 0.0 END),
        2
    ) AS confirmation_rate
FROM Signups s
LEFT JOIN Confirmations c
    ON s.user_id = c.user_id
GROUP BY s.user_id;
```

---

**Runtime** 684 ms (beats 71.2%) · **Memory** 0B (beats 100.0%)

<sub>Synced by AILeetHub on 2026-10-08.</sub>
