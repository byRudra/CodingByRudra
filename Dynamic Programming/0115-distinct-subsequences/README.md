# 115. Distinct Subsequences

![Hard](https://img.shields.io/badge/Difficulty-Hard-ff375f?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/distinct-subsequences/)

`String` · `Dynamic Programming`

## Intuition  
When we scan `s` from left to right, the number of ways to build any prefix of `t` depends only on the counts already computed for the previous characters of `s`. Specifically, after processing the first `i‑1` characters of `s`, `dp[j]` stores the number of subsequences that form the first `j` characters of `t`. Adding `s[i‑1]` either leaves those counts unchanged (if the characters differ) or creates new ways by extending every subsequence that previously formed `t[0…j‑2]`. This observation eliminates the need for a full `n×m` table and lets us keep only a single row, updating it in reverse so that `dp[j‑1]` is still the old value when we need it.

## Approach  
1. **Initialisation**  
   ```java
   int[] dp = new int[m + 1];
   dp[0] = 1;
   ```  
   *Invariant*: before any outer iteration, `dp[0]=1` (empty `t` can always be formed) and `dp[j]=0` for `j>0` because no characters of `s` have been considered yet.  

2. **Outer loop over `s`** (`i` from 1 to `n`)  
   *Exit condition*: `i > n`.  
   *Invariant*: at the start of each outer iteration, `dp[j]` equals the number of ways to obtain `t[0…j‑1]` using the first `i‑1` characters of `s`.  

3. **Inner loop over `t` in reverse** (`j` from `m` downto 1)  
   *Exit condition*: `j < 1`.  
   *Invariant*: during the inner loop, `dp[j]` still holds the value for the prefix of `s` that excludes `s[i‑1]`, while `dp[j‑1]` is the unchanged value from the same outer iteration.  

   - If `s.charAt(i‑1) == t.charAt(j‑1)`, execute  
     ```java
     dp[j] = dp[j] + dp[j - 1];
     ```  
     This adds the ways that end with the new matching character (`dp[j‑1]`) to the existing ways that already formed `t[0…j‑1]` (`dp[j]`).  
   - Otherwise, do nothing; `dp[j]` already represents the correct count because the new character cannot contribute to this prefix.  

4. **Result**  
   After the outer loop finishes, `dp[m]` holds the number of distinct subsequences of the whole `s` that equal the whole `t`.  

**Edge‑case handling**  
- If `t` is empty, `dp[0]` stays 1, correctly returning 1.  
- If `s` is empty, the outer loop never runs, leaving `dp[m]=0` for any `m>0`.  
- The reverse inner loop (`j--`) is crucial; iterating forward would overwrite `dp[j‑1]` before it is used, producing incorrect counts.  

## Dry Run  

Input: `s = "bab"`, `t = "ba"`  

| i (processed char) | j | dp before update | dp after update | note |
|--------------------|---|------------------|-----------------|------|
| 1 (`b`)            | 2 | `[1,0,0]`        | unchanged       | `b != a` |
| 1 (`b`)            | 1 | `[1,0,0]`        | `[1,1,0]`       | match → `dp[1]=0+dp[0]=1` |
| 2 (`a`)            | 2 | `[1,1,0]`        | `[1,1,1]`       | match → `dp[2]=0+dp[1]=1` |
| 2 (`a`)            | 1 | `[1,1,1]`        | unchanged       | `a != b` |
| 3 (`b`)            | 2 | `[1,1,1]`        | unchanged       | `b != a` |
| 3 (`b`)            | 1 | `[1,1,1]`        | `[1,2,1]`       | match → `dp[1]=1+dp[0]=2` |

Final `dp = [1,2,1]`; `dp[2] = 1` is the answer, meaning there is exactly one subsequence of `"bab"` that equals `"ba"` (`"ba"` formed by the first two characters).  

## Complexity  
- **Time:** `O(n · m)` – the outer loop runs `n` times and the inner loop runs at most `m` times per outer iteration.  
- **Space:** `O(m)` – only a one‑dimensional array of length `m+1` is stored; the output integer is not counted.

## Solution (Java)

```java
// class Solution {
//     public int numDistinct(String s, String t) {
//         int n = s.length();
//         int m = t.length();

//         int dp[][] = new int[n + 1][m + 1];
//         for(int i = 0; i <= n; i++){
//             dp[i][0] = 1;
//         }

//         for(int i = 1; i <= n; i++){
//             for(int j = 1; j <= m; j++){
//                 if(s.charAt(i - 1) == t.charAt(j - 1)){
//                     dp[i][j] = dp[i - 1][j] + dp[i - 1][j - 1];
//                 }
//                 else{
//                     dp[i][j] = dp[i - 1][j];
//                 }
//             }
//         }
//         return dp[n][m];
//     }
// }

// Space Optimized 
class Solution {
    public int numDistinct(String s, String t) {

        int n = s.length();
        int m = t.length();

        int[] dp = new int[m + 1];

        dp[0] = 1;

        for (int i = 1; i <= n; i++) {

            for (int j = m; j >= 1; j--) {

                if (s.charAt(i - 1) == t.charAt(j - 1)) {
                    dp[j] = dp[j] + dp[j - 1];
                }
            }
        }

        return dp[m];
    }
}
```

---

**Runtime** 14 ms (beats 92.7%) · **Memory** 42.7 MB (beats 97.4%)

<sub>Synced by AILeetHub on 2026-09-06.</sub>
