# 3069. Distribute Elements Into Two Arrays I

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/distribute-elements-into-two-arrays-i/)

`Array` · `Two Pointers` · `Simulation`

## Intuition  
The only thing that matters after each operation is the **last element** of each temporary array, because the rule compares those two values to decide where the next number goes. Consequently we can simulate the process with two pointers that always point to the current ends of `arr1` and `arr2`. A naïve solution might rebuild the two lists on the fly using dynamic structures or perform extra passes to concatenate them, but the observation that the ends are enough lets us store the elements in fixed‑size arrays and fill them in a single left‑to‑right scan. This is a classic **two‑pointer simulation** pattern.

## Approach  
1. **Initialisation** – Allocate two auxiliary arrays `arr1` and `arr2` of length `n`. Set write indices `i = 0` and `j = 0`.  
2. **Seed the first two elements** –  
   * `arr1[i++] = nums[0];` puts the first number into `arr1` and moves `i` to the next free slot.  
   * `arr2[j++] = nums[1];` does the same for the second number in `arr2`.  
   This handles the mandatory first two operations and guarantees that both arrays are non‑empty before the loop starts.  
3. **Main loop** – Iterate `k` from `2` to `n‑1` (inclusive). The loop invariant is: *the elements `arr1[0..i‑1]` and `arr2[0..j‑1]` are exactly the result of the first `k` operations, and `arr1[i‑1]` / `arr2[j‑1]` are the current last elements.*  
   * If `arr1[i‑1] > arr2[j‑1]` we append the next number to `arr1` with `arr1[i++] = nums[k];`.  
   * Otherwise we append it to `arr2` with `arr2[j++] = nums[k];`.  
   The `>` comparison follows the problem rule; using `<=` would flip the tie‑breaking convention, which the code deliberately avoids because all numbers are distinct.  
4. **Concatenation** – Allocate `result` of size `n` and a separate write pointer `index = 0`.  
   * Copy the filled prefix of `arr1` (`x = 0 .. i‑1`) into `result` while incrementing `index`.  
   * Then copy the filled prefix of `arr2` (`x = 0 .. j‑1`) in the same way.  
   Because `i + j == n`, every element of `nums` ends up exactly once in `result`.  
5. **Return** – The fully built `result` array is returned.

## Dry Run  
**Input:** `nums = [5, 4, 3, 8]`

| k | arr1 (i)          | arr2 (j)          | Decision (why)                     |
|---|-------------------|-------------------|------------------------------------|
| – | `[5]` (i=1)       | `[4]` (j=1)       | seed step                          |
| 2 | `arr1[i‑1]=5 > arr2[j‑1]=4` → `arr1[i++] = 3` → `[5,3]` (i=2) | `[4]` (j=1) | 5 > 4, so 3 goes to `arr1` |
| 3 | `arr1[i‑1]=3 < arr2[j‑1]=4` → `arr2[j++] = 8` → `[4,8]` (j=2) | `[5,3]` (i=2) | 3 < 4, so 8 goes to `arr2` |

After the loop `i = 2`, `j = 2`. Concatenating the prefixes yields `result = [5,3,4,8]`, which matches the required output.

## Complexity  
- **Time:** `O(n)` – the algorithm scans `nums` once (`k` loop) and then copies `i + j = n` elements into `result`.  
- **Space:** `O(n)` – two auxiliary arrays of size `n` plus the output array; no additional dynamic structures are used.

## Solution (Java)

```java
class Solution {
    public int[] resultArray(int[] nums) {
        int n = nums.length;

        int[] arr1 = new int[n];
        int[] arr2 = new int[n];

        int i = 0, j = 0;

        arr1[i++] = nums[0];
        arr2[j++] = nums[1];

        for (int k = 2; k < n; k++) {
            if (arr1[i - 1] > arr2[j - 1]) {
                arr1[i++] = nums[k];
            } else {
                arr2[j++] = nums[k];
            }
        }

        int[] result = new int[n];

        int index = 0;

        for (int x = 0; x < i; x++) {
            result[index++] = arr1[x];
        }

        for (int x = 0; x < j; x++) {
            result[index++] = arr2[x];
        }

        return result;
    }
}
```

---

**Runtime** 1 ms (beats 99.7%) · **Memory** 46.6 MB (beats 56.2%)

<sub>Synced by AILeetHub on 2026-09-27.</sub>
