class Solution {
    public String[] uncommonFromSentences(String s1, String s2) {
        Map<String, Integer> map = new HashMap<>();

        for (String word : (s1 + " " + s2).split(" ")) {
            map.put(word, map.getOrDefault(word, 0) + 1);
        }

        List<String> res = new ArrayList<>();

        for (String word : map.keySet()) {
            if (map.get(word) == 1) {
                res.add(word);
            }
        }

        return res.toArray(new String[0]);
    }
}