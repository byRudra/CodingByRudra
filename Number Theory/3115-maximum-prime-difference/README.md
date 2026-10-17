# 3115. Maximum Prime Difference

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/maximum-prime-difference/)

`Array` · `Math` · `Number Theory` · `Primality Test`

## Intuition  
The distance we need is simply the gap between the left‑most and right‑most prime positions. Once we know the first index where a prime appears (`min`) and the last such index (`max`), the answer is `max - min`. A naïve solution might collect all prime indices in a list and then compute the difference, which costs extra linear space and a second pass to pick the extremes. The key insight is that a single forward scan can maintain both extremes on the fly, eliminating any additional storage or passes. This is a classic **two‑pointer‑like** invariant where one pointer (`min`) is set at the first hit and the other (`max`) trails the most recent hit.

## Approach  
1. **Initialize** `min = -1` and `max = -1`. The sentinel `-1` signals that no prime has been seen yet.  
2. **Iterate** `i` from `0` to `nums.length‑1`.  
   - **Check primality** of `nums[i]` via `isPrime`.  
   - **If** `nums[i]` is prime:  
     - **If** `min == -1` (first prime encountered), assign `min = i`.  
     - **Always** assign `max = i` (the most recent prime index).  
   - Loop invariant: after processing index `i`, `min` holds the smallest prime index ≤ i and `max` holds the largest prime index ≤ i.  
3. **Exit** the loop when `i == nums.length`. At this point `min` and `max` are the global extremes because the invariant has been preserved for every element.  
4. **Return** `max - min`. The problem guarantees at least one prime, so `max` and `min` are never left at `-1`.  

**Edge handling** –  
- Empty or single‑element arrays are impossible by constraints, but the code would still work: a single prime yields `max == min`, returning `0`.  
- The `isPrime` routine stops at `i * i <= num`, avoiding overflow and unnecessary work; the loop uses `<=` because a factor equal to the square root still proves compositeness (e.g., `4 = 2*2`).  

## Dry Run  
Input: `nums = [4, 2, 9, 5, 3]`

| i | nums[i] | isPrime? | min | max | note |
|---|---------|----------|-----|-----|------|
| 0 | 4       | false    | -1  | -1  | no update |
| 1 | 2       | true     | 1   | 1   | first prime sets both |
| 2 | 9       | false    | 1   | 1   | unchanged |
| 3 | 5       | true     | 1   | 3   | max moves to new prime |
| 4 | 3       | true     | 1   | 4   | max moves again |

After the loop `min = 1`, `max = 4`; the function returns `4‑1 = 3`, which matches the expected maximum prime distance.

## Complexity  
- **Time:** `O(n * √M)` where `n = nums.length` and `M ≤ 100`. Each element is examined once, and `isPrime` runs up to `√M` (at most 10) iterations, so the overall work is linear in `n`.  
- **Space:** `O(1)` extra space; only a few integer variables (`min`, `max`, loop counters) are used, independent of input size. The output integer does not count toward auxiliary space.

## Solution (Java)

```java
class Solution {
    public int maximumPrimeDifference(int[] nums) {
        int min = -1;
        int max = -1;

        for (int i = 0; i < nums.length; i++) {
            if (isPrime(nums[i])) {
                if (min == -1) {
                    min = i;
                }
                max = i;
            }
        }

        return max - min;
    }

    private boolean isPrime(int num) {
        if (num < 2) return false;

        for (int i = 2; i * i <= num; i++) {
            if (num % i == 0) {
                return false;
            }
        }

        return true;
    }
}
```

---

**Runtime** 12 ms (beats 49.8%) · **Memory** 116.7 MB (beats 82.9%)

<sub>Synced by AILeetHub on 2026-10-17.</sub>
