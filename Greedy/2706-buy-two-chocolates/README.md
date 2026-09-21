# 2706. Buy Two Chocolates

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/buy-two-chocolates/)

`Array` · `Greedy` · `Sorting`

## Intuition  
The cheapest way to buy two chocolates is to pick the two smallest prices, because any larger price can only increase the total cost. Sorting the array makes those two minima appear at the front, so we can read them in O(1) time. A naïve approach would try every pair (O(n²)) or keep a running minimum while scanning (which still needs a second pass to find the second‑smallest). The sorting‑then‑pick‑first‑two pattern eliminates the extra pass and any auxiliary data structures.

## Approach  
1. **Sort the array** – `Arrays.sort(prices)`.  
   *Invariant*: after each internal iteration of the sort, the prefix `prices[0..i]` is in non‑decreasing order. The sort finishes when the whole array is ordered.  
2. **Compute the sum of the two smallest elements** – `int sum = prices[0] + prices[1];`.  
   *No loop*: the indices are fixed because sorting guarantees the two minima occupy positions 0 and 1.  
3. **Return the appropriate leftover** –  
   ```java
   return (sum > money) ? money : money - sum;
   ```  
   The ternary checks the only condition that can make the purchase impossible: the cheapest pair exceeds the budget. If `sum` is larger than `money`, we return the original `money`; otherwise we subtract `sum`.  
   *Edge handling*: the input size is guaranteed ≥ 2, so `prices[0]` and `prices[1]` always exist. Duplicate prices are fine because the two smallest may be equal. The comparison uses `>` (strictly greater) because a sum equal to `money` leaves a non‑negative leftover of 0, which is allowed.

## Dry Run  
**Input**: `prices = [3, 1, 2]`, `money = 4`

| Step | `prices` (after sort) | `sum = prices[0]+prices[1]` | Decision (`sum > money`?) | `result` |
|------|----------------------|-----------------------------|---------------------------|----------|
| 1    | `[1, 2, 3]`          | –                           | –                         | –        |
| 2    | –                    | `1 + 2 = 3`                 | `3 > 4` → false           | –        |
| 3    | –                    | –                           | –                         | `4 - 3 = 1` |

After sorting, the two smallest prices are 1 and 2, their sum 3 does not exceed the budget 4, so the algorithm returns `1`. This leftover is maximal while still buying two chocolates.

## Complexity  
- **Time:** **O(n log n)** – the dominant cost is `Arrays.sort`, which runs in `n log n` time for `n = prices.length`.  
- **Space:** **O(1)** – the algorithm uses only a few primitive variables; the sort is in‑place (ignoring the negligible recursion stack of the library implementation).

## Solution (Java)

```java
class Solution {
    public int buyChoco(int[] prices, int money) {
        Arrays.sort(prices);
        return (prices[0] + prices[1] > money) ? money : money - (prices[0] + prices[1]);
    }
}
```

---

**Runtime** 3 ms (beats 47.2%) · **Memory** 46.7 MB (beats 9.1%)

<sub>Synced by AILeetHub on 2026-09-21.</sub>
