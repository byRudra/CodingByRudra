# 345. Reverse Vowels of a String

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/reverse-vowels-of-a-string/)

`Two Pointers` · `String`

## Intuition  
The key observation is that a vowel at the left side of the string will always end up at a symmetric position on the right side after reversal, and vice‑versa. Therefore we can locate the next vowel from the front and the next vowel from the back, swap them, and move both pointers inward. A naïve solution that first collects all vowel indices, stores the vowels, and then writes them back would need an extra pass and O(n) auxiliary storage. By maintaining two indices (`start` and `end`) that walk toward each other, we eliminate the extra array and achieve the reversal in a single linear scan. This is the classic **two‑pointer** pattern.

## Approach  
1. **Initialisation** – `start = 0`, `end = s.length() - 1`. Convert the input to a mutable `char[] chars`. Build a `HashSet<Character> set` containing both lower‑case and upper‑case vowels for O(1) membership tests.  
2. **Outer loop condition** – `while (start <= end)`. The invariant is that all positions `< start` and `> end` already contain the correct final vowel (or a non‑vowel) and will never be touched again.  
3. **Advance `start`** – `while (start < end && !set.contains(chars[start])) start++;`. This stops exactly at the first vowel from the left that still needs to be swapped. The `< end` guard prevents crossing the right pointer before a vowel is found.  
4. **Retreat `end`** – `while (start < end && !set.contains(chars[end])) end--;`. Symmetrically, it stops at the first vowel from the right. The same `< start` guard avoids overshooting when the remaining segment contains no vowels.  
5. **Swap** – `char temp = chars[start]; chars[start] = chars[end]; chars[end] = temp;`. At this point both `chars[start]` and `chars[end]` are guaranteed to be vowels, so the swap contributes to the reversal.  
6. **Move pointers inward** – `start++; end--;`. After the swap the positions are fixed, so we shrink the window.  
7. **Termination** – When `start` passes `end` the outer loop ends; every vowel has been paired with its mirror counterpart, and the array now represents the required string.  
8. **Return** – `new String(chars)` builds the final immutable result.

Edge cases handled explicitly: an empty or single‑character string makes the outer condition false immediately; strings with no vowels never enter the inner `while` bodies, so `start` and `end` cross without swapping; the `<=` in the outer loop ensures that a middle vowel in an odd‑length string is left untouched (swapping it with itself is harmless).

## Dry Run  

**Input:** `IceCreAm`  

| iteration | start | end | chars (as string) | note |
|-----------|-------|-----|-------------------|------|
| 1 | 0 | 7 | `IceCreAm` | `chars[start]='I'` (vowel), `chars[end]='m'` (not vowel) → inner `end` loop moves `end` to 6 |
| 2 | 0 | 6 | `IceCreAm` | `chars[end]='A'` (vowel) → swap `I` ↔ `A` → `AceCreIm`, then `start=1`, `end=5` |
| 3 | 1 | 5 | `AceCreIm` | `chars[start]='c'` not vowel → `start` moves to 2; `chars[end]='e'` vowel |
| 4 | 2 | 5 | `AceCreIm` | swap `e` ↔ `e` (same vowel) → no visible change, `start=3`, `end=4` |
| 5 | 3 | 4 | `AceCreIm` | `chars[start]='C'` not vowel → `start`→4; now `start == end` and `chars[4]='r'` not vowel, inner loops skip, swap `r` with itself, then pointers cross |
| 6 | 5 | 3 | loop ends | All vowel pairs processed; final string `AceCreIm` is returned. |

The final state contains the vowels in reverse order while every consonant stays in place.

## Complexity  
- **Time:** O(n) – the outer loop runs at most n/2 iterations because `start` and `end` move inward by one each time, and each inner `while` advances a pointer only over non‑vowel characters.  
- **Space:** O(1) – only a fixed‑size `HashSet` of 10 characters and a few integer variables are used; the mutable `char[]` reuses the input’s storage and does not count as extra space beyond the required output.

## Solution (Java)

```java
class Solution {
    public String reverseVowels(String s) {
        int start = 0;
        int end = s.length() - 1;
        HashSet<Character> set = new HashSet<>(
                Arrays.asList('a', 'e', 'i', 'o', 'u',
                        'A', 'E', 'I', 'O', 'U'));

        char[] chars = s.toCharArray();

        while(start <= end){
             while (start < end && !set.contains(chars[start])) {
                start++;
            }

            while (start < end && !set.contains(chars[end])) {
                end--;
            }
            char temp = chars[start];
            chars[start] = chars[end];
            chars[end] = temp;
            end--;
            start++;
            
        }
        return new String(chars);
    }
}
```

---

**Runtime** 5 ms (beats 20.1%) · **Memory** 46.8 MB (beats 20.8%)

<sub>Synced by AILeetHub on 2026-10-03.</sub>
