# 3727. Maximum Alternating Sum of Squares

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/maximum-alternating-sum-of-squares/)

`Array` · `Greedy` · `Sorting`

## Intuition  
The alternating score is a sum of squared values with signs that alternate `+ – + – …`. Because squaring removes sign, the only thing that matters is **which squares receive a plus and which receive a minus**. There are exactly ⌈n/2⌉ plus‑positions (indices 0,2,4,…) and the rest are minus‑positions. To maximise the total we should give the largest squares to the plus‑positions and the smallest squares to the minus‑positions. A naïve solution would try every permutation (O(n!)) or use a hash‑map to track used elements, both far too expensive. The insight that the optimal assignment is obtained by sorting the squares and greedily splitting them yields a linear‑ith‑log‑n solution, a classic **greedy with sorting** pattern.

## Approach  
1. **Square every element** – iterate `i` from `0` to `n‑1`, store `(long)nums[i]*nums[i]` in `squares[i]`.  
   *Exit condition*: `i == n`. *Invariant*: after processing index `i‑1`, `squares[0..i‑1]` contain the correct squares.  
2. **Sort the squares** – `Arrays.sort(squares)`. After sorting, `squares` is non‑decreasing, guaranteeing that any prefix consists of the smallest values.  
3. **Determine how many plus signs are needed** – `howMuchPositive = (n + 1) / 2`. This formula works for both even and odd lengths because integer division discards the remainder, yielding ⌈n/2⌉.  
4. **Accumulate the score** – loop `i` from `0` to `n‑1`.  
   *Exit condition*: `i == n`.  
   *Invariant*: before each iteration, `score` equals the alternating sum of the already processed elements according to the rule “last `howMuchPositive` squares are added, the rest are subtracted”.  
   - If `i >= n - howMuchPositive` (i.e. the current index belongs to the largest `howMuchPositive` elements), do `score += squares[i]`.  
   - Otherwise do `score -= squares[i]`.  
   The comparison uses `>=` because the boundary `n‑howMuchPositive` itself belongs to the positive side; using `<` would incorrectly subtract one of the largest squares.  
5. **Return `score`** – the final value after the loop is the maximum alternating sum.

Edge cases:  
- `n == 1` → `howMuchPositive == 1`, the single square is added, giving the correct answer.  
- All numbers are zero → every square is zero, the algorithm returns `0`.  
- Negative inputs are harmless because squaring eliminates the sign before any ordering.

## Dry Run  

Input: `nums = [1, 2, 3, 4]`

| i | squares[i] | condition `i >= n‑howMuchPositive`? | action | cumulative score |
|---|------------|--------------------------------------|--------|-------------------|
| 0 | 1          | 0 ≥ 2 ? false                         | `score -= 1` | -1 |
| 1 | 4          | 1 ≥ 2 ? false                         | `score -= 4` | -5 |
| 2 | 9          | 2 ≥ 2 ? true                          | `score += 9` | 4 |
| 3 | 16         | 3 ≥ 2 ? true                          | `score += 16`| 20 |

After the loop `score = 20`, which is the maximum possible alternating sum for this set because the two largest squares (9 and 16) occupy the plus slots and the two smallest (1 and 4) occupy the minus slots.

## Complexity  
- **Time:** O(n log n) – sorting dominates; the two linear passes each run `n` iterations.  
- **Space:** O(n) – the extra `squares` array stores `n` long values; the output itself is a single `long` and does not affect the asymptotic bound.

## Solution (Java)

```java
class Solution {
    public long maxAlternatingSum(int[] nums) {
        int n = nums.length;
        long squares[] = new long[n];
        for(int i = 0; i < n; i++){
            squares[i] = (long) nums[i] * nums[i];
        }
        Arrays.sort(squares);

        int howMuchPositive = (n + 1) / 2;
        long score = 0;
        for(int i = 0; i < n; i++){
            if(i >= n - howMuchPositive){
                score += squares[i];
            }else{
                score -= squares[i];
            }
        }
        return score;
    }
}
```

---

**Runtime** 33 ms (beats 68.3%) · **Memory** 135.7 MB (beats 34.5%)

<sub>Synced by AILeetHub on 2026-10-13.</sub>
