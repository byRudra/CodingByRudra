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
    public boolean isPalindrome(ListNode head) {
         ListNode copyHead = new ListNode(head.val);
        ListNode copy = copyHead;
        ListNode curr = head.next;

        while (curr != null) {
            copy.next = new ListNode(curr.val);
            copy = copy.next;
            curr = curr.next;
        }

        // Reverse the copied list
        ListNode prev = null;
        curr = copyHead;

        while (curr != null) {
            ListNode next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }

        ListNode reversed = prev;
        while (head != null) {
            if (head.val != reversed.val)
                return false;
            head = head.next;
            reversed = reversed.next;
        }
        return true;
    }
}