# 2558. Take Gifts From the Richest Pile

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/take-gifts-from-the-richest-pile/)

`Array` · `Heap (Priority Queue)` · `Simulation`

## Intuition  
The only thing that matters after each second is the current largest pile, because the operation always targets that pile. If we can always retrieve the maximum in O(log n) time, we never need to scan the whole array or keep extra bookkeeping structures. A max‑heap gives exactly this capability, turning the naïve O(k·n) simulation into O(k log n). The pattern used here is the classic **two‑pointer‑like** “greedy with a priority queue” approach.

## Approach  
1. **Build a max‑heap** – Insert every element of `gifts` into `pq` with `pq.offer(gift)`. After this step the heap invariant holds: the element returned by `pq.poll()` is the current maximum.  
2. **Iterate `k` times** – The loop condition `while (k-- > 0)` guarantees exactly `k` executions, even when `k` is zero initially. Inside each iteration:  
   - `pq.poll()` removes the largest pile (`max`).  
   - `Math.sqrt(max)` computes its square‑root; casting to `int` truncates toward zero, which is the required floor.  
   - `pq.offer((int) Math.sqrt(max))` inserts the reduced pile back, restoring the heap property.  
   The invariant after each iteration is: the heap contains the sizes of all piles after the performed reductions, and the largest size is again at the top.  
3. **Aggregate the result** – Iterate over the heap with `for (int gift : pq)` and accumulate `sum += gift`. The heap already holds the final pile sizes, so no extra sorting is needed.  
4. **Return** the total `sum` as a `long` to avoid overflow when the sum exceeds 32‑bit range.

Edge handling:  
- If `gifts` has a single element, the heap still works; the loop repeatedly reduces that element.  
- When a pile’s size is `1`, `Math.sqrt(1)` is `1`, so the heap value does not change; the algorithm naturally stops affecting that pile.  
- The use of `Collections.reverseOrder()` ensures a max‑heap without custom comparator code.

## Dry Run  
**Input:** `gifts = [4, 9, 2]`, `k = 2`

| Iteration | `max = pq.poll()` | `new = (int)Math.sqrt(max)` | Heap after `pq.offer(new)` | Change |
|-----------|-------------------|-----------------------------|----------------------------|--------|
| 0 (init)  | –                 | –                           | `[9, 4, 2]` (max‑heap)     | heap built |
| 1         | 9                 | 3                           | `[4, 2, 3]` → reordered to `[4, 3, 2]` | largest reduced |
| 2         | 4                 | 2                           | `[3, 2, 2]` → reordered to `[3, 2, 2]` | next largest reduced |

After the loop the heap contains `[3, 2, 2]`; their sum is `7`, which the algorithm returns.

## Complexity  
- **Time:** `O((n + k) log n)` – building the heap takes `n log n`, each of the `k` iterations performs one `poll` and one `offer` (both `log n`).  
- **Space:** `O(n)` – the priority queue stores exactly `n` integers; the output sum uses O(1) extra space.

## Solution (Java)

```java
class Solution {
    public long pickGifts(int[] gifts, int k) {
        PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());
        for (int gift : gifts)
            pq.offer(gift);

        while (k-- > 0)
            pq.offer((int) Math.sqrt(pq.poll()));

        long sum = 0;

        for (int gift : pq)
            sum += gift;
        return sum;
    }
}
```

---

**Runtime** 5 ms (beats 98.5%) · **Memory** 44.5 MB (beats 20.2%)

<sub>Synced by AILeetHub on 2026-09-09.</sub>
