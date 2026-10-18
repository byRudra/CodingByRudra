# 25. Reverse Nodes in k-Group

![Hard](https://img.shields.io/badge/Difficulty-Hard-ff375f?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/reverse-nodes-in-k-group/)

`Linked List` · `Recursion`

## Intuition  
The list can be processed block‑by‑block: if we know the node that starts a block (`groupPrev.next`) and the node that ends it (`kth`), we can reverse the block in‑place by walking forward and rewiring each `next` to point to the previously visited node. The naive way would be to copy each block into an auxiliary array or to perform a separate pass for every group, both of which cost extra memory or extra traversals. The key observation is that while we locate the `k`‑th node with a simple forward walk, we can simultaneously reverse the segment by using a “prev = groupNext” anchor, so the reversal finishes exactly where the next block begins. This yields a classic **two‑pointer / in‑place reversal** pattern.

## Approach  
1. **Create a dummy head** (`dummy`) and attach it before the real head; set `groupPrev = dummy`. The dummy guarantees a stable predecessor for the first group.  
2. **Locate the k‑th node** of the current group: `kth = getKth(groupPrev, k)`. `getKth` walks `k` steps from `groupPrev`; if it returns `null` the remaining nodes are fewer than `k` and we stop.  
3. **Remember the node after the group**: `groupNext = kth.next`. This node will become the “prev” anchor for the reversal, ensuring the reversed segment points to the rest of the list.  
4. **Reverse the group**:  
   - Initialise `prev = groupNext` and `curr = groupPrev.next` (the first node of the group).  
   - While `curr != groupNext` repeat:  
     - `next = curr.next` (store forward link)  
     - `curr.next = prev` (rewire backward)  
     - `prev = curr` (move prev forward)  
     - `curr = next` (advance to original next)  
   The loop invariant is that all nodes between the original start and `curr` have been reversed and now point to `prev`.  
5. **Reconnect the reversed block**:  
   - `oldFirst = groupPrev.next` (the original head of the block, now the tail)  
   - `groupPrev.next = kth` (link predecessor to the new head)  
   - `groupPrev = oldFirst` (move `groupPrev` to the tail for the next iteration).  
6. **Repeat** from step 2 until `getKth` fails. Finally return `dummy.next`.

## Dry Run  
Input: `1 → 2 → 3 → 4 → 5`, `k = 2`

| Iter | groupPrev | kth | groupNext | prev | curr | Note |
|------|-----------|-----|-----------|------|------|------|
| 1 | dummy (0) | 2 | 3 | 3 | 1 | `kth` found, start reversal |
| 1 | – | – | – | 1 | 2 | `1.next = 3` |
| 1 | – | – | – | 2 | 3 | `2.next = 1` (group reversed) |
| 1 | – | – | – | – | – | Reconnect: `dummy.next = 2`, `groupPrev = 1` |
| 2 | 1 | 4 | 5 | 5 | 3 | Second group located |
| 2 | – | – | – | 3 | 4 | `3.next = 5` |
| 2 | – | – | – | 4 | 5 | `4.next = 3` (group reversed) |
| 2 | – | – | – | – | – | Reconnect: `1.next = 4`, `groupPrev = 3` |

`getKth` from `groupPrev` now returns `null` (only node 5 left), loop ends. The list is `2 → 1 → 4 → 3 → 5`, which is the required output.

## Complexity  
- **Time:** `O(n)` – each node is visited a constant number of times; the outer loop advances `groupPrev` by `k` nodes and the inner reversal loop also processes exactly those `k` nodes.  
- **Space:** `O(1)` – only a handful of pointers (`dummy`, `groupPrev`, `kth`, `prev`, `curr`, `next`) are used regardless of list length; the output list reuses the original nodes.

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
    public ListNode reverseKGroup(ListNode head, int k) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode groupPrev = dummy;

        while (true) {
            // Find the k-th node from groupPrev; if it doesn't exist, we're done
            ListNode kth = getKth(groupPrev, k);
            if (kth == null) break;

            ListNode groupNext = kth.next;

            // Reverse the group, stopping at groupNext
            ListNode prev = groupNext;
            ListNode curr = groupPrev.next;
            while (curr != groupNext) {
                ListNode next = curr.next;
                curr.next = prev;
                prev = curr;
                curr = next;
            }

            // Reconnect: old first node is now the group's tail
            ListNode oldFirst = groupPrev.next;
            groupPrev.next = kth;
            groupPrev = oldFirst;
        }
        return dummy.next;
    }

    // Moves k steps forward from node, or returns null if the list is too short
    private ListNode getKth(ListNode node, int k) {
        while (node != null && k > 0) {
            node = node.next;
            k--;
        }
        return node;
    }
}
```

---

**Runtime** 0 ms (beats 100.0%) · **Memory** 46 MB (beats 95.1%)

<sub>Synced by AILeetHub on 2026-10-18.</sub>
