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