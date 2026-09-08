class Solution {
    public int[][] kClosest(int[][] points, int k) {
        int ans[][] = new int[k][2];
        boolean[] used = new boolean[points.length];

        for (int i = 0; i < k; i++) {
            int minDistance = Integer.MAX_VALUE;
            int minIndex= -1;
            for (int j = 0; j < points.length; j++) {
                if (!used[j]) {
                    int x = points[j][0];
                    int y = points[j][1];

                    int currDistance = x * x + y * y; // origion so x1 = x & x2 = 0 x1 - x2 = x

                    if (currDistance < minDistance) {
                        minDistance = currDistance;
                        minIndex = j;
                    }
                }
            }
            ans[i] = points[minIndex];
            used[minIndex] = true;
        }
        return ans;
    }
}