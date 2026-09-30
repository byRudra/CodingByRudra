# 1111. Maximum Nesting Depth of Two Valid Parentheses Strings

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/maximum-nesting-depth-of-two-valid-parentheses-strings/)

`String` · `Stack` · `Bracket Sequences`

## Intuition  
The key observation is that the current nesting depth of a prefix completely determines which subsequence a parenthesis should belong to: if we assign every opening parenthesis to group `depth % 2` and close it to the same group, the two groups will inherit alternating layers of the original depth. Consequently the maximum depth of each group is at most ⌈overallDepth/2⌉, which is the smallest possible value. A naïve solution would first compute the whole depth profile, then try to balance the two strings with extra passes or a hash‑map; the parity insight eliminates all that extra work. This is an instance of the **parity‑based greedy split** pattern.

## Approach  
1. **Initialisation** – Create `ans`, an integer array of length `seq.length()`. Set `depth = 0`.  
2. **Iterate** – Loop `i` from `0` to `seq.length()‑1` (`i < seq.length()` is the exit condition). The invariant after each iteration is:  
   *`depth` equals the nesting depth of the processed prefix `seq[0..i]`.*  
3. **Handle '('** – When `ch == '('`:
   * Increment `depth` (`depth++`).  
   * Assign `ans[i] = depth % 2`. The parity after the increment decides the group, ensuring the opening parenthesis and its matching closing parenthesis will share the same group.  
4. **Handle ')'** – When `ch == ')'`:
   * Assign `ans[i] = depth % 2` **before** decreasing `depth`. This mirrors the assignment of its matching '(' because `depth` still reflects the layer that the pair belongs to.  
   * Decrement `depth` (`depth--`).  
5. **Return** – After the loop finishes, `ans` encodes a valid split; return it.

**Edge‑case decisions**  
* An empty string never reaches the loop, returning an empty array – trivially correct.  
* For a single pair `"()"`, `depth` goes 1→0, producing `[1,1]` (or `[0,0]` depending on parity) which is still a valid split.  
* The code uses `<=` vs `<` only in the loop condition (`i < seq.length()`); the parity check uses `% 2`, which works for both even and odd overall depths without overflow concerns because `depth` never exceeds `seq.length()` (≤ 10⁴).

## Dry Run  
Input: `seq = "(()())"`  

| i | ch | depth (after update) | ans[i] | note |
|---|----|----------------------|--------|------|
| 0 | '(' | 1 | 1 | `depth++`, assign to group 1 |
| 1 | '(' | 2 | 0 | `depth++`, assign to group 0 |
| 2 | ')' | 2 → 1 | 0 | assign before `depth--` |
| 3 | '(' | 2 | 0 | `depth++`, assign to group 0 |
| 4 | ')' | 2 → 1 | 0 | assign before `depth--` |
| 5 | ')' | 1 → 0 | 1 | assign before `depth--` |

Final `ans = [1,0,0,0,0,1]`. Groups 0 and 1 each form a valid parentheses string, and the deepest layer (depth 2) is split between them, giving a maximal depth of 1 for both – the minimal possible value.

## Complexity  
- **Time:** O(n) – the single `for` loop visits each character once; `depth` advances two steps for every pair, so the loop runs `n` times.  
- **Space:** O(n) – the output array `ans` stores one integer per character; no additional data structures are used. (The space for the result is not counted against auxiliary space.)

## Solution (Java)

```java
class Solution {
    public int[] maxDepthAfterSplit(String seq) {
             int[] ans = new int[seq.length()];
        int depth = 0;

        for (int i = 0; i < seq.length(); i++) {
            char ch = seq.charAt(i);

            if (ch == '(') {
                depth++;
                ans[i] = depth % 2;
            } else {
                ans[i] = depth % 2;
                depth--;
            }
        }

        return ans;   
    }
}
```

---

**Runtime** 2 ms (beats 57.9%) · **Memory** 45.1 MB (beats 91.1%)

<sub>Synced by AILeetHub on 2026-09-30.</sub>
