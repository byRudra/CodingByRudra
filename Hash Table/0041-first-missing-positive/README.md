# 41. First Missing Positive

![Hard](https://img.shields.io/badge/Difficulty-Hard-ff375f?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/first-missing-positive/)

`Array` · `Hash Table`

## Intuition  
If a number `v` is positive and not larger than the array length `n`, the only place it can be “correct” is index `v‑1`. By repeatedly swapping each such element into its own slot we obtain an arrangement where, after a single linear scan, the first index that does not contain its expected value `i+1` reveals the smallest missing positive. The naïve solution would sort the array or build a hash set, both costing `O(n log n)` time or `O(n)` extra space. The key insight is that the array itself can serve as a perfect hash table for the range `[1, n]`, eliminating any additional passes or containers.

## Approach  
1. **Initialize** `n = nums.length`.  
2. **Place each element** – iterate `i` from `0` to `n‑1`.  
   - **Loop condition**: `while (nums[i] > 0 && nums[i] <= n && nums[nums[i] - 1] != nums[i])`.  
   - **Invariant**: At the start of each iteration, all positions `< i` already hold the correct values `1 … i`.  
   - **Action**: Swap `nums[i]` with the element at its target position `nums[nums[i] - 1]`. This moves a valid positive into its rightful slot and brings a new candidate into `i`. The loop repeats until `nums[i]` is either out of range, already correctly placed, or a duplicate of the target slot.  
   - **Edge handling**:  
     * Empty or single‑element arrays are naturally handled because the outer `for` runs zero or one times.  
     * Values ≤ 0 or > n are ignored by the `while` guard, preventing out‑of‑bounds access.  
     * The check `nums[nums[i] - 1] != nums[i]` avoids infinite swapping when duplicates appear.  
3. **Detect the gap** – run a second `for` loop over `i` from `0` to `n‑1`.  
   - If `nums[i] != i + 1`, return `i + 1` immediately. The invariant from step 2 guarantees that any index that fails this test is the smallest missing positive.  
4. **All positions filled** – if the loop finishes, every index `0 … n‑1` holds `i+1`, so the answer is `n + 1`.  

## Dry Run  
Input: `nums = [3, 4, -1, 1]` (`n = 4`)

| iteration | i | nums (state after iteration)                | note                                   |
|-----------|---|--------------------------------------------|----------------------------------------|
| 1         | 0 | `[3, 4, -1, 1]` → swap 3 ↔ nums[2] → `[-1, 4, 3, 1]` | 3 should go to index 2; bring -1 to i |
| 2         | 0 | `[-1, 4, 3, 1]` (while stops, -1 out of range) | no further swap                        |
| 3         | 1 | `[ -1, 4, 3, 1 ]` → swap 4 ↔ nums[3] → `[ -1, 1, 3, 4 ]` | 4 belongs at index 3                     |
| 4         | 1 | `[ -1, 1, 3, 4 ]` → swap 1 ↔ nums[0] → `[ 1, -1, 3, 4 ]` | 1 belongs at index 0                     |
| 5         | 1 | `[ 1, -1, 3, 4 ]` (while stops, -1 out of range) |                                        |
| 6         | 2 | `[ 1, -1, 3, 4 ]` (while condition false, 3 already at index 2) |                                        |
| 7         | 3 | `[ 1, -1, 3, 4 ]` (4 already at index 3)   |                                        |
| 8         | – | **Second pass** finds `nums[1] = -1 != 2` → return **2** | first missing positive                |

The algorithm finishes with `2`, which is indeed the smallest positive absent from the original array.

## Complexity  
- **Time:** `O(n)` – each element is swapped at most once into its final slot, so the total number of swaps plus the two linear scans is bounded by a constant multiple of `n`.  
- **Space:** `O(1)` – only a few integer variables (`n`, `i`, `temp`) are used; the input array is rearranged in‑place, and no auxiliary data structures are allocated.

## Solution (Java)

```java
// class Solution {
//     public int firstMissingPositive(int[] nums) {
//         int firstMissingpositive = -1;
//         int index = 1;
//         HashSet<Integer> set = new HashSet<>();
//         for(int num : nums){
//             set.add(num);
//         }
//         for(int i = 0; i <= set.size(); i++){
//             if(!set.contains(index))
//                 return index;
//             index++;
//         }
//         return 0;
//     }
// }

// O( n )

class Solution {
    public int firstMissingPositive(int[] nums) {
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            while (nums[i] > 0 && nums[i] <= n && nums[nums[i] - 1] != nums[i]) {
                int temp = nums[nums[i] - 1];
                nums[nums[i] - 1] = nums[i];
                nums[i] = temp;
            }
        }
        for (int i = 0; i < n; i++) {
            if (nums[i] != i + 1)
                return i + 1;
        }
        return n + 1;
    }
}
```

---

**Runtime** 1 ms (beats 100.0%) · **Memory** 71 MB (beats 93.3%)

<sub>Synced by AILeetHub on 2026-09-11.</sub>
