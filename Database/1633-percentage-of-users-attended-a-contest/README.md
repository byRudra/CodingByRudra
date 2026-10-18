# 1633. Percentage of Users Attended a Contest

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/percentage-of-users-attended-a-contest/)

`Database`

## Intuition  
The percentage for a contest is simply “how many **different** users signed up for that contest” divided by “how many users exist at all”. The total number of users never changes, so we can compute it once and reuse it for every contest. By counting distinct `user_id`s inside each `contest_id` group we avoid a second pass, a hash map, or any procedural loop. This is the classic **two‑step aggregation** pattern: one sub‑query for a global constant, one `GROUP BY` for per‑contest statistics.

## Approach  
1. **Compute the global denominator** – the sub‑query `(SELECT COUNT(*) FROM Users)` returns the total number of rows in `Users`.  
2. **Group registrations** – `FROM Register GROUP BY contest_id` creates one bucket for each contest that appears in `Register`. The loop over the table stops when all rows have been assigned to a bucket; the invariant is that each bucket contains exactly the rows with the same `contest_id`.  
3. **Count distinct participants** – `COUNT(DISTINCT user_id)` inside the group counts each user at most once, discarding duplicate registrations for the same contest.  
4. **Calculate the percentage** – multiply the distinct count by `100.0` (the decimal forces floating‑point division) and divide by the global denominator, then apply `ROUND(..., 2)` to keep two decimal places.  
5. **Order the result** – `ORDER BY percentage DESC, contest_id ASC` first sorts by the computed ratio (largest first) and breaks ties by the smaller `contest_id`.  

**Edge‑case handling**  
- A contest with no rows in `Register` never appears in the output, which matches the requirement because there is no percentage to report.  
- Duplicate rows like `(208, 6)` are ignored by `DISTINCT`.  
- Using `100.0` instead of `100` guarantees a non‑integer division, avoiding truncation.  
- `ROUND` is applied after the division, not before, to preserve precision before rounding.

## Dry Run  

**Input tables (excerpt)**  

```
Users
user_id
6
2
7
```

```
Register
contest_id | user_id
208        | 2
208        | 6
208        | 7
209        | 2
209        | 6
209        | 7
215        | 6
215        | 7
207        | 2
```

| contest_id | distinct_user_cnt (step 3) | percentage = ROUND(cnt*100.0/3,2) | note |
|------------|----------------------------|-----------------------------------|------|
| 208        | 3                          | 100.00                            | all three users present |
| 209        | 3                          | 100.00                            | all three users present |
| 215        | 2                          | 66.67                             | missing user 2 |
| 207        | 1                          | 33.33                             | only user 2 |
| 210        | 0 (not in Register)       | –                                 | omitted from result |

The final result set is ordered by `percentage` descending, then `contest_id` ascending, yielding the exact output shown in the example.

## Complexity  
- **Time:** O(N + M) – the sub‑query scans `Users` once (M rows) and the main query scans `Register` once (N rows) while grouping.  
- **Space:** O(K) – additional memory holds the aggregation buckets for the K distinct `contest_id`s; the output array is not counted.

## Solution (MySQL)

```sql
# Write your MySQL query statement below
SELECT
    contest_id,
    ROUND(COUNT(DISTINCT user_id) * 100.0 / (SELECT COUNT(*) FROM Users), 2) AS percentage
FROM Register
GROUP BY contest_id
ORDER BY percentage DESC, contest_id ASC;
```

---

**Runtime** 993 ms (beats 69.2%) · **Memory** 0B (beats 100.0%)

<sub>Synced by AILeetHub on 2026-10-18.</sub>
