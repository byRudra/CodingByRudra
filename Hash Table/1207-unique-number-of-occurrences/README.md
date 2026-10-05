# 1207. Unique Number of Occurrences

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/unique-number-of-occurrences/)

`Array` · `Hash Table`

## Intuition  
The key observation is that the problem reduces to two independent uniqueness checks: first, each value’s occurrence count must be known, and second, those counts themselves must be distinct. A naïve solution would compare every pair of frequencies, costing O(n²), or sort the array and then the frequencies, costing O(n log n). By maintaining a hash map of value → frequency we obtain all counts in a single pass, and a hash set lets us detect a repeated frequency instantly, eliminating the extra pass or sort.

## Approach  
1. **Build frequency map** – Iterate over `arr`. For each `num`, execute `map.put(num, map.getOrDefault(num, 0) + 1)`.  
   *Exit condition*: loop ends after the last element; invariant: after processing the first *k* elements, `map` stores the exact occurrence count of each distinct value seen so far.  
2. **Validate uniqueness of frequencies** – Create an empty `HashSet<Integer> set`. Iterate over `map.values()`. For each `freq`, attempt `set.add(freq)`.  
   *Exit condition*: loop finishes when all frequencies have been examined; invariant: `set` always contains the distinct frequencies encountered up to the current iteration.  
   *Decision point*: `set.add` returns `false` precisely when a frequency is already present, so the code returns `false` immediately—this avoids a later separate scan.  
3. **Return result** – If the second loop completes without a duplicate, return `true`.  
   Edge cases such as an empty array (not possible per constraints) or a single‑element array are handled automatically: the map will contain one entry, the set will accept its single frequency, and the method returns `true`.

## Dry Run  
Input: `arr = [1,2,2,1,1,3]`

| Iter | `num` | `map` after update                     | `freq` (from map) | `set` after add | Note                              |
|------|-------|----------------------------------------|-------------------|-----------------|-----------------------------------|
| 1    | 1     | {1→1}                                  | –                 | –               | first occurrence of 1            |
| 2    | 2     | {1→1, 2→1}                             | –                 | –               | first occurrence of 2            |
| 3    | 2     | {1→1, 2→2}                             | –                 | –               | increment count of 2              |
| 4    | 1     | {1→2, 2→2}                             | –                 | –               | increment count of 1              |
| 5    | 1     | {1→3, 2→2}                             | –                 | –               | increment count of 1 again        |
| 6    | 3     | {1→3, 2→2, 3→1}                        | –                 | –               | first occurrence of 3            |
| –    | –     | –                                      | 3,2,1 (any order) | {3,2,1}         | each `set.add` succeeds           |
| –    | –     | –                                      | –                 | –               | loop ends → return **true**       |

All three frequencies (3, 2, 1) are distinct, so the final answer is `true`.

## Complexity  
- **Time:** O(n) – the first loop runs `n` times to fill `map`, and the second loop runs `|map| ≤ n` times to fill `set`.  
- **Space:** O(n) – `map` stores at most one entry per distinct array value and `set` stores at most one entry per distinct frequency; both are bounded by the input size. (The output boolean is excluded from the space count.)

## Solution (Java)

```java
class Solution {
    public boolean uniqueOccurrences(int[] arr) {
        HashMap<Integer, Integer> map = new HashMap<>();
        for(int num : arr){
            map.put(num, map.getOrDefault(num, 0) + 1);
        }
        HashSet<Integer> set = new HashSet<>();
        for (int freq : map.values()) {
            if (!set.add(freq)) {
                return false;
            }
        }
        return true;
    }
}
```

---

**Runtime** 2 ms (beats 97.9%) · **Memory** 43.6 MB (beats 52.4%)

<sub>Synced by AILeetHub on 2026-10-05.</sub>
