/*
 * File:  LinearAlgebra.java
 *
 * Copyright (C) 2000, Dennis Mikkelson
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 *
 * Contact : Dennis Mikkelson <mikkelsond@uwstout.edu>
 *           Department of Mathematics, Statistics and Computer Science
 *           University of Wisconsin-Stout
 *           Menomonie, WI 54751, USA
 *
 * This work was supported by the Intense Pulsed Neutron Source Division
 * of Argonne National Laboratory, Argonne, IL 60439-4845, USA.
 *
 * For further information, see <http://www.pns.anl.gov/ISAW/>
 *
 * Modified:
 *
 *  Programmer:  Dennis Mikkelson
 *
 *  Basic linear algebra operations such as QR factorization and solution of
 *  system of linear equations using QR factorization
 * 
 *  $Log$
 *  Revision 1.7  2003/01/30 21:03:15  pfpeterson
 *  Added methods to convert between float[][] and double[][]. Added
 *  method to find the determinant of 2x2 and 3x3 matrices. Added
 *  methods to confirm a matrix is rectangular or square. Added method
 *  to multiply two matrices together. Renamed some methods. Added more
 *  sanity checks before finding inverse including a simple transpose
 *  if the determinant is one.
 *
 *  Revision 1.6  2003/01/08 17:12:01  dennis
 *  Added method invert() to calculate the inverse of a matrix.
 *
 *  Revision 1.5  2002/11/27 23:15:47  pfpeterson
 *  standardized header
 *
 *  Revision 1.4  2002/06/17 18:54:26  dennis
 *  Added methods QR_solve() and Norm().
 *
 */

package DataSetTools.math;

import DataSetTools.util.*;

/**
 *  Basic linear algebra operations such as dot product, solution of
 *  system of linear equations and matrix inversion.
 */
public final class LinearAlgebra 
{
  /*
   * Don't let anyone instantiate this class.
   */
  private LinearAlgebra() {}

  /**
   * Determines a square matrix A[][] with it's inverse, if
   * possible. Actually casts into a double and calls the other
   * version.
   *
   * @see #getInverse(double[][])
   */
  public static float[][] getInverse(float[][] A){
    if( A==null ) return null;
    return double2float(getInverse(float2double(A)));
  }

  /**
   * Determines a square matrix A[][] with it's inverse, if
   * possible.
   *
   * @param A the square matrix to invert.
   *
   * @return If successful returns the inverse matrix. If A is not
   * invertible or something goes wrong this returns null.
   */
  public static double[][] getInverse(double[][] A){
    if( A==null ) return null;

    if( ! isSquare(A) ) return null;

    int size=A.length;
    double[][] invA=new double[size][size];
    double detA=Double.NaN;

    // check for rotation matrices
    if(size==2 || size==3){          // only know how to do determinants 
      detA=determinant(A);           // for 2x2 and 3x3 matrices
      if(detA==0.){ // this is not invertable
        return null;
      }else if(detA==1.){ // this is a rotation matrix
        for( int i=0 ; i<size ; i++ ){
          for( int j=0 ; j<size ; j++ ){
            invA[i][j]=A[j][i];
          }
        }
      }
    }

    if(size==3){
      if( ! Double.isNaN(detA) ){
        invA[0][0]=(A[1][1]*A[2][2]-A[2][1]*A[1][2])/detA;
        invA[0][1]=(A[2][1]*A[0][2]-A[0][1]*A[2][2])/detA;
        invA[0][2]=(A[0][1]*A[1][2]-A[1][1]*A[0][2])/detA;
        invA[1][0]=(A[2][0]*A[1][2]-A[1][0]*A[2][2])/detA;
        invA[1][1]=(A[0][0]*A[2][2]-A[2][0]*A[0][2])/detA;
        invA[1][2]=(A[1][0]*A[0][2]-A[0][0]*A[1][2])/detA;
        invA[2][0]=(A[1][0]*A[2][1]-A[2][0]*A[1][1])/detA;
        invA[2][1]=(A[2][0]*A[0][1]-A[2][1]*A[0][0])/detA;
        invA[2][2]=(A[0][0]*A[1][1]-A[1][0]*A[0][1])/detA;

        return invA;
      }
    }
    
    // put the identity matrix in temp[][]
    for ( int i = 0; i < size; i++ )
      invA[i][i] = 1;

    double u[][] = QR_factorization( A );

    double result;                       // now calculate the inverse by 
    for ( int i = 0; i < size; i++ )     // solving the equations Ax = invA[i]
    {
      result = QR_solve( A, u, invA[i] );
      if ( Double.isNaN( result ) )
        return null;
    }

    return invA;
  }

