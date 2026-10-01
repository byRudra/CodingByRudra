class RandomizedSet {

    ArrayList<Integer> list;
    HashMap<Integer, Integer> map;
    Random random;

    public RandomizedSet() {
        list = new ArrayList<>();
        map = new HashMap<>();
        random = new Random();
    }
    
    public boolean insert(int val) {

        // Already exists
        if (map.containsKey(val)) {
            return false;
        }

        // Add value
        list.add(val);

        // Store its index
        map.put(val, list.size() - 1);

        return true;
    }
    
    public boolean remove(int val) {

        // Doesn't exist
        if (!map.containsKey(val)) {
            return false;
        }

        int index = map.get(val);

        // Get last element
        int last = list.get(list.size() - 1);

        // Move last element to the position of val
        list.set(index, last);

        // Update last element's index
        map.put(last, index);

        // Remove last position
        list.remove(list.size() - 1);

        // Remove val from map
        map.remove(val);

        return true;
    }
    
    public int getRandom() {
        int index = random.nextInt(list.size());
        return list.get(index);
    }
}

/**
 * Your RandomizedSet object will be instantiated and called as such:
 * RandomizedSet obj = new RandomizedSet();
 * boolean param_1 = obj.insert(val);
 * boolean param_2 = obj.remove(val);
 * int param_3 = obj.getRandom();
 */