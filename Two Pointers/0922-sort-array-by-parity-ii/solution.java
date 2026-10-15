class Solution {
    public int[] sortArrayByParityII(int[] nums) {
        int even = 0;
        int odd = 1;
        int answer[] = new int[nums.length];
        for (int num : nums) {
            if (num % 2 == 0) {
                answer[even] = num;
                even += 2;
            } else {
                answer[odd] = num;
                odd += 2;
            }
        }

        return answer;

    }
}