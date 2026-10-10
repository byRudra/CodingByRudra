# 2333. Minimum Sum of Squared Difference

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/minimum-sum-of-squared-difference/)

`Array` · `Binary Search` · `Greedy` · `Sorting` · `Heap (Priority Queue)`

## Intuition  
The only thing that matters after the arrays are read is the absolute difference at each index; any further +1/‑1 operation changes exactly one of those differences by 1. Therefore the problem reduces to “given a multiset of non‑negative integers (the differences) and a budget of `k1+k2` unit reductions, distribute the reductions so that the sum of squares of the remaining numbers is minimal.” The naïve way would sort the differences after every reduction (O(k log n)) or use a priority queue (O(k log n)), which is too slow when `k` can be 10⁹. The key observation is that we can treat the differences as a frequency histogram and greedily push mass from the current maximum bucket downwards; each unit reduction moves one count from value i to i‑1, and we never need to look at values lower than the current maximum again.

## Approach  
1. **Compute raw differences** – for each index `i` set `diff[i] = Math.abs(nums1[i] - nums2[i])`. Simultaneously accumulate `totalDiff` and track `maxDiff`.  
2. **Early exit** – if `operations = (long)k1 + k2` is at least `totalDiff`, every difference can be driven to 0, so return 0.  
3. **Build frequency array** – allocate `long[] count = new long[maxDiff + 1]` and increment `count[d]` for each `d` in `diff`. Now `count[i]` equals the number of positions whose current difference equals i.  
4. **Greedy reduction loop** – iterate `i` from `maxDiff` down to 1:  
   - *Invariant*: before processing `i`, all differences larger than `i` have already been reduced as much as possible, so `i` is the current maximum value present.  
   - If `count[i] == 0` skip.  
   - Compute `reduce = Math.min(operations, count[i])`; this is how many unit‑steps we can apply to the `i`‑bucket.  
   - Decrease `count[i]` by `reduce` and increase `count[i‑1]` by the same amount, effectively moving those elements one step closer to zero.  
   - Subtract `reduce` from `operations`.  
   - Break when `operations` reaches 0 because no further reductions are possible.  
5. **Final sum** – iterate `i` from 1 to `maxDiff`; for each bucket add `count[i] * (long)i * i` to the answer. This computes Σ (diff²) using the histogram without revisiting individual elements.  

Edge cases handled: empty or single‑element arrays (loop runs zero times), `operations` larger than total diff (step 2), and the use of `long` for `operations` and `count` prevents overflow when `k1/k2` are up to 10⁹.

## Dry Run  
**Input**  
`nums1 = [1,4,10,12]`  
`nums2 = [5,8,6,9]`  
`k1 = 1, k2 = 1`

| iteration | i (max) | operations before | reduce | count[i] → after | count[i‑1] → after | note |
|-----------|---------|-------------------|--------|------------------|-------------------|------|
| 1 | 4 | 2 | 1 | count[4]: 3→2 | count[3]: 1→2 | move one 4‑diff down |
| 2 | 4 | 1 | 1 | count[4]: 2→1 | count[3]: 2→3 | second reduction, ops become 0 |
| – | – | 0 | – | – | – | loop exits |

After the loop the histogram is `count[4]=1`, `count[3]=3`. Final sum = `1·4² + 3·3² = 16 + 27 = 43`, which matches the optimal answer.

## Complexity  
- **Time:** O(n + maxDiff) – the first pass scans the arrays (O(n)), building the histogram is O(n), and the greedy sweep touches each possible difference value at most once (≤ maxDiff ≤ 10⁵).  
- **Space:** O(maxDiff) – the `count` array stores a frequency for every possible difference value; all other structures use O(1) extra space (the output does not count).

## Solution (Java)

```java
// TLE
// class Solution {
//     public long minSumSquareDiff(int[] nums1, int[] nums2, int k1, int k2) {
//         // making a priority queue as we have to minimize the res 
//         PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());
//         long operations = (long) k1 + k2;
//         int total = 0;
//         for(int i = 0; i < nums1.length; i++){
//             int diff = Math.abs(nums1[i] - nums2[i]);
//             total += diff;
//             pq.offer(diff);
//         }
//         if(operations >= total) return 0;
//         // Minimizing
//         while(operations != 0 && !pq.isEmpty()){
//             int diff = pq.poll();
//             if (diff == 0) break;
//             pq.offer(diff - 1);
//             operations--;
//         }

//         long answer = 0;
//         while(!pq.isEmpty()){
//             long diff = pq.poll();
//             answer += diff * diff;
//         }
//         return answer;
//     }
// }

// Better Approach

class Solution {
    public long minSumSquareDiff(int[] nums1, int[] nums2, int k1, int k2) {
        int n = nums1.length;
        int []diff = new int[n];
        long totalDiff = 0;
        int maxDiff = 0;
        for(int i = 0; i < n; i++){
            diff[i] = Math.abs(nums1[i] - nums2[i]);
            totalDiff += diff[i];
            maxDiff = Math.max(maxDiff, diff[i]);
        }

        long operations = (long) k1 + k2;
        if(operations >= totalDiff) return 0;

        long[] count = new long[maxDiff + 1];
        for(int d : diff) count[d]++;

        for(int i = maxDiff; i > 0; i--){
            if(count[i] == 0) continue;
            long reduce = Math.min(operations, count[i]);
            count[i] -= reduce;
            count[i - 1] += reduce;
            operations -= reduce;

            if(operations == 0) break;
        }

        long res = 0;
        for(int i = 1; i <= maxDiff; i++){
            if(count[i] > 0) res += count[i] * (long) i * i;
        }
        return res;
    }
}
```

---

**Runtime** 6 ms (beats 100.0%) · **Memory** 109.2 MB (beats 95.0%)

<sub>Synced by AILeetHub on 2026-10-10.</sub>
