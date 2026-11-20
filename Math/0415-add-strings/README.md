# 415. Add Strings

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/add-strings/)

`Math` · `String` · `Simulation`

## Intuition  
When two decimal numbers are added, each column (least‑significant digit first) contributes a digit to the answer and possibly a carry to the next column. The key observation is that we can simulate this column‑wise process directly on the character arrays of the strings: after *k* iterations the algorithm has processed the last *k* digits of both inputs, and the variable `carry` always holds the exact overflow that must be added to the next more‑significant column. This eliminates the need for a second pass, a hash map, or any big‑integer library; we only need a single linear scan from right to left.

## Approach  
1. **Initialize pointers** `i = num1.length() - 1` and `j = num2.length() - 1` so they point to the least‑significant characters of each string. Set `carry = 0` and create an empty `StringBuilder result`.  
2. **Loop condition** `while (i >= 0 || j >= 0 || carry > 0)`. The invariant: before each iteration `carry` equals the overflow from the previously processed column, and all digits to the right of `i` and `j` have already been appended (in reverse order) to `result`.  
3. **Compute column sum** `int sum = carry`. If `i >= 0`, add `num1.charAt(i--) - '0'` to `sum`; similarly for `j`. The post‑decrement moves the pointers leftward after the digit is consumed.  
4. **Extract digit and new carry**: `result.append(sum % 10)` stores the current column’s digit (still reversed). `carry = sum / 10` captures any overflow for the next iteration.  
5. **Exit** when both pointers have moved past the first character and no carry remains; at this point `result` holds the answer backwards.  
6. **Reverse** the builder (`result.reverse()`) and convert to `String`. The reversal restores the correct most‑significant‑first order.

Edge‑case handling:  
- Empty strings never occur because of the constraints, but the loop also works if one string is shorter; the corresponding `if` simply skips adding a digit.  
- When both inputs are `"0"` the loop runs once because `carry` is initially `0` but the condition `i >= 0 || j >= 0` is true, producing `"0"` after reversal.  
- The `carry > 0` clause ensures an extra digit is emitted for sums like `9 + 1 = 10`.

## Dry Run  

**Input:** `num1 = "456"`, `num2 = "77"`  

| Iter | i (after) | j (after) | carry (before) | sum (calc) | result (reversed) | Note |
|------|-----------|-----------|----------------|-----------|-------------------|------|
| 1    | 1         | 1         | 0              | 6+7+0=13  | "3"               | digit 3, carry 1 |
| 2    | 0         | 0         | 1              | 5+7+1=13  | "33"              | digit 3, carry 1 |
| 3    | -1        | -1        | 1              | 4+0+1=5   | "335"             | digit 5, carry 0 |
| 4    | -1        | -1        | 0              | loop ends |               | reverse → "533" |

After reversal the builder contains `"533"`, which is the correct sum.

## Complexity  
- **Time:** O(n + m) – the loop advances at most one step for each character of `num1` and `num2`, plus a possible final iteration for a leftover carry.  
- **Space:** O(max(n, m)) – the `StringBuilder` stores exactly the number of digits of the result, which is at most one more than the longer input; auxiliary variables use O(1) extra space.

## Solution (Java)

```java
class Solution {
    public String addStrings(String num1, String num2) {
         StringBuilder result = new StringBuilder();
        int i = num1.length() - 1;
        int j = num2.length() - 1;
        int carry = 0;

        while (i >= 0 || j >= 0 || carry > 0) {
            int sum = carry;
            if (i >= 0) sum += num1.charAt(i--) - '0';
            if (j >= 0) sum += num2.charAt(j--) - '0';
            result.append(sum % 10);
            carry = sum / 10;
        }
        return result.reverse().toString();
    }
}
```

---

**Runtime** 2 ms (beats 97.3%) · **Memory** 43.3 MB (beats 85.4%)

<sub>Synced by AILeetHub on 2026-11-20.</sub>
