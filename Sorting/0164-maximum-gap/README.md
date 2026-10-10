# 164. Maximum Gap

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/maximum-gap/)

`Array` · `Sorting` · `Bucket Sort` · `Radix Sort` · `Pigeonhole Principle`

## Intuition  
If the array is placed in non‑decreasing order, the largest gap can only appear between two consecutive elements—any larger distance would be split by an intermediate value. The naïve way of checking every pair costs O(n²); sorting collapses the problem to a single linear scan, eliminating the need for extra data structures or multiple passes. This solution therefore follows the classic “sort‑then‑scan” pattern.

## Approach  
1. **Sort the whole array** – `Arrays.sort(nums)`. The call rearranges `nums` in ascending order in‑place.  
2. **Initialize the answer** – `int maxGap = 0`. This variable will hold the greatest adjacent difference seen so far.  
3. **Iterate from the second element to the end** – `for (int i = 1; i < nums.length; i++)`.  
   - **Exit condition:** the loop stops when `i` reaches `nums.length`.  
   - **Invariant:** before each iteration, `maxGap` equals the maximum of `nums[j] - nums[j‑1]` for all processed indices `1 ≤ j < i`.  
   - Inside the body compute `int diff = nums[i] - nums[i‑1]` and update `maxGap = Math.max(maxGap, diff)`.  
4. **Return the result** – after the loop finishes, `maxGap` is the maximum gap of the sorted array, which is also the answer for the original unsorted input.  

*Edge handling:* If `nums.length < 2`, the loop body never executes, leaving `maxGap` at 0, which matches the required output. The sort routine safely deals with empty or single‑element arrays, so no extra guard is needed.

## Dry Run  

Input: `nums = [3, 6, 9, 1]`

| i | nums[i] | nums[i‑1] | diff = nums[i]‑nums[i‑1] | maxGap (after update) | note |
|---|---------|-----------|--------------------------|-----------------------|------|
| 1 | 3       | 1         | 2                        | 2                     | first adjacent pair |
| 2 | 6       | 3         | 3                        | 3                     | larger gap found |
| 3 | 9       | 6         | 3                        | 3                     | gap equals current max |

After the loop, `maxGap = 3`, which is the maximum difference between successive elements in the sorted form `[1, 3, 6, 9]`.

## Complexity  
- **Time:** **O(n log n)** – the dominant cost is `Arrays.sort`, which runs in `n log n` time; the subsequent linear scan is O(n).  
- **Space:** **O(1)** extra – sorting is performed in‑place, and only a few primitive variables (`maxGap`, `i`, `diff`) are allocated besides the input array. (The output integer does not count toward space.)

## Solution (Java)

```java
class Solution {
    public int maximumGap(int[] nums) {
        Arrays.sort(nums);
        int maxGap = 0;
        for(int i = 1; i < nums.length; i++){
            maxGap = Math.max(maxGap, nums[i] - nums[i - 1]);
        }
        return maxGap;
    }
}
```

---

**Runtime** 45 ms (beats 53.1%) · **Memory** 91.2 MB (beats 15.5%)

<sub>Synced by AILeetHub on 2026-10-10.</sub>
