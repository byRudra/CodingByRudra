class Solution {
    private int rows, cols;

    public int closedIsland(int[][] grid) {
        rows = grid.length;
        cols = grid[0].length;

        // checking if any island connects with the boundary if it does making the whole island 1 so we know it isnt the one we need to count 
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if ((r == 0 || c == 0 || r == rows - 1 || c == cols - 1) && grid[r][c] == 0) {
                    dfs(grid, r, c);
                }
            }
        }

        // now checking for the left overs
        // we start with 1 because 0 is the boundary
        int count = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == 0) {
                    count++;
                    // dfs from that point will make every 0 to a 1 and hence this will lead to conversion of the whole island that was 0 to 1
                    dfs(grid, r, c);
                }
            }
        }
        return count;
    }
    public void dfs(int[][] grid, int r, int c){
        if(r < 0 || c < 0 || r >= rows || c >= cols || grid[r][c] != 0) return;
        grid[r][c] = 1;
        dfs(grid, r - 1, c);
        dfs(grid, r + 1, c);
        dfs(grid, r, c + 1);
        dfs(grid, r, c - 1);
    }
}