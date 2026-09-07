# 290. Word Pattern

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/word-pattern/)

`Hash Table` · `String`

## Intuition  
The core observation is that the pattern‑to‑word relationship must be a **bijection**: each character must always correspond to the same word, and no two characters may share a word. If we can enforce both “forward” consistency (character → word) and “reverse” uniqueness (word not already used by another character) while scanning the two sequences in lockstep, a single pass suffices. A naïve solution would first build all mappings, then perform a second pass or use extra structures to detect duplicates, costing O(n) extra time or memory. By maintaining a hash map for the forward mapping and a hash set (or a reverse‑lookup via `containsValue`) for the reverse uniqueness, we eliminate any second traversal. This is the classic **two‑hash‑structure** pattern for bijective mapping problems.

## Approach  
1. **Split the input string**  
   ```java
   String[] words = s.split(" ");
   ```  
   The array `words` now holds the sequence of tokens that must align with `pattern`.  
2. **Length guard** – if `words.length != pattern.length()`, return `false`. This catches mismatched counts before any mapping work.  
3. **Iterate over the pattern** (`for (int i = 0; i < pattern.length(); i++)`):  
   - Retrieve the current character `ch = pattern.charAt(i)` and the corresponding token `word = words[i]`.  
   - **Forward check**: if `map.containsKey(ch)` then the previously stored word must equal `word`. If not, the pattern is violated → `return false`.  
   - **Reverse check**: if `ch` is unseen, we must ensure `word` is not already bound to a different character. The code does this with `map.containsValue(word)` (or a separate `HashSet<String> seen`). If the word is present, return `false`.  
   - **Insert mapping**: when both checks pass, store the new association `map.put(ch, word)`.  
4. After the loop finishes without early termination, every position satisfied the bijection, so return `true`.  

Key invariants:  
- At the start of each iteration, `map` contains a consistent character→word mapping for all indices `< i`.  
- The reverse‑uniqueness invariant holds because any word already in the map is guaranteed to be bound to exactly one character.  
Edge handling: empty pattern or single‑word inputs are covered by the length guard; the algorithm treats even and odd lengths uniformly because it never relies on parity. The use of `containsValue` avoids an extra `HashSet` while still guaranteeing O(1) average lookup.

## Dry Run  
**Input**: `pattern = "abba"`, `s = "dog cat cat dog"`  

| i | ch | word | map (after step)                | note                              |
|---|----|------|---------------------------------|-----------------------------------|
| 0 | a  | dog  | {a→dog}                         | `a` unseen, `dog` unused → insert |
| 1 | b  | cat  | {a→dog, b→cat}                  | `b` unseen, `cat` unused → insert |
| 2 | b  | cat  | {a→dog, b→cat}                  | `b` present, maps to `cat` → ok   |
| 3 | a  | dog  | {a→dog, b→cat}                  | `a` present, maps to `dog` → ok   |

Loop ends; all invariants hold, so the method returns **true** because the final `map` represents a perfect bijection.

## Complexity  
- **Time:** O(n) where n = `pattern.length()`. The split operation is O(n), and the single loop performs constant‑time hash lookups (`containsKey`, `containsValue`, `put`) for each character.  
- **Space:** O(m) where m = number of distinct characters (≤ n). The hash map stores at most one entry per unique pattern character, and the `words` array holds n strings; auxiliary space beyond the output is linear.

## Solution (Java)

```java
// class Solution {
//     public boolean wordPattern(String pattern, String s) {
//         HashSet<String> seen = new HashSet<>();

//         HashMap<Character, String> map = new HashMap<>();
//         String[] words = s.split(" ");
//         if(words.length != pattern.length())
//             return false; 

//         for (int i = 0; i < pattern.length(); i++) {
//             char ch = pattern.charAt(i);
//             String word = words[i];

//             if (map.containsKey(ch)) {
//                 if (map.get(ch).equals(word))
//                     continue;
//                 else
//                     return false;
//             }
//             if (seen.contains(word))
//                 return false;

//             seen.add(word);
//             map.put(ch, word);
//         }
//         return true;
//     }
// }

// Simplified Approach
class Solution {
    public boolean wordPattern(String pattern, String s) {

        HashMap<Character, String> map = new HashMap<>();
        String[] words = s.split(" ");

        if (words.length != pattern.length())
            return false;

        for (int i = 0; i < pattern.length(); i++) {
            char ch = pattern.charAt(i);
            String word = words[i];

            if (map.containsKey(ch)) {
                if (!words[i].equals(map.get(ch)))
                    return false;
            }else if(map.containsValue(word))
                return false;
            else
                map.put(ch, word);
        }
        return true;
    }
}
```

---

**Runtime** 1 ms (beats 83.8%) · **Memory** 42.1 MB (beats 99.2%)

<sub>Synced by AILeetHub on 2026-09-07.</sub>
