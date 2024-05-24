package ie.atu.sw.util;

/**
 * utility class with static methods for finding the indexes of the
 * minimum-or-maximum values in arrays of doubles
 */
public class Array {
    /**
     * find the indexes of 'howMany' maximum values of an array
     * 
     * @param array   - the array to search
     * @param howMany - how many results to search for
     * @return an array of the searched for indexes
     * @throws Exception if the array length is shorter than 'howMany'
     */
    public static int[] getMaxValuesIndexes(double[] array, int howMany) throws Exception {
        if (howMany > array.length)
            throw new Exception(
                    "Can't find largest " + howMany + " elements of array with length of " + array.length);

        int[] maxValuesIndexes = new int[howMany];

        double[] arrayCopy = new double[array.length];
        System.arraycopy(array, 0, arrayCopy, 0, array.length);

        for (int i = 0; i < maxValuesIndexes.length; i++)
            maxValuesIndexes[i] = getMaxValueIndex(arrayCopy);

        return maxValuesIndexes;
    }

    /**
     * find the index of the maximum value of an array, and set the value at that
     * index to the lowest value possible, effectively removing it from future
     * searches
     * 
     * @param array - the array to search
     * @return an array of the searched for indexes
     */
    public static int getMaxValueIndex(double[] array) {
        int maxValueIndex = 0;

        for (int i = 1; i < array.length; i++)
            if (array[i] > array[maxValueIndex])
                maxValueIndex = i;

        array[maxValueIndex] = -Double.MAX_VALUE;

        return maxValueIndex;
    }

    /**
     * find the indexes of 'howMany' minimum values of an array
     * 
     * @param array   - the array to search
     * @param howMany - how many results to search for
     * @return an array of the searched for indexes
     * @throws Exception if the array length is shorter than 'howMany'
     */
    public static int[] getMinValuesIndexes(double[] array, int howMany) throws Exception {
        if (howMany > array.length)
            throw new Exception(
                    "Can't find smallest " + howMany + " elements of array with length of " + array.length);

        int[] minValuesIndexes = new int[howMany];

        double[] arrayCopy = new double[array.length];
        System.arraycopy(array, 0, arrayCopy, 0, array.length);

        for (int i = 0; i < minValuesIndexes.length; i++)
            minValuesIndexes[i] = getMinValueIndex(arrayCopy);

        return minValuesIndexes;
    }

    /**
     * find the index of the minimum value of an array, and set the value at that
     * index to the highest value possible, effectively removing it from future
     * searches
     * 
     * @param array - the array to search
     * @return an array of the searched for indexes
     */
    public static int getMinValueIndex(double[] array) {
        int minValueIndex = 0;

        for (int i = 1; i < array.length; i++)
            if (array[i] < array[minValueIndex])
                minValueIndex = i;

        array[minValueIndex] = Double.MAX_VALUE;

        return minValueIndex;
    }
}
