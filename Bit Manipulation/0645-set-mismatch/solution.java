class Solution {
    public int[] findErrorNums(int[] nums) {
        int n = nums.length;
        int duplicate = 0;
        int sum = 0;
        boolean[] seen = new boolean[n + 1];

        for (int num : nums) {
            sum += num;

            if (seen[num]) {
                duplicate = num;
            }
            seen[num] = true;
        }

        int expectedSum = n * (n + 1) / 2;
        int missing = expectedSum + duplicate - sum;

        return new int[] { duplicate, missing };
    }
}