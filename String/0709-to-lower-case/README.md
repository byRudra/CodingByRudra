# 709. To Lower Case

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/to-lower-case/)

`String`

## Intuition  
The only work required is to map every uppercase ASCII letter to its lowercase counterpart. Java’s standard library already provides a reliable, locale‑aware routine that does exactly this in a single pass, so we can avoid writing an explicit loop, a lookup table, or any arithmetic on character codes. The naive approach would be to scan the string, test each character with `Character.isUpperCase`, and build a new string manually—an O(n) pass with extra bookkeeping. By delegating to `String.toLowerCase()` we eliminate that boilerplate while preserving the same linear time guarantee. This is an instance of the **standard library shortcut** pattern.

## Approach  
1. **Invoke the built‑in conversion** – `s.toLowerCase()` is called on the input string `s`.  
   - *Exit condition*: The method returns when it has examined every code unit of `s`.  
   - *Invariant*: After processing the first *k* characters, the returned prefix equals the lowercase version of the original prefix.  
2. **Return the result** – the newly created string, which contains the lower‑cased characters, is returned directly to the caller.  

The implementation deliberately relies on the default locale, which for the problem’s ASCII constraint yields a simple offset (`'A'` → `'a'`). Edge cases such as an empty string (not allowed by the constraints) or a string containing no uppercase letters are handled automatically: `toLowerCase()` returns the original reference when no changes are needed, avoiding unnecessary allocation.

## Dry Run  
Input: `s = "HeLLo"`  

| step | index | original char | result char | note |
|------|-------|---------------|------------|------|
| 1    | 0     | H             | h          | uppercase → lowercase |
| 2    | 1     | e             | e          | already lowercase |
| 3    | 2     | L             | l          | uppercase → lowercase |
| 4    | 3     | L             | l          | uppercase → lowercase |
| 5    | 4     | o             | o          | already lowercase |

After processing all five characters, the accumulated result is `"hello"`, which the method returns as the final answer.

## Complexity  
- **Time:** O(n) – the library call walks the string once, converting each character, where *n* is `s.length()`.  
- **Space:** O(n) – a new string of length *n* is allocated to hold the lower‑cased characters; no additional auxiliary structures are used.

## Solution (Java)

```java
class Solution {
    public String toLowerCase(String s) {
        return s.toLowerCase();
    }
}
```

---

**Runtime** 0 ms (beats 100.0%) · **Memory** 42.5 MB (beats 97.8%)

<sub>Synced by AILeetHub on 2026-09-25.</sub>
