# 445. Add Two Numbers II

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/add-two-numbers-ii/)

`Linked List` · `Math` · `Stack`

## Intuition  
When the most‑significant digit is at the head, adding from left to right would require looking ahead to handle carries. By reversing each list we turn the problem into the classic “add from least significant digit” scenario: after `k` iterations the pointer `l1` (or `l2`) has moved `k` nodes forward, exactly the digits we need to sum. This eliminates the need for extra passes, hash maps, or recursion; we only need a single linear scan after the two reversals. The overall pattern is a **two‑pointer traversal on reversed lists**.

## Approach  
1. **Reverse both inputs** – `l1 = reverse(l1)` and `l2 = reverse(l2)`.  
   *Loop invariant*: `reverseHead` always points to the already‑reversed prefix of the original list; the original `head` variable moves forward. The loop ends when `head` becomes `null`, meaning the whole list is reversed.  
2. **Initialize** `head = null` (the result’s front) and `carry = 0`.  
3. **Iterate while any source still has nodes or a carry exists**: `while(l1 != null || l2 != null || carry != 0)`.  
   *Invariant*: At the start of each iteration `carry` holds the overflow from the previous digit, and `head` points to the partially built result in correct forward order.  
   - Set `sum = carry`.  
   - If `l1 != null`, add `l1.val` to `sum` and advance `l1 = l1.next`.  
   - If `l2 != null`, add `l2.val` to `sum` and advance `l2 = l2.next`.  
   - Compute new `carry = sum / 10`.  
   - Create a new node `node = new ListNode(sum % 10)`.  
   - Prepend it: `node.next = head; head = node;`.  
   This prepend keeps the result in forward order even though we are processing digits from least to most significant.  
4. **Return** `head`, which now represents the sum with the most significant digit at the front.  

Edge‑case handling:  
- Empty or single‑node inputs are safely processed because the reversal loops handle `null` gracefully and the main loop’s condition includes `carry != 0`.  
- When the lists have different lengths, the `if(l1 != null)` / `if(l2 != null)` guards ensure we only add existing digits.  
- The `while` condition uses `||` (logical OR) rather than `&&` so the loop continues as long as any source still contributes a digit or a leftover carry exists.

## Dry Run  

Input: `l1 = [7,2,4,3]`, `l2 = [5,6,4]`

| Iter | l1.val | l2.val | sum (incl. carry) | carry | new node.val | head (as list) | note |
|------|--------|--------|-------------------|-------|--------------|----------------|------|
| 1    | 3      | 4      | 3+4+0 = 7         | 0     | 7            | [7]            | after reversal, least sig digits are 3 and 4 |
| 2    | 4      | 6      | 4+6+0 = 10        | 1     | 0            | [0,7]          | carry generated |
| 3    | 2      | 5      | 2+5+1 = 8         | 0     | 8            | [8,0,7]        | |
| 4    | 7      | null   | 7+0+0 = 7         | 0     | 7            | [7,8,0,7]      | l2 exhausted |
| 5    | null   | null   | 0+0+0 = 0 (loop ends) | – | – | – | loop exits because all pointers null and carry 0 |

Final `head` = `[7,8,0,7]`, which is the correct sum.

## Complexity  
- **Time:** O(n + m) – each list is traversed once for reversal (n and m nodes) and once more for the addition loop, giving a linear total.  
- **Space:** O(1) auxiliary – the algorithm only uses a few pointers (`head`, `carry`, `reverseHead`, temporary `temp`) and creates the output list; no extra data structures proportional to input size are allocated.

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
//     public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
//         Stack<Integer> nums1 = new Stack<>();
//         Stack<Integer> nums2 = new Stack<>();

//          while (l1 != null) {
//             nums1.push(l1.val);
//             l1 = l1.next;
//         }

//         while (l2 != null) {
//             nums2.push(l2.val);
//             l2 = l2.next;
//         }

//         ListNode head = null;
//         int carry = 0;

//         while(!nums1.isEmpty() || !nums2.isEmpty() || carry != 0){
//             int sum = carry;

//             if(!nums1.isEmpty()){
//                 sum += nums1.pop();
//             }

//             if(!nums2.isEmpty()){
//                 sum += nums2.pop();
//             }

//             carry = sum / 10;

//             ListNode node = new ListNode(sum % 10);
//             node.next = head;
//             head = node; 
//         }
//         return head;

//     }
// }


class Solution {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        l1 = reverse(l1);
        l2 = reverse(l2);

        ListNode head = null;
        int carry = 0;

        while(l1 != null || l2 != null || carry != 0){
            int sum = carry;

            if(l1 != null){
                sum += l1.val;
                l1 = l1.next;
            }
            if(l2 != null){
                sum += l2.val;
                l2 = l2.next;
            }

            carry = sum / 10;

            ListNode node = new ListNode(sum % 10);
            node.next = head;
            head = node; 
        }
        return head;

    }
    public ListNode reverse(ListNode head) {
        ListNode reverseHead = null;

        while (head != null) {
            ListNode temp = head.next;
            head.next = reverseHead;
            reverseHead = head;
            head = temp;
        }

        return reverseHead;
    }
}
```

---

**Runtime** 3 ms (beats 53.0%) · **Memory** 45.5 MB (beats 99.9%)

<sub>Synced by AILeetHub on 2026-10-23.</sub>
