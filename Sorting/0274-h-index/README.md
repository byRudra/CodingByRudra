# 274. H-Index

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/h-index/)

`Array` · `Sorting` · `Counting Sort`

## Intuition  
If the citation counts are ordered from smallest to largest, the *i*‑th element is the smallest citation among the last `n‑i` papers. Therefore, when `citations[i]` is at least `n‑i`, **all** papers from `i` to the end have citations ≥ `n‑i`, satisfying the definition of an h‑index of `n‑i`. The naive way would be to try every possible h value and count papers each time, costing O(n²). By sorting once and scanning linearly we replace those repeated counts with a single comparison per position. This is the classic “sort‑then‑scan” pattern for threshold problems.

## Approach  
1. **Sort** the array `citations` in non‑decreasing order.  
2. Initialise `n = citations.length` and `h = 0`.  
3. **Iterate** `i` from `0` to `n‑1` (inclusive):  
   - Compute `papers = n - i`, the number of papers that remain including the current one.  
   - **Invariant:** before each iteration, `papers` equals the count of elements from index `i` to the end, and all those elements are ≥ `citations[i]` because of the sorting.  
   - If `citations[i] >= papers`, then the current `papers` is a feasible h‑index; update `h = Math.max(h, papers)`.  
   - The loop continues even after finding a feasible value because a larger `papers` might appear later when `i` moves right and `papers` shrinks.  
4. After the loop, return `h`.  

**Edge handling:**  
- For an empty or single‑element array the loop still runs correctly; `papers` becomes `1` and the condition either holds or not, yielding `h = 0` or `1`.  
- When the array length is even or odd the formula `papers = n‑i` works uniformly; no off‑by‑one adjustments are needed.  
- The comparison uses `>=` (not `>`) because the definition allows exactly `h` citations for each of the `h` papers.

## Dry Run  

Input: `citations = [3,0,6,1,5]`  

Sorted: `[0,1,3,5,6]`  

| i | citations[i] | papers = n‑i | h (after update) | note |
|---|--------------|--------------|------------------|------|
| 0 | 0            | 5            | 0                | 0 < 5 → no update |
| 1 | 1            | 4            | 0                | 1 < 4 → no update |
| 2 | 3            | 3            | 3                | 3 ≥ 3 → h becomes 3 |
| 3 | 5            | 2            | 3                | 5 ≥ 2 but 2 < current h, keep 3 |
| 4 | 6            | 1            | 3                | 6 ≥ 1, h stays 3 |

The loop finishes with `h = 3`, which is the maximum number of papers that have at least 3 citations each.

## Complexity  
- **Time:** O(n log n) – sorting dominates; the subsequent linear scan runs `n` times, each iteration doing O(1) work.  
- **Space:** O(1) extra – the algorithm uses only a few integer variables (`n`, `h`, `i`, `papers`) besides the input array. (The sort may use O(log n) stack space depending on the library implementation, which is ignored here.)

## Solution (Java)

```java
class Solution {
    public int hIndex(int[] citations) {
        Arrays.sort(citations);

        int n = citations.length;
        int h = 0;

        for (int i = 0; i < n; i++) {
            int papers = n - i;

            if (citations[i] >= papers) {
                h = Math.max(h, papers);
            }
        }

        return h;
    }
}
```

---

**Runtime** 5 ms (beats 72.5%) · **Memory** 43.6 MB (beats 41.7%)

<sub>Synced by AILeetHub on 2026-09-18.</sub>
