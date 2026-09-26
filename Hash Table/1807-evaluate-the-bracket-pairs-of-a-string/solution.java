class Solution {
    public String evaluate(String s, List<List<String>> knowledge) {
        HashMap<String, String> map = new HashMap<>();

        for(List<String> curr : knowledge){
            map.put(curr.get(0), curr.get(1));
        }
        StringBuilder ans = new StringBuilder();
        int i = 0;
        while(i < s.length()){
            if(s.charAt(i) == '('){
                // finding the closing bracket )
                int j = i+1;
                while(s.charAt(j) != ')'){
                    j++;
                }
                String key = s.substring(i+1, j);
                if(map.containsKey(key))
                    ans.append(map.get(key));
                else 
                    ans.append('?');

                i = j + 1; 
            } else {
                ans.append(s.charAt(i));
                i++;
            }
        }

        return ans.toString();
    }
}