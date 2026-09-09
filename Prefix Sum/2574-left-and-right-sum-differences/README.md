# 2574. Left and Right Sum Differences

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/left-and-right-sum-differences/)

`Array` · `Prefix Sum`

## Intuition  
The key observation is that the sum of elements left of index *i* can be built incrementally: each step adds the previous element to the running total, and similarly the sum of elements right of *i* can be built by walking backward and adding the next element. By updating both directions in a single pass we avoid a second traversal or extra data structures. The naive solution would compute each left and right sum independently, costing O(n²) time; the incremental insight reduces it to linear time. This is a classic **prefix‑sum** pattern applied from both ends simultaneously.

## Approach  
1. **Initialise helper arrays** – `leftSum` and `rightSum` of length *n*. Set `leftSum[0] = 0` because nothing lies left of the first element, and `rightSum[n‑1] = 0` because nothing lies right of the last element.  
2. **Prepare two pointers** – `start = 1` moves forward from the second position, `end = n‑2` moves backward from the penultimate position.  
3. **Iterate while `start < n`**:  
   - **Update left prefix**: `leftSum[start] = leftSum[start‑1] + nums[start‑1]`. The invariant is that after each iteration `leftSum[k]` holds the sum of all elements before index *k*.  
   - **Update right suffix** (guarded by `if (end >= 0)`): `rightSum[end] = rightSum[end+1] + nums[end+1]`. The invariant is that after each iteration `rightSum[k]` holds the sum of all elements after index *k*.  
   - Increment `start` and decrement `end` to advance both directions symmetrically.  
   - The loop terminates when `start` reaches *n*; at that moment every position has its left and right sums computed, even for odd‑length arrays where the two pointers cross.  
4. **Combine results** – iterate `i` from `0` to `n‑1` and replace `leftSum[i]` with `Math.abs(leftSum[i] - rightSum[i])`. This overwrites the left‑sum array with the final answer, saving an extra allocation.  
5. **Return** the mutated `leftSum` array, which now contains `|leftSum[i] - rightSum[i]|` for each index.

Edge cases handled explicitly:  
- Single‑element input (`n == 1`) leaves both helper arrays as `[0]`, and the final loop yields `[0]`.  
- The `if (end >= 0)` guard prevents out‑of‑bounds writes when `n` is 1 or 2.  
- The loop condition `start < nums.length` (rather than `<=`) ensures we stop after processing the last valid `start` index.

## Dry Run  

**Input:** `nums = [10, 4, 8, 3]` (n = 4)

| iteration | start | end | leftSum (partial)                | rightSum (partial)               | note                                   |
|-----------|-------|-----|----------------------------------|----------------------------------|----------------------------------------|
| 0 (init)  | 1     | 2   | `[0, _, _, _]`                   | `[_, _, _, 0]`                   | bases set                              |
| 1         | 1→2  | 2→1 | `leftSum[1] = 0+10 = 10`         | `rightSum[2] = 0+3 = 3`          | first forward & backward update        |
| 2         | 2→3  | 1→0 | `leftSum[2] = 10+4 = 14`         | `rightSum[1] = 3+8 = 11`         | second forward & backward update       |
| 3         | 3→4  | 0→‑1| `leftSum[3] = 14+8 = 22`         | `rightSum[0] = 11+4 = 15`        | final forward update; backward guard true |
| 4         | stop (4 == n) |   |                                 |                                  | loop ends                               |

After the second loop, `leftSum = [0,10,14,22]` and `rightSum = [15,11,3,0]`.  
Applying the absolute difference yields `[15,1,11,22]`, which is the returned array.

## Complexity  
- **Time:** **O(n)** – the while‑loop runs *n‑1* times (each iteration advances `start` by one), and the final for‑loop scans the array once.  
- **Space:** **O(n)** – two auxiliary integer arrays of size *n* are allocated; the output reuses `leftSum`, so no extra output‑specific space is needed.

## Solution (Java)

```java
class Solution {
    public int[] leftRightDifference(int[] nums) {
        int leftSum[] = new int[nums.length];
        leftSum[0] = 0;
        int rightSum[] = new int[nums.length];
        rightSum[nums.length - 1] = 0;
        int start = 1, end = nums.length - 2;
        while (start < nums.length) {
            leftSum[start] = leftSum[start - 1] + nums[start - 1];
            if (end >= 0) {
                rightSum[end] = rightSum[end + 1] + nums[end + 1];
            }
            start++;
            end--;
        }

        for (int i = 0; i < nums.length; i++) {
            leftSum[i] = Math.abs(leftSum[i] - rightSum[i]);
        }
        return leftSum;
    }
}
```

---

**Runtime** 2 ms (beats 99.2%) · **Memory** 46.4 MB (beats 88.0%)

<sub>Synced by AILeetHub on 2026-09-09.</sub>
