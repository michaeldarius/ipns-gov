/*
 * File:  Polygon.java
 *
 * Copyright (C) 2001, Dennis Mikkelson
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
 * $Log$
 * Revision 1.5  2004/03/12 01:12:41  dennis
 * Moved to ViewTools.Panels.ThreeD
 *
 * Revision 1.4  2003/10/02 19:35:08  dennis
 * If polygon is "solid" just do fillPolygon.  (Previously, the
 * border of the polygon was also drawn.  Separately drawing the
 * border is no longer needed for filled polygons.)
 *
 * Revision 1.3  2002/11/27 23:12:53  pfpeterson
 * standardized header
 *
 */

package gov.anl.ipns.ViewTools.Panels.ThreeD;

import java.awt.*;
import java.io.*;
import gov.anl.ipns.MathTools.Geometry.*;


/**
 *  This class represents a 3D polygon of one color.
 */
public class Polygon  extends     ThreeD_Object
                      implements  Serializable
{

public static final int HOLLOW = 0;
public static final int SOLID  = 1;
 
private int type = SOLID;

private static int[] xtemp;    // to avoid rebuilding temp arrays
private static int[] ytemp;    // everytime we draw, keep static arrays
                               // big enough to hold anything we've drawn 

  /** 
   *  Construct a polygon using the specified vertices and color.
   *
   *  @param verts  Array of 3D points giving the vertices of this polygon
   *                in order.
   *
   *  @param color  The color to use for this Polygon
   */
  
  public Polygon( Vector3D verts[], Color color )
  {
    super( verts, color );
    if ( xtemp == null || x.length > xtemp.length )
    {
      xtemp = new int[ x.length ];
      ytemp = new int[ y.length ];
    }
  }


  /**
   *  Specify whether the polygon should be drawn just using lines for it's
   *  edges, or if it should be filled. 
   *
   *  @param polygon_type  Specifies the type as HOLLOW or SOLID
   */
  public void setType( int polygon_type )
  {
    type = polygon_type;
  }

  /**
   *  Draw this Polygon using the projected 2D points in the specified
   *  graphics context g.
   *
   *  @param  g   The graphics object into which the Polygon is to be drawn.
   */

  public void Draw( Graphics g )
  {
     if ( clipped )
       return;

     int max_dist = 0;          // find the maximum x,y distance to x[0],y[0]
     int dx,                    // so that if the polygon is extremely small
         dy;                    // we can draw as a point, or just the border

     xtemp[0] = (int)x[0];
     ytemp[0] = (int)y[0];
     for ( int i = 1; i < x.length; i++ )
     {
       xtemp[i] = (int)x[i];
       ytemp[i] = (int)y[i];

       dx = Math.abs(xtemp[i] - xtemp[0]);
       dy = Math.abs(ytemp[i] - ytemp[0]);
       if ( dx > max_dist )
         max_dist = dx;

       if ( dy > max_dist )
         max_dist = dy;
     }

     g.setColor( color );

     if ( max_dist <= 0 )                                     // draw "point"
       g.drawLine( xtemp[0], ytemp[0], xtemp[0], ytemp[0] );

     else if ( max_dist <= 1 )                                // draw border
       g.drawPolygon( xtemp, ytemp, x.length );

     else
     {
       if ( type == SOLID )
         g.fillPolygon( xtemp, ytemp, x.length );      
       else
         g.drawPolygon( xtemp, ytemp, x.length ); 
     }
  }

}
