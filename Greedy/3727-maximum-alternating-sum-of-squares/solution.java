class Solution {
    public long maxAlternatingSum(int[] nums) {
        int n = nums.length;
        long squares[] = new long[n];
        for(int i = 0; i < n; i++){
            squares[i] = (long) nums[i] * nums[i];
        }
        Arrays.sort(squares);

        int howMuchPositive = (n + 1) / 2;
        long score = 0;
        for(int i = 0; i < n; i++){
            if(i >= n - howMuchPositive){
                score += squares[i];
            }else{
                score -= squares[i];
            }
        }
        return score;
    }
}