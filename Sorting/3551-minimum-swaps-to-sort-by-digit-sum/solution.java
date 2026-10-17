import java.util.*;

class Solution {
    public int minSwaps(int[] nums) {
        int n = nums.length;

        // Sort indices by (digit sum, value)
        Integer[] idx = new Integer[n];
        for (int i = 0; i < n; i++) idx[i] = i;

        Arrays.sort(idx, (a, b) -> {
            int da = digitSum(nums[a]), db = digitSum(nums[b]);
            return da != db ? da - db : nums[a] - nums[b];
        });

        // idx[i] = original index of the element that belongs at position i
        boolean[] visited = new boolean[n];
        int swaps = 0;

        for (int i = 0; i < n; i++) {
            if (visited[i] || idx[i] == i) continue;

            int j = i, size = 0;
            while (!visited[j]) {
                visited[j] = true;
                j = idx[j];
                size++;
            }
            swaps += size - 1;
        }
        return swaps;
    }

    private int digitSum(int x) {
        int s = 0;
        while (x > 0) {
            s += x % 10;
            x /= 10;
        }
        return s;
    }
}