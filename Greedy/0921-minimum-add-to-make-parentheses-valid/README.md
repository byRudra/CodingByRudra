# 921. Minimum Add to Make Parentheses Valid

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/minimum-add-to-make-parentheses-valid/)

`String` · `Stack` · `Greedy` · `Bracket Sequences`

## Intuition  
When scanning a parentheses string left‑to‑right, every opening `'('` can potentially match a later closing `')'`. The key observation is that the number of unmatched openings seen so far (`opening`) tells us exactly how many future `')'` can be paired without any insertions. Conversely, whenever we encounter a `')'` while `opening` is zero, that `')'` has nothing to match, so we must insert a `'('` before it. This single pass eliminates the need for a second traversal, a stack, or any extra data structure.

## Approach  
1. **Initialize counters** – `opening = 0` (unmatched `'('` seen) and `ans = 0` (insertions needed for unmatched `')'`).  
2. **Iterate** over the string with `for (int i = 0; i < s.length(); i++)`.  
   - *Exit condition*: loop stops when `i == s.length()`.  
   - *Invariant*: before each iteration, `opening` equals the count of `'('` that have not yet been paired, and `ans` equals the number of `'('` we have hypothetically inserted to balance earlier stray `')'`.  
3. **Process current character** `c = s.charAt(i)`.  
   - If `c == '('`, increment `opening` because we have one more potential match.  
   - Else (`c == ')'`):  
     - If `opening > 0`, decrement `opening` – we pair this `')'` with a previous `'('`.  
     - Otherwise (`opening == 0`), increment `ans` – we must insert a `'('` before this `')'`.  
   - This branch choice (`> 0` vs `== 0`) avoids off‑by‑one errors: we never allow `opening` to become negative, which would incorrectly suggest a match that does not exist.  
4. **After the loop**, any remaining `opening` are unmatched `'('` that need closing `')'`. Return `ans + opening`.  
   - The code chooses to add the two counters at the end rather than inserting during the scan, which keeps the loop simple and guarantees O(1) extra space.

## Dry Run  

Input: `s = "())"`  

| i | c | opening (before) | ans (before) | change | opening (after) | ans (after) | note |
|---|---|------------------|--------------|--------|-----------------|-------------|------|
| 0 | '(' | 0 | 0 | +1 | 1 | 0 | see an opening, increase `opening` |
| 1 | ')' | 1 | 0 | -1 | 0 | 0 | match with previous `'('` |
| 2 | ')' | 0 | 0 | +1 | 0 | 1 | no `'('` to match, need an insertion (`ans++`) |
| – | – | – | – | – | 0 | 1 | loop ends; `opening` is 0, `ans` is 1 |

Final state: `ans + opening = 1`. One insertion (a `'('` before the last `')'`) makes the string valid.

## Complexity  
- **Time:** O(n) – the single `for` loop visits each character exactly once (`i` advances from 0 to `s.length() - 1`).  
- **Space:** O(1) – only two integer counters (`opening`, `ans`) are used, independent of input size. The output integer is not counted as extra space.

## Solution (Java)

```java
class Solution {
    public int minAddToMakeValid(String s) {
        int opening = 0;
        int ans = 0;

        for (int i = 0; i < s.length(); i++) {

            if (s.charAt(i) == '(') {
                opening++;
            } 
            else {
                if (opening > 0) {
                    opening--;
                } 
                else {
                    ans++;
                }
            }
        }

        return ans + opening;
    }
}
```

---

**Runtime** 0 ms (beats 100.0%) · **Memory** 43 MB (beats 39.7%)

<sub>Synced by AILeetHub on 2026-08-18.</sub>
