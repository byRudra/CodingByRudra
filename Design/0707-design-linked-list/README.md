# 707. Design Linked List

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/design-linked-list/)

`Linked List` · `Design`

## Intuition  
The list maintains a `size` counter, so any index check can be answered in O(1) without scanning. Because the structure is singly linked, the only way to reach a specific position is to walk from the head, but we only need to walk **up to the predecessor** of the target node for insertion and deletion. This reduces the work to a single linear pass instead of two passes or auxiliary containers. The pattern exploited here is the classic *single‑pointer traversal* on a singly linked list, combined with a guard variable (`size`) that eliminates out‑of‑bounds work.

## Approach  
1. **Construction** – set `head = null` and `size = 0`.  
2. **get(index)**  
   - If `index < 0 || index >= size` → return `-1`.  
   - Initialise `curr = head`.  
   - Loop `i` from `0` to `index‑1`, each iteration moving `curr = curr.next`.  
   - Invariant: after `i` iterations, `curr` points to the node at position `i`.  
   - Return `curr.val`.  
3. **addAtHead(val)**  
   - Create `newNode`.  
   - Link `newNode.next = head`; update `head = newNode`.  
   - Increment `size`.  
4. **addAtTail(val)**  
   - Create `newNode`.  
   - If `head` is `null`, treat as empty list: assign `head = newNode` and increment `size`.  
   - Otherwise, start `curr = head` and walk while `curr.next != null`.  
   - Invariant: `curr` always points to the last visited node; loop ends with `curr` at the current tail.  
   - Attach `curr.next = newNode` and increment `size`.  
5. **addAtIndex(index, val)**  
   - Reject if `index < 0 || index > size`.  
   - If `index == 0`, delegate to `addAtHead`.  
   - Otherwise, start `curr = head` and move `index‑1` steps (`for i < index‑1`).  
   - Invariant: after the loop, `curr` is the node just before the insertion point.  
   - Insert by `newNode.next = curr.next; curr.next = newNode;` then `size++`.  
6. **deleteAtIndex(index)**  
   - Reject if `index < 0 || index >= size`.  
   - If `index == 0`, remove head by `head = head.next` and decrement `size`.  
   - Otherwise, walk `index‑1` steps to reach the predecessor (`curr`).  
   - Bypass the target node: `curr.next = curr.next.next;` then `size--`.  

All loops terminate because the list length is bounded by `size`, and the guard checks guarantee we never dereference `null`.

## Dry Run  
Operation sequence (from the example):  

```
addAtHead(1) → addAtTail(3) → addAtIndex(1,2) → get(1) → deleteAtIndex(1) → get(1)
```

| Step | index | curr.val (after loop) | size | head → …                | Note                              |
|------|-------|-----------------------|------|------------------------|-----------------------------------|
| 1    | –     | –                     | 1    | 1                      | addAtHead creates node 1          |
| 2    | –     | –                     | 2    | 1 → 3                  | addAtTail walks to tail, appends 3|
| 3    | 1     | 1                     | 3    | 1 → 2 → 3              | addAtIndex walks to node 1, inserts 2 |
| 4    | 1     | 2                     | 3    | 1 → 2 → 3              | get returns 2                    |
| 5    | 1     | 1                     | 2    | 1 → 3                  | deleteAtIndex walks to node 1, skips node 2 |
| 6    | 1     | 3                     | 2    | 1 → 3                  | get returns 3                    |

After the final step the list is `1 → 3`, and the last `get` correctly yields `3`.

## Complexity  
- **Time:**  
  - `get`, `addAtTail`, `addAtIndex`, `deleteAtIndex` each run O(k) where *k* ≤ *size* because they walk at most `index` (or `size‑1`) nodes; in the worst case this is O(n).  
  - `addAtHead` is O(1) since it only rewires the head pointer.  
- **Space:** O(1) extra auxiliary space; the algorithm stores only a few pointers (`head`, `curr`, `newNode`) regardless of list length. The output values themselves are not counted.

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

**Runtime** 104 ms (beats 44.3%) · **Memory** 47.1 MB (beats 37.1%)

<sub>Synced by AILeetHub on 2026-09-17.</sub>
