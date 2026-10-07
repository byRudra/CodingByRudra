# 148. Sort List

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/sort-list/)

`Linked List` · `Two Pointers` · `Divide and Conquer` · `Sorting` · `Merge Sort`

## Intuition  
The list can be split exactly in half by moving one pointer (`fast`) two steps for every one step of another pointer (`slow`). After `k` iterations `fast` has traversed `2k` nodes while `slow` has traversed `k`, so `slow` stops at the node just before the midpoint. This observation lets us avoid any extra pass, hash map, or sorting of an auxiliary array. By recursively sorting the two halves and merging them, we obtain a classic **divide‑and‑conquer** solution that runs in `O(n log n)` time with only constant extra pointers.

## Approach  
1. **Base case** – If `head` is `null` or `head.next` is `null`, return `head` (empty or single‑node list is already sorted).  
2. **Find middle** – Call `getMiddle(head)`.  
   - Initialise `slow = head`, `fast = head.next`.  
   - Loop while `fast != null && fast.next != null`:  
     *Invariant*: `slow` points to the node preceding the start of the right half, `fast` is either at the end or one node before the end of the list.  
     - Advance `slow = slow.next`, `fast = fast.next.next`.  
   - When the loop exits, `slow` is the left‑half tail; return it.  
3. **Partition** – `right = mid.next; mid.next = null; left = head;`. This severs the list into two independent sub‑lists.  
4. **Recursive sort** – `left = sortList(left); right = sortList(right);`. Each call repeats steps 1‑3 on a smaller segment, guaranteeing termination because the length strictly decreases.  
5. **Merge** – Invoke `mergeList(left, right)`.  
   - Create a dummy sentinel `dummy = new ListNode(0)` and a tail pointer `tail = dummy`.  
   - While both `left` and `right` are non‑null: compare `left.val` and `right.val`. Append the smaller node to `tail.next`, advance the chosen list, and move `tail` forward.  
   - After the loop, attach the remaining non‑empty list (`tail.next = left` or `right`).  
   - Return `dummy.next`, which is the head of the merged, sorted list.  

Key edge handling: the initial `fast = head.next` makes the split biased toward the left side for even‑length lists, ensuring `mid` is the last node of the left half and preventing an infinite recursion on a two‑node list. All pointer updates are safe because the list is singly linked; no index arithmetic is needed.

## Dry Run  
Input: `4 → 2 → 1 → 3 → null`

| Step | slow | fast | left sublist (after split) | right sublist (after split) | note |
|------|------|------|----------------------------|-----------------------------|------|
| Init | 4 | 2 | – | – | `slow=head`, `fast=head.next` |
| 1st loop | 2 | 3 | – | – | `slow=slow.next`, `fast=fast.next.next` (fast becomes null) |
| Exit | 2 | null | 4 → 2 → null | 1 → 3 → null | `mid` returned as node 2 |
| Recursion on left (4→2) → split at 4 | 4 | null | 4 → null | 2 → null | base cases return nodes |
| Merge left & right of left half | – | – | 4 | 2 | `mergeList` produces 2 → 4 |
| Recursion on right (1→3) → split at 1 | 1 | null | 1 → null | 3 → null | base cases return nodes |
| Merge left & right of right half | – | – | 1 | 3 | `mergeList` produces 1 → 3 |
| Final merge of (2→4) and (1→3) | – | – | 2→4 | 1→3 | `mergeList` yields 1 → 2 → 3 → 4 |

The algorithm finishes with `1 → 2 → 3 → 4`, the correctly sorted list.

## Complexity  
- **Time:** `O(n log n)` – each level of recursion processes all `n` nodes in the merge step, and the depth of recursion is `log n` because the list is halved each time.  
- **Space:** `O(1)` auxiliary space – only a constant number of pointers (`slow`, `fast`, `mid`, `dummy`, `tail`, etc.) are used; the recursion stack uses `O(log n)` frames, which is excluded from the stated constant‑space requirement.

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
    public ListNode sortList(ListNode head) {
        if (head == null || head.next == null)
            return head;
        // partition
        ListNode mid = getMiddle(head);
        ListNode right = mid.next;
        mid.next = null;
        ListNode left = head;
        left = sortList(left);
        right = sortList(right);
        return mergeList(left, right);
    }

    private ListNode mergeList(ListNode left, ListNode right) {
        ListNode dummy = new ListNode(0);
        ListNode tail = dummy;
        while (left != null && right != null) {
            if (left.val < right.val) {
                tail.next = left;
                left = left.next;
            } else {
                tail.next = right;
                right = right.next;
            }
            tail = tail.next;
        }

        if (left != null)
            tail.next = left;
        if (right != null)
            tail.next = right;
        return dummy.next;
    }

    private ListNode getMiddle(ListNode head) {
        ListNode slow = head;
        ListNode fast = head.next;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;

    }
}
```

---

**Runtime** 10 ms (beats 48.7%) · **Memory** 59.2 MB (beats 75.4%)

<sub>Synced by AILeetHub on 2026-05-03.</sub>
