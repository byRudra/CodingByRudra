# 234. Palindrome Linked List

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/palindrome-linked-list/)

`Linked List` · `Two Pointers` · `Stack` · `Recursion`

## Intuition  
When two pointers move through a singly‑linked list at different speeds—`fast` advancing two nodes per step and `slow` advancing one—their positions after each iteration satisfy `fast = 2·slow`. Consequently, when `fast` reaches the end, `slow` is exactly at the list’s midpoint. This observation lets us split the list in a single pass, reverse the second half in‑place, and then walk both halves together to verify symmetry. The naïve alternative would copy the list into an array or use a stack, costing O(n) extra space; the fast/slow trick eliminates that overhead while still using only O(1) additional memory.

## Approach  
1. **Initialize pointers** `fast = head` and `slow = head`.  
2. **Locate the middle** – loop while `fast != null && fast.next != null`:  
   * `fast = fast.next.next` (moves two steps)  
   * `slow = slow.next` (moves one step)  
   * *Invariant*: after `k` iterations, `fast` is `2k` nodes ahead of the start and `slow` is `k` nodes ahead; thus `slow` always points to the node that will become the head of the second half.  
3. **Reverse the second half** – set `reverse = null`. While `slow != null`:  
   * `next = slow.next` (temporarily store)  
   * `slow.next = reverse` (link current node to the reversed prefix)  
   * `reverse = slow` (extend reversed list)  
   * `slow = next` (advance)  
   * *Invariant*: `reverse` is the head of the already‑reversed portion, and `slow` points to the first unreversed node.  
4. **Compare halves** – while `reverse != null`:  
   * If `reverse.val != head.val`, return `false`.  
   * Advance both: `reverse = reverse.next`, `head = head.next`.  
   * *Invariant*: the first `i` nodes of the original left half and the first `i` nodes of the reversed right half have been shown equal.  
5. If the loop finishes, every paired node matched; return `true`.

Edge cases are handled naturally: a single‑node list makes the first loop skip (since `fast.next` is `null`), leaving `slow` at the only node, which reverses to itself and matches immediately. An even‑length list stops with `fast == null`; an odd‑length list stops with `fast != null` but `fast.next == null`, leaving the central node in the reversed half—its value is compared against the symmetric counterpart, which is correct.

## Dry Run  

Input: `1 → 2 → 2 → 1`

| Iter | fast          | slow          | reverse (head) | Note                              |
|------|---------------|---------------|----------------|-----------------------------------|
| 0    | 1             | 1             | null           | initial pointers                  |
| 1    | 2 (third node) | 2 (second)   | null           | fast jumps two, slow one step     |
| 2    | null          | 2 (third)     | null           | fast hits end, loop ends          |
| –    | –             | –             | 2 → 1          | reverse second half (`slow` list) |
| –    | –             | –             | 2 → 1          | compare 1 vs 1 (head vs reverse) |
| –    | –             | –             | 1              | compare 2 vs 2                    |
| –    | –             | –             | null           | all nodes matched → true          |

The algorithm ends with `reverse` exhausted, confirming the list is a palindrome.

## Complexity  
- **Time:** O(n) – the three while‑loops together traverse each node at most a constant number of times (finding the middle, reversing half, and comparing).  
- **Space:** O(1) – only a handful of pointer variables are used; the output boolean does not count toward auxiliary space.

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
// class Solution {
//     public boolean isPalindrome(ListNode head) {
//          ListNode copyHead = new ListNode(head.val);
//         ListNode copy = copyHead;
//         ListNode curr = head.next;

//         while (curr != null) {
//             copy.next = new ListNode(curr.val);
//             copy = copy.next;
//             curr = curr.next;
//         }

//         // Reverse the copied list
//         ListNode prev = null;
//         curr = copyHead;

//         while (curr != null) {
//             ListNode next = curr.next;
//             curr.next = prev;
//             prev = curr;
//             curr = next;
//         }

//         ListNode reversed = prev;
//         while (head != null) {
//             if (head.val != reversed.val)
//                 return false;
//             head = head.next;
//             reversed = reversed.next;
//         }
//         return true;
//     }
// }

// better approach
class Solution {
    public boolean isPalindrome(ListNode head) {
        ListNode fast = head;
        ListNode slow = head;

        while(fast != null && fast.next != null){
            fast = fast.next.next;
            slow = slow.next;
        }

        ListNode reverse = null;
        while(slow != null){
            ListNode next = slow.next;
            slow.next = reverse;
            reverse = slow;
            slow = next;
        }

        while(reverse != null){
            if(reverse.val != head.val)
                return false;
            reverse = reverse.next;
            head = head.next;
        }
        return true;
    }
}
```

---

**Runtime** 3 ms (beats 99.8%) · **Memory** 94.4 MB (beats 54.4%)

<sub>Synced by AILeetHub on 2026-09-13.</sub>
