# 263. Ugly Number

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/ugly-number/)

`Math`

## Intuition  
The only way a positive integer can fail to be ugly is if, after stripping away every factor 2, 3, and 5, something other than 1 remains. Thus, if we repeatedly divide n by those three primes until none of them divide evenly, the final value tells us everything: 1 means all prime factors were allowed, any other value means an illegal prime factor survived. A naïve solution might try to factor n completely or test divisibility by every prime up to √n, both of which are far more work than needed. The insight that “removing all 2‑, 3‑, 5‑factors leaves either 1 or a disqualifying factor” lets us solve the problem with a handful of simple loops—an instance of the “divide‑out‑unwanted‑primes” pattern.

## Approach  
1. **Reject non‑positive inputs.**  
   - `if (n <= 0) return false;`  
   - Invariant: before any division, `n` is either positive (eligible) or non‑positive (immediately false).  
2. **Handle the special case 1.**  
   - `else if (n == 1) return true;`  
   - Invariant: 1 has no prime factors, so it is ugly by definition.  
3. **Strip factor 2.**  
   - `while (n % 2 == 0) n /= 2;`  
   - Exit condition: `n % 2 != 0`.  
   - Invariant: after each iteration, `n` is the original value divided by a power of 2, and no factor 2 remains.  
4. **Strip factor 3.**  
   - `while (n % 3 == 0) n /= 3;`  
   - Exit condition: `n % 3 != 0`.  
   - Invariant: after this loop, `n` has no factor 3 left, while all earlier 2‑factors are already gone.  
5. **Strip factor 5.**  
   - `while (n % 5 == 0) n /= 5;`  
   - Exit condition: `n % 5 != 0`.  
   - Invariant: after this loop, `n` contains none of the allowed prime factors.  
6. **Final check.**  
   - `return n == 1;`  
   - If any other prime factor existed, it survived the three loops, leaving `n > 1`.  

Edge‑case handling: the code treats zero and negatives uniformly as false, because ugly numbers are defined only for positive integers. The `while` conditions use `== 0` (exact divisibility) rather than `<` to avoid accidental infinite loops when `n` becomes 0 (which never happens after the initial guard).  

## Dry Run  
**Input:** `n = 30`  

| Step | n before iteration | Action                               | n after iteration | Note                         |
|------|-------------------|--------------------------------------|-------------------|------------------------------|
| 1    | 30                | `while (n % 2 == 0) n /= 2`          | 15                | removed one factor 2          |
| 2    | 15                | `while (n % 2 == 0)` fails           | 15                | no more factor 2              |
| 3    | 15                | `while (n % 3 == 0) n /= 3`          | 5                 | removed one factor 3          |
| 4    | 5                 | `while (n % 3 == 0)` fails           | 5                 | no more factor 3              |
| 5    | 5                 | `while (n % 5 == 0) n /= 5`          | 1                 | removed one factor 5          |
| 6    | 1                 | `while (n % 5 == 0)` fails           | 1                 | no more factor 5              |
| 7    | 1                 | `return n == 1`                      | true              | all allowed factors removed   |

The algorithm ends with `n == 1`, so it correctly returns `true`.

## Complexity  
- **Time:** O(log n) – each division reduces `n` by at least a factor of 2, so the total number of loop iterations is bounded by the logarithm of the original value.  
- **Space:** O(1) – only a few primitive variables are used, independent of input size.

## Solution (Java)

```java
class Solution {
    public boolean isUgly(int n) {
        if(n<=0){
            return false;
        }
        else if(n == 1){
            return true;
        }
        while (n % 2 == 0)
            n /= 2;
        while (n % 3 == 0)
            n /= 3;
        while (n % 5 == 0)
            n /= 5;

        return n == 1;
    }
}
```

---

**Runtime** 1 ms (beats 99.1%) · **Memory** 42.3 MB (beats 93.1%)

<sub>Synced by AILeetHub on 2026-09-15.</sub>
