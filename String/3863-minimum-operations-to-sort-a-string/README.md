# 3863. Minimum Operations to Sort a String

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/minimum-operations-to-sort-a-string/)

`String`

## Intuition  
The string can be sorted by moving only the smallest character to the front or the largest character to the back; any other rearrangement needs at least two operations. Hence the answer depends solely on where the global minimum (`mn`) and maximum (`mx`) characters appear. A naïve solution would try all substrings or simulate sorting, which is O(n²). The insight that the extremes alone decide the minimal count reduces the problem to a constant‑time decision after a single linear scan.

The pattern used is a **greedy extreme‑position check**.

## Approach  
1. **Check already sorted** – iterate `i` from 1 to `n‑1`; if `s.charAt(i) < s.charAt(i‑1)` the flag `sorted` becomes false. If the loop finishes without breaking, return 0.  
2. **Handle length‑2 edge** – when `n == 2` and the string is unsorted, no allowed substring can change the order, so return -1.  
3. **Find global extremes** – scan the whole string once, updating `mn` with the smallest seen character and `mx` with the largest.  
4. **One‑move possibility** – if the first character equals `mn` **or** the last character equals `mx`, a single substring that excludes the already‑correct extreme can be sorted, so return 1.  
5. **Detect interior extremes** – loop `i` from 1 to `n‑2`; set `minInside` true if `s.charAt(i) == mn`, and `maxInside` true if `s.charAt(i) == mx`.  
6. **Three‑move guarantee** – if neither `minInside` nor `maxInside` is true, both extremes are stuck at the ends but in the wrong places; the worst case needs three operations, so return 3.  
7. **Default two‑move case** – when at least one extreme appears inside the string but the ends are wrong, two operations suffice (first bring one extreme to its correct side, then finish sorting), so return 2.

Key decisions:  
- The check `i < n‑1` for the sorted test uses `<` because we compare each pair exactly once.  
- The length‑2 branch returns -1 because the only allowed substrings are single characters, which cannot change order.  
- The interior‑extreme scan excludes indices 0 and `n‑1` deliberately; placing an extreme already at an end would have been caught in step 4.

## Dry Run  
Input: `card`

| Step | Variables after step | Note |
|------|----------------------|------|
| 1. Sorted check | `sorted = false` (found `c > a`) | early exit not taken |
| 2. Length‑2 | `n = 4` → skip | |
| 3. Find extremes | `mn = 'a'`, `mx = 'r'` | scan all chars |
| 4. One‑move? | `s.charAt(0) = 'c' ≠ mn`, `s.charAt(3) = 'd' ≠ mx` → no return | |
| 5. Interior extremes | `minInside = true` (index 1), `maxInside = false` | only `a` appears inside |
| 6. Three‑move? | `!minInside && !maxInside` is false | |
| 7. Return | `2` | at least one extreme inside ⇒ two operations suffice |

The algorithm ends with `2`, matching the optimal sequence described in the statement.

## Complexity  
- **Time:** O(n) – the string is scanned three times at most (sorted check, min/max discovery, interior‑extreme check), each linear in `n`.  
- **Space:** O(1) – only a handful of primitive variables (`mn`, `mx`, booleans) are stored, independent of input size.

## Solution (Java)

```java
class Solution {
    public int minOperations(String s) {
        int n = s.length();

        // Already sorted?
        boolean sorted = true;
        for (int i = 1; i < n; i++) {
            if (s.charAt(i) < s.charAt(i - 1)) {
                sorted = false;
                break;
            }
        }
        if (sorted) return 0;

        // Length 2 and unsorted: only single-char substrings are allowed
        if (n == 2) return -1;

        char mn = 'z', mx = 'a';
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            if (c < mn) mn = c;
            if (c > mx) mx = c;
        }

        // One pass: min already in front, or max already at the end
        if (s.charAt(0) == mn || s.charAt(n - 1) == mx) return 1;

        // Check whether min or max appears in the middle (indices 1..n-2)
        boolean minInside = false, maxInside = false;
        for (int i = 1; i < n - 1; i++) {
            char c = s.charAt(i);
            if (c == mn) minInside = true;
            if (c == mx) maxInside = true;
        }

        // Neither extreme can be placed with a single move
        if (!minInside && !maxInside) return 3;

        return 2;
    }
}
```

---

**Runtime** 18 ms (beats 84.8%) · **Memory** 47.3 MB (beats 92.4%)

<sub>Synced by AILeetHub on 2026-10-21.</sub>
