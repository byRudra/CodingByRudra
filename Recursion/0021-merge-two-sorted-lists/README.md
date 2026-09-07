# 21. Merge Two Sorted Lists

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/merge-two-sorted-lists/)

`Linked List` · `Recursion`

## Intuition  
When both input lists are already sorted, the smallest remaining element is always at the front of one of the two lists. By repeatedly attaching the smaller front node to the result we guarantee that the merged list stays sorted without any extra storage or a second pass. A naïve solution could copy all values into an array, sort it, and rebuild a list, which would cost O((m + n) log(m + n)) time and O(m + n) space. The key observation lets us perform a single linear scan using two pointers, a classic **two‑pointer merge** pattern.

## Approach  
1. **Handle empty inputs** –  
   - If both `list1` and `list2` are `null`, return `list2` (which is `null`).  
   - If only one is `null`, return the non‑null list. These early returns avoid dereferencing `null` later.  

2. **Create a dummy anchor** –  
   - `dummy = new ListNode(0)` provides a stable head that never changes.  
   - `finalNode` is set to the smaller of the two initial heads: if `list1.val > list2.val` we swap `finalNode` to `list2` and advance `list2`. This establishes the invariant that `finalNode` always points to the last node of the merged prefix.  

3. **Main merging loop** –  
   - `while(list1 != null && list2 != null)` runs until one list is exhausted.  
   - Invariant: all nodes before `finalNode` are already merged in non‑decreasing order, and `list1`/`list2` point to the first unmerged nodes of their respective lists.  
   - If `list1.val > list2.val`, detach `list2` (`next = list2; list2 = list2.next`) and attach it (`finalNode.next = next; finalNode = finalNode.next`).  
   - Otherwise do the symmetric operation with `list1`.  

4. **Append the remainder of the non‑empty list** –  
   - After the main loop, exactly one of `list1` or `list2` may be non‑null.  
   - Two separate `while` loops walk the remaining nodes, each iteration preserving the invariant that `finalNode` is the tail of the merged list and simply linking the next node (`next = listX; listX = listX.next; finalNode.next = next; finalNode = finalNode.next`).  

5. **Return the merged list** –  
   - `dummy.next` skips the placeholder and yields the true head.

## Dry Run  
**Input**: `list1 = [1,4]`, `list2 = [2,3]`

| Iter | list1.val | list2.val | finalNode.val (tail) | Action                               | Note                         |
|------|-----------|-----------|----------------------|--------------------------------------|------------------------------|
| 0    | 1         | 2         | 1 (dummy→1)          | `finalNode` initialized to `list1`  | `list1` moves to 4            |
| 1    | 4         | 2         | 2                    | attach `list2` node (2)             | `list2` moves to 3            |
| 2    | 4         | 3         | 3                    | attach `list2` node (3)             | `list2` becomes null          |
| 3    | 4         | null      | 4                    | append remaining `list1` node (4)   | `list1` becomes null          |

After iteration 3 both source pointers are `null`; the merged list reachable from `dummy.next` is `[1,2,3,4]`, which is correctly sorted.

## Complexity  
- **Time:** O(m + n) – each node is visited exactly once; the main loop advances two pointers together, and the tail loops handle any leftover nodes.  
- **Space:** O(1) – only a few pointer variables (`dummy`, `finalNode`, `next`) are used; the output list reuses the original nodes, so no additional heap allocation beyond the constant‑size dummy node.

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
    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        
        if(list1 == null && list2 == null) return list2;
        else if(list1 == null) return list2;
        else if(list2 == null) return list1;
        ListNode dummy = new ListNode(0);
        ListNode finalNode = list1;
        if(list1.val > list2.val){
            finalNode = list2;
            list2 = list2.next;
        }
        dummy.next = finalNode;
        while(list1 != null && list2 != null){
            if(list1.val > list2.val){
                ListNode next = list2;
                list2 = list2.next;
                finalNode.next = next;
                finalNode = finalNode.next;
            }else{
                ListNode next = list1;
                list1 = list1.next;
                finalNode.next = next;
                finalNode = finalNode.next;
            }
        }
        while(list1 != null){
                ListNode next = list1;
                list1 = list1.next;
                finalNode.next = next;
                finalNode = finalNode.next;
        }
        while(list2 != null){
                ListNode next = list2;
                list2 = list2.next;
                finalNode.next = next;
                finalNode = finalNode.next;
        }
        return dummy.next;
    }
}
```

---

**Runtime** 0 ms (beats 100.0%) · **Memory** 44.2 MB (beats 75.0%)

<sub>Synced by AILeetHub on 2026-09-07.</sub>
