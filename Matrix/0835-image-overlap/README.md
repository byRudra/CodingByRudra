# 835. Image Overlap

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/image-overlap/)

`Array` · `Matrix`

## Intuition  
If two 1‑bits from the two images end up on the same cell after a translation, the translation vector (row‑shift, col‑shift) that aligns them is identical for every such pair. Therefore the overlap for a particular translation equals the number of pairs of 1‑bits that share the same vector. By enumerating all pairs of 1‑bits, counting how many times each vector occurs, and taking the highest count, we obtain the maximal possible overlap. The naïve way would slide one whole matrix over the other for every possible shift (O(n³)–O(n⁴)), but the vector‑counting observation removes the need for an explicit grid scan. This is a classic “frequency map of translation vectors” pattern.

## Approach  
1. **Collect coordinates** – Scan both `img1` and `img2`.  
   *Loop*: `i` from `0` to `n‑1`, inner `j` from `0` to `n‑1`.  
   *Invariant*: after processing `(i,j)`, all 1‑bits in rows `< i` and columns `< j` of the current row are already stored.  
   *Action*: if `img1[i][j]==1` add `[i,j]` to list `a`; if `img2[i][j]==1` add `[i,j]` to list `b`.  
   Handles empty or single‑element matrices automatically because the loops still run once.  

2. **Count translation vectors** – Initialise `ans = 0` and an empty `HashMap<String,Integer> map`.  
   *Outer loop*: for each `p1` in `a`.  
   *Inner loop*: for each `p2` in `b`.  
   *Exit condition*: both loops finish after all pairs are examined.  
   *Invariant*: at the start of each inner iteration, `map` contains the exact frequencies of all vectors derived from previously processed pairs.  
   *Computation*: `dr = p1[0] - p2[0]`, `dc = p1[1] - p2[1]`; `key = dr + "," + dc`. Increment the count for `key` and update `ans` with `Math.max(ans, count)`.  
   The choice of a string key (`"dr,dc"`) avoids dealing with a custom pair class and guarantees unique identification of each vector.  

3. **Return result** – After all pairs are processed, `ans` holds the largest frequency, i.e., the maximal overlap, and is returned.

## Dry Run  
Input  

```
img1 = [[1,0],
        [0,1]]

img2 = [[0,1],
        [1,0]]
```

Coordinates: `a = [(0,0),(1,1)]`, `b = [(0,1),(1,0)]`.

| iter | p1 (row,col) | p2 (row,col) | dr | dc | key   | map after update                | note                     |
|------|--------------|--------------|----|----|-------|----------------------------------|--------------------------|
| 1    | (0,0)        | (0,1)        | 0  | -1 | "0,-1"| {"0,-1":1}                      | first occurrence         |
| 2    | (0,0)        | (1,0)        | -1 | 0  | "-1,0"| {"0,-1":1, "-1,0":1}            | new vector                |
| 3    | (1,1)        | (0,1)        | 1  | 0  | "1,0" | {"0,-1":1, "-1,0":1, "1,0":1}   | new vector                |
| 4    | (1,1)        | (1,0)        | 0  | 1  | "0,1" | {"0,-1":1, "-1,0":1, "1,0":1, "0,1":1}| new vector                |

All four vectors appear once, so `ans = 1`. The algorithm correctly reports that the best overlap is a single overlapping 1‑bit.

## Complexity  
- **Time:** O(|a|·|b|) = O(k₁·k₂), where k₁ and k₂ are the numbers of 1‑bits in `img1` and `img2`. In the worst case (all cells are 1) this is O(n⁴), but for typical sparse inputs it is far smaller.  
- **Space:** O(|a|·|b|) = O(k₁·k₂) for the hashmap storing distinct translation vectors; the lists `a` and `b` use O(k₁ + k₂) extra space, both bounded by O(n²). The output integer does not affect the asymptotic bound.

## Solution (Java)

```java
class Solution {
    public int largestOverlap(int[][] img1, int[][] img2) {
        List<int[]> a = new ArrayList<>();
        List<int[]> b = new ArrayList<>();

        for (int i = 0; i < img1.length; i++) {
            for (int j = 0; j < img1.length; j++) {
                if (img1[i][j] == 1) {
                    a.add(new int[] { i, j });
                }
                if (img2[i][j] == 1) {
                    b.add(new int[] { i, j });
                }
            }
        }

        int ans = 0;
        HashMap<String, Integer> map = new HashMap<>();

        for (int[] p1 : a) {
            for (int[] p2 : b) {
                int dr = p1[0] - p2[0];
                int dc = p1[1] - p2[1];

                String key = dr + "," + dc;
                int count = map.getOrDefault(key, 0) + 1;
                map.put(key, count);
                ans = Math.max(ans, count);
            }
        }
        return ans;
    }
}
```

---

**Runtime** 188 ms (beats 19.8%) · **Memory** 47.6 MB (beats 17.9%)

<sub>Synced by AILeetHub on 2026-09-13.</sub>
