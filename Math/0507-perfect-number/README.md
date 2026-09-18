# 507. Perfect Number

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/perfect-number/)

`Math`

## Intuition  
The key observation is that divisors of `num` come in complementary pairs `(i, num/i)`. Traversing only up to √`num` guarantees we encounter each pair exactly once, so we can accumulate the sum of proper divisors in a single pass. A naïve solution would test every integer from 1 to `num‑1`, costing O(n) time; the pairing insight eliminates that extra factor. This is the classic **two‑pointer‑like** divisor‑pair enumeration (often described as “iterate to sqrt”).

## Approach  
1. **Handle trivial inputs** – If `num <= 1` return `false` because 1 has no proper divisor sum equal to itself.  
2. **Initialize accumulator** – Set `sum = 1` because 1 is a proper divisor of any `num > 1`.  
3. **Iterate over possible small divisors** –  
   - Loop variable `i` starts at 2 and continues while `i * i <= num`.  
   - **Invariant:** At the start of each iteration, `sum` equals the total of all proper divisors discovered so far, and every integer `d` with `2 ≤ d < i` that divides `num` has already contributed both `d` and its counterpart `num/d` (unless they are equal).  
   - Inside the loop, check `num % i == 0`. If true, `i` is a divisor.  
   - Add `i` to `sum`.  
   - If `i` is not the square root (`i != num / i`), also add the complementary divisor `num / i`. This avoids double‑counting when `num` is a perfect square.  
4. **Terminate loop** – The condition `i * i <= num` fails when `i` exceeds √`num`; at that point every divisor pair has been processed.  
5. **Final comparison** – Return `num == sum`; the equality holds exactly when `num` is perfect.

Edge‑case decisions:  
- The algorithm treats `num = 1` as non‑perfect by the early guard.  
- For even vs. odd `num`, the loop works uniformly; no special handling is needed.  
- The `i * i <= num` guard prevents overflow because `i` is an `int` and `num ≤ 10⁸`, so `i*i` stays within 32‑bit range.  
- Using `<=` (not `<`) ensures the square‑root divisor itself is examined, which is necessary for perfect squares like 4.

## Dry Run  

**Input:** `num = 28`

| Iteration | i | Condition `num % i == 0` | sum before | sum after | Note |
|-----------|---|--------------------------|------------|-----------|------|
| 1 | 2 | true | 1 | 1 + 2 + 14 = 17 | 2 and 28/2 added |
| 2 | 3 | false | 17 | 17 | no divisor |
| 3 | 4 | true | 17 | 17 + 4 + 7 = 28 | 4 and 28/4 added |
| 4 | 5 | false | 28 | 28 | loop ends because 5*5 > 28 |

After the loop, `sum = 28`, which equals `num`, so the method returns `true`. The table stops at the iteration where `i*i` exceeds `num`, confirming that all divisor pairs have been accounted for.

## Complexity  
- **Time:** O(√n) – the `for` loop runs at most √`num` times because `i` increments by 1 and stops when `i*i > num`.  
- **Space:** O(1) – only a few primitive variables (`sum`, `i`) are used; the algorithm does not allocate extra data structures.

## Solution (Java)

```java
class Solution {
    public boolean checkPerfectNumber(int num) {
        if (num <= 1)
            return false;

        int sum = 1;
        for (int i = 2; i * i <= num; i++) {
            if (num % i == 0) {
                sum += i;
                if (i != num / i)
                    sum += num / i;
            }

        }
        return num == sum;
    }
}
```

---

**Runtime** 62 ms (beats 66.4%) · **Memory** 42.1 MB (beats 44.4%)

<sub>Synced by AILeetHub on 2026-09-18.</sub>
