/*
 * File:  ViewController.java
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
 * Revision 1.7  2004/03/15 23:53:55  dennis
 * Removed unused imports, after factoring out the View components,
 * Math and other utils.
 *
 * Revision 1.6  2004/03/12 01:29:16  dennis
 * Moved to ViewTools.Panels.ThreeD package
 *
 * Revision 1.5  2002/11/27 23:12:53  pfpeterson
 * standardized header
 *
 */

package gov.anl.ipns.ViewTools.Panels.ThreeD;

import java.io.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;

import gov.anl.ipns.MathTools.Geometry.*;

/**
 *  A ViewController object controls the ViewTransform for one or more
 *  ThreeD_JPanel objects.  Derived classes implement various interfaces to
 *  allow the user to modify the transform.
 */ 

public class ViewController extends    JPanel
                            implements Serializable
{
  public static final String VIEW_CHANGED = "View Changed";

  private Vector   listeners  = null;

  private float    height,
                   width;
  private float    view_angle = -1;

  private Vector3D vrp,
                   cop,
                   vuv;

  private Vector   panel3D;


/* ------------------------- Default Constructor -------------------------- */
/**
 *  Construct a default ViewController, initially viewing the origin from
 *  (1,1,1) with a virtual screen height and width of 1. 
 */
  public ViewController()
  { 
    listeners = new Vector();

    height  = 1;
    width   = 1;
    vrp     = new Vector3D( 0, 0, 0 );
    cop     = new Vector3D( 1, 1, 1 );
    vuv     = new Vector3D( 0, 0, 1 );

    panel3D = new Vector();
  }

/* ------------------------------ Constructor ----------------------------- */
/**
 *  Construct a ViewController with the specified COP, VRP and VUV
 */
  public ViewController( Vector3D cop, Vector3D vrp, Vector3D vuv )
  {
    this();
    this.vrp = new Vector3D( vrp );
    this.cop = new Vector3D( cop );
    this.vuv = new Vector3D( vuv );
  }


/* -------------------------- addControlledPanel ------------------------- */ 
/**
 *  Add the specified 3D panel to the list of panels whose view is controlled
 *  by this controller.
 *
 *  @param panel  The new panel that this controller should control.
 */
 public void addControlledPanel( ThreeD_JPanel panel )
 {
   for ( int i = 0; i < panel3D.size(); i++ )         // don't add it if it's 
     if ( panel3D.elementAt( i ).equals( panel ) )    // already there
       return;

   panel3D.add( panel );
 }

/* -------------------------------- setCOP ------------------------------- */
/**
 *   Set the observer's position (i.e. the Center of Projection ) for this view.
 *
 *   @param  cop  The new vector to use for the observer's position.  THIS MUST
 *                BE DIFFERENT FROM THE VRP WHEN THE apply() METHOD IS CALLED.
 */
 protected void setCOP( Vector3D cop )
 {
   this.cop = new Vector3D( cop );
 }

/* -------------------------------- setVRP ------------------------------- */
/**
 *   Set the point the observer is looking at (i.e. the View Reference Point )
 *   for this view.
 *
 *   @param  vrp  The new vector to use for the point the observer is looking
 *                at.  THIS MUST BE DIFFERENT FROM THE COP WHEN THE apply() 
 *                METHOD IS CALLED.
 */
  protected void setVRP( Vector3D vrp )
  {
    this.vrp = new Vector3D( vrp );
  }

/* -------------------------------- setVUV ------------------------------- */
/**
 *   Set the direction that is "up" from the observer's point of view
 *   (i.e. the View Up Vector ) for this view.
 *
 *   @param  vuv  The new vector to use for the view up vector.  THIS MUST
 *                NOT BE IN THE SAME DIRECTION AS THE DIFFERENCE (cop-vrp)
 *                WHEN THE apply() METHOD IS CALLED.
 */
  protected void setVUV( Vector3D vuv )
  {
    this.vuv = new Vector3D( vuv );
  }

/* -------------------------------- getCOP ------------------------------- */
/**
 *   Get the observer's position (i.e. the Center of Projection ) for this view. *
 *   @return  The the observer's position.  
 */
  public Vector3D getCOP( )
  {
    return new Vector3D( cop );
  }

/* -------------------------------- getVRP ------------------------------- */
/**
 *   Get the point the observer is looking at (i.e. the View Reference Point )
 *   for this view.
 *
 *   @return  The point the observer is looking at.
 */
  public Vector3D getVRP()
  {
    return new Vector3D( vrp );
  }

/* -------------------------------- getVUV ------------------------------- */
/**
 *   Get the direction that is "up" from the observer's point of view
 *   (i.e. the View Up Vector ) for this view.
 *
 *   @return  The the view up vector. 
 */
  public Vector3D getVUV()
  {
    return new Vector3D( vuv );
  }


/* ------------------------- setVirtualScreenSize ------------------------ */
/**
 *  Set the size of the virtual screen onto which the objects are projected.
 *  The virtual screen is a 2D rectangle centered at the "view reference point"
 *  perpendicular to the line of sight, with the specified width and height.
 *
 *  NOTE: IF setViewAngle() has been called with a degrees value > 0, this
 *        method will no longer have any effect, since the width and height
 *        of the virtual screen will be calculated based on the view angle. 
 *
 *  @param  width  The width of the virtual viewing screen.
 *  @param  height The height of the virtual viewing screen.
 *
 */
  public void setVirtualScreenSize( float width, float height )
  {
    if ( view_angle > 0 )                     // use the view_angle to calculate
      return;                                 // the width and height instead.

                                              // don't allow negative sizes
    this.width  = Math.abs( width );
    this.height = Math.abs( height );

    if ( width == 0 )                         // don't allow degenerate sizes
      width = 1;

    if ( height == 0 )
      height = 1;
  }


/* ------------------------- setViewAngle ------------------------ */
/**
 *  Set the angle ( in the vertical dirction ) that will be subtended 
 *  by the virtual screen on which the objects are projected.  Calling
 *  this method causes the width and height of the virtual screen to be
 *  adjusted dynamically as the COP and VRP are changed.  
 *
 *  NOTE:  To disable this and control the size of the virtual screen 
 *         directly, call setViewAngle with a negative value for the degrees,
 *         the call setVirtualScreenSize() to set new values for the width 
 *         and height directly.
 *
 *  @param degrees The angle subtended by the virtual viewing screen in
 *                 the vertical direction.   
 *
 */
  public void setViewAngle( float degrees )
  {
    if ( degrees <= 0 )
    {
      view_angle = -1;
      return;
    }

    view_angle = (float)(degrees * Math.PI / 180.0);

    set_screen_size_from_view_angle();
  }


/* ------------------------------- apply --------------------------------- */
/**
 *   This method will apply the new viewing information to each of the 3D 
 *   panels it controls and then repaint each of the panels.
 *
 *   @param  reset_zoom   Flag indicating whether or not to reset the local
 *                       "zoomed" transform when the new viewing information
 *                        is applied.
 *
 */  
  public void apply( boolean reset_zoom )
  {
    int n_panels = panel3D.size();
    if ( n_panels <= 0 )
      return;

    ViewingTran3D tran = new ViewingTran3D( cop, vrp, vuv, true );
    set_screen_size_from_view_angle();

    for ( int i = 0; i < n_panels; i++ )
    {
      ThreeD_JPanel panel = (ThreeD_JPanel)panel3D.elementAt( i );
      panel.setVirtualScreenSize( width, height, reset_zoom );
      panel.setViewTran( tran );
      panel.repaint();
    } 
                                                    // send action event to
    for ( int i = 0; i < listeners.size(); i++ )    // all of the listeners
    {
      ActionListener listener = (ActionListener)listeners.elementAt(i);
      listener.actionPerformed( new ActionEvent( this, 0, VIEW_CHANGED ) );
    }
  }


 /* ------------------------ addActionListener -------------------------- */
 /**
  *  Add an ActionListener for this ViewController.  Whenever the apply 
  *  method is invoked to repaint the controlled panels, the action listeners
  *  will be notified.
  *
  *  @param listener  An ActionListener whose ActionPerformed() method is
  *                   to be called when the ViewController values are 
  *                   apllied to redraw the ThreeD_JPanels.
  */

  public void addActionListener( ActionListener listener )
  {
    for ( int i = 0; i < listeners.size(); i++ )       // don't add it if it's
      if ( listeners.elementAt(i).equals( listener ) ) // already there
        return;

    listeners.add( listener );
  }


/* -------------------------------------------------------------------------
 *
 *  PRIVATE METHODS
 *
 */

  private void set_screen_size_from_view_angle()
  {
    Vector3D  diff_vector = new Vector3D( cop );
    diff_vector.subtract( vrp );
    float distance = diff_vector.length();
    if ( distance > 0 )
      width  = (float)( 2 * distance * Math.tan( view_angle/2 ) );

    height = width;
  }

}
