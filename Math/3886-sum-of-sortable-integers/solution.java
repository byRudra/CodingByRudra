import java.util.*;

class Solution {
    public int sortableIntegers(int[] nums) {
        int n = nums.length;
        int[] target = nums.clone();
        Arrays.sort(target);

        // collect divisors of n in O(sqrt(n))
        List<Integer> fac = new ArrayList<>();
        for (int x = 1; (long) x * x <= n; x++) {
            if (n % x == 0) {
                fac.add(x);
                if (n / x != x) fac.add(n / x);
            }
        }

        int ans = 0;
        for (int d : fac) {
            boolean canSort = true;
            for (int s = 0; s < n; s += d) {
                if (!isRotation(nums, target, s, d)) {
                    canSort = false;
                    break;
                }
            }
            if (canSort) ans += d;
        }
        return ans;
    }

    // Does target[s..s+k) appear inside nums[s..s+k) doubled?
    private boolean isRotation(int[] nums, int[] target, int s, int k) {
        // prefix function of the pattern target[s..s+k)
        int[] pi = new int[k];
        int j = 0;
        for (int i = 1; i < k; i++) {
            while (j > 0 && target[s + i] != target[s + j]) j = pi[j - 1];
            if (target[s + i] == target[s + j]) j++;
            pi[i] = j;
        }

        // scan the doubled block: text[i] = nums[s + i % k]
        j = 0;
        for (int i = 0; i < 2 * k; i++) {
            int c = nums[s + i % k];
            while (j > 0 && c != target[s + j]) j = pi[j - 1];
            if (c == target[s + j]) j++;
            if (j == k) return true;
        }
        return false;
    }
}