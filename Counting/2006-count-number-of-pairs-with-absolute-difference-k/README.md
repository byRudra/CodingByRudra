# 2006. Count Number of Pairs With Absolute Difference K

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/count-number-of-pairs-with-absolute-difference-k/)

`Array` · `Hash Table` · `Counting`

## Intuition  
The only thing we need to know is whether any two positions satisfy `|nums[i]‑nums[j]| = k`. Because the array length is at most 200, enumerating every unordered pair is cheap enough; a full `O(n²)` scan avoids the overhead of a hash table or sorting. The key observation is that after fixing the left index `i`, the inner loop can safely walk through all larger indices `j` and count the matches, guaranteeing that each valid pair is counted exactly once.

## Approach  
1. **Initialize** `count = 0`.  
2. **Outer loop** `i` runs from `0` up to `nums.length‑1`.  
   - *Exit condition*: `i == nums.length`.  
   - *Invariant*: All pairs whose first index is `< i` have already been examined.  
3. **Inner loop** `j` starts at `nums.length‑1` and decrements while `j > i`.  
   - *Exit condition*: `j == i`.  
   - *Invariant*: For the current `i`, every index `j` with `i < j ≤ current j` has been processed.  
4. Inside the inner loop compute `Math.abs(nums[i] - nums[j])`.  
   - If the result equals `k`, increment `count`.  
5. After the inner loop finishes, the next iteration of the outer loop picks a new `i`.  
6. When both loops terminate, return `count`.  

**Edge‑case handling** – If `nums` has length `0` or `1`, the outer loop runs zero or one time and the inner loop never satisfies `j > i`, so `count` stays `0`. Duplicate values are naturally counted because each distinct index pair is examined. The descending `j` direction is a harmless convention; it still visits every `j > i` exactly once and avoids an off‑by‑one error that would arise if the loop used `j >= i`.  

## Dry Run  

Input: `nums = [1, 2, 2, 1]`, `k = 1`

| step | i | j | nums[i] | nums[j] | |nums[i]-nums[j]| | count | note |
|------|---|---|----------|----------|-------------------|-------|------|
| 1 | 0 | 3 | 1 | 1 | 0 | 0 | diff ≠ k |
| 2 | 0 | 2 | 1 | 2 | 1 | 1 | count++ |
| 3 | 0 | 1 | 1 | 2 | 1 | 2 | count++ |
| 4 | 1 | 3 | 2 | 1 | 1 | 3 | count++ |
| 5 | 1 | 2 | 2 | 2 | 0 | 3 | diff ≠ k |
| 6 | 2 | 3 | 2 | 1 | 1 | 4 | count++ |
| 7 | 3 | – | – | – | – | 4 | inner loop ends (j ≤ i) |
| 8 | – | – | – | – | – | 4 | outer loop ends (i == n) |

After the loops finish, `count = 4`, which matches the required answer because the four listed index pairs all have an absolute difference of 1.

## Complexity  
- **Time:** `O(n²)` – the outer loop runs `n` times and the inner loop runs at most `n‑i‑1` times, giving a total of roughly `n·(n‑1)/2` comparisons.  
- **Space:** `O(1)` – only a few integer variables (`count`, `i`, `j`) are used regardless of input size. The output integer does not affect the auxiliary space bound.

## Solution (Java)

```java
class Solution {
    public int countKDifference(int[] nums, int k) {
        int count = 0;
        for(int i = 0; i < nums.length; i++){
            for(int j = nums.length - 1; j > i; j --){
                if(Math.abs(nums[i] - nums[j]) == k){
                    count++;
                }
            }
        }
        return count;
    }
}
```

---

**Runtime** 7 ms (beats 58.3%) · **Memory** 44.9 MB (beats 87.3%)

<sub>Synced by AILeetHub on 2026-10-19.</sub>
