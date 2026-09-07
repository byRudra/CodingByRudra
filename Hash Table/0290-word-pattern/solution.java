// class Solution {
//     public boolean wordPattern(String pattern, String s) {
//         HashSet<String> seen = new HashSet<>();

//         HashMap<Character, String> map = new HashMap<>();
//         String[] words = s.split(" ");
//         if(words.length != pattern.length())
//             return false; 

//         for (int i = 0; i < pattern.length(); i++) {
//             char ch = pattern.charAt(i);
//             String word = words[i];

//             if (map.containsKey(ch)) {
//                 if (map.get(ch).equals(word))
//                     continue;
//                 else
//                     return false;
//             }
//             if (seen.contains(word))
//                 return false;

//             seen.add(word);
//             map.put(ch, word);
//         }
//         return true;
//     }
// }

// Simplified Approach
class Solution {
    public boolean wordPattern(String pattern, String s) {

        HashMap<Character, String> map = new HashMap<>();
        String[] words = s.split(" ");

        if (words.length != pattern.length())
            return false;

        for (int i = 0; i < pattern.length(); i++) {
            char ch = pattern.charAt(i);
            String word = words[i];

            if (map.containsKey(ch)) {
                if (!words[i].equals(map.get(ch)))
                    return false;
            }else if(map.containsValue(word))
                return false;
            else
                map.put(ch, word);
        }
        return true;
    }
}