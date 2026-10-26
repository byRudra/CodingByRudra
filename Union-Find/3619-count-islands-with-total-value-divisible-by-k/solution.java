class Solution {
    private int cols, rows;
    public int countIslands(int[][] grid, int k) {
        rows = grid.length;
        cols = grid[0].length;
        int count = 0;
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                if(grid[i][j] > 0){
                    long sum = dfs(grid, i, j);
                    if(sum % k == 0) count++;
                }
            }
        }
        return count;
    }
    private long dfs(int[][] grid, int r, int c){
        if(r < 0 || c < 0 || r >= rows || c >= cols || grid[r][c] <= 0) return 0;
        long value = grid[r][c];
        grid[r][c] = 0;
        return value + dfs(grid, r + 1, c) + dfs(grid, r - 1, c) + dfs(grid, r, c + 1) + dfs(grid, r, c - 1); 
    }
}