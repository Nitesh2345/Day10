package threading;

public class MatrixMultiplication {

        private static final int MATRIX_SIZE = 3; // Example size, can be changed
        private static int[][] matrixA = new int[MATRIX_SIZE][MATRIX_SIZE];
        private static int[][] matrixB = new int[MATRIX_SIZE][MATRIX_SIZE];
        private static int[][] resultMatrix = new int[MATRIX_SIZE][MATRIX_SIZE];

        public static void main(String[] args) {
            // Initialize matrices with example values
            initializeMatrices();

            // Create and start threads
            Thread[] threads = new Thread[MATRIX_SIZE];
            for (int i = 0; i < MATRIX_SIZE; i++) {
                threads[i] = new Thread(new Worker(i));
                threads[i].start();
            }

            // Wait for all threads to complete
            for (int i = 0; i < MATRIX_SIZE; i++) {
                try {
                    threads[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Print the result matrix
            printMatrix(resultMatrix);
        }

        private static void initializeMatrices() {
            // Example initialization, can be replaced with actual data
            for (int i = 0; i < MATRIX_SIZE; i++) {
                for (int j = 0; j < MATRIX_SIZE; j++) {
                    matrixA[i][j] = i + j;
                    matrixB[i][j] = i - j;
                }
            }
        }

        private static void printMatrix(int[][] matrix) {
            for (int[] row : matrix) {
                for (int value : row) {
                    System.out.print(value + " ");
                }
                System.out.println();
            }
        }

        // Worker class to perform matrix multiplication for a row
        static class Worker implements Runnable {
            private int row;

            public Worker(int row) {
                this.row = row;
            }

            @Override
            public void run() {
                for (int col = 0; col < MATRIX_SIZE; col++) {
                    resultMatrix[row][col] = 0;
                    for (int k = 0; k < MATRIX_SIZE; k++) {
                        synchronized (MatrixMultiplication.class) {
                            resultMatrix[row][col] += matrixA[row][k] * matrixB[k][col];
                        }
                    }
                }
            }
        }
}
