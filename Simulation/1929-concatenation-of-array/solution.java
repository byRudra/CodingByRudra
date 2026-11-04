class Solution {
    public int[] getConcatenation(int[] nums) {
        int r = nums.length;
        int n = 2 * r;
        int answer[] = new int[n];
        for(int i = 0; i < r; i++){
            answer[i] = nums[i];
            answer[i + r] = nums[i];
        }
        return answer;
    }
}