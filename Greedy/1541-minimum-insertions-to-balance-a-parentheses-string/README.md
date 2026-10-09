# 1541. Minimum Insertions to Balance a Parentheses String

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/minimum-insertions-to-balance-a-parentheses-string/)

`String` · `Stack` · `Greedy` · `Bracket Sequences`

## Intuition  
Each left parenthesis `(` must be closed by **exactly two** consecutive right parentheses `))`. While scanning the string we can keep a counter of how many `)` we still need to satisfy the already‑seen `(`. The key observation is: *if the needed count is odd, the next `(` cannot start a fresh pair of `))` until we insert one missing `)`*. This eliminates the need for a second pass, a stack, or any extra data structure; we only track the deficit (`currentNeed`) and the number of insertions performed (`answer`).

## Approach  
1. Initialise `answer = 0` (insertions made) and `currentNeed = 0` (how many `)` are still required).  
2. Iterate over each character `current` in `s`.  
   - **If `current` is `'('`**:  
     - *Invariant*: before processing a new `(`, `currentNeed` reflects the exact number of `)` still required for previous `(`.  
     - If `currentNeed` is odd (`currentNeed % 2 == 1`), one `)` is missing to complete a `))` pair. Insert it (`answer++`) and decrement `currentNeed` to make it even.  
     - After the possible fix, the new `(` itself demands two `)` → `currentNeed += 2`.  
   - **If `current` is `')'`**:  
     - *Invariant*: `currentNeed` is the number of `)` we are still waiting for.  
     - If `currentNeed == 0`, there is no pending `(` to consume this `)`. We must insert a matching `(` (`answer++`) and treat the current `)` as the first of its required pair, so set `currentNeed = 1`.  
     - Otherwise we simply satisfy one needed `)` → `currentNeed--`.  
3. After the loop, any remaining deficit (`currentNeed`) must be supplied by inserting that many `)`; add it to `answer`.  
4. Return `answer`.

Edge handling: an empty or single‑character string falls naturally into the loop logic; the odd‑even check guarantees correct handling of strings ending with a single `)`. The code chooses the convention “insert a `(` when a stray `)` appears” rather than postponing insertion, because that immediately restores a valid prefix and keeps `currentNeed` bounded by at most 2.

## Dry Run  
Input: `(()))`

| i | current | answer | currentNeed | note |
|---|---------|--------|-------------|------|
| 0 | `(` | 0 | 2 | new `(` needs two `)` |
| 1 | `(` | 0 | 4 | previous need unchanged, add two more |
| 2 | `)` | 0 | 3 | consume one needed `)` |
| 3 | `)` | 0 | 2 | consume another needed `)` |
| 4 | `)` | 0 | 1 | consume one, leaving an odd deficit |
| end | – | 0 | 1 | one `)` still missing → `answer + currentNeed = 1` |

Final state: `answer = 0`, `currentNeed = 1`; total insertions = `1`, which matches the optimal solution.

## Complexity  
- **Time:** `O(n)` – the single pass processes each character once, and `fast`‑like advancement is implicit in the `currentNeed` updates.  
- **Space:** `O(1)` – only two integer variables are maintained regardless of input size (output array not counted).

## Solution (Java)

```java
class Solution {
    public int minInsertions(String s) {
        int answer = 0;
        int currentNeed = 0;

        for (char current : s.toCharArray()) {
            if (current == '(') {
                // checking if our previours ( has already gotten its )) pair or not if need % 2 == 1 means 1 ) is remaining 
                if (currentNeed % 2 == 1) {
                    answer++; // intertion of one )
                    currentNeed--;
                }
                currentNeed += 2; // current ( requires two ))
            } else {
                if (currentNeed == 0) { // means the closing bracket ')' needs an opening bracket '('
                    answer++;
                    currentNeed = 1; // we have already gotten one ')' bracket so need = 1 not 2
                } else {
                    currentNeed--;
                }
            }
        }
        return answer + currentNeed;
    }
}
```

---

**Runtime** 8 ms (beats 99.4%) · **Memory** 47.8 MB (beats 11.9%)

<sub>Synced by AILeetHub on 2026-10-09.</sub>
