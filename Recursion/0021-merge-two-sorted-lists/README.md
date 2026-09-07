# 21. Merge Two Sorted Lists

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/merge-two-sorted-lists/)

`Linked List` · `Recursion`

## Intuition  
The merged list can be built incrementally because at any moment the smallest unmerged element is at the front of one of the two input lists. By repeatedly attaching the smaller front node to the result we guarantee that the already‑built prefix stays sorted, eliminating the need for a second pass, a hash map, or recursion. This “always take the current minimum” rule is the classic two‑pointer pattern for merging sorted sequences.

## Approach  
1. **Handle trivial cases.**  
   ```cpp
   if (!list1 || !list2) return list1 ? list1 : list2;
   ```  
   If either list is empty, the other list is already the correct answer.  

2. **Choose the first head.**  
   Compare `list1->val` and `list2->val`.  
   - If `list1->val <= list2->val`, set `Head = list1` and advance `list1 = list1->next`.  
   - Otherwise set `Head = list2` and advance `list2 = list2->next`.  
   This establishes the invariant: **`Head` points to the smallest node overall, and the merged prefix (currently just `Head`) is sorted.**  

3. **Initialize the tail pointer.**  
   ```cpp
   ListNode *p = Head;
   ```  
   `p` always refers to the last node of the merged prefix.  

4. **Iterate while both lists have nodes.**  
   ```cpp
   while (list1 && list2) { … }
   ```  
   *Invariant*: all nodes before `p` are sorted and come from the original lists in order.  
   Inside the loop:  
   - If `list1->val <= list2->val`, attach `list1` (`p->next = list1`) and move `list1` forward.  
   - Else attach `list2` and move `list2` forward.  
   After the attachment, advance `p = p->next` so that `p` again points to the tail.  

5. **Append the remainder.**  
   When the loop exits, exactly one list is exhausted. The remaining list is already sorted, so a single assignment attaches it:  
   ```cpp
   (list1) ? p->next = list1 : p->next = list2;
   ```  

6. **Return the merged head.**  
   ```cpp
   return Head;
   ```  

## Dry Run  
Input  
```
list1 = [1,2,4]
list2 = [1,3,4]
```

| Step | list1.val | list2.val | p.val (tail) | Action                               | Note                     |
|------|-----------|-----------|--------------|--------------------------------------|--------------------------|
| 0 (init) | 1 | 1 | 1 (Head) | `Head = list1` (since 1 ≤ 1), advance list1 | First node chosen |
| 1 | 2 | 1 | 1 | `p->next = list2`; list2→3; p→1 (list2) | Attach smaller 1 from list2 |
| 2 | 2 | 3 | 1 | `p->next = list1`; list1→4; p→2 (list1) | Attach 2 |
| 3 | 4 | 3 | 2 | `p->next = list2`; list2→4; p→3 (list2) | Attach 3 |
| 4 | 4 | 4 | 3 | `p->next = list1`; list1→null; p→4 (list1) | Attach 4 from list1 |
| 5 | null | 4 | 4 | Exit loop, attach remainder `p->next = list2` | Append final 4 |

After step 5 the merged list is `1→1→2→3→4→4`, which is the required sorted order.

## Complexity  
- **Time:** `O(n + m)` – each iteration advances either `list1` or `list2` by one node, so the total number of pointer moves equals the sum of the lengths of the two input lists.  
- **Space:** `O(1)` – only a handful of pointers (`Head`, `p`, `list1`, `list2`) are used; no additional data structures are allocated beyond the existing nodes.

## Solution (C++)

```cpp
/**
 * Definition for singly-linked list.
 * struct ListNode {
 *     int val;
 *     ListNode *next;
 *     ListNode() : val(0), next(nullptr) {}
 *     ListNode(int x) : val(x), next(nullptr) {}
 *     ListNode(int x, ListNode *next) : val(x), next(next) {}
 * };
 */
class Solution {
public:
    ListNode* mergeTwoLists(ListNode* list1, ListNode* list2) {
        if (!list1 || !list2) return list1 ? list1 : list2;
        ListNode *Head = NULL;
        
        if (list1->val <= list2->val) {
            Head = list1;
            list1 = list1->next;
        } else {
            Head = list2;
            list2 = list2->next;
        }
        ListNode *p = Head;

        while(list1 && list2){
            if(list1->val <= list2->val){
                p->next = list1;
                list1 = list1->next;
            }
            else{
                p->next = list2;
                list2 = list2->next;
            }
            p = p->next;
        }
        (list1) ? p->next = list1 : p->next = list2;

        return Head;
    }
};
```

---

**Runtime** 4 ms (beats 1.5%) · **Memory** 19.8 MB (beats 8.4%)

<sub>Synced by AILeetHub on 2024-09-17.</sub>
