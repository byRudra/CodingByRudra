# 409. Longest Palindrome

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/longest-palindrome/)

`Hash Table` · `String` · `Greedy`

## Intuition  
The length of any palindrome is determined by how many characters can be placed in symmetric pairs; each pair contributes two to the length. Any character that appears an odd number of times can still contribute its even part, and at most **one** odd‑count character may sit in the centre. A naïve solution would try every subset of characters or sort them, both of which cost extra passes or additional memory. The key insight is that we only need the frequency of each letter: from those frequencies we can greedily take all even contributions and, if we have not yet placed a centre, add a single odd character. This reduces the problem to a single linear scan plus a constant‑size frequency array – a classic greedy‑by‑count pattern.

## Approach  
1. **Count frequencies** – Create an integer array `freq[256]` (covers all ASCII letters). For each `c` in `s.toCharArray()`, execute `freq[c]++`.  
   *Invariant*: after processing the first *i* characters, `freq[x]` equals the number of occurrences of character `x` among those *i* characters.  
2. **Accumulate palindrome length** – Initialise `ans = 0`. Iterate over every `count` in `freq`.  
   a. Add the largest even number not exceeding `count`: `ans += count / 2 * 2`.  
   b. If `ans` is still even **and** `count` is odd, we can place one odd character in the centre, so `ans++`.  
   *Loop exit condition*: the `for` loop finishes when all 256 entries have been examined.  
   *Invariant*: after processing the first *k* entries, `ans` equals the maximum palindrome length that can be built using characters from those *k* entries, respecting the “at most one centre” rule.  
3. **Return result** – The final `ans` is the length of the longest possible palindrome.

**Edge handling** –  
- Empty or single‑character strings are handled automatically: the frequency loop yields `ans = 0` or `1` respectively.  
- Upper‑case and lower‑case letters map to distinct indices because the array is indexed by the raw ASCII code, preserving case sensitivity.  
- The condition `ans % 2 == 0 && count % 2 != 0` deliberately uses `<=`‑style logic (`ans` even) to ensure we add a centre **once**; subsequent odd counts are ignored because `ans` becomes odd after the first centre insertion.

## Dry Run  

Input: `s = "abccccdd"`

| Step | `c` (first loop) | `freq['a']` | `freq['b']` | `freq['c']` | `freq['d']` | `ans` after second loop iteration | Change |
|------|------------------|-------------|-------------|-------------|-------------|-----------------------------------|--------|
| 1    | –                | 1           | 1           | 4           | 2           | –                                 | frequency built |
| 2    | count=1 (`'a'`)  | 1           | –           | –           | –           | `ans = 0 + 0 = 0` (even, odd → `ans++`) → 1 | added centre |
| 3    | count=1 (`'b'`)  | –           | 1           | –           | –           | `ans = 1 + 0 = 1` (odd, odd → no centre) | unchanged |
| 4    | count=4 (`'c'`)  | –           | –           | 4           | –           | `ans = 1 + 4 = 5` (even part taken) | added pair block |
| 5    | count=2 (`'d'`)  | –           | –           | –           | 2           | `ans = 5 + 2 = 7` | added final pair block |
| 6‑256| remaining zeros  | –           | –           | –           | –           | `ans` stays 7 | no effect |

Final `ans = 7`, which matches the longest palindrome length.

## Complexity  
- **Time:** O(n + Σ) = O(n) because the first loop scans the string once (`n` characters) and the second loop iterates over a constant 256‑size array.  
- **Space:** O(1) extra space; the `freq` array has fixed size 256 regardless of input length, and the output integer does not count toward auxiliary space.

## Solution (Java)

```java
class Solution {
    public int longestPalindrome(String s) {
        int[] freq = new int[256];

        for (char c : s.toCharArray()) {
            freq[c]++;
        }
        int ans=0;
        for (int count : freq) {
            ans += count / 2 * 2;
            if (ans % 2 == 0 && count % 2 != 0)
                ans++;
        }
        return ans;
    }
}
```

---

**Runtime** 2 ms (beats 72.3%) · **Memory** 43.2 MB (beats 38.5%)

<sub>Synced by AILeetHub on 2026-07-13.</sub>
