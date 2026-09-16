# 1621. Number of Sets of K Non-Overlapping Line Segments

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/number-of-sets-of-k-non-overlapping-line-segments/)

`Math` · `Dynamic Programming` · `Combinatorics` · `Prefix Sum`

## Intuition  
The key observation is that a set of k non‑overlapping segments on points 0…n‑1 can be represented by choosing 2k ordered endpoints after we “reserve” one interior point for each segment. If we shrink every required interior point into a dummy slot, the effective line length becomes n + k − 1, and any valid configuration corresponds to picking 2k positions among these n + k − 1 slots. Hence the answer is the binomial coefficient C(n + k − 1, 2k). A naïve DP would be O(n·k) and a combinatorial enumeration would be exponential; the insight collapses the problem to a single closed‑form expression. The pattern used is **combinatorial counting via factorials and modular inverses**.

## Approach  
1. **Compute the numerator** – call `factorial(n + k - 1)`.  
   *Loop*: `for i = 2 … n+k-1` multiply `fact` by i mod MOD.  
   *Invariant*: after processing i, `fact` equals i! mod MOD.  
2. **Compute the two denominator parts** – `factorial(2 * k)` and `factorial(n - k - 1)`.  
   *Loop*: identical structure; the same invariant holds for each call.  
   *Edge handling*: when `n - k - 1` equals 0 the loop body never runs, returning 1, which correctly represents 0!.  
3. **Combine denominator** – `denominator = denominator1 * denominator2 % MOD`.  
   This respects modular arithmetic while keeping the product within 64‑bit range.  
4. **Find modular inverse of denominator** – `modInverse(denominator)` uses Fermat’s little theorem because MOD is prime.  
   *Loop*: binary exponentiation (`pow`) repeatedly squares the base and multiplies when the current bit of the exponent is 1.  
   *Invariant*: after each iteration, `result` equals the original base raised to the processed bits of the exponent, all modulo MOD.  
5. **Multiply numerator by the inverse** – `result = numerator * modInverse(denominator) % MOD`.  
   This yields C(n + k − 1, 2k) mod MOD, which is returned as an `int`.  

The code deliberately uses `<=` in the factorial loops (`i <= limit`) to include the upper bound, ensuring that limit! is fully computed. All arithmetic stays in `long` to avoid overflow before the modulo reduction.

## Dry Run  

**Input:** `n = 4, k = 2`

| Step | i (factorial loop) | fact (mod) | note |
|------|-------------------|------------|------|
| 1️⃣ numerator loop start | – | fact = 1 | initialize |
| 1️⃣ i=2 | fact = 2 | 2! |
| 1️⃣ i=3 | fact = 6 | 3! |
| 1️⃣ i=4 | fact = 24 | 4! |
| 1️⃣ i=5 | fact = 120 | 5! = numerator |
| 2️⃣ denom1 (`2k=4`) loop i=2 | fact = 2 | 2! |
| 2️⃣ i=3 | fact = 6 | 3! |
| 2️⃣ i=4 | fact = 24 | 4! = denominator1 |
| 3️⃣ denom2 (`n‑k‑1=1`) loop skips (i starts at 2) → fact = 1 = denominator2 |
| 4️⃣ denominator = 24 * 1 % MOD = 24 |
| 5️⃣ inverse = pow(24, MOD‑2) = 41666667 (pre‑computed) |
| 6️⃣ result = 120 * 41666667 % MOD = 5 |

Final state: `result = 5`, which matches the five valid segment sets.

## Complexity  
- **Time:** O(n + k) – the three factorial calls each traverse at most n + k − 1 steps, and binary exponentiation runs in O(log MOD) ≈ 30 steps, dominated by the linear passes.  
- **Space:** O(1) – only a handful of primitive variables are stored; the output array is not counted.

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

        long result = numerator * modInverse(denominator) % MOD;
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

**Runtime** 0 ms (beats 100.0%) · **Memory** 42 MB (beats 94.2%)

<sub>Synced by AILeetHub on 2026-09-16.</sub>
