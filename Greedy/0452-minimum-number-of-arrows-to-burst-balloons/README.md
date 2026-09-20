# 452. Minimum Number of Arrows to Burst Balloons

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/minimum-number-of-arrows-to-burst-balloons/)

`Array` · `Greedy` · `Sorting`

## Intuition  
If we line up all balloons by the rightmost point of each interval, the first balloon’s end is the earliest position where an arrow can possibly burst it. Placing an arrow exactly at that end also bursts every subsequent balloon whose start is ≤ that end, because all those intervals overlap the chosen point. The naive way would be to examine every balloon against every previously placed arrow ( O(n²) ) or to keep a hash of covered ranges, both of which are unnecessary. The key observation is that after sorting by the right endpoint, a single pass with a “current arrow position” suffices – a classic **greedy interval‑cover** pattern.

## Approach  
1. **Sort by end** – `Arrays.sort(points, (a, b) -> Integer.compare(a[1], b[1]));`  
   *Exit condition*: all `points` are ordered so that `points[i][1] ≤ points[i+1][1]`.  
   *Invariant*: after sorting, any balloon that can be hit by an arrow placed at `points[i][1]` must appear at index ≥ i.  

2. **Initialize first arrow** – `int arrows = 1; int arrowPos = points[0][1];`  
   Handles the guaranteed non‑empty input; for a single balloon the answer is 1.  

3. **Iterate from the second balloon** – `for (int i = 1; i < points.length; i++)`  
   *Loop exit*: `i == points.length`.  
   *Invariant each iteration*: `arrowPos` is the rightmost coordinate of the last arrow placed, and all balloons with index `< i` are already burst.  

   - **Check overlap** – `if (points[i][0] > arrowPos)`  
     *Why `>` not `>=`*: when `points[i][0] == arrowPos` the current arrow still hits the balloon because the interval is inclusive on both ends.  

   - **Place new arrow when needed** – `arrows++; arrowPos = points[i][1];`  
     This updates the greedy choice to the earliest possible end that can cover the current balloon and any following overlapping ones.  

4. **Return result** – `return arrows;`  

Edge‑case handling: the code never accesses `points[-1]` because the first arrow is set before the loop. The sorting step also guarantees correct behavior for duplicate intervals or intervals that share endpoints.

## Dry Run  

Input: `[[10,16],[2,8],[1,6],[7,12]]`

After sorting by end: `[[1,6],[2,8],[7,12],[10,16]]`

| i | arrowPos (prev) | points[i][0] | points[i][1] | arrows | Action | Note |
|---|----------------|--------------|--------------|--------|--------|------|
| 0 (init) | – | – | – | 1 | arrowPos = 6 | First arrow at end of first interval |
| 1 | 6 | 2 | 8 | 1 | no new arrow | 2 ≤ 6, balloon already burst |
| 2 | 6 | 7 | 12 | 2 | arrows=2, arrowPos=12 | 7 > 6, need new arrow at 12 |
| 3 | 12 | 10 | 16 | 2 | no new arrow | 10 ≤ 12, covered by second arrow |

Final state: `arrows = 2`, which is minimal because the two arrows at 6 and 12 each cover a maximal overlapping group.

## Complexity  
- **Time:** `O(n log n)` – sorting dominates; the subsequent loop runs `n‑1` times, each iteration performing O(1) work.  
- **Space:** `O(1)` extra – only a few integer variables are used; the sort is in‑place for primitive arrays. (Output integer does not count toward space.)

## Solution (Java)

```java
class Solution {
    public int findMinArrowShots(int[][] points) {
        Arrays.sort(points, (a, b) -> Integer.compare(a[1], b[1]));
        int arrows = 1;
        int arrowPos = points[0][1];
        for (int i = 1; i < points.length; i++) {
            if (points[i][0] > arrowPos) {
                arrows++;
                arrowPos = points[i][1];
            }
        }
        return arrows;
    }
}
```

---

**Runtime** 53 ms (beats 47.3%) · **Memory** 95.6 MB (beats 69.4%)

<sub>Synced by AILeetHub on 2026-09-20.</sub>
