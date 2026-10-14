class Solution {
    public int minimumEffortPath(int[][] heights) {
        int rows = heights.length;
        int cols = heights[0].length;
        int effort [][] = new int[rows][cols];
        for(int[] row : effort){
            Arrays.fill(row, Integer.MAX_VALUE);
        }

        effort[0][0] = 0;
        PriorityQueue<int []> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        pq.offer(new int[]{0, 0, 0});

        int dirs[][] = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};

        while(!pq.isEmpty()){
            int []cur = pq.poll();
            int e = cur[0], r = cur[1], c = cur[2];

            if(r == rows - 1 && c == cols - 1){
                return e;
            }
            if(e > effort[r][c]) continue;

            for(int dir[] : dirs){
                int nr = r + dir[0];
                int nc = c + dir[1];
                if(nr >= 0 && nr < rows && nc >=0 && nc < cols){
                    int ne = Math.max(e, Math.abs(heights[r][c] - heights[nr][nc]));
                    if(ne < effort[nr][nc]){
                        effort[nr][nc] = ne;
                        pq.offer(new int[] {ne, nr, nc});
                    }
                } 
            }
        }
        return 0;
    }
}