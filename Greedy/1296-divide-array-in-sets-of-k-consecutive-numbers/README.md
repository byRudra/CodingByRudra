# 1296. Divide Array in Sets of K Consecutive Numbers

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/divide-array-in-sets-of-k-consecutive-numbers/)

`Array` · `Hash Table` · `Greedy` · `Sorting`

## Intuition  
The key observation is that any valid grouping must begin with the smallest number that has not yet been placed, because a consecutive block cannot skip a lower value that is still available. Therefore, if we repeatedly take the current minimum element as the start of a length‑`k` sequence and consume one copy of each number in that range, we either succeed for the whole array or discover a missing element that makes the division impossible. A naïve solution might try all permutations or use a hash‑set with repeated scans, which would be O(n·k) or worse. By maintaining the numbers in sorted order and their frequencies, we can enforce the “start with the smallest” rule in a single greedy pass. This pattern is a **greedy algorithm with an ordered map**.

## Approach  
1. **Validate total length.**  
   - If `nums.length % k != 0`, return `false` immediately because groups of size `k` cannot cover the array.  
2. **Build frequency map.**  
   - Iterate over `nums`, inserting each `num` into a `TreeMap<Integer,Integer>` as `map.put(num, map.getOrDefault(num,0)+1)`.  
   - The `TreeMap` keeps keys in ascending order, enabling O(log n) access to the smallest remaining value.  
3. **Process groups while map is non‑empty.**  
   - Loop condition: `while (!map.isEmpty())`.  
   - **Invariant:** at the start of each outer iteration, all numbers smaller than `map.firstKey()` have been completely used.  
   - Set `start = map.firstKey()`. This is the smallest unused number.  
4. **Consume a consecutive block of length k.**  
   - For `i` from `start` to `start + k - 1` (inclusive):  
     - If `!map.containsKey(i)`, the required consecutive number is missing → return `false`.  
     - Retrieve its count `current = map.get(i)`.  
     - If `current == 1`, remove the entry with `map.remove(i)`; otherwise, decrement the count with `map.put(i, current - 1)`.  
   - **Loop exit condition:** the `for` loop finishes after exactly `k` iterations, having verified and consumed one copy of each needed value.  
5. **All groups formed.**  
   - When the outer `while` finishes, every element has been placed in a valid block, so return `true`.  

Edge considerations: an empty array passes the length check (`0 % k == 0`) and the map stays empty, returning `true` (trivially divisible). Single‑element arrays work only when `k == 1`. The `<=` check is not used; the `for` loop’s upper bound is `start + k` exclusive, matching the required size exactly.

## Dry Run  
Input: `nums = [1,2,2,3,3,4]`, `k = 3`

| Iteration | start | map before iteration (key→count) | i (inner) | action | map after iteration | note |
|-----------|-------|----------------------------------|----------|--------|---------------------|------|
| 1 | 1 | {1→1, 2→2, 3→2, 4→1} | 1 | count 1→0 → remove | {2→2, 3→2, 4→1} | removed 1 |
|   |   |                                  | 2 | count 2→1 | {2→1, 3→2, 4→1} | decremented 2 |
|   |   |                                  | 3 | count 3→1 | {2→1, 3→1, 4→1} | decremented 3 |
| 2 | 2 | {2→1, 3→1, 4→1} | 2 | count 2→0 → remove | {3→1, 4→1} | removed 2 |
|   |   |                                  | 3 | count 3→0 → remove | {4→1} | removed 3 |
|   |   |                                  | 4 | count 4→0 → remove | {} | removed 4, map empty |

The map becomes empty after two groups, so the algorithm returns `true`, which matches the valid partition `[1,2,3]` and `[2,3,4]`.

## Complexity  
- **Time:** **O(n log n)** – inserting each of the `n` numbers into the `TreeMap` costs `log n`, and each removal or decrement also costs `log n`; the inner `for` loop runs `n/k` times, each performing `k` map operations, yielding the same asymptotic bound.  
- **Space:** **O(n)** – the `TreeMap` stores at most one entry per distinct number, which in the worst case is `n` entries. (Output is a single boolean, not counted.)

## Solution (Java)

```java
class Solution {
    public boolean isPossibleDivide(int[] nums, int k) {
        if(nums.length % k != 0) return false;
        
        TreeMap<Integer, Integer> map = new TreeMap<>();
        // Counting each element
        for(int num : nums) map.put(num, map.getOrDefault(num, 0) + 1);

        while(!map.isEmpty()){
            int start = map.firstKey();
            for(int i = start; i < start + k; i++){
                if(!map.containsKey(i)) return false;
                int current = map.get(i);
                if(current == 1) map.remove(i);
                else map.put(i, current - 1);
            }
        }
        
        return true;
    }
}
```

---

**Runtime** 404 ms (beats 37.7%) · **Memory** 114.6 MB (beats 52.5%)

<sub>Synced by AILeetHub on 2026-10-27.</sub>
