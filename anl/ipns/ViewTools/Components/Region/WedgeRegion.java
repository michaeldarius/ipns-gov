/*
 * File: WedgeRegion.java
 *
 * Copyright (C) 2003, Mike Miller
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
 * Primary   Mike Miller <millermi@uwstout.edu>
 * Contact:  Student Developer, University of Wisconsin-Stout
 *           
 * Contact : Dennis Mikkelson <mikkelsond@uwstout.edu>
 *           Department of Mathematics, Statistics and Computer Science
 *           University of Wisconsin-Stout
 *           Menomonie, WI 54751, USA
 *
 * This work was supported by the National Science Foundation under grant
 * number DMR-0218882, and by the Intense Pulsed Neutron Source Division
 * of Argonne National Laboratory, Argonne, IL 60439-4845, USA.
 *
 * For further information, see <http://www.pns.anl.gov/ISAW/>
 *
 * Modified:
 *
 *  $Log$
 *  Revision 1.4  2003/12/12 06:11:44  millermi
 *  - Completely renovated how the points are selected.
 *    Previously slope was used to restrict points, now
 *    the angle of the point is calculated and compared
 *    with the starting/ending angles of the arc.
 *  - This class now assumes the arc will be drawn
 *    counterclockwise, so the starting angle may be
 *    larger than the stopping angle if the 4th to
 *    1st quadrant selection is made.
 *
 *  Revision 1.3  2003/10/22 20:26:09  millermi
 *  - Fixed java doc errors.
 *
 *  Revision 1.2  2003/08/21 22:41:08  millermi
 *  - Commented out debug statements and removed out code
 *    that was commented out.
 *
 *  Revision 1.1  2003/08/21 18:21:36  millermi
 *  - Initial Version - uses equation for ellipse and slope of a line to
 *    determine if a point is in the region.
 *
 */ 
package DataSetTools.components.View.Region;

import java.awt.Point;
import java.util.Vector;
 
import DataSetTools.util.floatPoint2D;
import DataSetTools.components.View.Cursor.SelectionJPanel;

/**
 * This class is a specific region designated by three points.
 * The WedgeRegion is used to pass points selected by a
 * wedge region (in SelectionOverlay) from the view component
 * to the viewer. Given the defining points of a region,
 * this class can return all of the points inside the selected region. 
 * The defining points of the wedge are:
 * 
 * p[0]   = center pt of circle that arc is taken from
 * p[1]   = last mouse point/point at intersection of line and arc
 * p[2]   = reflection of p[1]
 * p[3]   = top left corner of bounding box around arc's total circle
 * p[4]   = bottom right corner of bounding box around arc's circle
 * p[5].x = startangle, the directional vector in degrees
 * p[5].y = degrees covered by arc.
 *
 * The large number of defining points replaces the work of recalculating
 * information at each step of the region calculation. Like a EllipseRegion,
 * this region may appear to be circular, but may actually be elliptical.
 */ 
public class WedgeRegion extends Region
{
  /**
   * Constructor - uses Region's constructor to set the defining points.
   * The defining points are assumed to be in image values, where
   * the input points are in (x,y) where (x = col, y = row ) form.
   * The only exception is definingpoint[5] which holds angular (in degrees)
   * values.
   *
   *  @param  dp - defining points of the wedge
   */ 
   public WedgeRegion( Point[] dp )
   {
     super(dp);
   }
   
