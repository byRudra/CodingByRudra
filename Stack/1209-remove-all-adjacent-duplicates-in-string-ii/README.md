# 1209. Remove All Adjacent Duplicates in String II

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/remove-all-adjacent-duplicates-in-string-ii/)

`String` · `Stack`

## Intuition  
When we scan the string left‑to‑right, the only thing that can cause a removal is a *run* of identical letters whose length reaches **k**. If we keep, for each run, how many times the current character has appeared consecutively, we can decide immediately whether the run should disappear or stay. This observation eliminates the need for a second pass, a hash map, or any recursion: we just maintain a stack of `(character, currentRunLength)` pairs, discarding a pair as soon as its count becomes **k**. The pattern is the classic **two‑pointer/stack** technique for “process while you can”.

## Approach  
1. **Initialize** an empty `Stack<int[]>`. Each element stores `{charCode, count}`.  
2. **Iterate** over `s.toCharArray()` with variable `current`.  
   - *Loop exit*: when the array is exhausted.  
   - *Invariant*: the stack contains runs that have survived all possible deletions so far, and every stored `count` is `< k`.  
3. **If the stack is empty** → push `{current, 1}` because this character starts a new run.  
4. **Otherwise** pop the top element into `last`.  
   - **Case A – same character** (`last[0] == current`):  
     - Compute `newCount = last[1] + 1`.  
     - If `newCount != k`, push `{current, newCount}` back; the run is still alive.  
     - If `newCount == k`, do **not** push anything – the whole run disappears, which also removes the previously stored `last`.  
   - **Case B – different character**:  
     - Push `last` back (its run is finished).  
     - Push a fresh `{current, 1}` to start a new run.  
5. **After the scan**, the stack holds all surviving runs in their original order.  
6. **Reconstruct** the answer: for each `curr` in the stack, append `curr[1]` copies of `(char)curr[0]` to a `StringBuilder`.  
7. **Return** the built string.

*Edge handling*:  
- Empty or single‑character strings never enter the inner `else` block, so they are returned unchanged.  
- The check uses `!= k` (instead of `< k`) because the moment the count reaches exactly **k** the run must be removed; any larger count cannot occur because we would have eliminated the run at the moment it hit **k**.  

## Dry Run  
Input: `s = "deeedbbcccbdaa", k = 3`

| step | current | stack (bottom→top)                | note                                 |
|------|---------|-----------------------------------|--------------------------------------|
| 1    | d       | [(d,1)]                           | stack empty → push new run           |
| 2    | e       | [(d,1),(e,1)]                     | different from top → start new run  |
| 3    | e       | [(d,1),(e,2)]                     | same as top, count 2 ≠ 3             |
| 4    | e       | [(d,1)]                           | count becomes 3 → discard run        |
| 5    | d       | [(d,2)]                           | same as bottom, count 2 ≠ 3          |
| 6    | b       | [(d,2),(b,1)]                     | new character                        |
| 7    | b       | [(d,2),(b,2)]                     | same, count 2                         |
| 8    | c       | [(d,2),(b,2),(c,1)]               | new run                              |

Continuing the scan eventually removes the `ccc` run (step when count hits 3) and later the `bbb` and `ddd` runs. After processing the whole string the stack contains `[(a,2)]`, which reconstructs `"aa"` – the correct final string.

## Complexity  
- **Time:** `O(n)` – each character is examined once; the stack operations are constant‑time, and the final reconstruction walks the stack once more.  
- **Space:** `O(n)` – in the worst case (no deletions) the stack stores one entry per character; the `StringBuilder` output is not counted toward auxiliary space.

## Solution (Java)

```java
class Solution {
    public String removeDuplicates(String s, int k) {
        Stack<int[]> stack = new Stack<>();
        // this will contain the char and the freq we have seen till now 
        for (char current : s.toCharArray()) {
            if (stack.isEmpty()) {
                stack.push(new int[] { current, 1 });
            } else {
                int[] last = stack.pop();
                if (last[0] == current) {
                    if (last[1] + 1 != k) {
                        stack.push(new int[] { current, last[1] + 1 });
                    }
                } else {
                    stack.push(last);
                    stack.push(new int[] { current, 1 });
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int[] curr : stack) {
            for (int i = 1; i <= curr[1]; i++) {
                sb.append((char) curr[0]);
            }
        }
        return sb.toString();
    }
}
```

---

**Runtime** 28 ms (beats 57.5%) · **Memory** 47.2 MB (beats 59.6%)

<sub>Synced by AILeetHub on 2026-10-31.</sub>
