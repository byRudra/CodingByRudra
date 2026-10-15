class Solution {
    public List<List<Integer>> mergeSimilarItems(int[][] items1, int[][] items2) {
        TreeMap<Integer, Integer> map = new TreeMap<>();
        for(int[] item : items1){
            map.put(item[0], map.getOrDefault(item[0], 0) + item[1]);
        }
        
        for(int[] item : items2){
            map.put(item[0], map.getOrDefault(item[0], 0) + item[1]);
        }

        List<List<Integer>> result = new ArrayList<>();
        for (Map.Entry<Integer, Integer> e : map.entrySet()) {
            result.add(Arrays.asList(e.getKey(), e.getValue()));
        }
        return result;

    }
}