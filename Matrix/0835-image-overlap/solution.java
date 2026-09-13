class Solution {
    public int largestOverlap(int[][] img1, int[][] img2) {
        List<int[]> onesA = new ArrayList<>();
        List<int[]> onesB = new ArrayList<>();

        for (int i = 0; i < img1.length; i++) {
            for (int j = 0; j < img1.length; j++) {
                if (img1[i][j] == 1) {
                    onesA.add(new int[] { i, j });
                }
                if (img2[i][j] == 1) {
                    onesB.add(new int[] { i, j });
                }
            }
        }

        Map<Integer, Integer> shiftCount = new HashMap<>();
        int best = 0;

        for (int[] a : onesA) {
            for (int[] b : onesB) {
                int dr = a[0] - b[0];
                int dc = a[1] - b[1];
                int key = dr * 200 + dc; // encode (dr, dc) into one int; 200 safely exceeds 2*n for n <= 100
                int cnt = shiftCount.merge(key, 1, Integer::sum);
                best = Math.max(best, cnt);
            }
        }

        return best;
    }
}