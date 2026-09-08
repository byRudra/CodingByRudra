class Solution {
    public void nextPermutation(int[] nums) {
        int n = nums.length;
        int i = n - 2;
        // 1. Find the pivot
        while (i >= 0 && nums[i] >= nums[i + 1]) {
            i--;
        }

        if(i >= 0){
            int j = n - 1;
            while(j >= 0 && nums[j] <= nums[i])
                j--;
            int temp = nums[i];
            nums[i] = nums[j];
            nums[j] = temp;
        }


        int lo = i + 1;
        int hi = n - 1;

        while (lo < hi) {
            int temp = nums[lo];
            nums[lo] = nums[hi];
            nums[hi] = temp;
            lo++;
            hi--;
        }
    }
}