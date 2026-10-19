class Solution {
    public int[] closestDivisors(int num) {
        int[] ans = new int[2];
        int minDiff = Integer.MAX_VALUE;

        for (int target : new int[]{num + 1, num + 2}) {
            for (int i = (int) Math.sqrt(target); i >= 1; i--) {
                if (target % i == 0) {
                    int a = i;
                    int b = target / i;
                    int diff = b - a;

                    if (diff < minDiff) {
                        minDiff = diff;
                        ans[0] = a;
                        ans[1] = b;
                    }
                    break;
                }
            }
        }

        return ans;
    }
}