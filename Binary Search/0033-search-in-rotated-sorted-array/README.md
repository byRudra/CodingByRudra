# 33. Search in Rotated Sorted Array

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/search-in-rotated-sorted-array/)

`Array` · `Binary Search`

## Intuition  
In a rotated sorted array one half of any interval `[start, end]` remains in normal ascending order. After each midpoint calculation we can tell which side is ordered by comparing `nums[start]` and `nums[mid]`. If the target lies inside that ordered half we discard the other half; otherwise we keep the unordered half. This observation eliminates the need for a separate pass to locate the rotation pivot or for extra storage, turning the naïve O(n) scan into a true O(log n) binary search. The pattern used is a **modified two‑pointer binary search**.

## Approach  
1. Initialise `start = 0` and `end = nums.length - 1`.  
2. Loop while `start <= end`.  
   - Compute `mid = start + (end - start) / 2`.  
   - **Exit condition**: if `nums[mid] == target` return `mid`.  
   - Determine which side is sorted:  
     *If `nums[start] <= nums[mid]`* the left half `[start, mid]` is ordered.  
       - If `nums[start] <= target && target < nums[mid]` the target must be in this half, so set `end = mid - 1`.  
       - Otherwise the target is in the right half, set `start = mid + 1`.  
     *Else* the right half `[mid, end]` is ordered.  
       - If `target > nums[mid] && target <= nums[end]` keep the right half by `start = mid + 1`.  
       - Otherwise keep the left half by `end = mid - 1`.  
3. When the loop terminates without a match, return `-1`.  

**Edge handling**:  
- A single‑element array works because `start == end` initially; the loop checks `mid` directly.  
- The comparison `nums[start] <= nums[mid]` uses `<=` to treat a completely sorted segment (no rotation) as the “left ordered” case, avoiding an off‑by‑one flip when `start == mid`.  
- All index updates move past `mid` (`mid ± 1`) to guarantee progress and prevent infinite loops.

## Dry Run  

Input: `nums = [4,5,6,7,0,1,2]`, `target = 0`

| iteration | start | end | mid | note |
|-----------|-------|-----|-----|------|
| 1 | 0 | 6 | 3 | `nums[mid]=7` > target; left side `[0,3]` is ordered, target not in it → `start = 4` |
| 2 | 4 | 6 | 5 | `nums[mid]=1` > target; right side `[5,6]` is ordered, target not in it → `end = 4` |
| 3 | 4 | 4 | 4 | `nums[mid]=0` equals target → return 4 |

The algorithm stops after three iterations with `mid = 4`, which is the correct index because the ordered‑half checks correctly guided the search.

## Complexity  
- **Time:** `O(log n)` – each loop halves the search interval because `mid` moves either `start` or `end` past the current midpoint.  
- **Space:** `O(1)` – only a few integer variables (`start`, `end`, `mid`) are used; no additional data structures or recursion stack are allocated.

## Solution (Java)

```java
class Solution {
    public int search(int[] nums, int target) {
        int start = 0, end = nums.length - 1;
        while(start <= end){
            int mid = start + (end - start) / 2;
            if(nums[mid] == target) return mid;
            
            if(nums[start] <= nums[mid]){
                if(nums[start] <= target && target < nums[mid]){
                    end = mid - 1;
                }
                else{
                    start = mid + 1;
                }
            }
            else{
                 if(target  <= nums[end] && target > nums[mid]){
                    start = mid + 1;
                }
                else{
                    end = mid - 1;
                }
            }
        }
        return -1;
    }
}
```

---

**Runtime** 0 ms (beats 100.0%) · **Memory** 44 MB (beats 11.5%)

<sub>Synced by AILeetHub on 2026-01-11.</sub>
