# 2778. Sum of Squares of Special Elements 

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/sum-of-squares-of-special-elements/)

`Array` · `Enumeration`

## Intuition  
The only positions that can contribute to the answer are those whose 1‑based index divides the array length `n`. Because the first element always satisfies this condition, we can seed the sum with `nums[0]²`. A naïve solution might check every pair of indices or build a list of divisors first, incurring extra passes or auxiliary storage. The key observation is that we can decide on‑the‑fly whether an index is special simply by testing `n % (i+1) == 0` while scanning the array once. This yields a classic **single‑pass enumeration** pattern.

## Approach  
1. **Initialize** `answer` with the square of the first element: `answer = nums[0] * nums[0]`.  
   *Invariant:* after this step `answer` holds the sum of squares of all special elements examined so far (currently only index 1).  
2. **Iterate** `i` from `1` up to `nums.length‑1` (inclusive).  
   *Loop exit condition:* `i == nums.length`.  
   *Invariant each iteration:* `answer` equals the sum of squares of all special elements among indices `1 … i`.  
3. **Check divisibility**: compute `nums.length % (i + 1)`. If the remainder is `0`, the current 1‑based index `(i+1)` divides `n`.  
   *Why `i+1`*: the array is 0‑based in Java, but the problem definition is 1‑based; adding one aligns the indices.  
4. **Accumulate**: when the check succeeds, add the square of `nums[i]` to `answer` via `answer += nums[i] * nums[i]`.  
   *Off‑by‑one guard*: the condition uses `<=` implicitly because `i` never reaches `nums.length`; the modulo test handles the boundary correctly.  
5. **Return** `answer` after the loop finishes.  
   *Edge cases*: for a single‑element array the loop body never executes, and the initial `answer` already contains the correct result.

## Dry Run  
Input: `nums = [2, 7, 1, 19, 18, 3]` (n = 6)

| i (0‑based) | index = i+1 | `n % index` | add? | `answer` after iteration | note |
|-------------|-------------|-------------|------|--------------------------|------|
| 0 | 1 | 6 % 1 = 0 | yes | 2² = 4 | initialization |
| 1 | 2 | 6 % 2 = 0 | yes | 4 + 7² = 53 | 7 is special |
| 2 | 3 | 6 % 3 = 0 | yes | 53 + 1² = 54 | 1 is special |
| 3 | 4 | 6 % 4 = 2 | no | 54 | 19 not special |
| 4 | 5 | 6 % 5 = 1 | no | 54 | 18 not special |
| 5 | 6 | 6 % 6 = 0 | yes | 54 + 3² = 63 | 3 is special |

After processing all indices, `answer = 63`, which matches the required sum of squares of the special elements.

## Complexity  
- **Time:** `O(n)` – the single `for` loop visits each element exactly once, and the modulo operation is `O(1)`.  
- **Space:** `O(1)` – only a few primitive variables (`answer`, loop index) are used; the output integer does not count toward extra space.

## Solution (Java)

```java
class Solution {
    public int sumOfSquares(int[] nums) {
        int answer = nums[0] * nums[0];
        for (int i = 1; i < nums.length; i++) {
            if (nums.length % (i + 1) == 0)
                answer += nums[i] * nums[i];
        }
        return answer;
    }
}
```

---

**Runtime** 1 ms (beats 100.0%) · **Memory** 46.4 MB (beats 51.6%)

<sub>Synced by AILeetHub on 2026-10-11.</sub>
