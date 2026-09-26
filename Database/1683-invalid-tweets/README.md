# 1683. Invalid Tweets

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/invalid-tweets/)

`Database`

## Intuition  
The only thing that distinguishes a valid tweet from an invalid one is the number of characters in its `content`. If we can compute the length of each `content` string directly inside the query, we never need a second pass, a temporary table, or any procedural logic. The naïve approach would be to pull all rows into an application, count characters in a loop, and then filter—an O(n) scan plus extra memory and network overhead. By leveraging the built‑in `CHAR_LENGTH` function we can perform the length check in a single declarative step, which is exactly the “two‑pointer‑free” pattern of a direct filter on a computed column.

## Approach  
1. **Read the table** – `SELECT tweet_id FROM Tweets` starts a scan over every row.  
2. **Compute length** – for each row the expression `CHAR_LENGTH(content)` evaluates the exact number of characters (including spaces and punctuation).  
3. **Apply the predicate** – the `WHERE CHAR_LENGTH(content) > 15` clause keeps only rows whose length exceeds the allowed limit.  
4. **Project the identifier** – the `SELECT` list contains only `tweet_id`, discarding the original `content` because the problem asks for IDs only.  

*Loop invariant*: during the scan, every processed row either satisfies the predicate and is emitted, or does not and is ignored. The scan stops when the underlying engine reaches the end of the table. Edge cases are handled automatically: an empty `content` yields length 0 (fails the predicate), a tweet whose length is exactly 15 fails because the condition is strict (`>`), and `NULL` content is excluded because `CHAR_LENGTH(NULL)` returns `NULL`, which does not satisfy `> 15`.

## Dry Run  

**Input table**

| tweet_id | content                              |
|----------|--------------------------------------|
| 1        | Hello World                          |
| 2        | This tweet is definitely too long!   |
| 3        | Short                                |

**Iteration trace**

| step | tweet_id | content                        | len = CHAR_LENGTH(content) | len > 15? | emitted tweet_id |
|------|----------|--------------------------------|----------------------------|-----------|------------------|
| 1    | 1        | Hello World                    | 11                         | false     | –                |
| 2    | 2        | This tweet is definitely too long! | 33                         | true      | 2                |
| 3    | 3        | Short                          | 5                          | false     | –                |

After the scan finishes, only tweet 2 remains in the result set, which matches the definition of an invalid tweet.

## Complexity  
- **Time:** O(n) – the engine examines each of the *n* rows once, evaluating `CHAR_LENGTH` and the comparison.  
- **Space:** O(1) extra – no auxiliary data structures are allocated beyond the constant‑size work needed for the length calculation; the output list itself is not counted toward the auxiliary space.

## Solution (MySQL)

```sql
# Write your MySQL query statement below
Select tweet_id
from Tweets
WHERE CHAR_LENGTH(content) > 15;
```

---

**Runtime** 622 ms (beats 71.6%) · **Memory** 0B (beats 100.0%)

<sub>Synced by AILeetHub on 2026-09-26.</sub>
