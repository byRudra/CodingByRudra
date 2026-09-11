# 3483. Unique 3-Digit Even Numbers

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/unique-3-digit-even-numbers/)

`Array` · `Hash Table` · `Recursion` · `Enumeration`

## Intuition  
The only restriction that makes a three‑digit number valid is the parity of its last digit and the non‑zero requirement for the first digit. If we know which digit occupies the units place (it must be even) and which digit occupies the hundreds place (it must be non‑zero), the tens place can be any remaining digit, provided we still have at least one copy left. By counting how many choices remain for the middle digit after fixing the first two, we obtain the total number of distinct numbers without needing to generate them explicitly. This eliminates the need for a second pass, a hash‑set of generated numbers, or recursion over permutations. The pattern is a **frequency‑array enumeration**.

## Approach  
1. **Build frequency table** –  
   Iterate over `digits` and increment `freq[digit]`. After this step `freq[d]` holds the exact count of digit `d` available for use.  
2. **Choose the last (units) digit** –  
   Loop `last` from `0` to `8` stepping by `2`. The loop exits after `last = 8`. Invariant: at the start of each iteration `freq` still reflects the original multiset. If `freq[last] == 0` we skip because the digit is unavailable. Otherwise we decrement `freq[last]` to reserve it.  
3. **Choose the first (hundreds) digit** –  
   Loop `first` from `1` to `9`. The loop stops after `first = 9`. Invariant: after reserving `last`, `freq[first]` tells how many copies of `first` remain. If `freq[first] == 0` we continue; otherwise we decrement `freq[first]` to reserve the first digit.  
4. **Count possible middle digits** –  
   Loop `middle` from `0` to `9`. The loop ends after `middle = 9`. For each `middle` we simply test `freq[middle] != 0`. If true, a distinct three‑digit even number exists with the current `last` and `first`, so we increment `count`. No further decrement is needed because we are only counting, not constructing the number.  
5. **Restore state** –  
   After the inner `middle` loop finishes, we increment `freq[first]` to undo the reservation of the first digit. After the outer `last` loop finishes, we increment `freq[last]` to restore the original frequency table before the next candidate for the units place.  
6. **Return result** –  
   `count` now holds the total number of distinct three‑digit even numbers that can be formed.

Edge handling: the input length is guaranteed ≥ 3, so there is always enough digits to attempt a number. The loops deliberately use `<=` bounds (`first <= 9`, `middle <= 9`) because digit values range inclusively from 0 to 9. The `last` loop stops at 8 because 10 is not a digit; stepping by 2 guarantees we only examine even digits.

## Dry Run  
**Input:** `digits = [0, 2, 2]`

| iteration | last | freq after reserving last | first | freq after reserving first | middle examined | count change | note |
|-----------|------|---------------------------|-------|----------------------------|-----------------|--------------|------|
| 1 | 0 (even) | freq[0]=0 (used) | 1 → skip (freq[1]=0) | – | – | 0 | first digit cannot be 0 |
| 2 | 0 | freq[0]=0 | 2 (available) | freq[2]=1 (used) | middle=0 → freq[0]=0 → no inc | 0 | middle 0 unavailable |
| 3 | 0 | – | 2 | freq[2]=1 | middle=2 → freq[2]=1 → count++ | 1 | number 202 |
| 4 | 0 | – | 2 | freq[2]=1 | middle=…9 (only 2 works) | – | end middle loop |
| 5 | 2 (even) | freq[2]=1 (used) | 1 → skip | – | – | 1 | first digit cannot be 0 |
| 6 | 2 | freq[2]=1 | 2 (available) | freq[2]=0 (used) | middle=0 → freq[0]=1 → count++ | 2 | number 220 |
| 7 | 2 | – | 2 | freq[2]=0 | middle=2 → freq[2]=0 → no inc | – | end middle loop |
| 8 | loop ends | – | – | – | – | – | |

Final `count = 2`, matching the two valid numbers 202 and 220.

## Complexity  
- **Time:** O(n + 10³) = O(n). Building the frequency array scans `digits` once (O(n)), and the three nested loops each run over a constant range of size 10, contributing a constant 1,000 operations.  
- **Space:** O(1). The algorithm uses a fixed‑size array `freq[10]` and a few integer variables, independent of input size. (The output is a single integer, not counted toward extra space.)

## Solution (Java)

```java
class Solution {
    public int totalNumbers(int[] digits) {
        int freq[] = new int[10];
        for (int digit : digits) {
            freq[digit]++;
        }

        int count = 0;

        // Find last Must be EVEN

        for (int last = 0; last <= 8; last += 2) {
            if (freq[last] == 0)
                continue;
            freq[last]--;

            // Find First Must not be 0
            for (int first = 1; first <= 9; first++) {
                if (freq[first] == 0)
                    continue;

                freq[first]--;

                // Find the Middle
                for (int middle = 0; middle <= 9; middle++) {
                    if (freq[middle] != 0)
                        count++;
                }

                freq[first]++;
            }

            freq[last]++;

        }
        return count;
    }
}
```

---

**Runtime** 1 ms (beats 100.0%) · **Memory** 44.1 MB (beats 88.7%)

<sub>Synced by AILeetHub on 2026-09-11.</sub>
