# 2472. Maximum Number of Non-overlapping Palindrome Substrings

![Hard](https://img.shields.io/badge/Difficulty-Hard-ff375f?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/maximum-number-of-non-overlapping-palindrome-substrings/)

`Two Pointers` · `String` · `Dynamic Programming` · `Greedy`

## Intuition  
If we scan the string from left to right and always take the *earliest* palindrome that satisfies the length requirement, we never lose a chance to place another palindrome later, because any later palindrome would start at or after the current start index. Moreover, any palindrome of length ≥ k must contain either a palindrome of exactly length k or length k+1 that begins at the same position – the extra character (if any) can only be the middle of an odd‑length palindrome. Therefore we only need to test the two smallest possible lengths. This observation eliminates the need for a full DP table or a second pass to collect all palindrome intervals; a single greedy pass suffices.

## Approach  
1. **Initialize** `count = 0`, `start = 0`, `length = s.length()`.  
2. **Loop while a palindrome of length k could still fit**: `while (start + k <= length)`.  
   - *Invariant*: all characters before `start` have already been assigned to non‑overlapping palindromes (or skipped), and `start` is the leftmost index not yet processed.  
3. **Check length‑k palindrome**: call `isPalindrome(s, start, start + k - 1)`.  
   - If true, advance `start += k` and increment `count`. This consumes exactly the k characters we just verified.  
4. **Otherwise, check length‑(k+1) palindrome** (only if it fits): `if (start + k + 1 <= length && isPalindrome(s, start, start + k))`.  
   - If true, advance `start += k + 1` and increment `count`. This handles the smallest odd‑length case that still meets the minimum length.  
5. **Otherwise**, no palindrome starts at `start` with the allowed lengths, so we move one character forward: `start++`.  
6. **Terminate** when `start + k > length`; no further palindrome of required size can start.  
7. **Return** `count`.

**Edge handling**:  
- The loop guard `start + k <= length` prevents out‑of‑bounds when checking the k‑length case.  
- The second guard `start + k + 1 <= length` ensures the k+1 check is safe for odd‑length strings.  
- Single‑character strings never enter the loop because `k ≥ 1` and `start + k > length` immediately.  

## Dry Run  

Input: `s = "abaccdbbd"`, `k = 3`

| iteration | start | check k? (positions) | check k+1? (positions) | count | note |
|-----------|-------|----------------------|------------------------|-------|------|
| 1 | 0 | `aba` → true | – | 1 | palindrome of length 3 found, jump to 3 |
| 2 | 3 | `c c d` → false | `c cd` → false | 1 | no palindrome, move start to 4 |
| 3 | 4 | `c d b` → false | `c db` → false | 1 | no palindrome, move start to 5 |
| 4 | 5 | `d b b` → false | `d bb` → true | 2 | length‑4 palindrome `dbbd` found, jump to 9 (end) |

The loop ends because `start = 9` and `9 + 3 > 9`. The final `count = 2`, which matches the optimal selection (`"aba"` and `"dbbd"`).

## Complexity  
- **Time:** O(n · k) in the worst case, because each call to `isPalindrome` scans at most `k+1` characters and the outer loop advances at least one position per iteration, giving ≤ n iterations.  
- **Space:** O(1) extra space; only a few integer variables are used, independent of the input size. (The output integer does not count toward auxiliary space.)

## Solution (Java)

```java
class Solution {
    public int maxPalindromes(String s, int k) {
        int count = 0;
        int start = 0;
        int length = s.length();

        while (start + k <= length) {
            if (isPalindrome(s, start, start + k - 1)) {
                start += k;
                count++;
            } else if (start + k + 1 <= length && isPalindrome(s, start, start + k)) {
                start += k + 1;
                count++;
            } else {
                start++;
            }
        }
        return count;
    }

    private boolean isPalindrome(String s, int left, int right) {
        while (left < right) {
            if (s.charAt(left) != s.charAt(right))
                return false;

            left++;
            right--;
        }
        return true;
    }
}
```

---

**Runtime** 1 ms (beats 100.0%) · **Memory** 42.8 MB (beats 79.4%)

<sub>Synced by AILeetHub on 2026-09-15.</sub>
