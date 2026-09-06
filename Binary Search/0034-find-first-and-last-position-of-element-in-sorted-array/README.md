# 34. Find First and Last Position of Element in Sorted Array

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/)

`Array` · `Binary Search`

## Intuition  
When the array is sorted, any occurrence of the target splits the array into three monotonic parts: left of the first occurrence, the block of equal values, and right of the last occurrence. A standard binary search can locate *some* target index in O(log n), but we need the exact boundaries of that block. The key observation is that after finding a target at index mid, we can continue the search on the left side to see whether an earlier occurrence exists, and similarly on the right side for a later one. By performing two independent binary searches—one that always discards the right half after a hit, the other that always discards the left half—we obtain the smallest and largest indices without any extra passes, hash maps, or sorting.

## Approach  
1. **Initialize** `left = 0`, `right = nums.length‑1`, `first = -1`.  
2. **First‑position loop** (`while left <= right`):  
   - Compute `mid = left + (right‑left)/2`.  
   - *Invariant*: the target, if present, lies within `[left, right]`.  
   - If `nums[mid] == target`, record `first = mid` and move **leftward** by setting `right = mid‑1` (searches earlier indices).  
   - Else if `target > nums[mid]`, discard left half: `left = mid + 1`.  
   - Else (`target < nums[mid]`), discard right half: `right = mid ‑ 1`.  
   - Loop exits when the search window collapses, leaving `first` as the leftmost hit or `‑1` if none.  
3. **Reset** `left = 0`, `right = nums.length‑1`, `last = -1`.  
4. **Last‑position loop** (`while left <= right`):  
   - Same `mid` computation and invariant.  
   - If `nums[mid] == target`, record `last = mid` and move **rightward** by setting `left = mid + 1` (searches later indices).  
   - The two `else‑if` branches are identical to step 2, preserving the binary‑search invariant.  
5. **Return** `[first, last]`. The code uses `new int[]{first, last}` directly, so no extra container is allocated beyond the constant‑size result array.

*Edge handling*:  
- Empty array ⇒ `right = -1`, loop condition fails immediately, both indices stay `‑1`.  
- Single‑element array works because `mid` equals `left` equals `right`.  
- The `<=` comparison ensures the window containing a single candidate is still examined; using `<` would skip that final check.  

## Dry Run  

**Input**: `nums = [5,7,7,8,8,10]`, `target = 8`

| Iter | left | right | mid | first | last | note |
|------|------|-------|-----|-------|------|------|
| 1 (first) | 0 | 5 | 2 | -1 | – | `nums[2]=7 < 8` → `left=3` |
| 2 (first) | 3 | 5 | 4 | -1 | – | `nums[4]=8` → `first=4`, `right=3` |
| 3 (first) | 3 | 3 | 3 | 3 | – | `nums[3]=8` → `first=3`, `right=2` (loop ends) |
| 1 (last) | 0 | 5 | 2 | 3 | -1 | `nums[2]=7 < 8` → `left=3` |
| 2 (last) | 3 | 5 | 4 | 3 | 4 | `nums[4]=8` → `last=4`, `left=5` |
| 3 (last) | 5 | 5 | 5 | 3 | 4 | `nums[5]=10 > 8` → `right=4` (loop ends) |

Final state: `first = 3`, `last = 4`, which correctly describes the range of `8`.

## Complexity  
- **Time:** O(log n) + O(log n) = O(log n) because each binary search halves the interval, running at most ⌈log₂ n⌉ iterations.  
- **Space:** O(1) extra space (the two pointers and result array are constant‑size, independent of n).

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
            }
            else if(target > nums[mid]){
                left = mid + 1;
            }
            else{
                right = mid - 1; 
            }
        }
        int last = -1;
        left = 0;
        right = nums.length - 1;
        while(left <= right){
            int mid = left + (right - left) / 2;
            if(nums[mid] == target){
                last = mid;
                left = mid + 1;
            }
            else if(target > nums[mid]){
                left = mid + 1;
            }
            else{
                right = mid - 1; 
            }
        }
        return new int[]{first, last};
    }
}
```

---

**Runtime** 0 ms (beats 100.0%) · **Memory** 48.1 MB (beats 71.6%)

<sub>Synced by AILeetHub on 2026-09-06.</sub>
