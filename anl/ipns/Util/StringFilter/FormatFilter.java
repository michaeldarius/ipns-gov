/*
 * File: FormatFilter.java
 *
 * Copyright (C) 2003, Ruth Mikkelson
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
 * Contact : Ruth Mikkelson <mikkelsonr@uwstout.edu>
 *           Intense Pulsed Neutron Source Division
 *           Argonne National Laboratory
 *           9700 South Cass Avenue, Bldg 360
 *           Argonne, IL 60439-4845, USA
 *
 * This work was supported by the Intense Pulsed Neutron Source Division
 * of Argonne National Laboratory, Argonne, IL 60439-4845, USA.
 *
 * For further information, see <http://www.pns.anl.gov/ISAW/>
 *
 * Modified:
 *
 *  $Log$
 *  Revision 1.1  2003/10/27 14:58:52  rmikk
 *  A StringEntry Filter that only allows Fortran Format specifications
 *
 *  Revision 1.2  2002/11/27 23:23:49  pfpeterson
 *  standardized header
 *
 *  Revision 1.1  2002/06/06 16:03:13  pfpeterson
 *  Added to CVS.
 *
 *
 *
 */
 
package DataSetTools.util;

/**
 * Internal class to do all of the formatting checks and pass out
 * PropertChange events to listeners. Should only be used from within
 * the package.
 */
public class FormatFilter implements StringFilterer {
    

    public FormatFilter(){
       
    }
   

    public boolean isOkay(int offs, String inString, String curString){
       
        char[] source = inString.toCharArray();
        
        if( curString.length() < 1)
           return true;
        
          
        String SS = curString ;
        if("EeFfIi".indexOf(SS.charAt(0)) <0)
          return false;
        
        boolean decimal = false;
        boolean intt = false;
        if( "Ii".indexOf( SS.charAt(0)) >=0)
             intt = true;
        for( int i=1;i<SS.length(); i++)
           { char c = SS.charAt(i);
            
             if( Character.isDigit(c)){}
             else if( c=='.')
                if( decimal == true)
                  return false;
                else if( intt)
                   return false;
                else{
                   decimal = true;
                   
                }
           }
        
         return true;
         
    }

    public static int getWidth( String FormatString){
      if( FormatString == null)
        return -1;
      if( FormatString.length() < 2)
        return -1;
      int i= FormatString.indexOf('.');
      if( i < 0)
         i = FormatString.length();
      try
       {
         return (new Integer( FormatString.substring( 1,i))).intValue();
       }
      catch( Exception s)
        {
          return -1;
        }

    }

    public static int getDecimal( String FormatString){
        if( FormatString == null)
        return -1;
      if( FormatString.length() < 2)
        return -1;
      int i= FormatString.indexOf('.');
      if( i < 0)
         return -1;
      
      
      try
       {
         return (new Integer( FormatString.substring( i+1).trim())).intValue();
       }
      catch( Exception s)
        { 
          return -1;
        }
    }
    public String modifyString(int offs, String inString, String curString){
        return inString.toUpperCase();
    }

   public static void main( String args[]){
     FormatFilter F = new FormatFilter();
     System.out.println( F.isOkay(0, args[0], args[0]));
    

  }
}