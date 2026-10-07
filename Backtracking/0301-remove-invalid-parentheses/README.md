# 301. Remove Invalid Parentheses

![Hard](https://img.shields.io/badge/Difficulty-Hard-ff375f?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/remove-invalid-parentheses/)

`String` · `Backtracking` · `Breadth-First Search`

## Intuition  
The key observation is that the *minimum* number of deletions is determined solely by the surplus of unmatched left and right parentheses in the original string. After a single left‑to‑right scan we know exactly how many ‘(’ (`removeLeft`) and ‘)’ (`removeRight`) must be discarded; any solution that removes fewer cannot be valid. This eliminates the need for repeated passes, hash‑maps of visited strings, or exhaustive generation of all subsets. With those counts in hand we can explore only those choices that respect the limits, using a depth‑first search that prunes whenever the current prefix already has more right than left parentheses. The overall pattern is a **backtracking with early pruning**.

## Approach  
1. **Count required removals** – Iterate over `s` with counters `left` and `right`.  
   * If `ch == '('` → `left++`.  
   * If `ch == ')'` → if `left > 0` decrement `left`; else increment `right`.  
   After the loop `removeLeft = left` and `removeRight = right` are the minimal deletions.  
2. **Start DFS** – Call `dfs(s, 0, 0, removeLeft, removeRight, new StringBuilder())`.  
   *Parameters*: `index` (next character), `balance` (opened '(' not yet closed), remaining deletions `removeLeft`/`removeRight`, and the mutable `current` string.  
3. **Prune invalid prefix** – If `balance < 0` the prefix already has more ')' than '('; return immediately.  
4. **Termination** – When `index == s.length()`  
   *If* `balance == 0` **and** both removal counters are zero, add `current.toString()` to the global `result` set. Then return.  
5. **Process a non‑parenthesis character** – Append it to `current`, recurse with `index+1` keeping all counters unchanged, then backtrack by deleting the last character. This branch is mandatory because letters never affect validity.  
6. **Option to delete a parenthesis** –  
   * If `ch == '('` and `removeLeft > 0` → recurse skipping the character with `removeLeft‑1`.  
   * If `ch == ')'` and `removeRight > 0` → recurse skipping the character with `removeRight‑1`.  
   These branches respect the exact number of deletions computed earlier.  
7. **Option to keep the parenthesis** – Append `ch` to `current`.  
   * If `ch == '('` → recurse with `balance+1`.  
   * If `ch == ')'` → recurse only when `balance > 0` (a matching '(' exists) and pass `balance‑1`.  
   This maintains the invariant that `balance` is never negative.  
8. **Backtrack** – After exploring the keep‑branch, delete the appended character so the same `StringBuilder` can be reused for sibling branches.  

The algorithm systematically enumerates every string that uses exactly `removeLeft` left‑deletions and `removeRight` right‑deletions while never allowing an invalid prefix, guaranteeing that all collected results are minimal and unique (the `HashSet` removes duplicates).

## Dry Run  
Input: `s = "()())"`  

| step | index | ch | balance | removeLeft | removeRight | current | note |
|------|-------|----|---------|------------|-------------|---------|------|
| 1 | 0 | '(' | 0 → 1 | 1 | 0 | "(" | keep '(' (balance+1) |
| 2 | 1 | ')' | 1 → 0 | 1 | 0 | "()" | keep ')' (balance‑1) |
| 3 | 2 | '(' | 0 → 1 | 1 | 0 | "()(" | keep '(' |
| 4 | 3 | ')' | 1 → 0 | 1 | 0 | "()()" | keep ')' |
| 5 | 4 | ')' | 0 → **pruned** (balance would become -1) → instead **delete** using `removeRight>0` | 1 | 0 → 0 | "()()" | skip last ')' |
| 6 | 5 (end) | – | 0 | 0 | 0 | "()()" | added to result |

The DFS also explores the branch that deletes the first '(' (using `removeLeft`), producing the second valid minimal string `"()()"` (identical after set deduplication). The final `result` set contains `["()()", "(())"]`, matching the expected minimal removals.

## Complexity  
- **Time:** O( C ), where C is the number of distinct recursive states. Each state advances `index` by one, and `index` runs at most *n*; the pruning (`balance < 0`) and exact removal counters keep the branching factor bounded, so the total work is proportional to the number of valid combinations, which in the worst case is exponential but limited by the minimal‑removal constraint.  
- **Space:** O(n) for the recursion stack and the mutable `StringBuilder` (output list excluded), because the depth never exceeds the length of the input string.

## Solution (Java)

```java
class Solution {
    private Set<String> result = new HashSet<>();

    public List<String> removeInvalidParentheses(String s) {
        // Find min no of removal required
        int right = 0, left = 0;
        for (char ch : s.toCharArray()) {
            if (ch == '(')
                left++;
            else if (ch == ')'){
                if (left > 0)
                    left--;
                else
                    right++;
            }
        }

        // now we have the min no so lets backTrack

        dfs(s, 0, 0, left, right, new StringBuilder());
        return new ArrayList<>(result);
    }

    public void dfs(
            String s,
            int index,
            int balance,
            int removeLeft,
            int removeRight,
            StringBuilder current) {
        // Early pruning 
        if (balance < 0)
            return;

        // now check if the current is the one we need
        if (index == s.length()) { // checks if we have gone through the whole string or not
            if (balance == 0 && removeLeft == 0 && removeRight == 0) 
                result.add(current.toString());
            return;
        }

        char ch = s.charAt(index);
        if (ch != '(' && ch != ')') {
            // means it is a character so we include it any way and move forward
            current.append(ch);

            dfs(s, index + 1, balance, removeLeft, removeRight, current);

            // Now we remove this char and backtrack again
            current.deleteCharAt(current.length() - 1);

            return;
        }
        // Removing '(' OR ')'
        if (ch == '(' && removeLeft > 0) {
            dfs(
                    s,
                    index + 1,
                    balance,
                    removeLeft - 1,
                    removeRight,
                    current);
        }
        if (ch == ')' && removeRight > 0) {
            dfs(
                    s,
                    index + 1,
                    balance,
                    removeLeft,
                    removeRight - 1,
                    current);
        }

        // Now we take these as RemoveLeft OR RemoveRight is = 0
        // Hii Best Of Luck For Zomato
        current.append(ch);

        if (ch == '(') {
            dfs(
                    s,
                    index + 1,
                    balance + 1,
                    removeLeft,
                    removeRight,
                    current);
        } else {
            if (balance > 0) {
                dfs(
                        s,
                        index + 1,
                        balance - 1,
                        removeLeft,
                        removeRight,
                        current);
            }
        }

        // BackTrack again
        current.deleteCharAt(current.length() - 1);

    }
}
```

---

**Runtime** 148 ms (beats 22.7%) · **Memory** 43.9 MB (beats 86.5%)

<sub>Synced by AILeetHub on 2026-10-07.</sub>
