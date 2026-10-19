# 2089. Find Target Indices After Sorting Array

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/find-target-indices-after-sorting-array/)

`Array` · `Binary Search` · `Sorting`

## Intuition  
After sorting, every occurrence of the same value becomes a contiguous block. Therefore the indices of the target value are simply the positions of that block in the sorted array. A naïve solution might try to locate each target by scanning the original unsorted array, then recompute its rank after sorting, which would require extra passes or a map. The key observation is that once the array is sorted, a single linear scan is enough to collect all target indices. This follows the classic “sort‑then‑scan” pattern.

## Approach  
1. **Sort the array** – `Arrays.sort(nums)` rearranges `nums` into non‑decreasing order.  
2. **Iterate over the sorted array** – `for (int i = 0; i < nums.length; i++)`.  
   - *Exit condition*: `i == nums.length`.  
   - *Invariant*: All indices `< i` have been examined, and `result` already contains every index `j` (< i) where `nums[j] == target`.  
3. **Check each element** – Inside the loop, `if (nums[i] == target)` we `result.add(i)`.  
   - This adds the current index to the answer exactly when the element matches the target.  
4. **Return the collected indices** – After the loop finishes, `result` holds every index of `target` in the sorted order, already sorted because `i` increased monotonically.

**Edge‑case handling**  
- The constraints guarantee `nums.length ≥ 1`, so an empty array never occurs, but the loop safely handles it anyway.  
- If the target does not appear, the condition is never true and `result` stays empty, which matches the required output.  
- When every element equals the target, the loop adds every index from `0` to `nums.length‑1`.  
- No off‑by‑one errors appear because the loop uses `< nums.length` and the check uses `==` (exact match), not `<=`.  

## Dry Run  
**Input**: `nums = [1,2,5,2,3]`, `target = 2`  

After sorting: `nums = [1,2,2,3,5]`

| i | nums[i] | result (so far) | note |
|---|---------|-----------------|------|
| 0 | 1       | []              | 1 ≠ 2, nothing added |
| 1 | 2       | [1]             | 2 == target → add index 1 |
| 2 | 2       | [1,2]           | 2 == target → add index 2 |
| 3 | 3       | [1,2]           | 3 ≠ 2 |
| 4 | 5       | [1,2]           | 5 ≠ 2 |

Loop ends (`i == 5`). The final `result` is `[1,2]`, which are exactly the positions of `2` in the sorted array.

## Complexity  
- **Time:** `O(n log n)` – the dominant cost is `Arrays.sort`, which runs in `n log n` time; the subsequent linear scan is `O(n)`.  
- **Space:** `O(1)` extra – sorting is in‑place for primitive arrays, and the only additional storage is the output list, which the problem excludes from the space count.

## Solution (Java)

```java
class Solution {
    public List<Integer> targetIndices(int[] nums, int target) {
        List<Integer> result = new ArrayList<>();
        Arrays.sort(nums);
        for(int i = 0; i < nums.length; i++){
            if(nums[i] == target)
                result.add(i);
        }
        return result;
    }
}
```

---

**Runtime** 5 ms (beats 71.8%) · **Memory** 44.7 MB (beats 80.4%)

<sub>Synced by AILeetHub on 2026-10-19.</sub>
