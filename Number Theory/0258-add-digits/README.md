# 258. Add Digits

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/add-digits/)

`Math` · `Simulation` · `Number Theory`

## Intuition  
The only thing that changes when we repeatedly replace a number by the sum of its decimal digits is its magnitude: each transformation shrinks the value dramatically because the sum of at most 10 digits cannot exceed 90. Consequently, after a handful of steps the number is guaranteed to become a single digit. The naïve way would be to convert the integer to a string or to recurse until the length is one, both of which add extra overhead. By observing that a simple loop can keep applying the digit‑sum operation until `num <= 9`, we eliminate any need for recursion, extra containers, or string manipulation. This is the classic **simulation with two nested loops** pattern.

## Approach  
1. **Outer loop condition** – `while (num > 9)`.  
   *Invariant:* At the start of each iteration `num` holds the result of the previous digit‑sum and is guaranteed to be ≥ 10, so another reduction is required.  
2. **Compute digit sum** – assign `num = sumOfDigits(num)`.  
   *Decision:* We call a helper rather than inline the logic to keep the outer loop clean and to avoid off‑by‑one errors in the inner loop.  
3. **Helper `sumOfDigits`** – `int sum = 0; while (num > 0) { sum += num % 10; num /= 10; } return sum;`.  
   *Invariant:* Each inner iteration extracts the least‑significant digit (`num % 10`) and removes it (`num /= 10`). The loop stops when `num` becomes 0, meaning all digits have been consumed.  
   *Edge handling:* If the original `num` is already a single digit (including `0`), the outer condition fails immediately, returning the input unchanged. No special case for negative numbers is needed because the constraints guarantee non‑negative input.  

## Dry Run  

**Input:** `38`

| Outer Iter | Inner Iter | `num` before inner | `sum` after inner | `num` after assignment | Note |
|-----------|------------|-------------------|-------------------|------------------------|------|
| 1 | 1 | 38 | 8 (8 + 0) | 11 | extracted 8 |
|   | 2 | 3 | 11 (8 + 3) | 11 | extracted 3, inner loop ends |
| 2 | 1 | 11 | 1 (1 + 0) | 2 | extracted 1 |
|   | 2 | 1 | 2 (1 + 1) | 2 | extracted 1, inner loop ends |
| 3 | – | 2 | – | – | outer condition `num > 9` fails, return 2 |

After the second outer iteration `num` equals 2, which satisfies `num <= 9`; the algorithm terminates and returns the correct single‑digit result.

## Complexity  
- **Time:** **O(d)**, where *d* is the number of decimal digits of the original `num`. Each digit is visited at most twice (once in the first sum, once in the second if needed), so the total work is proportional to the digit count.  
- **Space:** **O(1)**, only a few integer variables (`sum`, `num`) are used regardless of input size; no auxiliary data structures or recursion stack are allocated.

## Solution (Java)

```java
class Solution {
    public int addDigits(int num) {
        while(num > 9){
            num = sumOfDigits(num);
        }
        return num;
    }
    public int sumOfDigits(int num){
        int sum = 0;
        while(num > 0){
            sum += num % 10;
            num /= 10;
        }
        return sum;
    }
}
```

---

**Runtime** 2 ms (beats 1.9%) · **Memory** 42.5 MB (beats 77.8%)

<sub>Synced by AILeetHub on 2026-10-17.</sub>
