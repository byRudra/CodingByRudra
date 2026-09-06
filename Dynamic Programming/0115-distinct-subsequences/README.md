# 115. Distinct Subsequences

![Hard](https://img.shields.io/badge/Difficulty-Hard-ff375f?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/distinct-subsequences/)

`String` · `Dynamic Programming`

## Intuition  
When we look at the prefixes `s[0..i)` and `t[0..j)`, the number of ways to form `t[0..j)` from `s[0..i)` depends only on two smaller sub‑problems: (1) ignoring the last character of `s` and (2) using it if it matches the last character of `t`. This eliminates the need for a second pass, a hash map, or recursion that would recompute overlapping sub‑problems. The whole computation can be captured in a two‑dimensional table – a classic **dynamic programming** pattern.

## Approach  
1. **Initialisation** – Create a table `dp` of size `(n+1) × (m+1)` where `n = s.length()` and `m = t.length()`.  
   *Invariant*: `dp[i][0] = 1` for every `i` because the empty string `t[0..0)` is a subsequence of any prefix of `s`.  
2. **Outer loop (`i` from 1 to `n`)** – At the start of each iteration `i`, the invariant “`dp[i‑1][*]` already stores the correct counts for the prefix `s[0..i‑1)`” holds.  
3. **Inner loop (`j` from 1 to `m`)** – For each cell `(i, j)` we maintain the invariant: “`dp[i][j]` equals the number of distinct subsequences of `s[0..i)` that equal `t[0..j)`”.  
   * If `s.charAt(i‑1) == t.charAt(j‑1)`, we have two choices: skip `s[i‑1]` (`dp[i‑1][j]`) or match it (`dp[i‑1][j‑1]`). Hence `dp[i][j] = dp[i‑1][j] + dp[i‑1][j‑1]`.  
   * Otherwise the character cannot be used, so we inherit the count from the shorter `s` prefix: `dp[i][j] = dp[i‑1][j]`.  
   The `<=` bounds are intentional; we need the extra row/column to represent the empty‑string cases.  
4. **Return** – After the loops finish, `dp[n][m]` holds the answer for the full strings.  
Edge cases are handled automatically: if `t` is longer than `s`, the inner loop never reaches a matching `j`, leaving `dp[n][m] = 0`. A single‑character `t` works because `dp[i][1]` accumulates matches as `i` grows.

## Dry Run  
Input: `s = "rabb"` , `t = "ab"`  

| i | j | dp[i][j] (value) | Change description |
|---|---|------------------|--------------------|
| 0 | 0 | 1 | base case (empty‑empty) |
| 1 | 1 | 0 | `s[0]='r'` ≠ `t[0]='a'` → copy `dp[0][1]=0` |
| 1 | 2 | 0 | `j>i` never matches, stays 0 |
| 2 | 1 | 1 | `s[1]='a'` matches → `dp[1][1]+dp[1][0]=0+1` |
| 2 | 2 | 0 | `s[1]='a'` ≠ `t[1]='b'` → copy `dp[1][2]=0` |
| 3 | 1 | 1 | `s[2]='b'` ≠ `t[0]='a'` → copy `dp[2][1]=1` |
| 3 | 2 | 1 | match → `dp[2][2]+dp[2][1]=0+1` |
| 4 | 1 | 1 | `s[3]='b'` ≠ `t[0]='a'` → copy `dp[3][1]=1` |
| 4 | 2 | 2 | match → `dp[3][2]+dp[3][1]=1+1` |

After processing all rows, `dp[4][2] = 2`, meaning there are two distinct subsequences of `"rabb"` that equal `"ab"` (`"ab"` using the first `a` and second `b`, and `"ab"` using the first `a` and third `b`).

## Complexity  
- **Time:** `O(n · m)` – the double loop visits each cell of the `(n+1) × (m+1)` table once.  
- **Space:** `O(n · m)` – the DP table stores an integer for every pair of prefix lengths (output array excluded).

## Solution (Java)

```java
class Solution {
    public int numDistinct(String s, String t) {
        int n = s.length();
        int m = t.length();

        int dp[][] = new int[n + 1][m + 1];
        for(int i = 0; i <= n; i++){
            dp[i][0] = 1;
        }

        for(int i = 1; i <= n; i++){
            for(int j = 1; j <= m; j++){
                if(s.charAt(i - 1) == t.charAt(j - 1)){
                    dp[i][j] = dp[i - 1][j] + dp[i - 1][j - 1];
                }
                else{
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }
        return dp[n][m];
    }
}
```

---

**Runtime** 19 ms (beats 69.1%) · **Memory** 54.1 MB (beats 73.0%)

<sub>Synced by AILeetHub on 2026-09-06.</sub>
