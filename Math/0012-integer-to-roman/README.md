# 12. Integer to Roman

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/integer-to-roman/)

`Hash Table` · `Math` · `String`

## Intuition  
The Roman system can be expressed as a sorted list of atomic values, each of which already incorporates the required subtractive forms (e.g., 900 = CM). If we always take the largest atomic value that does not exceed the remaining integer, the remainder shrinks monotonically and the symbols we emit are guaranteed to be in the correct order. This greedy observation removes the need for a separate pass per decimal digit, a hash‑map lookup for each place, or any recursion. The pattern is a classic **greedy scan with parallel value/symbol arrays**.

## Approach  
1. **Prepare parallel arrays** `values` and `symbols` in strictly descending order, including the six subtractive entries (900, 400, 90, 40, 9, 4).  
2. **Iterate over the indices** `i = 0 … values.length‑1`. The outer `for` loop guarantees we consider each atomic value exactly once, preserving the required high‑to‑low ordering.  
3. **Greedy inner loop** `while (num >= values[i])`.  
   - *Exit condition*: the loop stops as soon as `num` becomes smaller than the current atomic value.  
   - *Invariant*: before each iteration, `result` already encodes the Roman representation of the original input minus the current `num`, and `num` is always non‑negative and less than `values[i]` after the inner loop finishes.  
   - Inside the loop we `result.append(symbols[i])` and `num -= values[i]`. This consumes one atomic chunk and records its symbol.  
4. **Termination**: after the last index the inner loop has reduced `num` to zero, so `result` holds the complete Roman numeral.  
5. **Edge handling**: the algorithm works for the minimum (`1`) and maximum (`3999`) because the arrays cover the full range; no special case for odd/even length or duplicate symbols is required—the greedy subtraction naturally respects the “no more than three consecutive I, X, C, M” rule.

## Dry Run  

**Input:** `3749`

| Step | i | values[i] | symbols[i] | num before | result after | Note |
|------|---|-----------|------------|------------|--------------|------|
| 1 | 0 | 1000 | M | 3749 | M | 3749 ≥ 1000, subtract 1000 |
| 2 | 0 | 1000 | M | 2749 | MM | repeat while condition |
| 3 | 0 | 1000 | M | 1749 | MMM | repeat while condition |
| 4 | 1 | 900 | CM | 749 | MMMCM | 749 ≥ 900? no → exit inner loop |
| 5 | 2 | 500 | D | 749 | MMMCMD | 749 ≥ 500, subtract 500 |
| 6 | 3 | 400 | CD | 249 | MMMCMDCD | 249 ≥ 400? no |
| 7 | 4 | 100 | C | 249 | MMMCMDCDC | subtract 100 twice |
| 8 | 4 | 100 | C | 149 | MMMCMDCDCC | subtract 100 again |
| 9 | 5 | 90 | XC | 49 | MMMCMDCDCCXC | 49 ≥ 90? no |
|10 | 7 | 40 | XL | 49 | MMMCMDCDCCXCXL | 49 ≥ 40, subtract 40 |
|11 | 9 | 9 | IX | 9 | MMMCMDCDCCXCXLIX | 9 ≥ 9, subtract 9 → num = 0 |

The loop ends with `num = 0` and `result = "MMMDCCXLIX"`, which is the correct Roman numeral for 3749.

## Complexity  
- **Time:** O(k) = O(1) – the outer loop runs over a constant‑size array of 13 entries, and the inner `while` executes once per emitted Roman character; the total number of characters for any input ≤ 3999 is bounded by a small constant (≤ 15).  
- **Space:** O(1) – only a few integer variables and the `StringBuilder` are used; the output string itself is not counted against the auxiliary space.

## Solution (Java)

```java
class Solution {
    public String intToRoman(int num) {
        int[] values = {
            1000, 900, 500, 400,
            100, 90, 50, 40,
            10, 9, 5, 4, 1
        };

        String[] symbols = {
            "M", "CM", "D", "CD",
            "C", "XC", "L", "XL",
            "X", "IX", "V", "IV", "I"
        };

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < values.length; i++) {
            while (num >= values[i]) {
                result.append(symbols[i]);
                num -= values[i];
            }
        }

        return result.toString();
    }
}
```

---

**Runtime** 3 ms (beats 99.8%) · **Memory** 46.4 MB (beats 42.6%)

<sub>Synced by AILeetHub on 2026-09-12.</sub>
