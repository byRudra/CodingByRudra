# 2897. Apply Operations on Array to Maximize Sum of Squares

![Hard](https://img.shields.io/badge/Difficulty-Hard-ff375f?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/apply-operations-on-array-to-maximize-sum-of-squares/)

`Array` · `Hash Table` · `Greedy` · `Bit Manipulation`

## Intuition  
The operation `a = a & b, b = a | b` never creates new set bits – it only moves bits from one element to the other. Consequently, after any sequence of operations the multiset of all set bits across the array is invariant. If we can freely redistribute those bits, the optimal way to maximize the sum of squares of k chosen numbers is to concentrate as many bits as possible into each of the k numbers, building the k largest values one after another. The naïve way would be to try every possible redistribution or to simulate the pairwise operations, both of which are exponential. The key insight is that counting how many times each bit appears (`cnt[p]`) is enough, and a greedy construction that repeatedly takes one copy of every available bit yields the optimal k numbers. This is a classic “bit‑pooling” greedy pattern.

## Approach  
1. **Count set bits** – Initialise an array `cnt[31]`. For each `num` in `nums` iterate over its binary representation (`while (x > 0)`) and increment `cnt[p]` whenever the current least‑significant bit is 1. After this pass `cnt[p]` equals the total number of occurrences of the 2^p bit in the whole array.  
2. **Pre‑compute powers of two** – Fill `weight[p] = 2^p` using a simple loop (`weight[p] = weight[p‑1] * 2`). Storing them avoids repeated shifts and keeps the code readable.  
3. **Greedy construction of k numbers** – For `i` from 0 to k‑1:  
   - Initialise `val = 0`.  
   - Scan all bit positions `p = 0 … 30`. If `cnt[p] > 0` we “assign” one copy of that bit to the current number: decrement `cnt[p]` and add `weight[p]` to `val`.  
   - The inner loop’s exit condition is `p == BITS`; the invariant is that after processing position p, `cnt[0…p]` reflect the remaining unused copies of those bits.  
   - If `val == 0` (no bits left) we break early because further numbers would be zero and contribute nothing.  
   - Add `val²` to the answer modulo `MOD`. The modulo operation is performed as `(ans + (val * val) % MOD) % MOD` to stay within 64‑bit range.  
4. **Return** the accumulated answer cast to `int`.

**Edge handling** – The code works for `nums` of length 1 because the bit‑count loop still records bits, and the greedy loop will produce at most one non‑zero `val`. The break on `val == 0` prevents unnecessary iterations when the bit pool is exhausted, which also covers the case `k > total set bits`. Using `int` for `cnt` is safe because the maximum count is `n ≤ 10⁵`. `long` is used for `weight` and intermediate products to avoid overflow before the modulo.

## Dry Run  

Input: `nums = [2, 6, 5, 8]`, `k = 2`  

Initial bit counts (`cnt` for bits 0‑4):  

| bit | 0 | 1 | 2 | 3 | 4 |
|-----|---|---|---|---|---|
| cnt | 0 | 2 | 2 | 1 | 0 |

| i | p‑scan (bits taken) | cnt after scan | val | ans (mod) | note |
|---|---------------------|----------------|-----|-----------|------|
| 0 | take bits 1,2,3    | 0,1,1,0,0      | 2+4+8 = 14 | 14² = 196 | first number built |
| 1 | take bits 1,2      | 0,0,0,0,0      | 2+4 = 6   | 196+6²=232 | second number built |
| 2 | – (val==0) break   | –              | 0   | –         | no more bits |

Final answer = 232 (mod 1e9+7) = 232, which matches the optimal selection (14² + 6²).  

## Complexity  
- **Time:** `O((n + k) * BITS)` → `O(n + k)` because `BITS = 31` is a constant; we scan each number once to count bits and then perform at most `k` passes over the 31 bit positions.  
- **Space:** `O(BITS)` → `O(1)` extra space besides the input and output, storing only the bit counters and the pre‑computed powers. The modulo result does not affect the asymptotic bound.

## Solution (Java)

```java
import java.util.List;

class Solution {
    public int maxSum(List<Integer> nums, int k) {
        final long MOD = 1_000_000_007L;
        final int BITS = 31;

        // cnt[p] = how many numbers have the 2^p place set (counted with / and %)
        int[] cnt = new int[BITS];
        for (int num : nums) {
            int x = num;
            int p = 0;
            while (x > 0) {
                if (x % 2 == 1) {
                    cnt[p]++;
                }
                x /= 2;
                p++;
            }
        }

        long[] weight = new long[BITS];
        weight[0] = 1;
        for (int p = 1; p < BITS; p++) {
            weight[p] = weight[p - 1] * 2;
        }

        // Greedily build the k largest numbers by pooling set bits together.
        long ans = 0;
        for (int i = 0; i < k; i++) {
            long val = 0;
            for (int p = 0; p < BITS; p++) {
                if (cnt[p] > 0) {
                    cnt[p]--;
                    val += weight[p];
                }
            }
            if (val == 0) {
                break;
            }
            ans = (ans + (val * val) % MOD) % MOD;
        }
        return (int) ans;
    }
}
```

---

**Runtime** 120 ms (beats 11.1%) · **Memory** 80 MB (beats 100.0%)

<sub>Synced by AILeetHub on 2026-10-12.</sub>
