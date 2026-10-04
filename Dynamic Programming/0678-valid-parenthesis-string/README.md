# 678. Valid Parenthesis String

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/valid-parenthesis-string/)

`String` · `Dynamic Programming` · `Stack` · `Greedy` · `Bracket Sequences`

## Intuition  
The key observation is that after scanning any prefix of the string, the exact number of unmatched left parentheses is not fixed – it can lie anywhere between a **minimum** and a **maximum** count, depending on how we decide to treat each ‘*’. If we maintain these two bounds, we can decide validity on‑the‑fly: the minimum bound never drops below zero (otherwise too many ‘)’ have appeared), and the maximum bound never becomes negative (otherwise even the most optimistic interpretation cannot balance the parentheses). This single pass eliminates the need for a second traversal, a DP table, or exponential backtracking. The technique is a greedy interval‑tracking method.

## Approach  
1. **Initialize** `leftMin = 0` and `leftMax = 0`.  
2. **Iterate** over each character `ch` of `s` (`for(char ch : s.toCharArray())`).  
   - **Loop invariant**: after processing the first *i* characters, `leftMin` is the smallest possible number of unmatched ‘(’, and `leftMax` is the largest possible number of unmatched ‘(’. Both values are non‑negative.  
3. **Update bounds** according to `ch`:  
   - If `ch == '('` → `leftMin++`, `leftMax++` (a new mandatory ‘(’).  
   - Else if `ch == ')'` → `leftMin--`, `leftMax--` (a mandatory ‘)’ must close a previous ‘(’).  
   - Else (`ch == '*'`) → `leftMin--` (treat ‘*’ as ‘)’), `leftMax++` (treat ‘*’ as ‘(’).  
4. **Early failure check**: if `leftMax < 0` return `false`.  
   - *Why `< 0`*: even the most generous interpretation (all ‘*’ as ‘(’) still leaves more right parentheses than left, so the prefix can never be balanced.  
5. **Clamp the minimum**: `leftMin = Math.max(leftMin, 0)`.  
   - This discards impossible negative minima that arise when we treat a ‘*’ as ‘)’ but could instead be empty; the real minimum cannot be below zero because we could always interpret excess ‘)’ as empty.  
6. **After the loop**, return `leftMin == 0`.  
   - If the smallest possible open count is zero, there exists at least one concrete assignment of ‘*’ that yields a perfectly balanced string.

**Edge handling**:  
- Empty string never occurs because of the constraints, but the algorithm would correctly return `true`.  
- A single ‘(’ yields `leftMin = 1` at the end, so `false`.  
- For odd‑length strings the bounds naturally capture impossibility without extra parity checks.

## Dry Run  
Input: `(*))`

| step | ch | leftMin | leftMax | note |
|------|----|---------|---------|------|
| 0 (init) | – | 0 | 0 | start |
| 1 | '(' | 1 | 1 | '(' adds one to both bounds |
| 2 | '*' | 0 | 2 | treat as ')' for min, '(' for max |
| 3 | ')' | -1 → 0 | 1 | decrement both; clamp min to 0 |
| 4 | ')' | -1 → 0 | 0 | decrement both; max stays non‑negative |

After processing all characters `leftMin == 0`, so the string is valid. The table stops after 4 iterations, well within the 8‑row limit.

## Complexity  
- **Time:** O(n) – the single `for` loop visits each character once, and `leftMax` moves at most two steps per character.  
- **Space:** O(1) – only two integer counters (`leftMin`, `leftMax`) are stored, independent of input size. (The output boolean does not count toward extra space.)

## Solution (Java)

```java
class Solution {
    public boolean checkValidString(String s) {
        int leftMin = 0, leftMax = 0;
        for(char ch : s.toCharArray()){
            if(ch == '('){
                leftMin++;
                leftMax++;
            }
            else if(ch == ')'){
                leftMin--;
                leftMax--;
            }
            else{
                leftMin--;
                leftMax++;
            }
            if(leftMax < 0) return false;
            leftMin = Math.max(leftMin, 0);
        }
        return leftMin == 0;
    }
}
```

---

**Runtime** 0 ms (beats 100.0%) · **Memory** 42.7 MB (beats 51.4%)

<sub>Synced by AILeetHub on 2026-10-04.</sub>
