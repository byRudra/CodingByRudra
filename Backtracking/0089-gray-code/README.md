# 89. Gray Code

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/gray-code/)

`Math` · `Backtracking` · `Bit Manipulation`

## Intuition  
The key observation is that the Gray code of an integer `i` can be obtained directly from its binary representation: flipping the most‑significant differing bit between `i` and `i>>1` yields a number that differs from the previous Gray code by exactly one bit. This eliminates the need for a recursive construction, a lookup table, or a second traversal to enforce the adjacency property. By applying the formula `i ^ (i >> 1)` for every `i` from `0` to `2ⁿ‑1`, we generate the whole sequence in a single linear pass. The pattern used here is a **bit‑manipulation formula** for Gray codes.

## Approach  
1. **Compute total length** – `size = 1 << n` shifts `1` left `n` times, giving `2ⁿ`, the exact number of codes required.  
2. **Initialize result list** – `result = new ArrayList<>()` will hold the sequence in order.  
3. **Iterate over all indices** – `for (int i = 0; i < size; i++)` runs while `i < size`.  
   - *Invariant*: before each iteration, `result` already contains the Gray codes for all indices `< i`.  
   - Compute the Gray code for the current `i` as `gray = i ^ (i >> 1)`. The right‑shift discards the least‑significant bit, and the XOR flips exactly the bits where `i` and `i>>1` differ, guaranteeing a single‑bit change from the previous code.  
   - Append `gray` to `result`.  
4. **Return the list** – after the loop finishes, `result` holds `size` elements, each a distinct integer in `[0, 2ⁿ‑1]` and satisfying the Gray‑code adjacency constraints.

**Edge‑case handling**:  
- When `n = 0` (not allowed by constraints) the loop would run once, producing `[0]`.  
- For `n = 1` the loop runs twice, yielding `[0,1]`.  
- The shift and XOR operations are safe for `n ≤ 16` because `int` can represent up to `2³¹‑1`. No overflow occurs.

## Dry Run  
Input: `n = 2` (so `size = 4`)

| i | binary i | i>>1 | i ^ (i>>1) (gray) | note |
|---|----------|------|-------------------|------|
| 0 | 00       | 00   | 00 (0)            | first element |
| 1 | 01       | 00   | 01 (1)            | differs by 1 bit from previous |
| 2 | 10       | 01   | 11 (3)            | flips the second bit only |
| 3 | 11       | 01   | 10 (2)            | flips the least‑significant bit |

After the fourth iteration `result = [0,1,3,2]`, which is a valid 2‑bit Gray code because every adjacent pair (including the wrap‑around from 2 back to 0) differs in exactly one bit.

## Complexity  
- **Time:** `O(2ⁿ)` – the loop executes `size = 2ⁿ` times, and each iteration performs only constant‑time bit operations.  
- **Space:** `O(2ⁿ)` – the output list stores `2ⁿ` integers; no additional auxiliary structures are used.

## Solution (Java)

```java
class Solution {
    public List<Integer> grayCode(int n) {
        int size = 1 << n;  
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            result.add(i ^ (i >> 1));
        }
        return result;
    }
}
```

---

**Runtime** 4 ms (beats 99.5%) · **Memory** 53.9 MB (beats 9.8%)

<sub>Synced by AILeetHub on 2026-09-24.</sub>
