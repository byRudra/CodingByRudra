# 940. Distinct Subsequences II

![Hard](https://img.shields.io/badge/Difficulty-Hard-ff375f?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/distinct-subsequences-ii/)

`String` · `Dynamic Programming`

## Intuition  
When we extend all existing subsequences by the current character, we double the set of distinct subsequences, but any subsequence that already ended with this character would be created twice. The key observation is that the number of *new* subsequences contributed by a character equals the total count so far plus the singleton formed by the character itself, minus the subsequences that previously ended with the same character. Maintaining, for each letter, the count of subsequences that end with it lets us subtract the duplicates in O(1) time, eliminating the need for a second pass, a hash set, or exponential enumeration. This is the classic “two‑state DP with last‑occurrence tracking” pattern.

## Approach  
1. **Initialisation** – Create an array `count[26]` filled with zeros; `count[i]` will store the number of distinct subsequences that end with the i‑th letter. Initialise `total = 0`, the number of distinct non‑empty subsequences seen so far.  
2. **Iterate over characters** – For each `ch` in `s.toCharArray()`:  
   - Compute `idx = ch - 'a'`.  
   - `newValue = (total + 1) % MOD` represents all subsequences formed by appending `ch` to every existing subsequence (`total`) plus the subsequence consisting of `ch` alone.  
   - Update `total` with  
     `total = (total - count[idx] + newValue + MOD) % MOD`.  
     The invariant before the update is: `total` equals the sum of all `count[i]`. Subtracting `count[idx]` removes the duplicates that would arise from extending subsequences that already end with `ch`. Adding `newValue` inserts the fresh subsequences. The extra `+ MOD` guarantees a non‑negative intermediate value before the final modulo.  
   - Set `count[idx] = newValue` so that future iterations know the latest number of subsequences ending with `ch`.  
3. **Return** – After processing the whole string, `total` holds the answer; cast it to `int` as required.

**Edge considerations**  
- The algorithm works for length‑1 strings because `total` starts at 0, `newValue` becomes 1, and `total` ends as 1.  
- No special handling for even/odd length is needed; the update formula is uniform.  
- The modulo operation is applied after every arithmetic step to avoid overflow of the 64‑bit `long`.  

## Dry Run  

Input: `s = "aba"`

| iter | ch | idx | newValue = (total+1) % MOD | total (after update) | count[idx] (after update) | note |
|------|----|-----|----------------------------|----------------------|---------------------------|------|
| 1    | a  | 0   | (0+1)=1                    | (0‑0+1)=1            | 1                         | start new subsequence “a” |
| 2    | b  | 1   | (1+1)=2                    | (1‑0+2)=3            | 2                         | add “b” and extend “a” → “ab” |
| 3    | a  | 0   | (3+1)=4                    | (3‑1+4)=6            | 4                         | remove old “a” endings (1), add “a”, “ba”, “aa”, “aba” |

Final `total = 6`, which matches the distinct subsequences `{a,b,ab,ba,aa,aba}`.

## Complexity  
- **Time:** O(n) – the single loop runs `n` times, and each iteration performs only constant‑time arithmetic and array access.  
- **Space:** O(1) – we store a fixed‑size array of 26 longs and a few scalar variables, independent of the input length. (The output integer is not counted.)

## Solution (Java)

```java
class Solution {
    public int distinctSubseqII(String s) {
        final int MOD = 1_000_000_007;
        long count[] = new long[26];
        long total = 0;

        for(char ch : s.toCharArray()){
            int idx = ch - 'a';
            long newValue = (total + 1) % MOD;
            total = (total - count[idx] + newValue + MOD) % MOD;
            count[idx] = newValue;
        }

        return (int) total;
    }
}
```

---

**Runtime** 3 ms (beats 89.4%) · **Memory** 43.2 MB (beats 81.6%)

<sub>Synced by AILeetHub on 2026-09-07.</sub>