  /**
   * Determines a square matrix A[][] with it's inverse, if
   * possible.
   *
   * @param A the square matrix to invert.
   *
   * @return Returns true and the parameter A is changed to the
   * inverse of the original matrix A, if the calculation succeeds.
   * If The matrix is not square, this returns false and the matrix A
   * is not altered.  If the calculation of the inverse fails, this
   * returns false and the values in matrix A will have been altered.
   */
  public static boolean invert(double A[][]){
    double[][] invA=getInverse(A);
    if( invA==null ) return false; // something went wrong
    int size=A.length;
    for( int i=0 ; i<size ; i++ ){
      for( int j=0 ; j<size ; j++ ){
        A[i][j]=invA[i][j];
      }
    }
    return true;
  }

  /**
   * Prints to STDOUT any nXm matrix of doubles
   */
  public static void print(double[][] a){
    int ay=a[0].length;

    for( int i=0 ; i<a.length ; i++ ){
      for( int j=0 ; j<a[i].length ; j++ ){
        System.out.print(a[i][j]+" ");
      }
      System.out.println("");
    }
  }

  /**
   * Converts a rectangular float array to a rectangular double array
   */
  public static double[] float2double(float[] f){
    double[] d=new double[f.length];

    for( int i=0 ; i<f.length ; i++ ){
      d[i]=(double)f[i];
    }
    return d;
  }

  /**
   * Converts a double array to a float array
   */
  public static float[] double2float(double[] d){
    float[] f=new float[d.length];

    for( int i=0 ; i<d.length ; i++ ){
      f[i]=(float)d[i];
    }
    return f;
  }

  /**
   * Converts a rectangular float array to a rectangular double array
   */
  public static double[][] float2double(float[][] f){
    if( f==null ) return null; // make sure it isn't null
    for( int i=1; i<f.length ; i++ ){ // make sure matrix is rectangular
      if( f[i].length!=f[0].length) return null;
    }

    double[][] d=new double[f.length][f[0].length];

    for( int i=0 ; i< f.length ; i++ ){
      for( int j=0 ; j< f[0].length ; j++ ){
        d[i][j]=(double)f[i][j];
      }
    }
    return d;
  }

  /**
   * Converts a rectangular double array to a rectangular float array
   */
  public static float[][] double2float(double[][] d){
    if(!isRectangular(d)) return null;
    float[][] f=new float[d.length][d[0].length];

    for( int i=0 ; i< d.length ; i++ ){
      for( int j=0 ; j< d[0].length ; j++ ){
        f[i][j]=(float)d[i][j];
      }
    }
    return f;
  }

  /**
   * Determines whether or not the specified matrix is square by
   * comparing the lengths of the arrays.
   */
  static public boolean isSquare(float[][]a){
    return isSquare(float2double(a));
  }

  /**
   * Determines whether or not the specified matrix is rectangular by
   * comparing the lengths of the arrays.
   */
  static public boolean isRectangular( double[][] a){
    if(a==null) return false;
    for( int i=1 ; i<a.length ; i++ ){
      if(a[i].length!=a[0].length) return false;
    }
    return true;
  }

  /**
   * Determines whether or not the specified matrix is square by
   * comparing the lengths of the arrays.
   */
  static public boolean isSquare(double[][]a){
    if(a==null) return false;
    if(isRectangular(a)){
      return (a[0].length==a[1].length);
    }else{
      return false;
    }
  }

  /**
   * Find the determinant of a 3x3 or 2x2 matrix
   */
  static public double determinant( double[][] A){
    if(A==null) return Double.NaN;
    if(!isSquare(A)) return Double.NaN;

    double det=0.;
    if(A.length==2){
      det=det+(A[0][0]*A[1][1]-A[0][1]*A[1][0]);
    }else if(A.length==3){
      det=det+A[0][0]*(A[1][1]*A[2][2]-A[2][1]*A[1][2]);
      det=det-A[0][1]*(A[1][0]*A[2][2]-A[2][0]*A[1][2]);
      det=det+A[0][2]*(A[1][0]*A[2][1]-A[2][0]*A[1][1]);
    }else{
      return Double.NaN;
    }
    return det;
  }

