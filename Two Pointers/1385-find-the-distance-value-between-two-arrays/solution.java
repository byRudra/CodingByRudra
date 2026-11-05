class Solution {
    public int findTheDistanceValue(int[] arr1, int[] arr2, int d) {
        Arrays.sort(arr2);
        int count = 0;
        for (int x : arr1) {
            int i = lowerBound(arr2, x - d);
            if (i == arr2.length || arr2[i] > x + d) {
                count++;
            }
        }
        return count;
    }

    private int lowerBound(int arr[], int target) {
        int low = 0, high = arr.length;
        while (low < high) {
            int mid = low + (high - low) / 2;
            if (arr[mid] < target)
                low = mid + 1;
            else
                high = mid;
        }
        return low;
    }
}