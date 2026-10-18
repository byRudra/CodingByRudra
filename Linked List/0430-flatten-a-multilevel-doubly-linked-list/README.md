# 430. Flatten a Multilevel Doubly Linked List

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/flatten-a-multilevel-doubly-linked-list/)

`Linked List` · `Depth-First Search` · `Doubly-Linked List`

## Intuition  
When a node has a `child`, the whole child list must appear immediately after that node in the flattened order. If we can splice the child list between the node and its original `next`, the list becomes a single‑level structure and the spliced nodes will naturally be visited later by a simple linear walk. The naïve solution would first collect all nodes (e.g., via recursion or a stack) and then rebuild the list, costing extra passes or auxiliary memory. By observing that the child list’s tail can be linked to the original `next` in‑place, we eliminate any extra storage and keep the traversal one‑pass.

## Approach  
1. **Iterate** `curr` from `head` while `curr != null`.  
2. **Skip** nodes without a child (`if (curr.child == null) continue`).  
3. **Find the tail** of the child list: start `tail = curr.child` and move `tail = tail.next` until `tail.next == null`.  
   *Invariant*: `tail` is the last node of the current child sub‑list.  
4. **Connect tail to the original next**:  
   - `tail.next = curr.next`  
   - If `curr.next` exists, set `curr.next.prev = tail`.  
   This preserves the doubly‑linked property for the segment that follows the child list.  
5. **Splice the child list after `curr`**:  
   - `curr.next = curr.child`  
   - `curr.child.prev = curr`  
   - `curr.child = null` (the problem requires all child pointers cleared).  
   After this step the list segment `curr → child … tail → originalNext` is a correct doubly linked chain.  
6. **Advance** `curr = curr.next`. Because we just attached the child list, `curr.next` now points to the former child head, ensuring the newly inserted nodes are processed in subsequent iterations.  
7. **Return** `head` when the loop finishes.

Edge handling:  
- Empty list (`head == null`) yields immediate return.  
- Single‑node list with or without a child works because the tail search stops at the child node itself.  
- The `<=` vs `<` issue never appears; we always stop when `tail.next == null`, guaranteeing we have the true last node.  
- All pointer updates respect the doubly‑linked invariants, preventing off‑by‑one errors.

## Dry Run  

Input (visual): `1 ⇄ 2 ⇄ 3` with `2.child → 4 ⇄ 5` and `5.child → 6`.

| Iter | `curr.val` | `curr.child`? | `tail.val` (after search) | Action summary |
|------|------------|---------------|---------------------------|----------------|
| 1    | 1          | no            | –                         | No change, move to 2 |
| 2    | 2          | yes (4)       | 5                         | Link 5→3, 3←5; 2→4, 4←2; clear child |
| 3    | 4          | no            | –                         | Move to 5 |
| 4    | 5          | yes (6)       | 6                         | Link 6→3, 3←6; 5→6, 6←5; clear child |
| 5    | 6          | no            | –                         | Move to 3 |
| 6    | 3          | no            | –                         | End of list |

Final flattened order: `1 ⇄ 2 ⇄ 4 ⇄ 5 ⇄ 6 ⇄ 3`. All `child` pointers are `null`.

## Complexity  
- **Time:** O(N) – each node is visited at most twice (once as `curr`, once while walking to find a child’s tail).  
- **Space:** O(1) – only a few pointers (`curr`, `tail`) are used; the output list reuses the original nodes.

## Solution (Java)

```java
/*
// Definition for a Node.
class Node {
    public int val;
    public Node prev;
    public Node next;
    public Node child;
};
*/

class Solution {
    public Node flatten(Node head) {
        for(Node curr = head; curr != null; curr = curr.next){
            if(curr.child == null) continue;

            Node tail = curr.child;
            while(tail.next != null) tail = tail.next;
            tail.next = curr.next;
            if(curr.next != null) curr.next.prev = tail;

            curr.next = curr.child;
            curr.child.prev = curr;
            curr.child = null;

        }
        return head;
    }
}
```

---

**Runtime** 0 ms (beats 100.0%) · **Memory** 43.3 MB (beats 83.9%)

<sub>Synced by AILeetHub on 2026-10-18.</sub>
