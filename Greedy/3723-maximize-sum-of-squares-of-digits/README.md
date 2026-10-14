# 3723. Maximize Sum of Squares of Digits

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/maximize-sum-of-squares-of-digits/)

`Math` · `Greedy`

## Intuition  
The score of a digit is its square, and the square function is strictly increasing for non‑negative digits. Therefore, to maximize the total score we should allocate as much of the required digit‑sum as possible to the largest digit, 9. Each 9 contributes 81 to the score, which dominates any combination of smaller digits that sum to the same value. After using as many 9’s as the sum permits, at most one leftover digit (the remainder `sum % 9`) is needed; placing it next to the 9’s keeps the numeric value maximal because larger digits are left‑most. The remaining positions are filled with zeros, which do not affect the sum or the score. This observation eliminates the need for exhaustive search, sorting, or dynamic programming.

## Approach  
1. **Feasibility check** – If `sum > num * 9` the required digit‑sum cannot be achieved with `num` digits, so return `""`.  
2. **Compute maximal 9’s** – `countNine = sum / 9` gives the greatest number of 9’s that can appear without exceeding the sum.  
3. **Compute leftover digit** – `remaining = sum % 9` is the value of the single non‑9 digit (0 … 8).  
4. **Append 9’s** – Loop `for (int nines = 1; nines <= countNine; nines++)` appends a `'9'` each iteration.  
   *Invariant*: after `k` iterations, `answer` ends with `k` 9’s and its length equals `k`.  
5. **Append leftover digit** – If `remaining > 0`, execute `answer.append(remaining)`. This places the only non‑zero, non‑9 digit immediately after the block of 9’s, preserving the maximal lexical order.  
6. **Pad with zeros** – While `answer.length() < num`, run `answer.append('0')`.  
   *Invariant*: each iteration adds one `'0'` and increments the length by 1; the loop stops exactly when the length reaches `num`.  
7. **Return** – The built string already satisfies the digit count, digit sum, and yields the highest possible score; it is also the largest integer among ties.

Edge considerations:  
- When `sum` is a multiple of 9, `remaining` is 0 and step 5 is skipped.  
- `countNine` can never exceed `num` because the feasibility test guarantees `sum ≤ 9·num`.  
- For `num = 1` and `sum ≤ 9`, the algorithm returns the single digit itself; otherwise it returns `""`.

## Dry Run  

**Input:** `num = 2, sum = 3`

| Step | `answer` | Length | Note |
|------|----------|--------|------|
| 1 – feasibility | – | – | `3 ≤ 2·9`, continue |
| 2 – compute | `countNine = 0`, `remaining = 3` | – | No 9’s possible |
| 3 – 9‑loop (0 iterations) | `""` | 0 | No change |
| 4 – append leftover | `"3"` | 1 | `remaining > 0` |
| 5 – zero‑pad (first iteration) | `"30"` | 2 | Length < 2, add `'0'` |
| 6 – zero‑pad (stop) | `"30"` | 2 | Length == num, exit loop |

Final string `"30"` has digit sum 3, uses two digits, and its score \(3²+0² = 9\) is maximal.

## Complexity  
- **Time:** `O(num)` – the two loops together append at most `num` characters (at most `countNine` 9’s plus up to one remainder digit plus the remaining zeros).  
- **Space:** `O(num)` – the `StringBuilder` stores the resulting `num`‑character string; no additional auxiliary structures are used.

## Solution (Java)

```java
class Solution {
    public String maxSumOfSquares(int num, int sum) {
        if(sum > num * 9) return "";
        int countNine = sum / 9; // finding the num of nines that can be present in the orignal num
        int remaining = sum % 9; // remaining digit that can be included to sum up the num like if sum is 10 then we can have '9' and '1' which will be the max  
        StringBuilder answer = new StringBuilder();
        // append all the nines that can be done 
        for(int nines = 1; nines <= countNine; nines++){
            answer.append("9");
        }
        // append the remaining sum after removing '9'
        if(remaining > 0) answer.append(remaining);
        // filling zeros to make the length equal to the num of digits
        while(answer.length() < num){
            answer.append("0");
        }
        return answer.toString();
    }
}
```

---

**Runtime** 97 ms (beats 54.9%) · **Memory** 47.9 MB (beats 72.0%)

<sub>Synced by AILeetHub on 2026-10-14.</sub>
