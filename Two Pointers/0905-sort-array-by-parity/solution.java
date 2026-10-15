class Solution {
    public int[] sortArrayByParity(int[] nums) {
        int left = 0;
        int right = nums.length - 1;
        int answer[] = new int[nums.length];
        for(int num : nums){
            if(num % 2 == 0){
                answer[left++] = num;
            }
            else
                answer[right--] = num;
        }
        return answer;

    }
}