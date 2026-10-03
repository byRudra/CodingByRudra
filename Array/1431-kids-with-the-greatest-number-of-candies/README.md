# 1431. Kids With the Greatest Number of Candies

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/kids-with-the-greatest-number-of-candies/)

`Array`

## Intuition  
The only thing that matters for each kid is whether their candy count after receiving all extra candies can reach or exceed the **current global maximum**. Once we know that maximum, the decision for every kid becomes a single comparison, eliminating any need for sorting, extra passes, or auxiliary data structures. The naive idea would be to simulate giving the extra candies to each kid and then recompute the maximum each time, which would be O(n²). By observing that the maximum before any distribution is a fixed threshold, we can answer all queries in one additional linear scan. This is a classic **two‑pass, constant‑extra‑space** pattern.

## Approach  
1. **Initialize** an empty `List<Boolean> result` and a variable `max = 0`.  
2. **First pass – find the current maximum**:  
   - Loop `for (int candy : candies)`.  
   - Invariant: after processing the first *k* elements, `max` equals the greatest value among those *k* candies.  
   - Update with `max = Math.max(max, candy)`.  
   - The loop exits when every element has been examined, guaranteeing `max` is the overall maximum.  
3. **Second pass – evaluate each kid**:  
   - Loop again `for (int candy : candies)`.  
   - Invariant: at the start of each iteration, `candy` holds the current kid’s original count, and `max` remains the global maximum from step 2.  
   - Compute the condition `candy + extraCandies >= max`.  
   - Append the boolean result with `result.add(...)`.  
   - The loop terminates after the last element, having filled `result` with one entry per kid.  
4. **Return** the populated `result` list.

Edge handling: the code works for the minimum length `n = 2` because the first pass still produces a valid `max`. No special case for odd/even lengths is needed; the comparison is independent of array size. The `>=` operator is intentional so that a kid already at the maximum also receives `true` without extra candies.

## Dry Run  

**Input**: `candies = [2, 3, 5, 1]`, `extraCandies = 3`

| Iteration | Phase | `candy` | `max` (after first pass) | `candy + extraCandies` | Condition (`>= max`) | `result` after add |
|-----------|-------|---------|--------------------------|------------------------|----------------------|--------------------|
| 1         | Find max | 2 | 2 | – | – | – |
| 2         | Find max | 3 | 3 | – | – | – |
| 3         | Find max | 5 | 5 | – | – | – |
| 4         | Find max | 1 | 5 | – | – | – |
| 1         | Check   | 2 | 5 | 5 | true | `[true]` |
| 2         | Check   | 3 | 5 | 6 | true | `[true, true]` |
| 3         | Check   | 5 | 5 | 8 | true | `[true, true, true]` |
| 4         | Check   | 1 | 5 | 4 | false| `[true, true, true, false]` |

After the second pass, `result` equals `[true, true, true, false]`, which correctly marks every kid that can reach the greatest candy count.

## Complexity  
- **Time:** **O(n)** – the first loop scans `candies` once to compute `max`, and the second loop scans it again to build `result`; each runs `n` iterations.  
- **Space:** **O(1)** extra – only a few scalar variables (`max`, loop temporaries) are used; the output list `result` is required by the problem and not counted toward auxiliary space.

## Solution (Java)

```java
class Solution {
    public List<Boolean> kidsWithCandies(int[] candies, int extraCandies) {
        List<Boolean> result = new ArrayList<>();

        int max = 0;

        // Find the current maximum
        for (int candy : candies) {
            max = Math.max(max, candy);
        }

        // Check each kid
        for (int candy : candies) {
            result.add(candy + extraCandies >= max);
        }

        return result;
    }
}
```

---

**Runtime** 1 ms (beats 95.5%) · **Memory** 44 MB (beats 24.8%)

<sub>Synced by AILeetHub on 2026-10-03.</sub>
