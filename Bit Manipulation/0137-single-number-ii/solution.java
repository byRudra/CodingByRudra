class Solution {
    public int singleNumber(int[] nums) {
        Arrays.sort(nums);
        int index = 0;
        while(index < nums.length - 1){
            if(nums[index] == nums[index + 1])
                index += 3;
            else
                return nums[index];
        }
        return nums[nums.length - 1];
    }
}