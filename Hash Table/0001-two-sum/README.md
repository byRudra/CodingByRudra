# 1. Two Sum

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/two-sum/)

`Array` · `Hash Table`

## Intuition  
The key observation is that for any element `x` we only need to know whether its complement `target‑x` has already appeared earlier in the array. If we store each previously seen value together with its index, a single forward scan can answer the question instantly. A naïve double loop would cost O(n²) time, while sorting would lose the original indices. Maintaining a hash map of “value → index” eliminates the extra pass and preserves O(1) look‑ups. This is the classic **hash‑table lookup** pattern.

## Approach  
1. Initialise an empty `HashMap<Integer,Integer>` called `needing`.  
2. Iterate `i` from `0` to `nums.length‑1`.  
   - **Loop invariant:** All elements with index `< i` are already stored in `needing` mapping each value to its earliest index.  
3. Compute `needingVal = target - nums[i]`.  
4. Check `needing.containsKey(needingVal)`.  
   - If true, the pair `(needing.get(needingVal), i)` satisfies the sum, so return it immediately.  
   - The `<=` vs `<` issue does not arise because we never compare indices; we only test existence of the complement.  
5. If the complement is absent, insert the current value with `needing.put(nums[i], i)`.  
   - This choice stores the first occurrence, which is safe because the problem guarantees a unique solution and we never reuse the same index.  
6. After the loop finishes (theoretically unreachable given the guarantee), return `{-1,-1}` as a fallback.

Edge cases: an empty or single‑element array never reaches the return inside the loop, but the constraints forbid them. Duplicate values are handled because the map keeps the earliest index; when the second duplicate arrives, its complement (the first duplicate) is already present, yielding the correct pair.

## Dry Run  

**Input:** `nums = [2,7,11,15]`, `target = 9`

| i | nums[i] | needingVal (= target‑nums[i]) | needing (value→index) after iteration | Action |
|---|---------|------------------------------|--------------------------------------|--------|
| 0 | 2       | 7                            | {2→0}                                 | complement not found, insert 2 |
| 1 | 7       | 2                            | {2→0, 7→1}                            | `2` is in map → return {0,1} |

The loop stops at iteration 1 because `needing.containsKey(2)` is true, and the algorithm returns indices `[0,1]`, which indeed sum to the target.

## Complexity  
- **Time:** O(n) – the single `for` loop visits each element once, and each hash‑table operation (`containsKey`, `put`, `get`) runs in expected O(1) time.  
- **Space:** O(n) – in the worst case we store every array element in `needing`; the output array is not counted toward extra space.

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

**Runtime** 2 ms (beats 99.4%) · **Memory** 46.9 MB (beats 55.9%)

<sub>Synced by AILeetHub on 2026-05-06.</sub>
