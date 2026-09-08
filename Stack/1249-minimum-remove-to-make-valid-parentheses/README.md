# 1249. Minimum Remove to Make Valid Parentheses

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/minimum-remove-to-make-valid-parentheses/)

`String` · `Stack`

## Intuition  
The only thing that can make a parenthesis string invalid is a mismatched ‘)’ that appears before a matching ‘(’, or a leftover ‘(’ that never finds a partner. If we walk the string left‑to‑right and keep the indices of unmatched ‘(’ in a stack, every time we see a ‘)’ we can instantly decide whether it pairs (pop the stack) or must be deleted (mark it). After the scan the stack holds exactly the indices of ‘(’ that never got matched, which we also mark for removal. This single pass replaces the naïve two‑pass or hash‑map approaches that would require extra bookkeeping.

## Approach  
1. **Initialize** a `Stack<Integer> stack` and a boolean array `remove` of length `s.length()`.  
2. **First pass (i = 0 … n‑1):**  
   - Read `ch = s.charAt(i)`.  
   - If `ch == '('` → `stack.push(i)`.  
   - Else if `ch == ')'` →  
     *If `stack` is not empty* → `stack.pop()` (the current ‘)’ pairs with the most recent unmatched ‘(’).  
     *Otherwise* → `remove[i] = true` (this ‘)’ cannot be matched, so it must be deleted).  
   - The loop exits when `i == n`. Invariant: after processing position `i‑1`, `stack` contains exactly the indices of ‘(’ that have not yet found a matching ‘)’ among the first `i` characters, and `remove` is true precisely for the ‘)’ that are known to be invalid.  
3. **Second pass (clean up unmatched ‘(’):** While `!stack.isEmpty()` pop an index `idx` and set `remove[idx] = true`. After this, `remove[k]` is true for every character that must be discarded.  
4. **Third pass (build answer):** Iterate `i = 0 … n‑1`; if `remove[i]` is false, append `s.charAt(i)` to a `StringBuilder ans`. Finally return `ans.toString()`.  
   - Edge cases: an empty or single‑character string never enters the stack logic; an all‑parentheses string like `"))(("` ends with the stack empty after step 3, producing an empty result. The code uses `<=` only for the outer `for` loop’s bound (`i < s.length()`), which is the correct exclusive upper bound for array indexing.

## Dry Run  
Input: `a)b(c)d`

| i | ch | stack (top→bottom) | remove[i] | Action / Note |
|---|----|--------------------|-----------|----------------|
|0|a|[]|false|non‑parenthesis, nothing changes|
|1|)|[]|true|stack empty → mark this ‘)’ for removal|
|2|b|[]|false|non‑parenthesis|
|3|(|[3]|false|push index of ‘(’|
|4|c|[3]|false|non‑parenthesis|
|5|)|[]|false|stack not empty → pop 3, ‘)’ matched|
|6|d|[]|false|non‑parenthesis|

After the first pass the stack is empty, so the cleanup loop does nothing. The final builder skips only index 1, yielding `"ab(c)d"`.

## Complexity  
- **Time:** O(n) – the three linear scans each traverse the string at most once; the stack operations are O(1) per character.  
- **Space:** O(n) – the boolean `remove` array stores a flag per character and the stack holds at most all ‘(’ indices (worst‑case n/2), both proportional to the input size. The output `StringBuilder` is not counted in the extra‑space budget.

## Solution (Java)

```java
class Solution {
    public String minRemoveToMakeValid(String s) {
        Stack<Integer> stack = new Stack<>();
        boolean[] remove = new boolean[s.length()];

        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);

            if (ch == '(') {
                stack.push(i);
            } else if (ch == ')') {
                if (!stack.isEmpty()) {
                    stack.pop();
                } else {
                    remove[i] = true;
                }
            }
        }

        // Remaining  (  in the stack
        while (!stack.isEmpty())
            remove[stack.pop()] = true;

        StringBuilder ans = new StringBuilder();

        for (int i = 0; i < s.length(); i++) {
            if (remove[i])
                continue;
            ans.append(s.charAt(i));
        }
        return ans.toString();
    }
}
```

---

**Runtime** 20 ms (beats 48.1%) · **Memory** 47.4 MB (beats 51.6%)

<sub>Synced by AILeetHub on 2026-09-08.</sub>
