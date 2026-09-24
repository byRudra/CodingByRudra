# 3550. Smallest Index With Digit Sum Equal to Index

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/smallest-index-with-digit-sum-equal-to-index/)

`Array` · `Math`

## Intuition  
The only thing that matters is the relationship between an index `i` and the digit sum of the element at that position. If we scan the array from left to right, the first time we encounter `digitSum(nums[i]) == i` we have found the smallest possible index, because any later index is larger by definition. A naïve solution might try to pre‑compute all digit sums, store them in a map, or perform a second pass, but the observation that a single linear scan suffices eliminates all extra work. This is a classic **single‑pass linear scan** pattern.

## Approach  
1. **Iterate over indices** – `for (int i = 0; i < nums.length; i++)`.  
   *Exit condition*: `i` reaches `nums.length`.  
   *Invariant*: All indices `< i` have already been checked and none satisfied the condition.  
2. **Compute digit sum** – call `digitSum(nums[i])`.  
   Inside `digitSum`:  
   - Initialise `sum = 0`.  
   - While `num > 0`, add `num % 10` to `sum` and truncate the last digit with `num /= 10`.  
   *Exit condition*: `num` becomes `0`.  
   *Invariant*: `sum` always equals the sum of the digits processed so far.  
3. **Compare** – if the returned sum equals the current index `i`, immediately `return i`. This early return guarantees the smallest index because the loop proceeds in increasing order.  
4. **Fallback** – if the loop finishes without a match, `return -1`. This covers empty‑or‑single‑element edge cases automatically: the loop body simply never satisfies the equality, so `-1` is returned.

The code deliberately uses `<=` in the loop condition (`i < nums.length`) rather than `<= nums.length - 1` to avoid off‑by‑one confusion and to make the bound explicit. No overflow concerns arise because `nums[i] ≤ 1000`, and the digit‑sum loop reduces the number each iteration.

## Dry Run  

**Input**: `nums = [1, 10, 11]`

| Iteration | `i` | `nums[i]` | `digitSum(nums[i])` | Note |
|-----------|-----|-----------|----------------------|------|
| 1 | 0 | 1 | 1 | `1 != 0` → continue |
| 2 | 1 | 10 | 1 | `1 == 1` → return 1 |

The loop stops after the second iteration because the condition holds, and the algorithm returns `1`, which is the smallest qualifying index.

## Complexity  
- **Time:** **O(n · d)** where `n = nums.length` and `d` is the maximum number of digits in any element (≤ 4 for the given constraints). The outer loop runs `n` times, and each call to `digitSum` processes at most `d` digits.  
- **Space:** **O(1)** extra space; only a few integer variables (`i`, `sum`, `num`) are used, independent of input size. The output integer does not count toward the auxiliary space.

## Solution (Java)

```java
class Solution {
    public int smallestIndex(int[] nums) {
        for(int i = 0; i < nums.length; i++){
            if (digitSum(nums[i]) == i) return i;
        }
        return -1;
    }
    private int digitSum(int num){
        int sum = 0;
        while(num > 0){
            sum += num % 10;
            num /= 10;
        }
        return sum;
    }
}
```

---

**Runtime** 1 ms (beats 99.7%) · **Memory** 45.9 MB (beats 11.4%)

<sub>Synced by AILeetHub on 2026-09-24.</sub>
