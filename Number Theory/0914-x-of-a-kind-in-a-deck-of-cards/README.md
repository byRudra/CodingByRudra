# 914. X of a Kind in a Deck of Cards

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/x-of-a-kind-in-a-deck-of-cards/)

`Array` · `Hash Table` · `Math` · `Counting` · `Number Theory` · `Euclidean Algorithm` · `Greatest Common Divisor`

## Intuition  
The key observation is that a valid partition exists **iff** the frequency of every distinct card value shares a common divisor ≥ 2. Once we know each value’s count, the greatest common divisor (GCD) of all those counts tells us the largest group size that can be used for every value simultaneously. A naïve solution would try every possible x or store all divisors, which costs extra passes or memory. By reducing the problem to a single GCD computation we eliminate those overheads. This is a classic “frequency‑GCD” pattern.

## Approach  
1. **Count frequencies** – Iterate over `deck` with `for (int num : deck)`.  
   *Exit condition*: loop ends after the last element.  
   *Invariant*: after processing the first *k* elements, `map` contains exactly the occurrence count of each value seen so far.  
   The code updates `map` via `map.put(num, map.getOrDefault(num, 0) + 1)`.  
2. **Aggregate GCD** – Initialise `int g = 0`. Iterate over `map.values()` with `for (int count : map.values())`.  
   *Exit condition*: loop finishes when every distinct value’s count has been visited.  
   *Invariant*: after processing the first *i* counts, `g` equals the GCD of those *i* counts.  
   Inside the loop we compute `g = gcd(g, count)`. Starting from 0 works because `gcd(0, x) = x`.  
3. **Decision** – Return `g >= 2`. If the final GCD is at least 2, we can choose `x = g` (or any divisor of `g` ≥ 2) as the group size; otherwise no such x exists.  
4. **GCD helper** – The recursive method `gcd(int a, int b)` implements Euclid’s algorithm: `return b == 0 ? a : gcd(b, a % b);`. It terminates because each recursive call reduces the second argument.

Edge handling:  
- Empty or single‑element `deck` yields `g` equal to the sole count (1) → `false`.  
- Even vs. odd length is irrelevant; the GCD captures divisibility regardless of parity.  
- The `<=` check is not used; the final comparison is `g >= 2`, which correctly excludes the trivial divisor 1.

## Dry Run  

**Input**: `deck = [1,2,3,4,4,3,2,1]`

| Step | Action                              | `map` (value → count)                     | `g` | Note                                   |
|------|-------------------------------------|-------------------------------------------|-----|----------------------------------------|
| 1    | process `1`                         | {1→1}                                      | 0   | first frequency added                 |
| 2    | process `2`                         | {1→1, 2→1}                                 | 0   |                                         |
| 3    | process `3`                         | {1→1, 2→1, 3→1}                            | 0   |                                         |
| 4    | process `4`                         | {1→1, 2→1, 3→1, 4→1}                       | 0   |                                         |
| 5    | process second `4`                  | {1→1, 2→1, 3→1, 4→2}                       | 0   |                                         |
| 6    | process second `3`                  | {1→1, 2→1, 3→2, 4→2}                       | 0   |                                         |
| 7    | process second `2`                  | {1→1, 2→2, 3→2, 4→2}                       | 0   |                                         |
| 8    | process second `1`                  | {1→2, 2→2, 3→2, 4→2}                       | 0   | All frequencies collected             |
| 9    | start GCD aggregation (`g=0`)       | –                                           | 0   | `g` will be updated with each count    |
| 10   | `g = gcd(0,2)` → 2                  | –                                           | 2   | First count sets `g` to 2              |
| 11   | `g = gcd(2,2)` → 2                  | –                                           | 2   | GCD unchanged                         |
| 12   | `g = gcd(2,2)` → 2                  | –                                           | 2   | GCD unchanged                         |
| 13   | `g = gcd(2,2)` → 2                  | –                                           | 2   | GCD unchanged, loop ends               |

Final `g = 2`, and `g >= 2` evaluates to `true`. Hence the deck can be partitioned into groups of size 2.

## Complexity  
- **Time:** O(n + m) ≈ O(n), where *n* is `deck.length` (first loop) and *m* is the number of distinct values (second loop). The GCD computation is constant‑time per iteration because Euclid’s algorithm runs in O(log min(a,b)).  
- **Space:** O(m) for the hash map storing frequencies; no extra recursion depth beyond the GCD helper, which uses O(log maxCount) stack space, negligible compared to the output.

## Solution (Java)

```java
class Solution {
    public boolean hasGroupsSizeX(int[] deck) {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int num : deck) {
            map.put(num, map.getOrDefault(num, 0) + 1);
        }
        
        int g = 0;
        for (int count : map.values()) {
            g = gcd(g, count);
        }
        
        return g >= 2;
    }
    
    private int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }
}
```

---

**Runtime** 11 ms (beats 63.9%) · **Memory** 47.4 MB (beats 42.8%)

<sub>Synced by AILeetHub on 2026-09-11.</sub>
