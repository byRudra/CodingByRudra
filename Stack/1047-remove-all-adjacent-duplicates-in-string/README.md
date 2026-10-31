# 1047. Remove All Adjacent Duplicates In String

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/remove-all-adjacent-duplicates-in-string/)

`String` · `Stack`

## Intuition  
If we scan the string from left to right while maintaining the already‑processed prefix without any adjacent duplicates, the next character either cancially removes the last character of that prefix (when they are equal) or becomes the new tail of the prefix (when they differ). This invariant – “the stack always stores a prefix that cannot be reduced further” – lets us decide locally without ever looking back more than one position. A naïve solution would repeatedly scan the whole string for a pair, costing O(n²) or would use recursion to rebuild the string. The stack observation eliminates the extra passes and the need for a hash map; it is simply a **two‑pointer/stack** pattern.

## Approach  
1. **Initialisation** – Create an empty `Stack<Character> stack`.  
2. **Iterate over characters** – `for (char ch : s.toCharArray())`.  
   * **Loop invariant**: before each iteration, `stack` holds the shortest possible prefix of the processed part of `s` that contains no adjacent equal letters.  
   * **Decision**:  
     * If `stack` is non‑empty **and** `stack.peek() == ch`, then `stack.pop();` – the current character cancels the top because they form an adjacent duplicate.  
     * Otherwise `stack.push(ch);` – the character cannot be eliminated now, so it becomes the new tail of the prefix.  
   * The loop terminates when every character of `s` has been examined; at that point the invariant guarantees that `stack` is exactly the final reduced string.  
3. **Build the answer** – Initialise `StringBuilder sb`. Iterate over `stack` (which preserves insertion order) and `sb.append(ch)` for each element. Finally return `sb.toString()`.  
4. **Edge handling** – The code works for empty or single‑character strings because the loop simply never triggers a pop. The `<=` vs `<` issue never appears; the check `!stack.isEmpty()` guarantees a safe `peek`. The choice of `Stack` (LIFO) matches the need to compare only the most recent survivor with the incoming character.

## Dry Run  

Input: `abbaca`

| step | ch | stack (bottom → top) | note |
|------|----|----------------------|------|
| 1 | a | a | stack empty → push |
| 2 | b | a b | top ≠ b → push |
| 3 | b | a | top == b → pop (removes previous b) |
| 4 | a | (empty) | top == a → pop (removes a) |
| 5 | c | c | stack empty → push |
|  | a | c a | top ≠ a → push |

After processing all characters the stack holds `c a`, which the builder concatenates to `"ca"`. This matches the required result because every possible adjacent duplicate has been eliminated during the scan.

## Complexity  
- **Time:** O(n) – each character is examined once and each stack operation is O(1); the final traversal of the stack is another O(n).  
- **Space:** O(n) – in the worst case (e.g., `"abcdef"`), no character is removed and the stack stores all n characters; the `StringBuilder` does not affect the asymptotic bound.

## Solution (Java)

```java
class Solution {
    public String removeDuplicates(String s) {
        Stack<Character> stack = new Stack<>();

        for (char ch : s.toCharArray()) {
            if (!stack.isEmpty() && stack.peek() == ch) {
                stack.pop();
            } else {
                stack.push(ch);
            }
        }

        StringBuilder sb = new StringBuilder();
        for (char ch : stack) {
            sb.append(ch);
        }
        return sb.toString();
    }
}
```

---

**Runtime** 26 ms (beats 51.4%) · **Memory** 46.6 MB (beats 82.1%)

<sub>Synced by AILeetHub on 2026-10-31.</sub>
