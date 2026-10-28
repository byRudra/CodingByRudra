# 933. Number of Recent Calls

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/number-of-recent-calls/)

`Design` · `Queue` · `Data Stream`

## Intuition  
The key observation is that the timestamps arriving in strictly increasing order form a sliding window: any request older than `t‑3000` can never become relevant again, so it can be discarded permanently. A naïve solution would store all timestamps and scan the whole list on each `ping`, yielding O(n²) time. By maintaining only the timestamps that lie inside the current window, we eliminate the extra pass and the need for auxiliary structures such as a hash map or binary search. This is a classic **two‑pointer / sliding‑window** pattern where the queue’s front acts as the “left” pointer and the newly added element is the “right” pointer.

## Approach  
1. **Initialize** an empty `ArrayDeque<Integer>` called `queue`.  
2. **Add** the new timestamp `t` with `queue.offer(t)`. This represents moving the right pointer one step forward.  
3. **Shrink** the window: while `queue.peek() < t - 3000`  
   - `queue.poll()` removes the element at the front, advancing the left pointer.  
   - *Invariant*: after each iteration, all elements in `queue` satisfy `t_i ≥ t - 3000`.  
   - *Exit condition*: the front element is either `null` (empty queue) or `≥ t - 3000`.  
4. **Return** `queue.size()`, which equals the number of timestamps that remain inside the inclusive range `[t‑3000, t]`.  

Edge‑case handling:  
- An empty or single‑element stream works because the while‑loop simply skips when `queue.peek()` is already ≥ `t‑3000`.  
- The comparison uses `<` (not `<=`) because timestamps equal to `t‑3000` are still within the required inclusive window and must stay in the queue.  
- The queue never overflows: each `ping` adds exactly one element and removes at most the same number, guaranteeing O(1) amortized updates.

## Dry Run  

**Input:** `ping` calls with timestamps `[1, 100, 3001, 3002]`

| Step | `t` | Queue after `offer` | Condition (`queue.peek() < t‑3000`) | Action taken | Queue size |
|------|-----|----------------------|--------------------------------------|--------------|------------|
| 1    | 1   | [1]                  | 1 < -2999 → false                    | none         | 1 |
| 2    | 100 | [1, 100]             | 1 < -2900 → false                    | none         | 2 |
| 3    | 3001| [1, 100, 3001]       | 1 < 1 → false                        | none         | 3 |
| 4    | 3002| [1, 100, 3001, 3002] | 1 < 2 → true → `poll()` → [100, 3001, 3002] <br> 100 < 2 → false | removed 1 | 3 |

After the fourth call the queue holds `[100, 3001, 3002]`, all of which lie in `[2, 3002]`. Hence the returned counts are `1, 2, 3, 3`, matching the expected output.

## Complexity  
- **Time:** O(1) amortized per `ping`. Each timestamp is enqueued once and dequeued at most once, so the total work over *n* calls is O(n).  
- **Space:** O(k) where *k* ≤ 3000 ms worth of timestamps, i.e., at most the number of calls that can fit in a 3000 ms window (≤ n). The queue stores only the active window, independent of the total number of calls.

## Solution (Java)

```java
class RecentCounter {

    private final Queue<Integer> queue;

    public RecentCounter() {
        queue = new ArrayDeque<>();
    }

    public int ping(int t) {
        queue.offer(t);
        while (queue.peek() < t - 3000) {
            queue.poll();
        }
        return queue.size();
    }
}

/**
 * Your RecentCounter object will be instantiated and called as such:
 * RecentCounter obj = new RecentCounter();
 * int param_1 = obj.ping(t);
 */
```

---

**Runtime** 21 ms (beats 77.4%) · **Memory** 50.6 MB (beats 93.8%)

<sub>Synced by AILeetHub on 2026-10-28.</sub>
