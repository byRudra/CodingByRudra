# 881. Boats to Save People

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/boats-to-save-people/)

`Array` · `Two Pointers` · `Greedy` · `Sorting` · `Timsort`

## Intuition  
If the people are sorted, the heaviest remaining person can only share a boat with the lightest remaining person; any heavier partner would exceed the limit. Therefore, after each step we either pair the lightest with the heaviest (when their sum ≤ limit) or send the heaviest alone. This eliminates the need for a second pass, a hash map, or exhaustive pairing. The solution follows the classic **two‑pointer** pattern on a sorted array.

## Approach  
1. **Sort** the array `people`.  
2. Initialise two indices: `i = 0` (pointing to the lightest) and `j = people.length‑1` (pointing to the heaviest). Set `boats = 0`.  
3. **Loop** while `i <= j` – the condition guarantees that every person is considered exactly once.  
   - **Invariant:** All persons with indices `< i` and `> j` have already been assigned to boats.  
   - If `people[i] + people[j] <= limit`, the lightest can join the heaviest; increment `i` to remove the lightest from further consideration.  
   - Decrement `j` unconditionally because the heaviest always departs in the current boat, either alone or with the lightest.  
   - Increment `boats` to count the just‑launched boat.  
4. Return `boats`.  

**Edge handling:**  
- An empty input never reaches the loop because `i = 0` and `j = -1`.  
- A single person satisfies `i == j`; the loop runs once, sending that person alone.  
- The `i <= j` guard correctly processes both even and odd lengths without off‑by‑one errors.  
- The `<=` comparison (instead of `<`) is required because a pair whose sum equals the limit is still valid.

## Dry Run  

Input: `people = [3,2,2,1]`, `limit = 3`  

| Step | i | j | people[i] | people[j] | boats | Note |
|------|---|---|-----------|-----------|-------|------|
| 0 (init) | 0 | 3 | 1 | 3 | 0 | after sorting → `[1,2,2,3]` |
| 1 | 0 | 2 | 1 | 2 | 1 | 1+3 > 3 → only heaviest (3) leaves (`j--`) |
| 2 | 0 | 1 | 1 | 2 | 2 | 1+2 ≤ 3 → pair, `i++`, `j--` |
| 3 | 1 | 0 | 2 | 2 | 3 | `i > j` stops loop |

Final state: `boats = 3`, which is the minimal number because we paired the only feasible light‑heavy combination and sent the remaining heavy persons alone.

## Complexity  
- **Time:** O(n log n) – sorting dominates; the while‑loop runs at most n/2 iterations because `j` moves left each pass.  
- **Space:** O(1) extra – only a few integer variables are used; the sorting is in‑place (ignoring the output array).

## Solution (Java)

```java
class Solution {
    public int numRescueBoats(int[] people, int limit) {
        Arrays.sort(people);
        int i = 0, j = people.length - 1, boats = 0;
        while (i <= j) {
            if (people[i] + people[j] <= limit) i++; // lightest hops on too
            j--;                                      // heaviest always leaves
            boats++;
        }
        return boats;
    }
}
```

---

**Runtime** 20 ms (beats 84.5%) · **Memory** 56 MB (beats 88.0%)

<sub>Synced by AILeetHub on 2026-11-04.</sub>
