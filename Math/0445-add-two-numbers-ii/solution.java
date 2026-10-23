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