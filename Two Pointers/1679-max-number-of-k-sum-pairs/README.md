# 1679. Max Number of K-Sum Pairs

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/max-number-of-k-sum-pairs/)

`Array` · `Hash Table` · `Two Pointers` · `Sorting`

## Intuition  
When scanning the array from left to right, any element that can immediately close a previously seen “half‑pair” forms a valid operation. The key claim is: **if we keep a count of numbers that are still waiting for their complement (k − x), each new element either finishes one waiting pair or becomes a new waiting entry**. This eliminates the need for a second pass, sorting, or a hash set that stores all values. The pattern is a single‑pass hash‑map counting (often called “frequency map pairing”).

## Approach  
1. **Initialize** an empty `HashMap<Integer,Integer>` called `map` and a counter `operations = 0`.  
2. **Iterate** over each `num` in `nums` (the outer `for‑each` loop).  
   - *Invariant*: before processing the current `num`, `map` holds the exact multiset of values that have appeared earlier but have not yet been paired.  
3. **Compute** `compliment = k - num`.  
4. **Check** `map.getOrDefault(compliment, 0) > 0`.  
   - If true, a waiting complement exists:  
     a. Decrement its count with `map.put(compliment, map.get(compliment) - 1)`.  
     b. Increment `operations`.  
   - The decrement may drop the count to zero; the map entry is left with value 0, which is harmless because the `getOrDefault` guard ignores non‑positive counts.  
5. **Otherwise** (no complement waiting):  
   - Record the current `num` as a new waiting value: `map.put(num, map.getOrDefault(num, 0) + 1)`.  
   - This step ensures that later elements can pair with the current one.  
6. **After the loop**, return `operations`.  

Edge handling: an empty or single‑element array never enters the pairing branch, so `operations` stays 0. The code treats even and odd lengths uniformly because pairing is driven solely by complement availability, not by index parity. The `> 0` test (instead of `>= 0`) guarantees we only consume a genuine waiting entry, avoiding a false pair when the count is zero.

## Dry Run  

Input: `nums = [1, 2, 3, 4]`, `k = 5`

| Iteration | num | compliment | map before                | map after (action)                                 | operations |
|-----------|-----|------------|---------------------------|----------------------------------------------------|------------|
| 1         | 1   | 4          | {}                        | put(1,1) → `{1=1}`                                 | 0          |
| 2         | 2   | 3          | `{1=1}`                   | put(2,1) → `{1=1, 2=1}`                            | 0          |
| 3         | 3   | 2          | `{1=1, 2=1}`              | decrement 2 → `{1=1, 2=0}` → `operations=1`        | 1          |
| 4         | 4   | 1          | `{1=1, 2=0}` (2’s count is 0) | decrement 1 → `{1=0, 2=0}` → `operations=2`        | 2          |

After processing all elements, `operations = 2`, which is the maximum number of disjoint pairs summing to 5.

## Complexity  
- **Time:** O(n) – each element is examined once, and all map operations (`getOrDefault`, `put`) are O(1) on average.  
- **Space:** O(n) in the worst case – the map may store every distinct number when no complements are found; the output integer does not affect the bound.

## Solution (Java)

```java
class Solution {
    public int maxOperations(int[] nums, int k) {
        HashMap<Integer, Integer> map = new HashMap<>();
        int operations = 0;
        for(int num : nums){
            int compliment = k - num;
            if(map.getOrDefault(compliment, 0) > 0){
                map.put(compliment, map.get(compliment) - 1);
                operations++;
            }
            else{
                map.put(num, map.getOrDefault(num, 0)  + 1);
            }
        } 
        return operations;
    }
}
```

---

**Runtime** 40 ms (beats 14.0%) · **Memory** 70.2 MB (beats 21.4%)

<sub>Synced by AILeetHub on 2026-10-05.</sub>
