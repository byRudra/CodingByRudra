# 922. Sort Array By Parity II

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/sort-array-by-parity-ii/)

`Array` · `Two Pointers` · `Sorting`

## Intuition  
The key observation is that the required final layout forces every even number to occupy an even index and every odd number to occupy an odd index. Because the input guarantees exactly half even and half odd values, we can fill the result array by walking through the original numbers once and placing each value at the next free slot of the appropriate parity. This eliminates the need for a second pass, a hash map, or any sorting; we simply maintain two pointers that jump by two positions. The pattern used is the classic **two‑pointer** placement technique.

## Approach  
1. **Initialize placement pointers** – `even = 0` points to the next free even index, `odd = 1` points to the next free odd index.  
2. **Iterate over `nums`** with a for‑each loop (`for (int num : nums)`).  
   - **Loop invariant:** before each iteration, all numbers processed so far have been placed correctly; `even` is the smallest even index not yet filled, `odd` is the smallest odd index not yet filled.  
3. **Parity test** – `if (num % 2 == 0)` decides which pointer to use.  
   - **Even case:** assign `answer[even] = num` and advance `even += 2`. The `+=2` keeps the pointer on even positions only.  
   - **Odd case:** assign `answer[odd] = num` and advance `odd += 2`.  
4. **Termination** – the loop ends after the last element of `nums` is processed. At that point `even` will be equal to `nums.length` (or `nums.length‑1` for odd length, which never occurs) and `odd` will be out of bounds as well, guaranteeing the whole array is filled.  
5. **Return** the filled `answer` array.

**Edge‑case handling:**  
- Empty or single‑element arrays cannot appear because the constraints require an even length ≥ 2.  
- The code treats even and odd indices uniformly; there is no special case for the last element because the parity counts match the index counts.  
- Using `+= 2` instead of a conditional `if (even < nums.length)` is safe because the loop processes exactly half even numbers, so `even` never exceeds the array bound.  

## Dry Run  
Input: `nums = [4, 2, 5, 7]`

| Iter | `num` | `even` before | `odd` before | `answer` snapshot                | Change                              |
|------|-------|---------------|--------------|----------------------------------|-------------------------------------|
| 1    | 4     | 0             | 1            | `[_, _, _, _]`                   | placed 4 at index 0 → `even = 2`    |
| 2    | 2     | 2             | 1            | `[4, _, _, _]`                   | placed 2 at index 2 → `even = 4`    |
| 3    | 5     | 4             | 1            | `[4, _, 2, _]`                   | placed 5 at index 1 → `odd = 3`     |
| 4    | 7     | 4             | 3            | `[4, 5, 2, _]`                   | placed 7 at index 3 → `odd = 5`     |

After the fourth iteration the array is `[4,5,2,7]`, which satisfies the parity‑index condition, so the algorithm returns it.

## Complexity  
- **Time:** O(n) – the single pass processes each of the `n` elements once, and each pointer moves at most `n/2` steps.  
- **Space:** O(n) – an auxiliary array `answer` of size `n` is allocated; the algorithm uses only O(1) extra variables beyond that.

## Solution (Java)

```java
class Solution {
    public int[] sortArrayByParityII(int[] nums) {
        int even = 0;
        int odd = 1;
        int answer[] = new int[nums.length];
        for (int num : nums) {
            if (num % 2 == 0) {
                answer[even] = num;
                even += 2;
            } else {
                answer[odd] = num;
                odd += 2;
            }
        }

        return answer;

    }
}
```

---

**Runtime** 2 ms (beats 99.8%) · **Memory** 48.4 MB (beats 76.6%)

<sub>Synced by AILeetHub on 2026-10-15.</sub>
