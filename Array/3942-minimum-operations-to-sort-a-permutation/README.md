# 3942. Minimum Operations to Sort a Permutation

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/minimum-operations-to-sort-a-permutation/)

`Array`

## Intuition  
The array can only be sorted if it already follows the natural cyclic order either forward (`0,1,2,…`) or backward (`…,2,1,0`). In that case every adjacent pair satisfies a simple modular relation, so a single pass can tell us which pattern (if any) we have. Once the pattern is known, the whole problem reduces to counting how many left‑rotations are needed, possibly preceded or followed by a single reversal. The naïve idea of trying all sequences of operations would be exponential; the modular adjacency check eliminates the need for any search, hash map, or extra pass.

## Approach  
1. **Detect forward cyclic order** – Initialise `inc = true`. For each index `i` (0 ≤ i < n) compute `next = nums[(i+1)%n]`. If `next` is not `(nums[i] + 1) % n`, set `inc = false`.  
2. **Detect backward cyclic order** – Initialise `dec = true`. In the same loop, if `next` is not `(nums[i] - 1 + n) % n`, set `dec = false`.  
   *Loop invariant*: after processing the first `i` positions, `inc` (resp. `dec`) is true exactly when all examined adjacent pairs satisfy the forward (resp. backward) modular relation. The loop exits after `i = n‑1`, having examined the whole circular array.  
3. **Handle the forward case** – If `inc` remains true, the array is a left‑rotation of the sorted permutation. Let `k = nums[0]` (the value that currently sits at index 0).  
   * If `k == 0` the array is already sorted → 0 operations.  
   * Otherwise we have two viable strategies:  
     - Rotate left `n‑k` times to bring `0` to the front.  
     - Reverse first (1 op) then rotate left `k+1` times; the extra `+1` accounts for the reversal itself, giving `k+2` total.  
   The code stores the minimum of these two in `best`.  
4. **Handle the backward case** – If `dec` is true, a single reversal turns the array into a forward cyclic shift. After reversal the element that will become the new first is `(nums[0] + 1) % n`; call it `k`. We then need either `k` left rotations or `n‑k` left rotations, whichever is smaller. Adding the mandatory reversal gives `1 + min(k, (n‑k)%n)`. The result is merged with any previous `best`.  
5. **Return** – If neither pattern held, `best` stays at `Integer.MAX_VALUE` and we return `-1`; otherwise we return the computed minimum.

## Dry Run  
Input: `nums = [0,2,1]` (n = 3)

| i | nums[i] | next = nums[(i+1)%3] | inc? (next == (nums[i]+1)%3) | dec? (next == (nums[i]-1+3)%3) |
|---|---------|----------------------|------------------------------|--------------------------------|
| 0 | 0       | 2                    | false                        | false                          |
| 1 | 2       | 1                    | false                        | true                           |
| 2 | 1       | 0                    | false                        | true                           |

After the loop: `inc = false`, `dec = true`.  
`k = (nums[0] + 1) % 3 = 1`.  
`best = 1 + min(k, (n‑k)%n) = 1 + min(1,2) = 2`.  

The algorithm returns `2`, matching the optimal sequence: reverse → rotate left.

## Complexity  
- **Time:** O(n) – the single loop visits each element once; the subsequent constant‑time calculations are negligible.  
- **Space:** O(1) – only a handful of integer and boolean variables are used, independent of `n`. (The output integer does not count toward extra space.)

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

**Runtime** 8 ms (beats 10.0%) · **Memory** 94.9 MB (beats 81.7%)

<sub>Synced by AILeetHub on 2026-10-22.</sub>
