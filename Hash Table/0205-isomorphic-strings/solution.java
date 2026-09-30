class Solution {
    public boolean isIsomorphic(String s, String t) {
        if(s.length() != t.length()) return false;

        int[] mapTS  = new int[256];
        int[] mapST  = new int[256];
        Arrays.fill(mapTS, -1);
        Arrays.fill(mapST, -1);

        for(int i = 0; i < s.length(); i++){
            char a = s.charAt(i);
            char b = t.charAt(i);

            if(mapST[a] != -1 && mapST[a] != b) return false;
            if(mapTS[b] != -1 && mapTS[b] != a) return false;

            mapTS[b] = a; 
            mapST[a] = b; 
        }
        return true;
    }
}