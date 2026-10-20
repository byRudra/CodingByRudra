# 3886. Sum of Sortable Integers

![Hard](https://img.shields.io/badge/Difficulty-Hard-ff375f?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/sum-of-sortable-integers/)

`Array` · `Math` · `Sorting` · `Enumeration`

## Intuition  
The array can be sorted by rotating each length‑`k` chunk iff, for every chunk, the sorted version of that chunk appears somewhere inside the chunk after it is concatenated with itself. This “double‑block” observation eliminates the need to try every rotation explicitly. A naïve solution would test all `k` rotations for each chunk, costing `O(n·k)` per divisor, which is far too slow. Recognizing the rotation test as a substring problem lets us apply the linear‑time KMP algorithm, giving an overall `O(n·τ(n))` solution where `τ(n)` is the number of divisors of `n`. The pattern is a classic **two‑pointer / string‑matching** trick applied to arrays.

## Approach  
1. **Clone and sort** the whole array into `target`. This gives the exact order each position must finally hold.  
2. **Collect all divisors** of `n` in `fac` by iterating `x` from `1` to `√n`. For each `x` that divides `n`, add both `x` and `n/x` (avoiding duplication when `x*x == n`).  
3. **Iterate over each divisor `d`** in `fac`.  
   - Initialise `canSort = true`.  
   - Loop `s` from `0` to `n‑1` in steps of `d`. For each block starting at `s`, call `isRotation(nums, target, s, d)`.  
   - If any block fails, set `canSort = false` and break out of the inner loop.  
   - When all blocks succeed, add `d` to `ans`.  
4. **`isRotation`** checks whether `target[s..s+d)` is a rotation of `nums[s..s+d)`.  
   - Build the KMP prefix table `pi` for the pattern `target[s..s+d)`. The invariant: after processing `i` characters, `pi[i]` stores the length of the longest proper prefix that is also a suffix of `target[s..s+i]`.  
   - Scan the “doubled” text `nums[s + i % d]` for `i = 0 … 2·d‑1`. The invariant: `j` is the length of the current matched prefix of the pattern. When `j` reaches `d`, the pattern occurs, meaning a rotation exists, and the method returns `true`.  
   - If the scan finishes without a full match, return `false`.  
5. Return `ans`, the sum of all divisors that passed the rotation test.

Key edge handling:  
- When `n = 1`, the divisor list contains only `1`; the rotation test trivially succeeds.  
- For already sorted arrays, every divisor passes because each block already equals its target, and the KMP scan finds a match at the first character.  
- The loop condition `i < 2 * k` (instead of `< 2 * k - 1`) guarantees that a rotation that starts at the last element is still examined.

## Dry Run  

**Input:** `nums = [3, 1, 2]`  

Sorted `target = [1, 2, 3]` → divisors `fac = [1, 3]`.  
Only `d = 3` can succeed; we invoke `isRotation(nums, target, 0, 3)`.

| i | c = nums[0 + i % 3] | j (matched prefix length) | note |
|---|----------------------|---------------------------|------|
| 0 | 3 | 0 → 0 (3 ≠ 1) | no match, stay at 0 |
| 1 | 1 | 0 → 1 (1 = 1) | matched first element |
| 2 | 2 | 1 → 2 (2 = 2) | matched first two |
| 3 | 3 | 2 → 3 (3 = 3) | `j == k`, rotation found → return `true` |

All blocks succeed, so `ans += 3`. Final answer `3`.

## Complexity  
- **Time:** `O(n log n + n·τ(n))`. Sorting costs `O(n log n)`. For each divisor `d`, we scan every element at most twice inside `isRotation`, so the inner work is `O(n)`. Since there are `τ(n)` divisors, the total is `O(n·τ(n))`.  
- **Space:** `O(n)`. The cloned `target` array and the temporary prefix table `pi` of size `k ≤ n` dominate; extra output storage is excluded.

## Solution (Java)

```java
import java.util.*;

class Solution {
    public int sortableIntegers(int[] nums) {
        int n = nums.length;
        int[] target = nums.clone();
        Arrays.sort(target);

        // collect divisors of n in O(sqrt(n))
        List<Integer> fac = new ArrayList<>();
        for (int x = 1; (long) x * x <= n; x++) {
            if (n % x == 0) {
                fac.add(x);
                if (n / x != x) fac.add(n / x);
            }
        }

        int ans = 0;
        for (int d : fac) {
            boolean canSort = true;
            for (int s = 0; s < n; s += d) {
                if (!isRotation(nums, target, s, d)) {
                    canSort = false;
                    break;
                }
            }
            if (canSort) ans += d;
        }
        return ans;
    }

    // Does target[s..s+k) appear inside nums[s..s+k) doubled?
    private boolean isRotation(int[] nums, int[] target, int s, int k) {
        // prefix function of the pattern target[s..s+k)
        int[] pi = new int[k];
        int j = 0;
        for (int i = 1; i < k; i++) {
            while (j > 0 && target[s + i] != target[s + j]) j = pi[j - 1];
            if (target[s + i] == target[s + j]) j++;
            pi[i] = j;
        }

        // scan the doubled block: text[i] = nums[s + i % k]
        j = 0;
        for (int i = 0; i < 2 * k; i++) {
            int c = nums[s + i % k];
            while (j > 0 && c != target[s + j]) j = pi[j - 1];
            if (c == target[s + j]) j++;
            if (j == k) return true;
        }
        return false;
    }
}
```

---

**Runtime** 144 ms (beats 25.0%) · **Memory** 82.2 MB (beats 6.8%)

<sub>Synced by AILeetHub on 2026-10-20.</sub>
