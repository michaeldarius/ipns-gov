/*
 * File: IViewComponent.java
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
 *  Revision 1.7  2007/06/12 20:31:07  rmikk
 *  Redefined POINTED_AT_CHANGED to correspond to IObserver's POINTED_AT
 *     _CHANGED
 *
 *  Revision 1.6  2004/03/19 20:27:46  millermi
 *  - Now extends IPreserveState, thus requiring all view components
 *    to have methods get/setObjectState().
 *
 *  Revision 1.5  2004/03/15 23:53:50  dennis
 *  Removed unused imports, after factoring out the View components,
 *  Math and other utils.
 *
 *  Revision 1.4  2004/03/12 01:50:19  rmikk
 *  Fixed package names
 *
 *  Revision 1.3  2004/03/10 23:37:27  millermi
 *  - Changed IViewComponent interface, no longer
 *    distinguish between private and shared controls/
 *    menu items.
 *  - Combined private and shared controls/menu items.
 *
 *  Revision 1.2  2003/10/21 21:58:40  millermi
 *  - Removed setPointedAt() and getPointedAt() since not
 *    all extending interfaces will use these methods in
 *    the same way.
 *
 *  Revision 1.1  2003/10/21 00:46:42  millermi
 *  - Initial Version - Factored out common functionality
 *    between all view component interfaces into this
 *    top-level interface.
 *
 */
 
package gov.anl.ipns.ViewTools.Components;

import javax.swing.JPanel;
import java.awt.event.ActionListener;

import gov.anl.ipns.ViewTools.Components.Menu.ViewMenuItem;
import gov.anl.ipns.ViewTools.Components.ViewControls.*;
import gov.anl.ipns.Util.Messaging.*;

/**
 * Any class that implements this interface will interpret and display
 * data in a usable form. Examples include images, tables, and graphs.
 */
public interface IViewComponent extends IPreserveState
{
 /*
  * These variables are messaging strings for use by action listeners.
  */
 /**
  * "POINTED_AT_CHANGED" - this message String is used by view components
  * to inform listeners that the current point has changed.
  */
  public static final String POINTED_AT_CHANGED = IObserver.POINTED_AT_CHANGED;
  
 /**
  * "SELECTED_CHANGED" - this message String is used by view components
  * to inform listeners that the current selected region has changed.
  */
  public static final String SELECTED_CHANGED = "SELECTED_CHANGED";
  
 /**
  * This method is invoked to notify the view component when the data
  * has changed.
  */ 
  public void dataChanged();
  
 /**
  * Add a listener to this view component. A listener will be notified
  * when a selected point or region changes on the view component.
  */
  public void addActionListener( ActionListener act_listener );
  
 /**
  * Remove a specified listener from this view component.
  */ 
  public void removeActionListener( ActionListener act_listener );
 
 /**
  * Remove all listeners from this view component.
  */ 
  public void removeAllActionListeners();

 /**
  * Retrieve the jpanel that this component constructs.  
  */
  public JPanel getDisplayPanel();
 
 /**
  * Return controls needed by the component.
  */ 
  public ViewControl[] getControls();
  
 /**
  * Return view menu items needed by the component.
  */   
  public ViewMenuItem[] getMenuItems();
  
 /**
  * This method is called by the viewer to inform the view component
  * it is no longer needed. In turn, the view component closes all windows
  * created by it before closing.
  */
  public void kill();	
}

