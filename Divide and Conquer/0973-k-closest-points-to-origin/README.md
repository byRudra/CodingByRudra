# 973. K Closest Points to Origin

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/k-closest-points-to-origin/)

`Array` · `Math` · `Divide and Conquer` · `Geometry` · `Sorting` · `Heap (Priority Queue)` · `Quickselect` · `K-D Tree`

## Intuition  
The key observation is that we only need to remember the **k smallest distances** seen so far; any point farther than the current k‑th closest can be discarded immediately. By keeping a max‑heap of at most *k* elements, the heap’s root always holds the farthest point among those retained, so when a new point’s distance is smaller we replace the root. This eliminates the need for a full sort (O(n log n)) or a second pass with a hash map, reducing the work to O(n log k). The pattern used is a **max‑heap (priority queue) of bounded size**.

## Approach  
1. **Create a max‑heap** `pq` with a comparator that orders points by their squared Euclidean distance `x*x + y*y` in descending order.  
2. **Iterate over every `point` in `points`**:  
   - `pq.offer(point)` inserts the current point.  
   - **Invariant**: after each insertion the heap contains the *k* points with the smallest distances among all points processed so far, possibly plus one extra if the size exceeded *k*.  
   - If `pq.size() > k`, execute `pq.poll()` to remove the farthest point (the heap root). This restores the invariant that the heap size never exceeds *k*.  
3. **Extract the result**: allocate `answer` of size `k`. While `i < k`, assign `answer[i] = pq.poll()`. Because the heap now holds exactly the *k* closest points, the order of extraction does not matter.  
4. **Return `answer`**.  

Edge considerations: the input guarantees `1 ≤ k ≤ points.length`, so the loops always run at least once. The comparator uses squared distances to avoid floating‑point errors and the extra cost of `Math.sqrt`. The check `pq.size() > k` (strictly greater) ensures we keep exactly *k* elements; using `>=` would incorrectly discard a needed element when `k` equals the current size after insertion.

## Dry Run  
**Input**: `points = [[1,3], [-2,2], [2,-2]]`, `k = 2`

| Iteration | Processed point | Heap contents (distance, point) | Note |
|-----------|----------------|----------------------------------|------|
| 1 | `[1,3]` (dist = 10) | [(10, [1,3])] | heap size 1 ≤ k |
| 2 | `[-2,2]` (dist = 8) | [(10, [1,3]), (8, [-2,2])] | size = k, root is farthest (10) |
| 3 | `[2,-2]` (dist = 8) | [(8, [2,-2]), (8, [-2,2]), (10, [1,3])] → poll → [(8, [2,-2]), (8, [-2,2])] | new point smaller than root, root removed |
| End | – | [(8, [2,-2]), (8, [-2,2])] | heap holds the 2 closest points |

After the loop, `answer` receives the two points in the heap, which are exactly the k = 2 closest to the origin.

## Complexity  
- **Time:** O(n log k) – each of the *n* points triggers a heap insertion (`log k`) and at most one removal (`log k`).  
- **Space:** O(k) – the priority queue never stores more than *k* points; the output array is excluded from the auxiliary space count.

## Solution (Java)

```java
// Brute Force code
// class Solution {
//     public int[][] kClosest(int[][] points, int k) {
//         int ans[][] = new int[k][2];
//         boolean[] used = new boolean[points.length];

//         for (int i = 0; i < k; i++) {
//             int minDistance = Integer.MAX_VALUE;
//             int minIndex= -1;
//             for (int j = 0; j < points.length; j++) {
//                 if (!used[j]) {
//                     int x = points[j][0];
//                     int y = points[j][1];

//                     int currDistance = x * x + y * y; // origion so x1 = x & x2 = 0 x1 - x2 = x

//                     if (currDistance < minDistance) {
//                         minDistance = currDistance;
//                         minIndex = j;
//                     }
//                 }
//             }
//             ans[i] = points[minIndex];
//             used[minIndex] = true;
//         }
//         return ans;
//     }
// }

// Optimized priority Queue 

class Solution {
    public int[][] kClosest(int[][] points, int k) {

        // MAX HEAP KEEPS THE MAX VALUE AT THE TOP
        PriorityQueue<int[]> pq = new PriorityQueue<>(
                (a, b) -> Integer.compare(
                        b[0] * b[0] + b[1] * b[1],
                        a[0] * a[0] + a[1] * a[1]));

        for (int[] point : points) {
            pq.offer(point);

            // removing the top most element as it will be the largest among the group
            if (pq.size() > k)
                pq.poll();
        }

        int answer[][] = new int[k][2];

        for (int i = 0; i < k; i++) {
            answer[i] = pq.poll();
        }
        return answer;
    }
}
```

---

**Runtime** 30 ms (beats 66.7%) · **Memory** 53.2 MB (beats 91.4%)

<sub>Synced by AILeetHub on 2026-09-08.</sub>
