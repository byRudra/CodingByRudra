# 260. Single Number III

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/single-number-iii/)

`Array` · `Bit Manipulation`

## Intuition  
If we line the numbers up in sorted order, every value that appears twice will sit side‑by‑side. Consequently, a unique element is precisely the one whose immediate neighbours are different (or absent at the array ends). The naïve way—using a hash map to count frequencies—needs O(n) extra space; sorting eliminates that need by turning the “appear twice” condition into a simple adjacency check, at the cost of an O(n log n) pass.

## Approach  
1. **Sort the array** – `Arrays.sort(nums)`. After this step the invariant holds: any duplicate values occupy two consecutive indices.  
2. **Iterate with index `i` from 0 to `nums.length‑1`**. The loop exits when `i` reaches the length, guaranteeing every element is examined exactly once.  
3. **Compute `leftSame`**: `i > 0 && nums[i] == nums[i‑1]`. This is true iff the current element matches its left neighbour.  
4. **Compute `rightSame`**: `i < nums.length‑1 && nums[i] == nums[i+1]`. This is true iff the current element matches its right neighbour.  
5. **If both flags are false**, the current value has no equal neighbour on either side, so it is one of the two singletons. Store it in `result[index++]`. The code deliberately uses `<=`‑style bounds in the flag checks to avoid out‑of‑range access at the array ends.  
6. **Return `result`** after the scan. The array `result` is pre‑allocated with length 2, matching the problem guarantee that exactly two uniques exist.

Edge handling:  
- Empty or single‑element inputs never occur because the constraints require at least two elements.  
- For an even‑length array where the two uniques are adjacent, both `leftSame` and `rightSame` are false for each, so both are captured correctly.  
- The algorithm chooses the “first‑come” convention for filling `result`; the order of the two numbers is irrelevant per the problem statement.

## Dry Run  

**Input:** `[-1, 0, 0, 2, 2, 5]` (after sorting it is `[-1,0,0,2,2,5]`)

| i | nums[i] | leftSame | rightSame | index (after) | Action |
|---|---------|----------|-----------|---------------|--------|
| 0 | -1      | false    | false     | 1 → result[0] = -1 | add -1 |
| 1 | 0       | false    | true      | 1             | skip |
| 2 | 0       | true     | false     | 1             | skip |
| 3 | 2       | false    | true      | 1             | skip |
| 4 | 2       | true     | false     | 1             | skip |
| 5 | 5       | false    | false     | 2 → result[1] = 5 | add 5 |

Final `result = [-1, 5]`, which are exactly the two numbers that appear once.

## Complexity  
- **Time:** O(n log n) – the dominant cost is the call to `Arrays.sort`, which runs in n log n time; the subsequent linear scan adds only O(n).  
- **Space:** O(1) – apart from the input array (sorted in place) and the fixed‑size `result` array of length 2, the algorithm uses only a few primitive variables.

## Solution (Java)

```java
class Solution {
    public int[] singleNumber(int[] nums) {
        int result[] = new int[2];
        int index = 0;
        Arrays.sort(nums);
        for (int i = 0; i < nums.length; i++) {
            boolean leftSame = i > 0 && nums[i] == nums[i - 1];
            boolean rightSame = i < nums.length - 1
                    && nums[i] == nums[i + 1];

            if (!leftSame && !rightSame) {
                result[index++] = nums[i];
            }
        }
        return result;
    }
}
```

---

**Runtime** 10 ms (beats 10.4%) · **Memory** 48.4 MB (beats 9.0%)

<sub>Synced by AILeetHub on 2026-10-13.</sub>
