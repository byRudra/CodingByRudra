# 1021. Remove Outermost Parentheses

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/remove-outermost-parentheses/)

`String` · `Stack` · `Bracket Sequences`

## Intuition  
The key observation is that the *nesting depth* of a parenthesis tells us whether it belongs to the outermost layer of its primitive block. While scanning the string left‑to‑right, a `'('` increases the depth and a `')'` decreases it; the first `'('` that brings depth from 0 to 1 and the matching `')'` that brings depth back to 0 are precisely the outermost pair of the current primitive. By ignoring characters when the depth is 0 or 1 for `'('` and when the depth becomes 0 after a `')'`, we drop exactly those outermost brackets. The naïve way would be to first split the string into primitives (requiring extra storage or a second pass) and then trim each piece. Tracking depth eliminates the extra pass and any auxiliary data structures.

## Approach  
1. **Initialize** `StringBuilder finalString = new StringBuilder("")` and `int depth = 0`.  
2. **Iterate** over each `char current` in `s.toCharArray()`.  
   - **Loop invariant:** before processing `current`, `depth` equals the number of unmatched `'('` seen so far in the current primitive.  
3. **If** `current == '('`  
   - **Check** `if (depth > 0) finalString.append(current);` – we append only when we are already inside a primitive (depth ≥ 1), thereby skipping the outermost opening bracket.  
   - **Update** `depth++`.  
4. **Else** (`current == ')'`)  
   - **Update** `depth--` first, because the closing bracket belongs to the current depth level.  
   - **Check** `if (depth > 0) finalString.append(current);` – we append only while the primitive is still open after this decrement, thus omitting the matching outermost closing bracket.  
5. **After the loop**, `finalString` contains the original string with every primitive’s outermost pair removed. Return `finalString.toString()`.

Edge cases are handled naturally: an empty or single‑pair primitive never appends anything because `depth` never exceeds 1, and the loop’s `depth > 0` guard prevents off‑by‑one errors at the boundaries of each primitive.

## Dry Run  

Input: `(()())`

| i | current | depth (before) | depth (after) | finalString | note |
|---|---------|----------------|---------------|-------------|------|
| 0 | '('     | 0              | 1             | ""          | outer '(' skipped |
| 1 | '('     | 1              | 2             | "("         | inner '(' kept |
| 2 | ')'     | 2 → 1          | 1             | "()"        | inner ')' kept |
| 3 | '('     | 1              | 2             | "()("       | inner '(' kept |
| 4 | ')'     | 2 → 1          | 1             | "()()"      | inner ')' kept |
| 5 | ')'     | 1 → 0          | 0             | "()()"      | outer ')' skipped |

After processing all characters, `finalString` is `"()()"`, which is the original primitive without its outermost parentheses.

## Complexity  
- **Time:** O(n) – the single pass visits each of the `n` characters once, and `depth` updates are O(1).  
- **Space:** O(n) – `finalString` stores at most `n‑2k` characters (where `k` is the number of primitives); no additional data structures are used beyond the output buffer.

## Solution (Java)

```java
class Solution {
    public String removeOuterParentheses(String s) {
        StringBuilder finalString = new StringBuilder("");
        int depth = 0;
        for (char current : s.toCharArray()) {
            if (current == '(') {
                if (depth > 0)
                    finalString.append(current);
                depth++;
            } else {
                depth--;
                if (depth > 0)
                    finalString.append(current);
            }
        }

        return finalString.toString();
    }
}
```

---

**Runtime** 2 ms (beats 99.8%) · **Memory** 43.9 MB (beats 20.9%)

<sub>Synced by AILeetHub on 2026-10-08.</sub>
