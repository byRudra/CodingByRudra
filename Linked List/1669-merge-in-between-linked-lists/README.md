# 1669. Merge In Between Linked Lists

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/merge-in-between-linked-lists/)

`Linked List`

## Intuition  
The only thing that changes the shape of the two lists is the two “cut points”: the node just before index `a` and the node just after index `b`. If we can locate those two anchors, we can splice `list2` in one constant‑time rewiring. A naïve solution would copy the first part, copy `list2`, then copy the tail – O(n + m) extra memory and another pass. The observation that the original nodes can be reused eliminates both the extra pass and the auxiliary storage. This is a classic **two‑pointer splice** pattern.

## Approach  
1. **Find the node before `a`.**  
   ```java
   ListNode beforeA = list1;
   for (int i = 0; i < a - 1; i++) {
       beforeA = beforeA.next;
   }
   ```  
   *Exit condition:* loop stops when `i == a‑1`.  
   *Invariant:* after each iteration `beforeA` points to the node at position `i+1` (0‑based). When the loop ends, `beforeA` is exactly the node whose `next` should become the head of `list2`.  

2. **Find the node after `b`.**  
   ```java
   ListNode afterB = list1;
   for (int i = 0; i <= b; i++) {
       afterB = afterB.next;
   }
   ```  
   *Exit condition:* loop stops when `i == b`.  
   *Invariant:* after each iteration `afterB` points to the node at position `i+1`. After the loop, `afterB` is the first node that must follow the merged segment (the original node at index `b+1`).  

3. **Connect `beforeA` to the head of `list2`.**  
   ```java
   beforeA.next = list2;
   ```  
   This discards the original segment `[a … b]` from the chain.

4. **Advance `list2` to its tail.**  
   ```java
   while (list2.next != null) {
       list2 = list2.next;
   }
   ```  
   The loop walks to the last node of `list2`. The invariant is that `list2` always points to the current tail; when the condition fails, `list2` is the true tail.

5. **Link the tail of `list2` to `afterB`.**  
   ```java
   list2.next = afterB;
   ```  
   The original tail of `list1` after index `b` is now attached, completing the splice.

6. **Return the original head (`list1`).**  
   The head never changes because `a ≥ 1` by constraints, so `list1` remains the entry point.

*Edge handling:* The constraints guarantee `a ≥ 1` and `b < list1.length‑1`, so `beforeA` and `afterB` are always valid nodes; no extra null checks are needed.

## Dry Run  

**Input**  
`list1 = [10 → 1 → 13 → 6 → 9 → 5]`, `a = 3`, `b = 4`, `list2 = [1000000 → 1000001 → 1000002]`

| Step | i (first loop) | beforeA.val | i (second loop) | afterB.val | Action / Note |
|------|----------------|------------|-----------------|------------|----------------|
| 0    | –              | 10 (head)  | –               | 10         | initialization |
| 1    | 0 → 1          | 1          | –               | 10         | move `beforeA` |
| 2    | 1 → 2          | 13         | –               | 10         | `beforeA` now at index 2 (node before `a`) |
| 3    | –              | 13         | 0 → 1           | 1          | start second loop, `afterB` moves to index 1 |
| 4    | –              | 13         | 1 → 2           | 13         | `afterB` at index 2 |
| 5    | –              | 13         | 2 → 3           | 6          | `afterB` at index 3 |
| 6    | –              | 13         | 3 → 4           | 9          | `afterB` at index 4 |
| 7    | –              | 13         | 4 → 5           | 5          | loop ends; `afterB` points to node after `b` (value 5) |
| 8    | –              | 13         | –               | 5          | `beforeA.next = list2` attaches list2 |
| 9    | –              | –          | –               | –          | while loop moves `list2` to tail (value 1000002) |
|10    | –              | –          | –               | –          | `list2.next = afterB` links tail to node 5 |

Final list: `10 → 1 → 13 → 1000000 → 1000001 → 1000002 → 5`. The segment `[6,9]` (indices 3‑4) is removed and replaced by `list2`.

## Complexity  
- **Time:** O(n + m) – the first two `for` loops together traverse at most `b+1 ≤ n` nodes of `list1`, and the `while` loop walks the entire `list2` of length `m`.  
- **Space:** O(1) – only a handful of pointers (`beforeA`, `afterB`, `list2`) are used; no extra data structures are allocated.

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
    public ListNode mergeInBetween(ListNode list1, int a, int b, ListNode list2) {
        ListNode beforeA = list1;
        ListNode afterB = list1;

        for (int i = 0; i < a - 1; i++) {
            beforeA = beforeA.next;
        }

        for (int i = 0; i <= b; i++) {
            afterB = afterB.next;
        }

        beforeA.next = list2;
        while (list2.next != null) {
            list2 = list2.next;
        }
        list2.next = afterB;

        return list1;
    }
}
```

---

**Runtime** 1 ms (beats 100.0%) · **Memory** 49.8 MB (beats 5.3%)

<sub>Synced by AILeetHub on 2026-02-21.</sub>
