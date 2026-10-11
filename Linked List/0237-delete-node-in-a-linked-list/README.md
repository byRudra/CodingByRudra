# 237. Delete Node in a Linked List

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/delete-node-in-a-linked-list/)

`Linked List`

## Intuition  
The only obstacle to removing a node without head access is that we cannot change the predecessor’s `next` pointer. Because the list contains unique values and the target is guaranteed not to be the tail, we can sidestep this by **making the target node indistinguishable from its successor**: copy the successor’s value into the target and then skip the successor. This single observation eliminates the need for a second traversal, a hash map, or any auxiliary structure. The technique is a classic *in‑place overwrite* using two adjacent pointers.

## Approach  
1. **Overwrite the value** – `node.val = node.next.val`  
   *Invariant:* before the assignment, `node` holds the original value to be deleted and `node.next` holds the next distinct value. After the assignment, `node` now contains the value that originally lived in `node.next`.  
2. **Bypass the successor** – `node.next = node.next.next`  
   *Invariant:* after step 1, `node.next` still points to the original successor. Assigning `node.next` to `node.next.next` removes that successor from the chain, linking the current node directly to the following node (or `null` if the deleted node was the penultimate element).  

**Edge‑case handling**  
- The method assumes `node` is non‑null and `node.next` exists; the problem guarantees the node is not the tail, so no extra null‑checks are required.  
- Because values are unique, copying the successor’s value cannot create a duplicate in the list, preserving the “all values distinct” invariant.  
- No loop is used; the algorithm runs in constant time regardless of list length.  

## Dry Run  
**Input:** `head = [4 → 5 → 1 → 9]`, `node` points to the node with value 5  

| Step | `node.val` | `node.next.val` | `node.next` (reference) | Note |
|------|------------|----------------|--------------------------|------|
| 0 (initial) | 5 | 1 | → 1 → 9 | List is unchanged |
| 1 (`node.val = node.next.val`) | 1 | 1 | → 1 → 9 | Value 5 overwritten by 1 |
| 2 (`node.next = node.next.next`) | 1 | 9 | → 9 | Original node with value 1 is skipped |

After step 2 the list becomes `4 → 1 → 9`. The node that originally held 5 now holds 1 and points directly to 9, so the value 5 no longer appears in the list, satisfying the deletion requirement.

## Complexity  
- **Time:** **O(1)** – only two pointer/value assignments are executed, independent of the list size.  
- **Space:** **O(1)** – the algorithm uses a constant number of extra variables; the output list is the same structure with one node removed, so no additional memory is allocated.

## Solution (Java)

```java
/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) { val = x; }
 * }
 */
class Solution {
    public void deleteNode(ListNode node) {
        node.val = node.next.val;
        node.next = node.next.next;
    }
}
```

---

**Runtime** 0 ms (beats 100.0%) · **Memory** 45.1 MB (beats 78.5%)

<sub>Synced by AILeetHub on 2026-10-11.</sub>
