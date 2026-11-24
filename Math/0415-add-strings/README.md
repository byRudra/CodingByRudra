# 415. Add Strings

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/add-strings/)

`Math` · `String` · `Simulation`

## Intuition  
When we add two decimal numbers digit by digit from right to left, each step only needs the two current digits and a possible carry from the previous step. The key observation is that **the carry after processing the k‑th least‑significant position never depends on any more significant digits**; it is fully determined by the already‑processed suffixes. Therefore a single linear scan from the ends of the strings suffices, eliminating the need for a second pass, a hash map, or any big‑integer library.

## Approach  
1. **Initialize pointers** `i = num1.length() - 1` and `j = num2.length() - 1` so they point to the least‑significant characters, and set `carry = 0`.  
2. **Loop while** `i >= 0 || j >= 0 || carry > 0`.  
   - *Invariant*: before each iteration, `result` (still reversed) contains the sum of the already‑processed suffixes, and `carry` equals the overflow that must be added to the next more‑significant digit.  
3. **Compute `sum`** starting from `carry`. If `i >= 0`, add `num1.charAt(i--) - '0'`; similarly for `j`. The post‑decrement guarantees the pointer moves left after the digit is consumed, handling both even and odd length inputs uniformly.  
4. **Append the current digit** `sum % 10` to `result`. This stores the digit in reverse order, which is convenient because we are moving from least to most significant.  
5. **Update `carry`** to `sum / 10`. Since `sum` is at most `9 + 9 + 1 = 19`, `carry` is either `0` or `1`, so overflow is bounded and no overflow guard is needed.  
6. **After the loop**, reverse `result` to obtain the correct order and return it as a string.  
Edge cases such as a single‑character input or both strings being `"0"` are naturally covered: the loop runs once, `sum` becomes `0`, and the final reversal yields `"0"`.

## Dry Run  
Input: `num1 = "456", num2 = "77"`  

| Iter | i (after) | j (after) | carry (in) | sum | result (reversed) | Note |
|------|-----------|-----------|------------|-----|-------------------|------|
| 1    | 1         | 0         | 0          | 6+7=13 | "3"               | digit 3, carry 1 |
| 2    | 0         | -1        | 1          | 5+1=6  | "33"              | digit 3, carry 0 |
| 3    | -1        | -1        | 0          | 4     | "333"             | digit 4, carry 0 |
| 4    | -1        | -1        | 0          | loop ends | – | no more digits, carry 0 |

After reversing `"333"` we obtain `"534"`, which is the correct sum of 456 + 77.

## Complexity  
- **Time:** `O(max(n, m))` – the while‑loop iterates once per digit of the longer string plus at most one extra iteration for a final carry.  
- **Space:** `O(max(n, m))` – `result` stores one character per processed digit (the output itself); no additional data structures are used.

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

**Runtime** 2 ms (beats 98.1%) · **Memory** 43.5 MB (beats 52.5%)

<sub>Synced by AILeetHub on 2026-11-24.</sub>
