/*
 * File:  ImageJPanel2.java
 *
 * Copyright (C) 1999-2003, Dennis Mikkelson
 *               2005     , Mike Miller
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
 *  $Log: ImageJPanel2.java,v $
 *  Revision 1.1  2008/11/20 18:31:23  dennis
 *  Initial revision
 *
 *  Revision 1.21  2008/01/28 19:06:36  dennis
 *  The ImageJPanel2 maintains a copy of a thumbnail image, so that it
 *  does not need to be recalculated everytime the PanViewControl requests
 *  it.  This copy is now set to null if the color scale is changed, so that
 *  the next time the PanViewControl requests the thumbnail image, the
 *  thumbnail image will be reconstructed using the new color scale.  This
 *  fixes a bug where the color scale of the PanViewControl was not changed
 *  if the main image color scale was changed.
 *
 *  Revision 1.20  2007/08/05 20:11:35  dennis
 *  Fixed calculation of new bounds in makeImage.  No longer assumes
 *  that it will be called more than once, to set up new bounds that
 *  fall on pixel boundaries.
 *
 *  Revision 1.19  2007/07/29 20:45:16  dennis
 *  Changed local_transform and global_transform to be private
 *  in CoordJPanel class, to keep better control over who can
 *  change them, and how they can be changed.
 *
 *  Revision 1.18  2007/07/29 19:12:53  dennis
 *  Removed call to repaint() from makeImage() method, so the image
 *  can be reconstructed internally without being repainted yet.
 *  Modified LocalTransformChanged() method to call the super class's
 *  LocalTranformedChanged() method, after re-making the image.
 *
 *  Revision 1.17  2007/07/12 21:20:56  rmikk
 *  Removed the Color Model Object State and moved the TwoSided object
 *     state up to the ImageViewComponent, so the info was not recorded twice
 *
 *  Revision 1.16  2007/07/10 14:34:24  dennis
 *  Now again tests isVisible() instead of isShowing() before drawing.
 *
 *  Revision 1.15  2007/06/15 16:42:00  dennis
 *  Removed do_thumbnail parameter from makeImage(), since the thumbnail
 *  image is now made in the method makeThumbnailImage().
 *
 *  Revision 1.14  2007/06/15 16:23:33  dennis
 *  1. Making thumbnail image is now done in separate method, so that it
 *     no longer interacts with making the displayed image.  The thumbnail
 *     image is only made once after setting the data for the image.  The
 *     thumbnail is made at a default size specified by THUMBNAIL_SIZE, that
 *     is currently 256x256.
 *  2. The image is no longer sampled to the the size of the monitor and
 *     then resampled to smaller sizes.  The image is sampled, on demand,
 *     to "essentially" the size of the window in which it is displayed.
 *  3. The subSample() method now takes a requested width and height, and
 *     returns an Image object with width and height less than or equal to
 *     the specified width and height.
 *  4. Removed code that set local transforms from the setData() method, since
 *     that is done in makeImage().
 *
 *  Revision 1.13  2007/06/13 21:05:26  dennis
 *  Removed un-needed update() method that called paint().
 *  Changed paintComponent() method to use a copy of the Graphics
 *  object passed in.
 *
 *  Revision 1.12  2007/06/13 15:49:25  rmikk
 *  Replaced paint by paintComponent as preferred by the latest java
 *
 *  Revision 1.11  2007/06/05 20:10:53  rmikk
 *  Eliminated an off by one error.  end_col's float value can be NumColumns
 *
 *  Revision 1.10  2007/04/29 18:47:44  rmikk
 *  Fixed off by one error caused be previous fixes of such errors.  This caused
 *  the last row and last column not to be shown
 *
 *  Revision 1.9  2007/04/29 18:22:54  dennis
 *  Added boolean parameter to makeImage() method to determine
 *  whether or not the thumbnail image is to be constructed.
 *
 *  Revision 1.8  2007/04/28 13:56:05  rmikk
 *  Fixed error when there is only one row or one column of data
 *
 *  Revision 1.7  2007/03/23 15:34:56  rmikk
 *  Fixed an off by one error which made it difficult for fill lines to be drawn
 *    in the middle of blocks corresponding to array elements
 *
 *  Revision 1.6  2007/03/18 21:17:27  rmikk
 *  makeImage only increases the zoom region the first time when the local
 *    bounds do not correspond with pixel boundaries
 *
 *  Revision 1.5  2007/03/15 20:12:09  dennis
 *  Made getWorldToImageTransform() method public so that it can
 *  be used by the ImageViewComponent.
 *
 *  Revision 1.4  2007/02/05 04:32:14  dennis
 *  Removed small adjustment by 0.001 to World Coordinate bounds, which
 *  was not necessary and caused problems with selections containing
 *  the 0th row or column.
 *
 *  Revision 1.3  2005/08/14 05:22:11  dennis
 *  Added public method RebuildImage() to force the visibile image
 *  to be recalculated, and call repaint().  This is needed for
 *  setting the image scrolling is done with a separate scroll bar.
 *
 *  Revision 1.2  2005/06/17 20:10:35  kramer
 *
 *  Modified the getThumbnail() method by adding a 'forceRedraw' parameter
 *  that can force the redraw of the thumbnail.  This fixes the problem of
 *  the thumbnail not changing after the color scale changes on the big
 *  image.  This procedure works but a more efficient solution should be
 *  found.
 *
 *  Revision 1.1  2005/03/07 16:58:42  millermi
 *  - New Version of ImageJPanel based on IVirtualArray2D. This version
 *    eliminates image size limitations presented by Java.
 *  - Added methods enableAutoDataRange(), isAutoDataRangeEnabled(),
 *    and setDataRange() to allow user to set colorscale mapping to
 *    a specified data range.
 *  - Revised getThumbnail() so makeImage() does not have to be called
 *    so many times, since it is an expensive operation.
 *  - Added super.paint(g) to beginning of paint().
 *  - Revised makeImage() so that subsampling of data occurs before generating
 *    an image.
 *  - Added private method subSample() to subsample data.
 *
 *
 *********************** Log from ImageJPanel.java *************************
 *  Revision 1.29  2004/11/12 17:26:07  millermi
 *  - Code in setLogScale() was factored out into PseudoLogScaleUtil.
 *    setLogScale() now used PseudoLogScaleUtil to do log mapping.
 *
 *  Revision 1.28  2004/05/03 18:09:41  dennis
 *  Removed unused constant NUM_PSEUDO_COLORS.
 *
 *  Revision 1.27  2004/03/19 17:24:27  dennis
 *  Removed unused variables
 *
 *  Revision 1.26  2004/03/15 23:53:55  dennis
 *  Removed unused imports, after factoring out the View components,
 *  Math and other utils.
 *
 *  Revision 1.25  2004/03/12 01:47:00  dennis
 *  Moved to ViewTools.Panels.Image package
 *
 *  Revision 1.24  2004/02/12 21:53:10  millermi
 *  - Added method getImageCoords() which returns the image bounds.
 *
 *  Revision 1.23  2004/01/29 23:26:41  millermi
 *  - Two-sided no longer ignored for default state.
 *
 *  Revision 1.22  2004/01/29 08:18:14  millermi
 *  - Updated the getObjectState() to include parameter for specifying
 *    default state.
 *  - Added static variables DEFAULT and PROJECT to IPreserveState for
 *    use by getObjectState()
 *
 *  Revision 1.21  2003/12/23 20:59:15  millermi
 *  - Fixed bug introduced in makeImage() that restricted temp > 0,
 *    now changed to temp > -(LOG_TABLE_SIZE-1) so negative values
 *    also show.
 *
 *  Revision 1.20  2003/11/21 00:39:12  millermi
 *  - Added method getThumbnail() to get a replica of the
 *    image displayed by the ImageJPanel
 *  - *** makeImage() should be made more generic so the image
 *    can be set to more than just the current zoomed in region.
 *    Once this is done, the getThumbnail() should be editted
 *    to make use of this generality. ***
 *
 *  Revision 1.19  2003/11/18 00:56:19  millermi
 *  - ObjectState for IndexColorModel now saved as a string name,
 *    since IndexColorModel isn't serializable.
 *  - Made color_model transient.
 *  - Line 671, added check to make sure temp is a valid index.
 *
 *  Revision 1.18  2003/10/23 05:44:14  millermi
 *  - Added getObjectState() and setObjectState() methods to
 *    allow for preservation of state.
 *  - Now implements IPreserveState interface
 *  - Added public static Strings as keys for accessing state
 *    information.
 *
 *  Revision 1.17  2003/10/22 20:26:47  millermi
 *  - Fixed java doc error.
 *
 *  Revision 1.16  2003/07/10 13:37:05  dennis
 *  - Added some functionality to main() for testing purposes
 *  - Added isTwoSided as private data member to be used by the makeImage()
 *  - Now supports either one-sided or two sided color scales.
 *    (The pix array in makeImage() is now filled using zero_index in
 *     place of ZERO_COLOR_INDEX.  If the color model is two-sided,
 *     zero_index = ZERO_COLOR_INDEX, else zero_index = 0.)
 *  (Mike Miller)
 *
 *  Revision 1.15  2003/07/05 19:15:37  dennis
 *  Added methods to get min and max of data.  Added parameter to the
 *  setNamedColorMode() method to control whether the color scale is
 *  suitable for images with positive data or with both positive and
 *  negative data. (Mike Miller)
 *  Merged with previous changes.
 *
 *  Revision 1.14  2003/04/18 15:20:42  dennis
 *  Vertical scrolling is no longer automatically set true.
 *
 *  Revision 1.13  2003/02/25 22:28:12  dennis
 *  Added java docs.  Set data method now rejects ragged arrays, or
 *  degenerate or empty arrays.
 *
 *  Revision 1.12  2002/11/27 23:13:18  pfpeterson
 *  standardized header
 *
 *  Revision 1.11  2002/07/17 15:18:00  dennis
 *  Now sets valid default for the color model.
 *
 *  Revision 1.10  2002/07/15 16:55:11  pfpeterson
 *  No longer sets its own default value.
 *
 *  Revision 1.9  2002/06/19 22:43:24  dennis
 *  Added some additional checks to keep row,col values in range.
 */

