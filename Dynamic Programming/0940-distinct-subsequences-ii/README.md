# 940. Distinct Subsequences II

![Hard](https://img.shields.io/badge/Difficulty-Hard-ff375f?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/distinct-subsequences-ii/)

`String` · `Dynamic Programming`

## Intuition  
When we scan the string left‑to‑right, every existing subsequence can either stay as it is or be extended by the current character, which would double the number of distinct subsequences. The only mistake in this naïve doubling is that subsequences ending with the same character have been counted twice: the ones that were already created the last time this character appeared. If we remember, for each letter, how many subsequences were *newly* introduced when that letter was processed, we can subtract that stale contribution before adding the fresh double. This observation eliminates the need for an exponential enumeration or a hash‑set of all subsequences, and leads to a linear‑time, constant‑space DP. The pattern is a classic “DP with last‑occurrence correction”.

## Approach  
1. Initialise an array `count[26]` with zeros; `count[c]` will store the number of subsequences that were created **exactly** when character `c` was processed last.  
2. Initialise `total = 0`, representing the number of distinct non‑empty subsequences seen so far.  
3. Iterate over each character `ch` of `s`:  
   - Compute `idx = ch - 'a'`.  
   - The number of subsequences that would appear if we appended `ch` to every existing subsequence **plus** the subsequence consisting of `ch` alone is `newVal = (total + 1) % MOD`.  
   - Before adopting `newVal`, remove the stale contribution of the previous occurrence of this character: `total = (total - count[idx] + newVal + MOD) % MOD`. The extra `+ MOD` guarantees a non‑negative intermediate value.  
   - Record the fresh contribution for future duplicates: `count[idx] = newVal`.  
4. After the loop, `total` already equals the answer, so return it cast to `int`.

Key invariants:  
- At the start of each iteration, `total` equals the count of distinct subsequences formed from the prefix processed so far.  
- `count[c]` holds the number of subsequences that were added **when the most recent `c` was seen**.  

Edge cases: an empty string never reaches the loop, leaving `total = 0` (the problem guarantees length ≥ 1). Single‑character strings produce `newVal = 1`, and the subtraction term is zero, yielding the correct answer `1`. The modulo operation is applied after every arithmetic step to avoid overflow.

## Dry Run  
Input: `s = "aba"`

| i (char) | ch | total before | newVal = total+1 | count[a] before | count[b] before | total after | note |
|----------|----|--------------|-----------------|-----------------|-----------------|------------|------|
| 0        | a  | 0            | 1               | 0               | 0               | (0‑0+1)=1   | first ‘a’ creates “a” |
| 1        | b  | 1            | 2               | 0               | 0               | (1‑0+2)=3   | “b”, “ab” added |
| 2        | a  | 3            | 4               | 1               | 2               | (3‑1+4)=6   | old “a” contributions removed, new “a”, “ba”, “aba”, “aa” added |

Final `total = 6`, which matches the distinct subsequences `{a,b,ab,ba,aa,aba}`.

## Complexity  
- **Time:** O(n) – the single pass processes each character once, and the update `total = (total - count[idx] + newVal + MOD) % MOD` is O(1).  
- **Space:** O(1) – only a fixed‑size array of 26 longs and a few scalar variables are used, independent of the input length. (The output integer is not counted.)

## Solution (Java)

```java
class Solution {
    public int distinctSubseqII(String s) {
        final int MOD = 1_000_000_007;
        long[] count = new long[26];
        long total = 0;

        for (char ch : s.toCharArray()) {
            int idx = ch - 'a';
            long newVal = (total + 1) % MOD;
            total = (total - count[idx] + newVal + MOD) % MOD;  // +MOD guards against negative
            count[idx] = newVal;
        }

        return (int) total;
    }
}
```

---

**Runtime** 2 ms (beats 100.0%) · **Memory** 42.7 MB (beats 99.6%)

<sub>Synced by AILeetHub on 2026-09-07.</sub>
