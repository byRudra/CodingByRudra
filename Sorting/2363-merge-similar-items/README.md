# 2363. Merge Similar Items

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/merge-similar-items/)

`Array` · `Hash Table` · `Sorting` · `Ordered Set`

## Intuition  
The key observation is that every possible `value` lies in the small fixed range [1, 1000]. By allocating a direct‑address array of that size we can treat the `value` itself as an index and accumulate its total `weight` in O(1) time per item. The naïve alternative—sorting both lists or inserting each pair into a hash map—adds an extra O(n log n) or O(n) overhead and requires extra data structures. Here the “bucket‑by‑value” pattern (a form of counting sort) eliminates those costs entirely.

## Approach  
1. **Allocate bucket array** – `int[] list = new int[1001];` creates a zero‑filled array where `list[v]` will hold the cumulative weight for value `v`.  
2. **First pass (items1)** – For each `int[] item` in `items1`, execute `list[item[0]] += item[1];`. The loop stops when the iterator is exhausted; the invariant is that after processing `k` elements, `list[v]` equals the sum of weights of value `v` seen so far in `items1`.  
3. **Second pass (items2)** – Repeat the same accumulation for `items2`. The invariant now extends to “sum of weights from both arrays”.  
4. **Collect results** – Iterate `i` from 1 through 1000. If `list[i] > 0`, append `Arrays.asList(i, list[i])` to `result`. The exit condition is `i == 1000`; the invariant is that all values `< i` with positive weight have already been added to `result` in ascending order.  
5. **Return** – The built `result` list is already sorted because we traversed the bucket array in increasing index order.

Edge‑case handling: the code works for empty or single‑element inputs because the loops simply perform zero or one iteration. The upper bound `<= 1000` (instead of `< 1000`) is required because the array length is 1001, giving a valid slot for value 1000. No overflow checks are needed because the maximum possible accumulated weight is `1000 * 1000 = 10⁶`, well within `int`.

## Dry Run  
**Input**  
`items1 = [[1,1],[4,5],[3,8]]`  
`items2 = [[3,1],[1,5]]`

| Step | i (bucket index) | list[i] after processing | Action on result | Note |
|------|------------------|--------------------------|------------------|------|
| 1    | –                | after items1: list[1]=1, list[3]=8, list[4]=5 | – | First accumulation |
| 2    | –                | after items2: list[1]=6 (1+5), list[3]=9 (8+1) | – | Second accumulation |
| 3    | 1                | list[1]=6 >0 | add `[1,6]` | First output entry |
| 4    | 2                | list[2]=0 | – | Skipped because weight 0 |
| 5    | 3                | list[3]=9 >0 | add `[3,9]` | |
| 6    | 4                | list[4]=5 >0 | add `[4,5]` | |
| 7    | 5…1000           | all zero | – | Loop ends |

Final `result = [[1,6],[3,9],[4,5]]`, which is exactly the required merged list in ascending order.

## Complexity  
- **Time:** O(n + m + U) where `n = items1.length`, `m = items2.length`, and `U = 1000` is the fixed value range. The two accumulation loops each touch every input element once, and the final scan visits the 1000 buckets.  
- **Space:** O(U) = O(1000) for the bucket array; the output list itself is not counted against the auxiliary space budget.

## Solution (Java)

```java
class Solution {
    public List<List<Integer>> mergeSimilarItems(int[][] items1, int[][] items2) {
        int list[] = new int[1001];
        for (int[] item : items1) {
            list[item[0]] += item[1];
        }

        for (int[] item : items2) {
            list[item[0]] += item[1];
        }

        List<List<Integer>> result = new ArrayList<>();
        for (int i = 1; i <= 1000; ++i) {
            if (list[i] > 0)
                result.add(Arrays.asList(i, list[i]));
        }
        return result;

    }
}
```

---

**Runtime** 3 ms (beats 99.3%) · **Memory** 46.2 MB (beats 100.0%)

<sub>Synced by AILeetHub on 2026-10-15.</sub>
