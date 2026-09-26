# 620. Not Boring Movies

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/not-boring-movies/)

`Database`

## Intuition  
The only rows we ever need are those whose primary key is odd **and** whose `description` is not the literal string `"boring"`. Because the table already stores the rating, we can obtain the final ordering by a single `ORDER BY` on that column. A naïve solution might first pull all rows, then iterate in application code to drop even IDs or boring movies, and finally sort the remainder—costing extra passes and memory. The observation that SQL can express both predicates and the final sort in one statement eliminates all of that work. This is a classic **filter‑then‑sort** pattern using a single query.

## Approach  
1. **Select the source table** – `FROM Cinema` provides the full set of rows.  
2. **Apply the two predicates** – `WHERE id % 2 != 0` guarantees the identifier is odd, and `description != "boring"` discards the unwanted genre. The conjunction `AND` ensures a row survives only if *both* conditions hold.  
3. **Order the surviving rows** – `ORDER BY rating DESC` sorts the filtered result by the numeric `rating` column in decreasing order, which is exactly the required output order.  
   - *Edge handling*:  
     - If the table is empty or contains only even IDs, the `WHERE` clause yields an empty set, and the query correctly returns no rows.  
     - When the `rating` values are tied, the database’s stable sort preserves an arbitrary but deterministic order, which satisfies the problem because no secondary key is demanded.  
     - The modulo operator `%` works for any non‑negative integer `id`; negative IDs are impossible by definition of the primary key, so overflow or sign issues never arise.  

## Dry Run  
**Input**  

| id | movie       | description | rating |
|----|-------------|-------------|--------|
| 1  | War         | great 3D    | 8.9    |
| 2  | Science     | fiction     | 8.5    |
| 3  | irish       | boring      | 6.2    |
| 4  | Ice song    | Fantacy     | 8.6    |
| 5  | House card  | Interesting | 9.1    |

| Scan # | id | id % 2 != 0 | description != "boring" | keep? | rating | Note                              |
|--------|----|------------|--------------------------|-------|--------|-----------------------------------|
| 1      | 1  | true       | true                     | ✅    | 8.9    | odd and not boring                |
| 2      | 2  | false      | true                     | ❌    | 8.5    | even ID filtered out              |
| 3      | 3  | true       | false                    | ❌    | 6.2    | boring description filtered out   |
| 4      | 4  | false      | true                     | ❌    | 8.6    | even ID filtered out              |
| 5      | 5  | true       | true                     | ✅    | 9.1    | odd and not boring                |

After the `WHERE` clause the retained rows are `(id=1, rating=8.9)` and `(id=5, rating=9.1)`. The `ORDER BY rating DESC` then yields the final order: id 5 first, then id 1. This matches the expected output.

## Complexity  
- **Time:** `O(n log n)` – the scan of `n` rows is linear, and the subsequent sort on the `rating` column dominates with `n log n` work.  
- **Space:** `O(1)` extra – the database uses only constant auxiliary space besides the output rows (the result set itself is not counted).

## Solution (MySQL)

```sql
# Write your MySQL query statement below
SELECT * FROM Cinema WHERE id % 2 != 0 AND description != "boring" ORDER BY rating DESC
```

---

**Runtime** 258 ms (beats 89.2%) · **Memory** 0B (beats 100.0%)

<sub>Synced by AILeetHub on 2026-09-26.</sub>
