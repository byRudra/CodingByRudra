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