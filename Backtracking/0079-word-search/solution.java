class Solution {
    public boolean exist(char[][] board, String word) {
        char[] words = word.toCharArray();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (words[0] == board[i][j] && exists(board, words, 0, i, j))
                    return true;
            }
        }
        return false;
    }

    private boolean exists(char[][] board, char[] word, int idx, int i, int j) {
        if (i >= board.length || j >= board[0].length || j < 0 || i < 0 || word[idx] != board[i][j])
            return false;
        if (idx == word.length - 1)
            return true;
        char temp = board[i][j];
        board[i][j] = '#';

        boolean result = exists(board, word, idx + 1, i + 1, j) ||
                exists(board, word, idx + 1, i - 1, j) ||
                exists(board, word, idx + 1, i, j + 1) ||
                exists(board, word, idx + 1, i, j - 1);
        board[i][j] = temp;

        return result;
    }
}