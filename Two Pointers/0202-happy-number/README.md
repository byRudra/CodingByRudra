# 202. Happy Number

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/happy-number/)

`Hash Table` · `Math` · `Two Pointers` · `Floyd's Cycle Finding Algorithm`

## Intuition  
Each application of the digit‑square transformation maps a number to another deterministic number. Because there are only finitely many possible results (the sum of squares of digits of any 32‑bit integer is bounded by 9²·10 = 810), the sequence must eventually repeat. If the repetition occurs before reaching 1, the process is stuck in a cycle and the original number is unhappy; otherwise it terminates at 1. Detecting a repeat can be done by remembering every value we have already seen. Using a hash set gives O(1) membership checks, eliminating the need for a second full pass, a sorting step, or a recursive stack.

## Approach  
1. **Initialize a hash set** `set` to store all numbers encountered so far.  
2. **Loop while `n` is not 1** – the exit condition is `n == 1`, which means we have proven happiness.  
   - **Invariant:** at the start of each iteration `n` is the current value of the transformation sequence and all previous values are stored in `set`.  
3. **Cycle check:** `if (set.contains(n)) return false;` – if the current value has been seen, a cycle is detected and the number is unhappy.  
4. **Record the value:** `set.add(n);` – this guarantees the invariant holds for the next iteration.  
5. **Advance the sequence:** `n = sumOfSquares(n);` – compute the next number by summing the squares of the decimal digits.  
   - Inside `sumOfSquares`:  
     a. Initialise `sum = 0`.  
     b. While `n > 0` (loop invariant: `sum` holds the sum of squares of the digits processed so far, and `n` holds the remaining unprocessed suffix).  
        - Extract the least‑significant digit `digit = n % 10`.  
        - Add its square `sum += digit * digit`.  
        - Discard the digit `n /= 10`.  
     c. Return `sum`.  
6. **When the outer loop exits** because `n == 1`, return `true`. The set is no longer needed.

## Dry Run  
**Input:** `19`

| Iter | `n` before step | `sumOfSquares(n)` | `set` after add | Note |
|------|----------------|-------------------|----------------|------|
| 1    | 19             | 82                | {19}           | 19 not seen, added |
| 2    | 82             | 68                | {19,82}        | 82 new, added |
| 3    | 68             | 100               | {19,82,68}     | 68 new, added |
| 4    | 100            | 1                 | {19,82,68,100} | 100 new, added |
| 5    | 1              | –                 | –              | Loop condition fails, return true |

After the fourth iteration the transformation yields `1`, so the outer loop terminates and the algorithm returns `true`, confirming that 19 is a happy number.

## Complexity  
- **Time:** O(k) where *k* is the number of distinct values generated before reaching 1 or detecting a cycle; each iteration performs O(1) set operations and O(d) digit processing (d ≤ 10 for 32‑bit ints).  
- **Space:** O(k) for the hash set storing the visited numbers; the auxiliary `sum` variable uses O(1) extra space.

## Solution (Java)

```java
class Solution {
    public boolean isHappy(int n) {
        HashSet<Integer> set = new HashSet<>();

        while (n != 1) {
            if (set.contains(n)) {
                return false;
            }

            set.add(n);
            n = sumOfSquares(n);
        }

        return true;
    }

    public int sumOfSquares(int n) {
        int sum = 0;

        while (n > 0) {
            int digit = n % 10;
            sum += digit * digit;
            n /= 10;
        }

        return sum;
    }
}

```

---

**Runtime** 1 ms (beats 77.7%) · **Memory** 42.6 MB (beats 34.3%)

<sub>Synced by AILeetHub on 2026-09-07.</sub>
