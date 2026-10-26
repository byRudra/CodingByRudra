# 2605. Form Smallest Number From Two Digit Arrays

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/form-smallest-number-from-two-digit-arrays/)

`Array` · `Hash Table` · `Enumeration`

## Intuition  
The crucial observation is that the smallest valid number is either a single digit that appears in **both** arrays, or a two‑digit number built from the smallest digit of each array. If any digit is shared, that digit alone satisfies the requirement and is automatically minimal because no two‑digit number can be smaller than a single digit. When there is no overlap, the only way to include a digit from each side is to place the two smallest distinct digits side by side; putting the smaller one in the tens place yields the minimal two‑digit value. A naïve solution would enumerate every possible pair (O(|nums1|·|nums2|)) and then sort, but the insight above eliminates the need for exhaustive combination generation. The pattern is a direct enumeration of minima with a constant‑time composition step.

## Approach  
1. **Initialize trackers** – `min1` and `min2` start at 10 (greater than any possible digit) and `common` also at 10.  
2. **First pass over `nums1`** – for each `a` in `nums1`:  
   - Update `min1 = Math.min(min1, a)` to keep the smallest digit seen so far.  
   - **Inner loop** over `nums2` checks every `b`. If `a == b`, update `common = Math.min(common, a)`.  
   - *Invariant*: after processing the first `k` elements of `nums1`, `min1` holds the smallest of those `k` elements, and `common` holds the smallest digit that appears in both processed prefixes.  
3. **Early return on overlap** – if `common != 10` after the nested loops, a shared digit exists; return it immediately. This handles the single‑digit answer case.  
4. **Second pass over `nums2`** – iterate `b` in `nums2` to compute `min2 = Math.min(min2, b)`. At this point we know the arrays are disjoint, so `min1` and `min2` are the smallest digits from each side.  
5. **Compose the two‑digit result** – `Math.min(min1, min2) * 10 + Math.max(min1, min2)` places the smaller digit in the tens position and the larger in the ones position, guaranteeing the minimal two‑digit number that uses one digit from each array.  

Edge handling: the code works for length‑1 arrays because the loops still execute once, and the `<= 10` sentinel avoids overflow. The choice of `10` as the sentinel is safe because digits are limited to 1‑9.

## Dry Run  

**Input**: `nums1 = [4,1,3]`, `nums2 = [5,7]`

| Step | `a` | `b` (inner) | `min1` | `common` | `min2` (after step 4) | Note |
|------|-----|-------------|--------|----------|----------------------|------|
| 1 (outer) | 4 | – | 4 | 10 | – | `min1` becomes 4 |
| 1 (inner) | 4 | 5 | 4 | 10 | – | no match |
| 1 (inner) | 4 | 7 | 4 | 10 | – | no match |
| 2 (outer) | 1 | – | 1 | 10 | – | `min1` updated to 1 |
| 2 (inner) | 1 | 5 | 1 | 10 | – | no match |
| 2 (inner) | 1 | 7 | 1 | 10 | – | no match |
| 3 (outer) | 3 | – | 1 | 10 | – | `min1` stays 1 |
| 3 (inner) | 3 | 5 | 1 | 10 | – | no match |
| 3 (inner) | 3 | 7 | 1 | 10 | – | no match |
| 4 (second pass) | – | – | 1 | 10 | 5 | `min2` becomes 5 (then 5 stays) |
| 5 (compose) | – | – | 1 | 10 | 5 | Result = `1*10 + 5 = 15` |

No common digit was found, so the algorithm returns the composed two‑digit number **15**, which is the smallest possible.

## Complexity  
- **Time:** O(|nums1|·|nums2| + |nums2|) → O(n·m) in the worst case because the nested loops scan every pair to detect a common digit, followed by a linear scan of `nums2`.  
- **Space:** O(1) – only a handful of integer variables (`min1`, `min2`, `common`, loop indices) are used, independent of input size. The output integer does not count toward extra space.

## Solution (Java)

```java
class Solution {
    public int minNumber(int[] nums1, int[] nums2) {
        int min1 = 10, min2 = 10;
        int common = 10;

        for (int a : nums1) {
            min1 = Math.min(min1, a);

            for (int b : nums2) {
                if (a == b) {
                    common = Math.min(common, a);
                }
            }
        }

        if (common != 10) {
            return common;
        }

        for (int b : nums2) {
            min2 = Math.min(min2, b);
        }

        return Math.min(min1, min2) * 10 + Math.max(min1, min2);
    
    }
}
```

---

**Runtime** 1 ms (beats 68.1%) · **Memory** 42.9 MB (beats 92.0%)

<sub>Synced by AILeetHub on 2026-10-26.</sub>
