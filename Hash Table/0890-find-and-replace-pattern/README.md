# 890. Find and Replace Pattern

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/find-and-replace-pattern/)

`Array` · `Hash Table` · `String`

## Intuition  
The core observation is that two strings follow the same pattern iff the *relative* order of first appearances of their characters is identical. By recording, for each character, the index (plus one) of its most recent occurrence, we obtain a sequence of integers that is invariant under any bijective letter substitution. Comparing these integer sequences for the word and the pattern tells us instantly whether a bijection exists, eliminating the need for an explicit map, a second pass, or back‑tracking.

## Approach  
1. **Iterate over each candidate word** in `words`.  
2. **Invoke `matches(word, pattern)`** to decide inclusion.  
3. Inside `matches`:  
   - Initialise two length‑26 int arrays `w` and `p` to zero. `w[a]` stores the last position + 1 where letter `a` appeared in `word`; `p[b]` does the same for `pattern`.  
   - Loop `i` from `0` to `word.length()‑1` (the exit condition is `i == word.length()`).  
   - Compute `a = word.charAt(i) - 'a'` and `b = pattern.charAt(i) - 'a'`.  
   - **Invariant:** before each iteration, `w[x]` and `p[y]` hold the most recent 1‑based index where `x` (in `word`) and `y` (in `pattern`) were seen, or `0` if never seen.  
   - If `w[a] != p[b]`, the two strings have diverging histories of first appearances, so return `false`.  
   - Otherwise set `w[a] = p[b] = i + 1` to record the current position for both characters.  
4. After the loop finishes without a mismatch, return `true`.  
5. Back in the outer loop, add `word` to `res` when `matches` returns `true`.  
6. Return `res` after all words are processed.

**Edge handling:**  
- Empty or single‑character inputs are naturally handled because the loop runs zero or one iteration and the arrays start at zero, satisfying the invariant.  
- The algorithm treats even and odd lengths uniformly; no special case is needed.  
- Using `i + 1` (instead of `i`) avoids the ambiguity between “never seen” (`0`) and “seen at index 0”.  

## Dry Run  
Input: `words = ["mee","aqq","abc"]`, `pattern = "abb"`  

| i | a (word char) | b (pattern char) | w[a] before | p[b] before | Comparison (`w[a] != p[b]`) | Action (`w[a]=p[b]=i+1`) | Note |
|---|---------------|------------------|------------|------------|-----------------------------|--------------------------|------|
| 0 | m (12)        | a (0)            | 0          | 0          | false                       | w[12]=p[0]=1            | first occurrence of both |
| 1 | e (4)         | b (1)            | 0          | 0          | false                       | w[4]=p[1]=2             | second distinct letters |
| 2 | e (4)         | b (1)            | 2          | 2          | false                       | w[4]=p[1]=3             | repeated pattern letter matches repeated word letter |
Loop ends → `matches` returns `true`; “mee” is added. Repeating the same steps for “aqq” yields `true`; “abc” fails at i = 1 because `w['b']` (0) ≠ `p['b']` (2). Final result: `["mee","aqq"]`.

## Complexity  
- **Time:** O(N · L) where N is `words.length` and L is `pattern.length`. Each word is scanned once, and the inner loop runs L times, updating constant‑size arrays.  
- **Space:** O(1) extra space. The two int arrays have fixed size 26 regardless of input size; the output list is not counted toward auxiliary space.

## Solution (Java)

```java
class Solution {
    public List<String> findAndReplacePattern(String[] words, String pattern) {
                List<String> res = new ArrayList<>();
        for (String word : words) {
            if (matches(word, pattern)) res.add(word);
        }
        return res;
    }

    private boolean matches(String word, String pattern) {
        int[] w = new int[26]; // last position (+1) each word letter was seen
        int[] p = new int[26]; // last position (+1) each pattern letter was seen

        for (int i = 0; i < word.length(); i++) {
            int a = word.charAt(i) - 'a';
            int b = pattern.charAt(i) - 'a';

            if (w[a] != p[b]) return false;

            w[a] = p[b] = i + 1;
        }
        return true;
    }
}
```

---

**Runtime** 0 ms (beats 100.0%) · **Memory** 43.6 MB (beats 78.8%)

<sub>Synced by AILeetHub on 2026-09-30.</sub>
