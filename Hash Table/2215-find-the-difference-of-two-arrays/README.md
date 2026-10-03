# 2215. Find the Difference of Two Arrays

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/find-the-difference-of-two-arrays/)

`Array` · `Hash Table`

## Intuition  
The core observation is that we only need to know **whether a value appears at least once** in each array, not how many times. By collapsing each array into a set of its distinct elements, the problem reduces to two simple membership checks: a value belongs to the answer for `nums1` iff it is in `set1` but not in `set2`, and symmetrically for `nums2`. A naïve double‑loop would cost O(m·n) time, while sorting would add O(n log n). Using hash sets gives O(1) average look‑ups and eliminates the extra pass or sorting step. This is the classic *hash‑table* (set) pattern for deduplication and constant‑time membership testing.

## Approach  
1. **Build distinct collections** – iterate over `nums1`, inserting each `num` into `set1`; then iterate over `nums2`, inserting each `num` into `set2`.  
   *Invariant*: after each insertion, the set contains exactly the unique values seen so far from the corresponding array.  
2. **Collect exclusive elements from `nums1`** – traverse `set1`; for each `num`, if `!set2.contains(num)` then `ans1.add(num)`.  
   *Exit condition*: the loop finishes when every element of `set1` has been examined. The invariant is that `ans1` holds all values from `set1` that are not in `set2` up to the current iteration.  
3. **Collect exclusive elements from `nums2`** – similarly traverse `set2`; if `!set1.contains(num)` then `ans2.add(num)`.  
   *Invariant*: `ans2` accumulates values unique to `set2`.  
4. **Return the pair** – wrap `ans1` and `ans2` with `Arrays.asList` to produce the required `List<List<Integer>>`.  

Edge handling: empty arrays are impossible by constraints, but the loops naturally produce empty answer lists when one set is a subset of the other. The use of `HashSet` guarantees no duplicates in the result, regardless of repeated values in the input arrays.

## Dry Run  

**Input**: `nums1 = [1,2,3,3]`, `nums2 = [1,1,2,2]`

| Step | set1                | set2                | ans1          | ans2          | Note                              |
|------|---------------------|---------------------|---------------|---------------|-----------------------------------|
| 0    | {}                  | {}                  | []            | []            | start                             |
| 1    | {1,2,3} (after loop) | {}                  | []            | []            | built from `nums1`                |
| 2    | {1,2,3}             | {1,2} (after loop)  | []            | []            | built from `nums2`                |
| 3    | {1,2,3}             | {1,2}               | []            | []            | begin first collection loop       |
| 4    | {1,2,3}             | {1,2}               | []            | []            | `num=1` → in `set2`, skip         |
| 5    | {1,2,3}             | {1,2}               | []            | []            | `num=2` → in `set2`, skip         |
| 6    | {1,2,3}             | {1,2}               | [3]           | []            | `num=3` not in `set2`, add to `ans1` |
| 7    | {1,2,3}             | {1,2}               | [3]           | []            | first loop ends                   |
| 8    | {1,2,3}             | {1,2}               | [3]           | []            | begin second collection loop      |
| 9    | {1,2,3}             | {1,2}               | [3]           | []            | `num=1` in `set1`, skip           |
|10    | {1,2,3}             | {1,2}               | [3]           | []            | `num=2` in `set1`, skip           |
|11    | {1,2,3}             | {1,2}               | [3]           | []            | second loop ends                  |

Final state: `ans1 = [3]`, `ans2 = []`, which matches the required output because `3` appears only in `nums1` and all values of `nums2` appear in `nums1`.

## Complexity  
- **Time:** O(m + n) – each array is scanned once to build its set (`m` and `n` are the lengths), and each set is iterated once for the membership checks.  
- **Space:** O(m + n) – the two hash sets store at most the distinct elements of each input; the output lists add only the exclusive elements, which are subsets of these sets. (The returned list wrapper is O(1) extra.)

## Solution (Java)

```java
class Solution {
    public List<List<Integer>> findDifference(int[] nums1, int[] nums2) {
        Set<Integer> set1 = new HashSet<>();
        Set<Integer> set2 = new HashSet<>();

        for (int num : nums1) {
            set1.add(num);
        }

        for (int num : nums2) {
            set2.add(num);
        }

        List<Integer> ans1 = new ArrayList<>();
        List<Integer> ans2 = new ArrayList<>();

        for (int num : set1) {
            if (!set2.contains(num)) {
                ans1.add(num);
            }
        }

        for (int num : set2) {
            if (!set1.contains(num)) {
                ans2.add(num);
            }
        }

        return Arrays.asList(ans1, ans2);
    }
}
```

---

**Runtime** 10 ms (beats 92.1%) · **Memory** 47.7 MB (beats 21.8%)

<sub>Synced by AILeetHub on 2026-10-03.</sub>
