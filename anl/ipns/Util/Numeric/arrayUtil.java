/*
 * @(#)arrayUtil.java  1999/01/10   Dennis Mikkelson
 *
 *  $Log$
 *  Revision 1.5  2000/07/26 20:48:57  dennis
 *  added method to interpolate in tables of x,y values
 *
 *  Revision 1.4  2000/07/21 14:24:26  dennis
 *  Added binary search of a portion of an array of integers
 *
 *  Revision 1.3  2000/07/10 22:55:37  dennis
 *  July 10, 2000 version... many changes
 *
 *
 *  Revision 1.5  2000/06/13 14:39:25  dennis
 *  Added dcoumentation and simplified the method to get a portion of an array.
 *
 *  Revision 1.4  2000/06/08 19:07:37  dennis
 *  Fixed DOS text problem
 *
 *  Revision 1.3  2000/05/11 16:18:22  dennis
 *  Added RCS logging
 *
 *
 */ 
package DataSetTools.util;

/**
 *
 *  This class provides static methods for some basic array operations  
 *  such as reversing, finding values and extracting a portion of
 *  an array of floats. 
 *  
 */
public class arrayUtil 
{

  /**
   * Don't let anyone instantiate this class.
   */
  private arrayUtil() {}


  /**
   *  Reorder the elements of the specified array in reverse order.
   *
   *  @param  arr   The array whose elements are reversed.
   */
  public static void Reverse( float arr[] )
  {
    float temp;
    int   length = arr.length;
    int   length_m_1 = length-1;
    int   k;

    for ( int i = 0; i < length/2; i++ )
    {
      k = length_m_1 - i;
      temp = arr[i];
      arr[i] = arr[k];
      arr[k] = temp;
    }
  }


  /**
   *  Find a value in an array of floats that is IN INCREASING ORDER, by using
   *  a binary search.  
   *
   *  @param  x       The value to find
   *  @parma  x_vals  The array of x values IN INCREASING ORDER.
   *
   *  @return The last index, i, for which  x_vals[i] <= x.  If x_vals[i] > x
   *          for all indices, or if the array is empty, this returns -1.
   */
   
   public static int get_index_of( float x, float x_vals[] )
   {
     if ( x_vals == null || x_vals.length <= 0 )
       return -1;

     if ( x < x_vals[0] )                           // x to left of all values
       return -1;

     else if ( x > x_vals[ x_vals.length-1 ] )      // x to right of all values
       return x_vals.length-1;

                                             // do binary search to find value  
     int     first = 0;          
     int     last  = x_vals.length-1;  
     int     mid   = 0;
     boolean found = false;

     while ( !found && first <= last )
     {
       mid   = (first + last) / 2;
       if ( x == x_vals[mid] )
         found = true;
       else if ( x < x_vals[mid] )
         last = mid - 1;
       else if ( x > x_vals[mid] )
         first = mid + 1;
     }

    if ( found )                          // if exact value is in list, return
      return mid;                         // the position where it occurred.
    else
      return last;                        // first & last have crossed
   }


  /**
   *  Find a value in an array of ints that is IN INCREASING ORDER, by using
   *  a binary search.
   *
   *  @param  x       The value to find
   *  @parma  x_vals  The array of x values IN INCREASING ORDER.
   *  @param  first   The first position to be considered in the search.
   *  @param  last    The last position to be considered in the search.
   *
   *  @return  The index at which x was found in the array, or -1 if x was
   *           not found.
   */

