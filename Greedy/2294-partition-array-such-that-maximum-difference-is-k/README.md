# 2294. Partition Array Such That Maximum Difference Is K

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/partition-array-such-that-maximum-difference-is-k/)

`Array` · `Greedy` · `Sorting`

## Intuition  
If the array is sorted, any valid subsequence must occupy a contiguous block: once the smallest element of a block is fixed, every later element that exceeds `start + k` would break the “max‑min ≤ k” rule, so it cannot belong to the same subsequence. The naive way would be to try all partitions (exponential) or to maintain a complex data structure to track ranges, both of which cost extra passes or memory. By sorting first, the problem collapses to a single linear scan where we greedily cut a new group exactly when the current element is farther than `k` from the block’s first element. This is the classic **greedy after sorting** pattern.

## Approach  
1. **Sort the input** – `Arrays.sort(nums)`. After this step the numbers are in non‑decreasing order, guaranteeing that any element later in the array is ≥ any earlier element.  
2. **Initialize** – `group = 0` counts finished groups, `start = nums[0]` records the minimum of the current group (the first element of the sorted array).  
3. **Iterate** – `for (int x : nums)` processes each element in order.  
   - **Loop invariant:** `start` is the smallest value of the group that is currently being built, and all previously seen elements belong to either this group or to one of the `group` completed groups.  
   - **Decision:** if `k < x - start` (i.e., `x - start > k`), the current element cannot stay in the existing group. We finalize the current group by `group++` and start a new one with `start = x`.  
   - Otherwise, `x` fits into the current group and we simply continue; `start` remains unchanged because it is already the minimum of the group.  
4. **Finalize** – after the loop, the last open group has not been counted yet, so `return ++group;` increments `group` once more and returns the total number of subsequences.  
**Edge handling:** The constraints guarantee `nums.length ≥ 1`, so accessing `nums[0]` is safe. The comparison uses `<` rather than `<=` because the condition should trigger only when the difference strictly exceeds `k`. The algorithm chooses the convention of starting a new group at the offending element itself, which yields the minimal number of groups.

## Dry Run  

Input: `nums = [3,6,1,2,5]`, `k = 2`  

Sorted array: `[1,2,3,5,6]`

| iteration (x) | start before | group before | condition `k < x-start`? | action | start after | group after | note |
|---------------|--------------|--------------|--------------------------|--------|-------------|-------------|------|
| 1 | 1 | 0 | 2 < 1‑1 = false | keep | 1 | 0 | first element, starts group |
| 2 | 1 | 0 | 2 < 2‑1 = false | keep | 1 | 0 | still within k |
| 3 | 1 | 0 | 2 < 3‑1 = false | keep | 1 | 0 | still within k |
| 5 | 1 | 0 | 2 < 5‑1 = true | new group | 5 | 1 | 5 exceeds allowed range, close previous |
| 6 | 5 | 1 | 2 < 6‑5 = false | keep | 5 | 1 | fits in second group |

Loop ends → `++group` makes `group = 2`. The algorithm returns `2`, which matches the optimal partition `[1,2,3]` and `[5,6]`.

## Complexity  
- **Time:** `O(n log n)` – sorting dominates; the subsequent linear scan runs `n` times, each iteration doing only constant work.  
- **Space:** `O(1)` extra – only a few integer variables (`group`, `start`, loop variable) are used; the sort is in‑place and the output array is not counted.

## Solution (Java)

```java
class Solution {
    public int partitionArray(int[] nums, int k) {
        Arrays.sort(nums);
        int group = 0;
        int start = nums[0];
        for(int x : nums){
            if(k < x - start){
                group++;
                start = x;
            }
        }
        return ++group;

    }
}
```

---

**Runtime** 35 ms (beats 91.3%) · **Memory** 99 MB (beats 96.4%)

<sub>Synced by AILeetHub on 2026-10-13.</sub>
