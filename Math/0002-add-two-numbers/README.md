# 2. Add Two Numbers

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/add-two-numbers/)

`Linked List` · `Math` · `Recursion`

## Intuition  
When two numbers are stored digit‑wise in reverse order, the least significant digits line up at the heads of the lists. Adding them column by column is exactly the same as the elementary school addition: each step adds the two current digits plus any carry from the previous step, produces a new digit, and propagates a new carry. The naïve way would be to first reverse both lists or convert them to integers, which costs extra passes or large integer arithmetic. The key insight is that we can perform the addition in a single left‑to‑right sweep, maintaining only the running carry. This yields a classic **two‑pointer** (simultaneous traversal) pattern on linked lists.

## Approach  
1. **Create a dummy head** (`dummy = ListNode(0)`) and a pointer `current` that will build the result list.  
2. **Initialize `carry = 0`.**  
3. **Loop while any source still has nodes or a non‑zero carry** (`while l1 or l2 or carry:`).  
   - *Invariant*: before each iteration, `carry` holds the overflow from the previous digit, and `current` points to the last node of the partially built answer.  
4. **Extract current digit values**: `val1 = l1.val if l1 else 0` and `val2 = l2.val if l2 else 0`. This safely handles lists of unequal length and the case where one list is already exhausted.  
5. **Compute the column sum**: `sum = val1 + val2 + carry`.  
6. **Update carry**: `carry = sum // 10` (integer division yields 0 or 1 because each digit ≤9).  
7. **Append the new digit**: `current.next = ListNode(sum % 10)` and advance `current = current.next`.  
8. **Advance source pointers** only if they exist (`if l1: l1 = l1.next`, same for `l2`). This respects the convention that missing nodes contribute 0 and avoids `None.next` errors.  
9. When the loop exits, all digits and any final carry have been emitted, so `dummy.next` is the head of the correctly ordered result list.

Edge considerations:  
- Empty input cannot occur per constraints, but the loop condition also works for a single‑node list.  
- For odd‑length sums the final carry creates an extra node (e.g., 999 + 1 → 0→0→0→1).  
- The `or carry` clause ensures we don’t miss this trailing node.

## Dry Run  

**Input**: `l1 = [2,4,3]` (represents 342)  
**Input**: `l2 = [5,6,4]` (represents 465)

| Iter | val1 | val2 | carry_in | sum | digit (sum%10) | carry_out | Action on pointers |
|------|------|------|----------|-----|----------------|-----------|--------------------|
| 1    | 2    | 5    | 0        | 7   | 7              | 0         | l1→4, l2→6, add 7 |
| 2    | 4    | 6    | 0        | 10  | 0              | 1         | l1→3, l2→4, add 0 |
| 3    | 3    | 4    | 1        | 8   | 8              | 0         | l1→None, l2→None, add 8 |
| 4    | 0    | 0    | 0        | –   | –              | –         | Loop ends |

The constructed list is `[7,0,8]`, which indeed represents 807 = 342 + 465.

## Complexity  
- **Time:** O(n) – the loop runs once per digit of the longer list plus at most one extra iteration for a final carry, because `fast` (here the combined traversal) advances one node per iteration.  
- **Space:** O(n) – a new node is allocated for each output digit; aside from the output list, only a constant amount of auxiliary variables (`carry`, `current`, `val1`, `val2`) are used.

## Solution (Python3)

```python
# Definition for singly-linked list.
# class ListNode:
#     def __init__(self, val=0, next=None):
#         self.val = val
#         self.next = next
class Solution:
    def addTwoNumbers(self, l1: Optional[ListNode], l2: Optional[ListNode]) -> Optional[ListNode]:

        dummy = ListNode(0)
        current = dummy
        carry = 0

        while l1 or l2 or carry:
            val1 = l1.val if l1 else 0
            val2 = l2.val if l2 else 0

            sum = val1 + val2 + carry
            carry = sum // 10
            current.next = ListNode(sum % 10)
            current = current.next

            if l1 : l1 = l1.next
            if l2 : l2 = l2.next

        return dummy.next 

        
```

---

**Runtime** 0 ms (beats 100.0%) · **Memory** 19.3 MB (beats 79.2%)

<sub>Synced by AILeetHub on 2026-03-23.</sub>
