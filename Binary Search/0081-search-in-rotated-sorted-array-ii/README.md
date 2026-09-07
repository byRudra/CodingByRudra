# 81. Search in Rotated Sorted Array II

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/search-in-rotated-sorted-array-ii/)

`Array` · `Binary Search`

## Intuition  
In a rotated sorted array, one side of any middle index is guaranteed to be monotonically non‑decreasing—*unless* the values at `start`, `mid` and `end` are all equal, in which case the ordering information is lost. The key observation is that when the three endpoints differ, we can decide which half contains the target by comparing it with the ordered side, thereby discarding half the search space each iteration. The naïve linear scan would be O(n); the insight lets us keep the binary‑search spirit while safely handling duplicates by shrinking the ambiguous borders one step at a time. This pattern is a **modified two‑pointer binary search**.

## Approach  
1. Initialise `start = 0` and `end = nums.length‑1`.  
2. Loop while `start <= end`.  
   * **Invariant:** the sub‑array `nums[start…end]` still may contain `target`.  
3. Compute `mid = start + (end‑start)/2`.  
4. If `nums[mid] == target`, return `true`.  
5. If `nums[start] == nums[mid] && nums[mid] == nums[end]`  
   * The ordering on both sides is ambiguous; increment `start` and decrement `end` to shrink the window, preserving the invariant.  
6. Else if `nums[start] <= nums[mid]` (left half is ordered)  
   * If `nums[start] <= target && target < nums[mid]` → target lies in the left half, set `end = mid‑1`.  
   * Otherwise, discard the left half by `start = mid + 1`.  
7. Else (right half is ordered)  
   * If `target > nums[mid] && target <= nums[end]` → target lies in the right half, set `start = mid + 1`.  
   * Otherwise, discard the right half by `end = mid‑1`.  
8. If the loop exits, `target` is absent; return `false`.  

Edge cases handled explicitly: empty or single‑element arrays (the loop condition fails or succeeds immediately), even‑length versus odd‑length windows, and duplicate clusters that would otherwise break the classic rotated‑array binary search. The choice of `<=` in the ordered‑half check (`nums[start] <= nums[mid]`) follows the inclusive definition of a non‑decreasing segment.

## Dry Run  

**Input:** `nums = [2,5,6,0,0,1,2]`, `target = 0`

| Iter | start | end | mid | nums[mid] | Action | Note |
|------|-------|-----|-----|-----------|--------|------|
| 1 | 0 | 6 | 3 | 0 | `nums[mid]==target` → return true | Found at index 3 |
| (loop ends) |   |   |   |   |   |   |

For illustration, if `target = 3` the table would be:

| Iter | start | end | mid | nums[mid] | Action | Note |
|------|-------|-----|-----|-----------|--------|------|
| 1 | 0 | 6 | 3 | 0 | left ordered (`nums[start] <= nums[mid]` false) → right half, `start = 4` | discard left |
| 2 | 4 | 6 | 5 | 1 | left ordered (`nums[start] <= nums[mid]`) true → target not in left, `start = 6` | shrink |
| 3 | 6 | 6 | 6 | 2 | left ordered true, target not in left, `start = 7` | loop exits |
| 4 | – | – | – | – | return false | target absent |

The final state (`start > end`) confirms that `3` does not exist in the array.

## Complexity  
- **Time:** O(n) worst case – the loop may degrade to linear when many duplicates cause the `start++/end--` step each iteration, but each iteration still performs O(1) work.  
- **Space:** O(1) – only a few integer pointers (`start`, `end`, `mid`) are used, independent of input size.

## Solution (Java)

```java
class Solution {
    public boolean search(int[] nums, int target) {
        int start = 0, end = nums.length - 1;
        while (start <= end) {
            int mid = start + (end - start) / 2;
            if (nums[mid] == target)
                return true;
            if (nums[start] == nums[mid] && nums[mid] == nums[end]) {
                start++;
                end--;
            } else if (nums[start] <= nums[mid]) {
                if (nums[start] <= target && target < nums[mid]) {
                    end = mid - 1;
                } else {
                    start = mid + 1;
                }
            } else {
                if (target <= nums[end] && target > nums[mid]) {
                    start = mid + 1;
                } else {
                    end = mid - 1;
                }
            }
        }
        return false;
    }
}
```

---

**Runtime** 0 ms (beats 100.0%) · **Memory** 44.9 MB (beats 82.0%)

<sub>Synced by AILeetHub on 2026-09-07.</sub>
