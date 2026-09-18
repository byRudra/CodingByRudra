class Solution {
    public int arrangeCoins(int n) {
                int rows = 0;
         while (n >= rows + 1) {
            rows++;
            n = n - rows;
        }
        return rows;
    }
}