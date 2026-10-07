# 1688. Count of Matches in Tournament

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/count-of-matches-in-tournament/)

`Math` · `Simulation`

## Intuition  
In every match exactly one team is eliminated, regardless of whether the round starts with an even or odd number of teams. Consequently, to shrink the field from **n** teams down to a single champion we must discard **n − 1** teams, which means exactly **n − 1** matches are played. The naïve simulation that repeatedly computes `matches = n / 2` (or `(n‑1)/2`) and updates `n` would run in O(n) time, but the invariant “each match reduces the team count by one” eliminates the need for any loop or data structure. The solution therefore collapses to a simple arithmetic formula.

## Approach  
1. **Read the input** `n`.  
2. **Compute** `n - 1`. This expression directly encodes the invariant that each match eliminates one team.  
3. **Return** the computed value (`return n - 1;`).  
   - The only special case is `n == 1`; the formula yields `0`, which correctly represents that no match is required when there is already a winner.  
   - No branching on even/odd, no iteration, and no auxiliary containers are needed because the problem’s rules guarantee exactly one elimination per match.

## Dry Run  
Consider the minimal non‑trivial tournament `n = 7`.

| Step | Teams before | Teams after match | Total matches so far | Note |
|------|--------------|-------------------|----------------------|------|
| 0    | 7            | 7                 | 0                    | initial state |
| 1    | 7 → 6        | 6                 | 1                    | one match eliminates one team |
| 2    | 6 → 5        | 5                 | 2                    | repeat elimination |
| 3    | 5 → 4        | 4                 | 3                    | |
| 4    | 4 → 3        | 3                 | 4                    | |
| 5    | 3 → 2        | 2                 | 5                    | |
| 6    | 2 → 1        | 1                 | 6                    | last match produces the champion |

After six eliminations the tournament reaches a single team, so the answer is `6`, which equals `7 − 1`. The same reasoning holds for any `n`.

## Complexity  
- **Time:** **O(1)** – the method performs a single subtraction and a return; there is no loop that depends on `n`.  
- **Space:** **O(1)** – only a few primitive variables are used, and the output array or recursion stack is irrelevant because the algorithm is purely arithmetic.

## Solution (Java)

```java
class Solution {
    public int numberOfMatches(int n) {
        return n - 1;
    }
}
```

---

**Runtime** 0 ms (beats 100.0%) · **Memory** 42.3 MB (beats 34.8%)

<sub>Synced by AILeetHub on 2026-10-07.</sub>
