class Solution {
    public int[] singleNumber(int[] nums) {
        int result[] = new int[2];
        int index = 0;
        Arrays.sort(nums);
        for (int i = 0; i < nums.length; i++) {
            boolean leftSame = i > 0 && nums[i] == nums[i - 1];
            boolean rightSame = i < nums.length - 1
                    && nums[i] == nums[i + 1];

            if (!leftSame && !rightSame) {
                result[index++] = nums[i];
            }
        }
        return result;
    }
}