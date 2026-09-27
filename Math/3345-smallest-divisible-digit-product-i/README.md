# 3345. Smallest Divisible Digit Product I

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/smallest-divisible-digit-product-i/)

`Math` · `Enumeration`

## Intuition  
The key observation is that for the given limits (`1 ≤ n ≤ 100`, `1 ≤ t ≤ 10`) the answer lies very close to the starting value – at most a few dozen steps away. Because the product of the digits of a number can be computed in constant time, we can simply walk forward from `n` until we encounter a number whose digit product is a multiple of `t`. The naïve alternative would be to pre‑compute all numbers up to some huge bound or to factor `t` and try to construct a number digit‑by‑digit, both of which are unnecessary here. The pattern used is a straightforward linear scan (enumeration) combined with a helper that evaluates the digit product.

## Approach  
1. **Loop start** – Begin with the given `n`.  
2. **Compute product** – Call `product(n)`, which multiplies each decimal digit:  
   ```java
   int res = 1;
   while (num > 0) { res *= num % 10; num /= 10; }
   ```  
   The loop invariant is “`res` equals the product of all digits processed so far”.  
3. **Check divisibility** – Evaluate `product(n) % t`.  
   *Exit condition*: the remainder is `0`. While it is non‑zero, the current `n` does **not** satisfy the requirement.  
4. **Advance** – Increment `n` (`n++`) and repeat step 2. The outer loop invariant is “all numbers `< n` have been proven unsuitable, so the current `n` is the smallest candidate not yet ruled out”.  
5. **Return** – When the remainder becomes `0`, return the current `n`.  

**Edge‑case handling**  
- If any digit is `0`, the product becomes `0`; `0 % t` is `0` for any `t`, so numbers containing a zero are automatically accepted.  
- Single‑digit inputs work because the product loop runs once and the outer loop still increments correctly.  
- No overflow concerns: the largest possible product for a three‑digit number ≤ 100 is `9·9·9 = 729`, well within `int`.  

## Dry Run  

**Input:** `n = 15`, `t = 3`

| Iteration | n  | product(n) | product(n) % t | Note                         |
|-----------|----|------------|----------------|------------------------------|
| 1         | 15 | 5          | 2              | 5 % 3 ≠ 0 → continue          |
| 2         | 16 | 6          | 0              | 6 % 3 = 0 → stop             |

After the second iteration the loop exits and returns `16`, which is the smallest number ≥ 15 whose digit product (6) is divisible by 3.

## Complexity  
- **Time:** O(k) where *k* is the number of increments performed. In the worst case `k ≤ 90` (from 10 to 100), because each iteration does a constant‑time digit product.  
- **Space:** O(1) extra space; only a few integer variables are used, independent of input size. (The output integer itself is not counted.)

## Solution (Java)

```java
class Solution {
    public int smallestNumber(int n, int t) {
        while(product(n) % t != 0){
            n++;
        }
        return n;
    }
    int product(int num){
        int res = 1;
        while(num > 0){
            res *= num % 10;
            num /= 10;
        }
        return res;
    }
}
```

---

**Runtime** 1 ms (beats 99.8%) · **Memory** 42.8 MB (beats 32.1%)

<sub>Synced by AILeetHub on 2026-09-27.</sub>
