# 1. Two Sum

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/two-sum/)

`Array` · `Hash Table`

## Intuition  
The key observation is that for each element `x` we only need to know whether its complement `target‑x` has appeared earlier. If we store every previously seen value together with its index, the moment we encounter a number whose complement is already in the map we have both indices. A naïve double loop would examine every pair (O(n²)), but the complement‑lookup turns the second dimension into O(1) work, eliminating the extra pass. This is the classic **hash‑table (map) lookup** pattern.

## Approach  
1. **Create an empty hash map `needing`.** It will map a number to the index where it first appears.  
2. **Iterate `i` from `0` to `nums.length‑1`.**  
   - *Loop invariant:* before each iteration, `needing` contains exactly the values `nums[0..i‑1]` and their indices.  
3. **Compute `needingVal = target - nums[i]`.** This is the value we would need to pair with `nums[i]`.  
4. **Check `needing.containsKey(needingVal)`.**  
   - If true, the complement was seen earlier; return `{needing.get(needingVal), i}`.  
   - The `<=` vs `<` issue does not arise because we never compare indices, only existence in the map.  
5. **Otherwise, store the current element:** `needing.put(nums[i], i)`.  
   - This choice (store after the check) guarantees we never pair an element with itself, satisfying the “no reuse” rule.  
6. **If the loop finishes without a return, fall back to `{-1, -1}`.** The problem guarantees a solution, so this line is unreachable but keeps the compiler happy.

Edge cases handled explicitly:  
- Empty or single‑element arrays never enter the loop body, so the fallback is returned (though constraints forbid them).  
- Duplicate values are fine because the first occurrence is stored; when the second identical value is processed, its complement (itself) is already in the map, yielding the correct pair.

## Dry Run  

**Input:** `nums = [2, 7, 11, 15]`, `target = 9`

| i | nums[i] | needingVal (=9‑nums[i]) | needing (after possible put) | Action / Note |
|---|---------|------------------------|------------------------------|----------------|
| 0 | 2       | 7                      | {2→0}                        | 7 not in map → store 2 |
| 1 | 7       | 2                      | {2→0, 7→1}                   | 2 found → return {0,1} |
| 2 | 11      | -2                     | —                            | (loop ends early) |
| 3 | 15      | -6                     | —                            | (unreached) |

The algorithm stops at iteration 1 because `needingVal = 2` is already present, returning indices `[0,1]`, which indeed satisfy `nums[0] + nums[1] = 9`.

## Complexity  
- **Time:** O(n) – the single `for` loop visits each element once, and each hash‑map operation (`containsKey`, `get`, `put`) runs in expected O(1) time.  
- **Space:** O(n) – in the worst case we store every element of `nums` in `needing`; the output array is excluded from the space count.

## Solution (Java)

```java
class Solution {
    public int[] twoSum(int[] nums, int target) {
        HashMap<Integer, Integer> needing = new HashMap<>();

        for(int i = 0; i < nums.length; i++){
            int needingVal = target - nums[i];
            if (needing.containsKey(needingVal)) return new int[] {needing.get(needingVal), i};
            needing.put(nums[i], i);
        }
        return new int[]{-1, -1};
    }
}
```

---

**Runtime** 2 ms (beats 99.4%) · **Memory** 46.9 MB (beats 73.9%)

<sub>Synced by AILeetHub on 2026-05-06.</sub>
