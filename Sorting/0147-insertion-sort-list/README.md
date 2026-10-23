# 147. Insertion Sort List

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/insertion-sort-list/)

`Linked List` · `Sorting`

## Intuition  
The core observation is that insertion sort on a linked list can be performed in a single forward pass if we keep a growing “sorted” prefix anchored by a dummy head. At any moment the nodes before `curr` are already in non‑decreasing order, and `curr` is the first unsorted element. By scanning from the dummy each time we locate the exact predecessor (`prev`) where `curr` belongs, we can splice `curr` into the sorted prefix without ever revisiting earlier elements. A naïve approach would copy values into an array, sort, and rebuild the list (O(n log n) extra space) or use a nested loop that restarts from the head for each insertion (still O(n²) time but with more pointer gymnastics). The single‑pass, dummy‑anchored technique eliminates the extra pass and any auxiliary container, relying only on two pointers per iteration.

## Approach  
1. **Create a dummy node** (`dummy = new ListNode(0)`). It precedes the sorted list and simplifies edge handling (inserting before the original head).  
2. **Initialize `curr`** to the original `head`. This pointer walks through the original list exactly once.  
3. **Loop while `curr != null`**:  
   - Store `curr.next` in `next` because we will rewire `curr` and lose the original link.  
   - **Find insertion point**: set `prev = dummy`. While `prev.next != null && prev.next.val < curr.val`, advance `prev`. Invariant: all nodes before `prev` are ≤ `curr.val`, and `prev.next` is the first node > `curr.val` (or null). This handles empty sorted list, duplicates, and the case where `curr` belongs at the end.  
   - **Splice `curr`**: `curr.next = prev.next; prev.next = curr;`. After this, the sorted prefix (dummy → …) includes `curr` in the correct order.  
   - Move `curr` to `next` to process the next original element.  
4. Return `dummy.next`, the head of the fully sorted list.

Key decisions:  
- Using `<=` in the inner condition would place equal values after existing ones, preserving stability; the code uses `<` so equal values are inserted before the first larger element, which also yields a correct sorted order.  
- The dummy node avoids a special case when the smallest element appears later; without it we would need to update the external head pointer inside the loop.  

## Dry Run  
Input: `[4, 2, 1, 3]`

| Iter | `curr.val` | `prev.val` (after search) | Sorted list (dummy→) | Action |
|------|------------|---------------------------|----------------------|--------|
| 1    | 4          | 0 (dummy)                 | 4                    | Insert after dummy |
| 2    | 2          | 0 (dummy)                 | 2 → 4                | Insert before 4 |
| 3    | 1          | 0 (dummy)                 | 1 → 2 → 4            | Insert before 2 |
| 4    | 3          | 2                         | 1 → 2 → 3 → 4        | Insert between 2 and 4 |

After the fourth iteration `curr` becomes `null`; the dummy’s next points to `1 → 2 → 3 → 4`, which is the required sorted order.

## Complexity  
- **Time:** O(n²) in the worst case because for each of the n nodes we may traverse up to the current length of the sorted prefix (the inner `while`).  
- **Space:** O(1) extra auxiliary space; only a few pointers (`dummy`, `curr`, `next`, `prev`) are used, independent of the input size. The output list reuses the original nodes.

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
    public ListNode insertionSortList(ListNode head) {
        ListNode dummy = new ListNode(0);   // head of the sorted list
        ListNode curr = head;

        while (curr != null) {
            ListNode next = curr.next;      // save before relinking

            // find the node after which curr belongs
            ListNode prev = dummy;
            while (prev.next != null && prev.next.val < curr.val) {
                prev = prev.next;
            }

            // splice curr in
            curr.next = prev.next;
            prev.next = curr;

            curr = next;
        }
        return dummy.next;
    }
}
```

---

**Runtime** 22 ms (beats 5.5%) · **Memory** 45.6 MB (beats 98.6%)

<sub>Synced by AILeetHub on 2026-10-23.</sub>
