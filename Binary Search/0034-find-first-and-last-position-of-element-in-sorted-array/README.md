# 34. Find First and Last Position of Element in Sorted Array

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/)

`Array` · `Binary Search`

## Intuition  
The key observation is that in a sorted array the leftmost (rightmost) occurrence of a target can be found by a binary search that, once it lands on the target, continues to shrink the right (left) side of the search interval. This eliminates the need for a second linear scan or a hash map. By running the same binary‑search skeleton twice—once biasing left, once biasing right—we obtain both boundaries in O(log n) time. The pattern used is **binary search with a directional bias**.

## Approach  
1. **Initialize** `left = 0`, `right = nums.length‑1`, `first = -1`.  
2. **First binary search (leftmost index)**  
   - Loop while `left <= right`.  
   - Compute `mid = left + (right‑left)/2`.  
   - **Invariant:** the target, if present, lies in `[left, right]`.  
   - If `nums[mid] == target`, record `first = mid` and move `right = mid‑1` to keep looking leftward.  
   - Else if `target > nums[mid]`, discard left half with `left = mid + 1`.  
   - Else (`target < nums[mid]`), discard right half with `right = mid‑1`.  
   - The loop ends when the interval is empty; `first` holds the smallest index or stays `-1`.  
3. **Reset** `left = 0`, `right = nums.length‑1`, `last = -1`.  
4. **Second binary search (rightmost index)** – identical structure, but when `nums[mid] == target` we set `last = mid` and advance `left = mid + 1` to keep searching rightward.  
5. **Return** `{first, last}`.  
   - Edge cases: empty array (`right = -1` makes the loop skip), single‑element array (both searches handle `left == right` correctly), and absent target (both `first` and `last` remain `-1`).  
   - The code uses `<=` in the loop condition to ensure the final candidate index is examined; using `<` would miss the case where `left == right` holds the target.

## Dry Run  

**Input:** `nums = [5,7,7,8,8,10]`, `target = 8`

| Phase | left | right | mid | first | last | Note |
|------|------|-------|-----|-------|------|------|
| 1st search | 0 | 5 | 2 | -1 | – | `nums[2]=7 < 8` → `left=3` |
| 1st search | 3 | 5 | 4 | -1 | – | `nums[4]=8` → `first=4`, `right=3` |
| 1st search | 3 | 3 | 3 | 3 | – | `nums[3]=8` → `first=3`, `right=2` (loop ends) |
| 2nd search | 0 | 5 | 2 | 3 | -1 | `nums[2]=7 < 8` → `left=3` |
| 2nd search | 3 | 5 | 4 | 3 | -1 | `nums[4]=8` → `last=4`, `left=5` |
| 2nd search | 5 | 5 | 5 | 3 | 5 | `nums[5]=10 > 8` → `right=4` (loop ends) |

After both phases, `first = 3` and `last = 4`, which are exactly the required boundaries.

## Complexity  
- **Time:** `O(log n)` – each binary search halves the interval, running at most ⌈log₂ n⌉ iterations; two independent searches keep the same asymptotic bound.  
- **Space:** `O(1)` – only a constant number of integer variables (`left`, `right`, `mid`, `first`, `last`) are used; the output array does not count toward auxiliary space.

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

**Runtime** 0 ms (beats 100.0%) · **Memory** 48 MB (beats 71.6%)

<sub>Synced by AILeetHub on 2026-09-06.</sub>
