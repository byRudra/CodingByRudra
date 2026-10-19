# 1748. Sum of Unique Elements

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/sum-of-unique-elements/)

`Array` · `Hash Table` · `Counting`

## Intuition  
The key observation is that the answer depends only on how many times each value appears, not on their order. By counting occurrences once we can decide in O(1) per element whether it contributes to the sum. A naïve approach would scan the array repeatedly or store each element in a hash set and update a running total, both of which add extra passes or overhead. Using a fixed‑size frequency array (since `nums[i] ≤ 100`) gives us the needed counts in a single pass and lets us filter unique values in a second pass. This is the classic “counting sort” / frequency‑array pattern.

## Approach  
1. **Initialize frequency bucket** – `int[] freq = new int[101];` creates a zero‑filled array indexed by possible values (1 … 100).  
2. **First pass – count frequencies**  
   - Loop: `for (int num : nums) { freq[num]++; }`  
   - **Exit condition:** the enhanced‑for loop terminates after the last element of `nums`.  
   - **Invariant:** after processing the first *k* elements, `freq[v]` equals the number of times value *v* appears among those *k* elements.  
   - Edge handling: an empty or single‑element array still works because the loop simply does zero or one iteration; the bucket size safely covers all inputs.  
3. **Second pass – accumulate unique values**  
   - Loop: `for (int num : nums) { if (freq[num] == 1) sum += num; }`  
   - **Exit condition:** same as step 2, stops after the last element.  
   - **Invariant:** at the start of each iteration, `sum` holds the total of all previously seen numbers whose frequency is exactly one.  
   - The `== 1` check is crucial; using `< 2` would incorrectly include values that never appear (which cannot happen here) but `== 1` precisely captures “unique”.  
4. **Return result** – `return sum;` yields the sum of all values that occurred exactly once.

## Dry Run  
Input: `nums = [1, 2, 2, 3]`

| Iteration | `num` (first loop) | `freq` after update (relevant indices) | Note |
|-----------|-------------------|----------------------------------------|------|
| 1 | 1 | freq[1]=1 | first occurrence of 1 |
| 2 | 2 | freq[2]=1 | first occurrence of 2 |
| 3 | 2 | freq[2]=2 | second occurrence of 2 |
| 4 | 3 | freq[3]=1 | first occurrence of 3 |

Second pass:

| Iteration | `num` (second loop) | `freq[num]` | `sum` after possible addition | Note |
|-----------|--------------------|-------------|------------------------------|------|
| 1 | 1 | 1 | 1 | 1 is unique, added |
| 2 | 2 | 2 | 1 | 2 appears twice, skipped |
| 3 | 2 | 2 | 1 | same as above |
| 4 | 3 | 1 | 4 | 3 is unique, added |

Final state: `sum = 4`, which is exactly the sum of the unique elements `{1,3}`.

## Complexity  
- **Time:** O(n) – the first loop runs *n* times to fill `freq`, and the second loop runs *n* times to compute the sum; each iteration does O(1) work.  
- **Space:** O(1) – the auxiliary `freq` array has a constant size of 101 regardless of input length, and the extra variables (`sum`, loop indices) are constant‑space. (Output value does not affect the space bound.)

## Solution (Java)

```java
class Solution {
    public int sumOfUnique(int[] nums) {
        int[] freq = new int[101];

        for (int num : nums) {
            freq[num]++;
        }

        int sum = 0;

        for (int num : nums) {
            if (freq[num] == 1) {
                sum += num;
            }
        }

        return sum;
    }
}
```

---

**Runtime** 0 ms (beats 100.0%) · **Memory** 42.6 MB (beats 96.4%)

<sub>Synced by AILeetHub on 2026-10-19.</sub>
