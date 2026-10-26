# 1071. Greatest Common Divisor of Strings

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/greatest-common-divisor-of-strings/)

`Math` · `String` · `Euclidean Algorithm` · `Greatest Common Divisor`

## Intuition  
If two strings can be built by repeating the same smaller pattern, then concatenating them in either order yields the identical long string; otherwise the order matters and the concatenations differ. This observation lets us discard any candidate that does not satisfy the commutative concatenation test, eliminating the need for explicit enumeration of all divisors. Once the test passes, the length of the greatest common divisor string must be the greatest common divisor of the two lengths, because any common divisor’s length must divide both lengths. The solution therefore reduces to a single equality check followed by the Euclidean algorithm on the lengths – a classic two‑pointer‑free, constant‑space pattern.

## Approach  
1. **Concatenation check** – Compute `str1 + str2` and `str2 + str1`.  
   *Exit condition:* the `if` statement finishes when the two results differ.  
   *Invariant:* while the strings share a common divisor, the two concatenations are equal; a mismatch proves no common divisor exists, so we return `""`.  
2. **Length GCD** – Call `gcd(str1.length(), str2.length())`.  
   *Recursive invariant:* `gcd(a, b)` always returns the greatest integer that divides both `a` and `b`. The recursion stops when `b == 0`.  
   *Why recursion works:* each call replaces the pair `(a, b)` with `(b, a % b)`, preserving the set of common divisors (Euclidean algorithm).  
3. **Extract prefix** – Return `str1.substring(0, g)`, where `g` is the length obtained in step 2.  
   *Decision details:* we use `str1` (instead of `str2`) because after the equality test both strings start with the same pattern, so any prefix of length `g` from either string is a valid divisor.  

Edge handling: the constraints guarantee non‑empty inputs, so no extra guard for empty strings is needed. The equality test works for odd/even lengths alike; the Euclidean recursion safely terminates because `a % b < b`, preventing infinite loops.

## Dry Run  
**Input:** `str1 = "ABCABC"`, `str2 = "ABC"`  

| step | a (len str1) | b (len str2) | a % b | note |
|------|--------------|--------------|------|------|
| 1    | 6            | 3            | 0    | `gcd(6,3)` → recurse with `(3,0)` |
| 2    | 3            | 0            | –    | base case `b==0`, return `a = 3` |

After the equality check passes (`"ABCABCABC"` equals `"ABCABCABC"`), the recursion yields `g = 3`. `str1.substring(0,3)` produces `"ABC"`, which indeed divides both original strings.

## Complexity  
- **Time:** O(n + m + log min(n,m)) – the concatenation comparison scans both strings once (n + m), and the Euclidean algorithm runs in O(log min(n,m)) recursive steps.  
- **Space:** O(1) – only a few integer variables and the recursion stack of depth O(log min(n,m)); the output string is not counted against the auxiliary space.

## Solution (Java)

```java
class Solution {
    public String gcdOfStrings(String str1, String str2) {
        if (!(str1 + str2).equals(str2 + str1)) {
            return "";
        }
        int g = gcd(str1.length(), str2.length());
        return str1.substring(0, g);
    }

    private int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }
}
```

---

**Runtime** 1 ms (beats 91.1%) · **Memory** 43.1 MB (beats 90.3%)

<sub>Synced by AILeetHub on 2026-10-26.</sub>
