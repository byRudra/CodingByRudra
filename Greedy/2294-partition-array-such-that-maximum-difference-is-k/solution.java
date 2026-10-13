class Solution {
    public int partitionArray(int[] nums, int k) {
        Arrays.sort(nums);
        int group = 0;
        int start = nums[0];
        for(int x : nums){
            if(k < x - start){
                group++;
                start = x;
            }
        }
        return ++group;

    }
}