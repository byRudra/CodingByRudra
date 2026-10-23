class Solution {
    public int sortPermutation(int[] nums) {
        int operations = -1;
        for(int i = 0; i < nums.length; i++){
            if(nums[i] != i){
                operations &= nums[i];
            }
        }
        return operations == -1 ? 0 : operations;
    }
}