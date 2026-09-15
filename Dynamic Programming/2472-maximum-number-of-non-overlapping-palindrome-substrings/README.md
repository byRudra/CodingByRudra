# 2472. Maximum Number of Non-overlapping Palindrome Substrings

![Hard](https://img.shields.io/badge/Difficulty-Hard-ff375f?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/maximum-number-of-non-overlapping-palindrome-substrings/)

`Two Pointers` · `String` · `Dynamic Programming` · `Greedy`

## Intuition  
If we always take the left‑most palindrome that satisfies the length requirement, we never hurt the chance of adding more substrings later. Any palindrome longer than the minimum viable length ( k or k+1 ) consumes extra characters that could serve as the start of another valid piece, so a greedy choice of the shortest possible palindrome is optimal. The naïve way would be to try every subset of palindromes with DP, costing O(n²) time and extra memory. The observation that a local, shortest‑possible decision is safe lets us replace the DP with a single linear scan, using the classic two‑pointer palindrome test.

## Approach  
1. **Initialisation** – `n = s.length()`, `ans = 0`, `i = 0`.  
2. **Main loop** – `while (i + k <= n)` ensures there are at least k characters left to form a candidate. Invariant: all indices `< i` are already fixed and never overlap with future choices.  
3. **Try length k** – Call `isPalindrome(s, i, i + k - 1)`.  
   *If true*: increment `ans`, advance `i` by `k`, set `found = true`. This respects non‑overlap because we jump exactly past the taken substring.  
4. **Otherwise try length k+1** – Guarded by `i + k + 1 <= n` (the `+1` must still fit). Call `isPalindrome(s, i, i + k)`.  
   *If true*: increment `ans`, advance `i` by `k + 1`, set `found = true`. The extra character is allowed because a palindrome of odd length may start at the same index and still be the shortest odd candidate.  
5. **No palindrome found** – If both checks fail, increment `i` by 1 to shift the window rightward. This maintains the invariant that we have examined every possible start position.  
6. **Termination** – Loop ends when fewer than k characters remain, because no further substring can satisfy the length constraint. Return `ans`.  

The helper `isPalindrome` uses two indices `l` and `r` that move inward (`l++`, `r--`) until they cross; it stops early on a mismatch, guaranteeing O(length) work per call.

## Dry Run  
Input: `s = "abaccdbbd"`, `k = 3`

| iter | i (start) | check k | check k+1 | ans | note |
|------|-----------|---------|-----------|-----|------|
| 1 | 0 | `aba` → true | – | 1 | take “aba”, i←0+3 |
| 2 | 3 | `c c d` → false | `c cd` → false | 1 | move i←4 |
| 3 | 4 | `c d b` → false | `c db` → false | 1 | move i←5 |
| 4 | 5 | `d b b` → false | `d bbd` → true | 2 | take “dbbd”, i←5+4 |
| 5 | 9 | loop condition fails (`9+3>9`) | – | 2 | termination |

The algorithm finishes with `ans = 2`, exactly the optimal selection “aba” and “dbbd”.

## Complexity  
- **Time:** O(n · k) ≤ O(n²) – each iteration performs at most two palindrome checks, each scanning at most k or k+1 characters, and the outer loop runs at most n times.  
- **Space:** O(1) – only a few integer variables and the two‑pointer indices inside `isPalindrome` are used; no extra containers are allocated.

## Solution (Java)

```java
class Solution {
    public int maxPalindromes(String s, int k) {
        int n = s.length();
        int ans = 0;
        int i = 0;

       while (i + k <= n) {
            boolean found = false;

            // Check length k
            if (isPalindrome(s, i, i + k - 1)) {
                ans++;
                i += k;
                found = true;
            }
            // Check length k + 1
            else if (i + k + 1 <= n &&
                     isPalindrome(s, i, i + k)) {
                ans++;
                i += k + 1;
                found = true;
            }

            if (!found) {
                i++;
            }
        }

        return ans;
    }

    private boolean isPalindrome(String s, int l, int r) {
        while (l < r) {
            if (s.charAt(l++) != s.charAt(r--))
                return false;
        }
        return true;
    }
}
```

---

**Runtime** 1 ms (beats 100.0%) · **Memory** 42.9 MB (beats 71.9%)

<sub>Synced by AILeetHub on 2026-09-15.</sub>
