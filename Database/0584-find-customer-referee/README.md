# 584. Find Customer Referee

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/find-customer-referee/)

`Database`

## Intuition  
The key observation is that each customer’s eligibility depends only on the value of its own `referee_id`. If that column is `NULL` (meaning nobody referred them) or it contains any value other than 2, the customer must appear in the result. This eliminates the need for joins, sub‑queries, or extra passes over the table to discover indirect relationships. The problem therefore reduces to a single‑row predicate check, which can be expressed directly in the `WHERE` clause using a two‑value condition.

## Approach  
1. **Scan the `Customer` table row by row.**  
   - *Exit condition:* The scan ends when all rows have been examined.  
   - *Invariant:* For each processed row, the algorithm has determined whether the row satisfies the predicate `referee_id != 2 OR referee_id IS NULL`.  

2. **Evaluate the predicate for the current row.**  
   - If `referee_id` is `NULL`, the `IS NULL` part of the condition is true, so the row qualifies.  
   - Otherwise, compare `referee_id` with the constant 2; if they differ, the `!= 2` part is true, and the row qualifies.  

3. **Project the `name` column of qualifying rows.**  
   - Only the `name` field is retained in the output, matching the required schema.  

4. **Return the collected names in any order.**  
   - No explicit ordering clause is needed because the problem permits arbitrary order.

*Edge‑case handling:*  
- Empty tables produce an empty result set automatically.  
- A single‑row table is processed identically; the predicate correctly handles `NULL` and non‑`NULL` `referee_id` values.  
- The `!=` operator is safe here because `NULL` values are excluded by the `IS NULL` check; otherwise `NULL != 2` would evaluate to `UNKNOWN` and be filtered out.

## Dry Run  

**Input**  

| id | name  | referee_id |
|----|-------|------------|
| 1  | Will  | NULL       |
| 2  | Jane  | NULL       |
| 3  | Alex  | 2          |
| 4  | Bill  | NULL       |
| 5  | Zack  | 1          |
| 6  | Mark  | 2          |

**Iteration table**

| Row (id) | referee_id | `referee_id IS NULL` | `referee_id != 2` | Selected? | Note                     |
|----------|------------|----------------------|-------------------|-----------|--------------------------|
| 1        | NULL       | true                 | –                 | yes       | Null satisfies second clause |
| 2        | NULL       | true                 | –                 | yes       | Null satisfies second clause |
| 3        | 2          | false                | false             | no        | Both parts false        |
| 4        | NULL       | true                 | –                 | yes       | Null satisfies second clause |
| 5        | 1          | false                | true              | yes       | `1 != 2` makes predicate true |
| 6        | 2          | false                | false             | no        | Both parts false        |

After processing all rows, the selected names are **Will, Jane, Bill, Zack**, which matches the expected output.

## Complexity  
- **Time:** O(n) – each of the *n* rows is examined once, and the predicate evaluation is O(1).  
- **Space:** O(1) – aside from the output list (which the problem does not count), the algorithm uses only a constant amount of auxiliary memory.

## Solution (MySQL)

```sql
# Write your MySQL query statement below
SELECT name
FROM Customer
WHERE referee_id != 2
   OR referee_id IS NULL;
```

---

**Runtime** 482 ms (beats 84.0%) · **Memory** 0B (beats 100.0%)

<sub>Synced by AILeetHub on 2026-09-26.</sub>
