# 1929. Concatenation of Array

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/concatenation-of-array/)

`Array` · `Simulation`

## Intuition  
The whole task reduces to a single observation: each element of the original array must appear twice in the result, once at its original index `i` and once exactly `n` positions later. Because the target length is known (`2 * n`), we can fill the answer in one linear scan without any extra data structures. A naïve solution might first allocate two separate copies of `nums` and then concatenate them, which would require two passes or an auxiliary list. The insight that the two copies can be written side‑by‑side during the same iteration eliminates the extra pass. This is a classic **direct indexing** pattern.

## Approach  
1. **Determine sizes** – `r = nums.length` gives the original length, `n = 2 * r` is the length of the final array.  
2. **Allocate result** – `answer = new int[n]` creates a zero‑filled array of the required size.  
3. **Iterate** – `for (int i = 0; i < r; i++)` runs exactly `r` times.  
   * **Loop invariant:** after processing iteration `i` (0‑based), the first `i+1` positions of `answer` (`answer[0..i]`) contain the first `i+1` elements of `nums`, and the positions `answer[r..r+i]` contain the same elements shifted by `r`.  
   * Inside the body we write `answer[i] = nums[i]` and `answer[i + r] = nums[i]`. The `+ r` offset is safe because the maximum `i` is `r‑1`, so `i + r ≤ 2r‑1`, which is the last valid index of `answer`.  
4. **Return** – after the loop finishes, every index of `answer` is filled exactly once, so `return answer` yields the concatenated array.  

**Edge‑case handling:** The constraints guarantee `r ≥ 1`, so the loop executes at least once; no special case for an empty input is needed. The same logic works for both even and odd `r` because the offset `r` is independent of parity. Off‑by‑one errors are avoided by using `< r` as the loop condition and by writing to `i + r` rather than `i + r - 1`.

## Dry Run  

Input: `nums = [1, 2, 1]` (so `r = 3`, `n = 6`)

| i | answer[i] | answer[i+r] | note |
|---|-----------|-------------|------|
| 0 | 1         | 1           | copy first element to positions 0 and 3 |
| 1 | 2         | 2           | copy second element to positions 1 and 4 |
| 2 | 1         | 1           | copy third element to positions 2 and 5 |

After the third iteration the array `answer` is `[1, 2, 1, 1, 2, 1]`, which exactly matches the required concatenation.

## Complexity  
- **Time:** `O(n)` – the single `for` loop runs `r = n/2` iterations, each performing two constant‑time assignments.  
- **Space:** `O(n)` – we allocate an output array of size `2 * r`; no additional auxiliary structures are used (the space for the result is counted).

## Solution (Java)

```java
class Solution {
    public int[] getConcatenation(int[] nums) {
        int r = nums.length;
        int n = 2 * r;
        int answer[] = new int[n];
        for(int i = 0; i < r; i++){
            answer[i] = nums[i];
            answer[i + r] = nums[i];
        }
        return answer;
    }
}
```

---

**Runtime** 1 ms (beats 97.6%) · **Memory** 46.4 MB (beats 96.5%)

<sub>Synced by AILeetHub on 2026-11-04.</sub>
