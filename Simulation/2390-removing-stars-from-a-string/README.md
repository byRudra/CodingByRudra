# 2390. Removing Stars From a String

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/removing-stars-from-a-string/)

`String` · `Stack` · `Simulation`

## Intuition  
The key observation is that each `*` erases the most recent non‑star character that has not been removed yet. If we process the string from left to right and keep a structure that always gives us the last kept character, we can apply every deletion instantly without a second pass. A naïve solution might scan for a `*`, then search leftwards for the nearest letter, resulting in O(n²) time. By maintaining a stack of characters, the leftmost undeleted character is always on top, turning the whole process into a single linear scan. This is the classic **two‑pointer / stack simulation** pattern for “undo‑the‑last‑action” problems.

## Approach  
1. **Initialize** an empty `Stack<Character>` called `stack`.  
2. **Iterate** over each `ch` in `s.toCharArray()`.  
   - **Loop exit condition:** the loop ends after the last character of `s` is processed.  
   - **Invariant:** before processing `ch`, `stack` contains exactly the characters of the prefix processed so far after all deletions triggered by stars in that prefix.  
   - **If** `ch` is not `'*'`, execute `stack.push(ch)`. This records a new candidate for future deletion.  
   - **Else** (`ch == '*'`), the problem guarantees a deletable character exists, so we safely call `stack.pop()` (guarded by `!stack.isEmpty()` to handle edge cases like a leading star, though such input never occurs). This removes the closest left‑hand non‑star character.  
3. **Build the answer**: create a `StringBuilder ans`.  
   - **Loop** over the elements of `stack` in their natural order (which is the order they were pushed).  
   - **Invariant:** after each iteration, `ans` holds the concatenation of all characters seen so far in `stack`.  
   - Append each `ch` to `ans`.  
4. **Return** `ans.toString()`. The stack now represents the final string after all star operations.

Edge‑case handling:  
- Empty or single‑character strings are covered because the for‑loop simply never pushes a star without a preceding character.  
- Even vs. odd length does not matter; the stack size automatically reflects the net number of non‑star characters remaining.  
- The `<=` vs `<` discussion is irrelevant here because we never compare indices; we rely on the stack’s emptiness check.

## Dry Run  

Input: `s = "ab*c*"`  

| Iteration | ch processed | stack (top→bottom) | Action taken | Note |
|-----------|--------------|--------------------|--------------|------|
| 1 | 'a' | a | push | first letter kept |
| 2 | 'b' | b, a | push | second letter kept |
| 3 | '*' | a | pop | removes 'b' (closest left) |
| 4 | 'c' | c, a | push | new letter added |
| 5 | '*' | a | pop | removes 'c' |

Final stack content: `a`. The algorithm returns `"a"`, which matches the expected result after removing each star’s left neighbor.

## Complexity  
- **Time:** O(n) – the first loop visits each character once, and the second loop traverses the stack whose size is at most n.  
- **Space:** O(n) – in the worst case (no stars) the stack stores all n characters; the `StringBuilder` reuses this data, so additional auxiliary space is constant.

## Solution (Java)

```java
class Solution {
    public String removeStars(String s) {
        Stack<Character> stack = new Stack<>();

        for(char ch : s.toCharArray()){
            if(ch != '*'){
                stack.push(ch);
            }
            else{
                if(!stack.isEmpty()) stack.pop();
            }
        }

        StringBuilder ans = new StringBuilder();

        for (char ch : stack) {
            ans.append(ch);
        }

        return ans.toString();
    }
}
```

---

**Runtime** 71 ms (beats 45.0%) · **Memory** 48.1 MB (beats 52.8%)

<sub>Synced by AILeetHub on 2026-10-05.</sub>
