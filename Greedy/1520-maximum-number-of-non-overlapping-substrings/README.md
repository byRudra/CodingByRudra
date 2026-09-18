# 1520. Maximum Number of Non-Overlapping Substrings

![Hard](https://img.shields.io/badge/Difficulty-Hard-ff375f?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/maximum-number-of-non-overlapping-substrings/)

`Hash Table` · `String` · `Greedy` · `Sorting`

## Intuition  
For any character c, all occurrences of c must lie inside the same chosen substring, so the smallest possible substring that can contain c is the interval \[first[c], last[c]\]. If that interval also contains another character d whose own interval stretches beyond the current right bound, the substring must be expanded to include the whole interval of d. Repeating this expansion yields a *closed* interval that cannot be shortened without violating the “contain all occurrences” rule. An interval is a valid candidate only when no character inside it appears earlier than the interval’s left edge; otherwise the interval would have to start earlier and is therefore discarded. Once all closed, valid intervals are collected, the problem reduces to selecting the maximum number of non‑overlapping intervals with minimal total length – a classic greedy choice after sorting by right endpoint.

## Approach  
1. **Record extremes** – Scan the string once. For each letter `idx = s.charAt(i) - 'a'` store `first[idx]` (first index, initialised to -1) and `last[idx]` (most recent index).  
2. **Generate candidates** – For every letter `c` that appears (`first[c] != -1`):  
   a. Initialise `start = first[c]`, `end = last[c]`, `valid = true`.  
   b. Loop `i` from `start` **to** `end` (inclusive). The loop invariant: all positions `< i` are already guaranteed to belong to the final interval, and `end` is the farthest right index required so far.  
   c. For the character at `i` (`idx = s.charAt(i) - 'a'`):  
      - If `first[idx] < start`, a required occurrence lies left of the current interval → set `valid = false` and break.  
      - Otherwise update `end = Math.max(end, last[idx])` to absorb any farther occurrence.  
   d. After the loop, if `valid` remains true, add `s.substring(start, end + 1)` to `result`.  
3. **Sort by right edge** – Because `result` contains at most 26 strings, the comparator computes each interval’s end as `s.indexOf(str) + str.length()`. Sorting ensures a left‑to‑right scan can greedily pick the earliest finishing intervals.  
4. **Greedy selection** – Initialise `prevEnd = -1`. For each `str` in the sorted list: compute `start = s.indexOf(str)` and `end = start + str.length() - 1`. If `start > prevEnd`, the interval does not overlap the previously chosen one; append `str` to `answer` and set `prevEnd = end`.  

Edge handling: an empty or single‑character string yields a single interval that passes the validity test; the `<=` condition in the expansion loop is essential because `end` is inclusive. The algorithm deliberately chooses the “earliest finishing” convention to guarantee maximal count and minimal total length.

## Dry Run  
Input: `adefaddaccc`

| step | c (starting char) | start | end (dynamic) | i | action / change | note |
|------|-------------------|-------|---------------|---|-----------------|------|
| 1 | a | 0 | 8 | 0 | `first[a]=0`, `last[a]=8` | initial interval \[0,8\] |
| 2 | a | 0 | 8 | 1 (`d`) | `end = max(8,7)=8` | d’s last is 7, no change |
| 3 | a | 0 | 8 | 2 (`e`) | `first[e]=2 ≥ start` → keep, `end = max(8,2)=8` |
| 4 | a | 0 | 8 | 3 (`f`) | `first[f]=3 ≥ start`, `end = max(8,3)=8` |
| 5 | a | 0 | 8 | 4 (`a`) | nothing changes |
| 6 | a | 0 | 8 | 5 (`d`) | nothing changes |
| 7 | a | 0 | 8 | 6 (`d`) | nothing changes |
| 8 | a | 0 | 8 | 7 (`a`) | nothing changes |
| 9 | a | 0 | 8 | 8 (`c`) | `first[c]=9 > start`, `end = max(8,10)=10` → interval expands |
| 10 | a | 0 | 10 | 9 (`c`) | continue, `end` stays 10 |
| 11 | a | 0 | 10 | 10 (`c`) | loop ends, `valid` stays true → candidate `"adefaddaccc"` added |

Repeating the same process for `e`, `f`, and `c` yields valid intervals `"e"`, `"f"`, `"ccc"`. After sorting by end (`e` ends 2, `f` ends 3, `ccc` ends 10) the greedy pass picks all three, producing the final answer `["e","f","ccc"]`.

## Complexity  
- **Time:** O(n) – one linear scan builds `first`/`last`; each of the 26 characters expands an interval at most n steps total; sorting 26 items and the final greedy pass are negligible.  
- **Space:** O(1) extra – two fixed‑size arrays of length 26 and a few lists whose total size never exceeds 26, independent of input length. (The output list itself is not counted.)

## Solution (Java)

```java
class Solution {
    public List<String> maxNumOfSubstrings(String s) {
        int[] first = new int[26];
        int[] last = new int[26];

        Arrays.fill(first, -1);
        Arrays.fill(last, -1);

        // Find first and last occurrence
        for (int i = 0; i < s.length(); i++) {
            int idx = s.charAt(i) - 'a';

            if (first[idx] == -1) {
                first[idx] = i;
            }

            last[idx] = i;
        }

        List<String> result = new ArrayList<>();

        // Try every character as a starting point
        for (int c = 0; c < 26; c++) {

            if (first[c] == -1)
                continue;

            int start = first[c];
            int end = last[c];

            boolean valid = true;

            // Expand the interval
            for (int i = start; i <= end; i++) {

                int idx = s.charAt(i) - 'a';

                // This character appeared before our start
                if (first[idx] < start) {
                    valid = false;
                    break;
                }

                // Need to include its entire range
                end = Math.max(end, last[idx]);
            }

            if (valid) {
                result.add(s.substring(start, end + 1));
            }
        }

        // Sort by ending position
        result.sort((a, b) -> {
            int endA = s.indexOf(a) + a.length();
            int endB = s.indexOf(b) + b.length();
            return Integer.compare(endA, endB);
        });

        // Greedily choose non-overlapping intervals
        List<String> answer = new ArrayList<>();
        int prevEnd = -1;

        for (String str : result) {
            int start = s.indexOf(str);
            int end = start + str.length() - 1;

            if (start > prevEnd) {
                answer.add(str);
                prevEnd = end;
            }
        }

        return answer;
    }

}
```

---

**Runtime** 18 ms (beats 33.2%) · **Memory** 47.9 MB (beats 78.3%)

<sub>Synced by AILeetHub on 2026-09-18.</sub>
