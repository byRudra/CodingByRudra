# 234. Palindrome Linked List

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/palindrome-linked-list/)

`Linked List` · `Two Pointers` · `Stack` · `Recursion`

## Intuition  
If we could look at the list from both ends simultaneously, checking palindrome becomes trivial: the values at symmetric positions must match. The obstacle is that a singly‑linked list only lets us move forward. By **making a separate copy of the list and reversing that copy**, we obtain a second forward‑only traversal that visits the original nodes in reverse order. Then a single linear scan can compare the original and reversed copies node‑by‑node. The naïve way would either require repeatedly walking to the tail (O(n²) time) or storing all values in a stack (O(n) extra space). Copy‑and‑reverse eliminates the repeated walks while still using only O(n) auxiliary memory. This follows the classic *two‑list comparison* pattern.

## Approach  
1. **Create a deep copy** of the input list.  
   - Initialise `copyHead` with `head.val` and set `copy = copyHead`.  
   - Iterate with `curr = head.next` while `curr != null`. In each iteration attach a new node `new ListNode(curr.val)` to `copy.next`, then advance `copy` and `curr`.  
   - Invariant: after processing `k` original nodes, the copied list contains exactly those `k` values in the same order.  
2. **Reverse the copied list in‑place.**  
   - Set `prev = null` and `curr = copyHead`.  
   - Loop while `curr != null`: store `next = curr.next`, redirect `curr.next = prev`, then move `prev = curr` and `curr = next`.  
   - Invariant: at the start of each iteration, the segment from `copyHead` up to `prev` is already reversed, and `curr` points to the first unreversed node.  
   - When the loop ends, `prev` points to the head of the fully reversed copy (`reversed`).  
3. **Compare the original list with the reversed copy.**  
   - While `head != null` (the two lists have equal length, so a single condition suffices), check `head.val != reversed.val`. If any mismatch occurs, return `false`.  
   - Advance both pointers: `head = head.next`, `reversed = reversed.next`.  
   - Invariant: after `i` iterations, the first `i` pairs of nodes have been verified equal.  
4. If the loop finishes without a mismatch, return `true`.  
   - Edge cases: a single‑node list creates a copy of length 1, reverses to itself, and the comparison loop runs once, correctly returning `true`. Empty input is impossible per constraints, so no extra guard is needed.

## Dry Run  
Input: `[1, 2, 2, 1]`

| Iter | `head.val` | `reversed.val` | Note |
|------|------------|----------------|------|
| 1    | 1          | 1              | values match, advance both |
| 2    | 2          | 2              | values match, advance both |
| 3    | 2          | 2              | values match, advance both |
| 4    | 1          | 1              | values match, both become `null` |

After the fourth iteration both pointers are `null`; no mismatches were found, so the algorithm returns `true`, confirming the list is a palindrome.

## Complexity  
- **Time:** O(n) – one pass to copy, one pass to reverse the copy, and one pass to compare, each visiting every node a constant number of times.  
- **Space:** O(n) – a new list of `n` nodes is allocated for the copy; the algorithm does not use additional data structures beyond these nodes. (The output boolean is excluded.)

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
    public boolean isPalindrome(ListNode head) {
         ListNode copyHead = new ListNode(head.val);
        ListNode copy = copyHead;
        ListNode curr = head.next;

        while (curr != null) {
            copy.next = new ListNode(curr.val);
            copy = copy.next;
            curr = curr.next;
        }

        // Reverse the copied list
        ListNode prev = null;
        curr = copyHead;

        while (curr != null) {
            ListNode next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }

        ListNode reversed = prev;
        while (head != null) {
            if (head.val != reversed.val)
                return false;
            head = head.next;
            reversed = reversed.next;
        }
        return true;
    }
}
```

---

**Runtime** 6 ms (beats 34.0%) · **Memory** 108.1 MB (beats 5.3%)

<sub>Synced by AILeetHub on 2026-09-13.</sub>
