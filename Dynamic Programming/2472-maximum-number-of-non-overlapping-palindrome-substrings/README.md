# 2472. Maximum Number of Non-overlapping Palindrome Substrings

![Hard](https://img.shields.io/badge/Difficulty-Hard-ff375f?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/maximum-number-of-non-overlapping-palindrome-substrings/)

`Two Pointers` · `String` · `Dynamic Programming` · `Greedy`

## Intuition  
If we always take the *earliest* palindrome that satisfies the length requirement, the remaining suffix of the string is as long as possible, so we never lose a chance to place another valid substring later. A naïve solution would scan all subsets or run a DP that examines every possible palindrome, which costs extra passes or O(n²) memory. The key insight is that a greedy “first‑fit” choice is sufficient: once a palindrome ending at position r is found, any solution that skips it can be transformed into one that uses it without decreasing the total count. This reduces the problem to a single forward scan using two nested loops and a constant‑space palindrome checker.

## Approach  
1. **Initialize** `n = s.length()`, `count = 0`, `start = 0`.  
2. **Outer while** – continue while `start < n`. The invariant: all characters before `start` are already covered by selected substrings, and none of them can be used again.  
3. **Search for the earliest ending** `r` – loop `r` from `start + k - 1` up to `n‑1`. The condition guarantees that any candidate `[l, r]` has length ≥ k.  
4. **Try every feasible left bound** – for each `r`, loop `l` from `start` to `r - k + 1`. The invariant inside this inner loop is that `[l, r]` is the current candidate interval, still respecting the minimum length.  
5. **Palindrome test** – call `isPalindrome(s, l, r)`. This routine moves two pointers inward (`l++`, `r--`) and returns `false` on the first mismatch, otherwise `true`.  
6. **Accept the first palindrome** – when `isPalindrome` returns `true`, increment `count`, set `start = r + 1` (the next unchecked position), mark `found = true`, and break both the `l`‑loop and the `r`‑loop. This implements the greedy “first‑fit” rule.  
7. **No palindrome found** – if the `r`‑loop finishes without setting `found`, break the outer while because no further valid substring can start at the current `start`.  
8. **Return** `count` as the maximal number of non‑overlapping palindromes of length ≥ k.

## Dry Run  
**Input:** `s = "abaccdbbd"`, `k = 3`

| iteration | start | r | l | palindrome? | action | note |
|-----------|-------|---|---|-------------|--------|------|
| 1 | 0 | 2 | 0 | true (`"aba"`) | count=1, start=3 | earliest feasible palindrome found |
| 2 | 3 | 5 | 3 | false (`"acc"` not palindrome) | – | continue inner loop |
| 2 | 3 | 5 | 4 | false (`"cc"` too short) | – | |
| 2 | 3 | 6 | 3 | false (`"accd"` ) | – | |
| 2 | 3 | 7 | 3 | true (`"dbbd"` ) | count=2, start=8 | second palindrome selected |
| 3 | 8 | 8 | 8 | false (length 1 < k) | – | outer while exits because `found` stays false |

After the second selection `start` moves past index 7, leaving only one character (`'d'`) which cannot form a palindrome of length 3. The algorithm terminates with `count = 2`, which is optimal.

## Complexity  
- **Time:** **O(n³)** in the worst case. The outer while runs at most n times; for each start the double loop scans O(n²) pairs `(l, r)`, and each palindrome check may traverse up to O(n) characters.  
- **Space:** **O(1)** extra space, because only a few integer indices and a boolean flag are stored; the output integer does not count toward auxiliary memory.

## Solution (Java)

```java
class Solution {
    public int maxPalindromes(String s, int k) {
        int n = s.length();
                int count = 0;
        int start = 0;

        while (start < n) {
            boolean found = false;

            // Earliest ending position
            for (int r = start + k - 1; r < n; r++) {

                // Try every possible starting point
                for (int l = start; l <= r - k + 1; l++) {

                    if (isPalindrome(s, l, r)) {
                        count++;
                        start = r + 1;
                        found = true;
                        break;
                    }
                }

                if (found) break;
            }

            if (!found) break;
        }

        return count;
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

**Runtime** 3 ms (beats 80.9%) · **Memory** 42.7 MB (beats 89.9%)

<sub>Synced by AILeetHub on 2026-09-15.</sub>
