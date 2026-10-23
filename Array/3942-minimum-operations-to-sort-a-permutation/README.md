# 3942. Minimum Operations to Sort a Permutation

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/minimum-operations-to-sort-a-permutation/)

`Array`

## Intuition  
The permutation can only be sorted if, when we view the array as a circle, the numbers follow a single consistent step: either each element is the successor of the previous one modulo `n` (strictly increasing around the circle) or each element is the predecessor (strictly decreasing). This invariant eliminates the need for any search or simulation of operations; we only have to recognise which of the two circular orders (if any) the input already exhibits. Once the order is known, the number of required operations reduces to a simple arithmetic expression based on the position of `0`. The whole solution therefore follows the classic **two‑pattern detection** (increasing vs. decreasing) on a circular array.

## Approach  
1. **Initialize flags** `inc = true` and `dec = true`.  
2. **Single pass** over the array (`i = 0 … n‑1`):  
   - Compute `next = nums[(i + 1) % n]` to treat the array as circular.  
   - If `next` is not the modular successor of `nums[i]` (`(nums[i] + 1) % n`), set `inc = false`.  
   - If `next` is not the modular predecessor of `nums[i]` (`(nums[i] - 1 + n) % n`), set `dec = false`.  
   *Invariant*: after processing index `i`, `inc` (resp. `dec`) is true iff all examined adjacent pairs so far respect the increasing (resp. decreasing) circular rule.  
3. **Compute candidate answer for the increasing case** (`inc` true):  
   - Let `k = nums[0]`, the offset of `0` after a left rotation.  
   - If `k == 0` the array is already sorted → candidate `0`.  
   - Otherwise the minimal cost is `min(n - k, k + 2)`.  
     *Explanation*: rotating left `n‑k` times brings `0` to the front; alternatively, reverse first (cost 1) then rotate left `k` times (cost `k+1`), which simplifies to `k+2`.  
4. **Compute candidate answer for the decreasing case** (`dec` true):  
   - Let `k = (nums[0] + 1) % n`, the distance from the current front to the element that would become `0` after a reversal.  
   - The cost is `1 + min(k, (n - k) % n)`.  
     *Explanation*: we must reverse once (cost 1), then either rotate left `k` steps or rotate right `(n‑k)` steps (the latter is achieved by `n‑k` left rotations).  
5. **Select the best** of the two candidates stored in `best`. If neither pattern held (`best` stayed `Integer.MAX_VALUE`), return `-1`.  

Edge handling:  
- Empty or single‑element arrays trivially satisfy both patterns; the formulas correctly yield `0`.  
- The modulo arithmetic guarantees correct wrap‑around for both even and odd `n`.  
- The `<=` vs `<` distinction never appears because equality is explicitly tested against the modular expectations.

## Dry Run  
Input: `nums = [0, 2, 1]` (`n = 3`)

| i | nums[i] | next | inc before | dec before | inc after | dec after |
|---|---------|------|------------|------------|-----------|-----------|
| 0 | 0       | 2    | true       | true       | false (2 ≠ 1) | true (2 = 2) |
| 1 | 2       | 1    | false      | true       | false    | false (1 ≠ 1? actually predecessor check: (2‑1+3)%3=1 → true, so dec stays true) |
| 2 | 1       | 0    | false      | true       | false    | true (0 = (1‑1+3)%3) |

After the loop `inc = false`, `dec = true`.  
- For decreasing case: `k = (nums[0] + 1) % 3 = 1`.  
- Candidate = `1 + min(1, (3‑1)%3 = 2) = 2`.  
No increasing candidate, so `best = 2`. The algorithm returns `2`, matching the optimal sequence (rotate left, then reverse).

## Complexity  
- **Time:** `O(n)` – the single loop visits each element once; the subsequent constant‑time calculations depend only on `n`.  
- **Space:** `O(1)` – only a handful of primitive variables (`inc`, `dec`, `k`, `best`) are used, independent of input size. The output array is not counted.

## Solution (Java)

```java
class Solution {
    public int minOperations(int[] nums) {
        int n = nums.length;
        boolean inc = true, dec = true;
        for (int i = 0; i < n; i++) {
            int next = nums[(i + 1) % n];
            if (next != (nums[i] + 1) % n) inc = false;
            if (next != (nums[i] - 1 + n) % n) dec = false;
        }

        int best = Integer.MAX_VALUE;
        if (inc) {
            int k = nums[0];
            best = (k == 0) ? 0 : Math.min(n - k, k + 2);
        }
        if (dec) {
            int k = (nums[0] + 1) % n;
            best = Math.min(best, 1 + Math.min(k, (n - k) % n));
        }
        return best == Integer.MAX_VALUE ? -1 : best;
    }
}
```

---

**Runtime** 8 ms (beats 10.2%) · **Memory** 94.9 MB (beats 79.7%)

<sub>Synced by AILeetHub on 2026-10-22.</sub>
