# 20. Valid Parentheses

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/valid-parentheses/)

`String` · `Stack` · `Bracket Sequences`

## Intuition  
The key observation is that a closing bracket must match the most recent unmatched opening bracket; therefore the sequence of opens behaves like a LIFO stack. Scanning the string once and pushing every opening character while popping on a closing character guarantees that the top of the stack always holds the bracket we need to close next. A naïve solution might scan twice—first to collect opens, then to match closes—or use a hash map to search for partners, both of which add extra passes or data structures. The single‑pass, two‑pointer‑free method described here relies on the **stack** pattern.

## Approach  
1. **Create an empty stack** `stack`.  
2. **Iterate** over each character `c` in `s.toCharArray()`.  
   - *Loop invariant*: before processing `c`, `stack` contains exactly the unmatched opening brackets that appear earlier in the string, in the order they must be closed.  
3. **If** `c` is one of `'('`, `'['`, `'{'` → `stack.push(c)`.  
   - This records a new requirement to be satisfied later.  
4. **Else** (`c` is a closing bracket):  
   a. **Guard**: if `stack.isEmpty()` return `false` because there is no opening bracket to match.  
   b. **Pop** the top element `top = stack.pop()`.  
   c. **Validate** the pair:  
      - `c == ')' && top != '('`  
      - `c == '}' && top != '{'`  
      - `c == ']' && top != '['`  
      If any condition holds, return `false`.  
   - This step ensures the most recent open matches the current close; any mismatch violates the required order.  
5. **After the loop**, return `stack.isEmpty()`.  
   - An empty stack means every opening bracket found a matching closing one; any leftover indicates an unmatched open, so the string is invalid.

Edge cases handled explicitly: an empty input never occurs because of the constraints, but a single character string triggers the `isEmpty` guard and returns `false`. The code treats even and odd lengths uniformly; the final emptiness check captures both scenarios. The `if` chain uses `||` to combine the three mismatch tests, avoiding multiple nested `if`s and guaranteeing a single return point for failure.

## Dry Run  
**Input:** `s = "([)]"`  

| Iteration | c | stack before | action | stack after | note |
|-----------|---|--------------|--------|-------------|------|
| 1 | '(' | [] | push '(' | ['('] | opening bracket |
| 2 | '[' | ['('] | push '[' | ['(','['] | opening bracket |
| 3 | ')' | ['(','['] | pop → top='['; mismatch (`')'` expects '(') | ['('] | return `false` early |

The algorithm stops at iteration 3 because the closing `')'` does not match the most recent `'['`. The early `false` correctly indicates the string is invalid.

## Complexity  
- **Time:** O(n) – the single `for` loop visits each of the `n` characters once, and each stack operation is O(1).  
- **Space:** O(n) – in the worst case (e.g., `"(((...)))"`), all `n` opening brackets are stored on the stack; the output boolean does not affect the bound.

## Solution (Java)

```java
class Solution {
    public boolean isValid(String s) {
        Stack<Character> stack = new Stack<>();
        for(char c : s.toCharArray() ){
            if(c == '(' || c == '[' || c == '{'){
                stack.push(c);
            }
            else{
                if(stack.isEmpty()){return false;}
                char top = stack.pop();

                if (( c == ')' && top != '(' ) ||
                    ( c == '}' && top != '{' ) ||
                    ( c == ']' && top != '[' ))
                    {return false;}
            }
        }
        return stack.isEmpty();
    }
}
```

---

**Runtime** 3 ms (beats 85.9%) · **Memory** 41.8 MB (beats 100.0%)

<sub>Synced by AILeetHub on 2025-08-27.</sub>
