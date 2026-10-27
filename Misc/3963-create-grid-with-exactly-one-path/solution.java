class Solution {
    public String[] createGrid(int m, int n) {
        String grid[] = new String[m];
        for(int i = 0; i < m; i++){
            if(i == 0) grid[i] = ".".repeat(n);
            else grid[i] = "#".repeat(n - 1) + ".";
        }
        return grid;
    }
}