  /**
   * Multiply two matrices together
   */
  public static double[][] mult (double[][] a, double[][] b){
    if(!(isRectangular(a)&&isRectangular(b)))
      return null;

    int ax=a.length;
    int ay=a[0].length;
    int bx=b.length;
    int by=b[0].length;

    // check that this is possible
    if(ay!=bx)
      throw new ArrayIndexOutOfBoundsException("Matrices cannot be multiplied "
                                               +"due to dimensionality");

    // create the return matrix
    double[][] c=new double[ax][by];

    // do the math
    int i,j,k;
    for( i=0 ; i< ax ; i++ ){
      for( j=0 ; j<by ; j++ ){
        c[i][j]=0.;
        for( k=0 ; k<ay ; k++ ){
          c[i][j]=c[i][j]+a[i][k]*b[k][j];
        }
      }
    }

    return c;
  }

  /**
   * Multiply two matrices together
   */
  public static float[][] mult(float[][] a, float[][] b){
    if( a==null || b==null ) return null;
    double[][] da=float2double(a);
    double[][] db=float2double(b);

    if(!(isRectangular(da)&&isRectangular(db))){
      return null;
    }else{
      return double2float(mult(da,db));
    }
  }

  /* ------------------------------- solve -------------------------------- */
  /**
    * Solve the system of linear equations Ax = b using the QR factorization
    * of matrix A.  This is NOT the fastest way to solve a linear system, but
    * it is well behaved.  It also immediately provides the "least squares"
    * approximation to a solution of an overdetermined system of equations,
    * as is encountered when fitting a polynomial to data points.  In this case
    * the residual error is returned as the value of the function.  The 
    * solution is returned in parameter "b".  Specifically if A has N columns,
    * then the first N entries of "b" represent the solution to Ax = b if A 
    * is a square matrix.  The first N entries of "b" represent the least 
    * squares solution if A has more rows than columns.
    *
    * @param   A     Rectangular array containing a matrix "A".  This is altered
    *                to contain a matrix "R" where "R" is upper triangular and
    *                A = QR.  The number of rows of A must equal or exceed the
    *                number of columns of A.
    *
    * @param   b     One dimension array of values containing the right hand
    *                side of the linear system of equations Ax = b.  "b" must
    *                have as many rows as matrix "A".
    *
    * @ return  The return value represents the residual error in the least
    *           squares approximation if A has more rows than columns.  If
    *           A is square, the return value is 0.  If A has more columns
    *           than rows, or if the system is singular, this function fails 
    *           and returns NaN.
    */

  public static double solve( double A[][], double b[] )
  { 
    if ( A == null || b == null || 
         A.length != b.length || A.length < A[0].length )
    {
      System.out.println("ERROR: invalid parameters in LinearAlgebra.solve");
      return Double.NaN;
    }

    double u[][] = QR_factorization( A );
                                          // Apply the Householder transforms
                                          // that reduced A to upper triangular
                                          // form to the right hand side, b. 
    return QR_solve( A, u, b );
  }

  /* ------------------------------- QR_solve ----------------------------- */
  /**
   *  Solve a system of linear equations, Ax = b, using the QR factored 
   *  form of A.  This is a portion of the full solution process.  To solve
   *  Ax = b, either:
   *  1. use u = QR_factorization(A) to construct u = Q, A = R and
   *  2. use QR_solve(A,u,b) to replace the components of b with the components
   *     of the solution x.
   *
   *  or just use the solve(A,b) method which combines these two steps.
   *
   *  @param   A     Rectangular array containing a matrix "A", as altered by
   *                 the method QR_factorization(A).
   *
   *  @param  u      The QR factorization of A as returned by the method
   *                 QR_factorization.
   *
   *  @param  b      The right hand side of the linear equations Ax = b
   *
   *  @ return  The return value represents the residual error in the least
   *            squares approximation if u has more rows than columns.  If
   *            u is square, the return value is 0.  If u has more columns
   *            than rows, or if the system is singular, this function fails 
   *            and returns NaN.
   */
  public static double QR_solve( double A[][], double u[][], double b[] )
  {
                                          // Apply the Householder transforms
                                          // that reduced A to upper triangular
                                          // form to the right hand side, b. 
    for ( int i = 0; i < u.length; i++ )
      HouseholderTransform( u[i], b );
                                          // now back substitute  
    for ( int i = A[0].length-1; i >= 0; i-- )
    {
      if ( A[i][i] == 0 )
      {
        System.out.println("ERROR: singular system in  LinearAlgebra.solve");
        return Double.NaN;
      }
      double sum = 0.0;
      for ( int j = i+1; j < A[0].length; j++ )
        sum += b[j] * A[i][j];

      b[i] = ( b[i] - sum )/A[i][i];
    }

    double error = 0.0;
    for ( int i = A[0].length; i < A.length; i++ )
      error = b[i]*b[i];

    return Math.sqrt( error );
  }


