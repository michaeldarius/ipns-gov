/*
 * File: IViewComponent2D.java
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
 *  Revision 1.12  2007/08/09 15:35:41  dennis
 *  Minor fix to javadocs.
 *
 *  Revision 1.11  2007/03/16 18:28:30  dennis
 *  Added method getWorldToArrayTransform() to interface, so that users
 *  of these 2D view components can easily get the mapping from
 *  world to array coordinates.
 *
 *  Revision 1.10  2004/03/15 23:53:53  dennis
 *  Removed unused imports, after factoring out the View components,
 *  Math and other utils.
 *
 *  Revision 1.9  2004/03/12 02:20:55  rmikk
 *  Fixed Package Names
 *
 *  Revision 1.8  2003/12/29 02:41:26  millermi
 *  - get/setPointedAt() now uses floatPoint2D to pass information
 *    about the current pointed at instead of java.awt.Point.
 *
 *  Revision 1.7  2003/10/21 21:57:09  millermi
 *  - Replaced the setPointedAt() and getPointedAt() methods since
 *    these cannot be factored out at the "upper" level.
 *
 *  Revision 1.6  2003/10/21 00:47:50  millermi
 *  - Now extends IViewComponent, thus requiring less
 *    methods to be outlined by this interface.
 *
 *  Revision 1.5  2003/10/16 16:01:26  millermi
 *  - Added method getPointedAt() to get the current point.
 *
 *  Revision 1.4  2003/08/11 23:44:24  millermi
 *  - Changed getSelectedSet() to getSelectedRegions() which now
 *    returns an array of Regions.
 *  - Changed setSelectedSet() to setSelectedRegions() which now
 *    takes parameter of type Region.
 *
 *  Revision 1.3  2003/05/22 13:04:56  dennis
 *  Added method to get the display panel.  Changed to pass ViewMenuItems
 *  rather than JMenuItems to be placed in Menu bar. (Mike Miller)
 *
 *  Revision 1.2  2003/05/16 14:59:53  dennis
 *  Added acknowlegement of NSF funding.
 *
 */
 
package gov.anl.ipns.ViewTools.Components.TwoD;

import gov.anl.ipns.Util.Numeric.floatPoint2D;
import gov.anl.ipns.ViewTools.Components.Region.Region; 
import gov.anl.ipns.ViewTools.Components.IViewComponent;
import gov.anl.ipns.ViewTools.Components.IVirtualArray2D;
import gov.anl.ipns.ViewTools.Panels.Transforms.CoordTransform;

/**
 * Any class that implements this interface will interpret and display
 * data in a graphical form. Examples include images, tables, and graphs.
 */
public interface IViewComponent2D extends IViewComponent
{  

 /**
  * This method is a notification to the view component that the selected
  * point has changed.
  *
  *  @param  fpt - current point as a floatPoint2D.
  */ 
  public void setPointedAt( floatPoint2D fpt );

 
 /**
  * This method returns the world coordinates of the currently
  * "pointed at" point.  It can be called to obtain the coordinates
  * in response to a POINTED_AT_CHANGED message.
  *
  *  @return The world coordinates of the current point as a floatPoint2D.
  */ 
  public floatPoint2D getPointedAt();


 /**
  * Set a list of selected regions for the view component.
  *
  *  @param  rgn - array of regions
  */ 
  public void setSelectedRegions( Region[] rgn );

 
 /**
  * Retrieve array of currently selected regions. 
  *
  *  @return The selected regions.
  */
  public Region[] getSelectedRegions();


 /**
  * Get a copy of the tranformation that maps world coordinates to array
  * (col,row) coordinates for this view component. 
  *
  * @return a CoordTransform object that maps from world coordinates
  *         to array (col,row) coordinates.
  */
  public CoordTransform getWorldToArrayTransform();

  
 /**
  * This method is invoked to notify the view component when the IVirtualArray
  * of data has changed. 
  *
  *  @param  v2D - virtual array of data
  */ 
  public void dataChanged(IVirtualArray2D v2D);

}
