# 2363. Merge Similar Items

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/merge-similar-items/)

`Array` · `Hash Table` · `Sorting` · `Ordered Set`

## Intuition  
The key observation is that we can treat the *value* of each item as a key in an ordered dictionary: inserting a pair updates the accumulated weight for that key, and the dictionary maintains keys in ascending order automatically. A naïve solution would first collect all pairs, then sort the combined list – an extra O(n log n) pass plus a manual aggregation step. By using a `TreeMap`, the aggregation and ordering happen together, eliminating the need for a separate sort or a hash‑map plus a final sort. This is the classic **ordered map** (or “sorted dictionary”) pattern.

## Approach  
1. **Create an empty `TreeMap<Integer,Integer>` called `map`.**  
   *Invariant:* before any insertion, `map` contains the summed weights for all values seen so far, sorted by key.  

2. **Iterate over `items1`.**  
   - Loop condition: `for (int[] item : items1)` stops when the array is exhausted.  
   - Inside each iteration: `map.put(item[0], map.getOrDefault(item[0], 0) + item[1])`.  
   - *Invariant:* after processing the *k*‑th element of `items1`, `map` holds correct totals for the first *k* items, and keys remain ordered.  

3. **Iterate over `items2` with the same update logic.**  
   - Loop condition identical to step 2, now covering the second array.  
   - The same `put` call merges weights for values that may already exist from `items1`.  
   - *Invariant:* after processing the *k*‑th element of `items2`, `map` reflects the combined totals of all processed elements from both arrays.  

4. **Build the result list.**  
   - Create `result = new ArrayList<>()`.  
   - Traverse `map.entrySet()` in its natural order: `for (Map.Entry<Integer,Integer> e : map.entrySet())`.  
   - Append `Arrays.asList(e.getKey(), e.getValue())` to `result`.  
   - *Invariant:* each iteration adds the next smallest value‑weight pair, preserving ascending order.  

5. **Return `result`.**  
   - At this point `result` contains every distinct value exactly once, with its summed weight, sorted by value.

Edge‑case handling: if either input array is empty, the corresponding loop simply does nothing, leaving `map` unchanged; the final traversal still yields a correctly ordered list (possibly empty). Because `TreeMap` uses `compareTo` on `Integer`, overflow is impossible for the given constraints.

## Dry Run  
Input: `items1 = [[1,1],[4,5],[3,8]]`, `items2 = [[3,1],[1,5]]`

| Iteration | `item` processed | `map` state after put (value→weight) | Note |
|-----------|------------------|--------------------------------------|------|
| 1 | `[1,1]` (items1) | {1→1} | New key inserted |
| 2 | `[4,5]` (items1) | {1→1, 4→5} | New key inserted |
| 3 | `[3,8]` (items1) | {1→1, 3→8, 4→5} | New key inserted, map stays sorted |
| 4 | `[3,1]` (items2) | {1→1, 3→9, 4→5} | Existing key 3 updated |
| 5 | `[1,5]` (items2) | {1→6, 3→9, 4→5} | Existing key 1 updated |

After the traversal of `map.entrySet()`, `result` becomes `[[1,6],[3,9],[4,5]]`, which is exactly the required output because each value’s weight is the sum of its occurrences and the list is sorted.

## Complexity  
- **Time:** O((n₁ + n₂) log k) where *k* is the number of distinct values. Each `put` on a `TreeMap` costs O(log k), and we perform one such operation per element of both input arrays.  
- **Space:** O(k) extra space for the map and the output list, where *k* ≤ n₁ + n₂. The space does not include the input arrays themselves.

## Solution (Java)

```java
class Solution {
    public List<List<Integer>> mergeSimilarItems(int[][] items1, int[][] items2) {
        TreeMap<Integer, Integer> map = new TreeMap<>();
        for(int[] item : items1){
            map.put(item[0], map.getOrDefault(item[0], 0) + item[1]);
        }
        
        for(int[] item : items2){
            map.put(item[0], map.getOrDefault(item[0], 0) + item[1]);
        }

        List<List<Integer>> result = new ArrayList<>();
        for (Map.Entry<Integer, Integer> e : map.entrySet()) {
            result.add(Arrays.asList(e.getKey(), e.getValue()));
        }
        return result;

    }
}
```

---

**Runtime** 11 ms (beats 36.0%) · **Memory** 46.8 MB (beats 97.4%)

<sub>Synced by AILeetHub on 2026-10-15.</sub>
