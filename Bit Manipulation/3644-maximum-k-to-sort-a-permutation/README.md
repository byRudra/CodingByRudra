# 3644. Maximum K to Sort a Permutation

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/maximum-k-to-sort-a-permutation/)

`Array` · `Bit Manipulation`

## Intuition  
If a swap between two positions is allowed, the chosen k must be a subset of the bits that both numbers share, i.e. k ≤ (nums[i] & nums[j]). To bring every out‑of‑place element to its correct index we need a single k that works for **all** such elements, so k must be contained in the intersection of their bit‑sets. The largest integer satisfying this is exactly the bitwise AND of every element that is not already at its target position. The naïve idea of trying many k values or building a graph would be O(n log max) or worse; the AND observation eliminates all extra work.

## Approach  
1. Initialise `operations = -1`. In two’s‑complement -1 has every bit set to 1, serving as a neutral element for AND.  
2. Iterate `i` from 0 to `nums.length‑1`.  
   - **Loop invariant:** before each iteration, `operations` equals the AND of all previously seen misplaced values (or -1 if none).  
   - If `nums[i] == i` the element is already sorted; skip it.  
   - Otherwise execute `operations &= nums[i]`. This refines the candidate k by intersecting the bit‑set of the current misplaced value with the accumulated bits.  
3. After the loop, if no element was ever misplaced `operations` is still -1; return 0 as the problem requires.  
4. Otherwise return the final value of `operations`, which is the maximal k that is a subset of every misplaced element’s bits, guaranteeing that any needed swap can satisfy `nums[x] & nums[y] == k`.

Key edge handling:  
- Empty or single‑element arrays never enter the `if` body, leaving `operations` as -1, so the method returns 0.  
- The check uses `!=` rather than `<` because the permutation property guarantees each value’s correct index is exactly its value.  
- Using `&=` on a sentinel -1 avoids a separate “first‑element” branch and correctly accumulates bits without overflow concerns.

## Dry Run  
Input: `nums = [0, 3, 2, 1]`  

| i | nums[i] | nums[i] == i? | operations before | operation performed | operations after | note |
|---|---------|---------------|-------------------|---------------------|------------------|------|
| 0 | 0       | yes           | -1                | —                   | -1               | already correct |
| 1 | 3       | no            | -1                | `operations &= 3`   | -1 & 3 = 3       | first misplaced |
| 2 | 2       | yes           | 3                 | —                   | 3                | skip |
| 3 | 1       | no            | 3                 | `operations &= 1`   | 3 & 1 = 1        | refine bits |

Loop ends; `operations` = 1, not -1, so return 1. This is the maximum k because bit 0 is the only bit present in both misplaced values 3 (011) and 1 (001).

## Complexity  
- **Time:** O(n) – the single pass examines each element once; the AND operation is O(1).  
- **Space:** O(1) – only a few integer variables (`operations`, `i`) are used, independent of input size.

## Solution (Java)

```java
class Solution {
    public int sortPermutation(int[] nums) {
        int operations = -1;
        for(int i = 0; i < nums.length; i++){
            if(nums[i] != i){
                operations &= nums[i];
            }
        }
        return operations == -1 ? 0 : operations;
    }
}
```

---

**Runtime** 2 ms (beats 69.8%) · **Memory** 110.5 MB (beats 65.1%)

<sub>Synced by AILeetHub on 2026-10-23.</sub>
