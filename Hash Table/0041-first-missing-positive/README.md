# 41. First Missing Positive

![Hard](https://img.shields.io/badge/Difficulty-Hard-ff375f?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/first-missing-positive/)

`Array` · `Hash Table`

## Intuition  
The key observation is that in an array of length *n* the first missing positive integer is guaranteed to lie in the range [1, *n* + 1]. Anything larger than *n* + 1 cannot be the answer because there are only *n* slots to hold distinct positives. By storing every value in a hash‑set we can test membership of any candidate in O(1) average time, so we can simply walk the candidates 1, 2, 3,… until we hit the first one that is absent. This eliminates the need for sorting, extra passes to count frequencies, or a second array for bookkeeping.

## Approach  
1. **Collect all numbers** – Iterate over `nums` and insert each element into `set`. After this step `set` contains every integer that appears in the input, regardless of sign or duplicates.  
2. **Initialize the candidate** – Set `index = 1`, the smallest positive we might return.  
3. **Search sequentially** – Loop `i` from `0` up to `set.size()` (inclusive).  
   - **Exit condition:** The loop stops when `i` exceeds the number of distinct elements, because at that point we have examined at most `set.size() + 1` candidates, which covers the guaranteed range [1, *n* + 1].  
   - **Invariant:** At the start of each iteration, all integers `< index` are known to be present in `set`.  
   - Inside the loop, `if (!set.contains(index)) return index;` – the first candidate not found is the answer.  
   - Otherwise increment `index` and continue.  
4. **Fallback** – If the loop finishes without returning (theoretically impossible for valid input), the method returns `0` as a sentinel.

**Edge handling:**  
- Empty or single‑element arrays are handled because the set will have size 0 or 1, and the loop still checks `index = 1`.  
- Negative numbers and zeros are ignored automatically because the membership test only looks for positive `index`.  
- The bound uses `<= set.size()` rather than `<` to allow the case where all numbers 1…*n* are present; then `index` becomes *n* + 1 and the loop still executes one extra iteration to return it.

## Dry Run  
Input: `nums = [3, 4, -1, 1]`

| i | index | set.contains(index)? | Action | Note |
|---|-------|----------------------|--------|------|
| 0 | 1     | true                 | index++ | 1 is present |
| 1 | 2     | false                | return 2 | first missing positive found |

The loop terminates after the second iteration, returning **2**, which matches the expected answer because 1 exists while 2 does not.

## Complexity  
- **Time:** O(n) – one pass builds the hash set (`n` insertions) and a second pass checks at most `set.size() + 1 ≤ n + 1` candidates, each membership test O(1) on average.  
- **Space:** O(n) – the `HashSet` stores up to `n` distinct integers; auxiliary variables use O(1) extra space.

## Solution (Java)

```java
class Solution {
    public int firstMissingPositive(int[] nums) {
        int firstMissingpositive = -1;
        int index = 1;
        HashSet<Integer> set = new HashSet<>();
        for(int num : nums){
            set.add(num);
        }
        for(int i = 0; i <= set.size(); i++){
            if(!set.contains(index))
                return index;
            index++;
        }
        return 0;
    }
}
```

---

**Runtime** 18 ms (beats 12.6%) · **Memory** 93.3 MB (beats 6.8%)

<sub>Synced by AILeetHub on 2026-09-11.</sub>
