# 1190. Reverse Substrings Between Each Pair of Parentheses

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/reverse-substrings-between-each-pair-of-parentheses/)

`String` · `Stack` · `Bracket Sequences`

## Intuition  
When we scan the string from left to right, every time we encounter an opening parenthesis we are about to start a new, deeper reversal scope, and every closing parenthesis finishes the current scope. The key observation is that the characters built **inside** the most recent '(' are exactly the substring that must be reversed before being merged back into the outer context. By keeping the outer builders on a stack we can reverse each innermost segment in O(1) extra work and immediately attach it to its parent, eliminating the need for a second pass, a hash map, or repeated substring extraction.

## Approach  
1. **Initialize** `curr` as an empty `StringBuilder` and `reverse` as an empty `Stack<StringBuilder>`.  
2. **Iterate** over each character `ch` of `s`. The loop terminates when the array is exhausted.  
   - **Invariant**: `curr` holds the characters of the current deepest (unclosed) parenthesis level, while `reverse` stores the builders of all outer levels in order of opening.  
3. **If `ch` is '('**:  
   - Push the current builder onto `reverse` (`reverse.push(curr)`).  
   - Reset `curr` to a fresh `StringBuilder` to collect the inner segment.  
   - This correctly handles empty or nested parentheses because the stack depth mirrors the nesting depth.  
4. **Else if `ch` is ')'**:  
   - Reverse the content of `curr` in place (`curr.reverse()`).  
   - Pop the outer builder (`prev = reverse.pop()`) and prepend the reversed segment (`curr.insert(0, prev)`).  
   - The `insert(0, …)` order guarantees that the reversed inner part appears before the previously accumulated outer characters, matching the required concatenation.  
5. **Else** (`ch` is a lower‑case letter):  
   - Append it to `curr` (`curr.append(ch)`).  
   - No off‑by‑one issues arise because we never skip characters; each letter is processed exactly once.  
6. After the loop, `curr` contains the fully processed string with all parentheses removed, so return `curr.toString()`.

Edge cases:  
- A string without any parentheses never pushes/pops the stack; `curr` simply accumulates all letters.  
- Single‑pair parentheses like `"()"` push an empty builder, reverse an empty builder, and then prepend the outer empty builder, yielding an empty result as expected.  
- Even vs. odd nesting depth is handled uniformly because each '(' pushes and each ')' pops exactly one builder.

## Dry Run  

Input: `(u(love)i)`

| Step | ch | stack (top→bottom) | curr after step | Note |
|------|----|--------------------|----------------|------|
| 1 | '(' | [] | "" | push empty builder, start inner |
| 2 | 'u' | ["" ] | "u" | append letter |
| 3 | '(' | ["" , "u"] | "" | push "u", start deeper |
| 4 | 'l' | ["" , "u"] | "l" | append |
| 5 | 'o' | ["" , "u"] | "lo" | append |
| 6 | 'v' | ["" , "u"] | "lov" | append |
| 7 | 'e' | ["" , "u"] | "love" | append |
| 8 | ')' | ["" ] | "evolu" | reverse "love" → "evol", prepend popped "u" → "evolu" |
| 9 | 'i' | ["" ] | "evolui" | append |
|10 | ')' | [] | "iloveu" | reverse "evolui" → "iloveu", prepend popped "" → final |

The final `curr` is `"iloveu"`, which is exactly the required output.

## Complexity  
- **Time:** O(n) – the single pass processes each character once; `reverse()` runs in linear time on the current segment, but each character participates in a reversal at most once because after reversal the segment is merged and never visited again.  
- **Space:** O(n) – the stack holds at most the number of open parentheses (≤ n) and the accumulated `StringBuilder`s together store all characters of the input. The output string itself is not counted against the auxiliary space.

## Solution (Java)

```java
class Solution {
    public String reverseParentheses(String s) {
        StringBuilder curr = new StringBuilder();
        Stack<StringBuilder> reverse = new Stack<>();
        for(char ch : s.toCharArray()){
            if(ch == '('){
                reverse.push(curr);
                curr = new StringBuilder();
            }
            else if (ch == ')'){
                curr.reverse();
                curr.insert(0, reverse.pop());
            }
            else{
                curr.append(ch);
            }
        }
        return curr.toString();
    }
}
// find (  
// then continue till ) or ( appears'
// if ( appears start again till ) appears and call a function reverse 
```

---

**Runtime** 2 ms (beats 91.6%) · **Memory** 43 MB (beats 81.1%)

<sub>Synced by AILeetHub on 2026-09-27.</sub>
