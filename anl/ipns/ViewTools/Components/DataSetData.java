
/*
 * File:  DataSetData.java
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
 * Contact :  Ruth Mikkelson <mikkelsonr@uwstout.edu>
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
 * $Log$
 * Revision 1.12  2003/12/18 22:42:12  millermi
 * - This file was involved in generalizing AxisInfo2D to
 *   AxisInfo. This change was made so that the AxisInfo
 *   class can be used for more than just 2D axes.
 *
 * Revision 1.11  2003/12/16 23:19:01  dennis
 * Removed commented out code that returned controls.
 *
 * Revision 1.10  2003/12/16 23:17:07  dennis
 * Removed methods to get controls.  Combined methods to set x and y
 * values into one method.
 *
 * Revision 1.9  2003/11/21 15:33:33  rmikk
 * Notified all listeners when a new data set is set
 *
 * Revision 1.8  2003/11/21 14:52:15  rmikk
 * Add GPL
 * Added the setDataSet method
 * Implemented ActionListeners
 *
 */

package DataSetTools.components.View;
import DataSetTools.dataset.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;


public class DataSetData implements IVirtualArray1D
  {
     private DataSet ds;
  
 
   
   int[]  selectedInd ;
   float maxy;
   float minx;
   float maxx;
   float miny;
   boolean is_x_linear = true;
   boolean is_y_linear = true;
   

   public DataSetData( DataSet DS)
     {
       ds = DS;
      
       selectedInd = ds.getSelectedIndices();
       minx=maxx=miny=maxy= Float.NaN;
  
     }

   /**
     *  Change the DataSet being viewed to the specified DataSet.  Derived
     *  classes should override this and take what additional steps are needed
     *  to change the specific viewer to the deal with the new DataSet.
     *
     *  @param  ds  The new DataSet to be viewed
     */
    public void setDataSet( DataSet ds )
    { 
       this.ds = ds;
       selectedInd = ds.getSelectedIndices();
       minx=maxx=miny=maxy= Float.NaN;
       notifyAllListeners("DataChanged");
    }
    
   public AxisInfo getAxisInfo( int axis)
     {
      
      if( axis == AxisInfo.X_AXIS )
         return  new AxisInfo(findminX(), findmaxX(), 
               ds.getX_label(), ds.getX_units(), is_x_linear);
      else

         return  new AxisInfo(findminY(), findmaxY(), 
               ds.getY_label(), ds.getY_units(), is_y_linear);
     }


   
   private float findminX()
     { 
       if( !Float.isNaN(minx))
          return minx;
       
        float [] xvals;
        minx = 0;
        maxx = 1;
        xvals = getXValues(0);
        if (xvals != null)
        {
 	 minx =  xvals[0];
         maxx =  xvals[0];
        } 
	for (int line=0; line < getNumlines(); line++)
        {xvals = getXValues(line);
           for (int i=1; i < getNumPoints(line); i++)
	   {
	      if (xvals[i] < minx)
	   	minx = xvals[i];
	      if (xvals[i] > maxx)
		maxx = xvals[i];
	   }
         }
        return minx;
     }

  private float findmaxX()
    {
     findminX();
     return maxx;
    }
   private float findminY()
     {
       if( !Float.isNaN(miny))
          return miny;

	float [] yvals;
        miny = 0;
        maxy = 1;

	yvals = getYValues(0);
        if( yvals != null)
        {
          miny =  yvals[0];
          maxy =  yvals[0];
        }
	for (int line=0; line < getNumlines(); line++)
	{ yvals = getYValues(line);
           for (int i=1; i < yvals.length; i++)
	   {
	      if (yvals[i] < miny)
	   	miny = yvals[i];
	      if (yvals[i] > maxy)
		maxy = yvals[i];
	   }
	}
        return miny;
     }

 
  private float findmaxY()
    {
     findminY();
     return maxy;
    }

     
   public String getTitle()
     {
       return ds.getTitle();
      }

  public void setTitle( String title )
     {
         
     }

  public float[] getXValues( int line_number )
    {     
      if( line_number < 0)
        return null;
      if( line_number >= getNumlines())
        return null;

      float[] x = ds.getData_entry( selectedInd[line_number]).getX_scale().
                  getXs();
      return x;

    }

  public void setXYValues( float[] x_values, 
                           float[] y_values,
                           float[] errors,
                           int     group_id,
                           int     line_number )
    {
      System.out.println("DataSetData.setXYValues() is just a stub");
    }




  public float[] getYValues( int line_number )
    {
      if( line_number < 0)
        return null;
      if( line_number >= getNumlines())
        return null;

      float[] y = ds.getData_entry( selectedInd[line_number]).getY_values();
      return y;
    }


  public float[] getXVals_ofIndex(int index)
  {
     if( index < 0)
        return null;
     if( index >= getNumGraphs())
        return null;
     return ds.getData_entry(index).getX_values();
  }
  public float[] getYVals_ofIndex(int index)
  {
     if( index < 0)
        return null;
     if( index >= getNumGraphs())
        return null;
     return ds.getData_entry(index).getY_values();
  }
    

  public float [] getErrorValues( int line_number )
  {
     if( line_number < 0)
        return null;
     if( line_number >= getNumlines())
        return null;
     return ds.getData_entry( selectedInd[line_number]).getErrors( );
  }
  
  public float[] getErrorVals_ofIndex(int index)
  {
     if( index < 0)
        return null;
     if( index >= getNumGraphs())
        return null;
     return ds.getData_entry( index ).getErrors( );
  }

  public int getGroupID( int line_number )
  {
     if( line_number < 0)
        return 0;
     if( line_number >= getNumlines())
        return 0;
     return ds.getData_entry( selectedInd[line_number]).getGroup_ID( );
  }

  public int getPointedAtGraph()
  {
     return ds.getPointedAtIndex();
  }

  public int[] getSelectedGraphs()
  {
     return selectedInd;
  }

  public boolean isSelected(int index) 
  {
     return ds.isSelected(index);
  }
  
  public int getNumGraphs()
  {
     return ds.getNum_entries();
  }

  public void setAllValues( float value )
  { 

  }

  public void set_x_linear(boolean isLinear) 
  {
    is_x_linear = isLinear;
  }
  
  public void set_y_linear(boolean isLinear) 
  {
    is_y_linear = isLinear;
  }

  
 /** Returns the number of x values in the line line_number
 */
  public int getNumPoints( int line_number)
  {
    if( line_number < 0)
         return 0;
    if( line_number >= getNumlines())
        return 0;
    return ds.getData_entry( selectedInd[ line_number]).getX_scale().getNum_x();

  }

  public int getNumlines()
    { 
      if( selectedInd == null)
         return 0;
      return selectedInd.length;
    }

   
   Vector ActListeners = new Vector();
   public void addActionListener( ActionListener listener)
    {
       if( ActListeners.indexOf( listener) ==-1)
         ActListeners.addElement( listener);
    }
   public void removeActionListener( ActionListener listener)
    {
       ActListeners.remove( listener);
    }
   public void removeAllActionListeners()
    {
       ActListeners.clear();
    }
    private void notifyAllListeners( String evtCommand){
       ActionEvent evt = new ActionEvent(this,
               ActionEvent.ACTION_PERFORMED, evtCommand);

       for( int i=0; i< ActListeners.size(); i++)
          ((ActionListener)ActListeners.elementAt(i)).
                actionPerformed( evt);
         

      
    }

  }
