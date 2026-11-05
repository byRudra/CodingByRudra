# 1385. Find the Distance Value Between Two Arrays

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/find-the-distance-value-between-two-arrays/)

`Array` · `Two Pointers` · `Binary Search` · `Sorting`

## Intuition  
If `arr2` is sorted, the elements that could violate the distance condition for a given `x` from `arr1` must lie in the interval `[x‑d, x+d]`. The first element of `arr2` that is **not** smaller than `x‑d` tells us everything: if that element is also larger than `x+d`, the interval is empty and `x` contributes to the answer; otherwise some element falls inside the interval and `x` is discarded. A naïve double loop would cost `O(|arr1|·|arr2|)`, but the sorted order lets us locate the lower bound in `O(log|arr2|)` with binary search, eliminating the extra pass or hash‑map lookup. This is the classic **binary search on a sorted array** pattern.

## Approach  
1. **Sort `arr2`.**  
   `Arrays.sort(arr2);` guarantees the monotonic order required for binary search.  

2. **Iterate over each `x` in `arr1`.**  
   `for (int x : arr1) { … }` processes elements independently, preserving `O(1)` extra state per iteration.  

3. **Find the lower bound of `x‑d` in `arr2`.**  
   Call `lowerBound(arr2, x - d)`, which returns the smallest index `i` such that `arr2[i] ≥ x‑d`.  
   - Loop invariant: `arr[low‑1] < target ≤ arr[high]` (conceptually, elements before `low` are too small, those from `high` onward are candidates).  
   - Exit when `low == high`; then `low` is the desired index.  

4. **Check whether the candidate violates the distance.**  
   `if (i == arr2.length || arr2[i] > x + d) { count++; }`  
   - `i == arr2.length` means no element ≥ `x‑d` exists, so the interval is empty.  
   - `arr2[i] > x + d` means the first element ≥ `x‑d` already exceeds the upper bound, confirming no element lies in `[x‑d, x+d]`.  

5. **Return the accumulated `count`.**  
   After the loop, `count` equals the number of `arr1` elements satisfying the distance condition.

## Dry Run  
Input: `arr1 = [4,5,8]`, `arr2 = [10,9,1,8]`, `d = 2`  

After sorting `arr2 → [1,8,9,10]`.

| iter | x | low | high | mid | i (lowerBound) | arr2[i] | condition (`i==len || arr2[i]>x+d`) | count |
|------|---|-----|------|-----|----------------|---------|--------------------------------------|-------|
| 1    | 4 | 0   | 4    | 2   | 0 (target = 2) | 1       | true (`1 > 6` is false, but `i==len` false → actually arr2[0]=1 ≤ 6, so condition false) → count stays 0 |
| 2    | 5 | 0   | 4    | 2   | 1 (target = 3) | 8       | true (`8 > 7`) → count = 1 |
| 3    | 8 | 0   | 4    | 2   | 1 (target = 6) | 8       | false (`8 > 10` false) → count stays 1 |

Explanation of rows:  
- For `x=4`, `lowerBound` returns `0`; `arr2[0]=1` lies inside `[2,6]`, so `4` is excluded.  
- For `x=5`, `lowerBound` returns `1`; `arr2[1]=8` is greater than `7`, so no element falls in `[3,7]` and `5` is counted.  
- For `x=8`, `lowerBound` returns `1`; `arr2[1]=8` is within `[6,10]`, so `8` is excluded.  

Final `count = 1`, which matches the expected distance value.

## Complexity  
- **Time:** `O(|arr1|·log|arr2|)` – each `x` triggers a binary search that halves the search space (`low` and `high` move toward each other) until `low == high`.  
- **Space:** `O(1)` extra besides the input arrays and the output integer, because the algorithm uses only a few scalar variables (`low`, `high`, `mid`, `i`, `count`).

## Solution (Java)

```java
class Solution {
    public int findTheDistanceValue(int[] arr1, int[] arr2, int d) {
        Arrays.sort(arr2);
        int count = 0;
        for (int x : arr1) {
            int i = lowerBound(arr2, x - d);
            if (i == arr2.length || arr2[i] > x + d) {
                count++;
            }
        }
        return count;
    }

    private int lowerBound(int arr[], int target) {
        int low = 0, high = arr.length;
        while (low < high) {
            int mid = low + (high - low) / 2;
            if (arr[mid] < target)
                low = mid + 1;
            else
                high = mid;
        }
        return low;
    }
}
```

---

**Runtime** 7 ms (beats 29.8%) · **Memory** 45.8 MB (beats 82.6%)

<sub>Synced by AILeetHub on 2026-11-05.</sub>
