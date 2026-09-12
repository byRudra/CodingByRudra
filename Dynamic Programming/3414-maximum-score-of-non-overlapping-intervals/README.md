# 3414. Maximum Score of Non-overlapping Intervals

![Hard](https://img.shields.io/badge/Difficulty-Hard-ff375f?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/maximum-score-of-non-overlapping-intervals/)

`Array` · `Binary Search` · `Dynamic Programming` · `Sorting`

## Intuition  
If we order intervals by their right endpoint, any interval can only conflict with intervals that end **after** its start. Therefore, for a given interval we can locate the last earlier interval whose end is strictly smaller than the current start – all intervals before that are guaranteed to be non‑overlapping. This reduces the “choose up to 4 non‑overlapping intervals” problem to a classic DP where the state is “best total weight using exactly k intervals among the first i sorted intervals”. The binary‑search step eliminates the need for an extra O(n) scan or a hash map, and the DP naturally extends to the lexicographically smallest index list by keeping a parallel answer array.

## Approach  
1. **Transform & sort** – Build `arr[i] = [end, start, weight, originalIndex]` for every interval and sort `arr` by `end` ascending.  
2. **DP tables** – Allocate `dp[i][k]` (max weight using ≤ k intervals among first i) and `ans[i][k]` (lexicographically smallest list achieving `dp[i][k]`). Initialise all `ans` entries with empty `ArrayList`.  
3. **Iterate i = 1 … n** (1‑based for DP convenience).  
   * **Extract** `start = arr[i‑1][1]`, `weight = arr[i‑1][2]`.  
   * **Binary search** on `arr[0 … i‑2]` to find `prev`, the greatest index with `arr[prev][0] < start`. Invariant: all indices ≤ prev end before the current interval, all > prev overlap. If none satisfy, `prev = -1`.  
4. **Iterate k = 1 … 4** (number of intervals we may pick).  
   * **Skip case** – inherit `dp[i‑1][k]` and copy `ans[i‑1][k]`.  
   * **Take case** – compute `take = weight + dp[prev+1][k‑1]`. Build `candidate = ans[prev+1][k‑1]` plus the current original index `arr[i‑1][3]`; sort `candidate` to keep indices in increasing order.  
   * **Choose better** – if `take` exceeds the skip value, replace `dp[i][k]` and `ans[i][k]`. If equal, invoke `isSmaller(candidate, ans[i][k])` which performs a lexicographic comparison (shorter list wins when prefixes match).  
5. **Result** – After processing all intervals, `ans[n][4]` holds the optimal list (it may contain fewer than 4 indices if fewer intervals improve the score). Convert it to an `int[]` and return.

Key edge handling:  
* Empty or single‑element input works because the DP tables are sized `n+1` and the binary search gracefully returns `prev = -1`.  
* Overlap definition (“sharing a boundary is overlapping”) is respected by the strict `< start` check.  
* Sorting `candidate` each time guarantees the stored list is always in increasing original‑index order, which is required for lexicographic comparison.

## Dry Run  

Input (original order, 0‑based indices shown):  

```
[[1,3,2], [4,5,2], [1,5,5], [6,9,3]]
```

After sorting by end → `arr` (end,start,weight,idx):  

| i | end | start | weight | idx |
|---|-----|-------|--------|-----|
|0|3|1|2|0|
|1|5|4|2|1|
|2|5|1|5|2|
|3|9|6|3|3|

DP iteration (only k=1 shown for brevity; similar for k=2‑4):

| i | k | prev | skip dp | take dp | chosen list |
|---|---|------|---------|---------|-------------|
|1|1|-1|0|2|[0]|
|2|1|0|2|2|[0] (skip wins, tie → smaller list)|
|3|1|-1|2|5|[2] (take beats skip)|
|4|1|2|5|8|[2,3] (take adds weight 3)|
|…|…|…|…|…|…|

After the full loops, `ans[4][4] = [2,3]`, which is the lexicographically smallest set achieving the maximum total weight 8.

## Complexity  
- **Time:** O(n log n) for the initial sort plus O(n · 4 · log n) for the binary searches inside the DP loops; the dominant term is O(n log n).  
- **Space:** O(n · 4) for `dp` and `ans` tables (the output list itself is not counted), i.e., O(n) auxiliary memory.

## Solution (Java)

```java
class Solution {
    public int[] maximumWeight(List<List<Integer>> intervals) {
        int n = intervals.size();

        // [end, start, weight, originalIndex]
        int[][] arr = new int[n][4];

        for (int i = 0; i < n; i++) {
            arr[i][0] = intervals.get(i).get(1); // end
            arr[i][1] = intervals.get(i).get(0); // start
            arr[i][2] = intervals.get(i).get(2); // weight
            arr[i][3] = i; // original index
        }

        // Sort by end
        Arrays.sort(arr, (a, b) -> Integer.compare(a[0], b[0]));

        long[][] dp = new long[n + 1][5];

        // Store selected original indices
        List<Integer>[][] ans = new ArrayList[n + 1][5];

        for (int i = 0; i <= n; i++) {
            for (int k = 0; k <= 4; k++) {
                ans[i][k] = new ArrayList<>();
            }
        }

        for (int i = 1; i <= n; i++) {

            int start = arr[i - 1][1];
            int weight = arr[i - 1][2];

            // Find last interval with end < current start
            int lo = 0;
            int hi = i - 2;
            int prev = -1;

            while (lo <= hi) {
                int mid = lo + (hi - lo) / 2;

                if (arr[mid][0] < start) {
                    prev = mid;
                    lo = mid + 1;
                } else {
                    hi = mid - 1;
                }
            }

            for (int k = 1; k <= 4; k++) {

                // Option 1: Skip
                dp[i][k] = dp[i - 1][k];
                ans[i][k] = new ArrayList<>(ans[i - 1][k]);

                // Option 2: Take
                long take = weight + dp[prev + 1][k - 1];

                List<Integer> candidate = new ArrayList<>(ans[prev + 1][k - 1]);

                candidate.add(arr[i - 1][3]);
                Collections.sort(candidate);

                if (take > dp[i][k] ||
                        (take == dp[i][k] &&
                                isSmaller(candidate, ans[i][k]))) {

                    dp[i][k] = take;
                    ans[i][k] = candidate;
                }
            }
        }

        return ans[n][4]
                .stream()
                .mapToInt(Integer::intValue)
                .toArray();
    }

    private boolean isSmaller(List<Integer> a, List<Integer> b) {

        for (int i = 0; i < Math.min(a.size(), b.size()); i++) {

            if (!a.get(i).equals(b.get(i))) {
                return a.get(i) < b.get(i);
            }
        }

        return a.size() < b.size();

    }
}
```

---

**Runtime** 163 ms (beats 61.1%) · **Memory** 242.9 MB (beats 14.8%)

<sub>Synced by AILeetHub on 2026-09-12.</sub>
