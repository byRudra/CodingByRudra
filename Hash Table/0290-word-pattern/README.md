# 290. Word Pattern

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/word-pattern/)

`Hash Table` · `String`

## Intuition  
The core observation is that the pattern‑to‑word relationship must be a one‑to‑one mapping: each character must always correspond to the same word, and no two characters may share a word. If we can enforce this bijection while scanning the two sequences in lockstep, a single pass suffices. A naïve solution would compare every character with every word, leading to O(n²) work or would store both directions in two maps, which is unnecessary duplication. By keeping one map from character → word and a secondary set of already‑used words, we guarantee both directions with just one traversal – a classic two‑structure “bijection” pattern.

## Approach  
1. **Split the input string** – `String[] words = s.split(" ");` creates an array of the individual tokens.  
2. **Early length check** – `if (words.length != pattern.length()) return false;` ensures the two sequences have the same size; otherwise a bijection is impossible.  
3. **Iterate over the pattern** – `for (int i = 0; i < pattern.length(); i++)` processes characters and words in parallel.  
   - **Invariant**: before each iteration, `map` contains a consistent mapping for all processed positions, and `seen` holds exactly the set of words already assigned.  
4. **Fetch current items** – `char ch = pattern.charAt(i);` and `String word = words[i];`.  
5. **Existing character case** –  
   - If `map.containsKey(ch)`, the code checks `map.get(ch).equals(word)`.  
   - If they match, the loop continues; otherwise the pattern is violated and `false` is returned.  
6. **New character case** –  
   - If `seen.contains(word)`, another character has already claimed this word, breaking bijection → return `false`.  
   - Otherwise, record the new association: `seen.add(word);` and `map.put(ch, word);`.  
7. **Finish** – After the loop, every position satisfied the invariant, so `true` is returned.

Key edge handling: an empty pattern or a single‑character pattern works because the length check passes and the loop processes zero or one iteration. The `split(" ")` call never yields empty strings because the input guarantees no leading/trailing spaces and single spaces between words. The `<=` vs `<` issue does not arise; the loop condition `i < pattern.length()` correctly stops after the last index.

## Dry Run  

**Input**  
`pattern = "abba"`  
`s = "dog cat cat dog"`

| i | ch | word | map (after step)                | seen (after step)      | note                              |
|---|----|------|---------------------------------|------------------------|-----------------------------------|
| 0 | a  | dog  | {a→dog}                         | {dog}                  | new char, word unused → add both |
| 1 | b  | cat  | {a→dog, b→cat}                  | {dog, cat}             | new char, word unused → add both |
| 2 | b  | cat  | unchanged                       | unchanged               | map contains b, word matches → continue |
| 3 | a  | dog  | unchanged                       | unchanged               | map contains a, word matches → continue |

All four iterations finish without conflict, so the final state (`map` holds a↔dog, b↔cat) satisfies the bijection, and the method returns **true**.

## Complexity  
- **Time:** O(n) – the split creates `n` words, and the single `for` loop visits each character/word once; `map` and `seen` operations are O(1) average.  
- **Space:** O(m) – `map` stores at most `m` distinct character→word pairs and `seen` stores the same number of unique words, where `m` ≤ number of pattern characters (≤ 300). The output array `words` is excluded from the extra‑space count.

## Solution (Java)

```java
class Solution {
    public boolean wordPattern(String pattern, String s) {
        HashSet<String> seen = new HashSet<>();

        HashMap<Character, String> map = new HashMap<>();
        String[] words = s.split(" ");
        if(words.length != pattern.length())
            return false; 
            
        for (int i = 0; i < pattern.length(); i++) {
            char ch = pattern.charAt(i);
            String word = words[i];

            if (map.containsKey(ch)) {
                if (map.get(ch).equals(word))
                    continue;
                else
                    return false;
            }
            if (seen.contains(word))
                return false;

            seen.add(word);
            map.put(ch, word);
        }
        return true;
    }
}
```

---

**Runtime** 1 ms (beats 83.8%) · **Memory** 42.7 MB (beats 73.5%)

<sub>Synced by AILeetHub on 2026-09-07.</sub>
