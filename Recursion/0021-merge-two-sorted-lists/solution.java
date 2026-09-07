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