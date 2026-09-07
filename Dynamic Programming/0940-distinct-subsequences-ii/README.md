# 940. Distinct Subsequences II

![Hard](https://img.shields.io/badge/Difficulty-Hard-ff375f?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/distinct-subsequences-ii/)

`String` · `Dynamic Programming`

## Intuition  
When we scan the string left‑to‑right, every existing distinct subsequence can either ignore the current character or append it, which would *double* the count. The only over‑count comes from subsequences that already ended with the same character earlier – they would be created twice. If we remember how many subsequences ended with each letter, we can subtract exactly those duplicates. This observation eliminates the need for exponential enumeration or a hash‑set of strings. The technique is a linear‑time DP that tracks the contribution of the last occurrence of each character.

## Approach  
1. **Initialisation** – `count[26]` stores, for each letter, the number of distinct subsequences that end with that letter after processing the prefix seen so far. `total` holds the number of distinct non‑empty subsequences for the processed prefix. Both start at 0.  
2. **Iterate over characters** – `for (char ch : s.toCharArray())` runs until the last character; the loop invariant is: *after processing the first *k* characters, `total` equals the number of distinct non‑empty subsequences of the prefix `s[0..k‑1]`, and `count[idx]` equals the number of those subsequences that end with character `idx`.*  
3. **Compute new contributions** – `idx = ch - 'a'`. `newVal = (total + 1) % MOD` counts all subsequences formed by appending `ch` to each existing subsequence **plus** the subsequence consisting of `ch` alone (the “+ 1”).  
4. **Remove duplicates** – `total = (total - count[idx] + newVal + MOD) % MOD`. `count[idx]` is exactly the number of subsequences that would be duplicated by the current character, so we subtract it before adding `newVal`. Adding `MOD` guarantees a non‑negative intermediate value.  
5. **Update per‑character record** – `count[idx] = newVal` records the fresh number of subsequences that now end with `ch`.  
6. **Return** – after the loop, `total` already excludes the empty subsequence, so casting it to `int` yields the required answer.

Edge considerations: the algorithm works for a single‑character string because `newVal = 0+1 = 1` and `total` becomes 1. No special handling for empty input is needed (constraints guarantee length ≥ 1). The modulo operation is applied after every arithmetic step to avoid overflow of the 64‑bit `long`.

## Dry Run  

Input: `s = "aba"`

| step | ch | idx | newVal = total+1 | total (before) | total (after) = total‑count[idx]+newVal | count[idx] (after) | note |
|------|----|-----|------------------|----------------|------------------------------------------|--------------------|------|
| 1    | a  | 0   | 1                | 0              | (0‑0+1)=1                                 | 1                  | start new subsequence “a” |
| 2    | b  | 1   | 2                | 1              | (1‑0+2)=3                                 | 2                  | “b” and “ab” added |
| 3    | a  | 0   | 4                | 3              | (3‑1+4)=6                                 | 4                  | duplicates ending with ‘a’ removed, “a”, “aa”, “ba”, “aba” added |

After processing all characters, `total = 6`, which matches the distinct subsequences `{a, b, ab, aa, ba, aba}`.

## Complexity  
- **Time:** `O(n)` – the single pass iterates `n = s.length()` times, and each iteration performs only constant‑time arithmetic.  
- **Space:** `O(1)` – we keep a fixed‑size array of 26 `long`s and a few scalar variables; the output integer is not counted.

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

**Runtime** 2 ms (beats 100.0%) · **Memory** 42.8 MB (beats 98.6%)

<sub>Synced by AILeetHub on 2026-09-07.</sub>
