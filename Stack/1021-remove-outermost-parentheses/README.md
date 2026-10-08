# 1021. Remove Outermost Parentheses

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/remove-outermost-parentheses/)

`String` · `Stack` · `Bracket Sequences`

## Intuition  
When scanning a valid parentheses string, the current nesting **depth** tells us exactly whether a parenthesis belongs to the outermost layer of its primitive component. The first '(' that raises the depth from 0 to 1 and the matching ')' that brings it back to 0 are the outermost pair and must be omitted. All other characters appear while the depth is ≥ 1, so they belong to the interior and can be kept. A naïve solution might first split the string into primitives (requiring extra passes or a stack) and then trim each piece, but tracking depth alone eliminates any additional data structures or passes. This is the classic **two‑pointer / depth‑counter** pattern for bracket processing.

## Approach  
1. **Initialize** an empty `StringBuilder finalString` and set `depth = 0`.  
2. **Iterate** over each `current` character of `s.toCharArray()`. The loop exits when the array is exhausted; the invariant is that `depth` equals the number of unmatched '(' seen so far.  
3. **If** `current == '('`:  
   - *Invariant*: before increment, `depth` reflects the nesting of the previous character.  
   - **Decision**: append `current` only when `depth > 0` (i.e., we are already inside a primitive).  
   - Increment `depth`. This moves the invariant forward: after the increment, `depth` counts the newly opened '(' as well.  
4. **Else** (`current == ')'`):  
   - Decrement `depth` first, because the matching '(' for this ')' has already been counted.  
   - *Invariant*: after decrement, `depth` is the nesting level *after* closing this parenthesis.  
   - **Decision**: append `current` only when the new `depth > 0`, meaning the closing parenthesis is not the outermost one of its primitive.  
5. **Return** `finalString.toString()`. The loop guarantees that every character belonging to an outermost pair was skipped, while all interior characters were collected.

**Edge considerations**:  
- Empty or single‑character inputs cannot occur because the problem guarantees a valid non‑empty string.  
- For an even‑length string consisting solely of primitive `"()"` pairs, `depth` never exceeds 1, so no character is appended, yielding an empty result.  
- The `<=` vs `<` check is unnecessary; the code uses `depth > 0` after the increment/decrement, which correctly excludes the outermost pair without off‑by‑one errors.  

## Dry Run  

Input: `(()())`

| Step | `current` | `depth` (before) | Action (append?) | `depth` (after) | finalString | Note |
|------|-----------|------------------|------------------|-----------------|-------------|------|
| 1    | '('       | 0                | no (depth==0)    | 1               | ""          | opening outermost |
| 2    | '('       | 1                | yes              | 2               | "("         | interior start |
| 3    | ')'       | 2 → 1            | yes (depth>0)    | 1               | "()"        | interior close |
| 4    | '('       | 1                | yes              | 2               | "()("       | new interior |
| 5    | ')'       | 2 → 1            | yes              | 1               | "()()"      | interior close |
| 6    | ')'       | 1 → 0            | no (depth==0)    | 0               | "()()"      | closing outermost |

After processing all characters, `finalString` is `"()()"`, which is exactly the original string with the outermost parentheses of its single primitive removed.

## Complexity  
- **Time:** O(n) – the single `for` loop visits each of the `n` characters once, and `depth` updates are O(1) per iteration.  
- **Space:** O(n) – `finalString` stores at most `n‑2k` characters (where `k` is the number of primitives), which is linear in the input size; no auxiliary data structures are used beyond the output buffer.

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

**Runtime** 2 ms (beats 99.8%) · **Memory** 43.6 MB (beats 42.5%)

<sub>Synced by AILeetHub on 2026-01-07.</sub>