  /* --------------------------- QR_factorization -------------------------- */
  /**
    * Produce the QR factorization of a specified matrix A.  The matrix A is
    * assumed to be a full rectangular array of values ( not a "ragged array").
    * Matrix A is altered to be the upper triangular matrix "R" obtained by
    * applying a sequence of orthogonal transformations to A.  The information
    * required to construct Q is returned in a two-dimensional array as the
    * "value" of this function.
    *
    * @param   A     rectangular array containing a matrix "A".  This is altered
    *                to contain a matrix "R" where "R" is upper triangular and
    *                A = QR.  The number of rows of A must equal or exceed the
    *                number of columns of A.
    *
    * @return  A 2D array containing the unit vectors "U" that generate 
    *          the Householder transformations that were used to reduce the
    *          matrix A to upper triangular form.  Row zero of the returned
    *          array contains the vector "U" that was used to get 0's under
    *          element A[0][0].  Row one of the returned array contains the
    *          vector "U" that was used to get 0's under element A[1][1], etc.
    *          For each i, row i contains the entries of a column vector Ui 
    *          corresponding to an orthogonal transformation 
    *          Qi = I - 2*Ui*transpose(Ui), where I is the identity matrix.
    *          A is reduced to the triangular matrix R by multiplication on 
    *          the left by the product: Qn * ... * Q2 * Q1 * Q0.  
    */

  public static double[][] QR_factorization( double A[][] )
  {
    double s;                        // s holds the sqrt of the sum of the 
                                     // squares of the last terms in a column 
    double dot_prod;                 // holds the dot product of U with a vector

    double norm;
    int    row,
           col;
    int    n_rows_A = A.length;
    int    n_cols_A = A[0].length;
    double U[][] = new double[n_cols_A][n_rows_A];

    for ( col = 0; col < n_cols_A; col++ )        // reduce each column of A
    {
      s = 0.0;                                    // find sum of squares of
      for ( row = col; row < n_rows_A; row++ )    // later elements in A[][col]
        s += A[row][col] * A[row][col]; 
      s = Math.sqrt( s );

      for ( row = 0; row < col; row++ )           // build U vector
        U[col][row] = 0; 

      if ( A[col][col] > 0 )
        U[col][col] = A[col][col] + s; 
      else
        U[col][col] = A[col][col] - s; 

      for ( row = col+1; row < n_rows_A; row++ )
        U[col][row] = A[row][col];

      normalize( U[col] );

      // Now multiply A by the Housholder transform corresponding to U[col].  
      // The effect of the Householder transform on any vector V is 
      // to change V to V - cU, where c is twice the dot product of V and U.
      // Since the first entries in U are non-zero, we only alter the values 
      // of A in the lower right portion of A.
                  
      for ( int j = col; j < n_cols_A; j++ )   // alter column "j"
      {
         dot_prod = 0.0;                      // find dot product of U and col j
         for ( int i = col; i < n_rows_A; i++ )       
           dot_prod += U[col][i] * A[i][j];
                                              // V = V - cU, where V is col j 
                                              // of A
         for ( int i = col; i < n_rows_A; i++ )
           A[i][j] -= 2 * dot_prod * U[col][i];
      }
    }
    return U; 
  }


