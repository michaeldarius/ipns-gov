/**
 * File: StringUtil.java
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
 *           Menomonie, WI. 54751
 *           USA
 *
 * This work was supported by the Intense Pulsed Neutron Source Division
 * of Argonne National Laboratory, Argonne, IL 60439-4845, USA.
 *
 * For further information, see <http://www.pns.anl.gov/ISAW/>
 *
 * Modified:
 *
 *  $Log$
 *  Revision 1.6  2002/04/17 21:33:10  dennis
 *  Made replace() method more robust.  Now works even if the
 *  replaced string is contained in the new string.
 *  Added method replace_token() that only makes replacements if
 *  the string is bordered by non-alphanumeric characters.
 *  Added method extract_tokens() that places separate tokens that
 *  occur in a String into an array of Strings.
 *
 *  Revision 1.5  2001/08/14 15:14:54  dennis
 *  Fixed documentation.
 *
 *  Revision 1.4  2001/08/10 19:49:28  dennis
 *  Added method commandPresent() to check whether or not a specified
 *  single command with no argument occurs in an argument list.
 *
 *  Revision 1.3  2001/08/10 14:54:46  dennis
 *  Added methods to find the nth occurence of a string and to find
 *  arguments for commands of the form "-<letter><argument>" on a
 *  command line.
 *
 *  Revision 1.2  2001/04/25 22:24:45  dennis
 *  Added copyright and GPL info at the start of the file.
 *
 *  Revision 1.1  2000/07/28 16:16:08  dennis
 *  Utility for fixing file names for portability to different systems
 *
 */ 
package DataSetTools.util;

import java.awt.*;
import java.io.*;
import java.util.*;

/**
 *  Provide utilities for fixing file names, parsing command lines, etc.
 */

public class StringUtil
{
  /**
   *  Don't instantiate this class.
   */
  private StringUtil() {}


  /* ---------------------------- fixSeparator --------------------------- */
  /**
   *  Replace all occurrences of the possible file separators "/" "\" "\\"
   *  with the file separator needed for the local system.
   *
   *  @param  file_name  A file name string possibly containing improper
   *                     separators.
   *
   *  @return  A string containing the file name with all separators replaced 
   *           by the system dependent separtator needed on the local system. 
   */
  public static String fixSeparator( String file_name )
  {
    String separator = File.separator;

    String result = replace( file_name, "\\\\", separator );
    result = replace( result, "\\", separator );
    result = replace( result, "/", separator );

    return result;
  }

  /* ------------------------------ replace ------------------------------ */
  /**
   *  Replace all occurrences of a specified string by another string
   *
   *  @param   in_string  the string in which the replacement is to be 
   *                      made. 
   *
   *  @param   old_chars  the string which is to be replaced.
   *
   *  @param   new_chars  the string that replaces old_chars.
   *
   *  @return  A new string in which all occurences of the old_chars string 
   *           are replaced by the new_chars string. 
   */
  public static String replace( String in_string, 
                                String old_chars, 
                                String new_chars )
  {
    if (in_string == null || old_chars == null || new_chars == null )
      return null;

    if ( old_chars.equals( new_chars ) )
      return in_string;

    int    start;
    String result = in_string;

    int from_index = 0; 
    while ( result.indexOf( old_chars, from_index ) >= 0 )
    {
      start = result.indexOf( old_chars, from_index );
      result = result.substring(0, start) + 
               new_chars + 
               result.substring( start + old_chars.length() );
      from_index = start + new_chars.length();
    }
    return result;
  }


