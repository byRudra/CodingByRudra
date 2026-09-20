# 435. Non-overlapping Intervals

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/non-overlapping-intervals/)

`Array` · `Dynamic Programming` · `Greedy` · `Sorting`

## Intuition  
If we always keep the interval that finishes earliest, every later interval has the maximal room to start without overlapping. After sorting by end point, the current “kept” interval ends at `prevEnd`; any next interval whose start is `< prevEnd` must be discarded, because keeping it would force us to drop the earlier‑finishing one and reduce the total number of intervals we can retain. The naive solution would try all subsets or use a DP table, both O(n²); the greedy observation eliminates the extra pass and any auxiliary data structures. This is the classic **interval scheduling by earliest finish time** pattern.

## Approach  
1. **Sort** `intervals` in ascending order of their end coordinate (`a[1]`).  
   *Exit condition*: the whole array is ordered; the comparator guarantees a total order even when ends are equal.  
2. Initialise `prevEnd` with the end of the first interval (`intervals[0][1]`) and `count = 0`.  
   *Invariant*: all intervals before index i have been processed and the set of kept intervals ends at `prevEnd`.  
3. **Iterate** `i` from 1 to `intervals.length‑1`:  
   - **Check overlap**: `if (prevEnd > intervals[i][0])`.  
     *Invariant*: when true, the i‑th interval starts before the previously kept interval finishes, so they overlap.  
   - **Remove one interval**: increment `count`.  
     - Update `prevEnd` to the smaller end of the two overlapping intervals: `prevEnd = Math.min(prevEnd, intervals[i][1])`.  
       This choice preserves the earliest possible finish among the overlapping pair, keeping the greedy invariant intact.  
   - **No overlap** (`else` branch): keep the i‑th interval and set `prevEnd = intervals[i][1]`.  
4. After the loop, `count` equals the minimum number of deletions required; return it.

**Edge handling**:  
- The array length is guaranteed ≥ 1, so `intervals[0]` is safe.  
- When multiple intervals share the same end, the sort order does not matter because the greedy rule treats them identically.  
- The comparison uses `>` (strict) rather than `>=` because touching at a point is allowed (e.g., `[1,2]` and `[2,3]` are non‑overlapping).

## Dry Run  

Input: `[[1,2],[2,3],[1,3]]`

| i | intervals[i] | prevEnd (before) | condition `prevEnd > start` | action & new prevEnd | count |
|---|--------------|------------------|-----------------------------|----------------------|-------|
| 0 | [1,2]        | –                | –                           | init `prevEnd=2`     | 0 |
| 1 | [2,3]        | 2                | 2 > 2 → **false**          | keep → `prevEnd=3`   | 0 |
| 2 | [1,3]        | 3                | 3 > 1 → **true**           | remove → `prevEnd=min(3,3)=3` | 1 |

After processing all intervals, `count = 1`, which matches the optimal removal of `[1,3]`.

## Complexity  
- **Time:** O(n log n) – sorting dominates; the single pass runs `n‑1` iterations, each O(1).  
- **Space:** O(1) – only a few scalar variables (`count`, `prevEnd`, loop index) are used; the sort is in‑place (Java’s `Arrays.sort` on primitive arrays).

## Solution (Java)

```java
class Solution {
    public int eraseOverlapIntervals(int[][] intervals) {
        int count = 0;
        Arrays.sort(intervals, (a,b) -> Integer.compare(a[1], b[1]));
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

**Runtime** 48 ms (beats 41.2%) · **Memory** 115.8 MB (beats 57.1%)

<sub>Synced by AILeetHub on 2026-09-20.</sub>
