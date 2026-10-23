# 1154. Day of the Year

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/day-of-the-year/)

`Math` · `String`

## Intuition  
The day‑of‑year can be obtained by adding the lengths of all months that precede the given month and then adding the day of the month itself. The only wrinkle is February’s length, which depends on whether the year is a leap year. A naïve solution might iterate over every date from Jan 1 to the target, costing O(n) time for n days, or store a pre‑computed table for each year. Recognizing that the month lengths are fixed except for February lets us compute the answer in a single pass over at most 11 month entries, eliminating any extra passes or hash‑lookups. This is a classic “fixed‑size table + conditional adjustment” pattern.

## Approach  
1. **Parse the input** – `date.split("-")` yields three strings; `Integer.parseInt` converts them to `year`, `month`, and `day`.  
2. **Initialize month lengths** – an array `days` holds the standard lengths for the 12 months, with February set to 28.  
3. **Adjust for leap year** – if `year % 400 == 0` **or** (`year % 4 == 0` **and** `year % 100 != 0`), set `days[1] = 29`. The condition follows the Gregorian leap‑year rule; using `||` and `&&` guarantees the correct exclusive cases.  
4. **Start the accumulator** – `result` is initialized to `day`, because the current month’s contribution is already known.  
5. **Accumulate preceding months** – a `for` loop runs `i` from `0` up to `month‑2` (`i < month - 1`). Each iteration adds `days[i]` to `result`. The loop invariant is: after processing index `i`, `result` equals the sum of `day` plus the lengths of months `1 … i+1`. The loop stops exactly before the target month, handling both even and odd month numbers without off‑by‑one errors.  
6. **Return** – `result` now holds the total number of days elapsed since Jan 1, which is returned.

Edge cases are covered explicitly:  
- A single‑day date (`month == 1`) skips the loop because `month‑1` is zero.  
- Leap‑year adjustment occurs before the loop, so February’s extra day is counted automatically when `month > 2`.  

## Dry Run  
**Input:** `date = "2019-03-01"`  

| Iteration | i | result before | days[i] added | result after | Note |
|-----------|---|---------------|---------------|--------------|------|
| init      | – | –             | –             | 1            | `result = day` (1) |
| 1         | 0 | 1             | 31            | 32           | January (31) |
| 2         | 1 | 32            | 28            | 60           | February (non‑leap) |
| exit      | – | –             | –             | 60           | Loop ends (`i = 2` == `month‑1`) |

Final `result = 60`, which is the 60th day of 2019, matching the expected answer.

## Complexity  
- **Time:** O(1) – the loop iterates at most 11 times (the number of months before December), independent of the input size.  
- **Space:** O(1) – only a fixed‑size array of 12 integers and a few scalar variables are allocated; the output integer does not count toward extra space.

## Solution (Java)

```java
class Solution {
    public int dayOfYear(String date) {
        String[] parts = date.split("-");

        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int day = Integer.parseInt(parts[2]);

        int[] days = {31, 28, 31, 30, 31, 30, 
                      31, 31, 30, 31, 30, 31};

        // Check for leap year
        if (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0)) {
            days[1] = 29;
        }

        int result = day;

        for (int i = 0; i < month - 1; i++) {
            result += days[i];
        }

        return result;
    }
}
```

---

**Runtime** 19 ms (beats 13.3%) · **Memory** 46.7 MB (beats 91.7%)

<sub>Synced by AILeetHub on 2026-10-23.</sub>
