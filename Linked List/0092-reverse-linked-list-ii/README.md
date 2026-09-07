# 92. Reverse Linked List II

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/reverse-linked-list-ii/)

`Linked List`

## Intuition  
The key observation is that while scanning the list once we can keep the part before `left` fixed, then repeatedly pull the node **right after** the current head of the reversal segment and splice it to the front of that segment. After `k` iterations the segment between `prev.next` and `curr` is reversed for exactly `k` nodes, so when `k = right‑left` the whole interval `[left, right]` is reversed. A naïve solution would first locate the sub‑list, detach it, reverse it with a separate pass or a stack, and then reconnect – costing an extra traversal or extra memory. By using the “head‑insertion” trick we avoid any additional passes or containers, achieving the reversal in a single linear scan. This follows the classic **two‑pointer / in‑place head insertion** pattern.

## Approach  
1. **Create a dummy sentinel** (`dummy`) whose `next` points to `head`. This guarantees that `prev` always has a predecessor, even when `left == 1`.  
2. **Advance `prev`** from `dummy` to the node immediately before position `left`.  
   - Loop condition: `i < left`.  
   - Invariant: after each iteration, `prev` points to the `(i‑1)`‑th node, so when the loop ends `prev` is the `(left‑1)`‑th node.  
3. **Initialize `curr`** as `prev.next`, the first node of the segment to be reversed.  
4. **Iterate `right‑left` times**, each time moving the node after `curr` (`next`) to the front of the segment:  
   - `next = curr.next` captures the node to relocate.  
   - `curr.next = next.next` removes `next` from its original position.  
   - `next.next = prev.next` links `next` in front of the already‑reversed part.  
   - `prev.next = next` updates the head of the reversed portion.  
   - Loop invariant: nodes before `prev` are unchanged; the sub‑list from `prev.next` up to `curr` is the reversed prefix of the original interval, and `curr` remains the tail of that reversed prefix.  
5. **Return `dummy.next`**, which is the possibly new head of the whole list.

Edge cases handled explicitly: empty list (`head == null`), `left == right` (no work needed), and `left == 1` (dummy makes the splice logic identical).

## Dry Run  

Input: `head = [1,2,3,4,5]`, `left = 2`, `right = 4`

| i (iteration) | prev.val | curr.val | next.val | List after iteration | Note |
|---------------|----------|----------|----------|----------------------|------|
| 0 (setup) | 0 (dummy) | 2 | – | 1→2→3→4→5 | `prev` at node 1, `curr` at node 2 |
| 1 | 1 | 2 | 3 | 1→3→2→4→5 | `next` (3) moved before `curr` |
| 2 | 1 | 2 | 4 | 1→4→3→2→5 | `next` (4) moved before `curr` |
| end | 1 | 2 | – | 1→4→3→2→5 | Loop ran `right‑left = 2` times; segment `[2,4]` reversed |

After the loop, `prev.next` points to 4, `curr` points to 2 (the tail of the reversed segment), and the list matches the expected output.

## Complexity  
- **Time:** `O(n)` – the first loop walks `left‑1` nodes, the second loop runs `right‑left` times, and all pointer updates are constant‑time, so the total number of visited nodes is at most `n`.  
- **Space:** `O(1)` – only a few extra pointers (`dummy`, `prev`, `curr`, `next`) are used regardless of list size; the output list reuses the original nodes.

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
    public ListNode reverseBetween(ListNode head, int left, int right) {
         if (head == null || left == right) {
            return head;
        }

        // Dummy node helps when left = 1
        ListNode dummy = new ListNode(0);
        dummy.next = head;

        // Move prev to the node just before 'left'
        ListNode prev = dummy;

        for (int i = 1; i < left; i++) {
            prev = prev.next;
        }

        // Start of the section to reverse
        ListNode curr = prev.next;

        // Reverse nodes one by one
        for (int i = 0; i < right - left; i++) {

            ListNode next = curr.next;

            curr.next = next.next;
            next.next = prev.next;
            prev.next = next;
        }

        return dummy.next;
    }
}
```

---

**Runtime** 0 ms (beats 100.0%) · **Memory** 43.3 MB (beats 12.2%)

<sub>Synced by AILeetHub on 2026-09-07.</sub>
