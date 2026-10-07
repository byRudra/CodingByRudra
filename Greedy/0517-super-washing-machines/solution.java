class Solution {
    public int findMinMoves(int[] machines) {
        int totalClothes = 0;
        int n = machines.length;
        for (int cloth : machines) {
            totalClothes += cloth;
        }

        // A case where totalClothes is not a fully divisible by n means we cannot divide the clothes equally
        if (totalClothes % n != 0)
            return -1;

        int each = totalClothes / n;
        int answer = 0;
        int balance = 0;
        for (int cloth : machines) {
            int diff = cloth - each;
            balance += diff;

            answer = Math.max(answer, Math.max(Math.abs(balance), diff));
            // we do abs(balance) because direction dosent matter and we do max because if diff is +ve and balance was first -ve then we take max diff in account because it increases the moves
        }
        return answer;
    }
}