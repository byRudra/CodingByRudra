# 452. Minimum Number of Arrows to Burst Balloons

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/minimum-number-of-arrows-to-burst-balloons/)

`Array` · `Greedy` · `Sorting`

## Intuition  
If we line up the balloons by their rightmost coordinate, the earliest‑ending balloon determines the only x‑position where an arrow can still hit it without missing any earlier balloon. Any later balloon whose left end lies to the left of (or exactly at) that rightmost coordinate can be burst by the same arrow. Thus we can greedily “close” a group of overlapping intervals as soon as we encounter a balloon that starts after the current rightmost bound. The naïve solution would sort twice or use a set to track overlaps, but the single‑pass after one sort eliminates all extra passes or data structures. This is the classic **interval‑covering with two pointers** (or “greedy on sorted ends”) pattern.

## Approach  
1. **Sort by end coordinate** – `Arrays.sort(intervals, (a,b) -> Integer.compare(a[1], b[1]));`  
   *Invariant*: after sorting, `intervals[i][1] ≤ intervals[i+1][1]`.  
2. **Initialize** – `prevEnd = intervals[0][1];` and `count = 0;`  
   `prevEnd` holds the rightmost coordinate of the current arrow’s feasible region.  
3. **Iterate i = 1 … n‑1**  
   *Loop condition*: `i < intervals.length`.  
   *Invariant each iteration*: all balloons up to `i‑1` are already accounted for, and `prevEnd` is the smallest right end among the balloons that share the current arrow.  
   - If `prevEnd >= intervals[i][0]` (the current balloon starts before the feasible region ends) → overlap:  
     * increment `count` because this balloon can share the existing arrow,  
     * shrink `prevEnd` to `Math.min(prevEnd, intervals[i][1])` to keep the feasible region as tight as possible.  
   - Else (`prevEnd < intervals[i][0]`) → no overlap: start a new arrow region by setting `prevEnd = intervals[i][1]`.  
4. **Return result** – `intervals.length - count;`  
   `count` is the number of balloons that were merged into existing groups; subtracting from the total yields the number of distinct arrow groups (i.e., arrows needed).

**Edge handling**:  
- The code assumes `intervals` is non‑empty (problem guarantees at least one balloon).  
- For a single balloon the loop never runs, `count` stays 0, and the answer is `1`.  
- The comparison uses `>=` so a balloon that touches the previous one at exactly the same x (e.g., `[1,2]` and `[2,3]`) is considered overlapping, matching the problem’s inclusive interval definition.

## Dry Run  

Input: `[[10,16],[2,8],[1,6],[7,12]]`

After sorting by end: `[[1,6],[2,8],[7,12],[10,16]]`

| i | intervals[i][0] | intervals[i][1] | prevEnd (before) | count (before) | Action / Note                              |
|---|----------------|----------------|------------------|----------------|--------------------------------------------|
| 0 | 1              | 6              | 6                | 0              | initialization                              |
| 1 | 2              | 8              | 6                | 0              | 6 ≥ 2 → overlap → count=1, prevEnd=6        |
| 2 | 7              | 12             | 6                | 1              | 6 < 7 → new group → prevEnd=12              |
| 3 | 10             | 16             | 12               | 1              | 12 ≥ 10 → overlap → count=2, prevEnd=12    |

Loop ends. `intervals.length = 4`, `count = 2`, so answer = `4‑2 = 2`.  
Two arrows suffice: one at x = 6 (covers first two balloons) and one at x = 12 (covers last two).

## Complexity  
- **Time:** `O(n log n)` – sorting dominates; the subsequent single pass runs `n‑1` iterations, each doing O(1) work.  
- **Space:** `O(1)` – only a few primitive variables (`prevEnd`, `count`, loop index) are used; the sort is in‑place for primitive arrays. (Output integer is not counted.)

## Solution (Java)

```java
class Solution {
    public int findMinArrowShots(int[][] intervals) {
        int count = 0;
        Arrays.sort(intervals, (a,b) -> Integer.compare(a[1], b[1]));
        int prevEnd = intervals[0][1];
        for(int i = 1; i < intervals.length; i++){
            if(prevEnd >= intervals[i][0]){
                count ++;
                prevEnd = Math.min(prevEnd, intervals[i][1]);
            }
            else{
                prevEnd =  intervals[i][1];
            }
        }
        return intervals.length - count;
    }
}
```

---

**Runtime** 53 ms (beats 47.3%) · **Memory** 95.5 MB (beats 69.4%)

<sub>Synced by AILeetHub on 2026-09-20.</sub>
