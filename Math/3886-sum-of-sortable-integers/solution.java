class Solution {
    public int sortableIntegers(int[] nums) {
        int n = nums.length;
        int[] target = nums.clone();
        Arrays.sort(target);

        List<Integer> fac = new ArrayList<>();
        for (int x = 1; (long) x * x <= n; x++) {
            if (n % x == 0) {
                fac.add(x);
                if (n / x != x)
                    fac.add(n / x);
            }
        }

        int ans = 0;
        for (int d : fac) {
            boolean canSort = true;
            for (int s = 0; s < n; s += d) {
                if (!check(nums, target, s, d)) {
                    canSort = false;
                    break;
                }
            }
            if (canSort)
                ans += d;
        }
        return ans;
    }

    // Does target[s..s+k) appear inside nums[s..s+k) doubled?
    private boolean check(int[] nums, int[] target, int s, int k) {
        int split = -1; // -1 means "no descent", so the rotation starts at 0
        for (int i = 0; i < k - 1; i++) {
            if (nums[s + i] > nums[s + i + 1]) {
                split = i;
                break;
            }
        }
        int start = split + 1;
        for (int j = 0; j < k; j++) {
            if (nums[s + (start + j) % k] != target[s + j])
                return false;
        }
        return true;
    }
}