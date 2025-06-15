import java.util.Random;

public class DoublePivotQuickSort {

    public static void main(String[] args) {
        int[] powers = { 1, 2, 3, 4, 5, 6, 7 }; // You can go up to 10 based on system capacity

        for (int n : powers) {
            int size = (int) Math.pow(10, n);
            int[] arr = generateRandomArray(size, 1000);

            System.out.println("Sorting array of size 10^" + n + "...");
            long start = System.currentTimeMillis();

            doublePivotQuickSort(arr, 0, arr.length - 1);

            long end = System.currentTimeMillis();
            double durationSeconds = (end - start) / 1000.0;

            System.out.printf("Time taken to sort 10^%d elements: %.4f seconds\n\n", n, durationSeconds);
        }
    }

    public static int[] generateRandomArray(int size, int maxVal) {
        Random rand = new Random();
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = rand.nextInt(maxVal + 1); // values between 0 and maxVal
        }
        return arr;
    }

    public static void doublePivotQuickSort(int[] arr, int low, int high) {
        if (low < high) {
            int[] pivots = partition(arr, low, high);
            int pivot1 = pivots[0];
            int pivot2 = pivots[1];

            doublePivotQuickSort(arr, low, pivot1 - 1);
            doublePivotQuickSort(arr, pivot1 + 1, pivot2 - 1);
            doublePivotQuickSort(arr, pivot2 + 1, high);
        }
    }

    private static int[] partition(int[] arr, int low, int high) {
        if (arr[low] > arr[high]) {
            swap(arr, low, high);
        }

        int pivot1 = arr[low];
        int pivot2 = arr[high];

        int i = low + 1;
        int lt = low + 1;
        int gt = high - 1;

        while (i <= gt) {
            if (arr[i] < pivot1) {
                swap(arr, i++, lt++);
            } else if (arr[i] > pivot2) {
                swap(arr, i, gt--);
            } else {
                i++;
            }
        }

        swap(arr, low, --lt);
        swap(arr, high, ++gt);

        return new int[] { lt, gt };
    }

    private static void swap(int[] arr, int i, int j) {
        if (i != j) {
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }
}
