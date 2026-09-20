# 3498. Reverse Degree of a String

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/reverse-degree-of-a-string/)

`String` · `Simulation`

## Intuition  
The only thing that matters for each character is two numbers that are already available: its 1‑based position in the string and its rank in the reversed alphabet (`'a' → 26`, `'b' → 25`, …, `'z' → 1`). Multiplying these two values and adding them yields the contribution of that character to the final sum. A naïve solution might first build a lookup table for the reversed ranks or perform a second pass to translate each letter, which would add unnecessary overhead. The observation that the reversed rank can be computed on the fly with the expression `('z' - ch + 1)` lets us finish the whole task in a single linear scan—an instance of the classic **single‑pass iteration** pattern.

## Approach  
1. **Initialize** `result = 0`.  
2. **Iterate** `i` from `1` through `s.length()` (inclusive).  
   - *Loop condition*: `i <= s.length()`.  
   - *Invariant*: before each iteration, `result` equals the sum of contributions of the first `i‑1` characters, and `i` is the index of the next character to process.  
3. **Fetch current character** with `s.charAt(i‑1)`. The `-1` offset aligns the 1‑based loop index with Java’s 0‑based string indexing.  
4. **Compute reversed alphabet value**: `('z' - currentChar + 1)`. This works because `'z' - 'a' = 25`, so subtracting the character from `'z'` yields the distance from the end of the alphabet, and adding `1` converts it to a 1‑based rank.  
5. **Multiply** the reversed value by the position `i` to obtain the character’s contribution.  
6. **Accumulate** the contribution into `result`.  
7. **Return** `result` after the loop finishes.  

**Edge‑case handling**:  
- The loop naturally skips execution when `s` is empty, returning `0`; the problem guarantees at least one character, so this path is never exercised.  
- Using `i <= s.length()` (instead of `<`) ensures the last character (position `s.length()`) is processed.  
- The `i‑1` index prevents an `ArrayIndexOutOfBoundsException` and aligns the 1‑based position with Java’s 0‑based indexing.

## Dry Run  

Input: `s = "abc"`

| i | char | revIdx = `'z' - char + 1` | product = revIdx × i | result after iteration | note |
|---|------|---------------------------|----------------------|------------------------|------|
| 1 | a    | 26                        | 26 × 1 = 26          | 26                     | first character processed |
| 2 | b    | 25                        | 25 × 2 = 50          | 76 (=26+50)            | second character processed |
| 3 | c    | 24                        | 24 × 3 = 72          | 148 (=76+72)           | third character processed |

After the loop, `result = 148`, which matches the required reverse degree.

## Complexity  
- **Time:** O(n) – the `for` loop runs exactly `n = s.length()` times, performing O(1) work per iteration.  
- **Space:** O(1) – only a few primitive variables (`result`, `i`, temporary calculations) are used; the output integer does not count toward auxiliary space.

## Solution (Java)

```java
class Solution {
    public int reverseDegree(String s) {
        int result = 0;
        for (int i = 1; i <= s.length(); i++){
            int value = ('z' - s.charAt(i - 1) + 1) * i;
            result += value;
        }
        return result;
    }
}
```

---

**Runtime** 1 ms (beats 100.0%) · **Memory** 43.9 MB (beats 67.4%)

<sub>Synced by AILeetHub on 2026-09-20.</sub>
