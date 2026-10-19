# 3227. Vowels Game in a String

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/vowels-game-in-a-string/)

`Math` · `String` · `Brainteaser` · `Game Theory`

## Intuition  
The only requirement for Alice’s first move is to find a non‑empty substring that contains an odd number of vowels. A single vowel already satisfies this condition because its vowel count is 1 (odd). Consequently, if the original string contains **any** vowel, Alice can delete that one‑character substring and immediately put Bob in a losing position (the remaining string may still contain moves for Bob, but Alice’s win is guaranteed by optimal play). Conversely, when the string has no vowels, every possible substring has zero vowels, which is even, so Alice has no legal move and loses outright. This observation eliminates the need for any combinatorial game analysis, DP, or recursion; the problem collapses to a simple existence check for at least one vowel. The pattern used is a straightforward linear scan.

## Approach  
1. **Iterate** `i` from `0` to `s.length()‑1`.  
   *Exit condition:* `i == s.length()`. The invariant is that all characters before index `i` have been examined and none were vowels.  
2. **Read** `c = s.charAt(i)`.  
3. **Test** whether `c` belongs to the vowel set `{a, e, i, o, u}`.  
   *Decision point:* If the test is true, the invariant “no vowel seen so far” is broken, and we can **return `true`** immediately because Alice can win.  
4. **Continue** the loop if the test fails.  
5. After the loop finishes without finding a vowel, **return `false`**. At this point the invariant “no vowel in the entire string” holds, meaning Alice has no legal first move.

*Edge considerations:*  
- The constraints guarantee `s.length() ≥ 1`, so we never encounter an empty input.  
- A single‑character string that is a vowel triggers step 3 on the first iteration, yielding `true`.  
- Non‑vowel characters are ignored; the check uses `==` comparisons, which are safe for lowercase ASCII letters and avoid any off‑by‑one errors.  

## Dry Run  

**Input:** `s = "leetcoder"`

| i | c   | vowel? | Action                     | Note                         |
|---|-----|--------|----------------------------|------------------------------|
| 0 | l   | no     | continue                   | No vowel yet                 |
| 1 | e   | yes    | return `true`              | First vowel found → Alice wins |

The loop stops at `i = 1` because `c` is a vowel, and the function returns `true`. This matches the expected outcome: Alice can delete the single character “e” (odd vowel count) and secure a win.

## Complexity  
- **Time:** **O(n)** – the `for` loop examines each of the `n` characters at most once; it may exit earlier when a vowel is found.  
- **Space:** **O(1)** – only a few primitive variables (`i`, `c`) are used, independent of the input size. The output boolean does not count toward extra space.

## Solution (Java)

```java
class Solution {
    public boolean doesAliceWin(String s) {
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u') {
                return true;
            }
        }
        return false;
    }
}
```

---

**Runtime** 4 ms (beats 77.7%) · **Memory** 47.1 MB (beats 97.9%)

<sub>Synced by AILeetHub on 2026-10-19.</sub>
