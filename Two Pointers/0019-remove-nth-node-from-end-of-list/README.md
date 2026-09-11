# 19. Remove Nth Node From End of List

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/remove-nth-node-from-end-of-list/)

`Linked List` · `Two Pointers`

## Intuition  
If we advance one pointer `n` steps ahead of another, the gap between them stays exactly `n` nodes. Consequently, when the leading pointer reaches the end of the list, the trailing pointer sits right before the node that is `n`‑th from the tail. This single observation eliminates the need for a second traversal, a hash map, or recursion. The solution therefore follows the classic **two‑pointer** (or “fast‑slow”) pattern.

## Approach  
1. **Create a sentinel.**  
   `dummy = new ListNode(0, head)` ensures that removal of the head itself is handled uniformly. Both `fast` and `slow` start at `dummy`.  
2. **Advance `fast` `n` steps.**  
   ```java
   for (int i = 0; i < n; i++) fast = fast.next;
   ```  
   *Exit condition:* loop ends after exactly `n` iterations.  
   *Invariant:* after `i` iterations, `fast` is `i` nodes ahead of `slow`.  
   This step also guarantees `fast` is never `null` because `n ≤ sz`.  
3. **Slide both pointers together until `fast.next` is `null`.**  
   ```java
   while (fast.next != null) {
       slow = slow.next;
       fast = fast.next;
   }
   ```  
   *Exit condition:* `fast` points to the last real node, so `fast.next == null`.  
   *Invariant:* the distance between `fast` and `slow` remains `n` nodes throughout the loop.  
   When the loop stops, `slow` is positioned immediately before the target node, even for lists of length 1 or when the target is the first real node.  
4. **Remove the target.**  
   `slow.next = slow.next.next;` bypasses the `n`‑th node from the end. Because `slow` is never `null` (it starts at `dummy`), this assignment is safe for all edge cases.  
5. **Return the new head.**  
   `return dummy.next;` yields the original head unless it was removed, in which case `dummy.next` now points to the second node or `null`.

## Dry Run  

**Input:** `head = [1,2,3,4,5]`, `n = 2`

| Step | fast.val | slow.val | Note |
|------|----------|----------|------|
| Init | 0 (dummy) | 0 (dummy) | both at dummy |
| Advance 1 | 1 | 0 | `fast = fast.next` |
| Advance 2 | 2 | 0 | `fast` now `n` ahead |
| Loop 1 | 3 | 1 | both move one step |
| Loop 2 | 4 | 2 | both move one step |
| Loop 3 | 5 | 3 | `fast.next` becomes `null`, stop |
| Delete | – | – | `slow.next = slow.next.next` removes node 4 |
| Return | – | – | `dummy.next` points to `[1,2,3,5]` |

After the loop, `slow` sits before the node with value 4, so re‑linking skips it and yields the correct list.

## Complexity  
- **Time:** `O(L)` – the first `for` loop runs `n` times and the subsequent `while` loop traverses the remaining `L‑n` nodes, together visiting each node at most once.  
- **Space:** `O(1)` – only a few pointer variables (`dummy`, `fast`, `slow`) are used; the output list reuses the original nodes.

## Solution (Java)

```java
/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode() {}
 *     ListNode(int val) { this.val = val; }
 *     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 * }
 */
class Solution {
    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode dummy = new ListNode(0, head);
        ListNode fast = dummy;
        ListNode slow = dummy;

        for(int i = 0; i < n; i++){
            fast = fast.next;
        }
        while(fast.next != null){
            slow = slow.next;
            fast = fast.next;
        }
        slow.next = slow.next.next;
        return dummy.next;
    }
}
```

---

**Runtime** 0 ms (beats 100.0%) · **Memory** 43.5 MB (beats 58.2%)

<sub>Synced by AILeetHub on 2026-01-06.</sub>
