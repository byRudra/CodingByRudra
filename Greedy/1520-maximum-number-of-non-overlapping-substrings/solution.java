class Solution {
    public List<String> maxNumOfSubstrings(String s) {
        int[] first = new int[26];
        int[] last = new int[26];

        Arrays.fill(first, -1);
        Arrays.fill(last, -1);

        // Find first and last occurrence
        for (int i = 0; i < s.length(); i++) {
            int idx = s.charAt(i) - 'a';

            if (first[idx] == -1) {
                first[idx] = i;
            }

            last[idx] = i;
        }

        List<String> result = new ArrayList<>();

        // Try every character as a starting point
        for (int c = 0; c < 26; c++) {

            if (first[c] == -1)
                continue;

            int start = first[c];
            int end = last[c];

            boolean valid = true;

            // Expand the interval
            for (int i = start; i <= end; i++) {

                int idx = s.charAt(i) - 'a';

                // This character appeared before our start
                if (first[idx] < start) {
                    valid = false;
                    break;
                }

                // Need to include its entire range
                end = Math.max(end, last[idx]);
            }

            if (valid) {
                result.add(s.substring(start, end + 1));
            }
        }

        // Sort by ending position
        result.sort((a, b) -> {
            int endA = s.indexOf(a) + a.length();
            int endB = s.indexOf(b) + b.length();
            return Integer.compare(endA, endB);
        });

        // Greedily choose non-overlapping intervals
        List<String> answer = new ArrayList<>();
        int prevEnd = -1;

        for (String str : result) {
            int start = s.indexOf(str);
            int end = start + str.length() - 1;

            if (start > prevEnd) {
                answer.add(str);
                prevEnd = end;
            }
        }

        return answer;
    }

}