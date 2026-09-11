# 3483. Unique 3-Digit Even Numbers

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/unique-3-digit-even-numbers/)

`Array` · `Hash Table` · `Recursion` · `Enumeration`

## Intuition  
The key observation is that a three‑digit even number is completely determined by three independent choices: an even digit for the units place, a non‑zero digit for the hundreds place, and any remaining digit for the tens place. Because each digit may appear only as many times as it occurs in the input, we can keep a frequency table of size 10 and decrement counts as we pick a digit. A naïve approach would enumerate all 3‑permutations of the array and then filter by parity and leading‑zero rules, which costs O(n³) and requires extra bookkeeping for duplicates. By fixing the last digit first (the only place where evenness matters) and using the frequency array to enforce availability, we eliminate the need for sorting, hashing, or a separate pass.

## Approach  
1. **Build frequency table** – Iterate over `digits` and increment `freq[d]`. After this step `freq[x]` equals the number of copies of digit `x` we can still use.  
2. **Choose the last digit** – Loop `last` from 0 to 8 stepping by 2.  
   *Exit condition*: `last` exceeds 8.  
   *Invariant*: before each iteration `freq` reflects the original counts; after decrementing `freq[last]` we have reserved one copy of an even digit for the units place. If `freq[last]` is zero we skip because no such digit exists.  
3. **Choose the first digit** – Loop `first` from 1 to 9.  
   *Exit condition*: `first` exceeds 9.  
   *Invariant*: `freq[first]` is positive, meaning a non‑zero digit is still available after reserving the last digit. We decrement `freq[first]` to reserve it for the hundreds place.  
4. **Choose the middle digit** – Loop `middle` from 0 to 9.  
   *Exit condition*: `middle` exceeds 9.  
   *Invariant*: `freq[middle]` reflects the remaining pool after fixing last and first digits. If `freq[middle] > 0` we have at least one copy left, so a distinct three‑digit even number can be formed; we increment `ans`. No further decrement is needed because the middle digit is the final choice.  
5. **Restore counts** – After the inner loop finishes, increment `freq[first]` to undo the reservation of the first digit, then after the outermost loop finishes, increment `freq[last]` to restore the even digit. This ensures each combination is counted exactly once and avoids off‑by‑one errors when the same digit appears multiple times.  
6. **Return result** – `ans` now holds the total number of distinct valid numbers.

## Dry Run  
Input: `digits = [0,2,2]`

| step | last | first | middle | freq after picks | note |
|------|------|-------|--------|------------------|------|
| 1    | 0    | 2     | 0      | freq[0]=0, freq[2]=1 | `last=0` (even) reserved, `first=2` reserved, `middle=0` available → ans=1 |
| 2    | 0    | 2     | 2      | freq[0]=0, freq[2]=0 | `middle=2` still available (second copy) → ans=2 |
| 3    | 2    | 0 (skip) | – | – | `first` loop starts at 1, so `first=0` is never considered (no leading zero) |
| 4    | 2    | 2     | 0      | freq[2]=0, freq[0]=0 | `last=2` reserved, `first=2` reserved, `middle=0` available → ans=3 |
| 5    | 2    | 2     | 2      | freq[2]=0, freq[2]=0 | `middle=2` not available (no remaining copy) → no increment |

After processing all loops, `ans = 2`, corresponding to the numbers **202** and **220**, which matches the expected output.

## Complexity  
- **Time:** O(1) ≈ O(10·10·10) because the three nested loops iterate over a constant range of digit values (0‑9).  
- **Space:** O(1) – only the fixed‑size `freq[10]` array and a few integer variables are used, independent of the input size. (The output is a single integer, not counted toward extra space.)

## Solution (Java)

```java
class Solution {
    public int totalNumbers(int[] digits) {
        int[] freq = new int[10];

        for (int d : digits) {
            freq[d]++;
        }

        int ans = 0;

        // Choose the last digit: must be even
        for (int last = 0; last <= 8; last += 2) {

            if (freq[last] == 0) continue;

            freq[last]--;

            // Choose first digit: 1-9
            for (int first = 1; first <= 9; first++) {

                if (freq[first] == 0) continue;

                freq[first]--;

                // Choose middle digit: 0-9
                for (int middle = 0; middle <= 9; middle++) {
                    if (freq[middle] > 0) {
                        ans++;
                    }
                }

                freq[first]++;
            }

            freq[last]++;
        }

        return ans;
    }
}
```

---

**Runtime** 1 ms (beats 100.0%) · **Memory** 43.4 MB (beats 99.7%)

<sub>Synced by AILeetHub on 2026-09-11.</sub>