  /* ---------------------------- replace_token --------------------------- */
  /**
   *  Replace all occurrences of a specified string by another string, provide
   *  the occurrence of the string is bordered by non-alphanumeric characters
   *  or by the ends of the string.  This is to allow replacing occurrence of
   *  variables such as t, t0, t1, etc. in expressions by other tokens.  If
   *  the adjacent characters are not checked, just replacing all "t"s  by "x"
   *  would damage "t0" and "t1". 
   *
   *  @param   in_string  the string in which the replacement is to be
   *                      made.
   *
   *  @param   old_chars  the string which is to be replaced.
   *
   *  @param   new_chars  the string that replaces old_chars.
   *
   *  @return  A new string in which bounded occurences of the old_chars string
   *           are replaced by the new_chars string.
   */
  public static String replace_token( String in_string,
                                      String old_chars,
                                      String new_chars )
  {
    if (in_string == null || old_chars == null || new_chars == null )
      return null;

    if ( old_chars.equals( new_chars ) )
      return in_string;

    int    start;
    char   right_border;
    char   left_border;
    String result = in_string;

    int from_index = 0; 
    while ( result.indexOf( old_chars, from_index ) >= 0 )
    {

      start        = result.indexOf( old_chars, from_index );

      if ( start > 0 )
        left_border  = result.charAt( start-1 );
      else 
        left_border = '$'; 

      if ( (start + old_chars.length()) < result.length() )
        right_border = result.charAt( start + old_chars.length() );
      else
        right_border = '$'; 

      if ( !Character.isLetterOrDigit( left_border ) && 
           !Character.isLetterOrDigit( right_border)  )
        {
          result = result.substring(0, start) +
                   new_chars +
                   result.substring( start + old_chars.length() );
          from_index = start + new_chars.length();
        }
        else
          from_index = start + old_chars.length();
    }
    return result;
  }


  /* -------------------------- extract_tokens --------------------------- */
  /**
   *  Produce an array of string "tokens" that are present in the specified
   *  string and separated by the specified delimiters.
   *
   *  @param   in_string   the string from which the tokens are to be extracted
   *
   *  @param   delimiters  the string containing the delimiters, 
   *                       eg: " ,;:\t\n\r\f"
   *
   *  @return  An array of strings containing the tokens 
   */
  public static String[] extract_tokens( String in_string,
                                         String delimiters )
  {
    StringTokenizer tokenizer = new StringTokenizer( in_string, delimiters );
    Vector tokens = new Vector();
    String token;

    while (tokenizer.hasMoreTokens())
      tokens.add( tokenizer.nextToken() );

    String list[] = new String[ tokens.size() ];
    for ( int i = 0; i < list.length; i++ )
      list[i] = (String)tokens.elementAt(i);

    return list; 
  }

  /* ----------------------------- getCommand ------------------------- */
  /**
   *  Find the argument following a specified occurence of a command in a
   *  string.  Eg. If the string is
   *  "-D   /home/dennis/ -Llog.txt -D/usr/data/"
   *  the, calling this method with num = 2, and command = "-D" will return
   *  "/usr/data/".
   *
   *  @param  n        The number of the occurence of the command, 1, 2, etc.
   *  @param  command  The command to look for
   *  @param  s        The string containing commands and arguments
   *                   with space between successive command-argument pairs.
   *
   *  @return The string associated with the nth occurence of the given command.
   */
  public static String getCommand( int num, String command, String s )
  {
    boolean found = false;

    int position = nth_index_of( num, command, s );
    if ( position < 0 )
      return "";

    int start = position + command.length();

    while ( start < s.length() && Character.isWhitespace( s.charAt(start) ) )
      start++;

    if ( start >= s.length() )
      return "";

    int end = start;
    while ( end < s.length() && !Character.isWhitespace( s.charAt(end) ) )
      end++;

    if ( end < s.length() )
      return s.substring( start, end ).trim();
    else
      return s.substring( start ).trim();
  }

  /* ----------------------------- getCommand ------------------------- */
  /**
   *  Find the argument following a specified occurence of a command in a
   *  list of argument strings.  The list of argument strings is concatenated
   *  and the resulting string is passed to the version of getCommand that
   *  takes a single string.
   *
   *  @param  n        The number of the occurence of the command, 1, 2, etc.
   *  @param  command  The command to look for
   *  @param  args     The array of strings containing commands and arguments
   *                   with space between successive command-argument pairs.
   *
   *  @return The string associated with the nth occurence of the given command.
   */
  public static String getCommand( int n, String command, String args[] )
  {
    if ( args == null || args.length == 0 )
      return "";
    
    String s = args[0];
    for ( int i = 1; i < args.length; i++ )
      s += " " + args[i];

    return getCommand( n, command, s );
  }

  /* --------------------------- commandPresent -------------------------- */
  /**
   *  Determine whether or not the specified string occurs in the array 
   *  of strings.
   *
   *  @param  args  The array of strings containing commands and arguments
   *                with space between successive command-argument pairs or
   *                individual commands without arguments
   */
  public static boolean commandPresent( String command, String args[] )
  {
    if ( args == null || args.length == 0 )
      return false;
  
    String s = args[0];
    for ( int i = 1; i < args.length; i++ )
      s += " " + args[i];

    if ( s.indexOf( command ) >= 0 )
      return true;
    else
      return false;
      
  }


