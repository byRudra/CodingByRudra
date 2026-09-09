class Solution {
    public int[] leftRightDifference(int[] nums) {
        int leftSum[] = new int[nums.length];
        leftSum[0] = 0;
        int rightSum[] = new int[nums.length];
        rightSum[nums.length - 1] = 0;
        int start = 1, end = nums.length - 2;
        while (start < nums.length) {
            leftSum[start] = leftSum[start - 1] + nums[start - 1];
            if (end >= 0) {
                rightSum[end] = rightSum[end + 1] + nums[end + 1];
            }
            start++;
            end--;
        }

        for (int i = 0; i < nums.length; i++) {
            leftSum[i] = Math.abs(leftSum[i] - rightSum[i]);
        }
        return leftSum;
    }
}