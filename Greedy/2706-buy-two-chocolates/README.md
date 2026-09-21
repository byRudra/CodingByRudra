# 2706. Buy Two Chocolates

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/buy-two-chocolates/)

`Array` · `Greedy` · `Sorting`

## Intuition  
The only thing that matters for the leftover money is the cheapest possible pair of chocolates, because any more expensive pair can only reduce the leftover further. Therefore we just need the two smallest prices in the array. A naïve solution would sort the whole array ( O(n log n) ) or keep a hash map of frequencies, both of which are unnecessary work. By scanning once and maintaining the smallest and second‑smallest values we obtain the required pair in linear time using the two‑pointer‑like “two minima” pattern.

## Approach  
1. **Initialise** `firstMin` and `secondMin` to `Integer.MAX_VALUE`.  
2. **Iterate** over each `price` in `prices`.  
   - **Condition** `price < firstMin`:  
     - Update `secondMin = firstMin` (the previous smallest becomes second smallest).  
     - Set `firstMin = price`.  
   - **Else if** `price < secondMin`:  
     - Update `secondMin = price`.  
   - *Invariant*: after processing any prefix of the array, `firstMin` holds the smallest value seen so far and `secondMin` holds the smallest value larger than or equal to `firstMin` among the processed elements.  
3. **Compute** `cost = firstMin + secondMin`.  
4. **Return** `money - cost` if `cost` does not exceed `money`; otherwise return `money`.  
   - The ternary `cost > money ? money : money - cost` implements this decision in a single expression.

**Edge handling** – The loop works for any length ≥ 2 because the first two elements will populate `firstMin` and `secondMin`. Duplicate prices are correctly handled: when a price equals `firstMin` it fails the first test but may update `secondMin` if it is smaller than the current `secondMin`. The use of `>` (strictly greater) when comparing `cost` to `money` ensures we only subtract when the purchase is affordable; otherwise we keep the original amount.

## Dry Run  
Input: `prices = [5, 1, 4, 2]`, `money = 7`

| Iteration | price | firstMin | secondMin | note                              |
|-----------|-------|----------|-----------|-----------------------------------|
| 1         | 5     | 5        | MAX_VALUE | 5 becomes smallest                |
| 2         | 1     | 1        | 5         | 1 < firstMin → shift 5 to second   |
| 3         | 4     | 1        | 4         | 4 < secondMin → update secondMin   |
| 4         | 2     | 1        | 2         | 2 < secondMin → update secondMin   |

After the loop `firstMin = 1`, `secondMin = 2`, so `cost = 3`. Since `3 ≤ money`, the function returns `7 - 3 = 4`. The leftover 4 is maximal because any other pair costs at least 5.

## Complexity  
- **Time:** O(n) – the single `for‑each` loop visits each price once, and the final arithmetic is constant time.  
- **Space:** O(1) – only a few primitive variables (`firstMin`, `secondMin`, `cost`) are stored, independent of the input size. (The output integer does not affect the asymptotic bound.)

## Solution (Java)

```java
// class Solution {
//     public int buyChoco(int[] prices, int money) {
//         Arrays.sort(prices);
//         return (prices[0] + prices[1] > money) ? money : money - (prices[0] + prices[1]);
//     }
// }

class Solution {
    public int buyChoco(int[] prices, int money) {
        int firstMin = Integer.MAX_VALUE;
        int secondMin = Integer.MAX_VALUE;
        for (int price : prices) {
            if (price < firstMin) {
                secondMin = firstMin;
                firstMin = price;
            } else if (price < secondMin) {
                secondMin = price;
            }
        }

        int cost = firstMin + secondMin;

        return cost > money ? money : money - cost;
    }
}
```

---

**Runtime** 1 ms (beats 100.0%) · **Memory** 46.3 MB (beats 60.1%)

<sub>Synced by AILeetHub on 2026-09-21.</sub>
