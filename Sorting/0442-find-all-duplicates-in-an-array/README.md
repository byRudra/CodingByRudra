# 442. Find All Duplicates in an Array

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/find-all-duplicates-in-an-array/)

`Array` · `Hash Table` · `Sorting`

## Intuition  
The array values are guaranteed to lie in `[1, n]`, so we can use the index positions themselves as a frequency bucket. If we allocate a helper array `num` of length `n+1`, each occurrence of a value `v` can be recorded by incrementing `num[v]`. After a single linear scan, any index whose count equals 2 corresponds exactly to a duplicated element. A naïve solution might sort the input or use a hash map, both of which require `O(n log n)` time or `O(n)` extra space. By exploiting the bounded value range we replace those structures with a fixed‑size counting array, turning the problem into two simple linear passes.

## Approach  
1. **Allocate counting array** – `int[] num = new int[nums.length + 1];` creates a zero‑initialized bucket for every possible value `1…n`.  
2. **First pass – count frequencies**  
   - Loop: `for (int nu : nums)` iterates over each element `nu` in the input.  
   - Invariant: before processing the current `nu`, `num[v]` holds the exact number of times value `v` has been seen so far.  
   - Action: `num[nu]++` increments the bucket for the current value.  
   - Exit condition: the enhanced‑for loop finishes after all `n` elements have been processed.  
3. **Prepare result container** – `List<Integer> result = new ArrayList<>();` will hold the duplicated numbers.  
4. **Second pass – collect duplicates**  
   - Loop: `for (int n = 0; n < nums.length + 1; n++)` scans every possible value index.  
   - Invariant: at the start of each iteration, `num[n]` is the final frequency of value `n`.  
   - Decision point: the check `if (num[n] == 2)` adds `n` to `result` only when the count is exactly two, matching the problem’s “appears twice” requirement.  
   - Note on bounds: the loop starts at `0` even though `0` never appears in `nums`; this harmless iteration simplifies the index arithmetic and avoids an off‑by‑one guard.  
   - Exit condition: when `n` reaches `nums.length`, all possible values have been examined.  
5. **Return** – `return result;` yields the list of duplicated integers.

## Dry Run  
**Input:** `nums = [4,3,2,7,8,2,3,1]`  

| Step | `nu` (first loop) | `num[nu]` after `num[nu]++` | `n` (second loop) | `num[n]` | `result` after possible add | Note |
|------|-------------------|----------------------------|-------------------|----------|----------------------------|------|
| 1    | 4                 | num[4]=1                   | –                 | –        | []                         | count 4 |
| 2    | 3                 | num[3]=1                   | –                 | –        | []                         | count 3 |
| 3    | 2                 | num[2]=1                   | –                 | –        | []                         | count 2 |
| 4    | 7                 | num[7]=1                   | –                 | –        | []                         | count 7 |
| 5    | 8                 | num[8]=1                   | –                 | –        | []                         | count 8 |
| 6    | 2                 | num[2]=2                   | –                 | –        | []                         | second 2 |
| 7    | 3                 | num[3]=2                   | –                 | –        | []                         | second 3 |
| 8    | 1                 | num[1]=1                   | –                 | –        | []                         | count 1 |
| –    | –                 | –                          | 1                 | 1        | []                         | not duplicate |
| –    | –                 | –                          | 2                 | 2        | [2]                        | duplicate added |
| –    | –                 | –                          | 3                 | 2        | [2,3]                      | duplicate added |
| –    | –                 | –                          | 4                 | 1        | [2,3]                      | ignored |
| –    | –                 | –                          | 5‑8               | 0/1      | [2,3]                      | no further adds |

After the second pass finishes, `result` contains `[2,3]`, exactly the numbers that appear twice.

## Complexity  
- **Time:** `O(n)` – the first loop visits each of the `n` input elements once, and the second loop iterates `n+1` times over the counting array.  
- **Space:** `O(n)` – the auxiliary array `num` holds `n+1` integers; the output list is excluded from the space count.

## Solution (Java)

```java
class Solution {
    public List<Integer> findDuplicates(int[] nums) {
        int num[] = new int[nums.length + 1];
        for(int nu : nums){
            num[nu]++;
        }
        List<Integer> result = new ArrayList<>();
        for(int n = 0; n < nums.length + 1; n++){
            if(num[n] == 2) result.add(n);
        }
        return result;
    }
}
```

---

**Runtime** 4 ms (beats 98.0%) · **Memory** 61.7 MB (beats 49.3%)

<sub>Synced by AILeetHub on 2026-10-16.</sub>
