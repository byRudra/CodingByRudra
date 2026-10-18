# 844. Backspace String Compare

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/backspace-string-compare/)

`Two Pointers` · `String` · `Stack` · `Simulation`

## Intuition  
When a backspace character `'#'` appears, it always erases the most recent ordinary character that has not already been removed. Therefore, scanning a string from left to right while maintaining a mutable “stack” of kept characters lets us obtain the final typed text in a single pass. The naïve way would be to expand each string into a new one after handling backspaces and then compare, which costs linear time but also linear auxiliary space. The key insight is that a `StringBuilder` can serve as that stack: `append` pushes a character, and `deleteCharAt(length‑1)` pops it, so we never need an explicit data structure or a second pass.

## Approach  
1. **Create builders** – `StringBuilder res1 = new StringBuilder();` and `StringBuilder res2 = new StringBuilder();` to hold the processed characters of `s` and `t`.  
2. **Process `s`** – Iterate `for (char ch : s.toCharArray())`.  
   - *Loop invariant*: `res1` contains exactly the characters of `s` processed so far after applying all backspaces.  
   - If `ch == '#'` and `res1.length() > 0`, execute `res1.deleteCharAt(res1.length() - 1)` (pop). The length check prevents under‑flow when the string starts with backspaces.  
   - Otherwise (`ch != '#'`) execute `res1.append(ch)` (push).  
3. **Process `t`** – Repeat step 2 with `t` and `res2`. The same invariant holds for `res2`.  
4. **Compare results** – Return `res1.toString().equals(res2.toString())`. Because both builders now represent the fully typed texts, a simple equality check yields the answer.  
   - Edge cases handled automatically: empty input strings produce empty builders; consecutive `'#'` characters repeatedly pop until the builder is empty; the guards `> 0` avoid `IndexOutOfBoundsException`.

## Dry Run  

**Input**  
`s = "ab#c"`  
`t = "ad#c"`

| Step | ch (s) | res1 after step | Note                     |
|------|--------|-----------------|--------------------------|
| 1    | a      | a               | append a                 |
| 2    | b      | ab              | append b                 |
| 3    | #      | a               | pop b (delete last)      |
| 4    | c      | ac              | append c                 |

`res1` ends as `"ac"`.

| Step | ch (t) | res2 after step | Note                     |
|------|--------|-----------------|--------------------------|
| 1    | a      | a               | append a                 |
| 2    | d      | ad              | append d                 |
| 3    | #      | a               | pop d (delete last)      |
| 4    | c      | ac              | append c                 |

`res2` also ends as `"ac"`, so the final comparison returns `true`.

## Complexity  
- **Time:** `O(m + n)` – each character of `s` (length `m`) and `t` (length `n`) is visited exactly once; the loops run `m` and `n` iterations respectively.  
- **Space:** `O(m + n)` – the two `StringBuilder` instances store at most all non‑deleted characters of each string; no additional data structures are allocated. (The output strings themselves are excluded from the space count.)

## Solution (Java)

```java
class Solution {
    public boolean backspaceCompare(String s, String t) {
        StringBuilder res1 = new StringBuilder();
        StringBuilder res2 = new StringBuilder();

        for (char ch : s.toCharArray()) {
            if (ch == '#') {
                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
            } else {
                res1.append(ch);
            }
        }

        for (char ch : t.toCharArray()) {
            if (ch == '#') {
                if (res2.length() > 0) {
                    res2.deleteCharAt(res2.length() - 1);
                }
            } else {
                res2.append(ch);
            }
        }
        return res1.toString().equals(res2.toString());

    }
}
```

---

**Runtime** 2 ms (beats 60.3%) · **Memory** 42.7 MB (beats 87.0%)

<sub>Synced by AILeetHub on 2026-10-18.</sub>
