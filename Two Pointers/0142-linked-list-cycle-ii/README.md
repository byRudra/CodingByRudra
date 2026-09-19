# 142. Linked List Cycle II

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/linked-list-cycle-ii/)

`Hash Table` · `Linked List` · `Two Pointers` · `Floyd's Cycle Finding Algorithm`

## Intuition  
If two pointers start together and one moves twice as fast as the other, after *k* steps the fast pointer has travelled `2k` nodes while the slow pointer has travelled `k`. When a cycle exists, the fast pointer will eventually lap the slow pointer inside the loop, guaranteeing `fast == slow`. At that meeting point the distance from the head to the cycle start equals the distance from the meeting node to the cycle start, so resetting one pointer to the head and moving both one step at a time makes them meet exactly at the entry node. The naïve way would be to store every visited node in a hash set (O(n) extra memory); the two‑pointer invariant eliminates the need for any extra storage.

## Approach  
1. **Initialize** `slow` and `fast` to `head`.  
2. **Advance** both pointers in a `while (fast != null && fast.next != null)` loop:  
   - `fast = fast.next.next` (two steps)  
   - `slow = slow.next` (one step)  
   - **Invariant:** after each iteration `fast` is exactly twice as far from the start as `slow`.  
   - If `fast == slow`, a cycle is detected and we `break`.  
3. **Check termination**: if the loop exited because `fast` reached the list end (`fast == null || fast.next == null`), return `null` – no cycle.  
4. **Reset** `slow` to `head` while keeping `fast` at the meeting node.  
5. **Move together** in `while (slow != fast)`:  
   - `slow = slow.next`  
   - `fast = fast.next`  
   - **Invariant:** both pointers are the same distance from the cycle start; when they meet, that node is the entry point.  
6. **Return** `slow` (or `fast`), which now points to the first node of the cycle.

Edge handling: an empty list (`head == null`) or a single node without a loop makes the first loop fail the `fast != null && fast.next != null` guard, leading directly to the `null` return. The `<=` vs `<` issue does not arise because we explicitly test both `fast` and `fast.next` for null before the double‑step.

## Dry Run  
Input list: `3 → 2 → 0 → -4 → (back to node 2)`  

| Iter | slow.val | fast.val | Action                               | Note                              |
|------|----------|----------|--------------------------------------|-----------------------------------|
| 0    | 3        | 3        | init                                 | start both at head                |
| 1    | 2        | 0        | `slow=slow.next`, `fast=fast.next.next` | fast jumps over 2 → 0            |
| 2    | 0        | 2        | advance again                        | fast lands on node 2, meets slow? no |
| 3    | 2        | 2        | advance again                        | `fast == slow` → break (meeting) |
| 4    | 3 (reset) | 2        | reset `slow=head`                    | start second phase                |
| 5    | 2        | 0        | both move one step                   | still not equal                   |
| 6    | 0        | -4       | both move one step                   | still not equal                   |
| 7    | 2        | 2        | both move one step                   | `slow == fast` → entry found      |

After iteration 7 both pointers reference the node with value `2`, which is the cycle’s entry.  

## Complexity  
- **Time:** O(n) – the first loop runs at most `n` steps (fast moves two nodes per iteration), and the second loop runs at most the length of the non‑cyclic prefix.  
- **Space:** O(1) – only a constant number of pointers (`slow`, `fast`) are used, independent of the list size. (The output node is not counted as extra space.)

## Solution (Java)

```java
/**
 * Definition for singly-linked list.
 * class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) {
 *         val = x;
 *         next = null;
 *     }
 * }
 */
public class Solution {
    public ListNode detectCycle(ListNode head) {
        ListNode slow = head;
        ListNode fast = head;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
            if (fast == slow) {
                break;
            }
        }
        if (fast == null || fast.next == null) {
            return null;
        }

        slow = head;
        while (slow != fast) {
            slow = slow.next;
            fast = fast.next;
        }
        return slow;
    }
}
```

---

**Runtime** 0 ms (beats 100.0%) · **Memory** 46.3 MB (beats 96.4%)

<sub>Synced by AILeetHub on 2026-09-19.</sub>
