# 154. Find Minimum in Rotated Sorted Array II

![Hard](https://img.shields.io/badge/Difficulty-Hard-ff375f?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/find-minimum-in-rotated-sorted-array-ii/)

`Array` · `Binary Search`

## Intuition  
The key observation is that the minimum element is always the leftmost value in the current search interval `[left, right]` because the array is a rotated sorted sequence. After each iteration we can shrink this interval while preserving the invariant: **the sub‑array `nums[left…right]` still contains the global minimum**. A naïve linear scan would cost O(n); the insight that the relative order of `nums[mid]` and `nums[right]` tells us which half can be discarded eliminates the extra pass. The only wrinkle introduced by duplicates is that `nums[mid] == nums[right]` gives no ordering information, so we safely discard `right` by one step. This is the classic *binary search with duplicate handling* pattern.

## Approach  
1. **Initialize** `left = 0`, `right = nums.length - 1`.  
2. **Loop while** `left < right`. The invariant holds: the minimum lies in `nums[left…right]`.  
3. Compute `mid = left + (right - left) / 2`.  
4. **Compare** `nums[mid]` with `nums[right]`  
   - If `nums[mid] > nums[right]`, the minimum must be to the right of `mid`; set `left = mid + 1`.  
   - Else if `nums[mid] < nums[right]`, the minimum is at `mid` or to its left; set `right = mid`.  
   - Else (`nums[mid] == nums[right]`), we cannot decide; decrement `right--` to shrink the interval without losing the minimum (the rightmost element cannot be strictly smaller than the minimum).  
5. When the loop exits, `left == right`; return `nums[left]`.  

**Edge handling**:  
- A single‑element array satisfies `left == right` immediately, returning that element.  
- All‑duplicate arrays repeatedly hit the equality case, causing `right` to move left one step each iteration, which degrades to O(n) but still respects the invariant.  
- The `right--` uses `<=` in the loop condition (`left < right`) so we never skip the last candidate.

## Dry Run  
Input: `nums = [2, 2, 2, 0, 1]`

| Iter | left | mid | right | nums[left] | nums[mid] | nums[right] | Action / Note |
|------|------|-----|-------|------------|-----------|-------------|----------------|
| 1    | 0    | 2   | 4     | 2          | 2         | 1           | `nums[mid] > nums[right]` → `left = mid + 1` |
| 2    | 3    | 3   | 4     | 0          | 0         | 1           | `nums[mid] < nums[right]` → `right = mid` |
| 3    | 3    | 3   | 3     | 0          | 0         | 0           | loop ends (`left == right`) |

The algorithm terminates with `left = 3`, and `nums[3] = 0`, which is the minimum.

## Complexity  
- **Time:** O(log n) on average because each iteration discards roughly half of the interval; in the worst case (all duplicates) the loop may shrink by one element each time, yielding O(n).  
- **Space:** O(1) extra space, as the algorithm uses only a few integer variables regardless of input size.

## Solution (Java)

```java
// class Solution {
//     public int findMin(int[] nums) {
//         int min = Integer.MAX_VALUE;
//         for(int num : nums){
//             min = Math.min(min, num);
//         }
//         return min;
//     }
// }

// Using Binary Search

class Solution {
    public int findMin(int[] nums) {
        int left = 0, right = nums.length - 1;
        while (left < right) {

            int mid = left + (right - left) / 2;

            if (nums[mid] > nums[right]) {
                left = mid + 1;
            } else if (nums[mid] < nums[right]) {
                right = mid;
            } else {
                right--;
            }
        }
        return nums[left];
    }
}
```

---

**Runtime** 0 ms (beats 100.0%) · **Memory** 44.8 MB (beats 73.4%)

<sub>Synced by AILeetHub on 2026-09-09.</sub>
