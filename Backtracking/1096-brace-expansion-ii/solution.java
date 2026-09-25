class Solution {
    private String s;
    private int i;

    public List<String> braceExpansionII(String expression) {
        s = expression;
        i = 0;

        Set<String> result = parseExpression();

        List<String> ans = new ArrayList<>(result);
        Collections.sort(ans);

        return ans;
    }

    // expression = term (',' term)*
    private Set<String> parseExpression() {
        Set<String> result = parseTerm();

        while (i < s.length() && s.charAt(i) == ',') {
            i++; // skip comma
            result.addAll(parseTerm());
        }

        return result;
    }

    // term = factor factor factor...
    // Concatenation
    private Set<String> parseTerm() {
        Set<String> result = new HashSet<>();
        result.add("");

        while (i < s.length()
                && s.charAt(i) != '}'
                && s.charAt(i) != ',') {

            Set<String> factor = parseFactor();

            result = multiply(result, factor);
        }

        return result;
    }

    // factor = letter OR '{' expression '}'
    private Set<String> parseFactor() {
        if (s.charAt(i) == '{') {
            i++; // skip '{'

            Set<String> result = parseExpression();

            i++; // skip '}'

            return result;
        }

        // single character
        Set<String> result = new HashSet<>();
        result.add(String.valueOf(s.charAt(i)));

        i++;

        return result;
    }

    // Cartesian product = concatenation
    private Set<String> multiply(Set<String> a, Set<String> b) {
        Set<String> result = new HashSet<>();

        for (String x : a) {
            for (String y : b) {
                result.add(x + y);
            }
        }

        return result;
    }
}