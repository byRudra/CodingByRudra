# 680. Valid Palindrome II

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/valid-palindrome-ii/)

`Two Pointers` · `String` · `Greedy`

## Intuition  
When scanning a string from both ends, the moment a mismatch appears we know that at most one character may be removed. The key observation is that after the first mismatch the only way to salvage a palindrome is to skip either the left character **or** the right character and then check the rest strictly. This eliminates the need for any extra passes, hash tables, or recursion beyond a single linear scan. The pattern used is the classic two‑pointer technique.

## Approach  
1. **Initialize pointers** `left = 0` and `right = s.length() - 1`.  
2. **Main loop** `while (left <= right)`:  
   - **Invariant**: All characters between the original `left` and `right` indices that have been examined so far are equal, i.e., `s[0..left‑1]` mirrors `s[right+1..n‑1]`.  
   - If `s.charAt(left) == s.charAt(right)`, increment `left` and decrement `right` to shrink the window.  
   - If they differ, we have exhausted the “no‑deletion” case, so we **branch**:  
     a. Call `isPalindrome(s, left + 1, right)` – pretend we delete the left character.  
     b. Call `isPalindrome(s, left, right - 1)` – pretend we delete the right character.  
     Return `true` if either branch succeeds.  
3. **Helper `isPalindrome`** receives a substring defined by `left` and `right`.  
   - Loop `while (left < right)`: if characters differ, return `false`; otherwise move both pointers inward.  
   - If the loop finishes, the examined slice is a palindrome, so return `true`.  
4. If the main loop never encounters a mismatch, the original string is already a palindrome; return `true`.

**Edge handling**:  
- Empty or single‑character strings satisfy `left <= right` immediately and return `true`.  
- The `<=` condition in the main loop ensures the middle character of an odd‑length string is compared to itself, preserving correctness.  
- The helper uses `<` because when `left == right` the single middle character is trivially symmetric.

## Dry Run  

Input: `abca`

| Iter | left | right | s[left] | s[right] | Action / Note |
|------|------|-------|---------|----------|----------------|
| 1    | 0    | 3     | a       | a        | equal → left=1, right=2 |
| 2    | 1    | 2     | b       | c        | mismatch → test two branches |
| 2a   | 2    | 2     | c       | c        | `isPalindrome(left+1,right)` succeeds |
| 2b   | 1    | 1     | b       | b        | `isPalindrome(left,right-1)` would also succeed |
| End  | –    | –     | –       | –        | At least one branch true → overall `true` |

The algorithm stops after the second iteration because the first branch confirms the remaining substring `"ca"` (after skipping `b`) is a palindrome, so the original string can become a palindrome by deleting `'b'`.

## Complexity  
- **Time:** O(n) – the main loop runs at most n/2 steps; in the worst case a single extra `isPalindrome` scan of the remaining substring adds another O(n), still linear.  
- **Space:** O(1) – only a few integer pointers are stored; the helper reuses the same variables without allocating extra data structures. (Output boolean does not affect the bound.)

## Solution (Java)

```java
class Solution {
    public boolean validPalindrome(String s) {
        int left = 0;
        int right = s.length() - 1;
        while (left <= right) {
            if (s.charAt(left) != s.charAt(right)) {
                return isPalindrome(s, left + 1, right) || isPalindrome(s, left, right - 1);
            } else {
                left++;
                right--;
            }
        }
        return true;
    }

    private boolean isPalindrome(String s, int left, int right) {
        while (left < right) {
            if (s.charAt(left) != s.charAt(right)) return false;
            left++;
            right--;
        }
        return true;
    }
}
```

---

**Runtime** 4 ms (beats 98.6%) · **Memory** 47.8 MB (beats 48.5%)

<sub>Synced by AILeetHub on 2026-09-08.</sub>
