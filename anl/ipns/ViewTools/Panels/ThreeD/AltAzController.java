/*
 * File:  AltAzController.java
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
 * $Log$
 * Revision 1.5  2001/07/04 15:27:18  dennis
 * Angle sliders border and includes "angle" and degrees symbol.
 *
 * Revision 1.4  2001/07/03 21:25:38  dennis
 * Added digital readout of degrees & distance on sliders.
 *
 * Revision 1.3  2001/06/28 20:24:27  dennis
 * Uses new form of ThreeD_JPanel with named lists of
 * 3D objects.
 *
 * Revision 1.2  2001/05/29 14:58:54  dennis
 * Now complete and documented
 *
 * Revision 1.1  2001/05/23 17:36:35  dennis
 * Control view matrix using sliders that adjust the
 * altitude angle, azimuthal angle and distance from the
 * VRP to the COP.
 *
 *
 */

package DataSetTools.components.ThreeD;

import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import DataSetTools.math.*;
import DataSetTools.util.*;

/**
 *  A ViewController object controls the ViewTransform for one or more
 *  ThreeD_JPanel objects.  Derived classes implement various interfaces to
 *  allow the user to modify the transform.
 */ 

public class AltAzController extends    ViewController
                             implements Serializable
{
  public static final int   MAX_ALT_ANGLE         = 89;
  public static final float DISTANCE_SCALE_FACTOR = 10.0f;

  JSlider  azimuth_slider;
  JSlider  altitude_slider;
  JSlider  distance_slider;


  public AltAzController()
  {
    this( 45, 45, 1, 20, 10 );
  }


  public AltAzController( float altitude, 
                          float azimuth, 
                          float min_distance,
                          float max_distance,
                          float distance  )
  {
    setLayout( new GridLayout(3,1) );
    TitledBorder border = new TitledBorder(
                             LineBorder.createBlackLineBorder(),"View Control");
    border.setTitleFont( FontUtil.BORDER_FONT );
    setBorder( border );

    altitude_slider = new JSlider( JSlider.HORIZONTAL, 
                                  -MAX_ALT_ANGLE, 
                                   MAX_ALT_ANGLE, 
                                   0 );
    altitude_slider.addChangeListener( new SliderChanged() );
    border = new TitledBorder( LineBorder.createBlackLineBorder(),"Altitude");
    border.setTitleFont( FontUtil.BORDER_FONT );
    altitude_slider.setBorder( border );
    add( altitude_slider ); 

    azimuth_slider = new JSlider( JSlider.HORIZONTAL, -180, 180, 0 );
    azimuth_slider.addChangeListener( new SliderChanged() );
    border = new TitledBorder( LineBorder.createBlackLineBorder(),"Azimuth");
    border.setTitleFont( FontUtil.BORDER_FONT );
    azimuth_slider.setBorder( border );
    add( azimuth_slider ); 

    distance_slider = new JSlider( JSlider.HORIZONTAL, 1, 20, 10 );
    distance_slider.addChangeListener( new SliderChanged() );
    border = new TitledBorder( LineBorder.createBlackLineBorder(),"Distance");
    border.setTitleFont( FontUtil.BORDER_FONT );
    distance_slider.setBorder( border );
    add( distance_slider ); 

    setDistanceRange( min_distance, max_distance );
    setDistance( distance );
    setAltitudeAngle( altitude );
    setAzimuthAngle( azimuth );

    setView( true );
  }

  public void setDistanceRange( float min_distance, float max_distance )
  {
    if ( min_distance > max_distance )
    {
      float temp   = min_distance;
      min_distance = max_distance;
      max_distance = temp;
    }

    if ( min_distance == max_distance )
      max_distance = min_distance + 1;

    distance_slider.setMinimum( (int)(DISTANCE_SCALE_FACTOR * min_distance) );
    distance_slider.setMaximum( (int)(DISTANCE_SCALE_FACTOR * max_distance) );
  }


  public void setDistance( float distance )
  {
    distance = Math.abs(distance);
    if ( distance == 0 )
      distance = 1;
    distance_slider.setValue( (int)(DISTANCE_SCALE_FACTOR * distance) );
  }

  public void setAzimuthAngle( float degrees )
  {
    if ( degrees > 180 )
      degrees = 180;
    else if ( degrees < -180 )
      degrees = -180;

    azimuth_slider.setValue( (int)degrees );
  }

  public void setAltitudeAngle( float degrees )
  {
    if ( degrees > MAX_ALT_ANGLE )
       degrees = MAX_ALT_ANGLE;
    else if ( degrees < -MAX_ALT_ANGLE )
       degrees = -MAX_ALT_ANGLE;

    altitude_slider.setValue( (int)degrees );
  }

/* -------------------------------------------------------------------------
 *
 *  PRIVATE METHODS 
 *
 */

/**
 *  Calculate the new COP and apply the view controller to change the view
 *  for all of the controlled panels.
 *
 *  @param  reset_zoom   Flag indicating whether or not to reset the local
 *                      "zoomed" transform as well as the global transform.
 */

 private void setView( boolean reset_zoom )
 {
   float azimuth  = azimuth_slider.getValue();
   float altitude = altitude_slider.getValue();
   float distance = distance_slider.getValue()/DISTANCE_SCALE_FACTOR;

   float r = (float)(distance * Math.cos( altitude * Math.PI/180.0 ));

   float x = (float)(r * Math.cos( azimuth * Math.PI/180.0 ));
   float y = (float)(r * Math.sin( azimuth * Math.PI/180.0 ));
   float z = (float)(distance * Math.sin( altitude * Math.PI/180.0 ));

   float vrp[] = getVRP().get();
 
   setCOP( new Vector3D( x-vrp[0], y-vrp[0], z-vrp[0] ) );

   apply( reset_zoom );
 }
 

/* --------------------------------------------------------------------------
 *
 *  INTERNAL CLASSES 
 * 
 */
  private class SliderChanged    implements ChangeListener,
                                            Serializable
  {
     public void stateChanged( ChangeEvent e )
     {
       JSlider slider = (JSlider)e.getSource();

       TitledBorder border = (TitledBorder)slider.getBorder();
       if ( slider.equals( azimuth_slider ))
         border.setTitle( "Azimuth \u2220 " + slider.getValue() + "\u00B0" );
       else if ( slider.equals( altitude_slider ))
         border.setTitle( "Altitude \u2220 " + slider.getValue() + "\u00B0" );
       else if ( slider.equals( distance_slider ) )
         border.setTitle( "Distance "+slider.getValue()/DISTANCE_SCALE_FACTOR);

       if ( slider.equals( distance_slider ) )
         setView( true );
       else
         setView( false );
     }
  }


/** -------------------------------------------------------------------------
 *
 *   Main program for testing purposes only
 *
 */ 
  public static void main( String args[] )
  {
    JFrame f = new JFrame("Test for AltAzController");
    f.setBounds(0,0,200,200);
    AltAzController controller = new AltAzController();
//    controller.setVirtualScreenSize( 2, 2 );
    f.getContentPane().add( controller );
    f.setVisible( true );


    JFrame window = new JFrame("Test for AltAzController");
    window.setBounds(20,20,500,500);
    ThreeD_JPanel test = new ThreeD_JPanel();
    window.getContentPane().add( test );
    window.setVisible( true );

    controller.addControlledPanel( test );
    IThreeD_Object objs[] = new IThreeD_Object[1];
    Vector3D       pts[]  = new Vector3D[4];
    pts[0] = new Vector3D( -1,  1, 0 );
    pts[1] = new Vector3D(  1,  1, 0 );
    pts[2] = new Vector3D(  1, -1, 0 );
    pts[3] = new Vector3D( -1, -1, 0 );
    objs[0] = new Polyline( pts, Color.green );
    test.setObjects( "SAMPLE_OBJECTS", objs );
    controller.apply( true );
  }
 
}
