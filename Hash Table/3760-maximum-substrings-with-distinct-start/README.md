# 3760. Maximum Substrings With Distinct Start

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/maximum-substrings-with-distinct-start/)

`Hash Table` · `String`

## Intuition  
The only restriction is that the first character of each chosen substring must be unique. Consequently, the maximum number of substrings equals the number of distinct letters that appear in the whole string—once we have used a letter as a start we cannot reuse it, and there is no benefit in grouping letters together because that would only reduce the count. A naïve solution might try all possible splits (exponential) or maintain a map of substrings, but the observation above eliminates any need for extra passes or complex data structures. The pattern employed is a simple **frequency‑set** check using a fixed‑size boolean array.

## Approach  
1. **Initialise tracking array** – `boolean[] seen = new boolean[26]` creates a slot for each lowercase letter, all initially `false`.  
2. **Iterate over characters** – `for (char c : s.toCharArray())` walks the string left‑to‑right.  
   - *Exit condition*: the loop stops after the last character; the invariant is that after processing the first *i* characters, `seen[x]` is `true` exactly for the distinct letters among those *i* characters.  
3. **Check and record new start** – `if (!seen[c - 'a'])` tests whether the current character has been encountered before.  
   - If not, set `seen[c - 'a'] = true` and increment `count`. This records that we can start a new substring with this letter.  
4. **Return result** – after the loop, `count` holds the total number of distinct letters, which is the answer.

**Edge‑case handling**:  
- The constraints guarantee `s.length ≥ 1`, so the loop runs at least once.  
- For a single‑character string the algorithm correctly returns `1`.  
- The subtraction `c - 'a'` is safe because the input is limited to lowercase letters; no overflow or out‑of‑bounds checks are needed.  
- Using `<=` versus `<` is irrelevant here because the condition is a boolean check, not an index comparison.

## Dry Run  

Input: `s = "abca"`

| Iteration | `c` | `c - 'a'` | `seen` after update | `count` | Note |
|-----------|-----|----------|----------------------|---------|------|
| 1 | a | 0 | a:true, others:false | 1 | first occurrence of ‘a’ → count++ |
| 2 | b | 1 | a:true, b:true, others:false | 2 | new letter ‘b’ |
| 3 | c | 2 | a:true, b:true, c:true, others:false | 3 | new letter ‘c’ |
| 4 | a | 0 | unchanged (a already true) | 3 | duplicate ‘a’, no increment |

After processing all characters, `count = 3`, which matches the three distinct start letters `{a,b,c}` and is therefore the maximal number of substrings.

## Complexity  
- **Time:** `O(n)` – the single pass examines each of the `n` characters once, and each lookup/assignment in the 26‑element array is `O(1)`.  
- **Space:** `O(1)` – the boolean array has constant size 26, independent of input length; the output integer does not affect the asymptotic bound.

## Solution (Java)

```java
class Solution {
    public int maxDistinct(String s) {
        boolean[] seen = new boolean[26];
        int count = 0;

        for (char c : s.toCharArray()) {
            if (!seen[c - 'a']) {
                seen[c - 'a'] = true;
                count++;
            }
        }

        return count;
    }
}
```

---

**Runtime** 6 ms (beats 90.4%) · **Memory** 47.5 MB (beats 92.6%)

<sub>Synced by AILeetHub on 2026-10-12.</sub>
