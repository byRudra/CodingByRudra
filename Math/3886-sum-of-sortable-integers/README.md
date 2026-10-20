# 3886. Sum of Sortable Integers

![Hard](https://img.shields.io/badge/Difficulty-Hard-ff375f?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/sum-of-sortable-integers/)

`Array` · `Math` · `Sorting` · `Enumeration`

## Intuition  
The only freedom we have is rotating each length‑`k` segment; a rotation cannot change the multiset of values inside the segment. Therefore a segment can be sorted **iff** the sorted version of the whole array (`target`) appears somewhere in the circular view of the original segment. The key observation is that within a segment the rotation that could work is uniquely determined by the first place where the original values descend (i.e., `nums[i] > nums[i+1]`). If no descent exists the segment is already in the correct order, so the rotation start is at index 0. This eliminates the need for trying all `k` rotations or using extra data structures.

## Approach  
1. **Clone and sort** the input → `target`.  
2. **Enumerate all divisors** of `n` (both `x` and `n/x`) and store them in `fac`.  
3. **Iterate over each divisor `d`**:  
   - Initialise `canSort = true`.  
   - For every block start `s = 0, d, 2d, …` (`s < n`):  
     - Call `check(nums, target, s, d)`.  
     - If any block returns `false`, set `canSort = false` and break out of the block loop.  
   - If `canSort` stayed `true`, add `d` to the answer.  
4. **`check` routine** (`s` = block start, `k` = block length):  
   - Scan the block once (`i = 0 … k‑2`) to find the first index where `nums[s+i] > nums[s+i+1]`.  
   - Let `split = i`; if none is found `split = -1`.  
   - The rotation that could align the block with the sorted order must start at `start = split + 1`.  
   - Verify element‑wise that the rotated block (`nums[s + (start + j) % k]`) equals the corresponding slice of `target` (`target[s + j]`) for all `j = 0 … k‑1`.  
   - Return `true` only when every comparison matches.  

**Edge handling** –  
- When `n = 1` the divisor list contains only `1`; the inner loop runs once and `check` immediately succeeds because the scan finds no descent.  
- For even and odd `k` the same logic works; the descent‑based start works regardless of parity.  
- The `<=` guard in the divisor enumeration (`(long) x * x <= n`) prevents overflow and ensures the square root is included exactly once.  

## Dry Run  
Input: `nums = [3, 1, 2]` (`n = 3`)  

| step | `d` (divisor) | `s` (block start) | `split` (first descent) | `start` (rotation) | comparison result | note |
|------|--------------|-------------------|--------------------------|--------------------|-------------------|------|
| 1    | 1            | 0                 | 0 (`3 > 1`)              | 1                  | `nums[1] != target[0]` (`1 != 1` OK) → next j fails at j=1 (`nums[2] != target[1]` → `2 != 3`) | block 0 cannot be rotated to match |
| 2    | 3            | 0                 | 0 (`3 > 1`)              | 1                  | j=0: `nums[1]=1` vs `target[0]=1` ✔<br>j=1: `nums[2]=2` vs `target[1]=2` ✔<br>j=2: `nums[0]=3` vs `target[2]=3` ✔ | whole array matches after one left rotation |

`d = 1` is rejected, `d = 3` is accepted, so the final answer is `3`.  

## Complexity  
- **Time:** `O(n log n + n * τ(n))` where `τ(n)` is the number of divisors of `n`. Sorting costs `O(n log n)`. For each divisor we scan every element exactly once (the inner `check` scans each block of size `d` once), so the total work over all divisors is `O(n * τ(n))`.  
- **Space:** `O(n)` for the cloned `target` array and `O(τ(n))` for the divisor list; no additional structures proportional to `n` are created inside the loops.

## Solution (Java)

```java
class Solution {
    public int sortableIntegers(int[] nums) {
        int n = nums.length;
        int[] target = nums.clone();
        Arrays.sort(target);

        List<Integer> fac = new ArrayList<>();
        for (int x = 1; (long) x * x <= n; x++) {
            if (n % x == 0) {
                fac.add(x);
                if (n / x != x)
                    fac.add(n / x);
            }
        }

        int ans = 0;
        for (int d : fac) {
            boolean canSort = true;
            for (int s = 0; s < n; s += d) {
                if (!check(nums, target, s, d)) {
                    canSort = false;
                    break;
                }
            }
            if (canSort)
                ans += d;
        }
        return ans;
    }

    // Does target[s..s+k) appear inside nums[s..s+k) doubled?
    private boolean check(int[] nums, int[] target, int s, int k) {
        int split = -1; // -1 means "no descent", so the rotation starts at 0
        for (int i = 0; i < k - 1; i++) {
            if (nums[s + i] > nums[s + i + 1]) {
                split = i;
                break;
            }
        }
        int start = split + 1;
        for (int j = 0; j < k; j++) {
            if (nums[s + (start + j) % k] != target[s + j])
                return false;
        }
        return true;
    }
}
```

---

**Runtime** 86 ms (beats 31.8%) · **Memory** 75.8 MB (beats 40.9%)

<sub>Synced by AILeetHub on 2026-10-20.</sub>
