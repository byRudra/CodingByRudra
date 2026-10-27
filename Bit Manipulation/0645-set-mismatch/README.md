# 645. Set Mismatch

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/set-mismatch/)

`Array` · `Hash Table` · `Bit Manipulation` · `Sorting`

## Intuition  
The key observation is that the sum of the original set {1,…,n} is known (`n·(n+1)/2`). After the error the array’s total is larger by exactly the duplicated value and smaller by the missing value, i.e. `sum = expectedSum + duplicate – missing`. If we can locate the duplicated element in one pass, the missing element follows from a simple arithmetic rearrangement. A naïve solution would sort the array or use a hash map to count frequencies, both costing O(n log n) or extra O(n) space. By keeping a boolean “seen” flag for each possible value we detect the duplicate while accumulating the sum, eliminating the need for sorting or a full frequency map. This is a classic two‑pass, constant‑extra‑space pattern that leverages the known sum of a consecutive integer range.

## Approach  
1. **Initialize** `n = nums.length`, `duplicate = 0`, `sum = 0`, and a boolean array `seen` of size `n+1` (indices 1…n).  
2. **Iterate** over each `num` in `nums` (enhanced for‑loop).  
   - Add `num` to `sum`.  
   - If `seen[num]` is already `true`, assign `duplicate = num`.  
   - Set `seen[num] = true`.  
   *Loop invariant*: after processing the first *k* elements, `sum` equals the sum of those *k* numbers, and `seen[i]` is true exactly for the values that have appeared among the first *k* elements.  
3. **Compute** the theoretical sum of a perfect set: `expectedSum = n * (n + 1) / 2`.  
4. **Derive** the missing value using the rearranged equation: `missing = expectedSum + duplicate - sum`.  
5. **Return** a two‑element array `{ duplicate, missing }`.  

Edge handling: the boolean array is sized `n+1` so index 0 is unused, avoiding off‑by‑one errors. The code works for the smallest allowed input (`n = 2`) because the loop still processes both elements and the arithmetic remains valid. No overflow occurs for the given constraints (`n ≤ 10⁴`), so the product `n*(n+1)` fits comfortably in a 32‑bit signed int.

## Dry Run  

**Input**: `nums = [1, 2, 2, 4]`

| Iter | `num` | `sum` (after add) | `seen[num]` before | `duplicate` set? | Note |
|------|------|-------------------|--------------------|------------------|------|
| 1    | 1    | 1                 | false              | no               | mark `seen[1]=true` |
| 2    | 2    | 3                 | false              | no               | mark `seen[2]=true` |
| 3    | 2    | 5                 | true               | yes → `duplicate=2` | `seen[2]` already true |
| 4    | 4    | 9                 | false              | no               | mark `seen[4]=true` |

After the loop: `sum = 9`, `duplicate = 2`.  
`expectedSum = 4·5/2 = 10`.  
`missing = 10 + 2 - 9 = 3`.  

Final state: `{2, 3}` – the duplicated number is 2 and the missing number is 3, matching the required output.

## Complexity  
- **Time:** O(n) – the single pass over `nums` visits each element once, and the final arithmetic is O(1).  
- **Space:** O(n) – the boolean `seen` array of length `n+1` dominates auxiliary memory; the output array is not counted.

## Solution (Java)

```java
class Solution {
    public int[] findErrorNums(int[] nums) {
        int n = nums.length;
        int duplicate = 0;
        int sum = 0;
        boolean[] seen = new boolean[n + 1];

        for (int num : nums) {
            sum += num;

            if (seen[num]) {
                duplicate = num;
            }
            seen[num] = true;
        }

        int expectedSum = n * (n + 1) / 2;
        int missing = expectedSum + duplicate - sum;

        return new int[] { duplicate, missing };
    }
}
```

---

**Runtime** 2 ms (beats 95.5%) · **Memory** 46.9 MB (beats 97.4%)

<sub>Synced by AILeetHub on 2026-10-27.</sub>
