// class Solution {
//     public int buyChoco(int[] prices, int money) {
//         Arrays.sort(prices);
//         return (prices[0] + prices[1] > money) ? money : money - (prices[0] + prices[1]);
//     }
// }

class Solution {
    public int buyChoco(int[] prices, int money) {
        int firstMin = Integer.MAX_VALUE;
        int secondMin = Integer.MAX_VALUE;
        for (int price : prices) {
            if (price < firstMin) {
                secondMin = firstMin;
                firstMin = price;
            } else if (price < secondMin) {
                secondMin = price;
            }
        }

        int cost = firstMin + secondMin;

        return cost > money ? money : money - cost;
    }
}