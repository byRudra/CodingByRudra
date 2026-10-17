# 628. Maximum Product of Three Numbers

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/maximum-product-of-three-numbers/)

`Array` · `Math` · `Sorting`

## Intuition  
The key observation is that a maximum product of three numbers can only come from one of two patterns after the array is ordered: either the three largest values (all positive or the least negative) or the two smallest values (both negative, whose product becomes positive) multiplied by the largest value. This eliminates the need to examine every triple, which would be O(n³), and also avoids extra bookkeeping such as hash maps or multiple passes. The solution therefore relies on the **two‑candidates after sorting** pattern.

## Approach  
1. **Sort the array** `nums` in non‑decreasing order using `Arrays.sort`.  
2. **Identify length** `n = nums.length`.  
3. **Compute candidate A**: product of the three greatest elements – `nums[n‑1] * nums[n‑2] * nums[n‑3]`.  
4. **Compute candidate B**: product of the two smallest elements (potentially both negative) and the greatest element – `nums[0] * nums[1] * nums[n‑1]`.  
5. **Return the larger** of the two candidates with `Math.max`.  

*Loop/exit conditions*: The only loop is inside `Arrays.sort`, which terminates when the entire array is ordered; its invariant is that the prefix up to the current index is sorted. No explicit loops remain in the solution.  

*Edge‑case handling*:  
- **Length < 3** never occurs because the constraints guarantee at least three elements.  
- **All negative numbers**: candidate A (three largest, i.e., the three least negative) yields the correct maximum, as shown in `[-1,-2,-3]`.  
- **Mixed signs with even count of negatives**: candidate B captures the situation where two large‑magnitude negatives turn positive when multiplied, then multiplied by the largest positive.  
- The code uses `<=` internally in the sort implementation; the surrounding logic does not need additional bound checks because indices `0,1,n‑3,n‑2,n‑1` are always valid for `n ≥ 3`.

## Dry Run  

**Input**: `[-4, -2, 1, 3, 5]`

| Iteration | `nums` after sort                | `n` | Candidate A (`nums[n‑1]*nums[n‑2]*nums[n‑3]`) | Candidate B (`nums[0]*nums[1]*nums[n‑1]`) | Note                              |
|-----------|----------------------------------|-----|----------------------------------------------|------------------------------------------|-----------------------------------|
| 0         | `[-4, -2, 1, 3, 5]` (sorted)    | 5   | `5 * 3 * 1 = 15`                             | `-4 * -2 * 5 = 40`                       | Both products computed            |
| –         | –                                | –   | –                                            | –                                        | `Math.max(15,40) = 40` returned   |

The algorithm stops after sorting; the table shows the single evaluation step. The final answer is `40`, because using the two smallest negatives with the largest positive yields a larger product than the three largest numbers.

## Complexity  
- **Time:** O(n log n) – sorting dominates; the subsequent constant‑time multiplications run once.  
- **Space:** O(1) extra – only a few integer variables are used; the sort may use O(log n) stack space internally, which is excluded from the explicit allocation count.

## Solution (Java)

```java
class Solution {
    public int maximumProduct(int[] nums) {
        Arrays.sort(nums);
        int n = nums.length;

        return Math.max(
            nums[n - 1] * nums[n - 2] * nums[n - 3],
            nums[0] * nums[1] * nums[n - 1]
        );
    }
}
```

---

**Runtime** 15 ms (beats 74.0%) · **Memory** 47.9 MB (beats 67.7%)

<sub>Synced by AILeetHub on 2026-10-17.</sub>
