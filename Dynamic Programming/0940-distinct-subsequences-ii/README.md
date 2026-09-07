# 940. Distinct Subsequences II

![Hard](https://img.shields.io/badge/Difficulty-Hard-ff375f?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/distinct-subsequences-ii/)

`String` · `Dynamic Programming`

## Intuition  
When we scan the string left‑to‑right, every new character can either start a fresh subsequence (the single‑character subsequence) or be appended to each subsequence that already exists. Thus after processing the first *i* characters the total number of distinct subsequences equals “all previous subsequences plus the empty one”. The only thing that can cause double‑counting is re‑using a character that has appeared before: any subsequence that already ended with the current character would be generated again. If we remember, for each letter, how many distinct subsequences ended with that letter in the previous step, we can subtract that amount before adding the new contributions. This observation eliminates the need for exponential enumeration, a hash set, or a second pass. The pattern is a **dynamic programming with last‑occurrence subtraction**.

## Approach  
1. **Initialisation** – `count[26]` stores, for each letter `c`, the number of distinct subsequences that ended with `c` after the previous iteration. `total` holds the overall number of distinct non‑empty subsequences seen so far; both start at 0.  
2. **Iterate over characters** – the `for` loop runs until the end of `s.toCharArray()`. Its invariant: before processing `ch`, `total` equals the number of distinct subsequences formed from the prefix processed so far, and `count[idx]` equals the contribution of subsequences that end with `ch`.  
3. **Compute the raw contribution** – `newVal = (total + 1) % MOD`. The `+1` represents the empty subsequence, which can be turned into the single‑character subsequence consisting of `ch`.  
4. **Remove duplicates** – `total = (total - count[idx] + newVal + MOD) % MOD`. `count[idx]` is exactly the number of subsequences that would be recreated by appending `ch` to an older subsequence ending with the same character; subtracting it prevents double counting. Adding `MOD` guarantees a non‑negative intermediate value before the final modulo.  
5. **Update the per‑character record** – `count[idx] = newVal`. After the update, `count[idx]` now reflects the number of distinct subsequences that end with `ch` for the next iteration.  
6. **Return** – after the loop finishes, `total` already excludes the empty subsequence, so casting it to `int` yields the required answer.

Edge cases are handled automatically: an empty or single‑character string never enters the subtraction branch because `count[idx]` is initially 0; the modulo guard prevents overflow for the maximum length 2000.

## Dry Run  

Input: `s = "aba"`

| step | ch | idx | newVal | total (after update) | count[idx] (after update) | note |
|------|----|-----|--------|----------------------|---------------------------|------|
| 1    | a  | 0   | (0+1)=1 | (0‑0+1)=1            | 1                         | start “a” |
| 2    | b  | 1   | (1+1)=2 | (1‑0+2)=3            | 2                         | add “b”, “ab” |
| 3    | a  | 0   | (3+1)=4 | (3‑1+4)=6            | 4                         | remove old “a” contributions, add “a”, “ba”, “aa”, “aba” |

After processing all characters, `total = 6`, which matches the six distinct non‑empty subsequences listed in the example.

## Complexity  
- **Time:** `O(n)` because the loop visits each of the `n` characters exactly once, and all operations inside are constant‑time.  
- **Space:** `O(1)` (specifically `O(26)`) since we keep only a fixed‑size array of 26 longs and a few scalar variables, independent of the input length. The output integer is not counted toward extra space.

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
