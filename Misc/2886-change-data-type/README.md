# 2886. Change Data Type

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/change-data-type/)

## Intuition  
The only obstacle is that the *grade* column is stored as floating‑point numbers while the required output demands integers. Converting each value individually would be O(n) work and error‑prone, but pandas provides a vectorised cast that rewrites the whole column in a single pass. By applying `astype(int)` directly to the column we eliminate any need for explicit loops, temporary containers, or manual type checks. This is a classic **vectorised type conversion** pattern in pandas.

## Approach  
1. **Select the column** – `students["grade"]` extracts the *grade* Series from the DataFrame.  
2. **Cast the Series** – `.astype(int)` creates a new Series where every float value is truncated to an integer (the same as Python’s `int()` conversion).  
3. **Assign back** – `students["grade"] = …` overwrites the original column with the newly‑typed Series, preserving row order and all other columns unchanged.  
4. **Return the DataFrame** – the mutated `students` object is returned so the caller receives the updated table.

*Edge handling*:  
- If the DataFrame is empty, step 1 yields an empty Series; `astype(int)` is a no‑op and the function returns the empty frame unchanged.  
- For a single‑row frame the same steps apply; no special case is needed.  
- The code assumes the *grade* column exists and contains numeric types; pandas will raise a clear error if the column is missing or contains non‑numeric data, which is preferable to silently producing wrong results.  

## Dry Run  
**Input**

| student_id | name | age | grade |
|-----------|------|-----|-------|
| 1         | Ava  | 6   | 73.0 |
| 2         | Kate | 15  | 87.0 |

| Step | `students["grade"]` before | Operation (`astype`) | `students["grade"]` after | Note |
|------|----------------------------|----------------------|---------------------------|------|
| 1    | `[73.0, 87.0]`             | `astype(int)`        | `[73, 87]`                | Floats truncated to ints, column overwritten |
| 2    | – (function returns)       | –                    | –                         | Final DataFrame returned with integer grades |

After step 1 the *grade* column holds integer values, matching the required output format.

## Complexity  
- **Time:** O(n) – `astype` touches each of the *n* elements in the *grade* column once to perform the cast.  
- **Space:** O(1) auxiliary – pandas creates a new Series for the cast but reuses the original memory layout; no extra structures proportional to *n* are allocated beyond the inevitable temporary Series.

## Solution (Pandas)

```python
import pandas as pd

def changeDatatype(students: pd.DataFrame) -> pd.DataFrame:
    students["grade"] = students["grade"].astype(int)
    return students
```

---

**Runtime** 310 ms (beats 19.2%) · **Memory** 66 MB (beats 96.5%)

<sub>Synced by AILeetHub on 2026-10-07.</sub>
