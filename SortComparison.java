import java.util.*;
import java.io.*;

public class SortComparison {

    static List<String[]> csvRows = new ArrayList<>(); // rows for CSV

    public static void main(String[] args) {
        csvRows.add(new String[] { "Size", "Algorithm", "Time (seconds)" });

        for (int n = 1; n <= 5; n++) {
            int size = (int) Math.pow(10, n);
            System.out.println("\n▶ Sorting array of size 10^" + n + " (" + size + " elements)");

            int[] base = generateRandomArray(size, 1000);

            timeSort("Bubble Sort     ", base.clone(), SortComparison::bubbleSort, n);
            timeSort("Selection Sort  ", base.clone(), SortComparison::selectionSort, n);
            timeSort("Merge Sort      ", base.clone(), SortComparison::mergeSort, n);
            timeSort("QuickSort (Single)", base.clone(), arr -> quickSort(arr, 0, arr.length - 1), n);
            timeSort("QuickSort (DP)  ", base.clone(), arr -> doublePivotQuickSort(arr, 0, arr.length - 1), n);
            timeSort("Arrays.sort()   ", base.clone(), Arrays::sort, n);
        }

        writeCsv("sort_timings.csv");
        System.out.println("\n📁 Results written to sort_timings.csv");
    }

    public static void timeSort(String name, int[] arr, SortAlgorithm algorithm, int n) {
        long start = System.currentTimeMillis();
        algorithm.sort(arr);
        long end = System.currentTimeMillis();
        double duration = (end - start) / 1000.0;
        System.out.printf("⏱️  %-18s: %.4f seconds%n", name, duration);
        csvRows.add(new String[] { "10^" + n, name.trim(), String.format("%.4f", duration) });
    }

    // Sorting algorithms
    public static void bubbleSort(int[] arr) {
        int n = arr.length;
        boolean swapped;
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    swap(arr, j, j + 1);
                    swapped = true;
                }
            }
            if (!swapped)
                break;
        }
    }

    public static void selectionSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[minIdx])
                    minIdx = j;
            }
            swap(arr, i, minIdx);
        }
    }

    public static void mergeSort(int[] arr) {
        if (arr.length < 2)
            return;
        int mid = arr.length / 2;
        int[] left = Arrays.copyOfRange(arr, 0, mid);
        int[] right = Arrays.copyOfRange(arr, mid, arr.length);
        mergeSort(left);
        mergeSort(right);
        merge(arr, left, right);
    }

    private static void merge(int[] arr, int[] left, int[] right) {
        int i = 0, j = 0, k = 0;
        while (i < left.length && j < right.length)
            arr[k++] = left[i] <= right[j] ? left[i++] : right[j++];
        while (i < left.length)
            arr[k++] = left[i++];
        while (j < right.length)
            arr[k++] = right[j++];
    }

    public static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pi = singlePivotPartition(arr, low, high);
            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }

    private static int singlePivotPartition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (arr[j] < pivot) {
                swap(arr, ++i, j);
            }
        }
        swap(arr, i + 1, high);
        return i + 1;
    }

    public static void doublePivotQuickSort(int[] arr, int low, int high) {
        if (low < high) {
            int[] pivotIndexes = partitionWithTwoPivots(arr, low, high);

            int leftPivotIndex = pivotIndexes[0];
            int rightPivotIndex = pivotIndexes[1];

            doublePivotQuickSort(arr, low, leftPivotIndex - 1); // elements < left pivot
            doublePivotQuickSort(arr, leftPivotIndex + 1, rightPivotIndex - 1); // between pivots
            doublePivotQuickSort(arr, rightPivotIndex + 1, high); // elements > right pivot
        }
    }

    private static int[] partitionWithTwoPivots(int[] arr, int low, int high) {
        if (arr[low] > arr[high]) {
            swap(arr, low, high);
        }

        int leftPivot = arr[low];
        int rightPivot = arr[high];

        int lessThanLeft = low + 1;
        int greaterThanRight = high - 1;
        int i = low + 1;

        while (i <= greaterThanRight) {
            if (arr[i] < leftPivot) {
                swap(arr, i++, lessThanLeft++);
            } else if (arr[i] > rightPivot) {
                swap(arr, i, greaterThanRight--);
            } else {
                i++;
            }
        }

        swap(arr, low, --lessThanLeft);
        swap(arr, high, ++greaterThanRight);

        return new int[] { lessThanLeft, greaterThanRight };
    }

    private static void swap(int[] arr, int i, int j) {
        if (i != j) {
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }

    public static int[] generateRandomArray(int size, int maxVal) {
        Random rand = new Random();
        int[] arr = new int[size];
        for (int i = 0; i < size; i++)
            arr[i] = rand.nextInt(maxVal + 1);
        return arr;
    }

    public static void writeCsv(String filename) {
        try (PrintWriter writer = new PrintWriter(new File(filename))) {
            for (String[] row : csvRows) {
                writer.println(String.join(",", row));
            }
        } catch (Exception e) {
            System.out.println("Failed to write CSV: " + e.getMessage());
        }
    }

    @FunctionalInterface
    interface SortAlgorithm {
        void sort(int[] arr);
    }
}
