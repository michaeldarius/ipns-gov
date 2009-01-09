/* 
 * File: SimpleExec.java
 *
 * Copyright (C) 2008, Dennis Mikkelson
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
 * This work was supported by the Spallation Neutron Source Division
 * of Oak Ridge National Laboratory, Oak Ridge, TN, USA.
 *
 *  Last Modified:
 * 
 *  $Author$
 *  $Date$            
 *  $Revision$
 */
package gov.anl.ipns.Util.Sys;

import java.io.*;


/**
 *  The Exec method of this class will simple execute the specified
 *  command and simply dump any errors, input or output.  This is intended to
 *  be used for simple operations like calling the logging facility at
 *  the SNS to log the start of a slurm job.  NOTE: The command must not
 *  generate a significant amount of output.
 */
public class SimpleExec 
{
  public static void Exec( String command )  
  {
    try
    {    
      Process process = Runtime.getRuntime().exec( command );

      String line;

      OutputStream process_out = process.getOutputStream();
      InputStream  process_in  = process.getInputStream();

      InputStream process_err = process.getErrorStream();
      InputStreamReader process_err_reader = new InputStreamReader(process_err);
      BufferedReader process_err_buff = new BufferedReader( process_err_reader);

      boolean first_time = true;
      while ((line = process_err_buff.readLine()) != null)
      {
        if ( first_time )
        {
          System.out.println("ERRORS FOR COMMAND: " + command );
          first_time = false;
        }
        System.out.println(line);
      }  
      try 
      {
        if (process.waitFor() != 0) 
          System.out.println("exit value = " + process.exitValue() );
      }
      catch (InterruptedException e) 
      {
        System.err.println(e);
      }

      process_out.close();
      process_in.close();

      process_err_buff.close();
      process_err_reader.close();
      process_err.close();

      process.destroy();                          // get rid of the process
    }
    catch( Exception ex )
    {
      System.out.println( "EXCEPTION executing command: " + command );
      ex.printStackTrace();
    }
  }

}