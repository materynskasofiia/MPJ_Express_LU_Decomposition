import mpi.*;

public class Main {

    public static void main(String[] args) throws Exception {
        int n=4;
        int[] map= new int[n];
        double[][] a=new double[n][n];
        MPI.Init(args);
        int me = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        if(me==0) {
//            a = new double[][]{{2., -7., 4.},
//                    {1., 9., -6.},
//                    {-3., 8., 5.}};
//          a = new double[][]{{2., 4., -4.},
//                  {1., -4., 3.},
//                  {-6., -9., 5.}};
//           a = new double[][]{{1., 2., 3.},
//                  {2., -4., 6.},
//                  {3., -9., -3.}};
            for(int i=0; i<n; i++){
                for(int j=0; j<n; j++){
                    a[i][j]=Math.round(Math.random()*n*n);
                    System.out.print(a[i][j]+"\t");
                }
                System.out.println();
            }

        }
        for (int i = 0; i < n; i++) {
            MPI.COMM_WORLD.Bcast(a[i], 0, n, MPI.DOUBLE, 0);
        }
        for(int i = 0; i < n; i++) {
            map[i] = i % size;
        }

        for (int k = 0; k < n-1 ; k++) {
            for (int i = k + 1; i < n; i++) {
                a[i][k] /= a[k][k];
            }
            MPI.COMM_WORLD.Bcast(a[k], 0, n , MPI.DOUBLE, map[k]);
            for (int i = k + 1; i < n; i++) {
                for (int j = k+1 ; j < n; j++) {
                    a[i][j] -= a[i][k] * a[k][j];
                }
            }
        }

        // Printing the entries of the matrix
        for(int i = 0; i<n; i++) {
            if(map[i] == me) {
                for (int j = 0; j < n; j++) {
                    System.out.println(i + " " + j + "\t" + a[i][j]);
                }
            }
        }
        MPI.Finalize();
    }
}