package gov.anl.ipns.ViewTools.Panels.Image;

import java.awt.*;
import java.io.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.swing.*;

import gov.anl.ipns.ViewTools.Components.*;
import gov.anl.ipns.ViewTools.Panels.Transforms.*;
import gov.anl.ipns.ViewTools.Panels.Image.*;
import gov.anl.ipns.Util.Numeric.*;
import gov.anl.ipns.Util.Sys.*;

/**
 *    This class displays two dimensional arrays of floating point values as 
 *  pseudo-color images.  The pseudo-color scale may be specified to be 
 *  any of the named color scales from the IndexColorMaker class.  The
 *  range of values in the array is calculated and scaled logarithmically
 *  before being mapped to the pseudo-color scale.  IF the array contains
 *  negative values, one of the "dual" color models from the IndexColorMaker
 *  should be used.  The shape of the log function used for the scaling
 *  can be adjusted to control the apparent brightness of the image. 
 *    A "world coordinate" system can be applied to the image to map between
 *  row and column indices and "world coordinate" (x,y) values.  In addition
 *  cursor position information and zoom in/out is provided by methods from
 *  the base class, CoordJPanel.  Alternatively, methods to convert between
 *  image row and column values and pixel or world coordinate values are
 *  also provided.
 *
 *  @see CoordJPanel 
 *  @see IndexColorMaker
 */

