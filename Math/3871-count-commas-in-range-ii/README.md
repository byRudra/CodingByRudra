# 3871. Count Commas in Range II

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/count-commas-in-range-ii/)

`Math`

## Intuition  
When commas are inserted every three digits from the right, the only places they appear are at the thousand, million, billion … boundaries. After the first thousand numbers (1 000 – 999 999) each integer contributes exactly one comma, after the first million numbers (1 000 000 – 999 999 999) each contributes a second comma, and so on. Therefore the total number of commas equals the sum, over every power‑of‑1000 boundary that is ≤ n, of how many numbers lie at or beyond that boundary. The naïve way would be to iterate over every integer up to n, which is impossible for n up to 10¹⁵. The insight that commas appear in whole blocks defined by powers of 1000 lets us count each block in O(1) time.

## Approach  
1. **Initialize** `count = 0` and `threshold = 1000`.  
2. **Loop while** `n >= threshold`.  
   - *Invariant*: `threshold` is the smallest power of 1000 that is greater than the previous one, and all numbers ≥ `threshold` have at least one more comma than numbers < `threshold`.  
   - **Add** `n - threshold + 1` to `count`. This is exactly the number of integers in `[threshold, n]`, each of which contributes one additional comma for the current power‑of‑1000 level.  
   - **Advance** `threshold *= 1000`. Multiplying by 1000 moves to the next comma‑insertion boundary (thousand → million → billion …).  
3. **Exit** when `threshold` exceeds `n`; at that point no further numbers can add another comma.  
4. **Return** `count`.  

Edge handling:  
- If `n < 1000` the loop condition fails immediately, yielding `0` commas, which matches the definition for one‑ to three‑digit numbers.  
- The loop works for both even and odd numbers of digits because the multiplication by 1000 always jumps to the next exact boundary, avoiding off‑by‑one errors.  
- Using `>=` (not `>`) ensures that a number exactly equal to a boundary (e.g., 1 000) is counted, because it does contain a comma.

## Dry Run  

**Input:** `n = 1 234 567`

| Iteration | `threshold` before update | `n - threshold + 1` added | `count` after addition | Note |
|-----------|---------------------------|---------------------------|------------------------|------|
| 1 | 1 000 | 1 234 567 − 1 000 + 1 = 1 233 568 | 1 233 568 | All numbers 1 000…1 234 567 have one comma |
| 2 | 1 000 000 | 1 234 567 − 1 000 000 + 1 = 234 568 | 1 468 136 | Numbers 1 000 000…1 234 567 have a second comma |
| 3 | 1 000 000 000 | loop stops (`n < threshold`) | – | No numbers reach the billion boundary |

Final state: `count = 1 468 136`, which equals the total commas (1 233 568 from the thousand level + 234 568 from the million level).

## Complexity  
- **Time:** **O(log₁₀₀0 n)** – the loop runs once per power of 1000 up to n; each iteration performs constant‑time arithmetic.  
- **Space:** **O(1)** – only a few primitive variables (`count`, `threshold`) are used, independent of n. (The output itself is not counted as extra space.)

## Solution (Java)

```java
class Solution {
    public long countCommas(long n) {
        long count = 0;
        long threshold = 1000L;

        while (n >= threshold) {
            count += n - threshold + 1;
            threshold *= 1000;
        }

        return count;
    }
}
```

---

**Runtime** 1 ms (beats 99.4%) · **Memory** 42.5 MB (beats 71.2%)

<sub>Synced by AILeetHub on 2026-09-09.</sub>
