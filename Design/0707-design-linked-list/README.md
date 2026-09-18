# 707. Design Linked List

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/design-linked-list/)

`Linked List` · `Design`

## Intuition  
The list can be treated as a mutable chain where each operation only needs to know the node immediately before the target position. By keeping a `size` counter we can reject out‑of‑range indices without a full traversal, and by walking at most `index‑1` steps we locate the predecessor in one pass. A naïve solution would scan the whole list for every operation or store nodes in an auxiliary array, incurring extra passes or space. The key observation is that all modifications are local to a single link, so a single linear walk suffices for `addAtIndex`, `deleteAtIndex`, and `get`. This follows the classic **two‑pointer (single‑pointer) traversal** pattern on a singly linked list.

## Approach  
1. **Initialize** – `head = null` and `size = 0`.  
2. **get(index)**  
   - If `index < 0 || index >= size` return `-1`.  
   - Start `curr = head` and advance `curr = curr.next` exactly `index` times; the loop invariant is “`curr` points to the node at position `i`”.  
   - Return `curr.val`.  
3. **addAtHead(val)**  
   - Create `newNode = new Node(val)`.  
   - Link `newNode.next = head` and update `head = newNode`.  
   - Increment `size`.  
4. **addAtTail(val)**  
   - Create `newNode`.  
   - If `head` is `null`, treat the list as empty: assign `head = newNode`, increment `size`, and return.  
   - Otherwise, walk with `curr = head` while `curr.next != null`; invariant: “`curr` is the last visited node, and its `next` is the remainder of the list”.  
   - After the loop, set `curr.next = newNode` and increment `size`.  
5. **addAtIndex(index, val)**  
   - Guard with `if (index < 0 || index > size) return;`. Note the `>` (not `>=`) because inserting at `size` is allowed (append).  
   - If `index == 0`, delegate to `addAtHead`.  
   - Otherwise, locate the predecessor: `curr = head; for i = 0 … index‑2 { curr = curr.next; }`. Invariant: “`curr` is the node just before the insertion point”.  
   - Insert: `newNode.next = curr.next; curr.next = newNode; size++;`.  
6. **deleteAtIndex(index)**  
   - Guard with `if (index < 0 || index >= size) return;`.  
   - If `index == 0`, remove the head by `head = head.next; size--; return;`.  
   - Locate predecessor exactly as in step 5.  
   - Bypass the target: `curr.next = curr.next.next; size--;`.  

All loops terminate because the list length is bounded by `size`, and each operation updates `size` consistently.

## Dry Run  
Input operations: `["MyLinkedList","addAtHead","addAtTail","addAtIndex","get","deleteAtIndex","get"]` with values `[[],[1],[3],[1,2],[1],[1],[1]]`.

| Step | index | val | curr (pre‑op) | Action / Change | size |
|------|-------|-----|--------------|-----------------|------|
| 1 – init | – | – | head=null | `head=null, size=0` | 0 |
| 2 – addAtHead(1) | – | 1 | head=null | newNode→1, newNode.next=null, head=1 | 1 |
| 3 – addAtTail(3) | – | 3 | curr=1 (head) | traverse none (curr.next null), curr.next=3 | 2 |
| 4 – addAtIndex(1,2) | 1 | 2 | curr=1 (head) | loop runs 0 times, insert after 1 → 1→2→3 | 3 |
| 5 – get(1) | 1 | – | curr=head=1 | advance once → curr=2, return 2 | 3 |
| 6 – deleteAtIndex(1) | 1 | – | curr=1 (head) | loop 0 times, bypass 2 → 1→3 | 2 |
| 7 – get(1) | 1 | – | curr=head=1 | advance once → curr=3, return 3 | 2 |

Final list is `1 → 3`; the two `get` calls correctly return `2` then `3`.

## Complexity  
- **Time:** O(n) worst case per operation, because the longest walk (`addAtTail`, `addAtIndex`, `deleteAtIndex`, `get`) traverses at most `size` nodes, and `addAtHead` is O(1).  
- **Space:** O(1) auxiliary space; the algorithm stores only a few pointers (`head`, `curr`, `newNode`) regardless of list length. The output values themselves are not counted.

## Solution (Java)

```java
class MyLinkedList {

    class Node {
        int val;
        Node next;

        Node(int val) {
            this.val = val;
        }
    }

    Node head;
    int size;

    public MyLinkedList() {
        head = null;
        size = 0;
    }

    public int get(int index) {
        if (index < 0 || index >= size)
            return -1;

        Node curr = head;

        for (int i = 0; i < index; i++) {
            curr = curr.next;
        }

        return curr.val;
    }

    public void addAtHead(int val) {
        Node newNode = new Node(val);

        newNode.next = head;
        head = newNode;

        size++;
    }

    public void addAtTail(int val) {
        Node newNode = new Node(val);

        if (head == null) {
            head = newNode;
            size++;
            return;
        }

        Node curr = head;

        while (curr.next != null) {
            curr = curr.next;
        }

        curr.next = newNode;
        size++;
    }

    public void addAtIndex(int index, int val) {
        if (index < 0 || index > size)
            return;

        if (index == 0) {
            addAtHead(val);
            return;
        }

        Node curr = head;

        // Reach node just before index
        for (int i = 0; i < index - 1; i++) {
            curr = curr.next;
        }

        Node newNode = new Node(val);

        newNode.next = curr.next;
        curr.next = newNode;

        size++;
    }

    public void deleteAtIndex(int index) {
        if (index < 0 || index >= size)
            return;

        if (index == 0) {
            head = head.next;
            size--;
            return;
        }

        Node curr = head;

        // Reach node just before index
        for (int i = 0; i < index - 1; i++) {
            curr = curr.next;
        }

        curr.next = curr.next.next;

        size--;
    }
}
```

---

**Runtime** 10 ms (beats 44.6%) · **Memory** 47.1 MB (beats 36.9%)

<sub>Synced by AILeetHub on 2026-09-17.</sub>
