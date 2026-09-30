class Solution {
    public List<String> findAndReplacePattern(String[] words, String pattern) {
                List<String> res = new ArrayList<>();
        for (String word : words) {
            if (matches(word, pattern)) res.add(word);
        }
        return res;
    }

    private boolean matches(String word, String pattern) {
        int[] w = new int[26]; // last position (+1) each word letter was seen
        int[] p = new int[26]; // last position (+1) each pattern letter was seen

        for (int i = 0; i < word.length(); i++) {
            int a = word.charAt(i) - 'a';
            int b = pattern.charAt(i) - 'a';

            if (w[a] != p[b]) return false;

            w[a] = p[b] = i + 1;
        }
        return true;
    }
}