# 1011. Capacity To Ship Packages Within D Days

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/capacity-to-ship-packages-within-d-days/)

`Array` · `Binary Search`

## Intuition  
The key observation is that the number of days needed to ship all packages is a monotonic decreasing function of the ship’s capacity: if a capacity C works, any larger capacity also works, and any smaller capacity cannot work. Therefore we can binary‑search the smallest capacity that yields at most `days` shipments. A naïve solution would try every integer between the heaviest package and the total weight, costing O(n·range) or would use DP with O(n·sum) memory. The monotonic insight eliminates the extra pass and the heavy DP table, leaving only a logarithmic search over the feasible range. This pattern is the classic **binary search on answer**.

## Approach  
1. **Compute search bounds** –  
   *`minCapacity`* is the maximum single weight (no ship can be smaller than the heaviest package).  
   *`maxCapacity`* is the sum of all weights (a ship that can carry everything in one day).  
   The `for` loop updates these two values: `minCapacity = Math.max(weight, minCapacity); maxCapacity += weight;`.

2. **Binary search loop** –  
   While `minCapacity < maxCapacity` we compute the midpoint `mid = minCapacity + (maxCapacity - minCapacity) / 2`.  
   The invariant: every capacity `< minCapacity` is infeasible, every capacity `> maxCapacity` is feasible.

3. **Simulate shipping with capacity `mid`** –  
   Initialise `sum = 0` (current day load) and `day = 1`.  
   Iterate over `weights`:  
   - If `sum + weight > mid` we have exceeded the day's limit, so we start a new day (`day++`) and reset `sum = 0`.  
   - Then `sum += weight` adds the current package to the (new or existing) day.  
   This loop respects the order of packages and never over‑loads the ship.

4. **Adjust bounds** –  
   After the simulation, if `day > days` the midpoint is too small, so we raise the lower bound: `minCapacity = mid + 1`.  
   Otherwise the midpoint is sufficient (maybe even larger than needed), so we tighten the upper bound: `maxCapacity = mid`.

5. **Return result** –  
   When the loop exits, `minCapacity == maxCapacity` and represents the minimal feasible capacity.

Edge cases handled explicitly:  
- A single‑element array yields `minCapacity == maxCapacity` immediately.  
- The `<=` check is avoided; we use `>` to decide when to start a new day, which correctly handles the case where `sum + weight == mid` (the package fits exactly).  
- No overflow occurs because `mid` is computed with the difference formula.

## Dry Run  

**Input:** `weights = [1,2,3,4,5,6,7,8,9,10]`, `days = 5`

| iter | mid | sum (after processing current weight) | day | note |
|------|-----|---------------------------------------|-----|------|
| 1 | 55 | 55 (all weights added) | 1 | `mid` = (55+55)/2, whole array fits in one day → `day ≤ days`, set `maxCapacity = 55` |
| 2 | 30 | after 1‑5 → sum=15, next weight 6 triggers new day → day=2, sum=6; continue … final day=4 | 4 | `day ≤ days`, tighten `maxCapacity = 30` |
| 3 | 18 | day splits: [1‑5]=15, [6‑7]=13, [8]=8, [9‑10] exceeds → new day → day=5, sum=10 | 5 | `day == days`, tighten `maxCapacity = 18` |
| 4 | 12 | splits into 7 days (>5) → `day > days`, raise `minCapacity = 13` |
| 5 | 15 | splits into exactly 5 days → `day ≤ days`, set `maxCapacity = 15` |
| 6 | 14 | splits into 6 days (>5) → `minCapacity = 15` |
| loop ends | | | | `minCapacity == maxCapacity == 15`, answer is 15 |

The final state `minCapacity = 15` is the smallest capacity that ships all packages within 5 days.

## Complexity  
- **Time:** O(n · log R) where *n* is `weights.length` and *R* = `maxCapacity - minCapacity` (the weight range). The outer binary‑search runs `log R` iterations, each inner simulation scans the array once.  
- **Space:** O(1) extra space, only a few integer variables are used regardless of input size. (The output integer does not count toward the space bound.)

## Solution (Java)

```java
class Solution {
    public int shipWithinDays(int[] weights, int days) {
        int minCapacity = 0, maxCapacity = 0;
        for(int weight : weights){
            minCapacity = Math.max(weight, minCapacity);
            maxCapacity += weight;
        }

        while(minCapacity < maxCapacity){
            int mid = minCapacity + (maxCapacity - minCapacity) / 2;

            int sum = 0, day = 1;
            for(int weight : weights){
                if(sum + weight > mid){
                    day++;
                    sum = 0;
                }
                sum += weight;
            }

            if(day > days){
                minCapacity = mid + 1;
            }
            else{
                maxCapacity = mid;
            }
        }
        return minCapacity;
    }
}
```

---

**Runtime** 9 ms (beats 98.7%) · **Memory** 50.1 MB (beats 76.1%)

<sub>Synced by AILeetHub on 2026-09-06.</sub>
