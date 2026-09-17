# 1527. Patients With a Condition

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/patients-with-a-condition/)

`Database`

## Intuition  
The only thing that matters is whether any token in the space‑separated **conditions** column begins with the prefix `DIAB1`. Because the column is a plain string, we can treat the problem as a pattern‑matching task instead of splitting the string or scanning it token by token. A naïve solution would split the column into an array, iterate over each element, and compare the prefix – that would require extra parsing and a second pass. The observation is that a token that starts a string is matched by the pattern `'DIAB1%'`, and a token that appears later is always preceded by a space, so the pattern `'% DIAB1%'` captures those cases. Using these two `LIKE` patterns together finds every row that contains a `DIAB1`‑prefixed condition in a single scan. The pattern‑matching technique is the classic **two‑pattern LIKE** approach.

## Approach  
1. **Read each row** from `Patients`.  
2. **Evaluate the WHERE clause**:  
   - `conditions LIKE 'DIAB1%'` – true when the first token of the column starts with `DIAB1`.  
   - `OR conditions LIKE '% DIAB1%'` – true when any later token starts with `DIAB1` because it must be preceded by a space.  
3. **Select all columns** (`SELECT *`) for rows where the combined predicate is true.  
4. The query finishes when the engine has inspected every row; the invariant is that after processing a row, we know definitively whether it belongs to the result set.  
**Edge‑case handling**:  
- Empty `conditions` (e.g., `NULL` or `''`) fails both `LIKE` checks, so the row is correctly excluded.  
- Tokens that contain `DIAB1` as a suffix (e.g., `XDIAB1Y`) are not matched because the pattern requires the prefix to start at the beginning of a token.  
- The space before `DIAB1` in the second pattern prevents false positives like `ABCDIAB1XYZ` that appear inside a longer token.  

## Dry Run  
**Input rows**

| patient_id | patient_name | conditions          |
|-----------|--------------|---------------------|
| 1         | Daniel       | YFEV COUGH          |
| 2         | Alice        | (empty)             |
| 3         | Bob          | DIAB100 MYOP        |
| 4         | George       | ACNE DIAB100        |
| 5         | Alain        | DIAB201             |

**Iteration table**

| Row | conditions          | `LIKE 'DIAB1%'` | `LIKE '% DIAB1%'` | Pass? | Note                              |
|-----|---------------------|-----------------|-------------------|-------|-----------------------------------|
| 1   | YFEV COUGH          | false           | false             | no    | No token starts with DIAB1        |
| 2   | (empty)             | false           | false             | no    | Empty string matches neither      |
| 3   | DIAB100 MYOP        | true            | false             | yes   | First token matches prefix        |
| 4   | ACNE DIAB100        | false           | true              | yes   | Second token preceded by space    |
| 5   | DIAB201             | true            | false             | yes   | First token matches prefix        |

The rows that satisfy at least one predicate are 3, 4, and 5; they are returned because each contains a condition beginning with `DIAB1`.

## Complexity  
- **Time:** O(N · L) where N is the number of rows and L is the average length of the `conditions` string; the engine scans each row once and applies two constant‑time pattern checks.  
- **Space:** O(1) extra space (ignoring the output), because the query holds only a few scalar values per row while evaluating the predicates.

## Solution (MySQL)

```sql
SELECT *
FROM Patients
WHERE conditions LIKE 'DIAB1%'
OR conditions LIKE '% DIAB1%';
```

---

**Runtime** 494 ms (beats 30.0%) · **Memory** 0B (beats 100.0%)

<sub>Synced by AILeetHub on 2026-09-17.</sub>
