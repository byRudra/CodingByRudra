# 835. Image Overlap

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/image-overlap/)

`Array` · `Matrix`

## Intuition  
The overlap after any translation is completely determined by how many pairs of 1‑bits share the same displacement vector (dr, dc). If we enumerate every 1 in img1 and every 1 in img2, the most frequent displacement tells us the best way to slide one image onto the other. A naïve solution would shift one matrix over every possible offset (O(n³) or O(n⁴) work) and count overlaps each time, which is unnecessary because the same information is already encoded in the pairwise differences of the 1‑coordinates. The algorithm therefore reduces the problem to a single frequency‑counting pass, a classic “hash‑map counting of translation vectors” pattern.

## Approach  
1. **Collect coordinates of all 1‑bits.**  
   - Loop `i = 0 … n‑1`, `j = 0 … n‑1`.  
   - If `img1[i][j] == 1` add `[i, j]` to `onesA`; if `img2[i][j] == 1` add `[i, j]` to `onesB`.  
   - The loops terminate when the whole matrix has been scanned; the invariant is that after processing cell `(i, j)` both lists contain exactly the coordinates of all 1‑bits seen so far.  
   - Edge case: if either list stays empty the later loops simply never execute, and `best` remains 0, which is correct for all‑zero inputs.

2. **Count how often each shift occurs.**  
   - Initialise an empty `Map<Integer,Integer> shiftCount` and `best = 0`.  
   - For each `a` in `onesA` (outer loop) and each `b` in `onesB` (inner loop) compute  
     `dr = a[0] - b[0]` and `dc = a[1] - b[1]`.  
   - Encode the pair into a single key: `key = dr * 200 + dc`. The constant 200 exceeds `2*n` for the given constraints (`n ≤ 30`), guaranteeing a unique mapping for all possible shifts.  
   - Update the map with `merge(key, 1, Integer::sum)`, obtaining the new count `cnt`.  
   - Update `best = max(best, cnt)`.  
   - The inner loops exit when every pair of 1‑bits has been examined; the invariant is that `shiftCount[key]` always equals the number of processed pairs that produce that exact displacement.

3. **Return the maximum frequency.**  
   - After all pairs are processed, `best` holds the largest number of overlapping 1‑bits achievable by any translation, which is returned.

## Dry Run  
Input  

```
img1 = [[1,0],
        [0,1]]

img2 = [[0,1],
        [1,0]]
```

`onesA = [(0,0),(1,1)]`, `onesB = [(0,1),(1,0)]`.

| Step | a (r,c) | b (r,c) | dr = a.r‑b.r | dc = a.c‑b.c | key = dr*200+dc | cnt after update | Note                     |
|------|---------|---------|--------------|--------------|----------------|------------------|--------------------------|
| 1    | (0,0)   | (0,1)   | 0            | -1           | -1             | 1                | first occurrence of (0,-1) |
| 2    | (0,0)   | (1,0)   | -1           | 0            | -200           | 1                | first occurrence of (-1,0) |
| 3    | (1,1)   | (0,1)   | 1            | 0            | 200            | 1                | first occurrence of (1,0) |
| 4    | (1,1)   | (1,0)   | 0            | 1            | 1              | 1                | first occurrence of (0,1) |

All four keys have count 1, so `best = 1`. The algorithm correctly reports that the maximum possible overlap is 1.

## Complexity  
- **Time:** O(k₁·k₂) ≤ O(n⁴) in the worst case, because the double‑nested pair loops run once for every pair of 1‑bits (`k₁ = |onesA|`, `k₂ = |onesB|`).  
- **Space:** O(k₁·k₂) ≤ O(n⁴) for the hashmap storing shift frequencies, plus O(k₁ + k₂) for the two coordinate lists; the output integer itself is O(1).

## Solution (Java)

```java
class Solution {
    public int largestOverlap(int[][] img1, int[][] img2) {
        List<int[]> onesA = new ArrayList<>();
        List<int[]> onesB = new ArrayList<>();

        for (int i = 0; i < img1.length; i++) {
            for (int j = 0; j < img1.length; j++) {
                if (img1[i][j] == 1) {
                    onesA.add(new int[] { i, j });
                }
                if (img2[i][j] == 1) {
                    onesB.add(new int[] { i, j });
                }
            }
        }

        Map<Integer, Integer> shiftCount = new HashMap<>();
        int best = 0;

        for (int[] a : onesA) {
            for (int[] b : onesB) {
                int dr = a[0] - b[0];
                int dc = a[1] - b[1];
                int key = dr * 200 + dc; // encode (dr, dc) into one int; 200 safely exceeds 2*n for n <= 100
                int cnt = shiftCount.merge(key, 1, Integer::sum);
                best = Math.max(best, cnt);
            }
        }

        return best;
    }
}
```

---

**Runtime** 52 ms (beats 81.7%) · **Memory** 47.2 MB (beats 40.7%)

<sub>Synced by AILeetHub on 2026-09-13.</sub>
