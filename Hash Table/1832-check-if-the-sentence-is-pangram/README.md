# 1832. Check if the Sentence Is Pangram

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/check-if-the-sentence-is-pangram/)

`Hash Table` · `String`

## Intuition  
The only thing that matters for a pangram is whether each of the 26 lowercase letters appears at least once. If we can record the presence of a letter the moment we see it, a single left‑to‑right scan is enough. The naïve alternative would be to sort the characters, build a `Set`, or count frequencies with a map—each adds extra passes or overhead. By using a fixed‑size boolean array indexed by `word - 'a'`, we capture the required information in constant space and eliminate any need for additional data structures.

## Approach  
1. **Allocate tracking array** – `boolean[] isPangram = new boolean[26];` creates a slot for every alphabet letter, all initially `false`.  
2. **Early length guard** – `if (size < 26) return false;` exploits the fact that a string shorter than the alphabet cannot be a pangram, saving work for trivial cases.  
3. **First pass (populate)** – `for (char word : sentence.toCharArray()) { isPangram[word - 'a'] = true; }` iterates over each character; the invariant after processing the first *i* characters is: `isPangram[c]` is `true` exactly for those letters that have appeared among the first *i* positions. The subtraction `word - 'a'` maps `'a'..'z'` to indices `0..25`.  
4. **Second pass (verify)** – `for (boolean check : isPangram) { if (!check) return false; }` checks every slot. The loop exits early on the first `false`, guaranteeing that any missing letter causes an immediate `false`. The invariant here is: all examined slots so far are `true`.  
5. **Return success** – If the second loop finishes without returning, every entry is `true`, so `return true;` confirms the sentence is a pangram.

Key edge handling: the guard in step 2 handles empty or sub‑26 strings; the loops use `<=`‑style bounds implicitly via the enhanced `for` construct, avoiding off‑by‑one errors. The choice of a fixed 26‑element array sidesteps potential overflow concerns because the index is always within `[0,25]` for valid lowercase input.

## Dry Run  
**Input:** `sentence = "abcde"`  

| Iteration | `word` (first loop) | `isPangram` after update (shown as indices set) | Note |
|-----------|--------------------|-----------------------------------------------|------|
| 1 | 'a' | `[0] = true` | marks 'a' |
| 2 | 'b' | `[0,1] = true` | marks 'b' |
| 3 | 'c' | `[0,1,2] = true` | marks 'c' |
| 4 | 'd' | `[0,1,2,3] = true` | marks 'd' |
| 5 | 'e' | `[0,1,2,3,4] = true` | marks 'e' |

After the first loop `size = 5 < 26`, so step 2 returns `false` immediately; the second loop never runs. The final state is `false` because the sentence cannot contain all 26 letters.

## Complexity  
- **Time:** O(n) + O(26) = O(n), where *n* is `sentence.length()`. The first loop scans each character once, and the second loop checks the constant‑size array.  
- **Space:** O(1) extra space, because `isPangram` holds exactly 26 booleans regardless of input size (output boolean does not count).

## Solution (Java)

```java
class Solution {
    public boolean checkIfPangram(String sentence) {
        boolean[] isPangram = new boolean[26];

        int size = sentence.length();
        
        if (size < 26) return false;
        
        for(char word : sentence.toCharArray()){
            isPangram[word - 'a'] = true;
        }
        
        for(boolean check : isPangram){
            if(!check) return false;
        }
        
        return true;

    }
}
```

---

**Runtime** 1 ms (beats 82.6%) · **Memory** 43 MB (beats 51.4%)

<sub>Synced by AILeetHub on 2026-09-26.</sub>
