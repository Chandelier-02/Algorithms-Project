import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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

        // for (int[] i : generateMatrix(generateArrays())) {
        //     System.out.println(Arrays.toString(i));
        // }
        printToFile(generateMatrix(generateArrays()));
    }

    private static int[] readFromFile() {
        int[] intFromFile = new int[10];
        try (BufferedReader in = new BufferedReader(new FileReader("lib/phw_input.txt"))) {
            String str = in.readLine();
            String[] strValues = str.split(",");
            
            for (int i = 0; i < strValues.length; i++) {
                intFromFile[i] = Integer.parseInt(strValues[i]);
            }
            in.close();
            return intFromFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new int[]{};
    }

    private static void printToFile(int[][] matrixIn) {
        try (BufferedWriter out = new BufferedWriter(new FileWriter("lib/ChandlerDykes_phw_output.txt"))) {
            String str = "Algorithm-1,Algorithm-2,Algorithm-3,Algorithm-4,T1(n),T2(n),T3(n),T4(n)";
            out.write(str);

            for (int row = 0; row < 19; row++) {
                out.append("\n");
                for (int column = 0; column < 8; column++) {
                    str = Integer.toString(matrixIn[row][column]);
                    out.append(str);

                    if (column != 7) {
                        out.append(",");
                    }
                }
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int max(int v1, int v2) {
        return Math.max(v1, v2);
    }
    
    private static int max(int v1, int v2, int v3) {
        return Math.max(v1, (Math.max(v2, v3)));
    }

    private static int Algorithm1(int[] X) {
        int P = 0;
        int Q = X.length - 1;
        int maxSoFar = 0;
        for (int L = P; L <= Q; L++) {
            for (int U = L; U <= Q; U++) {
                int sum = 0;
                for (int I = L; I <= U; I++) {
                    sum += X[I];
                    /* sum now contains the sum of X[L..U] */
                }
                maxSoFar = max(maxSoFar, sum);
            }
        }
        return maxSoFar;
    }

    private static int Algorithm2(int[] X) {
        int P = 0;
        int Q = X.length - 1;
        int maxSoFar = 0;
        for (int L = P; L <= Q; L++) {
            int sum = 0;
            for (int U = L; U <= Q; U++) {
                sum += X[U];
                /* sum now contains the sum of X[L..U] */
                maxSoFar = max(maxSoFar, sum);
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
            maxToLeft = max(maxToLeft, sum);
        }

        /* Find max crossing to right */
        sum = 0;
        int maxToRight = 0;
        for (int I = M + 1; I <= U; I++) {
            sum += X[I];
            maxToRight = max(maxToRight, sum);
        }

        int maxCrossing = maxToLeft + maxToRight;

        int maxInA = MaxSum(X, L, M);
        int mazInB = MaxSum(X, M + 1, U);

        return max(maxCrossing, maxInA, mazInB);
    }

    private static int Algorithm4(int[] X) {
        int P = 0;
        int Q = X.length - 1;
        int maxSoFar = 0;
        int maxEndingHere = 0;
        for (int I = P; I <= Q; I++) {
            maxEndingHere = max(0, maxEndingHere + X[I]);
            maxSoFar = max(maxSoFar, maxEndingHere);
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
        
        for (int row = 0; row < 19; row++) {
            int t1 = 0;
            int t2 = 0;
            int t3 = 0;
            int t4 = 0;
            for (int j = 0; j < N; j++) {
                long t1_start = System.nanoTime();
                Algorithm1(randomValuesArrayIn[row]);
                long t1_end = System.nanoTime();
                t1 += t1_end - t1_start;

                long t2_start = System.nanoTime();
                Algorithm2(randomValuesArrayIn[row]);
                long t2_end = System.nanoTime();
                t2 += t2_end - t2_start;
                
                long t3_start = System.nanoTime();
                MaxSum(randomValuesArrayIn[row], 0, randomValuesArrayIn[row].length - 1);
                long t3_end = System.nanoTime();
                t3 += t3_end - t3_start;
                
                long t4_start = System.nanoTime();
                Algorithm4(randomValuesArrayIn[row]);
                long t4_end = System.nanoTime();
                t4 += t4_end - t4_start;
            }
            matrix[row][0] = (int) t1 / N; 
            matrix[row][1] = (int) t2 / N;
            matrix[row][2] = (int) t3 / N;
            matrix[row][3] = (int) t4 / N;
        }

        int n = 10;
        for (int row = 0; row < 19; row++) {
            matrix[row][4] = (int) Math.ceil(6 + (7/6) * Math.pow(n, 3) + (45/6) * Math.pow(n, 2) + (44/6) * n);
            matrix[row][5] = (int) Math.ceil(6 * Math.pow(n, 2) + 8 * n + 5);
            matrix[row][6] = (int) Math.ceil(14 * n * (Math.log(n) / Math.log(2)) + 14 * n);
            matrix[row][7] = (int) Math.ceil(18 * n + 5);
            n += 5;
        }

        return matrix;
    }
}