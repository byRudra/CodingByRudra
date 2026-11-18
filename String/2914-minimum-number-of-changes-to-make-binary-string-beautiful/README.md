# 2914. Minimum Number of Changes to Make Binary String Beautiful

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/minimum-number-of-changes-to-make-binary-string-beautiful/)

`String`

## Intuition  
In a beautiful binary string every allowed substring has even length and consists of identical characters, which forces each *adjacent* pair of positions to be equal. Consequently, the whole string can be viewed as a sequence of disjoint pairs `(s[0],s[1]), (s[2],s[3]), …`. If a pair already contains two equal bits, it needs no modification; if the bits differ, flipping either one makes the pair uniform, and exactly one change is sufficient. The naïve idea of trying all possible partitions or using a DP would be O(n²) or require extra memory, but the pair‑wise observation eliminates any need for additional passes, maps, or recursion. This is a classic **two‑pointer / pair‑wise** pattern.

## Approach  
1. **Validate length** – The code first checks `s.length() % 2 != 0`. If the length is odd it returns `-1` (the problem guarantees even length, so this guard is defensive).  
2. **Initialize counter** – `int changes = 0;` will accumulate the required flips.  
3. **Iterate over pairs** – `for (int i = 0; i < s.length() - 1; i += 2)`  
   - *Exit condition*: the loop stops when `i` reaches `s.length() - 1`, i.e., after processing the last complete pair.  
   - *Invariant*: at the start of each iteration, all pairs with starting index `< i` have already been examined and `changes` equals the number of mismatched pairs among them.  
   - Inside the body, `if (s.charAt(i) != s.charAt(i + 1)) changes++;` counts a mismatch. The `!=` test is the only place where a decision could be off‑by‑one; using `i + 1` is safe because the loop bound guarantees `i + 1` is a valid index.  
4. **Return result** – After the loop, `changes` holds the minimal flips, and the method returns it.

Key pitfalls avoided:  
- Not handling an empty string (not possible under constraints).  
- Forgetting to step by two, which would double‑count pairs.  
- Using `< s.length()` instead of `< s.length() - 1` would cause an out‑of‑bounds access on the final iteration.  

## Dry Run  

**Input:** `s = "1001"`  

| i | pair `s[i]s[i+1]` | `changes` before | `changes` after | note |
|---|-------------------|------------------|-----------------|------|
| 0 | `10`              | 0                | 1               | bits differ → increment |
| 2 | `01`              | 1                | 2               | bits differ → increment |
| – | –                 | 2                | 2               | loop ends (`i = 4`) |

After processing both pairs, `changes = 2`, which is the minimal number of flips needed (e.g., change the second and fourth characters to obtain `"1100"`).

## Complexity  
- **Time:** `O(n)` – the single `for` loop visits each character exactly once, advancing `i` by two each iteration, so it runs `n/2` times.  
- **Space:** `O(1)` – only a few primitive variables (`changes`, `i`) are used; the output integer does not count toward auxiliary space.

## Solution (Java)

```java
class Solution {
    public int minChanges(String s) {
        if(s.length() % 2 != 0) return -1;
        int changes = 0;
        for(int i = 0; i < s.length() - 1; i+=2){
            if(s.charAt(i) != s.charAt(i + 1)) changes++;
        }
        return changes;
    }
}
```

---

**Runtime** 3 ms (beats 97.6%) · **Memory** 45.7 MB (beats 98.2%)

<sub>Synced by AILeetHub on 2026-11-18.</sub>
