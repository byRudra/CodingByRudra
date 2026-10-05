# 2390. Removing Stars From a String

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/removing-stars-from-a-string/)

`String` · `Stack` · `Simulation`

## Intuition  
When we scan the string from left to right, every `*` erases the most recent non‑star character that has not been erased yet. This means the characters that survive form a **last‑in‑first‑out** sequence: the newest character is the first one that can be removed by a later star. A single pass with a LIFO container therefore eliminates the need for a second pass, a hash map, or any explicit back‑tracking. The pattern used here is the classic *stack* simulation.

## Approach  
1. **Initialize** an empty `StringBuilder sb` which will act as a mutable stack.  
2. **Iterate** over each `char ch` in `s.toCharArray()`.  
   - **Loop condition:** runs until the end of the character array; the invariant is that `sb` contains exactly the characters that remain after processing the prefix seen so far.  
3. **If** `ch == '*'`  
   - **Action:** `sb.setLength(sb.length() - 1)` removes the top element of the stack, i.e., the closest left non‑star character. The code relies on the problem guarantee that a star never appears when the stack is empty, so no extra guard is needed.  
4. **Else** (`ch` is a lowercase letter)  
   - **Action:** `sb.append(ch)` pushes the character onto the stack.  
5. **After the loop**, `sb` holds the final characters in their original order because we never reorder the stack; we only append or pop the most recent element.  
6. **Return** `sb.toString()`, converting the mutable buffer to the required immutable result.

*Edge considerations*  
- An empty or single‑character input never triggers the `*` branch, so `sb` simply accumulates the characters.  
- For an even number of stars the stack may become empty multiple times; `setLength` safely reduces the length to zero.  
- The implementation chooses `StringBuilder` over `java.util.Stack` to avoid the overhead of boxing `Character` objects and to achieve O(1) amortized push/pop via length adjustments.

## Dry Run  

**Input:** `leet**cod*e`

| Iteration | ch | sb before            | sb after               | Note                              |
|-----------|----|----------------------|------------------------|-----------------------------------|
| 1         | l  | ""                   | "l"                    | push letter                       |
| 2         | e  | "l"                  | "le"                   | push                              |
| 3         | e  | "le"                 | "lee"                  | push                              |
| 4         | t  | "lee"                | "leet"                 | push                              |
| 5         | *  | "leet"               | "lee"                  | pop (removes 't')                 |
| 6         | *  | "lee"                | "le"                   | pop (removes 'e')                 |
| 7         | c  | "le"                 | "lec"                  | push                              |
| 8         | o  | "lec"                | "leco"                 | push                              |
| 9         | d  | "leco"               | "lecod"                | push                              |
|10         | *  | "lecod"              | "leco"                 | pop (removes 'd')                 |
|11         | e  | "leco"               | "lecoe"                | push                              |

Final `sb` = `"lecoe"`, which is exactly the required result because every star has removed its nearest left survivor.

## Complexity  
- **Time:** O(n) – the single pass processes each character once, and each `setLength` or `append` is O(1).  
- **Space:** O(n) – in the worst case (no stars) the `StringBuilder` stores all n characters; the extra space does not include the output string itself because `sb` *is* the output buffer.

## Solution (Java)

```java
// class Solution {
//     public String removeStars(String s) {
//         Stack<Character> stack = new Stack<>();

//         for(char ch : s.toCharArray()){
//             if(ch != '*'){
//                 stack.push(ch);
//             }
//             else{
//                 if(!stack.isEmpty()) stack.pop();
//             }
//         }

//         StringBuilder ans = new StringBuilder();

//         for (char ch : stack) {
//             ans.append(ch);
//         }

//         return ans.toString();
//     }
// }

class Solution {
    public String removeStars(String s) {
        StringBuilder sb = new StringBuilder();
        for(char ch : s.toCharArray()){
            if(ch == '*')
                sb.setLength(sb.length() - 1);
            else
                sb.append(ch);
        }
        return sb.toString();

    }
}
```

---

**Runtime** 22 ms (beats 94.3%) · **Memory** 48.2 MB (beats 52.8%)

<sub>Synced by AILeetHub on 2026-10-05.</sub>
