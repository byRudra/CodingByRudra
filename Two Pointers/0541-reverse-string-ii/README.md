# 541. Reverse String II

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/reverse-string-ii/)

`Two Pointers` · `String`

## Intuition  
The key observation is that the string can be processed in independent blocks of size 2k: the first k characters of each block must be reversed, while the second k (if present) stay unchanged. By walking the array with a step of 2k we never need to look back or store anything beyond the current block, eliminating the need for an extra pass, a hash map, or recursion. This block‑wise view leads directly to a two‑pointer reversal inside each block.

## Approach  
1. Convert the input string to a mutable `char[] arr`.  
2. Iterate over the array with index `i` starting at 0 and incrementing by `2 * k` each round.  
   - **Exit condition:** `i >= arr.length`.  
   - **Invariant:** All characters before index `i` have already been processed according to the problem rules.  
3. For the current block compute the reversal window:  
   - `left = i` (the first character of the block).  
   - `right = Math.min(i + k - 1, arr.length - 1)` ensures we never step past the array end; if fewer than k characters remain, `right` becomes the last index, so the whole tail is reversed.  
4. While `left < right` swap `arr[left]` and `arr[right]`, then move `left++` and `right--`.  
   - **Loop invariant:** The segment between the original `i` and the current `left‑1` is already reversed, and the segment from `right+1` to the original `right` is also reversed.  
   - The condition uses `<` (not `<=`) because when `left == right` the middle character of an odd‑length segment is already in the correct place.  
5. After the outer loop finishes, construct a new `String` from `arr` and return it.

Edge handling: an empty or single‑character string never enters the outer loop, so it is returned unchanged. When `k` exceeds the string length, step 2 still runs once with `i = 0`, and step 3’s `right` becomes the last index, causing the whole string to be reversed as required.

## Dry Run  

**Input:** `s = "abcdefg", k = 2`  

| iteration (i) | left before | right before | arr after inner swaps | note |
|---------------|------------|--------------|----------------------|------|
| 0 | 0 | 1 | `bacdefg` | swap positions 0 and 1 |
| 0 (inner) | 1 | 0 | — | loop ends (`left >= right`) |
| 4 | 4 | 5 | `bacdfeg` | swap positions 4 and 5 |
| 4 (inner) | 5 | 4 | — | loop ends |
| 8 | – | – | — | outer loop exits (`i >= 7`) |

Final array `bacdfeg` matches the required output because each 2k‑segment (`ab`, `cd`, `ef`, `g`) had its first k characters reversed.

## Complexity  
- **Time:** O(n) – the outer loop visits each character at most once, and the inner two‑pointer loop reverses at most k characters per block, totaling n swaps.  
- **Space:** O(n) – the character array `arr` holds a copy of the input string; no additional data structures are allocated beyond this output buffer.

## Solution (Java)

```java
class Solution {
    public String reverseStr(String s, int k) {
        char[] arr = s.toCharArray();

        for (int i = 0; i < arr.length; i += 2 * k) {
            int left = i;
            int right = Math.min(i + k - 1, arr.length - 1);

            while (left < right) {
                char temp = arr[left];
                arr[left] = arr[right];
                arr[right] = temp;

                left++;
                right--;
            }
        }

        return new String(arr);
    }
}
```

---

**Runtime** 1 ms (beats 96.4%) · **Memory** 44.6 MB (beats 77.1%)

<sub>Synced by AILeetHub on 2026-09-30.</sub>
