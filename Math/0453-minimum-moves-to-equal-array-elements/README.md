# 453. Minimum Moves to Equal Array Elements

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/minimum-moves-to-equal-array-elements/)

`Array` · `Math`

## Intuition  
The key observation is that adding 1 to every element except one is equivalent to subtracting 1 from that omitted element while leaving the rest unchanged. Therefore, after any sequence of moves the relative differences between elements never change; the only way to make all values equal is to bring every element down to the current minimum. The naive idea would be to simulate each move or repeatedly sort the array, which would be O(n²) or O(n log n). By recognizing the equivalence to “decrement the maximum element” we can compute the answer directly with a single pass that tracks the minimum value and the total sum.

*Pattern:* single‑pass aggregation (two simple variables).

## Approach  
1. **Initialize** `min = Integer.MAX_VALUE` and `sum = 0`.  
2. **Iterate** over `nums` with the enhanced `for` loop:  
   - *Exit condition:* loop ends after the last element; invariant – before each iteration `sum` equals the total of all processed elements and `min` is the smallest among them.  
   - Update `sum += num`.  
   - Update `min = Math.min(min, num)`.  
3. **Compute** the result with the formula `sum - min * nums.length`.  
   - The expression equals the total number of required decrements of all elements down to `min`, which is exactly the number of “increment‑n‑1” moves needed.  

**Edge handling:**  
- If the array has a single element, the loop runs once, `min` becomes that element, and the formula yields `0` because `sum == min * n`.  
- Negative numbers are handled naturally; `min` may be negative, and the arithmetic stays within 32‑bit range as guaranteed by the problem.  
- The code chooses `<=` in `Math.min` implicitly via the library call; this ensures that when multiple elements share the minimum, `min` remains that value without ambiguity.

## Dry Run  

Input: `nums = [1, 2, 3]`

| Iteration | `num` | `sum` (after) | `min` (after) | Note                              |
|-----------|------|---------------|--------------|-----------------------------------|
| 1         | 1    | 1             | 1            | First element sets both sum/min   |
| 2         | 2    | 3             | 1            | `min` stays 1 (1 < 2)             |
| 3         | 3    | 6             | 1            | `min` stays 1 (1 < 3)             |

After the loop: `sum = 6`, `min = 1`, `n = 3`.  
Result = `6 - 1 * 3 = 3`, which matches the three required moves.

## Complexity  
- **Time:** **O(n)** – the single `for` loop visits each of the `n` elements exactly once.  
- **Space:** **O(1)** – only two primitive variables (`min`, `sum`) are used regardless of input size; the output integer does not count toward extra space.

## Solution (Java)

```java
class Solution {
    public int minMoves(int[] nums) {
        int min = Integer.MAX_VALUE;
        int sum = 0;
        for(int num : nums){
            sum += num;
            min = Math.min(min, num);
        }

        return sum - min * nums.length;
    }
}
```

---

**Runtime** 3 ms (beats 27.6%) · **Memory** 46.5 MB (beats 94.1%)

<sub>Synced by AILeetHub on 2026-11-01.</sub>
