# 22. Generate Parentheses

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/generate-parentheses/)

`String` · `Dynamic Programming` · `Backtracking` · `Bracket Sequences`

## Intuition  
The key observation is that a well‑formed parentheses string can be built incrementally as long as we never place a closing bracket before there is a matching opening one. Consequently, at any prefix the number of `(` must be at least the number of `)`. This invariant lets us prune the search space dramatically: we only need to try adding an opening bracket while we still have spare pairs, and we may add a closing bracket only when the count of opens already exceeds closes. A naïve brute‑force would generate all \(2^{2n}\) strings and then filter, which is exponential in a useless way. By maintaining the two counters (`open` and `close`) and respecting the invariant, the algorithm explores exactly the valid configurations using a depth‑first backtracking pattern.

## Approach  
1. **Initial call** – `generateParenthesis` creates an empty `result` list and invokes `backtrack(result, "", 0, 0, n)`.  
2. **Termination check** – Inside `backtrack`, if `current.length() == 2*n` the string is complete; it is added to `result` and the call returns. This is the only base case.  
3. **Try opening bracket** – If `open < n`, the code recurses with `current + "("`, `open+1`, `close`. The invariant after this call is `open ≤ n` and `open ≥ close`.  
4. **Try closing bracket** – If `close < open`, the code recurses with `current + ")"`, `open`, `close+1`. The guard `close < open` guarantees we never close more than we have opened, preserving the well‑formed prefix property.  
5. **Backtrack** – After each recursive call returns, execution continues to the next conditional (or returns to the caller). No explicit undo step is needed because strings are immutable; each call works on its own `current` copy.  
6. **Edge handling** – The algorithm naturally handles `n = 0` (returns an empty list) and `n = 1` (produces `"()"`). The checks use `<=` only where equality is allowed (`open < n` permits the last opening bracket, `close < open` permits the final closing bracket).  

## Dry Run  
Input: `n = 2` (target length = 4)

| Step | current | open | close | note |
|------|---------|------|-------|------|
| 1 | "" | 0 | 0 | start |
| 2 | "(" | 1 | 0 | added '(' (open < 2) |
| 3 | "((" | 2 | 0 | added '(' (open < 2) |
| 4 | "(()" | 2 | 1 | added ')' (close < open) |
| 5 | "(())" | 2 | 2 | length = 4 → added to result |
| 6 | "()" | 1 | 1 | backtrack, add ')' after first '(' |
| 7 | "()(" | 2 | 1 | added '(' (open < 2) |
| 8 | "()()" | 2 | 2 | length = 4 → added to result |

The recursion finishes with `result = ["(())","()()"]`, which are exactly the two well‑formed strings for two pairs.

## Complexity  
- **Time:** **O(Cₙ)**, where \(Cₙ = \frac{1}{n+1}\binom{2n}{n}\) is the *n*‑th Catalan number. Each recursive leaf produces one valid string, and the total number of leaves equals the Catalan count.  
- **Space:** **O(Cₙ)** for the output list plus **O(n)** auxiliary stack space for the recursion depth (the maximum length of `current`). The extra space excludes the result itself, as required.

## Solution (Java)

```java
class Solution {
    public List<String> generateParenthesis(int n) {
        List<String> result = new ArrayList<>();
        backtrack(result, "", 0, 0, n);
        return result;
    }

    private void backtrack(List<String> result, String current, int open, int close, int n) {
        if(current.length() == 2*n){
            result.add(current);
            return;
        }
        if(open < n){
            backtrack(result, current + "(", open+1, close, n);
        }
        if(close < open){
            backtrack(result, current + ")", open, close+1, n);
        }
    }
}
```

---

**Runtime** 2 ms (beats 68.8%) · **Memory** 44.4 MB (beats 71.7%)

<sub>Synced by AILeetHub on 2026-07-02.</sub>
