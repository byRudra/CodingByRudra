# 205. Isomorphic Strings

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/isomorphic-strings/)

`Hash Table` · `String`

## Intuition  
The core observation is that two strings are isomorphic exactly when there exists a **bijective** character‑to‑character correspondence that is consistent at every position. If we record, for each character in `s`, which character it has been mapped to in `t` **and** the reverse mapping from `t` back to `s`, any conflict immediately disproves isomorphism. A naïve solution would build a map on the fly and then, for each new pair, search the whole map for duplicates, yielding O(n²) time. By maintaining two fixed‑size arrays (`mapST` and `mapTS`) indexed by the ASCII code, we achieve constant‑time look‑ups and eliminate the extra pass or hash‑map overhead. This is the classic *two‑directional mapping* pattern.

## Approach  
1. **Length guard** – If `s.length() != t.length()`, return `false`. No further work is needed.  
2. **Initialize tables** – Create two int arrays of length 256 (`mapST` for s→t, `mapTS` for t→s) and fill each entry with `-1` to denote “unmapped”.  
3. **Iterate over positions** (`i` from 0 to `s.length()-1`):  
   - Retrieve the current characters: `char a = s.charAt(i); char b = t.charAt(i);`.  
   - **Forward consistency check**: if `mapST[a] != -1` (meaning `a` was seen before) and `mapST[a] != b`, the existing mapping contradicts the new pair → return `false`.  
   - **Reverse consistency check**: if `mapTS[b] != -1` and `mapTS[b] != a`, the character `b` is already mapped to a different source → return `false`.  
   - **Record the mapping**: set `mapST[a] = b` and `mapTS[b] = a`. This establishes the bijection for the current pair.  
4. **Successful termination** – After the loop finishes without conflicts, return `true`.  

Key invariants: after processing index `i`, `mapST` contains the exact forward mapping for all characters seen in `s[0..i]`, and `mapTS` contains the exact reverse mapping for all characters seen in `t[0..i]`. The loop exits when `i == s.length()`. Edge cases such as empty strings, single‑character strings, or strings containing duplicate characters are handled uniformly because the tables start at `-1` and the consistency checks guard against both over‑mapping and under‑mapping.

## Dry Run  

**Input**: `s = "egg", t = "add"`

| i | a (s[i]) | b (t[i]) | mapST[a] before | mapTS[b] before | Action / Note                              |
|---|----------|----------|-----------------|-----------------|--------------------------------------------|
| 0 | e        | a        | -1              | -1              | set `mapST[e]=a`, `mapTS[a]=e`             |
| 1 | g        | d        | -1              | -1              | set `mapST[g]=d`, `mapTS[d]=g`             |
| 2 | g        | d        | d               | g               | both checks pass (existing mapping matches) |

Loop ends; no conflict was found, so the algorithm returns **true**, which is the correct answer because the mapping `{e→a, g→d}` is bijective.

## Complexity  
- **Time:** O(n) – the single `for` loop visits each of the `n` character pairs once, and every array access (`mapST[a]`, `mapTS[b]`) is O(1).  
- **Space:** O(1) – two fixed‑size int arrays of length 256 are allocated regardless of input size; the output boolean does not count toward extra space.

## Solution (Java)

```java
class Solution {
    public boolean isIsomorphic(String s, String t) {
        if(s.length() != t.length()) return false;

        int[] mapTS  = new int[256];
        int[] mapST  = new int[256];
        Arrays.fill(mapTS, -1);
        Arrays.fill(mapST, -1);

        for(int i = 0; i < s.length(); i++){
            char a = s.charAt(i);
            char b = t.charAt(i);

            if(mapST[a] != -1 && mapST[a] != b) return false;
            if(mapTS[b] != -1 && mapTS[b] != a) return false;

            mapTS[b] = a; 
            mapST[a] = b; 
        }
        return true;
    }
}
```

---

**Runtime** 6 ms (beats 87.3%) · **Memory** 43.7 MB (beats 73.5%)

<sub>Synced by AILeetHub on 2026-09-30.</sub>
