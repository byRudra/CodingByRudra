# 2487. Remove Nodes From Linked List

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/remove-nodes-from-linked-list/)

`Linked List` · `Stack` · `Recursion` · `Monotonic Stack`

## Intuition  
If we look at a node from right to left, we only need to know the greatest value seen so far: any node smaller than that maximum must disappear, because a larger value exists to its right. Scanning the list in its natural left‑to‑right order would require a data structure that can query “maximum to the right” for each position, which costs extra passes or a stack. By reversing the list first, the “right side” becomes the “left side”, and a single forward pass can maintain the running maximum and drop smaller nodes on the fly. The whole trick is therefore **reverse → greedy keep‑max → reverse back**, a classic two‑pointer/monotonic‑stack pattern realized with O(1) extra memory.

## Approach  
1. **Reverse the input list** (`reverse(head)`).  
   *Loop invariant*: `prev` points to the already‑reversed prefix, `head` points to the yet‑to‑be‑processed suffix. The loop stops when `head` becomes `null`, leaving `prev` as the new head.  
2. **Initialize** `curr = head` (the new head after reversal) and `max = curr.val`.  
3. **Traverse while `curr.next != null`**:  
   - *Invariant*: All nodes up to `curr` are kept, and `max` equals the maximum value among them.  
   - If `curr.next.val < max`, bypass the smaller node with `curr.next = curr.next.next`. This deletes it without moving `curr`.  
   - Otherwise (`curr.next.val >= max`), advance `curr = curr.next` and update `max = curr.val`.  
   The loop ends when there is no further node to examine, guaranteeing that every remaining node is ≥ every node to its left in the reversed order.  
4. **Reverse the filtered list again** (`reverse(head)`) to restore original order, now without the removed nodes.  
5. **Return** the head of this final list.

Edge handling: the constraints guarantee at least one node, so `curr` is never `null` after step 2. For a single‑node list the second while‑loop never executes, and the double reversal returns the original node unchanged. The code uses `<=` only when comparing `curr.next.val` with `max`; the strict `<` ensures that equal‑valued nodes are retained, matching the problem’s “greater value” condition.

## Dry Run  
Input: `5 → 2 → 13 → 3 → 8`

| Step | Action | `curr.val` | `max` | List after action |
|------|--------|------------|------|-------------------|
| 0 | Reverse whole list | – | – | `8 → 3 → 13 → 2 → 5` |
| 1 | Init `curr=head(8)`, `max=8` | 8 | 8 | unchanged |
| 2 | `curr.next.val=3 < max` → delete | 8 | 8 | `8 → 13 → 2 → 5` |
| 3 | `curr.next.val=13 ≥ max` → move | 13 | 13 | unchanged |
| 4 | `curr.next.val=2 < max` → delete | 13 | 13 | `8 → 13 → 5` |
| 5 | `curr.next.val=5 < max` → delete | 13 | 13 | `8 → 13` |
| 6 | `curr.next == null` → stop | – | – | – |
| 7 | Reverse filtered list | – | – | `13 → 8` |

Final list `13 → 8` matches the expected output because every removed node had a larger value to its right in the original order.

## Complexity  
- **Time:** O(n) – each of the two reversals traverses the list once (`n` steps) and the middle filtering loop visits each node at most once.  
- **Space:** O(1) – only a few pointer variables (`prev`, `curr`, `next`, `max`) are used; the output list reuses the original nodes.

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
    public ListNode removeNodes(ListNode head) {
        head = reverse(head);
        ListNode curr = head;
        int max = curr.val;
        while (curr.next != null) {
            if (curr.next.val < max) {
                curr.next = curr.next.next;
            } else {
                curr = curr.next;
                max = curr.val;
            }
        }
        return reverse(head);
    }

    private ListNode reverse(ListNode head) {
        ListNode prev = null;
        while (head != null) {
            ListNode next = head.next;
            head.next = prev;
            prev = head;
            head = next;
        }
        return prev;
    }
}
```

---

**Runtime** 6 ms (beats 92.6%) · **Memory** 130.1 MB (beats 93.9%)

<sub>Synced by AILeetHub on 2026-09-16.</sub>
