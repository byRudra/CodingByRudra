class Solution {
    public boolean hasGroupsSizeX(int[] deck) {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int num : deck) {
            map.put(num, map.getOrDefault(num, 0) + 1);
        }
        
        int g = 0;
        for (int count : map.values()) {
            g = gcd(g, count);
        }
        
        return g >= 2;
    }
    
    private int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }
}