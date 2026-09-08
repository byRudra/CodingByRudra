# 703. Kth Largest Element in a Stream

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/kth-largest-element-in-a-stream/)

`Tree` · `Design` · `Binary Search Tree` · `Heap (Priority Queue)` · `Binary Tree` · `Data Stream`

## Intuition  
The key observation is that the *k*‑th largest value is exactly the smallest element among the *k* biggest numbers seen so far. If we can keep those *k* biggest numbers in a structure where the smallest of them is instantly accessible, we obtain the answer after each insertion without scanning the whole stream. A naïve solution would sort the entire list after every `add`, costing O(n log n) per operation, or store all elements in a hash map and recompute ranks, which also requires extra passes. By maintaining a min‑heap of at most *k* elements, we eliminate the need for a full sort or extra passes—only the heap’s top gives the answer. This follows the classic **two‑heap (or min‑heap of size k) pattern** for order‑statistics in a stream.

## Approach  
1. **Initialize**  
   - Store `k`.  
   - Create an empty `PriorityQueue<Integer> minHeap`.  
   - Iterate over the initial array `nums`. For each `num`, invoke `add(num)`.  
   - *Invariant*: after processing any prefix of `nums`, `minHeap` contains the largest `min(k, processedCount)` values, and its size never exceeds `k`.  

2. **add(int val)**  
   - `minHeap.offer(val)`: insert the new value.  
   - If `minHeap.size() > k`, execute `minHeap.poll()`. This removes the smallest element, guaranteeing the heap size returns to `k`.  
   - Return `minHeap.peek()`, which is the smallest among the stored *k* largest numbers, i.e., the current *k*‑th largest.  

   *Loop/condition details*: the only loop is the `for` in the constructor, which runs `nums.length` times. Inside `add`, the `if` guard (`size() > k`) is a strict `>` because we allow the heap to temporarily hold *k + 1* elements before discarding the excess smallest one. This choice avoids an off‑by‑one error where the *k*‑th largest would be hidden behind an extra element.

3. **Edge handling**  
   - When `nums` is empty, the constructor builds an empty heap; the first `add` call will populate it up to size `k`.  
   - If `k` exceeds the initial number of elements, the heap simply grows until it reaches `k`.  
   - Duplicate values are treated like any other integers; the heap may contain multiple equal entries, preserving correct ordering.

## Dry Run  
Input: `k = 3`, `nums = [4,5,8,2]` then calls `add(3)`, `add(5)`, `add(10)`, `add(9)`, `add(4)`.

| step | operation | minHeap (as sorted list) | peek | note |
|------|-----------|--------------------------|------|------|
| 0 | init (process 4) | [4] | 4 | heap size ≤ k |
| 1 | init (process 5) | [4,5] | 4 | |
| 2 | init (process 8) | [4,5,8] | 4 | |
| 3 | init (process 2) | [4,5,8] | 4 | 2 added → size 4 > k, poll removes 2 |
| 4 | add(3) | [3,5,8] | 3 | 3 added, size 4 > k, poll removes 4 |
| 5 | add(5) | [5,5,8] | 5 | 5 added, size 4 > k, poll removes 3 |
| 6 | add(10) | [5,8,10] | 5 | 10 added, poll removes 5 (the smaller 5) |
| 7 | add(9) | [8,9,10] | 8 | 9 added, poll removes 5 |
| 8 | add(4) | [8,9,10] | 8 | 4 added, poll removes 4 (heap unchanged) |

After the final insertion the heap holds the three largest numbers `[8,9,10]`; `peek` = 8, which is the 3‑rd largest overall.

## Complexity  
- **Time:** O(n log k) for construction (each of the *n* initial elements triggers `add`, which costs O(log k) because the heap never exceeds size *k*), and O(log k) per subsequent `add` call (heap insertion and possible removal).  
- **Space:** O(k) auxiliary space for the heap, independent of the total number of streamed elements (the output itself is not counted).

## Solution (Java)

```java
class KthLargest {

    private PriorityQueue<Integer> minHeap;
    private int k;

    public KthLargest(int k, int[] nums) {
        this.k = k;
        minHeap = new PriorityQueue<>();

        for (int num : nums) {
            add(num);
        }
    }

    public int add(int val) {
        minHeap.offer(val);

        if (minHeap.size() > k) {
            minHeap.poll();
        }

        return minHeap.peek();
    }
}

/**
 * Your KthLargest object will be instantiated and called as such:
 * KthLargest obj = new KthLargest(k, nums);
 * int param_1 = obj.add(val);
 */
```

---

**Runtime** 22 ms (beats 83.6%) · **Memory** 51.9 MB (beats 87.6%)

<sub>Synced by AILeetHub on 2026-09-08.</sub>
