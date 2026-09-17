# 1477. Find Two Non-overlapping Sub-arrays Each With Target Sum

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/find-two-non-overlapping-sub-arrays-each-with-target-sum/)

`Array` · `Hash Table` · `Binary Search` · `Dynamic Programming` · `Sliding Window`

## Intuition  
When a sliding window expands to the right, its left edge moves only when the current sum exceeds the target. Hence after each iteration the window `[left, right]` is the **shortest possible** sub‑array ending at `right` that could equal the target. The missing piece is a way to remember the best (shortest) valid sub‑array that ends **strictly before** the current window starts. If we keep that “best‑before” length and combine it with the current window length whenever the current sum equals the target, we obtain the minimum total length without a second pass, a hash map, or sorting. This is a classic two‑pointer / sliding‑window pattern with a *shadow* window that lags behind the main one.

## Approach  
1. **Initialize**  
   - `left = 0`, `sum = 0` for the main window.  
   - `l2 = 0`, `r2 = -1`, `sum2 = 0` for the shadow window that scans the prefix `[0, left‑1]`.  
   - `bestBefore = INF` stores the shortest length found in the prefix.  
   - `ans = INF` stores the best combined length.  

2. **Advance the main window** (`right` from `0` to `n‑1`)  
   - `sum += arr[right]`.  
   - While `sum > target`, shrink from the left: `sum -= arr[left]; left++`.  
   - At this point `[left, right]` is the minimal window ending at `right` with sum ≤ target.  

3. **Catch the shadow window up to `left‑1`**  
   - While `r2 < left‑1`, move `r2` forward, adding `arr[r2]` to `sum2`.  
   - If `sum2 > target`, shrink `l2` similarly: `sum2 -= arr[l2]; l2++`.  
   - Whenever `sum2 == target`, update `bestBefore = min(bestBefore, r2‑l2+1)`.  
   - This loop guarantees that after it finishes, `bestBefore` is the length of the shortest sub‑array wholly inside `[0, left‑1]`.  

4. **Combine when the main window hits the target**  
   - If `sum == target`, compute `length = right‑left+1`.  
   - If `bestBefore` is finite, update `ans = min(ans, bestBefore + length)`.  

5. **Return** `ans` if it changed, otherwise `-1`.  

Key invariants:  
- Main window always satisfies `sum ≤ target`.  
- Shadow window always satisfies `sum2 ≤ target` and never crosses `left`.  
- `bestBefore` is the minimal length of any target‑sum sub‑array completely before `left`.  

Edge handling: empty or single‑element arrays are covered because the loops simply never satisfy `sum == target`. The condition `r2 < left‑1` (rather than `≤`) prevents overlap; using `<=` would allow the two windows to share an index.

## Dry Run  

Input: `arr = [3,2,2,4,3]`, `target = 3`

| step | right | left | sum | r2 | l2 | sum2 | bestBefore | note |
|------|-------|------|-----|----|----|------|------------|------|
| 1 | 0 | 0 | 3 | 0 | 0 | 3 | 1 | main window hits target, shadow catches index 0 |
| 2 | 1 | 1 | 2 | 0 | 0 | 3 | 1 | shrink main (`sum<target`), shadow already up to `left‑1` |
| 3 | 2 | 2 | 2 | 1 | 1 | 2 | 1 | no new target in shadow |
| 4 | 3 | 3 | 4 → shrink → sum=0, left=4 | 2 | 2 | 2 | 1 | main window empty after shrink |
| 5 | 4 | 4 | 3 | 3 | 3 | 4 → shrink → sum2=0, l2=4 | 1 | main hits target, combine with `bestBefore=1` → ans=2 |

Final state: `ans = 2`, which is the minimum possible sum of lengths (`[3]` at index 0 and `[3]` at index 4).

## Complexity  
- **Time:** `O(n)` – each pointer (`right`, `left`, `r2`, `l2`) moves forward at most `n` steps; the inner `while` loops together perform a linear number of operations.  
- **Space:** `O(1)` – only a constant number of integer/long variables are used, independent of `n` (output array not counted).

## Solution (Java)

```java
class Solution {
    public int minSumOfLengths(int[] arr, int target) {
        int n = arr.length;

        // main window: finds windows summing to target, scanning forward
        int left = 0;
        long sum = 0;
        int ans = Integer.MAX_VALUE;

        // shadow window: independently scans [0, left-1], tracking the
        // best (shortest) valid subarray length found so far in that range
        int l2 = 0, r2 = -1;
        long sum2 = 0;
        int bestBefore = Integer.MAX_VALUE;

        for (int right = 0; right < n; right++) {
            sum += arr[right];
            while (sum > target) {
                sum -= arr[left];
                left++;
            }

            // catch the shadow window up to cover indices [0, left-1]
            while (r2 < left - 1) {
                r2++;
                sum2 += arr[r2];
                while (sum2 > target) {
                    sum2 -= arr[l2];
                    l2++;
                }
                if (sum2 == target) {
                    bestBefore = Math.min(bestBefore, r2 - l2 + 1);
                }
            }

            if (sum == target) {
                int length = right - left + 1;
                if (bestBefore != Integer.MAX_VALUE) {
                    ans = Math.min(ans, bestBefore + length);
                }
            }
        }

        return ans == Integer.MAX_VALUE ? -1 : ans;
    }
}
```

---

**Runtime** 10 ms (beats 51.1%) · **Memory** 88.4 MB (beats 92.8%)

<sub>Synced by AILeetHub on 2026-09-17.</sub>
