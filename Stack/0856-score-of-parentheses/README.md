# 856. Score of Parentheses

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/score-of-parentheses/)

`String` · `Stack` · `Bracket Sequences`

## Intuition  
When scanning a balanced parentheses string left‑to‑right, the current nesting depth tells us exactly how much a primitive pair `()` contributes to the total score: it is worth `2^depth` where `depth` is the number of open parentheses *after* we have moved past the opening `'('`. The naïve way would be to build a tree or use a stack to combine sub‑scores, which costs extra space and a second pass. By observing that only immediate `()` pairs add a power‑of‑two amount, we can accumulate the answer in a single linear scan with two integer variables. This is the classic “depth‑based counting” pattern.

## Approach  
1. Initialise `depth = 0` and `score = 0`.  
2. Iterate `i` from `0` to `s.length()‑1`.  
   * **Loop invariant:** before processing `s[i]`, `depth` equals the number of `'('` that have been seen but not yet closed, and `score` holds the sum of contributions from all `()` pairs encountered so far.  
3. If `s.charAt(i) == '('` → increment `depth`.  
   * This records that we have entered a deeper level of nesting.  
4. Else (`s.charAt(i) == ')'`) → decrement `depth` *first*, because the current `')'` closes the most recent `'('`.  
5. After the decrement, check whether the current `')'` directly follows its matching `'('` by testing `s.charAt(i‑1) == '('`.  
   * If true, we have found a primitive `()`. Its contribution is `2^depth` (the depth *after* the closing, i.e., the depth of the surrounding context). Add `Math.pow(2, depth)` to `score`.  
   * The `<=` vs `<` issue does not arise here; the condition is exactly equality because only an immediate pair should contribute.  
6. Continue the loop until all characters are processed.  
7. Return `score`.  

Edge cases handled explicitly:  
* Empty or single‑character strings cannot occur because the constraints guarantee a balanced string of length ≥ 2.  
* For an odd‑length string the algorithm would never see a matching `')'` after `'('`, but such input is invalid per the problem statement, so no extra guard is needed.  

## Dry Run  

**Input:** `"(())()"`  

| i | char | depth (after update) | score (after possible add) | note |
|---|------|----------------------|----------------------------|------|
| 0 | '('  | 1                    | 0                          | entered first level |
| 1 | '('  | 2                    | 0                          | entered second level |
| 2 | ')'  | 1                    | 0                          | closes inner '(' but not immediate `()` |
| 3 | ')'  | 0                    | 2 (`2^0`)                  | immediate `()` at depth 0 → add 1, but depth after decrement is 0, so `2^0 = 1`; actually this is the closing of the outer pair, previous char was `')'` so no add. Wait correction: at i=3 previous char is `')'`, so no add. |
| 4 | '('  | 1                    | 2                          | new outer pair starts |
| 5 | ')'  | 0                    | 3 (`+2^0`)                 | immediate `()` → add `2^0 = 1` (total 3) |

Final `score = 3`, which equals `score("(())") + score("()") = 2 + 1 = 3`.

## Complexity  
- **Time:** O(n) – the single `for` loop visits each character once, and the power computation `Math.pow(2, depth)` is O(1) for the small depth range (`depth ≤ n`).  
- **Space:** O(1) – only two integer variables (`depth`, `score`) are used regardless of input size; the output integer is not counted as extra space.

## Solution (Java)

```java
class Solution {
    public int scoreOfParentheses(String s) {
        int depth = 0;
        int score = 0;

        for (int i = 0; i < s.length(); i++) {

            if (s.charAt(i) == '(') {
                depth++;
            } else {
                depth--;

                // Check if this ')' immediately closes '('
                if (s.charAt(i - 1) == '(') {
                    score += Math.pow(2, depth);
                }
            }
        }

        return score;
    }
}
```

---

**Runtime** 0 ms (beats 100.0%) · **Memory** 42.9 MB (beats 29.6%)

<sub>Synced by AILeetHub on 2026-10-05.</sub>