  /* ---------------------- HouseholderTransform --------------------------- */
  /**
   *  Calculate the effect of the Householder transform Q = I - 2 u transp(u)
   *  on a vector y, given the unit vector u.  This function replaces y by Qy. 
   *
   *  @param  u   Unit vector that determines the Householder transformation, Q.
   *  @param  y   Vector to which the Householder transformation is applied. 
   *              y is altered by the method and on completion y = Qy.
   *
   */
   public static void HouseholderTransform( double u[], double y[] )
   {
      if ( u == null || y == null || u.length != y.length )
      {
        System.out.println("ERROR: Invalid vectors in Householder Tranform");
        return;
      }

      double sum = 0.0;
      for ( int i = 0; i < y.length; i++ )
        sum += u[i] * y[i];

      double c = 2.0 * sum;
      for ( int i = 0; i < y.length; i++ )
        y[i] -= c * u[i]; 
   }


  /* -------------------------- DotProduct --------------------------------- */
  /**
   *  Calculate the dot product of two vectors.
   *
   *  @param  u   First vector
   *  @param  v   Second vector 
   *
   *  @return  The dot product  u.v  provided that both u and v have the 
   *           same number of entries.  If u and v have different numbers
   *           of entries, the shorter length is used.  If either u or v has
   *           length 0, this returns NaN. 
   */
   public static double dotProduct( double u[], double v[] )
   {
      if ( u == null || v == null || u.length == 0 || v.length == 0 )
        return Double.NaN;

      int n = u.length;
      if ( v.length < u.length )
        n = v.length;

      double sum = 0.0;
      for ( int i = 0; i < n; i++ )
        sum += u[i] * v[i];

      return sum;         
   }

  /* --------------------------- Normalize --------------------------------- */
  /**
   *  Normalize the given vector to have length 1, by dividing by it's length
   *  if it's not the zero vector.
   *
   *  @param  v    The vector to be normalized.  If v is not the zero vector,
   *               each component of v is divided by the length of v.  If v is 
   *               the zero vector, it is not changed.
   */
 
  public static void normalize( double v[] )
  {
    double norm = 0.0;

    for ( int i = 0; i < v.length; i++ )
      norm += v[i]*v[i];

    if ( norm == 0 )              // if we have a zero vector, don't change it
      return;

    norm = Math.sqrt( norm );
                   
    for ( int i = 0; i < v.length; i++ )
      v[i] /= norm;
  }


  /* ----------------------------- Norm --------------------------------- */
  /**
   *  Calculate the L2 norm of a vector.
   *
   *  @param  v    The vector whose norm is to be calculated.
   *
   *  @return  The square root of the sum of the squares of the components
   *           of the specified vector.
   */

  public static double norm( double v[] )
  {
    double norm = 0.0;

    for ( int i = 0; i < v.length; i++ )
      norm += v[i]*v[i];

    return Math.sqrt( norm );
  }


  /* ---------------------------- main -------------------------------- */
  /* main program for test purposes only 
  */
   static public void main( String[] args )
   {
     double v[] = new double[3];

     v[0] = 1;
     v[1] = 4;
     v[2] = 8;

     normalize(v);
     for ( int i = 0; i < v.length; i++ )
       System.out.println( " v[" + i + "] = " + v[i] );

     double A[][] = new double[4][3];

     A[0][0] = 1;
     A[0][1] = 1;
     A[0][2] = 1;

     A[1][0] = 1;
     A[1][1] = 2;
     A[1][2] = 4; 

     A[2][0] = 1;
     A[2][1] = 3;
     A[2][2] = 9;

     A[3][0] = 1;
     A[3][1] = 4;
     A[3][2] = 16;

     boolean test_QR = false;
     if ( test_QR )
     {
       double U[][] = QR_factorization( A );

       System.out.println("R = ");
       for ( int i = 0; i < A.length; i++ )
       {
         for ( int j = 0; j < A[i].length; j++ )
           System.out.print( " "+ A[i][j] ); 
         System.out.println();
       }

       System.out.println("U = ");
       for ( int i = 0; i < U.length; i++ )
       {
         for ( int j = 0; j < U[i].length; j++ )
           System.out.print( " "+ U[i][j] );  
         System.out.println();
       }
    }
    else
    {
      double b[] = new double[4];
      b[0] = 3;
      b[1] = 5;
      b[2] = 7;
      b[3] = 9;
      double result = solve( A, b );
      for ( int i = 0; i < b.length; i++ )
        System.out.println( "b[" + i + "] = " + b[i] );

      System.out.println("returned value = " + result );
    } 
   }
}
