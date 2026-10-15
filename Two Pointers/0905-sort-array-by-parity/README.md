# 905. Sort Array By Parity

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/sort-array-by-parity/)

`Array` · `Two Pointers` · `Sorting`

## Intuition  
The key observation is that while scanning the array from left to right, every time we encounter an even number we can safely place it at the current “front” of the array because everything before the front index is already known to be even. After processing the element at index `right`, the pointer `left` always marks the first position that has not yet been confirmed to be even. This invariant lets us finish the task in a single pass, eliminating the need for a second traversal, extra storage, or sorting. The technique is the classic **two‑pointer (partition) pattern**.

## Approach  
1. **Initialize** `left = 0`. This pointer will trail the next slot that should hold an even number.  
2. **Iterate** `right` from `0` to `nums.length‑1` (inclusive).  
   - **Loop condition:** `right < nums.length`.  
   - **Invariant:** All indices `< left` contain even numbers; indices in `[left, right)` contain odd numbers (or are untouched).  
3. **Check parity** of `nums[right]`.  
   - If `nums[right] % 2 == 0` (even):  
     a. Swap `nums[left]` and `nums[right]`. The swap works even when `left == right`, so no special case is needed.  
     b. Increment `left` (`left++`). Now the invariant still holds because the newly placed even number sits at index `left‑1`.  
   - If odd: do nothing; the invariant remains unchanged.  
4. **Finish** the loop. At exit, `left` points just past the last even element, and the suffix `[left … n‑1]` consists solely of odd numbers.  
5. **Return** `nums`, which has been rearranged in‑place.

Edge cases are handled automatically: an empty or single‑element array never enters the body that swaps, and the `right` loop runs zero or one times respectively. The `<=` vs `<` decision is irrelevant here because the loop uses `< nums.length`, guaranteeing we never read past the array.

## Dry Run  

**Input:** `nums = [3, 1, 2, 4]`

| right | left | nums after iteration            | Action                               |
|------|------|----------------------------------|--------------------------------------|
| 0    | 0    | `[3, 1, 2, 4]`                  | `nums[0]` is odd → no swap          |
| 1    | 0    | `[3, 1, 2, 4]`                  | `nums[1]` is odd → no swap          |
| 2    | 0    | `[2, 1, 3, 4]`                  | swap `nums[0]`↔`nums[2]`, `left=1`   |
| 3    | 1    | `[2, 4, 3, 1]`                  | swap `nums[1]`↔`nums[3]`, `left=2`   |

The loop ends (`right == 4`). All even numbers (`2, 4`) occupy indices `< left` (0‑1) and all odds (`3, 1`) occupy the remainder, satisfying the required ordering.

## Complexity  
- **Time:** `O(n)` – the single `for` loop visits each element once; `right` advances `n` steps while `left` moves at most `n` steps.  
- **Space:** `O(1)` – only a few integer variables (`left`, `right`, `temp`) are used; the rearrangement is performed in‑place, not counting the output array itself.

## Solution (Java)

```java
class Solution {
    public int[] sortArrayByParity(int[] nums) {
        int left = 0;
        for (int right = 0; right < nums.length; right++) {
            if (nums[right] % 2 == 0) {
                int temp = nums[left];
                nums[left] = nums[right];
                nums[right] = temp;
                left++;
            }
        }
        return nums;

    }
}
```

---

**Runtime** 1 ms (beats 42.4%) · **Memory** 46.1 MB (beats 99.6%)

<sub>Synced by AILeetHub on 2026-10-15.</sub>
