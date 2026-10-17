class Solution {
    public int maximumPrimeDifference(int[] nums) {
        int min = -1;
        int max = -1;

        for (int i = 0; i < nums.length; i++) {
            if (isPrime(nums[i])) {
                if (min == -1) {
                    min = i;
                }
                max = i;
            }
        }

        return max - min;
    }

    private boolean isPrime(int num) {
        if (num < 2) return false;

        for (int i = 2; i * i <= num; i++) {
            if (num % i == 0) {
                return false;
            }
        }

        return true;
    }
}