# 2130. Maximum Twin Sum of a Linked List

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/maximum-twin-sum-of-a-linked-list/)

`Linked List` · `Two Pointers` · `Stack`

## Intuition  
When a fast pointer moves two nodes for every one node the slow pointer moves, after *k* iterations fast has advanced `2k` nodes while slow has advanced `k`. Because the list length is even, the moment fast reaches the end, slow points exactly at the start of the second half. This observation lets us pair each node with its twin without a second traversal or extra storage. By reversing the second half in‑place we can walk the two halves simultaneously, compute each twin sum, and keep the maximum. The overall pattern is the classic **two‑pointer with in‑place reversal** technique.

## Approach  
1. **Find the midpoint**  
   ```java
   while (fast != null && fast.next != null) {
       slow = slow.next;
       fast = fast.next.next;
   }
   ```  
   *Exit condition*: `fast` is `null` or `fast.next` is `null`.  
   *Invariant*: `slow` is always exactly half the distance traveled by `fast`. When the loop ends, `slow` is the first node of the right half.

2. **Reverse the right half**  
   ```java
   ListNode prev = null;
   ListNode curr = slow;
   while (curr != null) {
       ListNode next = curr.next;
       curr.next = prev;
       prev = curr;
       curr = next;
   }
   ```  
   *Exit condition*: `curr` becomes `null`.  
   *Invariant*: Nodes before `prev` are already reversed, nodes after `curr` are untouched. After the loop, `prev` points to the head of the reversed second half.

3. **Traverse both halves to compute twin sums**  
   ```java
   ListNode first = head;
   ListNode second = prev;
   while (second != null) {
       int sum = first.val + second.val;
       first = first.next;
       second = second.next;
       maxSum = Math.max(sum, maxSum);
   }
   ```  
   *Exit condition*: `second` becomes `null` (the reversed half is exhausted).  
   *Invariant*: `first` and `second` always point to nodes that are twins in the original list. The loop updates `maxSum` with the largest observed twin sum.

4. **Return `maxSum`** – the required answer.

*Edge handling*: The code works for the minimal even length `2` because the first loop stops immediately (`fast.next` is `null`), `slow` points to the second node, reversal yields a single‑node list, and the final loop processes exactly one pair. No special case for odd lengths is needed because the problem guarantees even length.

## Dry Run  
Input: `[5, 4, 2, 1]`

| iter | first.val | second.val | sum | maxSum | note |
|------|-----------|------------|-----|--------|------|
| 1    | 5         | 1          | 6   | 6      | first→4, second→2 |
| 2    | 4         | 2          | 6   | 6      | first→null, second→null (loop ends) |

After the loop `maxSum = 6`, which is the maximum twin sum for the list.

## Complexity  
- **Time:** `O(n)` – the fast/slow scan, the reversal, and the final pairwise scan each traverse at most `n/2` nodes.  
- **Space:** `O(1)` – only a handful of pointers (`slow`, `fast`, `prev`, `curr`, `first`, `second`) are used; the list is modified in‑place and no auxiliary data structures are allocated.

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
    public int pairSum(ListNode head) {
        ListNode slow = head;
        ListNode fast = head;
        while(fast != null && fast.next != null){
            slow = slow.next;
            fast = fast.next.next;
        }
        ListNode prev = null;
        ListNode curr = slow;

        while(curr != null){
            ListNode next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }
        int maxSum = 0;
        ListNode first = head;
        ListNode second = prev;
        while(second != null){
            int sum = first.val + second.val;
            first = first.next;
            second = second.next;
            maxSum = Math.max(sum, maxSum);
        }
        return maxSum;

    }
}
```

---

**Runtime** 4 ms (beats 98.8%) · **Memory** 100.9 MB (beats 80.7%)

<sub>Synced by AILeetHub on 2026-10-11.</sub>
