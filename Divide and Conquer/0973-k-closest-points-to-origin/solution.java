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