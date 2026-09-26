# 328. Odd Even Linked List

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/odd-even-linked-list/)

`Linked List`

## Intuition  
The list can be split into two interleaved subsequences: nodes at odd positions and nodes at even positions. After each full step the “odd” pointer has moved one odd node forward while the “even” pointer has moved one even node forward, so the two pointers together advance two positions per iteration. This means we can build the odd and even subsequences in a single pass without extra storage, eliminating the need for a second traversal, a hash map, or recursion.

## Approach  
1. **Handle trivial cases.**  
   - If `head` is `null` or `head.next` is `null` the list has 0 or 1 node, so it is already correctly ordered; return `head`.  

2. **Initialize pointers.**  
   - `odd = head` points to the first (odd‑indexed) node.  
   - `even = head.next` points to the second (even‑indexed) node.  
   - `evenHead = even` remembers the start of the even subsequence so it can be attached later.  

3. **Iterate while both `even` and `even.next` exist.**  
   - **Loop condition:** `while (even != null && even.next != null)`.  
   - **Invariant:** before each iteration, `odd` is the last node of the odd subsequence, `even` is the last node of the even subsequence, and the original relative order inside each subsequence is preserved.  

   a. **Link the next odd node.**  
      - `odd.next = even.next;` skips the current even node and attaches the next odd node.  
      - `odd = odd.next;` advances `odd` to this newly attached node.  

   b. **Link the next even node.**  
      - `even.next = odd.next;` now points `even` to the node that follows the new odd node (which is the next even node, if any).  
      - `even = even.next;` advances `even` similarly.  

   This two‑step update maintains the alternating pattern while preserving original ordering within each group. The loop stops when there is no further even node or no node after the current even node, which correctly handles both even‑length and odd‑length lists.  

4. **Concatenate the two subsequences.**  
   - After the loop, `odd` points to the last odd‑indexed node. Setting `odd.next = evenHead;` attaches the even subsequence (starting at `evenHead`) directly after the odd subsequence.  

5. **Return the reordered list.**  
   - The head of the list has not changed, so `return head;` yields the required ordering.

## Dry Run  
**Input:** `1 → 2 → 3 → 4 → 5 → null`

| Iter | odd.val | even.val | odd.next.val | even.next.val | Change |
|------|---------|----------|--------------|---------------|--------|
| 0 (init) | 1 | 2 | 2 | 3 | set `odd=head`, `even=head.next`, `evenHead=2` |
| 1 | 1 | 2 | 3 | 4 | `odd.next=3`, `odd=3`; `even.next=4`, `even=4` |
| 2 | 3 | 4 | 5 | null | `odd.next=5`, `odd=5`; `even.next=null`, `even=null` (loop exits) |
| post‑loop | 5 | null | null | — | `odd.next = evenHead (2)` attaches even list |

Final list: `1 → 3 → 5 → 2 → 4 → null`. The odd‑indexed nodes appear first, preserving their original order, followed by the even‑indexed nodes in their original order.

## Complexity  
- **Time:** `O(n)` – the while‑loop processes each node at most once; `odd` and `even` together advance two positions per iteration.  
- **Space:** `O(1)` – only a constant number of pointers (`odd`, `even`, `evenHead`) are used; the output list reuses the original nodes.

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
    public ListNode oddEvenList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }

        ListNode odd = head;
        ListNode even = head.next;
        ListNode evenHead = even;

        while (even != null && even.next != null) {
            // Connect odd nodes
            odd.next = even.next;
            odd = odd.next;

            // Connect even nodes
            even.next = odd.next;
            even = even.next;
        }

        // Join odd list with even list
        odd.next = evenHead;

        return head;
    }
}
```

---

**Runtime** 0 ms (beats 100.0%) · **Memory** 46.4 MB (beats 35.7%)

<sub>Synced by AILeetHub on 2026-09-26.</sub>
