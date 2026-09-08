# 973. K Closest Points to Origin

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/k-closest-points-to-origin/)

`Array` · `Math` · `Divide and Conquer` · `Geometry` · `Sorting` · `Heap (Priority Queue)` · `Quickselect` · `K-D Tree`

## Intuition  
The only thing that matters for ordering points by closeness to the origin is the squared Euclidean distance `x² + y²`; taking the square root does not change the relative order. A naïve solution would sort all `n` points, costing `O(n log n)`. The insight here is that we do not need a full ordering—just the `k` smallest distances. By repeatedly scanning the whole array and ignoring points already selected, we can extract the next closest point in linear time, eliminating the need for a sort, a heap, or a quick‑select partition.

## Approach  
1. **Allocate result and bookkeeping arrays** – `ans` holds the final `k` points, `used` marks points already taken.  
2. **Outer loop (`i` from `0` to `k‑1`)** – each iteration selects one more closest point.  
   - **Invariant:** before iteration `i`, exactly `i` points are marked `used` and stored in `ans[0..i‑1]`.  
3. **Initialize per‑iteration minima** – `minDistance = Integer.MAX_VALUE`, `minIndex = -1`.  
4. **Inner loop (`j` over all indices)** – examine every point that is not `used[j]`.  
   - Compute `currDistance = x*x + y*y` for `points[j]`.  
   - If `currDistance < minDistance`, update both `minDistance` and `minIndex`.  
   - **Exit condition:** loop finishes after the last index; the invariant guarantees that the smallest unseen distance is captured.  
5. **Record the found point** – assign `ans[i] = points[minIndex]` and set `used[minIndex] = true`.  
6. **After `k` iterations** the outer loop ends and `ans` contains the required points, which are then returned.

Key edge handling:  
- When `points` has length `1`, the outer loop runs once and the inner loop immediately picks that sole element.  
- The algorithm treats even and odd lengths uniformly because it never relies on pairing indices.  
- `used` prevents the same point from being chosen twice, avoiding off‑by‑one errors that could arise from a simple “skip already taken” check.  

## Dry Run  
Input: `points = [[1,3], [-2,2], [2,-1]]`, `k = 2`

| iter (i) | j examined | minDistance (sq) | minIndex | used after iter | note |
|----------|------------|------------------|----------|-----------------|------|
| 0 | 0 → 2 | after j=0: 10 (idx0) → after j=1: 8 (idx1) → after j=2: 8 (idx1) | 1 | used[1]=true | First closest is `[-2,2]` |
| 1 | 0 → 2 (skip j=1) | after j=0: 10 (idx0) → after j=2: 5 (idx2) | 2 | used[2]=true | Second closest is `[2,-1]` |

After the two iterations `ans = [[-2,2], [2,-1]]`, which are exactly the two points with smallest squared distances (8 and 5).

## Complexity  
- **Time:** `O(k·n)` – the outer loop runs `k` times and each inner scan traverses all `n` points, because `fast` advances two nodes while `slow` advances one is not used here; instead we repeatedly scan the whole array.  
- **Space:** `O(n)` – the `used` boolean array of length `n` plus the output `ans` (which the problem counts as required output) are the only extra allocations.

## Solution (Java)

```java
class Solution {
    public int[][] kClosest(int[][] points, int k) {
        int ans[][] = new int[k][2];
        boolean[] used = new boolean[points.length];

        for (int i = 0; i < k; i++) {
            int minDistance = Integer.MAX_VALUE;
            int minIndex= -1;
            for (int j = 0; j < points.length; j++) {
                if (!used[j]) {
                    int x = points[j][0];
                    int y = points[j][1];

                    int currDistance = x * x + y * y; // origion so x1 = x & x2 = 0 x1 - x2 = x

                    if (currDistance < minDistance) {
                        minDistance = currDistance;
                        minIndex = j;
                    }
                }
            }
            ans[i] = points[minIndex];
            used[minIndex] = true;
        }
        return ans;
    }
}
```

---

**Runtime** 959 ms (beats 5.1%) · **Memory** 57.6 MB (beats 25.6%)

<sub>Synced by AILeetHub on 2026-09-08.</sub>
