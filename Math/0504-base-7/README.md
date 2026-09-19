# 504. Base 7

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/base-7/)

`Math` · `String`

## Intuition  
The base‑7 representation can be obtained by repeatedly extracting the least‑significant digit with `num % 7` and then discarding it with integer division `num /= 7`. Each extraction yields the next digit **in reverse order**, so the string built during the loop must be reversed at the end. This observation eliminates the need for a second pass, a lookup table, or recursion. The only extra work is handling the sign, which is done by remembering whether the original number was negative and appending `'-'` after the digits are collected. The overall pattern is a classic “repeated division” with a post‑process reversal.

## Approach  
1. **Zero shortcut** – If `num == 0`, return `"0"` immediately; no loop is needed.  
2. **Sign handling** – Store `negative = num < 0`. Convert `num` to its absolute value with `Math.abs(num)` so the loop works with non‑negative integers only.  
3. **Digit extraction loop** –  
   - **Condition:** `while (num > 0)` – the loop stops exactly when all digits have been consumed.  
   - **Invariant:** At the start of each iteration, `sb` holds the base‑7 digits of the original absolute value **in reverse order** for the part already processed, and `num` is the remaining high‑order portion.  
   - **Body:** Compute `remainder = num % 7`; append `remainder` to `sb`; then shrink `num` with `num /= 7`.  
4. **Attach sign** – After the loop, if `negative` is true, append `'-'` to `sb`. The sign is placed after the digits because the final reversal will bring it to the front.  
5. **Reverse and return** – Call `sb.reverse()` and convert to a string. The reversal restores the correct most‑significant‑to‑least‑significant order and moves a possible leading `'-'` to the front.

**Edge‑case decisions:**  
- The early `if (num == 0)` avoids an empty `StringBuilder` and the need for a special “no‑digit” check after the loop.  
- Using `Math.abs` is safe under the given constraints (`|num| ≤ 10⁷`), so overflow on `Integer.MIN_VALUE` cannot occur.  
- The sign is appended **before** reversal rather than prepended, because `StringBuilder` has O(1) `append` but O(n) `insert(0, …)`.  

## Dry Run  

Input: `100`

| Iteration | `num` before | `remainder` (`num % 7`) | `sb` after `append` | Note |
|-----------|--------------|------------------------|---------------------|------|
| 1         | 100          | 2                      | "2"                 | Least‑significant digit captured |
| 2         | 14 (`100/7`) | 0                      | "20"                | Next digit added (still reversed) |
| 3         | 2 (`14/7`)   | 2                      | "202"               | Final digit; loop ends after `num` becomes 0 |
| End       | 0            | –                      | "202" → reverse → "202" (sign absent) | Reversed string equals the correct base‑7 representation |

The final reversed string `"202"` is exactly the base‑7 form of 100.

## Complexity  
- **Time:** O(log₇ |num|) – each loop iteration divides `num` by 7, so the number of iterations equals the number of base‑7 digits.  
- **Space:** O(log₇ |num|) – the `StringBuilder` stores one character per digit (plus an optional sign); the output itself is not counted as extra space.

## Solution (Java)

```java
class Solution {
    public String convertToBase7(int num) {
        if (num == 0) {
            return "0";
        }

        boolean negative = num < 0;
        num = Math.abs(num);

        StringBuilder sb = new StringBuilder();

        while (num > 0) {
            int remainder = num % 7;
            sb.append(remainder);
            num /= 7;
        }

        if (negative) {
            sb.append('-');
        }

        return sb.reverse().toString();
    }
}
```

---

**Runtime** 1 ms (beats 72.3%) · **Memory** 42.9 MB (beats 24.9%)

<sub>Synced by AILeetHub on 2026-09-19.</sub>