  /**
   * Get all of the points inside the wedge region. This method first determines
   * if the point is in the ellipse, then uses angles to find if the points
   * in the wedge region. 
   *
   *  @return array of points included within the wedge region.
   */
   public Point[] getSelectedPoints()
   { 
    /* p[0]   = center pt of circle that arc is taken from
     * p[1]   = last mouse point/point at intersection of line and arc
     * p[2]   = reflection of p[1]
     * p[3]   = top left corner of bounding box around arc's total circle
     * p[4]   = bottom right corner of bounding box around arc's circle
     * p[5].x = startangle, the directional vector in degrees
     * p[5].y = degrees covered by arc.
     */
     Point center = new Point( definingpoints[0] );
     Point p1 = new Point( definingpoints[1] );
     Point rp1 = new Point( definingpoints[2] );   // reflection of p1
     Point topleft = new Point( definingpoints[3] );
     Point bottomright = new Point( definingpoints[4] );
     double xextent = (double)(bottomright.x - topleft.x)/2;
     double yextent = (double)(bottomright.y - topleft.y)/2;
     floatPoint2D fcenter = new floatPoint2D( (float)(topleft.x + xextent),
                               (float)(topleft.y + yextent) );     
     Vector points = new Vector();
     
     int startangle = definingpoints[5].x;
     int totalangle = definingpoints[5].y + startangle;
     int stopangle = totalangle;
     int p1quad = 0;  
     // using the startangle, find the quadrant of p1
     if( startangle <= 180 )
     {
       if( startangle <= 90 )
       {
         p1quad =  1;
       }
       else
       {
         p1quad =  2;
       }
     }
     else
     {
       if( startangle < 270 )
       {
         p1quad =  3;
       }
       else
       {
         p1quad =  4;
       }
     } 
     // make sure angle is between 0 and 360  
     if( totalangle >= 360 )
       totalangle -= 360;
     //System.out.println("Total: " + totalangle );
     int rp1quad = 0;
     // using the startangle + arcangle, find quadrant of reflection of p1
     if( totalangle <= 180 )
     {
       if( totalangle <= 90 )
       {
         rp1quad =  1;
       }
       else
       {
         rp1quad =  2;
       }
     }
     else
     {
       if( totalangle < 270 )
       {
         rp1quad =  3;
       }
       else
       {
         rp1quad =  4;
       }
     }
     //System.out.println("p1/rp1 Quad: " + p1quad + "/" + rp1quad );
     
     // using formula for ellipse: (x-h)^2/a^2 + (y-k)^2/b^2 = 1
     // where x,y is point, (h,k) is center, and a,b are x/y extent (radius)
     int quadnum = 0;
     // these values are specific to each quadrant, once quadrant is know, set
     // these values.
     int ystart = 0;
     int ystop = 0;
     int xstart = 0;
     int xstop = 0;
     
     // if rp1quad < p1quad, angle goes from 4th quad to 1st quad. so
     // adjust rp1quad so it works as a ending bound for the for loop.
     if( rp1quad < p1quad )
       rp1quad += 4;
     // if the two are in the same quad, but the total angle is greater than 90,
     // the shape must be a pie with a slice removed.
     else if( rp1quad == p1quad && definingpoints[5].y > 90 )
       rp1quad += 3;
     //System.out.println("p1quad/rp1quad: " + p1quad + "/" + rp1quad );
     // Step through each quadrant involved in the selection.
     for( int quadcount = p1quad; quadcount <= rp1quad; quadcount++ )
     {
       // since rp1quad could be > 4, restrict the quadnum to max of 4.
       if(quadcount > 4 )
     	 quadnum = quadcount - 4;
       else
         quadnum = quadcount;
       
       // Depending on which quadrant, set the bounds for the two for loops
       // below which will step through all the points in that quadrant
       // and add ones contained in the selection.
       // Since y values go top to bottom, the ystart and stop are switched
       // and since quad 2 & 3 x values should go right to left, those xstart
       // and stop are switched.
       if( quadnum == 1 )
       {
         ystart = center.y;
         ystop  = topleft.y;
         xstart = center.x;
         xstop  = bottomright.x;
       }
       else if( quadnum == 2 )
       {
         ystart = center.y;
         ystop  = topleft.y;
         xstart = topleft.x;
         xstop  = center.x - 1;
       }
       else if( quadnum == 3 )
       {
         ystart = bottomright.y;
         ystop  = center.y + 1;
         xstart = topleft.x;
         xstop  = center.x - 1;
       }
       else if( quadnum == 4 )
       {
         ystart = bottomright.y;
         ystop  = center.y + 1;
         xstart = center.x;
         xstop  = bottomright.x;
       }
       //System.out.println("Current quad(c): " + quadnum );
       double dist = 0;
       double xdiff = 0;
       double ydiff = 0;
       // These two loops will check every point within the given quadrant
       // and test to see if the point is in the ellipse from which the
       // arc comes from.
       for( int y = ystart; y >= ystop; y-- )
       {
         for( int x = xstart; x <= xstop; x++ )
         {
     	   xdiff = 0;
           ydiff = 0;
           // x/y diff represent x-h/y-k respectively
     	   xdiff = Math.abs( (double)x - fcenter.x );
     	   ydiff = Math.abs( (double)y - fcenter.y );
           // Subtracting 1/(xextent*4) is to account for fractional pixels.
           // This will give a smoother, more accurate selected region.
     	   dist = Math.pow((xdiff - 1/(xextent*4)),2)/Math.pow(xextent,2) + 
        	  Math.pow((ydiff - 1/(yextent*4)),2)/Math.pow(yextent,2);
     	   //System.out.println("(" + x + "," + y + ")..." + dist ); 
     	   // Using ellipse equation, the distance must be < 1 in order to
	   // be contained within the ellipse.
	   if( dist <= 1 )
     	   {
	     int pointangle  = -Math.round( (float)Math.toDegrees(Math.atan2( 
                                                    (double)(y - fcenter.y),
                                                    (double)(x - fcenter.x))));
             // put everything from 0-360
             if( pointangle < 0 )
               pointangle = 360 + pointangle;
             
     	    //System.out.println("Point/Stop: " + pointangle + "/" + stopangle);
	     // if stopangle >= 360, the angle goes from 4th to 1st quadrant,
	     // thus start - 360, and 0 - stop becomes the interval.
	     if( stopangle >= 360 )
	     {
	       if( pointangle >= startangle || pointangle <= stopangle - 360 )
        	 points.add( new Point( x, y ) );
	     }
	     // otherwise the angle must be between the start and stop angle.
	     else
	     {
	       if( pointangle >= startangle && pointangle <= stopangle )
        	 points.add( new Point( x, y ) );
	     }
     	   } // if( dist < 1 )
     	 } // end for x
       } // end for y
     } // for quad
     
     // put the vector of points into an array of points
     selectedpoints = new Point[points.size()];
     for( int i = 0; i < points.size(); i++ )
         selectedpoints[i] = (Point)points.elementAt(i);
     return selectedpoints;     
   }
}
