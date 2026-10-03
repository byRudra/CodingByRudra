class Solution {
    public String reverseVowels(String s) {
        int start = 0;
        int end = s.length() - 1;
        HashSet<Character> set = new HashSet<>(
                Arrays.asList('a', 'e', 'i', 'o', 'u',
                        'A', 'E', 'I', 'O', 'U'));

        char[] chars = s.toCharArray();

        while(start <= end){
             while (start < end && !set.contains(chars[start])) {
                start++;
            }

            while (start < end && !set.contains(chars[end])) {
                end--;
            }
            char temp = chars[start];
            chars[start] = chars[end];
            chars[end] = temp;
            end--;
            start++;
            
        }
        return new String(chars);
    }
}