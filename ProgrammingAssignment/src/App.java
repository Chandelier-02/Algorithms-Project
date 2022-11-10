import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class App {
    public static void main(String[] args) throws Exception {
        int[] inputFromFile = readFromFile();
        int[] outputs = {
            Algorithm1(inputFromFile),
            Algorithm2(inputFromFile),
            MaxSum(inputFromFile, 0, inputFromFile.length - 1),
            Algorithm4(inputFromFile)
        };

        for (int i = 0; i < outputs.length; i++) {
            System.out.print("algorithm" + "-" + (i + 1) + ":" + outputs[i] + " ");
        }
        System.out.println("");

        for (int[] i : generateMatrix(generateArrays())) {
            System.out.println(Arrays.toString(i));
        }
    }

    private static int[] readFromFile() {
        int[] intFromFile = new int[10];
        try (BufferedReader in = new BufferedReader(new FileReader("lib/phw_input.txt"))) {
            String str = in.readLine();
            String[] strValues = str.split(",");
            
            for (int i = 0; i < strValues.length; i++) {
                intFromFile[i] = Integer.parseInt(strValues[i]);
            }

            return intFromFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new int[]{};
    }

    private static int Algorithm1(int[] X) {
        int maxSoFar = 0;
        for (int L = 0; L < X.length; L++) {
            for (int U = L; U < X.length; U++) {
                int sum = 0;
                for (int I = L; I < U; I++) {
                    sum += X[I];
                    /* sum now contains the sum of X[L..U] */
                }
                maxSoFar = Math.max(maxSoFar, sum);
            }
        }
        return maxSoFar;
    }

    private static int Algorithm2(int[] X) {
        int maxSoFar = 0;
        for (int L = 0; L < X.length; L++) {
            for (int U = L; U < X.length; U++) {
                int sum = 0;
                sum += X[U];
                /* sum now contains the sum of X[L..U] */
                maxSoFar = Math.max(maxSoFar, sum);
            }
        }
        return maxSoFar;
    }

    private static int MaxSum(int[] X, int L, int U) {
        if (L > U) return 0; /* zero- element vector */
        if (L == U) return Math.max(0, X[L]); /* one-element vector */
        int M = (L + U) / 2; /* A is X[L..M], B is X[M+1..U] */

        /* Find max crossing to left */
        int sum = 0;
        int maxToLeft = 0;
        for (int I = M; I >= L; I--) {
            sum += X[I];
            maxToLeft = Math.max(maxToLeft, sum);
        }

        /* Find max crossing to right */
        sum = 0;
        int maxToRight = 0;
        for (int I = M + 1; I < U; I++) {
            sum += X[I];
            maxToLeft = Math.max(maxToRight, sum);
        }

        int maxCrossing = maxToLeft + maxToRight;

        int maxInA = MaxSum(X, L, M);
        int mazInB = MaxSum(X, M + 1, U);

        return Math.max(maxCrossing, Math.max(maxInA, mazInB));
    }

    private static int Algorithm4(int[] X) {
        int maxSoFar = 0;
        int maxEndingHere = 0;
        for (int I = 0; I < X.length; I++) {
            maxEndingHere = Math.max(0, maxEndingHere + X[I]);
            maxSoFar = Math.max(maxSoFar, maxEndingHere);
        }
        return maxSoFar;
    }

    private static int[][] generateArrays() {
        int numberOfValues = 10;
        int[][] randomValuesArray = new int[19][];
        int[] values = new int[]{};
        Random rn = new Random();
        
        for (int i = 0; i < 19; i++) {
            values = new int[numberOfValues];
            for (int j = 0; j < numberOfValues; j++) {
                values[j] = rn.nextInt(100 + 100) - 100;
            }
            randomValuesArray[i] = values;
            numberOfValues += 5;
        }

        return randomValuesArray;
    }

    private static int[][] generateMatrix(int[][] randomValuesArrayIn) {
        int[][] matrix = new int[19][8];
        int N = 1000;
        
        for (int column = 0; column < 4; column++) {
            for (int row = 0; row < 19; row++) {
                long t1 = System.nanoTime();
                for (int j = 0; j < N; j++) {
                    Algorithm1(randomValuesArrayIn[row]);
                }
                t1 = System.nanoTime() - t1;
                matrix[row][column] = (int) t1 / 100; 
            }
        }
        return matrix;
    }
}
