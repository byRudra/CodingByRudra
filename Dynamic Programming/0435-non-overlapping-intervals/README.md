# 435. Non-overlapping Intervals

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/non-overlapping-intervals/)

`Array` · `Dynamic Programming` · `Greedy` · `Sorting`

## Intuition  
If the intervals are ordered by their start points, any conflict can only occur between the current interval and the one that ends last among all intervals seen so far. The optimal choice is to keep the interval with the **smallest end** because it leaves the most room for the rest of the list. A naïve solution would compare every pair (O(n²)) or keep a hash of occupied ranges, both of which are unnecessary once the intervals are sorted. This observation leads to the classic **greedy two‑pointer** pattern: walk once through the sorted array while maintaining the end of the last kept interval.

## Approach  
1. **Sort** `intervals` by their start coordinate (`a[0]`).  
2. Initialise `prevEnd` with the end of the first interval (`intervals[0][1]`) and `count = 0`.  
3. Iterate `i` from 1 to `intervals.length‑1`:  
   - **Loop condition**: `i < intervals.length`.  
   - **Invariant** before each iteration: `prevEnd` is the smallest possible end among all intervals kept up to index `i‑1`, and the kept intervals are non‑overlapping.  
   - If `prevEnd > intervals[i][0]` (overlap detected):  
     * Increment `count` because one interval must be removed.  
     * Update `prevEnd` to `Math.min(prevEnd, intervals[i][1])` – we keep the interval that ends earlier, preserving the invariant.  
   - Else (no overlap):  
     * Set `prevEnd = intervals[i][1]` – the current interval becomes the last kept one.  
4. After the loop, `count` equals the minimum number of removals; return it.

**Edge handling**:  
- The code assumes `intervals.length ≥ 1` (guaranteed by constraints).  
- For a single interval the loop never runs, so `count` stays 0.  
- The comparison uses `>` (strict) because intervals that only touch (`prevEnd == intervals[i][0]`) are allowed.  

## Dry Run  

Input: `[[1,2],[2,3],[1,3]]`  

| i | intervals[i] | prevEnd (before) | count (before) | Action & new values | Note |
|---|--------------|------------------|----------------|----------------------|------|
| 1 | [2,3]        | 2                | 0              | no overlap → `prevEnd = 3` | keep `[2,3]` |
| 2 | [1,3]        | 3                | 0              | overlap → `count = 1`, `prevEnd = min(3,3)=3` | remove one, keep the earlier‑ending `[1,3]` (same end) |

Loop ends; `count = 1`, which is the minimal removals required.

## Complexity  
- **Time:** `O(n log n)` – sorting dominates; the single pass runs `n‑1` iterations, each O(1).  
- **Space:** `O(1)` – only a few primitive variables (`prevEnd`, `count`, loop index) are used; the sort is in‑place for the given `int[][]`.

## Solution (Java)

```java
class Solution {
    public int eraseOverlapIntervals(int[][] intervals) {
        int count = 0;
        Arrays.sort(intervals, (a,b) -> Integer.compare(a[0], b[0]));
        int prevEnd = intervals[0][1];
        for(int i = 1; i < intervals.length; i++){
            if(prevEnd > intervals[i][0]){
                count ++;
                prevEnd = Math.min(prevEnd, intervals[i][1]);
            }
            else{
                prevEnd =  intervals[i][1];
            }
        }
        return count;
    }
}
```

---

**Runtime** 51 ms (beats 18.1%) · **Memory** 115.4 MB (beats 93.2%)

<sub>Synced by AILeetHub on 2026-09-20.</sub>
