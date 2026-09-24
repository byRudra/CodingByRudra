# 387. First Unique Character in a String

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/first-unique-character-in-a-string/)

`Hash Table` · `String` · `Queue` · `Counting`

## Intuition  
The string contains only lowercase English letters, so there are at most 26 distinct characters. By counting how many times each letter appears we can decide in constant extra space whether a character is unique. A naïve double‑loop would compare every pair of positions (O(n²)) or a hash map would need O(n) extra space and extra hashing overhead. The key observation is that a fixed‑size frequency array lets us gather all counts in one linear pass and then locate the first count‑one character in a second linear pass, eliminating any need for sorting, hashing, or additional traversals.

## Approach  
1. **Allocate frequency array** – `int charArray[] = new int[26];` creates a slot for each letter `'a'` … `'z'`.  
2. **First pass – count frequencies**  
   *Loop:* `for (char ch : s.toCharArray())` iterates over every character.  
   *Invariant:* after processing the first *k* characters, `charArray[c‑'a']` holds the exact number of occurrences of each letter *c* among those *k* characters.  
   *Update:* `charArray[ch - 'a']++` increments the slot for the current character.  
   *Exit:* when the iterator reaches the end of `s`; at that point the array contains the full frequency distribution of the whole string.  
3. **Second pass – find first unique**  
   *Loop:* `for (int i = 0; i < s.length(); i++)` scans indices from left to right.  
   *Invariant:* at the start of each iteration, all positions `< i` have been examined and none satisfied the uniqueness test.  
   *Check:* `if (charArray[s.charAt(i) - 'a'] == 1)` – the current character occurs exactly once in the entire string.  
   *Return:* `i` immediately when the condition holds, guaranteeing the smallest index.  
   *Exit:* either by returning inside the loop or, after the loop finishes, returning `-1` because no count‑one entry exists.  
4. **Edge handling** – The code works for a single‑character string (the count will be 1, so index 0 is returned) and for strings where every character repeats (the final `return -1` covers this). No special case for empty input is needed because the constraints guarantee `s.length() ≥ 1`.

## Dry Run  

**Input:** `loveleetcode`  

| i | current char | charArray[current‑char] (after first pass) | condition `== 1`? | action |
|---|--------------|--------------------------------------------|-------------------|--------|
| 0 | l | 2 | false | continue |
| 1 | o | 2 | false | continue |
| 2 | v | 1 | true | return 2 |
| 3 | e | 4 | – | – |
| 4 | l | 2 | – | – |
| … | … | … | … | … |

The first two loops have already filled `charArray` (e.g., `'l'` appears twice, `'v'` once). During the second loop, the iteration at `i = 2` finds `'v'` with a frequency of 1 and returns index 2, which matches the expected answer.

## Complexity  
- **Time:** O(n) – the first loop visits each of the *n* characters once to build frequencies, and the second loop visits each character at most once more to locate the first unique.  
- **Space:** O(1) – the auxiliary `charArray` has a constant size of 26 regardless of input length; it does not grow with *n*. (The output integer does not affect the asymptotic bound.)

## Solution (Java)

```java
class Solution {
    public int firstUniqChar(String s) {
        
        int charArray[] = new int[26];

        for(char ch : s.toCharArray()){
            charArray[ch - 'a']++; 
        }

        for(int i = 0; i < s.length(); i++){
           if (charArray[s.charAt(i) - 'a'] == 1) {
                return i;
            }
        }
        return -1;
    }
}
```

---

**Runtime** 6 ms (beats 85.7%) · **Memory** 46.8 MB (beats 72.7%)

<sub>Synced by AILeetHub on 2026-04-23.</sub>
