# 1362. Closest Divisors

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/closest-divisors/)

`Math` · `Prime Factorization`

## Intuition  
The product of two integers that is closest to a given number is obtained when the pair straddles the square root of that number – the farther a factor is from √target, the larger the absolute difference between the two factors. Therefore, for each candidate `target = num+1` or `num+2` we only need to search downward from `⌊√target⌋` until we hit a divisor; the first divisor found yields the pair with minimal difference for that target. A naïve approach would try every possible factor up to `target`, costing O(target) time, or store all factor pairs and sort them, costing extra space. The observation above eliminates both extra passes and auxiliary storage, leaving a simple two‑nested‑loop scan that runs in O(√num) time. This is the classic “search from the square root” pattern for divisor problems.

## Approach  
1. **Initialize result containers** – `ans` holds the best pair, `minDiff` is set to `Integer.MAX_VALUE`.  
2. **Iterate over the two candidates** – `for (int target : new int[]{num + 1, num + 2})`.  
   - *Invariant*: before each iteration `minDiff` stores the smallest difference seen among all previously processed targets.  
3. **Search for the closest divisor of `target`** – `for (int i = (int) Math.sqrt(target); i >= 1; i--)`.  
   - *Exit condition*: loop stops when `i` reaches 0 or when a divisor is found and `break` is executed.  
   - *Invariant*: at the start of each inner iteration, all integers greater than `i` have been proven not to divide `target`. Hence `i` is the largest possible divisor ≤ √target.  
4. **When a divisor is found** (`if (target % i == 0)`):  
   - Compute `a = i` and `b = target / i`.  
   - `diff = b - a` is the absolute difference (non‑negative because `b ≥ a`).  
   - If `diff < minDiff`, update `minDiff` and store the pair in `ans`.  
   - `break` out of the inner loop because any smaller `i` would only increase the difference.  
5. **Return** `ans` after both targets have been examined.

Edge cases handled explicitly:  
- `num = 1` yields targets 2 and 3; the inner loop still runs because `Math.sqrt` returns 1, and the divisor check succeeds.  
- For odd vs. even `target`, the downward search automatically lands on the appropriate divisor without separate parity logic.  
- The `break` ensures we never examine more than one divisor per target, avoiding off‑by‑one errors.

## Dry Run  
**Input:** `num = 8`  

| step | target | i (√down) | a = i | b = target/i | diff = b‑a | minDiff before | ans before | action |
|------|--------|-----------|------|--------------|------------|----------------|------------|--------|
| 1    | 9      | 3         | 3    | 3            | 0          | INF            | [0,0]      | diff < INF → minDiff=0, ans=[3,3]; break |
| 2    | 10     | 3         | 3    | 3 (remainder) → not divisor | – | 0 | [3,3] | continue |
| 3    | 10     | 2         | 2    | 5            | 3          | 0              | [3,3]      | diff (3) ≥ minDiff → no update; break |

After processing both candidates, `ans = [3,3]`, which indeed has the smallest possible difference.

## Complexity  
- **Time:** O(√(num+2)) ≈ O(√num) because the outer loop runs twice and the inner loop scans at most √target steps, stopping early after the first divisor.  
- **Space:** O(1) extra space; only a few primitive variables and the fixed‑size `ans` array are used, independent of input size.

## Solution (Java)

```java
class Solution {
    public int[] closestDivisors(int num) {
        int[] ans = new int[2];
        int minDiff = Integer.MAX_VALUE;

        for (int target : new int[]{num + 1, num + 2}) {
            for (int i = (int) Math.sqrt(target); i >= 1; i--) {
                if (target % i == 0) {
                    int a = i;
                    int b = target / i;
                    int diff = b - a;

                    if (diff < minDiff) {
                        minDiff = diff;
                        ans[0] = a;
                        ans[1] = b;
                    }
                    break;
                }
            }
        }

        return ans;
    }
}
```

---

**Runtime** 6 ms (beats 82.7%) · **Memory** 42.1 MB (beats 97.8%)

<sub>Synced by AILeetHub on 2026-10-19.</sub>
