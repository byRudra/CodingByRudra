# 2089. Find Target Indices After Sorting Array

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/find-target-indices-after-sorting-array/)

`Array` · `Binary Search` · `Sorting`

## Intuition  
The only thing that matters after sorting is how many elements are strictly smaller than `target` and how many are equal to it. Once we know those two counts, the sorted positions of all `target` values are forced: they occupy the consecutive indices starting at `lessThanTargetCount`. The naïve solution would sort the whole array ( `O(n log n)` ) and then scan it, but the observation lets us avoid any ordering work entirely. This reduces the problem to a single pass that tallies two frequencies, a classic counting‑based pattern.

## Approach  
1. **Initialize counters** `lessThanTargetCount = 0` and `targetCount = 0`.  
2. **First pass – frequency tally**  
   - Iterate `for (int num : nums)`.  
   - *Invariant*: before each iteration, `lessThanTargetCount` equals the number of processed elements that are `< target`, and `targetCount` equals the number of processed elements that are `== target`.  
   - If `num < target` → increment `lessThanTargetCount`.  
   - Else if `num == target` → increment `targetCount`.  
   - Elements `> target` are ignored because they never affect the final indices.  
   - The loop terminates when every array element has been examined, guaranteeing the counters reflect the whole input.  
3. **Prepare result list** `result = new ArrayList<>()`.  
4. **Second pass – index generation**  
   - Loop `for (int i = 0; i < targetCount; i++)`.  
   - *Invariant*: at the start of each iteration, `i` is the offset from the first possible target index.  
   - Append `lessThanTargetCount + i` to `result`.  
   - This produces exactly `targetCount` consecutive indices beginning at the count of smaller elements.  
5. **Return** `result`.  
   - Edge cases are naturally handled: if `targetCount` is zero the second loop never runs, yielding an empty list; if the array has length 1 the same logic applies without special casing.

## Dry Run  
**Input**: `nums = [1, 2, 5, 2, 3]`, `target = 2`

| iteration (first loop) | num | lessThanTargetCount | targetCount | note |
|------------------------|-----|---------------------|-------------|------|
| 0 | 1 | 1 | 0 | 1 < 2 → less++ |
| 1 | 2 | 1 | 1 | 2 == 2 → target++ |
| 2 | 5 | 1 | 1 | 5 > 2 → no change |
| 3 | 2 | 1 | 2 | another 2 → target++ |
| 4 | 3 | 1 | 2 | 3 > 2 → no change |

After the first pass: `lessThanTargetCount = 1`, `targetCount = 2`.  

| iteration (second loop) | i | added index (`lessThanTargetCount + i`) | result so far |
|--------------------------|---|------------------------------------------|---------------|
| 0 | 0 | 1 | [1] |
| 1 | 1 | 2 | [1, 2] |

The final `result` is `[1, 2]`, which matches the sorted‑array positions of all `2`s.

## Complexity  
- **Time:** `O(n)` – the algorithm makes one linear scan to count and a second scan of at most `targetCount ≤ n` to emit indices.  
- **Space:** `O(k)` – only the output list of size `k = targetCount` is allocated; all other variables use constant extra space. (The input array is not duplicated.)

## Solution (Java)

```java
// class Solution {
//     public List<Integer> targetIndices(int[] nums, int target) {
//         List<Integer> result = new ArrayList<>();
//         Arrays.sort(nums);
//         for(int i = 0; i < nums.length; i++){
//             if(nums[i] == target)
//                 result.add(i);
//         }
//         return result;
//     }
// }

// Clever Solution

class Solution {
    public List<Integer> targetIndices(int[] nums, int target) {
        int lessThanTargetCount = 0;
        int targetCount = 0;
        for (int num : nums) {
            if (num < target) {
                lessThanTargetCount++;
            } else if (num == target) {
                targetCount++;
            }
        }

        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < targetCount; i++) {
            result.add(lessThanTargetCount + i);
        }

        return result;
    }
}
```

---

**Runtime** 1 ms (beats 82.8%) · **Memory** 44.2 MB (beats 98.2%)

<sub>Synced by AILeetHub on 2026-10-19.</sub>
