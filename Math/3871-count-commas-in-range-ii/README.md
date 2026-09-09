# 3871. Count Commas in Range II

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/count-commas-in-range-ii/)

`Math`

## Intuition  
When a number reaches a new thousand‑group (10³, 10⁶, 10⁹, …) it gains **one additional comma** compared to all smaller numbers. Therefore the total comma count equals the sum, over every thousand‑group, of how many integers lie in that group or beyond. The naïve way would be to iterate from 1 to n and count commas per number – O(n) time and unnecessary work. The key observation is that each group contributes a simple arithmetic series, so we can add the contribution of each group in constant time using the formula `n - (group‑1)`.

## Approach  
1. **Initialize** `count = 0`.  
2. **First group (10³):**  
   *Condition:* `if (n >= 1_000)`.  
   *Invariant:* All numbers `>= 1_000` have at least one comma.  
   *Action:* `count += n - 999` (the number of integers that possess the first comma).  
3. **Second group (10⁶):**  
   *Condition:* `if (n >= 1_000_000)`.  
   *Invariant:* Every integer `>= 1_000_000` already contributed the first comma; it now needs a **second** comma.  
   *Action:* `count += (n - 999_999) * 1` – adds one extra comma for each such integer.  
4. **Third group (10⁹):**  
   *Condition:* `if (n >= 1_000_000_000L)`.  
   *Invariant:* Numbers `>= 1_000_000_000` have three commas; the third comma is missing from the current total.  
   *Action:* `count += (n - 999_999_999L) * 1`.  
5. **Fourth group (10¹²):**  
   *Condition:* `if (n >= 1_000_000_000_000L)`.  
   *Invariant:* Add the fourth comma to every integer `>= 1_000_000_000_000`.  
   *Action:* `count += (n - 999_999_999_999L) * 1`.  
6. **Fifth group (10¹⁵):**  
   *Condition:* `if (n >= 1_000_000_000_000_000L)`.  
   *Invariant:* Add the fifth comma to every integer `>= 1_000_000_000_000_000`.  
   *Action:* `count += (n - 999_999_999_999_999L) * 1`.  
7. **Return** `count`.  

The code deliberately uses `<=`‑style thresholds (`1_000`, `1_000_000`, …) and subtracts `threshold‑1` so that the range starts exactly at the first number that actually contains the new comma. This avoids off‑by‑one errors and works uniformly for all groups.

## Dry Run  

**Input:** `n = 1_234_567`

| Step | Condition (threshold) | Added (`n - (threshold‑1)`) | `count` after step | Note |
|------|-----------------------|-----------------------------|--------------------|------|
| 1 | `n >= 1_000` | `1_234_567 - 999 = 1_233_568` | 1_233_568 | First comma for every number ≥ 1 000 |
| 2 | `n >= 1_000_000` | `1_234_567 - 999_999 = 234_568` | 1_468_136 | Second comma for numbers ≥ 1 000 000 |
| 3 | `n >= 1_000_000_000` | false | 1_468_136 | No third‑comma contribution (n too small) |
| 4 | `n >= 1_000_000_000_000` | false | 1_468_136 | No fourth‑comma contribution |
| 5 | `n >= 1_000_000_000_000_000` | false | 1_468_136 | No fifth‑comma contribution |

**Final state:** `count = 1_468_136`, which is the exact total number of commas appearing in all integers from 1 to 1,234,567.

## Complexity  
- **Time:** **O(1)** – the algorithm executes a constant number (five) of conditional checks and arithmetic operations, regardless of `n`.  
- **Space:** **O(1)** – only a few primitive variables (`count`, loop‑independent temporaries) are stored; the output itself is not counted toward the space budget.

## Solution (Java)

```java
class Solution {
    public long countCommas(long n) {
        long count = 0;

        if (n >= 1_000) {
            count += n - 999;
        }

        if (n >= 1_000_000) {
            count += (n - 999_999) * 1;
        }

        if (n >= 1_000_000_000L) {
            count += (n - 999_999_999L) * 1;
        }

        if (n >= 1_000_000_000_000L) {
            count += (n - 999_999_999_999L) * 1;
        }

        if (n >= 1_000_000_000_000_000L) {
            count += (n - 999_999_999_999_999L) * 1;
        }

        return count;
    }
}
```

---

**Runtime** 1 ms (beats 99.4%) · **Memory** 42.4 MB (beats 93.6%)

<sub>Synced by AILeetHub on 2026-09-09.</sub>
