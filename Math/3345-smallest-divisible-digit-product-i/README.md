# 3345. Smallest Divisible Digit Product I

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/smallest-divisible-digit-product-i/)

`Math` · `Enumeration`

## Intuition  
The key observation is that the constraints are tiny ( n ≤ 100, t ≤ 10 ), so the answer cannot be far from the starting value. If we can compute the digit product of a candidate in O(number of digits) time, we can simply test successive integers until the product becomes a multiple of t. A naïve “enumerate all numbers up to 10⁹” would be hopeless, but here the search space is bounded by at most a few hundred steps, eliminating the need for any pre‑computation, hashing, or binary search. This is a classic **brute‑force enumeration** pattern.

## Approach  
1. **Loop until condition holds** – `while (product(n) % t != 0) { n++; }`.  
   *Exit condition*: `product(n) % t == 0`.  
   *Invariant*: At the start of each iteration, `n` is the smallest integer ≥ the original input that has not yet been proven to satisfy the divisibility requirement.  
2. **Compute digit product** – `product(int num)` multiplies all decimal digits of `num`.  
   *Loop*: `while (num > 0) { res *= num % 10; num /= 10; }`.  
   *Exit condition*: `num == 0`.  
   *Invariant*: After each inner iteration, `res` equals the product of the digits processed so far, and the remaining `num` holds the yet‑unprocessed suffix.  
3. **Return the found value** – once the outer loop exits, `n` is guaranteed to be the smallest qualifying number, so `return n;`.  

**Edge‑case handling**:  
- If the original `n` already satisfies the condition, the outer loop body is skipped entirely, returning the input unchanged.  
- A digit `0` forces `res` to become `0`; because `0 % t == 0` for any positive `t`, numbers containing a zero are automatically accepted, matching the problem’s definition.  
- The code treats all numbers uniformly; there is no special case for single‑digit inputs because the product loop correctly returns the digit itself.  

## Dry Run  

**Input**: `n = 15, t = 3`

| Iteration | n  | product(n) | product(n) % t | Change                         |
|-----------|----|------------|----------------|--------------------------------|
| 0 (start) | 15 | 1·5 = 5    | 5 % 3 = 2      | initial check fails           |
| 1         | 16 | 1·6 = 6    | 6 % 3 = 0      | n incremented, product now 0  |

The loop stops after the second check because `product(16) % 3 == 0`. The final state is `n = 16`, which is the smallest number ≥ 15 whose digit product is divisible by 3.

## Complexity  
- **Time:** O(k·d) where *k* is the number of increments performed and *d* ≤ 3 is the digit count of each candidate (since n ≤ 100). In the worst case *k* ≤ 90, so the loop runs at most O(100) steps.  
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
