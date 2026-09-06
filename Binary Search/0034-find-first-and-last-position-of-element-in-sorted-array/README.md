# 34. Find First and Last Position of Element in Sorted Array

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/)

`Array` · `Binary Search`

## Intuition  
When the array is sorted, any occurrence of the target partitions the array into three contiguous blocks: values < target, the block of target values, and values > target. If we can locate the leftmost index of that middle block, the rightmost index follows symmetrically. A naïve linear scan would cost O(n), and a single binary search only tells us whether the target exists. The key observation is that binary search can be steered after a hit: by continuing the search on the left half we pin down the first position, and by continuing on the right half we pin down the last. This yields two independent O(log n) passes, a classic **two‑phase binary search** pattern.

## Approach  
1. **Initialize** `left = 0`, `right = nums.length‑1`, `first = -1`.  
2. **First binary search** (find leftmost):  
   - Loop while `left <= right`.  
   - Compute `mid = left + (right‑left)/2`.  
   - If `nums[mid] == target`, record `first = mid` **and** shrink the right side with `right = mid‑1` to keep looking left.  
   - Else if `target > nums[mid]`, move left bound: `left = mid + 1`.  
   - Else (`target < nums[mid]`), move right bound: `right = mid ‑ 1`.  
   - Invariant: the target, if present, lies in `[left, right]`; after each iteration the interval halves.  
3. **Reset** `left = 0`, `right = nums.length‑1`, `last = -1`.  
4. **Second binary search** (find rightmost):  
   - Same loop condition and mid computation.  
   - On a hit, set `last = mid` **and** advance left side with `left = mid + 1` to keep searching right.  
   - The other two branches are identical to step 2.  
   - Invariant mirrors step 2 but now the interval contracts toward the rightmost occurrence.  
5. **Return** `new int[]{first, last}`.  
   - Edge cases: empty array (`right = -1` makes the loop skip, leaving `first`/`last` as ‑1); single‑element array works because the same hit logic updates both indices correctly. The code deliberately uses `<=` for the loop guard to ensure the final candidate index is examined.

## Dry Run  
Input: `nums = [5,7,7,8,8,10]`, `target = 8`

| Iter | left | right | mid | first | last | note |
|------|------|-------|-----|-------|------|------|
| 1 (first) | 0 | 5 | 2 | -1 | - | `nums[2]=7 < 8` → `left=3` |
| 2 (first) | 3 | 5 | 4 | -1 | - | `nums[4]=8` → `first=4`, `right=3` |
| 3 (first) | 3 | 3 | 3 | 3 | - | `nums[3]=8` → `first=3`, `right=2` (loop ends) |
| 1 (last) | 0 | 5 | 2 | - | -1 | `nums[2]=7 < 8` → `left=3` |
| 2 (last) | 3 | 5 | 4 | - | 4 | `nums[4]=8` → `last=4`, `left=5` |
| 3 (last) | 5 | 5 | 5 | - | 4 | `nums[5]=10 > 8` → `right=4` (loop ends) |

After both passes `first = 3`, `last = 4`, which are exactly the required boundaries.

## Complexity  
- **Time:** O(log n) + O(log n) = O(log n) – each binary search halves the search interval, running at most ⌈log₂ n⌉ iterations.  
- **Space:** O(1) – only a handful of integer variables are used; the output array does not count toward auxiliary space.

## Solution (Java)

```java
class Solution {
    public int[] searchRange(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        int result[] = new int[2];

        int first = -1;

        while(left <= right){
            int mid = left + (right - left) / 2;
            
            if(nums[mid] == target){
                first = mid;
                right = mid - 1;
            } else if(target > nums[mid]){
                left = mid + 1;
            } else{
                right = mid - 1; 
            }
        }

        left = 0;
        right = nums.length - 1;

        int last = -1;
        
        while(left <= right){
            int mid = left + (right - left) / 2;
        
            if(nums[mid] == target){
                last = mid;
                left = mid + 1;
            } else if(target > nums[mid]){
                left = mid + 1;
            } else{
                right = mid - 1; 
            }
        }
        
        return new int[]{first, last};
    }
}
```

---

**Runtime** 83 ms (beats 0.4%) · **Memory** 48 MB (beats 71.6%)

<sub>Synced by AILeetHub on 2026-09-06.</sub>