  /* ----------------------------- nth_index_of ------------------------- */
  /**
   *  Find the "nth" occurence of a string in another string.  This will return
   *  -1 if the specified occurence does not exist.
   *
   *  @param  n        The number of the occurence of the wanted string that
   *                   is being searched for
   *  @param  wanted   The string to search for
   *  @param  s        The string to search in
   *
   *  @return The index of the first character of the "nth" occurence of the 
   *          wanted string in the specified string s, if there is an "nth"
   *          occurence, -1 otherwise.
   */

  public static int nth_index_of( int n, String wanted, String s )
  {
    if( n <= 0 )
      return -1;

    if( n > s.length() - wanted.length() )
      return -1;

    int count = 0;
    int k = s.indexOf(wanted);
    while ( k >= 0 )
    {
      count++;
      if( count == n )
        return k;

       k = s.indexOf( wanted, k+1 );
     }
     return -1;
  }




/* ---------------------------------------------------------------------------
 *
 *  Main program for test purposes.
 *
 */
  public static void main(String[] args)
  {
    String s       = "-D   /home/dennis/ -Llog.txt -D/usr/data/";
    String command = "-D";
    int    n       = 2;
    System.out.println("In string " + s + " with command " + command + " " + n);
    System.out.println( getCommand( n, command, s ) );

    n       = 1;
    System.out.println("In string " + s + " with command " + command + " " + n);
    System.out.println( getCommand( n, command, s ) );

    command = "-L";
    System.out.println("In string " + s + " with command " + command + " " + n);
    System.out.println( getCommand( n, command, s ) );

    n = 0;
    System.out.println("In string " + s + " with command " + command + " " + n);
    System.out.println( getCommand( n, command, s ) );

    n = -1;
    System.out.println("In string " + s + " with command " + command + " " + n);
    System.out.println( getCommand( n, command, s ) );

    n = 2;
    System.out.println("In string " + s + " with command " + command + " " + n);
    System.out.println( getCommand( n, command, s ) );

    n = 300;
    System.out.println("In string " + s + " with command " + command + " " + n);
    System.out.println( getCommand( n, command, s ) );

    command = "-D";
    n = 2;
    System.out.println(" with command " + command + " " + n);
    System.out.println("On command line, found");
    System.out.println( getCommand( n, command, args ) );


    int [] ilist;
    String in_str;

    for (int i = 0; i < args.length; i++)
    {
      in_str = args[i];

      System.out.println( in_str + " translates to " + fixSeparator(in_str) );
    } 

    in_str = "C:\\junk\\junk2\\run2.run";
    System.out.println( in_str + " translates to " + fixSeparator(in_str) );

    in_str = "C:\\\\junk\\\\junk2\\\\run2.run";
    System.out.println( in_str + " translates to " + fixSeparator(in_str) );

    in_str = "C:/junk/junk2/run2.run";
    System.out.println( in_str + " translates to " + fixSeparator(in_str) );

    in_str = "C:/junk\\\\junk2\\run2.run";
    System.out.println( in_str + " translates to " + fixSeparator(in_str) );

    in_str = "\\junk\\\\junk2\\run2.run";
    System.out.println( in_str + " translates to " + fixSeparator(in_str) );

    in_str = "\\junk\\\\junk2\\run2.run\\";
    System.out.println( in_str + " translates to " + fixSeparator(in_str) );

                                                  // test token replacement
    String equation = "x1*x*x+x2*x+x3";
    System.out.println("equation   = " + equation );
    String equation1 = replace_token( equation, "x", " t " );
    System.out.println("equation 1 = " + equation1 );
    String equation2 = replace_token( equation1, "x1", " a " );
    System.out.println("equation 2 = " + equation2 );
    String equation3 = replace_token( equation2, "x2", " b " );
    System.out.println("equation 3 = " + equation3 );
    String equation4 = replace_token( equation3, "x3", " c " );
    System.out.println("equation 4 = " + equation4 );

    System.out.println("Tokens in: X1, x2	total1;  ");
    String tokens[] = extract_tokens( "X1, x2	total1;  ", " ,;:\t\n\r\f");
    for ( int i = 0; i < tokens.length; i++ )
      System.out.println("|"+tokens[i]+"|");
  }
}
