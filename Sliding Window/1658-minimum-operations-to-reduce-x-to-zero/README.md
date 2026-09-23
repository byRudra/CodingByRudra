# 1658. Minimum Operations to Reduce X to Zero

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/minimum-operations-to-reduce-x-to-zero/)

`Array` · `Hash Table` · `Binary Search` · `Sliding Window` · `Prefix Sum`

## Intuition  
The key observation is that removing elements from the left or right to reach `x` is equivalent to **keeping** a contiguous sub‑array whose sum equals `total – x`. If we can find the longest such sub‑array, we minimize the number of removed elements because the answer is `n – length`. A naïve approach would try every combination of left/right deletions, which is exponential, or use a hash map of prefix sums, which needs extra space. The insight that the problem reduces to a classic “maximum‑length sub‑array with given sum” lets us solve it with a single linear scan using two pointers.

## Approach  
1. **Compute total sum** – iterate `for (int num : nums)` and accumulate `total`.  
2. **Derive target** – `target = total - x`.  
   - If `target == 0`, the whole array must be removed → return `nums.length`.  
   - If `target < 0`, the required sum exceeds the whole array → return `-1`.  
3. **Initialize sliding window** – `left = 0`, `sum = 0`, `maxLen = -1`.  
4. **Expand window with `right`** – for each `right` from `0` to `nums.length‑1`:
   - Add `nums[right]` to `sum`.  
   - **Maintain invariant** `sum ≤ target` by shrinking from the left while `sum > target`:
     - Subtract `nums[left]` from `sum` and increment `left`.  
   - After the inner `while`, the window `[left, right]` satisfies `sum ≤ target`.  
   - If `sum == target`, update `maxLen` with `right - left + 1`.  
5. **Derive answer** – if `maxLen` stayed `-1` no suitable sub‑array exists → return `-1`; otherwise return `nums.length - maxLen`.  

*Edge handling*: The code treats an empty or single‑element array uniformly; the `while (sum > target && left <= right)` guard prevents `left` from overtaking `right`. The choice of `<=` in the condition ensures the window can shrink to a single element when necessary.

## Dry Run  

**Input**: `nums = [1,1,4,2,3]`, `x = 5`  

| right | left | sum | maxLen | note |
|------|------|-----|--------|------|
| 0 | 0 | 1 | -1 | added nums[0] |
| 1 | 0 | 2 | -1 | added nums[1] |
| 2 | 0 | 6 → shrink → sum=5, left=1 | 2 | removed nums[0] (sum>target) |
| 3 | 1 | 7 → shrink → sum=6, left=2 → shrink → sum=2, left=3 | 2 | removed nums[1] then nums[2] |
| 4 | 3 | 5 | 3 | added nums[4]; sum equals target, window [3,4] length 2 → maxLen=2 (actually length 2, but previous maxLen was 2, now right‑left+1=2) |

After the loop `maxLen = 2`, so answer = `5 - 2 = 3`.  
The longest sub‑array summing to `total‑x = 5` is `[4,2]` (indices 2‑3), length 2, thus we need to remove the remaining three elements, which matches the optimal 2‑operation solution (removing the rightmost two elements).

## Complexity  
- **Time:** `O(n)` – the outer `for` iterates `n` times and the inner `while` moves `left` at most `n` steps total, so each element is processed a constant number of times.  
- **Space:** `O(1)` – only a few integer variables (`total`, `target`, `left`, `right`, `sum`, `maxLen`) are used regardless of input size. (The output integer does not count toward extra space.)

## Solution (Java)

```java
class Solution {
    public int minOperations(int[] nums, int x) {
        int total = 0;

        for (int num : nums) {
            total += num;
        }

        int target = total - x;

        // We need to remove everything
        if (target == 0) {
            return nums.length;
        }

        // Impossible
        if (target < 0) {
            return -1;
        }

        int left = 0;
        int sum = 0;
        int maxLen = -1;

        for (int right = 0; right < nums.length; right++) {
            sum += nums[right];

            while (sum > target && left <= right) {
                sum -= nums[left];
                left++;
            }

            if (sum == target) {
                maxLen = Math.max(maxLen, right - left + 1);
            }
        }

        return maxLen == -1 ? -1 : nums.length - maxLen;
    }
}
```

---

**Runtime** 4 ms (beats 98.1%) · **Memory** 102.3 MB (beats 18.8%)

<sub>Synced by AILeetHub on 2026-09-23.</sub>
