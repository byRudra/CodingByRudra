# 3942. Minimum Operations to Sort a Permutation

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/minimum-operations-to-sort-a-permutation/)

`Array`

## Intuition  
The array can be sorted only if it already follows the natural order **up to a cyclic shift**, either increasing (0→1→2…) or decreasing (…→2→1→0) when viewed around the circle. This invariant is cheap to test: each adjacent pair must be the successor modulo n for the increasing case, or the predecessor modulo n for the decreasing case. If the invariant fails, no combination of whole‑array reversals and left rotations can ever produce the sorted permutation, so the answer is –1. Once the invariant holds, the problem reduces to counting how many left‑rotations (and possibly one reversal) are needed to bring 0 to the front, because after that the array is exactly `[0,1,…,n‑1]`. The pattern used is the classic **circular‑order check** followed by a closed‑form arithmetic computation.

## Approach  
1. **Detect circular order**  
   *Loop `i` from 0 to n‑1*  
   - `next = nums[(i+1)%n]` (wrap‑around).  
   - If `next != (nums[i]+1)%n` set `inc = false`.  
   - If `next != (nums[i]-1+n)%n` set `dec = false`.  
   The loop exits after one full pass; the invariant “all consecutive pairs respect the chosen direction” holds at each iteration.

2. **Handle the increasing circular case** (`inc == true`)  
   - Let `k = nums[0]`, i.e. the value that currently sits at index 0.  
   - If `k == 0` the array is already sorted → `best = 0`.  
   - Otherwise we have two viable ways:  
     *Rotate left `k` times* (cost `k`).  
     *Reverse first (cost 1) then rotate left `n‑k` times* (cost `n‑k+1`).  
     The code compresses the second option to `n‑k` because a reverse followed by a left rotation is equivalent to a right rotation, which can be simulated by `n‑k` left rotations without an extra count.  
   - It picks the cheaper of `n‑k` and `k+2` (the `+2` accounts for the reverse plus the extra rotation needed after reversal).

3. **Handle the decreasing circular case** (`dec == true`)  
   - After a reversal the array becomes increasing, but the index of 0 changes.  
   - Compute `k = (nums[0] + 1) % n`, which is the distance from the current front element to where 0 will appear after the reversal.  
   - One reversal is mandatory (`+1`). After that we can either rotate left `k` steps or rotate right `n‑k` steps (the latter is ` (n‑k) % n ` left rotations).  
   - The total cost is `1 + min(k, (n‑k)%n)`.

4. **Combine results**  
   - `best` holds the minimum cost among the feasible directions.  
   - If neither `inc` nor `dec` stayed true, `best` remains `Integer.MAX_VALUE` and the method returns `-1`.

## Dry Run  
Input: `nums = [0,2,1]` (n = 3)

| iter | i | nums[i] | next (wrap) | inc? | dec? |
|------|---|---------|-------------|------|------|
| 0    | 0 | 0       | 2           | false (2≠1) | true (2≡‑1 mod 3) |
| 1    | 1 | 2       | 1           | false | true (1≡1) |
| 2    | 2 | 1       | 0           | false | true (0≡0) |

After the loop: `inc = false`, `dec = true`.  
`k = (nums[0]+1)%3 = (0+1)%3 = 1`.  
Cost = `1 + min(1, (3‑1)%3 = 2)` = `1 + 1 = 2`.  
`best = 2`, returned value `2`, which matches the optimal sequence (reverse → rotate).

## Complexity  
- **Time:** `O(n)` – the single pass examines each of the `n` elements once; the subsequent arithmetic is constant time.  
- **Space:** `O(1)` – only a handful of primitive variables (`inc`, `dec`, `k`, `best`) are used, independent of `n`.

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
