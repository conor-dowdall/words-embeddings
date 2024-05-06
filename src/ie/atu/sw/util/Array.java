package ie.atu.sw.util;

public class Array {
    public static int[] getMaxValuesIndexes(double[] array, int howMany) throws Exception {
        if (howMany > array.length)
            throw new Exception(
                    "Can't find largest " + howMany + " elements of array with length of " + array.length);

        int[] maxValuesIndexes = new int[howMany];

        double[] arrayCopy = new double[array.length];
        System.arraycopy(array, 0, arrayCopy, 0, array.length);

        maxValuesIndexes[0] = getMaxValueIndex(arrayCopy);
        for (int i = 1; i < maxValuesIndexes.length; i++)
            maxValuesIndexes[i] = getMaxValueIndex(arrayCopy);

        return maxValuesIndexes;
    }

    public static int getMaxValueIndex(double[] array) {
        int maxValueIndex = 0;

        for (int i = 1; i < array.length; i++)
            if (array[i] > array[maxValueIndex])
                maxValueIndex = i;

        array[maxValueIndex] = -Double.MAX_VALUE;

        return maxValueIndex;
    }

    public static int[] getMinValuesIndexes(double[] array, int howMany) throws Exception {
        if (howMany > array.length)
            throw new Exception(
                    "Can't find smallest " + howMany + " elements of array with length of " + array.length);

        int[] minValuesIndexes = new int[howMany];

        double[] arrayCopy = new double[array.length];
        System.arraycopy(array, 0, arrayCopy, 0, array.length);

        minValuesIndexes[0] = getMinValueIndex(arrayCopy);
        for (int i = 1; i < minValuesIndexes.length; i++)
            minValuesIndexes[i] = getMinValueIndex(arrayCopy);

        return minValuesIndexes;
    }

    public static int getMinValueIndex(double[] array) {
        int minValueIndex = 0;

        for (int i = 1; i < array.length; i++)
            if (array[i] < array[minValueIndex])
                minValueIndex = i;

        array[minValueIndex] = Double.MAX_VALUE;

        return minValueIndex;
    }
}
