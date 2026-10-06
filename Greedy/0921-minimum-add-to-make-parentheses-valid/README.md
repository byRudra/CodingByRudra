# 921. Minimum Add to Make Parentheses Valid

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/minimum-add-to-make-parentheses-valid/)

`String` · `Stack` · `Greedy` · `Bracket Sequences`

## Intuition  
When scanning a parentheses string from left to right, each `'('` creates a pending opening that must later be closed, while each `')'` tries to consume one pending opening. If a closing appears when no opening is pending, it is *unmatched* and will require an extra `'('` insertion. Conversely, any openings left pending after the scan will each need a `')'`. The naive approach would be to simulate insertions or use a stack, both of which cost extra memory or extra passes. The key insight is that we only need two counters: one for currently unmatched openings (`opening`) and one for unmatched closings (`ending`). This “two‑counter” pattern captures the balance in a single linear pass.

## Approach  
1. **Initialize counters** – `opening = 0` (unmatched `'('` seen so far) and `ending = 0` (unmatched `')'` that cannot be paired).  
2. **Iterate over the string** (`i` from `0` to `s.length()‑1`).  
   - **Loop invariant:** before processing `s[i]`, `opening` equals the number of `'('` that have not yet been matched, and `ending` equals the number of `')'` that could not be matched earlier.  
3. **If the current character is `'('`** → increment `opening`. This records a new pending opening.  
4. **Else the character is `')'`** →  
   - If `opening > 0`, a pending `'('` exists; decrement `opening` to pair them.  
   - Otherwise (`opening == 0`), this `')'` has nothing to close; increment `ending` to note that one extra `'('` will be required later.  
5. **After the loop**, `opening` holds the count of unmatched `'('` that need closing, and `ending` holds the count of unmatched `')'` that need opening.  
6. **Return the sum** `ending + opening`, which is the minimal number of insertions required.

*Edge handling*:  
- Empty or single‑character strings are naturally covered because the loop runs zero or one iteration, leaving the counters correctly reflecting the needed insertions.  
- The code treats both even and odd lengths uniformly; no special parity logic is needed.  
- The condition `opening > 0` (rather than `>= 0`) prevents underflow and ensures we never decrement a counter below zero.

## Dry Run  

Input: `s = "())"`  

| i | char | opening (before) | ending (before) | action                               | opening (after) | ending (after) | note                              |
|---|------|------------------|-----------------|--------------------------------------|-----------------|----------------|-----------------------------------|
| 0 | '('  | 0                | 0               | opening++                            | 1               | 0              | new pending '('                   |
| 1 | ')'  | 1                | 0               | opening>0 → opening--                | 0               | 0              | pair with previous '('            |
| 2 | ')'  | 0                | 0               | opening==0 → ending++                | 0               | 1              | unmatched ')', need an extra '(' |

Loop ends. `opening = 0`, `ending = 1`. Return `1`, which is exactly the minimal insertion (prepend `'('`).

## Complexity  
- **Time:** O(n) – the single `for` loop visits each of the `n` characters once, and `fast`‑forward logic is replaced by constant‑time counter updates.  
- **Space:** O(1) – only two integer variables (`opening`, `ending`) are used regardless of input size; the output integer does not count toward extra space.

## Solution (Java)

```java
class Solution {
    public int minAddToMakeValid(String s) {
        int opening = 0;
        int ending = 0;

        for (int i = 0; i < s.length(); i++) {

            if (s.charAt(i) == '(') {
                opening++;
            } 
            else {
                if (opening > 0) {
                    opening--;
                } 
                else {
                    ending++;
                }
            }
        }

        return ending + opening;
    }
}
```

---

**Runtime** 0 ms (beats 100.0%) · **Memory** 43 MB (beats 22.0%)

<sub>Synced by AILeetHub on 2026-10-06.</sub>
