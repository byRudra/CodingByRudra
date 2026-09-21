# 1864. Minimum Number of Swaps to Make the Binary String Alternating

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/minimum-number-of-swaps-to-make-the-binary-string-alternating/)

`String` · `Greedy`

## Intuition  
The string can only be alternating in one of two ways: it either begins with `'0'` and then follows the pattern `0101…`, or it begins with `'1'` and follows `1010…`. Because each swap fixes two misplaced characters, the number of swaps needed for a given pattern equals half the number of positions where the current character differs from the target pattern. The only obstacle is the balance of zeros and ones: an alternating string of length n can have at most one more of the majority character. If `|no1‑no0|>1` the task is impossible, eliminating the need for any extra passes, hash maps, or sorting. This is a classic **two‑pattern greedy** check.

## Approach  
1. **Count characters** – iterate over `s.toCharArray()`, increment `no0` for `'0'` and `no1` for `'1'`.  
2. **Feasibility test** – if `Math.abs(no1‑no0) > 1` return `-1`. The invariant here is that a valid alternating string cannot have a difference larger than one.  
3. **Initialize mismatch counters** – `startZero = 0` (mismatches if pattern starts with `'0'`) and `startOne = 0` (mismatches if pattern starts with `'1'`). Set `index = 0`.  
4. **Single pass to count mismatches** – for each `ch` in the character array:  
   * Compute `startwithZero = index % 2 == 0 ? '0' : '1'`.  
   * Compute `startwithOne  = index % 2 == 0 ? '1' : '0'`.  
   * If `startwithZero != ch` increment `startZero`.  
   * If `startwithOne  != ch` increment `startOne`.  
   * Increment `index`.  
   The loop exits after the last character; the invariant is that `startZero` (resp. `startOne`) always equals the number of positions seen so far that disagree with the respective target pattern.  
5. **Derive answer from counts** –  
   * If `no1 > no0` the only feasible pattern is the one that starts with `'1'`; return `startOne / 2`.  
   * If `no1 < no0` the only feasible pattern starts with `'0'`; return `startZero / 2`.  
   * If counts are equal, both patterns are possible; the optimal swaps are `Math.min(startZero, startOne) / 2`.  
   Division by 2 works because each swap corrects two mismatched positions.

## Dry Run  
Input: `s = "111000"`

| index | ch | startwithZero | startwithOne | startZero | startOne | note |
|------|----|---------------|--------------|-----------|----------|------|
| 0 | 1 | 0 | 1 | 1 | 0 | `startZero` mismatches, `startOne` matches |
| 1 | 1 | 1 | 0 | 1 | 1 | `startOne` now mismatches |
| 2 | 1 | 0 | 1 | 2 | 1 | `startZero` mismatches again |
| 3 | 0 | 1 | 0 | 2 | 2 | `startOne` mismatches, `startZero` matches |
| 4 | 0 | 0 | 1 | 2 | 3 | `startOne` mismatches |
| 5 | 0 | 1 | 0 | 3 | 3 | both patterns mismatch |

After the loop `no1 = 3`, `no0 = 3`, `startZero = 3`, `startOne = 3`. Both patterns are viable, so answer = `Math.min(3,3)/2 = 1`. One swap indeed makes the string alternating.

## Complexity  
- **Time:** `O(n)` – the algorithm scans the string twice (once for counts, once for mismatches); each loop processes `n` characters.  
- **Space:** `O(1)` – only a handful of integer variables (`no0`, `no1`, `startZero`, `startOne`, `index`) are used, independent of input size. The output integer is not counted toward extra space.

## Solution (Java)

```java
class Solution {
    public int minSwaps(String s) {
        int no1 = 0;
        int no0 = 0;
        for(char ch : s.toCharArray()){
            if(ch == '0') no0++;
            else no1++;
        }
        if (Math.abs(no1 - no0) > 1) return -1;

        int startZero = 0;
        int startOne = 0;
        int index = 0;
        for(char ch : s.toCharArray()){
            char startwithZero = index % 2 == 0 ? '0' : '1';
            char startwithOne = index % 2 == 0 ? '1' : '0';
            if(startwithZero != ch) startZero++;
            if(startwithOne != ch) startOne++;
            index++;
        }
        if(no1 > no0)
            return startOne / 2;
        if(no1 < no0)
            return startZero / 2;
        return Math.min(startZero, startOne) / 2;
    }
}
```

---

**Runtime** 1 ms (beats 100.0%) · **Memory** 43.2 MB (beats 35.9%)

<sub>Synced by AILeetHub on 2026-09-21.</sub>
