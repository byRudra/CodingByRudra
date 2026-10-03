# 32. Longest Valid Parentheses

![Hard](https://img.shields.io/badge/Difficulty-Hard-ff375f?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/longest-valid-parentheses/)

`String` · `Dynamic Programming` · `Stack` · `Bracket Sequences`

## Intuition  
When we scan a parentheses string from left to right, the number of ‘)’ that have appeared can never exceed the number of ‘(’ in a *valid* prefix. If it does, any substring that starts before this excess cannot be part of the longest valid block, so we can safely discard everything seen so far and restart counting. The same reasoning holds in reverse: scanning from right to left guarantees that a surplus of ‘(’ cannot belong to a valid suffix. By performing both passes we capture the longest well‑formed segment regardless of whether the excess occurs on the left or on the right. This eliminates the need for extra memory (e.g., a stack) or a second full pass to compute lengths.

## Approach  
1. **Initialize counters** `left = 0`, `right = 0`, `max = 0`.  
2. **Left‑to‑right pass** (`i` from `0` to `s.length()-1`):  
   - If `s.charAt(i) == '('` increment `left`; else increment `right`.  
   - **Invariant:** `left` and `right` count the numbers of '(' and ')' seen since the last reset.  
   - If `left == right`, a balanced substring of length `2*left` ends at `i`; update `max`.  
   - If `right > left`, the current prefix cannot become valid; reset both counters to `0`.  
3. **Reset counters** `left = 0`, `right = 0` before the second scan.  
4. **Right‑to‑left pass** (`i` from `s.length()-1` down to `0`):  
   - Same update rules, but now a surplus of '(' (`left > right`) forces a reset because a valid suffix cannot start before this point.  
   - When `left == right` we again have a balanced block and possibly improve `max`.  
5. **Return** `max`, the length of the longest balanced substring found in either direction.

**Edge‑case handling** –  
- Empty or single‑character strings never satisfy `left == right`, so `max` stays `0`.  
- The reset condition uses `>` (or `<` in the reverse pass) rather than `>=` because equality already yields a candidate length; only a strict excess invalidates the current window.  
- The algorithm chooses the convention “reset both counters” instead of sliding a window because it keeps the code O(1) space and matches the invariant that a balanced block must start after the last imbalance.

## Dry Run  

Input: `")()())"`  

| i (L→R) | char | left | right | max | note |
|--------|------|------|-------|-----|------|
| 0 | `)` | 0 | 1 | 0 | `right > left` → reset |
| 1 | `(` | 1 | 0 | 0 | counters start anew |
| 2 | `)` | 1 | 1 | 2 | balanced → `max = 2` |
| 3 | `(` | 2 | 1 | 2 | – |
| 4 | `)` | 2 | 2 | 4 | balanced → `max = 4` |
| 5 | `)` | 2 | 3 | 4 | `right > left` → reset |

After the forward pass `max = 4`.  
The backward pass (right→left) never finds a longer balanced segment, so the final answer is `4`, which corresponds to the substring "`()()`".

## Complexity  
- **Time:** `O(n)` – each character is visited twice (once forward, once backward), and the counters move at most `n/2` steps per pass because `fast`‑like advances are simulated by the `right` counter.  
- **Space:** `O(1)` – only a few integer variables are used; the output integer does not count toward extra space.

## Solution (Java)

```java
class Solution {
    public int longestValidParentheses(String s) {
        int left = 0;
        int right = 0;
        int max = 0;

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(')
                left++;
            else
                right++;
            if (left == right)
                max = Math.max(max, left * 2);
            else if (right > left) {
                right = 0;
                left = 0;
            }
        }

        left = 0;
        right = 0;
        for(int i = s.length() - 1; i >= 0; i--){
            if (s.charAt(i) == '(')
                left++;
            else
                right++;
            if (left == right)
                max = Math.max(max, left * 2);
            else if (left > right) {
                right = 0;
                left = 0;
            }
        }
        return max;
    }
}
```

---

**Runtime** 2 ms (beats 96.1%) · **Memory** 44.1 MB (beats 99.4%)

<sub>Synced by AILeetHub on 2026-10-03.</sub>
