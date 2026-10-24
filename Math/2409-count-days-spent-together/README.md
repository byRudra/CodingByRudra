# 2409. Count Days Spent Together

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/count-days-spent-together/)

`Math` · `String`

## Intuition  
The only thing that matters for two intervals on a calendar is their numeric positions along the year. If we can map every “MM‑DD” string to the day‑of‑year count, the overlap becomes a simple one‑dimensional interval problem. A naïve solution would compare every pair of dates or store a boolean array for the whole year, both of which waste O(365) space or extra passes. The key observation is that the intersection of two closed intervals `[a₁, b₁]` and `[a₂, b₂]` is `[max(a₁,a₂), min(b₁,b₂)]`, and its length is `max(0, min‑max+1)`. This reduces the problem to two constant‑time arithmetic operations after a linear‑time conversion of each date. The pattern used is **interval intersection via numeric mapping**.

## Approach  
1. **Convert a date string to a day index** – `toDays(date)`.  
   - Parse `month` from `date.charAt(0..1)` and `day` from `date.charAt(3..4)`.  
   - Initialise `total = day`.  
   - Loop `i` from `0` to `month‑2` (inclusive). In each iteration add `daysInMonth[i]` to `total`.  
   - Invariant: after processing index `i`, `total` equals the number of days from Jan 1 up to the current month‑`i+1` day.  
   - The loop ends when all months preceding the target month have been summed, yielding the exact day‑of‑year for the input.
2. **Compute the later start** – `start = Math.max(toDays(arriveAlice), toDays(arriveBob))`.  
   - Guarantees `start` is the first day both could possibly be together.  
3. **Compute the earlier end** – `end = Math.min(toDays(leaveAlice), toDays(leaveBob))`.  
   - Guarantees `end` is the last day both could possibly be together.  
4. **Derive the overlap length** – `Math.max(0, end - start + 1)`.  
   - If `end < start` the intervals do not intersect, so the outer `max` clamps the result to 0.  
   - The `+1` accounts for inclusive endpoints.  
5. **Return the result** – the number of days Alice and Bob share in Rome.

Edge handling:  
- Empty or single‑day intervals are naturally covered because `toDays` works for any valid month/day.  
- When the intervals touch at a single day, `end - start + 1` evaluates to 1.  
- The code never overflows because the maximum day index is 365, well within `int` range.

## Dry Run  
Input: `arriveAlice = "08-15", leaveAlice = "08-18", arriveBob = "08-16", leaveBob = "08-19"`

We trace `toDays("08-15")` (the same conversion is performed for the other three dates).

| i (loop index) | month‑prefix added | total after addition | note                              |
|----------------|--------------------|----------------------|-----------------------------------|
| 0              | Jan 31             | 46 (=15+31)          | after processing January          |
| 1              | Feb 28             | 74 (=46+28)          | after processing February         |
| 2              | Mar 31             | 105 (=74+31)         | after processing March            |
| 3              | Apr 30             | 135 (=105+30)        | after processing April            |
| 4              | May 31             | 166 (=135+31)        | after processing May              |
| 5              | Jun 30             | 196 (=166+30)        | after processing June             |
| 6              | Jul 31             | 227 (=196+31)        | after processing July; loop ends |
| –              | –                  | **227**              | day‑of‑year for 08‑15             |

Similarly, `toDays("08-18") = 230`, `toDays("08-16") = 228`, `toDays("08-19") = 231`.  
`start = max(227, 228) = 228`, `end = min(230, 231) = 230`.  
Overlap = `max(0, 230‑228+1) = 3`, which matches the expected answer.

## Complexity  
- **Time:** O(1) overall. Each `toDays` runs a loop of at most 11 iterations (the months before the given one), so the four conversions cost a constant amount, and the remaining arithmetic is O(1).  
- **Space:** O(1) extra space. Only a few integer variables (`month`, `day`, `total`, loop index) are used; the output array is not counted.

## Solution (Java)

```java
class Solution {
    public int countDaysTogether(String arriveAlice, String leaveAlice, String arriveBob, String leaveBob) {
        int start = Math.max(toDays(arriveAlice), toDays(arriveBob));
        int end = Math.min(toDays(leaveAlice), toDays(leaveBob));

        return Math.max(0, end - start + 1);
    }
    private int toDays(String date){
        int[] daysInMonth = {31, 28, 31, 30, 31, 30,
                             31, 31, 30, 31, 30, 31 };
        int month = (date.charAt(0) - '0') * 10 + (date.charAt(1) - '0');
        int day = (date.charAt(3) - '0') * 10 + (date.charAt(4) - '0');

        // calculating days from the start of the year
        int total = day;
        for(int i = 0; i < month - 1; i++){
            total += daysInMonth[i];
        }
        return total;
    }
}
```

---

**Runtime** 0 ms (beats 100.0%) · **Memory** 42.4 MB (beats 91.1%)

<sub>Synced by AILeetHub on 2026-10-24.</sub>
