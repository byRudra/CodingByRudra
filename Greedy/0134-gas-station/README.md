# 134. Gas Station

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/gas-station/)

`Array` · `Greedy`

## Intuition  
If we walk the circle once while keeping a running balance `tank = Σ(gas[i]‑cost[i])` from a candidate start, the moment this balance becomes negative we know that **no station between the original start and the current index can be a valid start**—the deficit cannot be compensated later. By discarding that whole segment and restarting just after it we eliminate the need for a second pass or extra storage. The whole process is a classic greedy “single‑pass elimination” using two pointers (start and current).

## Approach  
1. **Initialize** `total = 0`, `start = 0`, `tank = 0`.  
2. **Iterate** `i` from `0` to `gas.length‑1`.  
   - Compute `diff = gas[i] - cost[i]`.  
   - Update `tank += diff` and `total += diff`.  
   - **Invariant**: `tank` holds the net gas after traveling from `start` up to `i`. `total` accumulates the global net gas for the entire circuit.  
   - **Check**: if `tank < 0` the current segment `[start … i]` cannot be completed. Set `start = i + 1` (the next station) and reset `tank = 0`. This discards the impossible prefix while preserving `total`.  
3. **After the loop**, `total` tells whether the whole circle has enough gas (`total >= 0`).  
4. **Return** `start` if `total >= 0`; otherwise return `-1`.  
5. **Edge handling**:  
   - Empty or single‑element arrays are covered because the loop runs zero or one iteration and the final check uses `total`.  
   - When `tank` becomes exactly `0`, we keep the current `start` because the segment is still viable; the condition is `< 0`, not `<= 0`.  
   - The update `start = i + 1` works for both even and odd lengths because we always move past the failing index, guaranteeing progress and preventing infinite loops.

## Dry Run  
**Input**: `gas = [1,2,3,4]`, `cost = [2,2,2,2]`

| i | diff (gas‑cost) | tank before | tank after | start before | start after | note |
|---|-----------------|-------------|------------|--------------|-------------|------|
| 0 | -1 | 0 | -1 | 0 | 1 | tank < 0 → discard 0, reset |
| 1 | 0  | 0 | 0  | 1 | 1 | tank stays non‑negative |
| 2 | 1  | 0 | 1  | 1 | 1 | accumulate |
| 3 | 2  | 1 | 3  | 1 | 1 | accumulate |

After the loop `total = (-1)+0+1+2 = 2 ≥ 0`, so the algorithm returns `start = 1`. Starting at station 1 indeed allows a full circuit.

## Complexity  
- **Time:** `O(n)` – the single `for` loop visits each station once; `tank` advances at most `n` steps because `start` only moves forward.  
- **Space:** `O(1)` – only a few integer variables are used, independent of input size (output index excluded).

## Solution (Java)

```java
class Solution {
    public int canCompleteCircuit(int[] gas, int[] cost) {
        int total = 0, start = 0, tank = 0;

        for(int i = 0; i < gas.length; i++){
            int diff = gas[i] - cost[i];

            tank += diff;
            total += diff;

            if(tank < 0){
                start = i + 1;
                tank = 0;
            }
        }
        return total >= 0 ? start : -1;
    }
}
```

---

**Runtime** 3 ms (beats 31.5%) · **Memory** 119.3 MB (beats 64.4%)

<sub>Synced by AILeetHub on 2025-11-15.</sub>