   public static int get_index_of( int  x, 
                                   int  x_vals[], 
                                   int  first, 
                                   int  last )
   {
     if ( x_vals == null || x_vals.length <= 0 )
       return -1;

     if ( x < x_vals[0] )                           // x to left of all values
       return -1;

     else if ( x > x_vals[ last ] )                 // x to right of all values
       return -1;

     if ( first > last )
       return -1;

                                             // do binary search to find value
     if ( first < 0 )
       first = 0;

     if ( last > x_vals.length - 1 )
       last = x_vals.length - 1;

     int     mid   = 0;
     boolean found = false;

     while ( !found && first <= last )
     {
       mid   = (first + last) / 2;
       if ( x == x_vals[mid] )
         found = true;
       else if ( x < x_vals[mid] )
         last = mid - 1;
       else if ( x > x_vals[mid] )
         first = mid + 1;
     }

    if ( found )                          // if exact value is in list, return
      return mid;                         // the position where it occurred.
    else
      return -1;                          // first & last have crossed, so
                                          // not in list
   }


/**
 *  Obtain an approximate value for y(x) by interpolating using tables of 
 *  values for x and y.  If the x_value is outside of the interval of x values
 *  in the table, this returns the first or last y value.  The table of 
 *  x values must be in order and should all be distinct.
 *
 *  @param  x_value      the x value for which the corresponding y value is to
 *                       be interpolated
 *  @param  x[]          array of x values
 *  @param  y[]          array of corresponding y values.  There must be
 *                       at least as many y values as x values.  
 *
 *  @return interpolated y value at the specified x value
 */
public static float interpolate( float x_value, float x[], float y[] )
{
  if ( x_value < x[0] )
    return y[0];

  if ( x_value > x[x.length-1] )
    if ( x.length <= y.length )
      return y[ x.length - 1 ];
    else
      return y[ y.length - 1 ];         // take the closest value we have

  int index = arrayUtil.get_index_of( x_value, x );

  if ( x_value == x[index] )            // if exact value is in list, return
    return y[index];                    // the corresponding y value.

  float x1 = x[index];                  // x_value between two listed x_vals
  float x2 = x[index + 1];

  if ( x1 == x2 )                       // duplicate x values 
    return y[index];  

  float y1 = y[index];                  // otherwise, interpolate
  float y2 = y[index+1];
  return y1 + ( x_value - x1 )*( y2 - y1 ) / ( x2 - x1 );
}




  /**
   *  Extract a sequence of elements from the given array to form a new
   *  array.  If a longer array is needed, the unused positions are filled
   *  with zeros.
   *
   *  @param   arr        the source array from which elements are extracted.
   *  @param   new_length the size of the new array to be created.
   *
   *  @return  An new array filled with elements from the given array and
   *           zero padded if necessary. 
   */
  public static float[] getPortion( float arr[], int new_length )
  {
    float new_arr[] = new float[ new_length ];

    if ( arr.length < new_length )  
    {                                                   // too few, so 0 pad
      System.arraycopy( arr, 0, new_arr, 0, arr.length );
      for ( int i = arr.length; i < new_length; i++ )
        new_arr[i] = 0;
    } 
    else                                                // too many, truncate
      System.arraycopy( arr, 0, new_arr, 0, new_length );

    return new_arr; 
  }


  /**
   *  Get the largest value in an array.  
   *
   *  @param  arr  The array in which the largest value is to be found
   *
   *  @return  The largest value in the array.  If the array is empty, 
   *           return Float.NEGATIVE_INFINITY
   */
  public static float getMax( float arr[] )
  {
    if ( arr == null || arr.length <= 0 )
      return Float.NEGATIVE_INFINITY;
    else
    {
      float max = arr[0];
      for ( int i = 1; i < arr.length; i++ )
        if ( max < arr[i] )
          max = arr[i]; 
      return max;
    }
  }

  /**
   *  Get the smallest value in an array.  
   *
   *  @param  arr  The array in which the smallest value is to be found
   *
   *  @return  The smallest value in the array.  If the array is empty, 
   *           return Float.POSITIVE_INFINITY
   */
  public static float getMin( float arr[] )
  {
    if ( arr == null || arr.length <= 0 )
      return Float.POSITIVE_INFINITY;
    else
    {
      float min = arr[0];
      for ( int i = 1; i < arr.length; i++ )
        if ( min > arr[i] )
          min = arr[i];
      return min;
    }
  }


  /**
   *  Main program for testing purposes only.
   */
  public static void main( String argv[] )
  {
    float test[] = { 1, 2, 3, 4, 5, 6, 7, 8 };

    System.out.println( "Original:");
    for ( int i = 0; i < test.length; i++ )
      System.out.println( test[i] );

    arrayUtil.Reverse( test );
    System.out.println( "Reversed:");
    for ( int i = 0; i < test.length; i++ )
      System.out.println( test[i] );

    float new_test[] = arrayUtil.getPortion( test, 5 );
    System.out.println( "Truncated to 5 entries:");
    for ( int i = 0; i < new_test.length; i++ )
      System.out.println( new_test[i] );

    new_test = arrayUtil.getPortion( test, 10 );
    System.out.println( "Expanded to 10 entries:");
    for ( int i = 0; i < new_test.length; i++ )
      System.out.println( new_test[i] );

  }
}
