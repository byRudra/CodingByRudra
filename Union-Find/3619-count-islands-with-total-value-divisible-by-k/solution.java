// class Solution {
//     private int cols, rows;
//     public int countIslands(int[][] grid, int k) {
//         rows = grid.length;
//         cols = grid[0].length;
//         int count = 0;
//         for(int i = 0; i < rows; i++){
//             for(int j = 0; j < cols; j++){
//                 if(grid[i][j] > 0){
//                     long sum = dfs(grid, i, j);
//                     if(sum % k == 0) count++;
//                 }
//             }
//         }
//         return count;
//     }
//     private long dfs(int[][] grid, int r, int c){
//         if(r < 0 || c < 0 || r >= rows || c >= cols || grid[r][c] <= 0) return 0;
//         long value = grid[r][c];
//         grid[r][c] = 0;
//         return value + dfs(grid, r + 1, c) + dfs(grid, r - 1, c) + dfs(grid, r, c + 1) + dfs(grid, r, c - 1); 
//     }
// }

// Iterative DFA
class Solution {
    private int cols, rows;
    public int countIslands(int[][] grid, int k) {
        rows = grid.length;
        cols = grid[0].length;
        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, 1, -1};
        int count = 0;
        for(int r = 0; r < rows; r++){
            for(int c = 0; c < cols; c++){
                if (grid[r][c] > 0) {
                    long sum = 0;
                    java.util.ArrayDeque<int[]> stack = new java.util.ArrayDeque<>();
                    stack.push(new int[]{r, c});
                    sum += grid[r][c];
                    grid[r][c] = 0;

                    while (!stack.isEmpty()) {
                        int[] cur = stack.pop();
                        for (int d = 0; d < 4; d++) {
                            int nr = cur[0] + dr[d], nc = cur[1] + dc[d];
                            if (nr >= 0 && nc >= 0 && nr < rows && nc < cols && grid[nr][nc] > 0) {
                                sum += grid[nr][nc];
                                grid[nr][nc] = 0;
                                stack.push(new int[]{nr, nc});
                            }
                        }
                    }
                    if (sum % k == 0) count++;
                }
            }
        }
        return count;
    }
}