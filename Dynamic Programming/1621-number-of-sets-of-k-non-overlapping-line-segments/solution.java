class Solution {
    static final long MOD = 1000000007;

    public int numberOfSets(int n, int k) {

        long numerator = factorial(n + k - 1);

        long denominator1 = factorial(2 * k);
        long denominator2 = factorial(n - k - 1);

        long denominator = (denominator1 * denominator2) % MOD;

        long answer = numerator * modInverse(denominator) % MOD;

        return (int) answer;
    }

    long factorial(int n) {
        long ans = 1;

        for (int i = 2; i <= n; i++) {
            ans = (ans * i) % MOD;
        }

        return ans;
    }

    long modInverse(long n) {
        return power(n, MOD - 2);
    }

    long power(long a, long b) {
        long ans = 1;

        while (b > 0) {
            if (b % 2 == 1)
                ans = ans * a % MOD;

            a = a * a % MOD;
            b /= 2;
        }

        return ans;
    }
}