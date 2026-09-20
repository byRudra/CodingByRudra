# 57. Insert Interval

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/insert-interval/)

`Array`

## Intuition  
The key observation is that the sorted, non‑overlapping nature of *intervals* lets us treat the insertion as three consecutive blocks: (1) everything that ends before the new interval starts, (2) every interval that overlaps the new interval, and (3) everything that starts after the new interval ends. By scanning once from left to right we can identify the exact boundaries of these blocks, merge the middle block into a single interval, and output the three parts in order. The naïve alternative—checking every pair for overlap or repeatedly inserting and re‑sorting—would require extra passes or auxiliary structures. This solution relies on the classic **two‑pointer / linear scan** pattern.

## Approach  
1. **Initialize** `result` as an empty list, `start = 0`, and `n = intervals.length`.  
2. **First while loop** – `while (start < n && intervals[start][1] < newInterval[0])`  
   *Exit condition*: either we have examined all intervals or the current interval’s end is no longer strictly less than the new interval’s start.  
   *Invariant*: All intervals added to `result` so far end before `newInterval` begins, guaranteeing no overlap. Inside the loop we `result.add(intervals[start++])`.  
3. **Second while loop** – `while (start < n && intervals[start][0] <= newInterval[1])`  
   *Exit condition*: we stop when the next interval starts after the current merged interval’s end.  
   *Invariant*: `newInterval` always represents the union of all intervals seen that overlap the original `newInterval`. Each iteration expands `newInterval[0]` to the minimum start and `newInterval[1]` to the maximum end among overlapping intervals, then advances `start`.  
4. After the second loop we `result.add(newInterval)`. At this point `newInterval` is the correctly merged interval covering the entire overlapping region.  
5. **Third while loop** – `while (start < n)`  
   *Exit condition*: `start == n`.  
   *Invariant*: All remaining intervals start after the merged interval’s end, so they can be appended unchanged. We `result.add(intervals[start++])`.  
6. Finally convert the list to a primitive array with `result.toArray(new int[result.size()][])` and return it.

Edge cases handled explicitly: an empty input (`n == 0`) bypasses all loops and returns a list containing only `newInterval`; a single‑element input falls into one of the three loops without special code; the `<=` in the second loop correctly treats touching intervals (e.g., `[1,2]` and `[2,3]`) as overlapping, matching the problem definition.

## Dry Run  

**Input**  
`intervals = [[1,3],[6,9]]`  
`newInterval = [2,5]`

| iteration | start | result (as list)                     | newInterval   | note                                 |
|-----------|-------|--------------------------------------|---------------|--------------------------------------|
| 1 (first loop) | 0 → 1 | `[[1,3]]` (added)                    | `[2,5]`       | `intervals[0][1]=3` ≥ `newInterval[0]=2` → loop stops |
| 2 (second loop) | 1 → 2 | `[[1,3]]` (no add yet)                | `[1,5]`       | overlap: min start = 1, max end = 5 |
| 3 (second loop) | 2 → 2 | `[[1,3]]` (no add yet)                | `[1,5]`       | `start == n`, loop ends |
| 4 (add merged) | 2 | `[[1,3],[1,5]]`                       | —             | merged interval inserted |
| 5 (third loop) | 2 → 2 | `[[1,3],[1,5]]` (no change)          | —             | no remaining intervals |

Final `result` = `[[1,5],[6,9]]`. The first interval was merged with the new one, and the second interval remained untouched.

## Complexity  
- **Time:** O(n) – each interval is examined at most once; the three while loops together advance `start` from 0 to `n`.  
- **Space:** O(n) – the output list stores at most `n+1` intervals; no additional data structures proportional to input size are created beyond the result. (The returned array itself is not counted as extra space.)

## Solution (Java)

```java
class Solution {
    public int[][] insert(int[][] intervals, int[] newInterval) {
        List<int[]> result = new ArrayList<>();
        int start = 0, n = intervals.length;
        // Skip intervals that are shorter
        while (start < n && intervals[start][1] < newInterval[0]) {
            result.add(intervals[start++]);
        }
        // Now can add the interval but there can be overlapping intervals too soo include them
        while (start < n && intervals[start][0] <= newInterval[1]) {
            newInterval[0] = Math.min(newInterval[0], intervals[start][0]);
            newInterval[1] = Math.max(newInterval[1], intervals[start][1]);
            start++;
        }
        result.add(newInterval);
        // Now for the bigger intervals
        while (start < n) {
            result.add(intervals[start++]);
        }
        return result.toArray(new int[result.size()][]);

    }
}
```

---

**Runtime** 1 ms (beats 98.0%) · **Memory** 47 MB (beats 56.2%)

<sub>Synced by AILeetHub on 2026-09-20.</sub>
