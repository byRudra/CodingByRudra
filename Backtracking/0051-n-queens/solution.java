class Solution {
    public List<List<String>> solveNQueens(int n) {
        List<List<String>> result = new ArrayList<>();
        
        // constructing the board
        char[][] board = new char[n][n];
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                board[i][j] = '.';
            }
        }

        backTrack(0, board, result, n);
        return result;
    }

    // BackTrack Function
    private void backTrack(int row, char[][] board, List<List<String>> result, int n){
        // Base Condition
        if(row == n){
            List<String> candidate = new ArrayList<>();
            for(char[] r : board){
                candidate.add(new String(r));
            }
            result.add(candidate);
            return;
        }

        // now we traverse for each column in this row 
        for(int col = 0; col < n; col++){
            // check if we can place a queen in this col
            if(isSafe(row, col, board, n)){
                board[row][col] = 'Q';
                backTrack(row + 1, board, result, n);
                board[row][col] = '.';
            }
        }

    }


    // Check if Safe 
    private boolean isSafe(int row, int col, char[][] board, int n){
        
        // check column
        for(int i = 0; i < row; i++){
            if(board[i][col] == 'Q') return false;
        }
        // check upper diagonals
        // only check upper because the bellow ones arent yet constructed so col - 1 goes to the left diagonal index
        
        // upper left
        for(int i = row - 1, j = col - 1; i >= 0 && j >= 0; i--, j--){
            if(board[i][j] == 'Q') return false; 
        }

        // upper right
        // col + 1 will go to the right column but row - 1 will take it above as the diagonal
        for(int i = row - 1, j = col + 1; i >= 0 && j < n; i--, j++){
            if(board[i][j] == 'Q') return false; 
        }


        return true;
    }
}