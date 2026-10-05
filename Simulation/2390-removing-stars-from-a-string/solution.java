// class Solution {
//     public String removeStars(String s) {
//         Stack<Character> stack = new Stack<>();

//         for(char ch : s.toCharArray()){
//             if(ch != '*'){
//                 stack.push(ch);
//             }
//             else{
//                 if(!stack.isEmpty()) stack.pop();
//             }
//         }

//         StringBuilder ans = new StringBuilder();

//         for (char ch : stack) {
//             ans.append(ch);
//         }

//         return ans.toString();
//     }
// }

class Solution {
    public String removeStars(String s) {
        StringBuilder sb = new StringBuilder();
        for(char ch : s.toCharArray()){
            if(ch == '*')
                sb.setLength(sb.length() - 1);
            else
                sb.append(ch);
        }
        return sb.toString();

    }
}