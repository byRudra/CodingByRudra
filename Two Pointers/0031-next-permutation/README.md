# 31. Next Permutation

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/next-permutation/)

`Array` · `Two Pointers`

## Intuition  
The array can be split into a longest non‑increasing suffix and the part that precedes it. Any permutation larger than the current one must differ first at the element just before that suffix – call it the **pivot**. If the whole array is non‑increasing, no larger permutation exists and we must wrap around to the smallest order. The key insight is that swapping the pivot with the smallest element larger than it in the suffix, then reversing the suffix, yields the immediate lexicographic successor. A naïve approach would generate all permutations ( O(n · n!) ) or sort the suffix with extra memory; the pivot‑swap‑reverse trick eliminates both.

## Approach  
1. **Locate the pivot**  
   ```java
   int i = n - 2;
   while (i >= 0 && nums[i] >= nums[i + 1]) i--;
   ```  
   *Exit condition*: `i < 0` (entire array non‑increasing) or `nums[i] < nums[i+1]`.  
   *Invariant*: All indices > i form a non‑increasing sequence.

2. **If a pivot exists (`i >= 0`), find the rightmost successor**  
   ```java
   int j = n - 1;
   while (j >= 0 && nums[j] <= nums[i]) j--;
   ```  
   *Exit condition*: `nums[j] > nums[i]`.  
   *Invariant*: Elements to the right of `j` are ≤ `nums[i]`; `j` ends at the smallest element larger than the pivot because we scan from the end.

3. **Swap pivot and successor**  
   ```java
   int temp = nums[i];
   nums[i] = nums[j];
   nums[j] = temp;
   ```  
   This creates a prefix that is just larger than the original prefix.

4. **Reverse the suffix** (`i+1 … n-1`) to obtain the minimal ordering after the new prefix  
   ```java
   int lo = i + 1, hi = n - 1;
   while (lo < hi) {
       int temp = nums[lo];
       nums[lo] = nums[hi];
       nums[hi] = temp;
       lo++; hi--;
   }
   ```  
   *Exit condition*: `lo >= hi`.  
   *Invariant*: The segment `[i+1, hi]` is being mirrored; after the loop it is sorted in ascending order because the original suffix was non‑increasing.

Edge cases handled explicitly:  
- Length 1 (`n‑2` becomes `-1`) → the first while terminates immediately, `i < 0`, and the final reverse (which is a no‑op) leaves the array unchanged.  
- All equal elements → same path as the non‑increasing case, resulting in the original array (which is already the smallest permutation).  
- Even vs. odd length does not affect the reverse loop because the `lo < hi` guard stops before crossing.

## Dry Run  

**Input**: `nums = [1, 2, 3]`

| Step | i | j | lo | hi | nums (after step) | Note |
|------|---|---|----|----|-------------------|------|
| Init pivot search | 1 | – | – | – | [1,2,3] | `i = n-2 = 1` |
| while (i≥0 && nums[i]≥nums[i+1]) | 0 | – | – | – | [1,2,3] | `nums[1]=2 < nums[2]=3` stops, pivot at index 0 |
| Find successor (`j`) | 0 | 2 | – | – | [1,2,3] | `nums[2]=3 > nums[0]=1` |
| Swap pivot & successor | 0 | 2 | – | – | [3,2,1] | swap positions 0 and 2 |
| Init reverse (`lo=i+1`, `hi=n-1`) | 0 | 2 | 1 | 2 | [3,2,1] | |
| Reverse iteration 1 (`lo<hi`) | 0 | 2 | 2 | 1 | [3,1,2] | swap indices 1 and 2 |
| Loop ends (`lo>=hi`) | – | – | – | – | [3,1,2] | suffix now ascending |

The final array `[3,1,2]` is the lexicographically smallest permutation larger than `[1,2,3]`.

## Complexity  
- **Time:** `O(n)` – the first while scans at most `n‑1` elements, the second while scans at most `n‑1` elements, and the final reverse touches each element once.  
- **Space:** `O(1)` – only a handful of integer variables (`i, j, lo, hi, temp`) are used; the transformation is performed in‑place.

## Solution (Java)

```java
class Solution {
    public void nextPermutation(int[] nums) {
        int n = nums.length;
        int i = n - 2;
        // 1. Find the pivot
        while (i >= 0 && nums[i] >= nums[i + 1]) {
            i--;
        }

        if(i >= 0){
            int j = n - 1;
            while(j >= 0 && nums[j] <= nums[i])
                j--;
            int temp = nums[i];
            nums[i] = nums[j];
            nums[j] = temp;
        }


        int lo = i + 1;
        int hi = n - 1;

        while (lo < hi) {
            int temp = nums[lo];
            nums[lo] = nums[hi];
            nums[hi] = temp;
            lo++;
            hi--;
        }
    }
}
```

---

**Runtime** 0 ms (beats 100.0%) · **Memory** 44.3 MB (beats 96.5%)

<sub>Synced by AILeetHub on 2026-09-08.</sub>
