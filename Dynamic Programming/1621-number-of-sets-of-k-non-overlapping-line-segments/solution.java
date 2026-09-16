class Solution {
    static final long MOD = 1000000007;
    // Combination Formula NCr
    // numerator = N!

    // Denominator = R! * (N - R)!
    // N = n + k - 1
    // R = 2k

    // Numerator = (n + k - 1)! 
    // Denominator = 2k! * (n - k - 1)! 

    public int numberOfSets(int n, int k) {
        long numerator = factorial(n + k - 1);
        long denominator1 = factorial(2 * k);
        long denominator2 = factorial(n - k - 1);
        long denominator = denominator1 * denominator2 % MOD;

        long result = numerator * modInverse(denominator) % MOD;
        return (int) result ;
    }

    long factorial(int n) {
        long fact = 1;
        for (int i = 2; i <= n; i++) {
            fact *= i;
            fact %= MOD;
        }
        return fact;
    }

    long modInverse(long n) {
        return pow(n, MOD - 2);
    }

    long pow(long n, long power){
        long result = 1;
        while (power > 0) {
            if (power % 2 == 1)
                result = result * n % MOD;

            n = n * n % MOD;
            power /= 2;
        }
        return result;
    }
}