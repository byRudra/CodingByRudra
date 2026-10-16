# 2916. Subarrays Distinct Element Sum of Squares II

![Hard](https://img.shields.io/badge/Difficulty-Hard-ff375f?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/subarrays-distinct-element-sum-of-squares-ii/)

`Array` · `Hash Table` · `Binary Indexed Tree` · `Segment Tree`

## Intuition  
When we extend the array one element at a time, the distinct count of every subarray that ends at the new position either stays the same or grows by 1. Specifically, if the value `nums[r]` last appeared at index `prev`, then **all subarrays whose left bound `L` satisfies `prev < L ≤ r` gain a new distinct element**, because the previous occurrence is now outside the window. This observation lets us avoid recomputing distinct counts from scratch for each subarray. By treating the left bounds as a range and adding +1 to that whole range, we can maintain the sum of distinct counts and the sum of their squares with a data structure that supports range‑add and range‑query – a lazy segment tree fits perfectly.

The naïve double‑loop solution costs O(n²) time and O(n) extra space for a frequency map. The insight above removes the inner scan entirely; we only need a logarithmic‑time update per new element.

The pattern used is **range‑add with lazy propagation on a segment tree**.

## Approach  
1. **Initialisation** – Build three arrays `sum`, `sq`, and `lazy` of size `4·n`.  
   *`sum[node]`* stores the total distinct count contributed by all left endpoints covered by `node`.  
   *`sq[node]`* stores the sum of squares of those distinct counts.  
   *`lazy[node]`* holds a pending increment `d` that has to be applied to the whole segment.  

2. **Iterate `r` from 0 to n‑1** (right end of subarrays).  
   * Retrieve the previous occurrence `prev` of `nums[r]` from the map `last` (‑1 if none).  
   * Call `update(1, 0, n‑1, prev+1, r, 1)` – this adds +1 to every left bound `L` in `[prev+1, r]`.  

3. **`update(node, l, r, ql, qr, d)`** – standard lazy segment‑tree range‑add.  
   * **Exit condition**: the query interval `[ql, qr]` is disjoint from `[l, r]`.  
   * **Full cover** (`ql ≤ l && r ≤ qr`): invoke `apply(node, len, d)`.  
   * **Partial cover**: push any pending lazy value to children (`push`), recurse on both halves, then recompute `sum[node]` and `sq[node]` as the modular sum of the children.  

4. **`apply(node, len, d)`** – updates the stored aggregates for a whole segment of length `len`.  
   * New square sum: `sq += 2·d·sum + d²·len` (derived from \((x+d)² = x² + 2dx + d²\)).  
   * New linear sum: `sum += d·len`.  
   * Accumulate the lazy tag: `lazy += d`. All operations are performed modulo `MOD`.  

5. **Accumulate answer** – after the update, `sq[1]` equals the sum of squares of distinct counts for **all** subarrays that end at `r`. Add it to `ans`.  

6. **Record the current position** – `last.put(nums[r], r)` so future iterations know the latest occurrence.

## Dry Run  

**Input:** `nums = [1, 2, 1]`  

| r | prev | update range `[prev+1, r]` | sum values (per L) | sq[1] after update | change description |
|---|------|----------------------------|--------------------|--------------------|--------------------|
| 0 | -1   | [0,0] (+1)                 | L=0:1              | 1² = 1             | first element creates distinct count 1 |
| 1 | -1   | [0,1] (+1)                 | L=0:2, L=1:1       | 2²+1² = 5          | new value adds 1 to both left bounds |
| 2 | 0    | [1,2] (+1)                 | L=0:2, L=1:2, L=2:1| 2²+2²+1² = 9       | repeat of `1` only left bounds >0 gain a new distinct |

`ans = 1 + 5 + 9 = 15`, which matches the expected sum of squares.

## Complexity  
- **Time:** O(n log n) – each of the `n` iterations performs a single range‑add on a segment tree, which traverses at most `2·log n` nodes.  
- **Space:** O(n) – three auxiliary arrays of size `4·n` plus the hash map `last`; the output itself is not counted.

## Solution (Java)

```java
// import java.util.*;

// class Solution {
//     public int sumCounts(int[] nums) {
//         int MOD = 1_000_000_007;
//         int n = nums.length;
//         long ans = 0;

//         for (int i = 0; i < n; i++) {
//             Map<Integer, Integer> freq = new HashMap<>();
//             int distinct = 0;

//             for (int j = i; j < n; j++) {
//                 if (freq.getOrDefault(nums[j], 0) == 0) {
//                     distinct++;
//                 }

//                 freq.put(nums[j], freq.getOrDefault(nums[j], 0) + 1);

//                 ans = (ans + (long) distinct * distinct) % MOD;
//             }
//         }

//         return (int) ans;
//     }
// }

// BKB LEETCODE KI
import java.util.*;

class Solution {
    static final long MOD = 1_000_000_007L;
    long[] sum, sq, lazy;
    int n;

    public int sumCounts(int[] nums) {
        n = nums.length;
        sum = new long[4 * n];
        sq = new long[4 * n];
        lazy = new long[4 * n];

        Map<Integer, Integer> last = new HashMap<>();
        long ans = 0;

        for (int r = 0; r < n; r++) {
            int prev = last.getOrDefault(nums[r], -1);
            update(1, 0, n - 1, prev + 1, r, 1);
            ans = (ans + sq[1]) % MOD;
            last.put(nums[r], r);
        }
        return (int) ans;
    }

    void apply(int node, int len, long d) {
        sq[node] = (sq[node] + 2 * d % MOD * sum[node] + d * d % MOD * len) % MOD;
        sum[node] = (sum[node] + d * len) % MOD;
        lazy[node] = (lazy[node] + d) % MOD;
    }

    void push(int node, int l, int r) {
        if (lazy[node] != 0) {
            int mid = (l + r) / 2;
            apply(2 * node, mid - l + 1, lazy[node]);
            apply(2 * node + 1, r - mid, lazy[node]);
            lazy[node] = 0;
        }
    }

    void update(int node, int l, int r, int ql, int qr, long d) {
        if (qr < l || r < ql) return;
        if (ql <= l && r <= qr) {
            apply(node, r - l + 1, d);
            return;
        }
        push(node, l, r);
        int mid = (l + r) / 2;
        update(2 * node, l, mid, ql, qr, d);
        update(2 * node + 1, mid + 1, r, ql, qr, d);
        sum[node] = (sum[2 * node] + sum[2 * node + 1]) % MOD;
        sq[node] = (sq[2 * node] + sq[2 * node + 1]) % MOD;
    }
}
```

---

**Runtime** 218 ms (beats 50.0%) · **Memory** 125.6 MB (beats 39.3%)

<sub>Synced by AILeetHub on 2026-10-16.</sub>
