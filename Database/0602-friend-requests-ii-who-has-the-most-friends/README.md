# 602. Friend Requests II: Who Has the Most Friends

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/friend-requests-ii-who-has-the-most-friends/)

`Database`

## Intuition  
Each friendship appears exactly once in the table, but the two participants are stored in separate columns. If we view every `requester_id` and every `accepter_id` as a single stream of user IDs, the number of times an ID occurs equals the total number of friends that user has. The naïve way would be to join the table to itself or to run two separate aggregations and then merge the results, which costs an extra pass and extra bookkeeping. The key insight is that a single `UNION ALL` can flatten the two columns into one column, after which a plain `GROUP BY` gives the friend counts. This is the classic **two‑column aggregation via union** pattern.

## Approach  
1. **Flatten the IDs** –  
   ```sql
   SELECT requester_id AS id FROM RequestAccepted
   UNION ALL
   SELECT accepter_id AS id FROM RequestAccepted
   ```  
   The `UNION ALL` keeps every occurrence (no deduplication), so each friendship contributes one count to each participant.  
2. **Count per user** –  
   ```sql
   SELECT id, COUNT(*) AS num FROM ( … ) AS combined GROUP BY id
   ```  
   The `GROUP BY` iterates over the combined rows; the invariant is that after processing *k* rows, `COUNT(*)` for each seen `id` equals the number of friendships encountered so far for that user.  
3. **Find the maximum** –  
   ```sql
   ORDER BY num DESC
   ```  
   Sorting preserves the invariant that the first row after sorting has the largest `num`.  
4. **Return only the top scorer** –  
   ```sql
   LIMIT 1
   ```  
   The loop of the `ORDER BY` stops as soon as the first row is produced, guaranteeing O(1) additional work after sorting.  

**Edge handling** –  
- An empty `RequestAccepted` yields no rows, so the query returns an empty result set.  
- A single friendship still produces two rows in the flattened set, correctly giving each participant a count of 1.  
- Because the primary key prevents duplicate `(requester_id, accepter_id)` pairs, we do not need to guard against double‑counting the same edge; `UNION ALL` is deliberately chosen over `UNION` to avoid the hidden `DISTINCT` step that would erase legitimate duplicates from different friendships.

## Dry Run  

Input table  

| requester_id | accepter_id |
|--------------|-------------|
| 1            | 2           |
| 1            | 3           |
| 2            | 3           |
| 3            | 4           |

Flattened `combined` result (order shown for illustration):

| id |
|----|
| 1 |
| 1 |
| 2 |
| 2 |
| 3 |
| 3 |
| 3 |
| 4 |

| Iteration | id processed | Current counts (`num`)                | Note                              |
|-----------|--------------|---------------------------------------|-----------------------------------|
| 1         | 1            | 1:1                                   | First occurrence of 1             |
| 2         | 1            | 1:2                                   | Second occurrence of 1            |
| 3         | 2            | 1:2, 2:1                              | First occurrence of 2             |
| 4         | 2            | 1:2, 2:2                              | Second occurrence of 2            |
| 5         | 3            | 1:2, 2:2, 3:1                         | First occurrence of 3             |
| 6         | 3            | 1:2, 2:2, 3:2                         | Second occurrence of 3            |
| 7         | 3            | 1:2, 2:2, 3:3                         | Third occurrence of 3             |
| 8         | 4            | 1:2, 2:2, 3:3, 4:1                    | First occurrence of 4             |

After grouping, sorting descending by `num` yields `(3, 3)` as the first row, which the `LIMIT 1` returns. Hence user 3 has the most friends (3).

## Complexity  
- **Time:** O(n) – the `UNION ALL` scans the table twice (once per column) and the `GROUP BY` processes each of the 2 n rows once; sorting is O(m log m) where *m* is the number of distinct users, but the dominant linear scan remains O(n).  
- **Space:** O(m) – the hash table used by `GROUP BY` stores a counter for each distinct user ID; no extra space is needed beyond that aggregation structure.

## Solution (MySQL)

```sql
SELECT id, COUNT(*) AS num
FROM (
    SELECT requester_id AS id FROM RequestAccepted
    UNION ALL
    SELECT accepter_id AS id FROM RequestAccepted
) AS combined
GROUP BY id
ORDER BY num DESC
LIMIT 1;
```

---

**Runtime** 335 ms (beats 59.7%) · **Memory** 0B (beats 100.0%)

<sub>Synced by AILeetHub on 2026-09-09.</sub>