public class ImageJPanel2 extends    CoordJPanel 
                          implements Serializable, IPreserveState
{
 // these variables preserve state for the ImageJPanel
 /**
  * "Color Model" - This constant String is a key for referencing the state
  * information about the IndexColorModel used by the ImageJPanel. The value
  * referenced by this key is a String name constant from IndexColorMaker.java.
  */
  public static final String COLOR_MODEL = "Color Model";
  
 /**
  * "Log Scale" - This constant String is a key for referencing the state
  * information about the log scale table used to map data values to colors
  * within the colorscale. The value referenced by this key is an array of
  * bytes.
  */
  public static final String LOG_SCALE   = "Log Scale";
  
 /**
  * "Two Sided" - This constant String is a key for referencing the state
  * information about displaying the data. If one-sided, negative data will
  * be mapped to zero and a one-sided color model will be used, where as
  * two-sided data will remain negative and be mapped to colors on a two-sided
  * color model.
  */
  public static final String TWO_SIDED   = "Two Sided";

  /**
   * "Number of Colors" - The constant String is a key for referencing
   * object state info on the number of colors used in for the color model
   */
  public static final String NUM_COLORS = "Number of Colors";
  
  /**
   * "Is pseudo log Scale" - The constant String is a key for referencing
   * object state info on the whether this panel is displaying a psudo log
   * scale
   */
  public static final String IS_LOG = "Is pseudo log Scale";
  

  
  /**
   * "PreScale factor" - The constant String is a key for referencing
   * object state info on the prescaling facto applied to all the data
   */
  public static final String PRESCALE ="PreScale factor";
  
  /**
   * "PreScale factor" - The constant String is a key for referencing
   * object state info on the prescaling facto applied to all the data
   */
  public static final String  AUTOSCALE="Auto scale using the data";
  
  /**
   * "PreScale factor" - The constant String is a key for referencing
   * object state info on the prescaling facto applied to all the data
   */
  public static final String  MINDATA="Min Data Value";
  
  /**
   * "PreScale factor" - The constant String is a key for referencing
   * object state info on the prescaling facto applied to all the data
   */
  public static final String  MAXDATA="Max Data Value";
  
  
  
 /**
  * This constant specifies the size at which the thumbnail image is 
  * calculated internally.
  */
  public static final int THUMBNAIL_SIZE = 256;

  
  private final int       DEFAULT_TABLE_SIZE          = 60000;
  private final int       DEFAULT_NUM_POSITIVE_COLORS = 127; 
  private Image           image;
  private float           prescale = 1;            // factor for scaling data
  private Image           rescaled_image  = null;
  private Image           thumbnail_image = null;
  private IVirtualArray2D data;
  private float           min_data;
  private float           max_data;

  private transient IndexColorModel color_model;
  private transient int             num_positive_colors;

  private String          color_model_string;
  private byte[]          color_scale;
  private boolean         isTwoSided;
  private boolean         log_color_map;

  private boolean         auto_scale_data = true; // Initially allow auto scale.


/* ------------------------- constructor ---------------------------------- */
/**
 *  Construct an ImageJPanel with default values for the color scale, log
 *  scaling factor and array of values.  Most applications using this class
 *  will at least have to use the setData() method to provide the actual
 *  values to be displayed.
 */
  public ImageJPanel2()
  { 
    float[][] temp = { { -4, -3, -2 },
                       { -1,  0,  1 }, 
                       {  2,  3,  4 } };
    data = new VirtualArray2D( temp );
    setData( data, false );
    
    color_scale = new byte[DEFAULT_TABLE_SIZE];
    setLogScale( 10 );                       // this sets log_color_map = true
                                             // and isTwoSided = true
  
    color_model_string = IndexColorMaker.HEATED_OBJECT_SCALE_2;
    setNamedColorModel( color_model_string, isTwoSided, false ); 
 
    CJP_handle_arrow_keys = false;
    addKeyListener( new ImageKeyAdapter() );
  }


/* ------------------------- setObjectState ------------------------------- */
 /**
  * This method will set the current state variables of the object to state
  * variables wrapped in the ObjectState passed in.
  *
  *  @param  new_state
  */
  public void setObjectState( ObjectState new_state )
  {
    // since ImageJPanel extends CoordJPanel, set those state variables first.
    super.setObjectState(new_state);
    boolean redraw = false;  // if any values are changed, repaint.
    boolean makeNewColorModel = false;
     Object temp = new_state.get(LOG_SCALE);
    if( temp != null )
    {
      color_scale = (byte[])temp;
      redraw = true;  
    }  
   
   temp = new_state.get(TWO_SIDED); //Moved to ImageViewComponent
    if( temp != null )
    {
      boolean r1 =((Boolean)temp).booleanValue();
      if( r1 != isTwoSided)
         makeNewColorModel = true;
      isTwoSided = r1;
      
      redraw = true;  
    }
    
    temp = new_state.get(COLOR_MODEL);
    if( temp != null )
    {
      color_model_string = (String)temp;

      makeNewColorModel = true;
     
      redraw = true;  
    }
    temp = new_state.get( NUM_COLORS );
    if( temp != null){
       int n1 = ((Number)temp).intValue();
       if( n1 != num_positive_colors)
          makeNewColorModel = true;
       num_positive_colors = n1;
       redraw = true;
    }
    temp = new_state.get(  IS_LOG);
    if( temp != null){
       boolean r1 =((Boolean)temp).booleanValue();

       if( r1 != log_color_map)
          makeNewColorModel = true;
       log_color_map = r1;

       makeNewColorModel = true;
       redraw = true;
    }
    if( makeNewColorModel){
      setNamedColorModel(  color_model_string,
               isTwoSided,
                num_positive_colors,
                true );
    }
    
    temp = new_state.get(  PRESCALE);
    if( temp != null){
     
       prescale = ((Number)temp).floatValue();
       redraw = true;
    }
    
    temp = new_state.get(  MINDATA);
    if( temp != null){
       
       float x =((Number)temp).floatValue();
       if( x != min_data){
          min_data =x;
          redraw = true;
       }
       
    }
    
    temp = new_state.get(  MAXDATA);
    if( temp != null){

       
       float x =((Number)temp).floatValue();
       if( x != max_data){
          max_data =x;
          redraw = true;
       }
    }
    
    
    temp = new_state.get(  AUTOSCALE);
    if( temp != null){
       boolean b1 = ((Boolean)temp).booleanValue();
       if( b1 != auto_scale_data){
          auto_scale_data = b1;
          setData( data, false);
          redraw = true;
       }
       
    }
  
    
    
    if( redraw ){
      image = null;//will remake image
      repaint();
    }
      
  } 

 
/* ------------------------- getObjectState ------------------------------- */
 /**
  * This method will get the current values of the state variables for this
  * object. These variables will be wrapped in an ObjectState.
  *
  *  @param  isDefault Should selective state be returned, that used to store
  *		       user preferences common from project to project?
  *  @return if true, the default state containing user preferences,
  *	     if false, the entire state, suitable for project specific saves.
  */ 
  public ObjectState getObjectState( boolean isDefault )
  {

     ObjectState state = super.getObjectState(isDefault);
     if( isDefault){

        state.insert( COLOR_MODEL, System.getProperty( "ColorScale","Heat 1" ) );
        
        byte[] Cscale = color_scale;
        boolean SaveLogColorMap = log_color_map;
        setLogScale( 10 );
        color_scale = Cscale;
        state.insert( LOG_SCALE, color_scale );
        log_color_map = SaveLogColorMap;
        state.insert( TWO_SIDED, false );
        state.insert( NUM_COLORS , DEFAULT_NUM_POSITIVE_COLORS );
        state.insert( IS_LOG ,false );
        state.insert( PRESCALE, 1f );
        state.insert( AUTOSCALE , true );
        state.insert(  MINDATA , -4f );
        state.insert(  MAXDATA , 4f );
        
        return state;
       
     }
    //get ObjectState of CoordJPanel
    state.insert( COLOR_MODEL, color_model_string );
    state.insert( LOG_SCALE, color_scale );
    state.insert( TWO_SIDED, new Boolean(isTwoSided) );
    state.insert( NUM_COLORS , num_positive_colors );
    state.insert( IS_LOG ,log_color_map );
    state.insert( PRESCALE, prescale );
    state.insert( AUTOSCALE , auto_scale_data );
    state.insert(  MINDATA , min_data );
    state.insert(  MAXDATA , max_data);
    
    return state;
  }


/* ------------------------- changeLogScale -------------------------- */
/**
 *  Change the control parameter for the logarithmic scaling applied to
 *  the image values.  If the image has negative values, the logarithmic
 *  scaling is applied to the absolute value of the values.
 *
 *  @param   s             The control parameter, s, clamped to the range
 *                         [0,100].  If s is 0, the scale is essentially 
 *                         linear.  If s is 100, the relative intensity of 
 *                         small values is greatly increased so the image 
 *                         is lightened.
 *  @param   rebuild_image Flag to determine whether the displayed image is
 *                         rebuilt with the new log scale factor, or if
 *                         rebuilding the displayed image should be delayed
 *                         since other changes will also be made before
 *                         rebuilding the image.  A value of "true" will
 *                         cause the image to be rebuilt immediately.
 */
  public void changeLogScale( double s, boolean rebuild_image )
  {                                       
    setLogScale( s );

    if ( rebuild_image )
      RebuildImage();
  }


/* ---------------------------- setPrescale ------------------------------ */
/**
 *  Set a prescaling factor that will be multiplied times all data values
 *  before the data values are associated with colors.
 *
 *  @param  prescale       The prescale factor that should multiply 
 *                         all data values.
 *  @param   rebuild_image Flag to determine whether the displayed image is
 *                         immediately rebuilt using the new prescale factor. 
 */
  public void setPrescale( float prescale, boolean rebuild_image )
  {
    this.prescale = prescale;

    if ( rebuild_image )
      RebuildImage();
  }


/* ----------------------- changeColorIndexTable -------------------------- */
/**
 *  Change the lookup table giving the color index relative to the 
 *  the image value.  Image values are mapped linearly from interval
 *  [min_val,max_val] to an index in [0,table.length-1].  The byte entry at
 *  the corresponding index is used as the color index in the current 
 *  color model.  This method also changes the data range bounds, setting
 *  the range max to max_val and the range min to min_val.  
 *
 *  @param  table          Array of bytes providing indices into the 
 *                         color table.  The bytes should have values
 *                         between 0 and the number of colors-1;
 *
 *  @param  is_log_scale   Set true if this color table represents a log
 *                         of intensity scale.  If true, the min and max
 *                         values must both be positive with max > min.
 *
 *  @param  min_val        The image data value that corresponds to the
 *                         last entry in the table.  This must be less than
 *                         max_val.  For log scales this must be positive. 
 *
 *  @param  max_val        The image data value that corresponds to the
 *                         last entry in the table.  This must be more than
 *                         min.
 *
 *  @param  rebuild_image  Flag to determine whether the displayed image is
 *                         rebuilt immediately with the new color table, or if
 *                         rebuilding the displayed image should be delayed
 *                         since other changes will also be made before
 *                         rebuilding the image.  A value of "true" will
 *                         cause the image to be rebuilt immediately.
 */
  public void changeColorIndexTable( byte[]   table, 
                                     boolean  is_log_scale,
                                     float    min_val,
                                     float    max_val,
                                     boolean  rebuild_image )
  {
    color_scale = new byte[ table.length ];
    System.arraycopy( table, 0, color_scale, 0, table.length );

    log_color_map = is_log_scale;
    if ( is_log_scale )
    {
      if ( min_val <= 0 )
      {
        min_val = 0.01f;
        SharedMessages.addmsg("ERROR: min_val <= 0 when using log scale in " +
                          " changeColorIndexTable(), setting min_val to " +
                          min_val );
      }

      if ( max_val <= min_val )
      {
        max_val = min_val * 10000;
        SharedMessages.addmsg("ERROR: max_val <= min_val in "+
                          " changeColorIndexTable(), setting max_val = " +
                            max_val );
      }
    }

    setDataRange( min_val, max_val );

    if ( rebuild_image )
      RebuildImage();
  }


/* -------------------------- setNamedColorModel --------------------------- */
/**
 *  Change the color model to used for the image.  If the data has negative
 *  values, one of the "Dual" color models should be used.
 *
 *  @param   color_scale_name  Name of the new color scale to use for the
 *                             image.  Supported color scales are listed in
 *                             the IndexColorMaker class.
 *  @param   twosided          Flag that determines whether a color scale 
 *                             that includes colors for both positive and 
 *                             negative values is built, or if only positive
 *                             values are represented.
 *  @param   rebuild_image     Flag to determine whether the displayed image is
 *                             rebuilt with the new log scale factor, or if
 *                             rebuilding the displayed image should be delayed
 *                             since other changes will also be made before 
 *                             rebuilding the image.  A value of "true" will
 *                             cause the image to be rebuilt immediately.
 * 
 *  @see IndexColorMaker
 */
  public void setNamedColorModel( String   color_scale_name,
                                  boolean  twosided,
                                  boolean  rebuild_image   )
  {
     setNamedColorModel( color_scale_name, 
                         twosided, 
                         DEFAULT_NUM_POSITIVE_COLORS,
                         rebuild_image );
  }


/* -------------------------- setNamedColorModel --------------------------- */
/**
 *  Change the color model to used for the image.  If the data has negative
 *  values, one of the "Dual" color models should be used.
 *
 *  @param   color_scale_name  Name of the new color scale to use for the
 *                             image.  Supported color scales are listed in
 *                             the IndexColorMaker class.
 *  @param   twosided          Flag that determines whether a color scale 
 *                             that includes colors for both positive and 
 *                             negative values is used, or if only positive
 *                             values are represented.
 *  @param   num_colors        The number of colors to use in the color scale,
 *                             between 2 and 127;
 *  @param   rebuild_image     Flag to determine whether the displayed image is
 *                             rebuilt with the new log scale factor, or if
 *                             rebuilding the displayed image should be delayed
 *                             since other changes will also be made before 
 *                             rebuilding the image.  A value of "true" will
 *                             cause the image to be rebuilt immediately.
 * 
 *  @see IndexColorMaker
 */
  public void setNamedColorModel( String   color_scale_name,
                                  boolean  twosided,
                                  int      num_colors,
                                  boolean  rebuild_image   )
  {
    if ( num_colors < 2 )
      num_colors = 2;

    if ( num_colors > 127 )
      num_colors = 127;

    isTwoSided = twosided;

    color_model_string = color_scale_name;

    if( isTwoSided )                      // color table is built with 2*n+1 
    {                                     // entries, 0, n positive, n negative
      num_positive_colors = num_colors;
      color_model = IndexColorMaker.getDualColorModel( color_model_string,
                                                       num_colors );
    }
    else                                  // color table is built with n colors
    {                                     // 0 and n-1 positive colors
      num_positive_colors = num_colors - 1;
      color_model = IndexColorMaker.getColorModel( color_model_string,
                                                   num_colors );
    }

    if ( rebuild_image )
      RebuildImage();

    thumbnail_image = null;    // Changing color scale makes the old 
                               // thumbnail invalid
  }

  
 /* ----------------------- enableAutoDataRange --------------------------- */
 /**
  * Determine whether the minimum and maximum data values are calculated
  * by the setData() [True] or explicitly set by setDataRange() [False].
  * Initially, auto data range calculation is on. Be sure to call this
  * method only after the data min/max have been set either by the
  * setData() or setDataRange() methods.
  *
  *  @param  auto_range_on If true, data range is calculated by setData()
  *                        and will dynamically change each time setData()
  *                        is called.
  *                        If false, data range must be set explicitly
  *                        using the setDataRange() method.
  */
  public void enableAutoDataRange( boolean auto_range_on )
  {
    auto_scale_data = auto_range_on;
  }

  
 /* --------------------- isAutoDataRangeEnabled ------------------------- */
 /**
  * Determine whether the minimum and maximum data values are calculated
  * by the setData() [True] or explicitly set by setDataRange() [False].
  *
  *  @return True if calculated by setData().
  *          False if set explicitly by setDataRange().
  */
  public boolean isAutoDataRangeEnabled()
  {
    return auto_scale_data;
  }
  

 /* -------------------------- setDataRange ------------------------------ */
 /**
  * This method will set the data range to [data_min,data_max]. Calling this
  * method will disable auto data range calculation done when setData() is
  * called.  The color scale mapping will apply only to image values in
  * the specified range.  
  *
  *  @param  data_min The minimum data value mapped to the minimum color.
  *  @param  data_max The maximum data value to be mapped to the max color.
  */
  public void setDataRange( float data_min, float data_max )
  {
    // Make sure data_min < data_max
    if( data_min > data_max )
    {
      float swap = data_min;
      data_min = data_max;
      data_max = swap;
    }

    // Prevent data_min = data_max
    if( data_min == data_max )
      data_max = data_min + 1;

    // Set min/max_data
    min_data = data_min;
    max_data = data_max;

    // turn off auto data range calculation.
    enableAutoDataRange(false);
  }


/* ------------------------------- setData -------------------------------- */
/**
 *  Change the array of floats that is displayed by this ImageJPanel.
 *
 *  @param   a2d           IVirtualArray2D containing a 2 dimensional array. 
 *                         an image.
 *
 *  @param   rebuild_image Flag to determine whether the displayed image is
 *                         rebuilt now, or if rebuilding the displayed image 
 *                         should be delayed since other changes will also 
 *                         be made before the image must be rebuilt and 
 *                         drawn.  A value of "true" will
 *                         cause the image to be rebuilt immediately.
 */

  public void setData( IVirtualArray2D a2d, boolean rebuild_image  )
  {
    // if nothing was passed in, do nothing.
    if ( a2d == null || a2d.getNumRows() <= 0 || a2d.getNumColumns() <= 0)
    {
      System.out.println("ERROR: empty virtual array in ImageJPanel.setData");
      return;
    }
    data = a2d;
    
    if( isAutoDataRangeEnabled() )
    {
      max_data = Float.NEGATIVE_INFINITY;
      min_data = Float.POSITIVE_INFINITY;
      float temp;
      int row_count = data.getNumRows();
      int col_count = data.getNumColumns();
      for ( int row = 0; row < row_count; row++ )
      {
        for ( int col = 0; col < col_count; col++ )
        {
          temp = data.getDataValue(row,col);
          if ( temp > max_data )
            max_data = temp;
          if ( temp < min_data )
            min_data = temp;
        }
       }

      if ( min_data == max_data )    // avoid division by 0 when scaling data
        max_data = min_data + 1;

    } // End if( isAutoScaleDataEnabled() )


    thumbnail_image = null;

    if ( rebuild_image )
      RebuildImage();

  } // End setData()


 /* -------------------------- getThumbnail ----------------------------- */  
 /**
  *  Get a thumbnail of the entire image shown by this ImageJPanel.  The
  *  thumbnail is obtained by resampling a thumbnail image that is
  *  precomputed at a size specified by THUMBNAIL_SIZE.
  *
  *  @param  width  The desired width of the thumbnail.
  *  @param  height The desired height of the thumbnail.
  *
  *  @return A thumbnail of the Image.
  */ 
  public Image getThumbnail(int width, int height)
  {
    if ( thumbnail_image == null )
      thumbnail_image = makeThumbnailImage();

    Image thumbnail = null; 

    if( thumbnail_image != null )
    {
      if( width == 0 || height == 0 )
        thumbnail = thumbnail_image.getScaledInstance( 100, 100,
                                                       Image.SCALE_DEFAULT );
      else
        thumbnail = thumbnail_image.getScaledInstance( width, height,
	                                               Image.SCALE_DEFAULT);
     }
     return thumbnail;
  } 


/* --------------------------- getNumDataRows ----------------------------- */
/**
 *  Get the number of rows in the data for this image panel.
 *
 *  @return  The number of rows.
 */
  public int getNumDataRows()
  {
    if ( data != null )
      return data.getNumRows();

    return 0;
  }


/* --------------------------- getNumDataColumns ------------------------ */
/**
 *  Get the number of columns in the data for this image panel.
 *
 *  @return  The number of columns.
 */
  public int getNumDataColumns()
  {
    if ( data != null )
      return data.getNumColumns();

    return 0;
  }


/* ---------------------------- paintComponent --------------------------- */
/**
 *  This method is invoked by swing to draw the image.  Applications must not
 *  call this directly.
 *
 *  @param g  The Graphics object to use for drawing the image
 */
  public void paintComponent( Graphics g )
  {
    Graphics2D g2d = (Graphics2D)g.create();

    stop_box( current_point, false );   // if the system redraws this without
    stop_crosshair( current_point );    // our knowlege, we've got to get rid
                                        // of the cursors, or the old position
                                        // will be drawn rather than erased 
                                        // when the user moves the cursor (due
                                        // to XOR drawing). 

    if ( image == null )                // the component might not have been
      makeImage();                      // visible when makeImage was called

    if ( image != null )                // the component not showing
      g2d.drawImage( image, 0, 0, this ); 

    g2d.dispose();
  }


/* -------------------------- ImageRow_of_PixelRow ----------------------- */
/**
 *  Get the row number in the data array of the specified pixel row.
 *
 *  @param  pix_row    Pixel "row" value, i.e. pixel y coordinate.
 *
 *  @return  the row number in the data array cooresponding to the specified
 *           pixel row.
 */
  public int ImageRow_of_PixelRow( int pix_row )
  {
    float WC_y = getLocal_transform().MapYFrom( pix_row );
 
    return ImageRow_of_WC_y( WC_y );
  }


/* -------------------------- ImageRow_of_WC_y ----------------------- */
/**
 *  Get the row number in the data array corresponding to the specified
 *  world coordinate y value. 
 *
 *  @param  y  The world coordinate y value 
 *
 *  @return  the row number in the data array cooresponding to the specified
 *           pixel world coordinate y.
 */
  public int ImageRow_of_WC_y( float y )
  {
    CoordTransform world_to_image = getWorldToImageTransform();
   
    int   row = (int)( world_to_image.MapYTo( y ) );
    if ( row < 0 )
      row = 0;
    else if ( row > data.getNumRows() - 1 )
      row = data.getNumRows() - 1;
    return row;
  }


/* -------------------------- ImageCol_of_PixelCol ----------------------- */
/**
 *  Get the column number in the data array of the specified pixel col.
 *
 *  @param  pix_col    Pixel "col" value, i.e. pixel x coordinate.
 *
 *  @return  the column number in the data array cooresponding to the specified
 *           pixel column.
 */
  public int ImageCol_of_PixelCol( int pix_col )
  {
    float WC_x = getLocal_transform().MapXFrom( pix_col );

    return  ImageCol_of_WC_x( WC_x );
  }


/* -------------------------- ImageCol_of_WC_x ----------------------- */
/**
 *  Get the column number in the data array corresponding to the specified
 *  world coordinate x value.
 *
 *  @param  x  The world coordinate x value
 *
 *  @return  the column number in the data array cooresponding to the specified
 *           pixel world coordinate x.
 */
  public int ImageCol_of_WC_x( float x )
  {
    CoordTransform world_to_image = getWorldToImageTransform();
   
    int col = (int)( world_to_image.MapXTo( x ) );
    if ( col < 0 )
      col = 0;
    else if ( col > data.getNumColumns() - 1 )
      col = data.getNumColumns() - 1;

    return col;
  }


/* ----------------------- getWorldToImageTransform ---------------------- */

  public CoordTransform getWorldToImageTransform()
  {
    CoordBounds     world_bounds;
    CoordBounds     image_bounds;

    SetTransformsToWindowSize();
    image_bounds = new CoordBounds( 0,
                                    0,
                                    data.getNumColumns(),
                                    data.getNumRows() );
    world_bounds = getGlobal_transform().getSource();
    return( new CoordTransform( world_bounds, image_bounds ) );
  }


/* -------------------------- ImageValue_at_Pixel ----------------------- */
/**
 *  Get the data value from the data array that cooresponds to the specified
 *  pixel position.
 *
 *  @param  pixel_pt   The 2D coordinates of the pixel
 *
 *  @return The data value drawn at that pixel location
 */
  public float ImageValue_at_Pixel( Point pixel_pt )
  {
    int row = ImageRow_of_PixelRow( pixel_pt.y );
    int col = ImageCol_of_PixelCol( pixel_pt.x );

    return data.getDataValue(row,col);
  }


/* -------------------------- ImageValue_at_Cursor ----------------------- */
/**
 *  Get the data value from the data array at the current cursor position
 *
 *  @return The data value drawn at the current cursor location
 */
  public float ImageValue_at_Cursor( )
  {
    return ImageValue_at_Pixel( getCurrent_pixel_point() );
  }


/* ---------------------------- getPreferredSize ------------------------- */
/**
 *  Get the preferred size of this component based on the number of rows
 *  and columns and on whether or not scrolling has been requested.
 *
 *  @see CoordJPanel
 *
 *  @return  (0,0) is returned if scrolling has not been requested.  If
 *           vertical scrolling has been requested, the actual number of 
 *           rows in the data array will be used instead of 0.  If horizontal
 *           scrolling has been requested, the actual number of columns in
 *           in the data array will be used instead of 0.
 */
public Dimension getPreferredSize()
{
    if ( preferred_size != null )     // if someone has specified a preferred
      return preferred_size;          // size, just use it.

    int rows, cols;                   // otherwise calculate the preferred
                                      // width based on the data dimensions
                                      // if scrolling is to be used.
    if ( v_scroll )
      rows = data.getNumRows();
    else
      rows = 0;

    if ( h_scroll )
      cols = data.getNumColumns();
    else
      cols = 0;

    return new Dimension( cols, rows );
}


/* -------------------------------- getDataMin --------------------------- */
/**
 *  Get the minimum value of the data represented by the image.
 *
 *  @return  min_data
 */
public float getDataMin()
{
  return min_data;
}


/* -------------------------------- getDataMax --------------------------- */
/**
 *  Get the maximum value of the data represented by the image.
 *
 *  @return  max_data
 */
public float getDataMax()
{
  return max_data;
}


/* -------------------------- getImageCoords --------------------------- */
/**
 *  Get the image number of rows/columns wrapped in a CoordBound object to
 *  allow for mapping to another coordinate system.
 *
 *  @return The image transformation.
 */
public CoordBounds getImageCoords()
{
  return getWorldToImageTransform().getDestination();
}


/* -------------------------- RebuildImage ---------------------------- */
/**
 *  Force rebuilding the visibile portion of the image and call repaint.
 *  This routine must be called after the zoom region is set.
 */
public void RebuildImage()
{
   makeImage();
   repaint();
}


/* ---------------------- LocalTransformChanged -------------------------- */
/**
 *  This method is responsible for doing what is necessary to regenerate
 *  the display, AFTER the local transform is changed.  For an ImageJPanel2,
 *  the extra work required is to reconstruct a new image, corresponding to
 *  the new source region of the image.
 */
protected void LocalTransformChanged()
{
  makeImage();                          // Reconstruct the image and then
  super.LocalTransformChanged();        // do anything that the base class
                                        // needs to do to regenerate after a
                                        // a change of the local transform
}


/* -----------------------------------------------------------------------
 *
 * PRIVATE METHODS
 *
 */


/* ---------------------------------- makeImage --------------------------- */
 /*
  * This method will create a visible image based on the data passed in.
  * By calling subSample(), this method can display any size array
  * in approximately the same time frame.  NOTE: This method will readjust
  * the local transformation so that it includes only whole pixels.  The
  * fact that the local transformation is changed may cause problems.  Code
  * that sets the local transformation should not assume that it has full
  * control of the local transformation, but should always get it before 
  * using it.
  *
  * Note: The color model used is either built as one-sided:
  *
  *   Color model has indices [0,k-1] with color "intensity" increasing with
  *   index.  k-1 is the number of positive colors.
  *
  * or as two-sided:
  *
  *   Color model has indices [0,2k].  The middle index k, corresponds to the
  *   color index that will be associated with the single value 0.  Positive 
  *   color intensities increase with increasing index, k+1 to 2k.  Negative 
  *   color intensities increase with decreasing index k-1 to 0.  Here, k is
  *   the number of positive (or negative) colors.
  */
  private void makeImage()
  {
    if ( ! isShowing() )             // don't do it yet if not "visible" 
      return;
    
    // Map the currently specified local world coordinate bounds to image
    // coordinates...

    CoordTransform world_to_image = getWorldToImageTransform();
    CoordBounds    bounds         = getLocal_transform().getSource();
    bounds = world_to_image.MapTo( bounds );

    // Then find the range of rows and columns that are even partly included
    // in the region.  Rows and columns in that range will be resampled and
    // rescaled to be fully included in the displayed image. 

    int start_row = (int)Math.floor( bounds.getY1() );
    int end_row   = (int)Math.ceil( bounds.getY2() ) - 1;

    int start_col = (int)Math.floor( bounds.getX1() );
    int end_col   = (int)Math.ceil( bounds.getX2() ) - 1;

    // Next, we need to bound the selected range of rows and columns so that
    // they are actually rows and columns of the image, and the start is 
    // before the end.
                                                              // bound start_row
    start_row = Math.max( start_row, 0 );
    start_row = Math.min( start_row, data.getNumRows() - 1 );
                                                              // bound end_row
    end_row = Math.min( end_row, data.getNumRows() - 1 );
    if ( end_row < start_row )
      end_row = start_row;
                                                             // bound start_col
    start_col = Math.max( start_col, 0 );
    start_col = Math.min( start_col, data.getNumColumns() - 1 );
                                                             // bound end_col
    end_col = Math.min( end_col, data.getNumColumns() - 1 );
    if ( end_col < start_col )
      end_col = start_col;

    // Now that we know what range of rows and columns are in the image we
    // can reset the local coordinate bounds.  Note: the coordinates range 
    // the left edge of the first column to the right edge of the last
    // column, and similarly for rows.  Consequently the end row and column
    // values of the world coordinate bounds are one larger than the last
    // row and column number.

    CoordBounds new_bounds = new CoordBounds( start_col, start_row,
                                              end_col+1, end_row+1 );

    new_bounds = world_to_image.MapFrom( new_bounds );
    setLocalWorldCoords( new_bounds );

    // Finally, make the image object by resampling and rescaling the range
    // of image rows and columns to match the number of pixels on the screen
   
    int width  = getWidth(); 
    int height = getHeight(); 

    image = subSample( start_row, end_row,
                       start_col, end_col,
                       width,     height  );

    if ( image != null )
    {
      stop_box( current_point, false );
      stop_crosshair( current_point );

      rescaleImage();
      image = rescaled_image;
    }
  }
 

 /* ------------------------- makeThumbnailImage ------------------------ */
 /*
  * This method will create a visible image based on the data passed in.
  * By calling subSample(), this method can display any size array
  * in approximately the same time frame.
  */
  private Image makeThumbnailImage()
  {
    if ( data                 == null || 
         data.getNumRows()    == 0    || 
         data.getNumColumns() == 0    ) 
      return null;

    int end_row = data.getNumRows() - 1;
    int end_col = data.getNumColumns() - 1;

    return subSample( 0, end_row, 0, end_col, THUMBNAIL_SIZE, THUMBNAIL_SIZE );
  }


 /* ---------------------------- subSample ------------------------------- */
 /*
  *  This method produces an Image object by sub sampling (if necessary) a 
  *  specified sub rectangle of the array of data.  The returned image is
  *  has size at most width X height, but may be smaller, if the number of
  *  data rows or columns is less than the specified height or width.  
  *  There are two possible cases regarding the color model, either the 
  *  color model is "one-sided" in which case indices 0..k-1 are used, where
  *  k is the total number of colors, or the color model is split with 
  *  approximately have of the colors used for positive value and half for
  *  negative values.
  *    If the color model is two-sided then the total table length is 2k+1
  *  where k is the number of positive colors.  The "zero" position in the 
  *  table will be in position k, with positive values mapping to indices
  *  k+1 to 2k, and negative values mapping in reverse order to indices
  *  k-1 to 0.
  *    For a "true" logarithmic color scale the user should guarantee that
  *  the minimum data value is positive.
  *  
  *  @param  start_row   The first row of data to use.
  *  @param  end_row     The last row of data to use.
  *  @param  start_col   The first column of data to use.
  *  @param  end_col     The last column of data to use.
  *  @param  width       The width of the image to produce
  *  @param  height      The height of the image to produce
  *
  *  @return an image with the specified width and height, or smaller if
  *          the data does not have enough rows or columns.
  */ 
  private Image subSample( int start_row, int end_row,
                           int start_col, int end_col,
                           int width,     int height  )
  {
    int color_table_size = color_scale.length;
                                     // find the scale factor and zero offset
                                     // index based on the min/max data values
    float scale_factor = 0;
    float shift = 0;
    byte  zero_index = 0; 
                                    // NOTE: For one sided log scale, we 
                                    //       require 0 < min_data < max_data

    int last = color_table_size - 1;

/*
    System.out.println("------In subSample, width = " + width );
    System.out.println("In subSample, log_color_map = " + log_color_map );
    System.out.println("In subSample, isTwoSided    = " + isTwoSided );
    System.out.println("In subSample, min_data      = " + min_data );
    System.out.println("In subSample, max_data      = " + max_data );
    System.out.println("In subSample, table[0]      = " + color_scale[0] );
    System.out.println("In subSample, table[last]   = " + color_scale[last] );
*/

                                      // NOTE: For two sided color map, make
    if (isTwoSided)                   //       mapping symmetric around 0.
    { 
      float max_abs = 0;
      if ( Math.abs( max_data ) > Math.abs( min_data ) )
        max_abs = Math.abs( max_data );
      else
        max_abs = Math.abs( min_data );

      if ( max_abs > 0 )
        scale_factor = color_table_size / max_abs;
      else
      {
        scale_factor = 1;
        SharedMessages.addmsg("ERROR: Max absolute value of data range = 0, "+
                            "set scale factor to 1" );
      }
      zero_index = (byte)num_positive_colors;
    }
    else                          // just map values between min and max data
    {   
      scale_factor = color_table_size / (max_data - min_data);
      shift = -min_data * scale_factor;
      zero_index = 0;
    }

    scale_factor *= prescale;     // multiply in the prescale factor so that
                                  // it applies to all data values

/*
    System.out.println("MIN_DATA = " + min_data );
    System.out.println("MAX_DATA = " + max_data );
    System.out.println("TABLE_SIZE = " + color_table_size );
    System.out.println("NUM POSIITVE = " + num_positive_colors );
    System.out.println("ZERO INDEX = " + zero_index );
    System.out.println("SCALE_FACTOR = " + scale_factor );
    System.out.println("SHIFT = " + shift );
*/
                                     // Get the number of rows and columns to
                                     // use for the image.  This will be
                                     // approximately width and height

    int n_data_rows = Math.abs(end_row - start_row) + 1;
    int n_data_cols = Math.abs(end_col - start_col) + 1;
    
    int x_step = (int)Math.floor( n_data_cols/(double)width  );
    int y_step = (int)Math.floor( n_data_rows/(double)height );

    if( x_step < 1 )                 // If step less than one, make it 1
      x_step = 1;

    if( y_step < 1 )
      y_step = 1;

    int n_image_cols = (int)Math.ceil( n_data_cols/(double)x_step );
    int n_image_rows = (int)Math.ceil( n_data_rows/(double)y_step );
    byte  pix[] = new byte[ n_image_rows * n_image_cols ];
    float temp  = 0;
    int   index = 0;
                                     // make an array of bytes by sampling the
                                     // array in steps of x_step and y_step

    if ( isTwoSided )                // two-sided case is symmetric about 0.
    {                                // we use the same table for + and - vals
      for (int y = start_row; y <= end_row; y = y + y_step)
        for (int x = start_col; x <= end_col; x = x + x_step)
        {
          temp = data.getDataValue(y,x) * scale_factor;
	  if( temp > color_table_size - 1 )
	    temp = color_table_size - 1;
          else if( temp < -( color_table_size - 1) )
	    temp = -( color_table_size - 1);
	
          if ( temp >= 0 )
            pix[index++] = (byte)(zero_index + 1 + color_scale[(int)temp]);
          else
            pix[index++] = (byte)(zero_index - 1 - color_scale[(int)(-temp)]);
        }
    }
    else                             // one-sided case may not be symmetric
    {
      for (int y = start_row; y <= end_row; y = y + y_step)
        for (int x = start_col; x <= end_col; x = x + x_step)
        {
          temp = data.getDataValue(y,x) * scale_factor + shift;
          if ( temp > color_table_size - 1 )
            temp = color_table_size - 1;
          else if ( temp < 0 )
            temp = 0;

          pix[index++] = (byte)(zero_index + color_scale[(int)temp]);
        }
    }

    Image new_image = createImage(new MemoryImageSource(n_image_cols,
                                                        n_image_rows,
                                                        color_model, pix, 0,
                                                        n_image_cols));
    return new_image;
  }


/* ---------------------------- rescaleImage -------------------------- */

  private void rescaleImage()
  {
    int       new_width,
              new_height;

    SetTransformsToWindowSize();

    Dimension size = this.getSize();
    new_width  = size.width;
    new_height = size.height;

    if ( new_width == 0 || new_height == 0 )   // region not yet sized properly
      return;

    if ( image != null )
    {
      if ( v_scroll && new_height < data.getNumRows() )
        new_height = data.getNumRows();

      if ( h_scroll && new_width < data.getNumColumns() )
        new_width = data.getNumColumns();

      rescaled_image = image.getScaledInstance(new_width, new_height, 
                                               Image.SCALE_DEFAULT );
    }
  }


/* ----------------------------- setLogScale -------------------------- */
/**
 *  "Old style" pseudo-log scale.  This sets log_color_map true.
 */
  private void setLogScale( double s )
  {
    int color_table_size = color_scale.length;
    PseudoLogScaleUtil log_scaler = new PseudoLogScaleUtil(
                                          0f, (float)color_table_size,
					  0f, DEFAULT_NUM_POSITIVE_COLORS );
    for( int i = 0; i < color_table_size; i++ )
      color_scale[i] = (byte)(log_scaler.toDest(i,s));

    log_color_map = true;
  }


/*-----------------------------------------------------------------------
 *
 *  INTERNAL CLASSES
 *
 */

class ImageKeyAdapter extends KeyAdapter
{
  public void keyPressed( KeyEvent e )
  {
    int code = e.getKeyCode();

    boolean  is_arrow_key;
    is_arrow_key = ( code == KeyEvent.VK_LEFT || code == KeyEvent.VK_RIGHT ||
                     code == KeyEvent.VK_UP   || code == KeyEvent.VK_DOWN   );
                                                 // only process arrow keys
    if ( !is_arrow_key )
      return;

    CoordTransform world_to_image = getWorldToImageTransform();
    floatPoint2D cur_WC_point = getCurrent_WC_point();
    floatPoint2D cur_image_pix = world_to_image.MapTo( cur_WC_point );

    cur_image_pix.x = ImageCol_of_PixelCol( current_point.x ) + 0.5f;
    cur_image_pix.y = ImageRow_of_PixelRow( current_point.y ) + 0.5f;

    if ( code == KeyEvent.VK_UP )
    {
      if ( cur_image_pix.y > 1 )
        cur_image_pix.y = cur_image_pix.y - 1;
    }
    else if ( code == KeyEvent.VK_DOWN )
    {
      if ( cur_image_pix.y < data.getNumRows() - 1 )
        cur_image_pix.y = cur_image_pix.y + 1;
    }
    else if ( code == KeyEvent.VK_LEFT )
    {
      if ( cur_image_pix.x > 1 )
        cur_image_pix.x = cur_image_pix.x - 1;
    }
    else if ( code == KeyEvent.VK_RIGHT )
    { 
      if ( cur_image_pix.x < data.getNumColumns() - 1 )
        cur_image_pix.x = cur_image_pix.x + 1;
    }

    Point old_screen_pix_pt = getCurrent_pixel_point();
    cur_WC_point = world_to_image.MapFrom( cur_image_pix );
    setCurrent_WC_point( cur_WC_point );
    Point new_screen_pix_pt = getCurrent_pixel_point();

    if ( (new_screen_pix_pt.x == old_screen_pix_pt.x) &&
         (new_screen_pix_pt.y == old_screen_pix_pt.y)   )
    {
      if ( code == KeyEvent.VK_UP )
        new_screen_pix_pt.y--;
      else if ( code == KeyEvent.VK_DOWN )
        new_screen_pix_pt.y++;
      else if ( code == KeyEvent.VK_LEFT )
        new_screen_pix_pt.x--;
      else if ( code == KeyEvent.VK_RIGHT )
        new_screen_pix_pt.x++;
    }
    setCurrent_pixel_point( new_screen_pix_pt );

    int id = 0;                               // synthesize a mouse event and
    int modifiers = 0;                        // send it to this CoordJPanel
    int clickcount = 0;                       // to trigger the proper response

    if ( !isDoingBox() && !isDoingCrosshair() )
      id = MouseEvent.MOUSE_PRESSED;
    else
      id = MouseEvent.MOUSE_DRAGGED;

    if ( e.isShiftDown() )
      modifiers  = InputEvent.BUTTON2_MASK;
    else
      modifiers  = MouseEvent.BUTTON1_MASK;

    MouseEvent mouse_e = new MouseEvent( this_panel,
                                         id,
                                         e.getWhen(),
                                         modifiers,
                                         current_point.x,
                                         current_point.y,
                                         clickcount,
                                         false );
    this_panel.dispatchEvent( mouse_e );

    if ( id == MouseEvent.MOUSE_PRESSED )      // Also send dragged event
    {
       mouse_e = new MouseEvent( this_panel,
                                 MouseEvent.MOUSE_DRAGGED,
                                 e.getWhen()+1,
                                 modifiers,
                                 current_point.x,
                                 current_point.y,
                                 clickcount,
                                 false );
      this_panel.dispatchEvent( mouse_e );
    }

  }
}


/* -------------------------------------------------------------------------
 *
 * MAIN
 *
 */

 /* Basic main program for testing purposes only. */

  public static void main(String[] args)
  {                                        // Test the default image
    ImageJPanel2 default_image = new ImageJPanel2();
    JFrame default_frame = new JFrame("Default Image");
    default_frame.setBounds(500,500,400,400);
    default_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    default_frame.getContentPane().add( default_image );
    default_frame.setVisible( true ); 

                                           // Test a huge image
    int rows = 10000;
    int cols = 1000;  
    float test_array[][] = new float[rows][cols];

    for ( int i = 0; i < rows; i++ )
      for ( int j = 0; j < cols; j++ )
      {
        if ( i % 50 == 0 )
          test_array[i][j] = 20 * i;
        else if ( j % 50 == 0 )
          test_array[i][j] = 20 * j;
        else
          test_array[i][j] = i * j;
      }
    VirtualArray2D varray2d = new VirtualArray2D(test_array);
    JFrame f = new JFrame("Test for ImageJPanel2");
    f.setBounds(0,0,500,500);
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    ImageJPanel2 panel = new ImageJPanel2();
    panel.setData( varray2d, true );
    panel.setNamedColorModel( IndexColorMaker.HEATED_OBJECT_SCALE_2, 
                              true,
                              true );
    panel.setGlobalWorldCoords( new CoordBounds( 0, 0, 20, 20 ) );
    panel.setLocalWorldCoords( new CoordBounds( 10, 10, 12, 10.2f ) );
    f.getContentPane().add(panel);
    f.setVisible(true);
  }
}
