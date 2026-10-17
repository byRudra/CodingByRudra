# 371. Sum of Two Integers

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/sum-of-two-integers/)

`Math` · `Bit Manipulation`

## Intuition  
When two binary numbers are added, each bit’s result is the XOR of the bits (the “sum without carry”) while the carry for the next higher bit is the AND of the bits shifted left. Repeating this process until no carry remains yields the exact arithmetic sum. The naïve way would simulate addition digit‑by‑digit or use `+`, both of which are disallowed; the insight removes the need for any extra pass, extra storage, or recursion by folding carry propagation into a single loop.

## Approach  
1. **Initialize** `a` and `b` with the two input integers.  
2. **Loop while `b != 0`** – the loop invariant is: `a` holds the intermediate sum without the carries that are still stored in `b`.  
3. **Compute carry:** `carry = (a & b) << 1`.  
   * `a & b` isolates positions where both bits are 1 (a carry is generated).  
   * Shifting left aligns the carry with the next higher bit position.  
4. **Update sum without carry:** `a = a ^ b`.  
   * XOR adds bits where exactly one of the operands has a 1, discarding the newly generated carries.  
5. **Propagate the carry:** `b = carry`.  
   * The next iteration will treat these shifted carries as the new “addend”.  
6. **Terminate** when `b` becomes zero – at that point all carries have been merged into `a`, so `a` is the final result.  
7. **Return** `a`.

Edge handling: the code works for negative numbers because Java’s two’s‑complement representation makes bitwise AND, XOR, and left shift behave identically for signed ints. No special case for zero or single‑element inputs is needed; the loop condition `b != 0` naturally skips the body when `b` is initially zero.

## Dry Run  
Input: `a = 1 (0001)`, `b = 2 (0010)`

| Iteration | a (binary) | b (binary) | carry = (a & b) << 1 | a = a ^ b | b = carry | Note |
|-----------|------------|------------|----------------------|-----------|-----------|------|
| 0 (start) | 0001       | 0010       | 0000                 | 0011      | 0000      | compute first carry, sum without carry |
| 1 (exit)  | 0011       | 0000       | –                    | –         | –         | loop ends because b == 0 |

After the loop, `a = 0011₂ = 3`, which is the correct sum.

## Complexity  
- **Time:** O(k) where *k* is the number of carry propagation steps; in the worst case it is O(log |max(a,b)|) because each iteration shifts the carry left, and for 32‑bit ints *k* ≤ 32.  
- **Space:** O(1) extra space, only a few integer variables are used regardless of input size. (The output integer itself is not counted.)

## Solution (Java)

```java
class Solution {
    public int getSum(int a, int b) {
        while (b != 0) {
            int carry = (a & b) << 1;
            a = a ^ b;
            b = carry;
        }
        return a;
    }
}
```

---

**Runtime** 0 ms (beats 100.0%) · **Memory** 41.6 MB (beats 97.1%)

<sub>Synced by AILeetHub on 2026-10-17.</sub>
