# 1822. Sign of the Product of an Array

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/sign-of-the-product-of-an-array/)

`Array` · `Math`

## Intuition  
The sign of a product is completely determined by two facts: any factor equal to 0 forces the whole product to 0, and otherwise the product is positive exactly when the number of negative factors is even. Computing the actual product would overflow even for modest arrays, so we can discard the magnitude and keep only a zero‑check and a parity counter. This observation eliminates the need for a second pass, a hash map, or big‑integer arithmetic. The pattern used is a simple **parity count** (counting negatives modulo 2).

## Approach  
1. **Initialize** `negativeCount = 0`.  
2. **Iterate** over each `num` in `nums` using the enhanced `for` loop.  
   - **Exit condition**: the loop ends when all elements have been visited.  
   - **Invariant** after processing the first *k* elements:  
     *`negativeCount` equals the number of negative values among those *k* elements, and no zero has been seen.*  
   - **Step actions**:  
     a. If `num == 0`, **return 0** immediately – the product’s sign is fixed.  
     b. If `num < 0`, increment `negativeCount`.  
3. **After the loop**, all elements are non‑zero. The product’s sign depends only on the parity of `negativeCount`.  
   - Return `1` if `negativeCount % 2 == 0`, otherwise return `-1`.  

**Edge‑case handling**:  
- An empty array cannot occur because the constraints guarantee at least one element.  
- For a single‑element array the loop processes that element; a zero yields `0`, a negative yields `-1`, and a positive yields `1`.  
- The code uses `<=` vs `<` only for the zero check (`num == 0`), which is the correct exact comparison; no off‑by‑one issues arise because we never use indices.  

## Dry Run  

**Input**: `[-1, 1, -1, 1, -1]`

| Iteration | `num` | `negativeCount` | Action taken | Note |
|-----------|------|----------------|--------------|------|
| 1 | -1 | 0 → 1 | increment | first negative |
| 2 | 1  | 1 | none | positive, unchanged |
| 3 | -1 | 1 → 2 | increment | second negative |
| 4 | 1  | 2 | none | positive, unchanged |
| 5 | -1 | 2 → 3 | increment | third negative |
| End | – | 3 | parity check | 3 % 2 = 1 → return -1 |

The final `negativeCount` is odd, so the algorithm returns `-1`, matching the sign of the actual product (‑1).

## Complexity  
- **Time:** O(n) – the single `for` loop visits each of the *n* elements once, and the early return on zero can only shorten the traversal.  
- **Space:** O(1) – only a constant‑size integer (`negativeCount`) is stored; the output integer does not count toward extra space.

## Solution (Java)

```java
class Solution {
    public int arraySign(int[] nums) {
        int negativeCount = 0;
        for(int num : nums){
            if (num == 0) return 0;
            if (num < 0) negativeCount++;
        }
        return negativeCount % 2 == 0 ? 1 : -1; 
    }
}
```

---

**Runtime** 0 ms (beats 100.0%) · **Memory** 45.6 MB (beats 6.3%)

<sub>Synced by AILeetHub on 2026-10-12.</sub>
