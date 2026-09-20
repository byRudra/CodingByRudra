# 56. Merge Intervals

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/merge-intervals/)

`Array` · `Sorting` · `Quicksort`

## Intuition  
The key observation is that once the intervals are ordered by their start coordinate, any overlap can only occur between a current interval and the next one in that order. Therefore we can sweep once from left to right, expanding a “current” merged interval whenever the next interval begins before or exactly at the current’s end. This eliminates the need for a second pass, a hash‑based lookup, or any recursive merging. The pattern used is a classic **two‑pointer sweep** (here the “pointer” is the mutable `current` interval).

## Approach  
1. **Sort by start** – `Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));`  
   *Exit condition*: the whole array is ordered; the invariant is that for any index `i < j`, `intervals[i][0] ≤ intervals[j][0]`.  
2. **Initialize** – `int[] current = intervals[0];` and add it to `result`.  
   Handles the edge case of a single‑element input because the loop below starts at `i = 1`.  
3. **Iterate** – `for (int i = 1; i < intervals.length; i++)`  
   *Exit condition*: `i` reaches `intervals.length`.  
   *Invariant*: `current` always represents the merged interval covering all original intervals up to `i‑1`.  
   a. **Overlap test** – `if (intervals[i][0] <= current[1])`  
      The `<=` is intentional: intervals that touch at the boundary (e.g., `[1,4]` and `[4,5]`) are considered overlapping per the problem definition.  
   b. **Merge** – `current[1] = Math.max(current[1], intervals[i][1]);`  
      Extends the end of `current` to the farthest reachable point.  
   c. **No overlap** – `else { current = intervals[i]; result.add(current); }`  
      Starts a new merged interval; the previous one is already stored in `result`.  
4. **Return** – Convert the `List<int[]>` to the required array shape with `toArray`. The output list already contains the correctly merged intervals, so no extra copying is needed beyond this call.

## Dry Run  
**Input**: `[[1,3],[2,6],[8,10],[15,18]]`

| i | intervals[i] | current (start,end) | Action | Note |
|---|--------------|---------------------|--------|------|
| 0 | – (init)     | (1,3)               | add    | result = [[1,3]] |
| 1 | [2,6]        | (1,3)               | merge  | 2 ≤ 3 → current becomes (1,6) |
| 2 | [8,10]       | (1,6)               | new    | 8 > 6 → current = (8,10), add to result |
| 3 | [15,18]      | (8,10)              | new    | 15 > 10 → current = (15,18), add to result |

Final `result` = `[[1,6],[8,10],[15,18]]`, which is exactly the merged set because every overlapping pair was collapsed during the sweep.

## Complexity  
- **Time:** O(n log n) – sorting dominates with `n log n`; the subsequent linear scan runs `n‑1` iterations.  
- **Space:** O(n) – the `result` list stores at most `n` intervals; aside from the output, only a constant amount of extra variables is used.

## Solution (Java)

```java
class Solution {
    public int[][] merge(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
        List<int[]> result = new ArrayList<>();
        int[] current = intervals[0];
        result.add(current);

        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] <= current[1]) {
                current[1] = Math.max(current[1], intervals[i][1]);
            } else {
                current = intervals[i];
                result.add(current);
            }
        }
        return result.toArray(new int[result.size()][]);  
    }
}
```

---

**Runtime** 7 ms (beats 98.9%) · **Memory** 49.4 MB (beats 14.1%)

<sub>Synced by AILeetHub on 2026-09-20.</sub>
