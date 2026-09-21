# 3524. Find X Value of Array I

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/find-x-value-of-array-i/)

`Array` · `Math` · `Dynamic Programming`

## Intuition  
The operation “remove a (possibly empty) prefix and suffix” is equivalent to **choosing a non‑empty contiguous subarray to keep**. Hence the x‑value of the array is simply the number of subarrays whose product leaves remainder x modulo k. A naïve solution would enumerate all O(n²) subarrays and compute each product, which is far too slow. The key observation is that the product modulo k of a subarray can be updated incrementally: when we extend a subarray by one element, the new remainder is `(oldRemainder * (num % k)) % k`. By maintaining, for each possible remainder, how many subarrays end at the previous position, we can compute the counts for the current position in O(k) time. This is the classic **dynamic programming over remainders** pattern.

## Approach  
1. **Initialisation** – Create `result[k]` to store final counts and `dp[k]` to store counts of subarrays ending at the previous index for each remainder. All entries start at 0.  
2. **Iterate through `nums`** (`for (int num : nums)`).  
   - Compute `rem = num % k`.  
   - Allocate a fresh array `next[k]` for the current index.  
3. **Start a new subarray at the current element** – Increment `next[rem]` because the subarray consisting solely of `num` has remainder `rem`.  
4. **Extend previous subarrays** – Loop `r` from 0 to k‑1:  
   - If `dp[r] != 0`, the product of a subarray ending at the previous index has remainder `r`.  
   - The new remainder after appending `num` is `newRem = (int)((long) r * rem % k)`.  
   - Add `dp[r]` to `next[newRem]`. This respects the invariant that `dp` always reflects counts for subarrays ending exactly at the last processed position.  
5. **Accumulate into the answer** – For every remainder `r`, add `next[r]` to `result[r]`. At this point `next` contains *all* subarrays that end at the current index, so each contributes to the global count.  
6. **Shift window** – Assign `dp = next` so that the next iteration treats the current position as the “previous” one.  
7. **Return** `result` after the loop finishes.

Edge cases are handled naturally: an empty prefix/suffix corresponds to starting a new subarray (`next[rem]++`), and a single‑element array is counted in the first iteration. Because `k ≤ 5`, the inner loop over remainders is constant‑time, and the algorithm never uses modulo‑inverse or division, avoiding overflow by casting to `long` before multiplication.

## Dry Run  
**Input:** `nums = [2, 3, 4]`, `k = 3`

| i (index) | num | rem | dp before (r=0,1,2) | next after processing | result after this step (r=0,1,2) | change |
|----------|-----|-----|----------------------|-----------------------|-----------------------------------|--------|
| 0 | 2 | 2 | (0,0,0) | start: `next[2]++` → (0,0,1) | result += next → (0,0,1) | new subarray `[2]` |
| 1 | 3 | 0 | (0,0,1) | start: `next[0]++` → (1,0,0) <br> extend r=2: `newRem = (2*0)%3 = 0` → `next[0]+=1` → (2,0,0) | result += next → (2,0,1) | `[3]`, `[2,3]` |
| 2 | 4 | 1 | (2,0,0) | start: `next[1]++` → (0,1,0) <br> extend r=0 (cnt = 2): `newRem = (0*1)%3 = 0` → `next[0]+=2` → (2,1,0) | result += next → (4,1,1) | `[4]`, `[3,4]`, `[2,3,4]` |

Final `result = [4,1,1]`, meaning there are 4 subarrays with product ≡ 0 ( `[2,3]`, `[3]`, `[2,3,4]`, `[3,4]` ), 1 with ≡ 1 (`[4]`), and 1 with ≡ 2 (`[2]`).

## Complexity  
- **Time:** O(n · k) – the outer loop runs `n` times and the inner remainder loop runs `k` times (here `k ≤ 5`, so effectively linear).  
- **Space:** O(k) – only the three length‑k arrays (`result`, `dp`, `next`) are stored, independent of `n`. The output array is excluded from the extra‑space count.

## Solution (Java)

```java
class Solution {
    public long[] resultArray(int[] nums, int k) {
        long[] result = new long[k];

        // dp[r] = number of subarrays ending at the previous index
        // whose product % k == r
        long[] dp = new long[k];

        for (int num : nums) {
            long[] next = new long[k];

            int rem = num % k;

            // Start a new subarray with nums[i]
            next[rem]++;

            // Extend every previous subarray
            for (int r = 0; r < k; r++) {
                if (dp[r] != 0) {
                    int newRem = (int)((long) r * rem % k);
                    next[newRem] += dp[r];
                }
            }

            // Add all subarrays ending at this position
            for (int r = 0; r < k; r++) {
                result[r] += next[r];
            }

            dp = next;
        }

        return result;
    }
}
```

---

**Runtime** 14 ms (beats 67.3%) · **Memory** 93.4 MB (beats 48.1%)

<sub>Synced by AILeetHub on 2026-09-21.</sub>
