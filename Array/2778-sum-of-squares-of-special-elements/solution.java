class Solution {
    public int sumOfSquares(int[] nums) {
        int answer = nums[0] * nums[0];
        for (int i = 1; i < nums.length; i++) {
            if (nums.length % (i + 1) == 0)
                answer += nums[i] * nums[i];
        }
        return answer;
    }
}