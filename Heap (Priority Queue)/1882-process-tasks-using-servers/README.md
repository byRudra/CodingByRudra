# 1882. Process Tasks Using Servers

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/process-tasks-using-servers/)

`Array` · `Heap (Priority Queue)`

## Intuition  
The whole process can be driven by a single clock `t` that never moves backwards: at any moment we either have a free server ready for the next queued task, or we must wait until the earliest busy server finishes. This observation eliminates the need for a separate pass to pre‑compute finish times or to sort all events. By keeping two priority queues—one ordered by `(weight, index)` for free servers and another ordered by `(freeTime, weight, index)` for busy servers—we can always retrieve the optimal server in O(log n) and jump `t` forward exactly when the next server becomes available. The pattern is a classic **two‑heap simulation**.

## Approach  
1. **Initialisation**  
   * Fill `free` with every server as `[weight, index]`. The comparator prefers smaller weight, then smaller index.  
   * Create an empty `busy` heap that stores `[freeTime, weight, index]`.  
   * Prepare `ans` array and set the global clock `t = 0`.

2. **Iterate tasks (`j` from 0 to m‑1)**  
   * `t = Math.max(t, j)` – the j‑th task cannot be processed before its arrival second `j`.  
   * **Fast‑forward**: if `free` is empty and the earliest busy server finishes after `t` (`busy.peek()[0] > t`), set `t = busy.peek()[0]`. This moves the clock to the first moment a server becomes free.  
   * **Release servers**: while the top of `busy` has `freeTime ≤ t`, remove it and push `[weight, index]` back into `free`. This invariant guarantees that after the loop all servers with `freeTime ≤ t` are available.  
   * **Assign task**: poll the best free server `s = free.poll()`. Record `ans[j] = s[1]`. Compute the finish time `t + tasks[j]` and push `[finish, s[0], s[1]]` into `busy`.  

3. **Return** `ans` after the loop.

**Key invariants**  
* At the start of each iteration, `t` equals the current time, all tasks with index `< j` are already assigned, and every server in `free` is truly idle at `t`.  
* `busy` always contains only servers whose `freeTime` is > `t`.  

**Edge handling**  
* Empty or single‑server inputs are covered because the initial `free` heap contains all servers; the loop still works when `free` becomes empty.  
* When multiple servers finish simultaneously, the `while` loop releases all of them before the assignment, preserving the required tie‑break on weight then index.  
* The comparison `busy.peek()[0] > t` uses `>` (not `>=`) because if a server finishes exactly at `t` it is already free and will be released by the subsequent `while` loop.

## Dry Run  
Input: `servers = [3,1]`, `tasks = [2,2,1]`

| j | t (before) | free (weight,index) | busy (freeTime,weight,index) | assigned server | t (after) |
|---|------------|----------------------|------------------------------|----------------|-----------|
|0|0|[(1,1),(3,0)]|[]|1|0+2=2|
|1|max(2,1)=2|[(3,0)]|[(2,1,1)]|0|2+2=4|
|2|max(4,2)=4|[]|[(2,1,1),(4,3,0)]|—|busy.peek()[0]=2 ≤4 → release (1,1) → free[(1,1)] → assign 1|4+1=5|

Final `ans = [1,0,1]`. After the third iteration all tasks are assigned, and the recorded servers match the required priority rules.

## Complexity  
- **Time:** O((n + m) log n) – each server enters and leaves each heap at most once, and heap operations dominate with logarithmic cost.  
- **Space:** O(n) – the two priority queues together store at most `n` server descriptors; the output array `ans` is excluded from the extra‑space count.

## Solution (Java)

```java
class Solution {
    public int[] assignTasks(int[] servers, int[] tasks) {
        int n = servers.length, m = tasks.length;

        // free: {weight, index} -> smallest weight first, then smallest index
        PriorityQueue<int[]> free = new PriorityQueue<>(
                (a, b) -> a[0] != b[0] ? Integer.compare(a[0], b[0]) : Integer.compare(a[1], b[1]));
        for (int i = 0; i < n; i++) {
            free.offer(new int[] { servers[i], i });
        }

        // busy: {timeFreeAgain, weight, index}; long because finish times can exceed int range
        PriorityQueue<long[]> busy = new PriorityQueue<>((a, b) -> {
            if (a[0] != b[0])
                return Long.compare(a[0], b[0]);
            if (a[1] != b[1])
                return Long.compare(a[1], b[1]);
            return Long.compare(a[2], b[2]);
        });

        int[] ans = new int[m];
        long t = 0;
        for (int j = 0; j < m; j++) {
            // Task j enters the queue at second j; time never moves backwards.
            t = Math.max(t, j);

            // No free server: jump ahead to when the earliest one finishes.
            if (free.isEmpty() && busy.peek()[0] > t) {
                t = busy.peek()[0];
            }

            // Release every server that has finished by time t.
            while (!busy.isEmpty() && busy.peek()[0] <= t) {
                long[] s = busy.poll();
                free.offer(new int[] { (int) s[1], (int) s[2] });
            }

            int[] s = free.poll();
            ans[j] = s[1];
            busy.offer(new long[] { t + tasks[j], s[0], s[1] });
        }
        return ans;
    }
}
```

---

**Runtime** 254 ms (beats 32.2%) · **Memory** 165.1 MB (beats 88.0%)

<sub>Synced by AILeetHub on 2026-11-07.</sub>
