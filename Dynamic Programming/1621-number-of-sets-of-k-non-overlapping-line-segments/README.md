# 1621. Number of Sets of K Non-Overlapping Line Segments

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/number-of-sets-of-k-non-overlapping-line-segments/)

`Math` · `Dynamic Programming` · `Combinatorics` · `Prefix Sum`

## Intuition  
The key observation is that a set of k non‑overlapping segments on points 0…n‑1 can be encoded by choosing 2k positions for the segment endpoints among n+k‑1 “slots” that arise after we insert a dummy separator after each chosen endpoint. This bijection yields the closed‑form count  

\[
\binom{n+k-1}{2k} = \frac{(n+k-1)!}{(2k)!\,(n-k-1)!}.
\]

A naïve DP would enumerate left/right choices for every point, costing O(n·k) time and O(n·k) memory. The combinatorial insight eliminates the DP entirely, reducing the problem to a single binomial coefficient computed modulo 10⁹+7. The pattern used is **modular combinatorics via factorials and modular inverses**.

## Approach  
1. **Compute the numerator** – call `factorial(n + k - 1)` which iterates `i` from 2 to `n + k - 1`, multiplying `fact` by `i` and taking `% MOD` each step.  
2. **Compute the first denominator term** – `factorial(2 * k)` using the same routine, producing `(2k)! mod MOD`.  
3. **Compute the second denominator term** – `factorial(n - k - 1)`, yielding `(n‑k‑1)! mod MOD`.  
4. **Combine denominator terms** – `denominator = denominator1 * denominator2 % MOD`. The loop invariant is that after each multiplication `denominator` holds the product of the processed factorial parts modulo MOD.  
5. **Find modular inverse** – invoke `modInverse(denominator)`, which calls `pow(denominator, MOD‑2)`. The fast exponentiation loop maintains the invariant “`result` equals `denominator` raised to the already‑processed bits of the exponent, modulo MOD”.  
6. **Assemble the answer** – multiply `numerator` by the modular inverse, reduce modulo MOD, and cast to `int`. The final product equals the binomial coefficient under the modulus.

Edge cases are handled implicitly: when `n‑k‑1` equals 0 the factorial loop returns 1; the algorithm never accesses negative indices because the constraints guarantee `k ≤ n‑1`.

## Dry Run  
**Input:** `n = 4, k = 2`  

| Step | i (loop variable) | fact (numerator) | denominator1 (2k!) | denominator2 (n‑k‑1)! | note |
|------|-------------------|------------------|--------------------|----------------------|------|
| 1    | 2                 | 2                | –                  | –                    | start numerator |
| 2    | 3                 | 6                | –                  | –                    | 3! |
| 3    | 4                 | 24               | –                  | –                    | 4! |
| 4    | 5                 | 120              | –                  | –                    | 5! (stop, n+k‑1=5) |
| 5    | 2 (den1)          | –                | 2                  | –                    | (2k)! = 2! |
| 6    | 3 (den1)          | –                | 6                  | –                    | 3! → 6 |
| 7    | 4 (den1)          | –                | 24                 | –                    | 4! → 24 = (2k)! |
| 8    | 1 (den2)          | –                | –                  | 1                    | (n‑k‑1)! = 0! =1 |
| 9    | –                 | –                | –                  | –                    | denominator = 24·1 % MOD = 24 |
|10    | –                 | –                | –                  | –                    | inv = pow(24, MOD‑2) = 41666667 |
|11    | –                 | –                | –                  | –                    | result = 120 * 41666667 % MOD = 5 |

Final state: `result = 5`, which matches the five valid segment sets.

## Complexity  
- **Time:** O(n + k) – the three factorial loops each run at most `n + k - 1` iterations, and the fast‑power loop runs in O(log MOD) ≈ 30 steps, all bounded by a linear scan of the input size.  
- **Space:** O(1) – only a handful of `long` variables are kept; the output array is not counted.

## Solution (Java)

```java
class Solution {
    static final long MOD = 1000000007;
    // Combination Formula NCr
    // numerator = N!

    // Denominator = R! * (N - R)!
    // N = n + k - 1
    // R = 2k

    // Numerator = (n + k - 1)! 
    // Denominator = 2k! * (n - k - 1)! 

    public int numberOfSets(int n, int k) {
        long numerator = factorial(n + k - 1);
        long denominator1 = factorial(2 * k);
        long denominator2 = factorial(n - k - 1);
        long denominator = denominator1 * denominator2 % MOD;

        long result = numerator * modInverse(denominator);
        result = result % MOD;
        return (int) result ;
    }

    long factorial(int n) {
        long fact = 1;
        for (int i = 2; i <= n; i++) {
            fact *= i;
            fact %= MOD;
        }
        return fact;
    }

    long modInverse(long n) {
        return pow(n, MOD - 2);
    }

    long pow(long n, long power){
        long result = 1;
        while (power > 0) {
            if (power % 2 == 1)
                result = result * n % MOD;

            n = n * n % MOD;
            power /= 2;
        }
        return result;
    }
}
```

---

**Runtime** 1 ms (beats 95.5%) · **Memory** 41.8 MB (beats 98.7%)

<sub>Synced by AILeetHub on 2026-09-16.</sub>
