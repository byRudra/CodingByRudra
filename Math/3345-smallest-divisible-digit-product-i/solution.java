class Solution {
    public int smallestNumber(int n, int t) {
        while(product(n) % t != 0){
            n++;
        }
        return n;
    }
    int product(int num){
        int res = 1;
        while(num > 0){
            res *= num % 10;
            num /= 10;
        }
        return res;
    }
}