class Solution {
    public List<List<Integer>> mergeSimilarItems(int[][] items1, int[][] items2) {
        int list[] = new int[1001];
        for (int[] item : items1) {
            list[item[0]] += item[1];
        }

        for (int[] item : items2) {
            list[item[0]] += item[1];
        }

        List<List<Integer>> result = new ArrayList<>();
        for (int i = 1; i <= 1000; ++i) {
            if (list[i] > 0)
                result.add(Arrays.asList(i, list[i]));
        }
        return result;

    }
}