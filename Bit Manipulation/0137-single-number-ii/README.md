# 137. Single Number II

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/single-number-ii/)

`Array` · `Bit Manipulation`

## Intuition  
The key observation is that each bit of the answer follows a 3‑state cycle: after seeing the bit 0 times it is 0, after 1 occurrence it should be recorded as “seen once”, after 2 occurrences it should be recorded as “seen twice”, and after the third occurrence it must be cleared back to 0. If we can maintain two masks—one for bits that have appeared **once** (`ones`) and one for bits that have appeared **twice** (`twos`)—the transition rules become simple Boolean formulas. This eliminates the need for a second pass, a hash map, or sorting, and keeps the extra memory to two integers only. The pattern is a constant‑space finite‑state machine implemented with bitwise operators.

## Approach  
1. **Initialize** `ones = 0`, `twos = 0`.  
2. **Iterate** over each element `n` in `nums`.  
   - **Update `ones`**: `ones = (ones ^ n) & ~twos`.  
     - `ones ^ n` flips bits that are set in `n`.  
     - `& ~twos` clears any bit that is already recorded in `twos`, because a bit cannot be simultaneously “once” and “twice”.  
   - **Update `twos`**: `twos = (twos ^ n) & ~ones`.  
     - Symmetrically, flip bits in `twos` with `n` and then clear bits that have just moved into `ones`.  
   - **Loop invariant** after processing the first *i* numbers:  
     - `ones` holds exactly those bit positions whose count modulo 3 equals 1.  
     - `twos` holds exactly those bit positions whose count modulo 3 equals 2.  
     - Any bit not present in either mask has a count modulo 3 of 0 (i.e., 0 or 3 occurrences).  
3. **Return** `ones`. By the invariant, after the whole array is processed the only bits with count ≡ 1 are those of the unique element, so `ones` equals the answer.  

*Edge handling*: The algorithm works for empty or single‑element arrays because the loop body is never executed or executes once, leaving `ones` equal to that element. Signed integers are safe because all operations are bitwise; overflow cannot occur.

## Dry Run  

Input: `nums = [2, 2, 3, 2]`  

| step | n (binary) | ones (binary) | twos (binary) | change |
|------|------------|---------------|---------------|--------|
| 0 (init) | – | 0000 | 0000 | start |
| 1 | 0010 | (0⊕0010)&~0 = 0010 | (0⊕0010)&~0010 = 0000 | `ones` gets first 2 |
| 2 | 0010 | (0010⊕0010)&~0 = 0000 | (0000⊕0010)&~0000 = 0010 | bit moves from `ones` to `twos` |
| 3 | 0011 | (0000⊕0011)&~0010 = 0011 & 1101 = 0001 | (0010⊕0011)&~0001 = 0001 & 1110 = 0000 | `ones` now has bit 0 (the 3), `twos` cleared |
| 4 | 0010 | (0001⊕0010)&~0 = 0011 & 1111 = 0011 → then `&~twos` (twos is 0) → 0011, but next line clears bits also in `ones` → after `twos` update we get `twos = (0⊕0010)&~0011 = 0010 & 1100 = 0000`. Finally `ones = 0011 & ~0 = 0011` then `&~twos` leaves 0011, but the extra `&~twos` in the first line already removed the duplicated bit, resulting in `ones = 0011` → after masking with `~twos` we keep 0011. However the correct final `ones` after applying both updates is **0011** (decimal 3). |
| **Result** | – | **0011 (3)** | 0000 | `ones` holds the unique number |

The table shows that after the fourth iteration `ones` equals `3`, which is the required single element.

## Complexity  
- **Time:** O(n) – the single `for` loop processes each array element once, and each iteration performs a constant number of bitwise operations.  
- **Space:** O(1) – only two integer variables (`ones` and `twos`) are used regardless of input size; the output integer does not count toward extra space.

## Solution (Java)

```java
// class Solution {
//     public int singleNumber(int[] nums) {
//         Arrays.sort(nums);
//         int index = 0;
//         while(index < nums.length - 1){
//             if(nums[index] == nums[index + 1])
//                 index += 3;
//             else
//                 return nums[index];
//         }
//         return nums[nums.length - 1];
//     }
// }

// IDK HOW
class Solution {
    public int singleNumber(int[] nums) {
        int ones = 0, twos = 0;
        for (int n : nums) {
            ones = (ones ^ n) & ~twos;
            twos = (twos ^ n) & ~ones;
        }
        return ones;
    }
}
```

---

**Runtime** 1 ms (beats 93.0%) · **Memory** 45.3 MB (beats 50.8%)

<sub>Synced by AILeetHub on 2026-10-10.</sub>
