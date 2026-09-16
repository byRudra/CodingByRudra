# 1621. Number of Sets of K Non-Overlapping Line Segments

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/number-of-sets-of-k-non-overlapping-line-segments/)

`Math` · `Dynamic Programming` · `Combinatorics` · `Prefix Sum`

## Intuition  
The key observation is that a set of k non‑overlapping segments on points 0…n‑1 can be represented uniquely by choosing 2k “effective” endpoints among n + k ‑ 1 positions after we contract each mandatory interior point of a segment into a “gap”. This bijection turns the counting problem into a single multinomial coefficient  
\[
\frac{(n+k-1)!}{(2k)!\,(n-k-1)!}.
\]  
A naïve DP would scan the line O(n·k) times or use a combinatorial DP table, both O(nk) ≈ 10⁶ operations. The closed‑form formula eliminates the extra pass and any auxiliary DP array, leaving only a few factorial evaluations.

The pattern used is **combinatorial closed‑form counting with modular arithmetic**.

## Approach  
1. **Compute the numerator** – `numerator = factorial(n + k - 1)`.  
   *Loop invariant*: after processing `i` (2 ≤ i ≤ current), `ans = i! mod MOD`.  
2. **Compute the two denominator parts** –  
   * `denominator1 = factorial(2 * k)` (product of 1…2k).  
   * `denominator2 = factorial(n - k - 1)` (product of 1…n‑k‑1).  
   Both loops share the same invariant as step 1.  
3. **Combine denominators** – `denominator = (denominator1 * denominator2) % MOD`.  
   The multiplication is performed modulo MOD to avoid overflow.  
4. **Find modular inverse of the denominator** – `modInverse(denominator)` uses Fermat’s little theorem because MOD is prime.  
   * Inside `power(a, b)`, the invariant is “`ans` equals a^(original b ‑ current b) mod MOD”. The loop halves `b` each iteration, guaranteeing O(log b) steps.  
5. **Multiply numerator by the inverse** – `answer = numerator * modInverse(denominator) % MOD`.  
   This yields the binomial‑like value modulo MOD, which is returned as an `int`.  

Edge handling:  
* When `n = 2` and `k = 1`, the loops still run because `n+k-1 = 2` and `2k = 2`; factorial(0) is never called.  
* The code never accesses negative indices because constraints guarantee `n‑k‑1 ≥ 0`.  

## Dry Run  

**Input:** `n = 4, k = 2`

| Step | i (factorial loop) | ans (mod) | note |
|------|-------------------|-----------|------|
| 1️⃣ numerator loop (n+k‑1 = 5) | 2 → 2 | 2 | 2! |
|  | 3 → 6 | 6 | 3! |
|  | 4 → 24 | 24 | 4! |
|  | 5 → 120 | 120 | 5! = numerator |
| 2️⃣ denominator1 loop (2k = 4) | 2 → 2 | 2 | 2! |
|  | 3 → 6 | 6 | 3! |
|  | 4 → 24 | 24 | 4! = denominator1 |
| 3️⃣ denominator2 loop (n‑k‑1 = 1) | (no iteration) | 1 | factorial(1)=1 |
| 4️⃣ combine denominators | – | denominator = 24·1 % MOD = 24 | |
| 5️⃣ modular inverse (pow) | b=MOD‑2 ≈ 1e9+5 → series of squarings | inv = 41666667 | 24⁻¹ mod MOD |
| 6️⃣ final answer | – | 120·41666667 % MOD = 5 | returned value |

The final state is `answer = 5`, matching the five valid segment sets.

## Complexity  
- **Time:** O(n + k + log MOD) → the two factorial loops run up to `n+k-1` (≤ 2000) and the fast‑power loop runs O(log MOD) ≈ 30 steps.  
- **Space:** O(1) → only a handful of `long` variables are kept; the output array is not counted.

## Solution (Java)

```java
class Solution {
    static final long MOD = 1000000007;

    public int numberOfSets(int n, int k) {

        long numerator = factorial(n + k - 1);

        long denominator1 = factorial(2 * k);
        long denominator2 = factorial(n - k - 1);

        long denominator = (denominator1 * denominator2) % MOD;

        long answer = numerator * modInverse(denominator) % MOD;

        return (int) answer;
    }

    long factorial(int n) {
        long ans = 1;

        for (int i = 2; i <= n; i++) {
            ans = (ans * i) % MOD;
        }

        return ans;
    }

    long modInverse(long n) {
        return power(n, MOD - 2);
    }

    long power(long a, long b) {
        long ans = 1;

        while (b > 0) {
            if (b % 2 == 1)
                ans = ans * a % MOD;

            a = a * a % MOD;
            b /= 2;
        }

        return ans;
    }
}
```

---

**Runtime** 0 ms (beats 100.0%) · **Memory** 42.3 MB (beats 84.5%)

<sub>Synced by AILeetHub on 2026-09-16.</sub>
