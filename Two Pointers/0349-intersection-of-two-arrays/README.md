# 349. Intersection of Two Arrays

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/intersection-of-two-arrays/)

`Array` · `Hash Table` · `Two Pointers` · `Binary Search` · `Sorting`

## Intuition  
The key observation is that membership testing in a hash table is O(1) on average, so once we store every distinct value of the first array, we can decide in a single pass whether each element of the second array belongs to the intersection. A naïve double‑loop would cost O(m·n) time, and sorting both arrays would require O(m log m + n log n) extra work. By exploiting constant‑time look‑ups we eliminate the extra pass or sorting step entirely. This solution follows the **hash‑set** pattern.

## Approach  
1. **Build a set from `nums1`.**  
   - Iterate `for (int num : nums1)` and execute `set.add(num)`.  
   - *Invariant:* after processing the first *k* elements, `set` contains exactly the distinct values among `nums1[0 … k‑1]`.  
   - Handles empty or single‑element `nums1` naturally because the loop simply adds whatever is present.

2. **Collect common elements into a second set.**  
   - Iterate `for (int num : nums2)` and test `if (set.contains(num))`.  
   - When the test succeeds, execute `result.add(num)`.  
   - *Invariant:* after processing the first *k* elements of `nums2`, `result` holds all distinct numbers that appear in both arrays among `nums2[0 … k‑1]`.  
   - Using a set for `result` automatically deduplicates duplicates from `nums2`; the check `contains` is O(1), so the loop runs exactly `nums2.length` times.

3. **Transform the result set into an array.**  
   - Allocate `int[] ans = new int[result.size()]`.  
   - Fill it with `for (int num : result) ans[i++] = num;`.  
   - No off‑by‑one errors appear because `i` starts at 0 and stops at `result.size() - 1`.  

The code deliberately chooses `HashSet` (unordered) rather than `TreeSet` because order is irrelevant and `HashSet` offers constant‑time operations. All edge cases—empty inputs, all duplicates, or completely disjoint arrays—are covered by the same flow.

## Dry Run  

**Input**  
`nums1 = [1,2,2,1]`  
`nums2 = [2,2]`

| Iteration | `set` after step 1 | `num` (nums2) | `result` after step 2 | Note |
|-----------|-------------------|---------------|----------------------|------|
| 1 (build) | {1}               | –             | –                    | add 1 |
| 2 (build) | {1,2}             | –             | –                    | add 2 |
| 3 (build) | {1,2}             | –             | –                    | 2 already present |
| 4 (build) | {1,2}             | –             | –                    | 1 already present |
| 5 (collect) | {1,2}           | 2             | {2}                  | 2 found in `set` |
| 6 (collect) | {1,2}           | 2             | {2}                  | duplicate ignored by `result` |

After the loops, `result` holds `{2}`; converting it yields `[2]`, which is the correct intersection.

## Complexity  
- **Time:** O(m + n) – the first loop runs `m = nums1.length` times, the second runs `n = nums2.length` times, and all set operations are O(1) on average.  
- **Space:** O(m + k) – `set` stores up to `m` distinct values from `nums1`, and `result` stores up to `k` common distinct values (k ≤ min(m, n)). The output array is not counted toward extra space.

## Solution (Java)

```java
class Solution {
    public int[] intersection(int[] nums1, int[] nums2) {
        HashSet<Integer> set = new HashSet<>();

        // Put all elements of nums1 into the set
        for (int num : nums1) {
            set.add(num);
        }

        // Store common elements
        HashSet<Integer> result = new HashSet<>();

        for (int num : nums2) {
            if (set.contains(num)) {
                result.add(num);
            }
        }

        // Convert HashSet to int[]
        int[] ans = new int[result.size()];
        int i = 0;

        for (int num : result) {
            ans[i++] = num;
        }

        return ans;
    }
}
```

---

**Runtime** 2 ms (beats 97.5%) · **Memory** 45.1 MB (beats 25.3%)

<sub>Synced by AILeetHub on 2026-09-12.</sub>
