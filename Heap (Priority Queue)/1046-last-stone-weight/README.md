# 1046. Last Stone Weight

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/last-stone-weight/)

`Array` · `Heap (Priority Queue)`

## Intuition  
The game’s state is completely determined by the multiset of stone weights; each turn we must remove the two largest elements and possibly insert their difference. The naive way would be to sort the array after every smash, costing O(n²). The key insight is that a max‑heap gives us constant‑time access to the current heaviest stone and logarithmic‑time updates, so we can repeatedly perform the smash in O(log n) each without re‑sorting. This reduces the whole process to a single pass over the heap. The pattern used is a **max‑heap (priority queue)**.

## Approach  
1. **Build the heap** – Iterate over `stones` and call `pq.offer(stone)` for each element. After this step the heap contains all weights and satisfies the max‑heap invariant (the largest weight is at the head).  
2. **Repeated smashing** – While `pq.size() > 1` (the loop exits when zero or one stone remains):  
   - `int maxheavy = pq.poll();` removes the current largest weight.  
   - `int notmaxheavy = pq.poll();` removes the second largest.  
   - The loop invariant: before each iteration the heap holds exactly the stones that are still alive, and the two `poll` calls retrieve the two heaviest among them.  
   - If `maxheavy != notmaxheavy`, the difference `maxheavy - notmaxheavy` is a new stone weight, inserted with `pq.offer(maxheavy - notmaxheavy)`. This maintains the heap invariant because `offer` percolates the new element to its proper position.  
   - When the two weights are equal, nothing is re‑inserted, correctly modeling their mutual destruction.  
3. **Return the result** – After the loop, either the heap is empty (`pq.isEmpty()`), in which case we return 0, or it contains a single element, returned by `pq.poll()`. This final check handles the edge case of an initial single‑stone array without entering the loop.

## Dry Run  
Input: `stones = [2, 7, 4, 1, 8, 1]`

| Iter | maxheavy | notmaxheavy | Heap after iteration (desc) | Note |
|------|----------|-------------|-----------------------------|------|
| 0 (init) | – | – | [8,7,4,2,1,1] | heap built by offers |
| 1 | 8 | 7 | [4,2,1,1,1] | 8‑7 = 1 re‑inserted |
| 2 | 4 | 2 | [1,1,1,1] | 4‑2 = 2 re‑inserted, then 2 becomes new max |
| 3 | 2 | 1 | [1,1,1] | 2‑1 = 1 re‑inserted |
| 4 | 1 | 1 | [1] | equal → both destroyed |
| 5 | – | – | [1] | loop ends (size = 1) |

The heap finally holds a single stone of weight 1, which is the answer.

## Complexity  
- **Time:** O(n log n) – building the heap costs O(n log n) (each `offer` is O(log n)), and each smash performs two `poll` operations and at most one `offer`, each O(log n); the number of smashes is ≤ n‑1.  
- **Space:** O(n) – the heap stores at most all original stones, and no additional data structures are used beyond the input array.

## Solution (Java)

```java
class Solution {
    public int lastStoneWeight(int[] stones) {
        PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());

        for(int stone : stones){
            pq.offer(stone);
        }

        while(pq.size() > 1){
            int maxheavy = pq.poll();
            int notmaxheavy = pq.poll();

            if(maxheavy != notmaxheavy)
                pq.offer(maxheavy - notmaxheavy);
        }
        return pq.isEmpty() ? 0 : pq.poll();
    }
}
```

---

**Runtime** 1 ms (beats 99.7%) · **Memory** 43.1 MB (beats 15.3%)

<sub>Synced by AILeetHub on 2026-09-08.</sub>
