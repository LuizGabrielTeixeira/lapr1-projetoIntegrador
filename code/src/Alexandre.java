import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.Arrays;

public class Alexandre {
    // Calcula o Erro Absoluto Médio (EAM)
    public static void main(String[] args) {
        // Matriz de exemplo (3x3)
        double[][] A = {
                {1.0, 2.0, 3.0},
                {2.0, 1.0, 6.0},
                {3.0, 6.0, 1.0}
        };

        int N = A[0].length;

        double[][] colunaMedia = colunaMedia(A);
        System.out.println("Média das colunas:");
        for (int i = 0; i < colunaMedia.length; i++) {
            System.out.println(colunaMedia[i][0]);
        }
        System.out.println();
        System.out.println("A:");
        double[][] desvios = matrixDesvios(A,colunaMedia);
        for (int i = 0; i < desvios.length; i++) {
            for (int j = 0; j < desvios[i].length; j++) {
                System.out.print(desvios[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
        System.out.println("C:");
        double[][] covariancia = covariancias(desvios,N);
        for (int i = 0; i < covariancia.length; i++) {
            for (int j = 0; j < covariancia[i].length; j++) {
                System.out.print(covariancia[i][j] + " ");
            }
            System.out.println();

        }
        System.out.println();
        System.out.println("Valores C:");
        double[][] valProC = valoresPropriosC(covariancia);
        for (int i = 0; i < valProC.length; i++) {
            for (int j = 0; j < valProC[i].length; j++) {
                System.out.print(valProC[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
        System.out.println("Vetores C:");
        double[][] vetProC = vetoresPropriosC(covariancia);
        for (int i = 0; i < vetProC.length; i++) {
            for (int j = 0; j < vetProC[i].length; j++) {
                System.out.print(vetProC[i][j] + " ");
            }
            System.out.println();

        }
    }
    public static double calculateEAM(double[][] A, double[][] Ak) {
        int M = A.length;
        int N = A[0].length;
        double erroAbsMed = 0;
        // Percorre cada elemento da matriz
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                erroAbsMed += Math.abs(A[i][j] - Ak[i][j]);
            }
        }

        // Calcula o erro médio
        return erroAbsMed / (M * N);
    }

    public static boolean testCalculateEAM(double[][] A, double[][] Ak, double expectedEAM) {
        // Chama a função que calcula o EAM
        double calculatedEAM = calculateEAM(A, Ak);

        // Compara o valor calculado com o esperado
        return calculatedEAM == expectedEAM;
    }

    //3-----------------------------------------------------
    public static double[][] colunaMedia(double[][] matrix) {
        int linhas = matrix.length;
        int colunas = matrix[0].length;
        double[][] mediaColuna = new double[linhas][1];
        for (int i = 0; i < linhas; i++) {
            double soma = 0;
            for (int j = 0; j < colunas; j++) {
                soma = soma + matrix[i][j]; // Soma os elementos de cada linha
            }
            mediaColuna[i][0] = soma/colunas; // Calcula a média e armazena na matriz coluna
        }
        return mediaColuna;
    }
    public static boolean testColunaMedia(double[] matrix, double[] expectedColMedia) {
        return Arrays.equals(matrix, expectedColMedia);
    }

    public static double[][] matrixDesvios(double[][] matrix, double[][] colunaMedia) {
        int colunas = matrix[0].length;
        int linhas = matrix.length;
        double[][] desvios = new double[linhas][colunas];

        for (int j = 0; j < colunas; j++) {
            for (int i = 0; i < linhas; i++) {
                desvios[i][j] =  colunaDesvio(matrix[i][j],colunaMedia[i][0]);
            }

        }
        return desvios;
    }

    public static double colunaDesvio(double valorMatrix, double valorColunaMedia) {

        double desvio = valorMatrix - valorColunaMedia; // Calcula o desvio de cada elemento da matriz em relação à média da linha correspondente

        return desvio;
    }

    /* public static double[][] calculoDesvios(double[][] matrix, double[][] colunaMedia) {
        int colunas = matrix[0].length;
        int linhas = matrix.length;
        double[][] desvios = new double[linhas][colunas];

        for (int j = 0; j < colunas; j++) {
            for (int i = 0; i < linhas; i++) {
                desvios[i][j] = matrix[i][j] - colunaMedia[i][0]; // Calcula o desvio de cada elemento da matriz em relação à média da linha correspondente
            }

        }
        return desvios;
    }*/

    public static boolean testCalDesvios(double[][] matrix, double[][] expectedDesvios) {
        return Arrays.equals(matrix, expectedDesvios);
    }

    public static double[][] covariancias(double[][] A,int N) {
        double[][] AT = transpostaMatriz(A);
        double[][] AAT = multiplicaMatrizes(A,AT);
        return multiplicaMatrizPorEscalar(AAT,1.0/N);
    }

    public static boolean testCovariancia(double[][] C, double[][] expectedC) {
        return C == expectedC;
    }



    //-------------------------------------------------------

    //4------------------------------------------------------

    public static double[][] valoresPropriosC(double[][] C) {
        EigenDecomposition eigenDecomposition = decomposeMatrix(C);
        RealMatrix D = eigenDecomposition.getD();
        double[][] dArray = D.getData();
        return dArray;
    }

    public static double[][] vetoresPropriosC(double[][] C) {
        EigenDecomposition eigenDecomposition = decomposeMatrix(C);
        RealMatrix V = eigenDecomposition.getV();
        double[][] vArray = V.getData();
        return vArray;
    }

    //------------------------------------------------------

    public static EigenDecomposition decomposeMatrix(double[][] arrayParaDecompor) {
        Array2DRowRealMatrix matrix = new Array2DRowRealMatrix(arrayParaDecompor);
        EigenDecomposition eigenDecomposition = new EigenDecomposition(matrix);

//        RealMatrix eigenVectors = eigenDecomposition.getV();
//        RealMatrix eigenValues = eigenDecomposition.getD();
//        RealMatrix eigenVectorsTranspose = eigenDecomposition.getVT();

        return eigenDecomposition;
    }

    public static double[][] transpostaMatriz(double[][] matriz) {
        double[][] matrizTransposta = new double[matriz[0].length][matriz.length];
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[0].length; j++) {
                matrizTransposta[j][i] = matriz[i][j];
            }
        }
        return matrizTransposta;
    }

    public static double[][] multiplicaMatrizes(double[][] matrizLeft, double[][] matrizRight) {
        double[][] matrizResultante = new double[matrizLeft.length][matrizRight[0].length];
        for (int i = 0; i < matrizLeft.length; i++) {
            for (int j = 0; j < matrizRight[0].length; j++) {
                for (int k = 0; k < matrizRight.length; k++) {
                    matrizResultante[i][j] += matrizLeft[i][k] * matrizRight[k][j];
                }
            }
        }
        return matrizResultante;
    }

    public static double[][] multiplicaMatrizPorEscalar(double[][] matriz, double escalar) {
        double[][] matrizResultante = new double[matriz.length][matriz[0].length];
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[0].length; j++) {
                matrizResultante[i][j] = matriz[i][j] * escalar;
            }
        }
        return matrizResultante;
    }
}