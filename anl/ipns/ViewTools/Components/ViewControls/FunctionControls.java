/*
 * File:  FunctionControls.java
 *
 * Copyright (C) 2003 Brent Serum
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
 * of Argonne National Laboratory, Argonne, IL 60439-4845, USA and by
 * the National Science Foundation under grant number DMR-0218882.
 *
 * For further information, see <http://www.pns.anl.gov/ISAW/>
 *
 * Modified:
 *
 * $Log$
 * Revision 1.44  2004/12/08 22:03:48  serumb
 * Set the x and y range controls on zoom and reset zoom.
 *
 * Revision 1.43  2004/11/12 03:34:03  millermi
 * - Reversed min/max for y-scale to reflect changed to
 *   fvc.getAxisInformation().
 *
 * Revision 1.42  2004/11/11 19:49:37  millermi
 * - Changed LogScaleUtil functions to match reimplementation.
 *
 * Revision 1.41  2004/11/05 22:02:44  millermi
 * - Revised method for generating log cursor readouts.
 * - Removed getLogScale().
 *
 * Revision 1.40  2004/10/06 20:32:50  serumb
 * Removed the log scale slider control because it is not used with
 * the true log scale.
 *
 * Revision 1.39  2004/09/15 21:55:47  millermi
 * - Updated LINEAR, TRU_LOG, and PSEUDO_LOG setting for AxisInfo class.
 *   Adding a second log required the boolean parameter to be changed
 *   to an int. These changes may affect any ObjectState saved configurations
 *   made prior to this version.
 *
 * Revision 1.38  2004/07/28 19:38:35  robertsonj
 * Changed the curser readouts to read the correct log coordinates depending
 * on which scale is chosen to be logerithmic
 *
 * Revision 1.37  2004/07/02 20:12:32  serumb
 * Added control to set the shift factor for the offset.
 *
 * Revision 1.36  2004/06/16 22:11:21  serumb
 * Repaint components after changes made to the graph.
 *
 * Revision 1.35  2004/06/10 23:28:26  serumb
 * Add a legend control.
 *
 * Revision 1.34  2004/05/20 16:33:53  serumb
 * Removed unused variables.
 *
 * Revision 1.33  2004/04/21 02:34:40  millermi
 * - Fixed bug that incorrectly named selected indices.
 *
 * Revision 1.32  2004/04/16 20:25:41  millermi
 * - Now uses new methods from the IVirtualArrayList1D.
 *
 * Revision 1.31  2004/03/15 23:52:01  dennis
 * Cleaned up imports and changed references to static fields to
 * be interms of the class.
 *
 * Revision 1.30  2004/03/12 22:57:52  serumb
 * Now uses IVirtualArrayList1D in place of IVirtualArray1D.
 *
 * Revision 1.29  2004/03/12 21:08:46  serumb
 * Took out references to DataSetData, replaced them with
 * calls to setAxisInfo from IVirtualArray1D.
 *
 * Revision 1.28  2004/03/12 03:31:47  dennis
 * Moved to package gov.anl.ipns.ViewTools.Components.ViewControls
 *
 * Revision 1.27  2004/03/12 02:55:56  millermi
 * - Changed package, fixed most of the imports.
 *
 * Revision 1.26  2004/03/11 23:35:41  serumb
 * Removed String entry objects.
 *
 * Revision 1.25  2004/03/10 19:34:22  serumb
 * Added kill method.
 *
 * Revision 1.24  2004/03/10 16:38:19  serumb
 * Changed the action listener to listen for the correct
 * message when listening to the axis and annotation
 * overlay controls.
 *
 * Revision 1.23  2004/01/30 22:29:23  millermi
 * - Added new messaging String paths when listened for by action
 *   listeners.
 *
 * Revision 1.22  2004/01/09 21:12:46  serumb
 * Fixed problem with x and y range when in
 * log axes.
 *
 * Revision 1.21  2004/01/09 20:32:56  serumb
 * Utilize getLocalLogWorldCoords to correct log
 * transformations.
 *
 * Revision 1.20  2004/01/06 23:11:15  serumb
 * Put in the correct bounds for all log scale util.
 *
 * Revision 1.19  2004/01/06 22:50:24  serumb
 * Put in the correct bounds for the log scale util.
 *
 * Revision 1.18  2003/12/18 22:42:13  millermi
 * - This file was involved in generalizing AxisInfo2D to
 *   AxisInfo. This change was made so that the AxisInfo
 *   class can be used for more than just 2D axes.
 *
 * Revision 1.17  2003/11/06 01:37:50  serumb
 * Set the initial colors for the error bar and point marker buttons.
 *
 * Revision 1.15  2003/11/05 17:49:51  serumb
 * Changed the cursor output strings to X and Y.
 *
 * Revision 1.14  2003/10/31 18:14:32  dennis
 * Since the frame containing the controls may be reused,
 * call validate() to get the displayed contents updated properly.
 *
 * Revision 1.13  2003/10/31 17:55:06  dennis
 * Added constructor that builds the controls in an existing frame.
 * Changed the close_frame() and display_controls() methods so that
 * the current position of the frame is retained when opening and
 * closing the controls.
 *
 * Revision 1.12  2003/10/21 20:20:01  serumb
 * Now uses the CursorOutputControl.
 *
 * Revision 1.11  2003/09/11 21:21:48  serumb
 * Added control to show the cursor readings.
 *
 * Revision 1.10  2003/08/29 18:58:13  serumb
 * Change the color of the button text for the color buttons to the
 * color selected.
 *
 * Revision 1.9  2003/08/08 21:05:21  serumb
 * Added get_frame() method.
 *
 * Revision 1.8  2003/08/08 18:29:49  serumb
 * Updates x and y range values when message is recieved.
 *
 * Revision 1.7  2003/08/06 19:33:19  serumb
 * Added controls for adding grid lines.
 *
 * Revision 1.6  2003/08/05 23:28:31  serumb
 * Zoom controls adjust to log and linear axes.
 *
 * Revision 1.5  2003/07/31 18:51:20  serumb
 * Changed the log scale listener so it only acts if the
 * logarithmic axies are set.
 *
 * Revision 1.4  2003/07/30 20:54:24  serumb
 * Added control slider for the log scale, and a combobox to set
 * axies logarithmic.
 *
 * Revision 1.3  2003/07/17 20:40:51  serumb
 * Changed the zoom controls to fit better, and allow more
 * freedom for the user.
 *
 * Revision 1.2  2003/07/10 21:46:42  serumb
 * Added controls for zooming on the y axis.
 *
 */
package gov.anl.ipns.ViewTools.Components.ViewControls;

//import DataSetTools.components.ParametersGUI.*;
//import DataSetTools.components.ui.*;
import gov.anl.ipns.ViewTools.UI.*; 
import gov.anl.ipns.ViewTools.Panels.Transforms.*; 
import gov.anl.ipns.ViewTools.Components.*;  // IVirtualArrayList1D
import gov.anl.ipns.ViewTools.Components.OneD.*;
import gov.anl.ipns.ViewTools.Components.Transparency.*;  //Axis Overlays
import gov.anl.ipns.ViewTools.Panels.Graph.*;//GraphJPanel
                                                                                                                                               
// component changes
import java.awt.*;
import java.awt.event.*;

// Component location and resizing within the big_picture
import javax.swing.*;
import javax.swing.border.*;  

/*
 * This class creates the controls and adds them to the control panel.
 */

  public class FunctionControls
  {

  private IVirtualArrayList1D Varray1D;
  private FunctionViewComponent fvc;
  private GraphJPanel gjp;
  private JPanel big_picture = new JPanel();

  private JPanel panel1      = new JPanel(  );
  private JPanel panel2      = new JPanel(  );
  private JPanel panel3      = new JPanel(  );
  private JPanel RboxPanel   = new JPanel(  );
  private JPanel TFP         = new JPanel(  );
  private JPanel controlpanel= new JPanel(  );
  private JPanel label_panel = new JPanel(  );
  private JPanel z_panel     = new JPanel(  );
  private String label1      = "Line Selected";
  private String label2      = "Line Style";
  private String label3      = "Line Width";
  private String label4      = "Point Marker";
  private String label5      = "Point Marker Size";
  private String label6      = "Error Bars";
  private String label7      = "Shift";
  private String label8      = "Logarithmic Axis";
  private JComboBox LineBox  = new JComboBox(  );
  private JComboBox LineStyleBox = new JComboBox(  );
  private JComboBox LineWidthBox = new JComboBox(  );
  private JComboBox PointMarkerBox = new JComboBox(  );
  private JComboBox PointMarkerSizeBox = new JComboBox(  );
  private JComboBox ErrorBarBox = new JComboBox(  );
  private JComboBox ShiftBox = new JComboBox(  );
  private JComboBox ShiftFactor = new JComboBox(  );
  private JComboBox LogBox = new JComboBox(  );
  private String[] lines;
  private String[] line_type;
  private String[] line_width;
  private String[] mark_types;
  private String[] mark_size;
  private String[] bar_types;
  private String[] shift_types;
  private String[] log_placements;
  private ButtonControl LineColor;
  private ButtonControl MarkColor;
  private ButtonControl ErrorColor;
  private int line_index     = 1;
  private int linewidth      = 1;
  private Box leftBox        = new Box( 1 );
  private Box rightBox       = new Box( 1 );
  private Box control_box    = new Box( 0 );
  private LabelCombobox labelbox1;
  private LabelCombobox labelbox2;
  private LabelCombobox labelbox3;
  private LabelCombobox labelbox4;
  private LabelCombobox labelbox5;
  private LabelCombobox labelbox6;
  private LabelCombobox labelbox7;
  private LabelCombobox labelbox8;
  private LabelCombobox labelbox9;
  private JLabel control_label;
  private Font label_font;
  private TextRangeUI x_range; 
  private TextRangeUI y_range;
  private TitledBorder border;
 
  private Box vert_box = new Box(1);
  private ControlCheckboxButton axis_checkbox = new ControlCheckboxButton(true);
  private ControlCheckboxButton annotation_checkbox = 
                                    new ControlCheckboxButton(  );
  private ControlCheckboxButton legend_checkbox = 
                                    new ControlCheckboxButton(  );
  private double log_scale = 10;
  private float shift_factor = 1;
  private ViewControlsPanel main_panel;
  private JFrame the_frame;

  private CursorOutputControl cursor;

  public static final int FRAME_WIDTH  = 480; 
  public static final int FRAME_HEIGHT = 350; 
  
  /**
   *  Constructor that builds the controls in an existing frame.
   */ 
  public FunctionControls( IVirtualArrayList1D varr, 
                           GraphJPanel graph_j_panel,
                           JPanel display_panel, 
                           FunctionViewComponent FVC,
                           JFrame frame ) {
    the_frame = frame;
    main_panel = new ViewControlsPanel();
    Varray1D = varr;
    fvc = FVC;
    gjp = graph_j_panel;
    big_picture = display_panel;
    buildControls();

    main_panel.addViewControl(controlpanel);
    the_frame.setSize( FRAME_WIDTH, FRAME_HEIGHT );
    the_frame.getContentPane().removeAll();
    the_frame.getContentPane().add( (JComponent)main_panel.getPanel() );
    the_frame.validate();
  }

  /**
   *  Constructor that builds the controls in a new frame.
   */ 
  public FunctionControls( IVirtualArrayList1D varr, 
                           GraphJPanel graph_j_panel,
                           JPanel display_panel, 
                           FunctionViewComponent FVC) {
    this( varr, graph_j_panel, display_panel, FVC, 
          new JFrame( "ISAW Function View Controls" ) );
  }


  public void buildControls() {
    label_panel.setLayout( new FlowLayout( 1 ) );
    label_font      = new Font( "Times", Font.PLAIN, 16 );
    control_label   = new JLabel( "Controls" );
    control_label.setFont( label_font );
    label_panel.add( control_label );
    
    String group_id;
    
    lines = new String[Varray1D.getNumSelectedGraphs(  )];
    int index = 0;
    for( int i = 0; i < Varray1D.getNumGraphs(  ); i++ ) {
      if( Varray1D.isSelected(i) )
      {
        group_id   = Varray1D.getGraphTitle( i );
        lines[index++]   = "Group ID:" + group_id;
      }
    }
          
    //LineBox = new JComboBox(lines);
    labelbox1 = new LabelCombobox( label1, lines );
                                                                                   
    line_type      = new String[5];
    line_type[0]   = "Solid";
    line_type[1]   = "Dashed";
    line_type[2]   = "Dotted";
    line_type[3]   = "Dash Dot Dot";
    line_type[4]   = "Transparent";
                                                                                   
    //LineStyleBox = new JComboBox(line_type);
    labelbox2   = new LabelCombobox( label2, line_type );
                                                                                   
    line_width      = new String[5];
    line_width[0]   = "1";
    line_width[1]   = "2";
    line_width[2]   = "3";
    line_width[3]   = "4";
    line_width[4]   = "5";
    labelbox3       = new LabelCombobox( label3, line_width );
                                                                                   
    mark_types      = new String[6];
    mark_types[0]   = "DOT";
    mark_types[1]   = "PLUS";
    mark_types[2]   = "STAR";
    mark_types[3]   = "BOX";
    mark_types[4]   = "CROSS";
    mark_types[5]   = "NO POINT MARKS";
    labelbox4       = new LabelCombobox( label4, mark_types );
    labelbox4.setSelected( 5 );
                                                                                   
    mark_size      = new String[5];
    mark_size[0]   = "1";
    mark_size[1]   = "2";
    mark_size[2]   = "3";
    mark_size[3]   = "4";
    mark_size[4]   = "5";
    labelbox5      = new LabelCombobox( label5, mark_size );
    labelbox5.setSelected( 1 );
                                                                                   
    bar_types      = new String[3];
    bar_types[1]   = "At Points";
    bar_types[2]   = "At Top";
    bar_types[0]   = "None";
    labelbox6      = new LabelCombobox( label6, bar_types );
                                                                                   
    shift_types    = new String[3];
    shift_types[0]   = "Diagonal";
    shift_types[1]   = "Vertical";
    shift_types[2]   = "Overlaid";
    labelbox7      = new LabelCombobox( label7, shift_types );
    labelbox7.setSelected( 2 );

    log_placements = new String[4];
    log_placements[0] = "None";
    log_placements[1] = "X";
    log_placements[2] = "Y";
    log_placements[3] = "X and Y";
    labelbox8      = new LabelCombobox( label8, log_placements );

    String [] factors      = new String[3];
    factors[0]   = "1";
    factors[1]   = "1.5";
    factors[2]   = "2";
    labelbox9      = new LabelCombobox( "Shift Factors", factors );

   
    LineColor   = new ButtonControl( "Line Color" );
    MarkColor   = new ButtonControl( "Point Marker Color" );
    MarkColor.button.setForeground( Color.red );
    ErrorColor  = new ButtonControl( "Error Bar Color" );
    ErrorColor.button.setForeground( Color.blue );
    axis_checkbox.setTitle( "Axis Overlay" );
    annotation_checkbox.setTitle( "Annotation Overlay" );
    legend_checkbox.setTitle( "Legend Overlay" );
   
    x_range = new TextRangeUI("X Range", gjp.getXmin(), gjp.getXmax());
    y_range = new TextRangeUI("Y Range", gjp.getYmin(), gjp.getYmax()); 
    String the_string[] = {"X ","Y "};
    cursor = new CursorOutputControl(the_string);

    GridLayout G_lout = new GridLayout( 1, 1 );

    panel1.setLayout( G_lout );
    panel2.setLayout( G_lout );
    panel3.setLayout( G_lout );
    panel1.add( LineColor.button );
    panel2.add( MarkColor.button );
    panel3.add( ErrorColor.button );
                                                                                   
    // the left box is the left side of the control panel
    leftBox.add( labelbox1.theBox );
    leftBox.add( labelbox2.theBox );
    leftBox.add( labelbox3.theBox );
    leftBox.add( panel1 );
    leftBox.add( labelbox4.theBox );
    leftBox.add( labelbox5.theBox );
    leftBox.add( panel2 );
    leftBox.add( labelbox6.theBox );
    leftBox.add( panel3 );
    leftBox.add( labelbox7.theBox );
    leftBox.add( labelbox9.theBox );
        
                                                                                 
    LineBox              = labelbox1.cbox;
    LineStyleBox         = labelbox2.cbox;
    LineWidthBox         = labelbox3.cbox;
    PointMarkerBox       = labelbox4.cbox;
    PointMarkerSizeBox   = labelbox5.cbox;
    ErrorBarBox          = labelbox6.cbox;
    ShiftBox             = labelbox7.cbox;
    ShiftFactor          = labelbox9.cbox;
    LogBox               = labelbox8.cbox;
    
   
    control_box.add(leftBox);
    vert_box.add( x_range );
    vert_box.add( y_range );

    border = new TitledBorder(LineBorder.createBlackLineBorder(),"Scale");
    border.setTitleFont( FontUtil.BORDER_FONT );

    TFP.setLayout( G_lout );
    TFP.setBorder( border );
    TFP.add( vert_box );
    
    
    rightBox.add( axis_checkbox );
    rightBox.add( annotation_checkbox );
    rightBox.add( legend_checkbox );
    rightBox.add( TFP );
    rightBox.add( cursor );
    rightBox.add( labelbox8 );
                                                                              
    RboxPanel.setLayout(G_lout);
    RboxPanel.add(rightBox);
                                                                                   
    control_box.add(RboxPanel);
                                                                                   
    controlpanel.setLayout( G_lout );
    controlpanel.add( control_box );
                                                                                   
    LineBox.addActionListener( new ControlListener(  ) );
    LineStyleBox.addActionListener( new ControlListener(  ) );
    LineWidthBox.addActionListener( new ControlListener(  ) );
    PointMarkerBox.addActionListener( new ControlListener(  ) );
    PointMarkerSizeBox.addActionListener( new ControlListener(  ) );
    ErrorBarBox.addActionListener( new ControlListener(  ) );
    LineColor.addActionListener( new ControlListener(  ) );
    MarkColor.addActionListener( new ControlListener(  ) );
    ErrorColor.addActionListener( new ControlListener(  ) );
    axis_checkbox.addActionListener( new ControlListener(  ) );
    annotation_checkbox.addActionListener( new ControlListener(  ) );
    legend_checkbox.addActionListener( new ControlListener(  ) );
    ShiftBox.addActionListener( new ControlListener(  ) );
    ShiftFactor.addActionListener( new ControlListener(  ) );
    LogBox.addActionListener( new ControlListener(  ) );
    x_range.addActionListener( new RangeListener(  ) );
    y_range.addActionListener( new RangeListener(  ) );
    gjp.addActionListener( new ImageListener(  ) );
    fvc.addActionListener( new ImageListener(  ) ); 
  }
  
  public ViewControlsPanel get_panel() {
    return main_panel;
  }
  
  public JFrame get_frame() {
    return the_frame;
  }

  public void display_controls() {
    the_frame.setVisible( true  );  //display the frame
  }

  public void close_frame() {
    // get the current location and reset the bounds, so that 
    // the frame will reappear in the same place.

    Point location =  the_frame.getLocation();
    the_frame.setVisible( false );
    the_frame.setBounds( location.x, location.y, FRAME_WIDTH, FRAME_HEIGHT );
  }
  
  // method to remove panels upon removal of view components 
  public void kill() {
  }

  private class RangeListener implements ActionListener {

    public void actionPerformed( ActionEvent ae ) {
          // System.out.println("Entered: " + x_range.getText() );
          // System.out.println("Min = " + x_range.getMin() );
          // System.out.println("Max = " + x_range.getMax() );
      LogScaleUtil loggery = new LogScaleUtil(gjp.getPositiveYmin(),
                                              gjp.getYmax(),
                                              gjp.getPositiveYmin(),
					      gjp.getYmax());
      LogScaleUtil loggerx = new LogScaleUtil(gjp.getPositiveXmin(),
        			              gjp.getXmax(), 
				              gjp.getPositiveXmin(),
					      gjp.getXmax());

      if(gjp.getLogScaleX() == true && gjp.getLogScaleY() == true) {

        gjp.setZoom_region( loggerx.toDest(x_range.getMin()),
        		    loggery.toDest(y_range.getMax()),
        		    loggerx.toDest(x_range.getMax()),
        		    loggery.toDest(y_range.getMin()) );
      }
      else if(gjp.getLogScaleX() == true && gjp.getLogScaleY() == false) { 
        gjp.setZoom_region( loggerx.toDest(x_range.getMin()), 
        		    y_range.getMax(),
        		    loggerx.toDest(x_range.getMax()),
        		    y_range.getMin() );
      }
      else
      gjp.setZoom_region( x_range.getMin(), y_range.getMax(),
        		  x_range.getMax(), y_range.getMin() );
    }
  }
 
 private class ImageListener implements ActionListener {
    //~ Methods ****************************************************************

                                                                                             
    public void actionPerformed( ActionEvent ae ) {
      String message = ae.getActionCommand(  );
     
       float xmin,xmax,ymin,ymax;
       xmin = gjp.getPositiveXmin();
       xmax = gjp.getXmax();
       ymin = gjp.getPositiveYmin();
       ymax = gjp.getYmax();
       
       LogScaleUtil loggery = new LogScaleUtil(ymin,ymax,ymin,ymax);
       LogScaleUtil loggerx = new LogScaleUtil(xmin,xmax,xmin,xmax); 

      if( message.equals("Reset Zoom") || message.equals("Zoom In") ) {
      
       float x_lower, x_upper, y_lower, y_upper;	      
       CoordBounds range = gjp.getLocalWorldCoords();
       if (range.getY1() > range.getY2())
	       range.invertBounds();
       if (range.getX1() > range.getX2())
       {        
         x_lower = range.getX2();
         x_upper = range.getX1();
       }
       else{
         x_lower = range.getX1();
         x_upper = range.getX2();
       }  
       y_lower = range.getY1();
       y_upper = range.getY2();
	      
       if (gjp.getLogScaleY()){
         if (ymin >  y_lower)
	      y_lower = ymin;
       }
       if (gjp.getLogScaleX()){
	 if (xmin >  x_lower)
 	      x_lower = xmin;
       } 
       
        x_range.setMin(x_lower);
        x_range.setMax(x_upper);
        y_range.setMin(y_lower);
        y_range.setMax(y_upper);
	
      }	 
      
      else if(message.equals("Cursor Moved")){
         if(gjp.getLogScaleX() == true && gjp.getLogScaleY() == true) {
             cursor.setValue(0,loggerx.toSource(gjp.getCurrent_WC_point().x));
             cursor.setValue(1,loggery.toSource(gjp.getCurrent_WC_point().y));

           }
           else if(gjp.getLogScaleX() == false && gjp.getLogScaleY() == true) {
             cursor.setValue(0,gjp.getCurrent_WC_point().x);
             cursor.setValue(1,loggery.toSource(gjp.getCurrent_WC_point().y));
           }
           else if(gjp.getLogScaleX() == true && gjp.getLogScaleY() == false) {
             cursor.setValue(0,loggerx.toSource(gjp.getCurrent_WC_point().x));
             cursor.setValue(1,gjp.getCurrent_WC_point().y);
           }
           else {
             cursor.setValue(0,gjp.getCurrent_WC_point().x);
             cursor.setValue(1,gjp.getCurrent_WC_point().y);

           } 
      }
    }
 }
  private class ControlListener implements ActionListener {
    //~ Methods ****************************************************************
    public void actionPerformed( ActionEvent ae ) {
      String message = ae.getActionCommand(  );

      //System.out.println( "action command: " + message );
      //System.out.println( "action event: " + ae.getSource() );
     
      /*
         listens for the color buttons and displays a color chooser
         and sets the object to the appropriate color.
     */  
      if( message.equals( "BUTTON_PRESSED" ) ) {
        if( ae.getSource(  ) == LineColor ) {
          Color c = JColorChooser.showDialog( null, "color chart", Color.black );

          if( c != null ) {
            LineColor.button.setForeground( c );
            gjp.setColor( c, line_index, true );
          }
        }

        if( ae.getSource(  ) == MarkColor ) {
          Color m = JColorChooser.showDialog( null, "color chart", Color.black );

          if( m != null ) {
            MarkColor.button.setForeground( m );
            gjp.setMarkColor( m, line_index, true );
          }
        }
        if( ae.getSource(  ) == ErrorColor ) {
          Color e = JColorChooser.showDialog( null, "color chart", Color.black );

          if( e != null ) {
            ErrorColor.button.setForeground( e );
            gjp.setErrorColor( e, line_index, true );
          }
        }
      } else if( message.equals( "Button Pressed" )) {
 
        if( ae.getSource() instanceof ControlCheckboxButton )
           {
             ControlCheckboxButton ccb = (ControlCheckboxButton)ae.getSource();
             if( ccb.getTitle().equals("Axis Overlay") )
             {
               AxisOverlay2D axis = (AxisOverlay2D)big_picture.getComponent(
                                    big_picture.getComponentCount() - 2 );
               axis.editGridLines();
             }
             else if( ccb.getTitle().equals("Annotation Overlay") )
             {
               AnnotationOverlay note = (AnnotationOverlay)
                                 big_picture.getComponent(
                                 big_picture.getComponentCount() - 3 );
               note.editAnnotation();
             }
             else if( ccb.getTitle().equals("Legend Overlay") )
             {
               LegendOverlay legend = (LegendOverlay)
                                 big_picture.getComponent(
                                 big_picture.getComponentCount() - 4 );
               legend.editLegend();
             }
            fvc.paintComponents( );
           }
        /* 
           listens for the edit annotation button and brings up an edit 
           annotation pane.
        */
      } else if( message.equals( "comboBoxChanged" ) ) {
        // System.out.println("action" + LineBox.getSelectedItem());
        // System.out.println("index" + LineBox.getSelectedIndex());
        // System.out.println("source " + ae.getSource());

        /* 
           gets the index for the line selected. The index is used for the
           line that is pointed at so 1 is added to the line index for 
           selected lines.
        */
        if( ae.getSource(  ) == LineBox ) {
          line_index = LineBox.getSelectedIndex(  ) + 1;

          GraphData gd = ( GraphData )gjp.graphs.elementAt( line_index );
         
          /*
            sets the line style combo box to the style of the line selected.
          */
          if( 
            gjp.getStroke( line_index ).equals( 
                gjp.strokeType( GraphJPanel.DOTTED, line_index ) ) ) {
            LineStyleBox.setSelectedIndex( 2 );
          } else if( 
           gjp.getStroke( line_index ).equals( 
                gjp.strokeType( GraphJPanel.LINE, line_index ) ) ) {
            LineStyleBox.setSelectedIndex( 0 );
          } else if( 
            gjp.getStroke( line_index ).equals( 
                gjp.strokeType( GraphJPanel.DASHED, line_index ) ) ) {
            LineStyleBox.setSelectedIndex( 1 );
          } else if( 
            gjp.getStroke( line_index ).equals( 
                gjp.strokeType( GraphJPanel.DASHDOT, line_index ) ) ) {
            LineStyleBox.setSelectedIndex( 3 );
          } else if( 
            gjp.getStroke( line_index ).equals( 
                gjp.strokeType( GraphJPanel.TRANSPARENT, line_index ) ) ) {
            LineStyleBox.setSelectedIndex( 4 );
          }
          
          /*
            sets the mark size combo box to the mark size of the line selected.
          */
          if( gd.marksize == 1 ) {
            PointMarkerSizeBox.setSelectedIndex( 0 );
          } else if( gd.marksize == 2 ) {
            PointMarkerSizeBox.setSelectedIndex( 1 );
          } else if( gd.marksize == 3 ) {
            PointMarkerSizeBox.setSelectedIndex( 2 );
          } else if( gd.marksize == 4 ) {
            PointMarkerSizeBox.setSelectedIndex( 3 );
          } else if( gd.marksize == 5 ) {
            PointMarkerSizeBox.setSelectedIndex( 4 );
          }

          /*
            sets the mark type combo box to the mark type of the line selected.
          */
          if( gd.marktype == 0 ) {
            PointMarkerBox.setSelectedIndex( 5 );
          } else if( gd.marktype == 1 ) {
            PointMarkerBox.setSelectedIndex( 0 );
          } else if( gd.marktype == 2 ) {
            PointMarkerBox.setSelectedIndex( 1 );
          } else if( gd.marktype == 3 ) {
            PointMarkerBox.setSelectedIndex( 2 );
          } else if( gd.marktype == 4 ) {
            PointMarkerBox.setSelectedIndex( 3 );
          } else if( gd.marktype == 5 ) {
            PointMarkerBox.setSelectedIndex( 4 );
          }

          /*
            sets the line width combo box to the line width 
            of the line selected.
          */
          if( gd.linewidth == 1 ) {
            LineWidthBox.setSelectedIndex( 0 );
          } else if( gd.linewidth == 2 ) {
            LineWidthBox.setSelectedIndex( 1 );
          } else if( gd.linewidth == 3 ) {
            LineWidthBox.setSelectedIndex( 2 );
          } else if( gd.linewidth == 4 ) {
            LineWidthBox.setSelectedIndex( 3 );
          } else if( gd.linewidth == 5 ) {
            LineWidthBox.setSelectedIndex( 4 );
          }

          /*
            sets the error bar combo box to the error bar location
            of the line selected.
          */
          if( gd.getErrorLocation(  ) == 0 ) {
            ErrorBarBox.setSelectedIndex( 0 );
          } else if( gd.getErrorLocation(  ) == 11 ) {
            ErrorBarBox.setSelectedIndex( 1 );
          } else if( gd.getErrorLocation(  ) == 12 ) {
            ErrorBarBox.setSelectedIndex( 2 );
          } else if( gd.getErrorLocation(  ) == 13 ) {
            ErrorBarBox.setSelectedIndex( 3 );
          }

            MarkColor.button.setForeground( gjp.getMarkColor(line_index) );
            LineColor.button.setForeground( gjp.getColor(line_index) );
            ErrorColor.button.setForeground( gjp.getErrorColor(line_index) );
          
        /*
          Sets the appropriate line style
        */
        } else if( ae.getSource(  ) == LineStyleBox ) {
          if( LineStyleBox.getSelectedItem(  ).equals( "Solid" ) ) {
            gjp.setTransparent(false, line_index, false);
            gjp.setStroke( 
              gjp.strokeType( GraphJPanel.LINE, line_index ), line_index, true );
          }

          if( LineStyleBox.getSelectedItem(  ).equals( "Dashed" ) ) {
            gjp.setTransparent(false, line_index, false);
            gjp.setStroke( 
              gjp.strokeType( GraphJPanel.DASHED, line_index ), line_index, true );
          }

          if( LineStyleBox.getSelectedItem(  ).equals( "Dotted" ) ) {
            gjp.setTransparent(false, line_index, false);
            gjp.setStroke( 
              gjp.strokeType( GraphJPanel.DOTTED, line_index ), line_index, true );
          }

          if( LineStyleBox.getSelectedItem(  ).equals( "Dash Dot Dot" ) ) {
             gjp.setTransparent(false, line_index, false);
             gjp.setStroke( 
              gjp.strokeType( GraphJPanel.DASHDOT, line_index ), line_index, true );  
          }

          if( LineStyleBox.getSelectedItem(  ).equals( "Transparent" ) ) {
             gjp.setTransparent(true, line_index, true);
             gjp.setStroke( 
              gjp.strokeType( GraphJPanel.TRANSPARENT, line_index ), line_index, true );
          }

        /*
           sets the appropriate line width
        */
        } else if( ae.getSource(  ) == LineWidthBox ) {
          linewidth = LineWidthBox.getSelectedIndex(  ) + 1;

          gjp.setLineWidth( linewidth, line_index, true );

          if( LineStyleBox.getSelectedItem(  ).equals( "Solid" ) ) {
            gjp.setStroke( 
              gjp.strokeType( GraphJPanel.LINE, line_index ), line_index, true );
          }

          if( LineStyleBox.getSelectedItem(  ).equals( "Dashed" ) ) {
            gjp.setStroke( 
              gjp.strokeType( GraphJPanel.DASHED, line_index ), line_index, true );
          }

          if( LineStyleBox.getSelectedItem(  ).equals( "Dotted" ) ) {
            gjp.setStroke( 
              gjp.strokeType( GraphJPanel.DOTTED, line_index ), line_index, true );
          }

          if( LineStyleBox.getSelectedItem(  ).equals( "Dash Dot Dot" ) ) {
            gjp.setStroke( 
              gjp.strokeType( GraphJPanel.DASHDOT, line_index ), line_index, true );
          }
        /* 
          Listens for a point marker change and sets the appropriate
          point marker type.
        */  

        } else if( ae.getSource(  ) == PointMarkerBox ) {
          if( PointMarkerBox.getSelectedItem(  ).equals( "DOT" ) ) {
            gjp.setMarkType( GraphJPanel.DOT, line_index, true );
          } else if( PointMarkerBox.getSelectedItem(  ).equals( "PLUS" ) ) {
            gjp.setMarkType( GraphJPanel.PLUS, line_index, true );
          } else if( PointMarkerBox.getSelectedItem(  ).equals( "STAR" ) ) {
            gjp.setMarkType( GraphJPanel.STAR, line_index, true );
          } else if( PointMarkerBox.getSelectedItem(  ).equals( "BOX" ) ) {
            gjp.setMarkType( GraphJPanel.BOX, line_index, true );
          } else if( PointMarkerBox.getSelectedItem(  ).equals( "CROSS" ) ) {
            gjp.setMarkType( GraphJPanel.CROSS, line_index, true );
          } else if( 
            PointMarkerBox.getSelectedItem(  ).equals( "NO POINT MARKS" ) ) {
            gjp.setMarkType( 0, line_index, true );
          }

        /* 
          Listens for a point marker size  change and sets the appropriate
          point marker size.
        */  
        } else if( ae.getSource(  ) == PointMarkerSizeBox ) {
          if( PointMarkerSizeBox.getSelectedItem(  ).equals( "1" ) ) {
            gjp.setMarkSize( 1, line_index, true );
          } else if( PointMarkerSizeBox.getSelectedItem(  ).equals( "2" ) ) {
            gjp.setMarkSize( 2, line_index, true );
          } else if( PointMarkerSizeBox.getSelectedItem(  ).equals( "3" ) ) {
            gjp.setMarkSize( 3, line_index, true );
          } else if( PointMarkerSizeBox.getSelectedItem(  ).equals( "4" ) ) {
            gjp.setMarkSize( 4, line_index, true );
          } else if( PointMarkerSizeBox.getSelectedItem(  ).equals( "5" ) ) {
            gjp.setMarkSize( 4, line_index, true );
          }

        /* 
          Listens for a error bar change and sets the appropriate
          error bar location.
        */  
        } else if( ae.getSource(  ) == ErrorBarBox ) {
          //System.out.println("zoom region:"+ gjp.getZoom_region());
          //CoordBounds data_bound = getGlobalWorldCoords();
          //data_bound.getBounds()
          if( ErrorBarBox.getSelectedItem(  ).equals( "None" ) ) {
            gjp.setErrors( Varray1D.getErrorValues( line_index - 1  ), 0, 
                           line_index, true );
          } else if( ErrorBarBox.getSelectedItem(  ).equals( "At Points" ) ) {
            gjp.setErrors( Varray1D.getErrorValues( line_index - 1  ), 
                           GraphJPanel.ERROR_AT_POINT, line_index, true );
          } else if( ErrorBarBox.getSelectedItem(  ).equals( "At Top" ) ) {
            gjp.setErrors( Varray1D.getErrorValues( line_index - 1 ),
                           GraphJPanel.ERROR_AT_TOP, line_index, true );
          }

        /* 
          Listens for a line shift change and sets the appropriate
          line /shift.
        */  
        } else if( ae.getSource( ) == ShiftBox) {
            if ( ShiftBox.getSelectedItem( ).equals( "Diagonal" ))
              { 
                gjp.setMultiplotOffsets((int)(20 * shift_factor),
                                        (int)( 20 * shift_factor));
                gjp.repaint();
              } 
            else if( ShiftBox.getSelectedItem( ).equals( "Vertical" ))
              {
                gjp.setMultiplotOffsets(0,(int)(20 * shift_factor));
                gjp.repaint();
              }
            else if( ShiftBox.getSelectedItem( ).equals( "Overlaid" ))
              {
                gjp.setMultiplotOffsets(0,0);
                gjp.repaint();
              }
            else {
              gjp.setMultiplotOffsets(0,0);
              gjp.repaint();
            }
        } else if( ae.getSource( ) == ShiftFactor) {
            if (ShiftFactor.getSelectedItem( ).equals( "1" ))
            shift_factor = 1;
            if (ShiftFactor.getSelectedItem( ).equals( "1.5" ))
            shift_factor = 1.5f;
            if (ShiftFactor.getSelectedItem( ).equals( "2" ))
            shift_factor = 2;
            
            ShiftBox.setSelectedItem(ShiftBox.getSelectedItem());

        } else if( ae.getSource( ) == LogBox) {
            AxisOverlay2D note = (AxisOverlay2D)big_picture.getComponent(
                                 big_picture.getComponentCount() - 2);

            if ( LogBox.getSelectedItem( ).equals( "None" ))
            {
              Varray1D.setAxisInfo(AxisInfo.X_AXIS,
                        Varray1D.getAxisInfo(AxisInfo.X_AXIS).getMin(),
                        Varray1D.getAxisInfo(AxisInfo.X_AXIS).getMax(),
                        Varray1D.getAxisInfo(AxisInfo.X_AXIS).getLabel(),
                        Varray1D.getAxisInfo(AxisInfo.X_AXIS).getUnits(),
                        AxisInfo.LINEAR);
              Varray1D.setAxisInfo(AxisInfo.Y_AXIS,
                        Varray1D.getAxisInfo(AxisInfo.Y_AXIS).getMin(),
                        Varray1D.getAxisInfo(AxisInfo.Y_AXIS).getMax(),
                        Varray1D.getAxisInfo(AxisInfo.Y_AXIS).getLabel(),
                        Varray1D.getAxisInfo(AxisInfo.Y_AXIS).getUnits(),
                        AxisInfo.LINEAR);

              note.setXScale( AxisInfo.LINEAR );
              note.setYScale( AxisInfo.LINEAR );
              note.setTwoSided(false);
              gjp.setLogScaleX(false);
              gjp.setLogScaleY(false);
              fvc.paintComponents(  );
            }
            else if( LogBox.getSelectedItem().equals( "X" ))
            {
              Varray1D.setAxisInfo(AxisInfo.X_AXIS,
                        Varray1D.getAxisInfo(AxisInfo.X_AXIS).getMin(),
                        Varray1D.getAxisInfo(AxisInfo.X_AXIS).getMax(),
                        Varray1D.getAxisInfo(AxisInfo.X_AXIS).getLabel(),
                        Varray1D.getAxisInfo(AxisInfo.X_AXIS).getUnits(),
                        AxisInfo.TRU_LOG);
              Varray1D.setAxisInfo(AxisInfo.Y_AXIS,
                        Varray1D.getAxisInfo(AxisInfo.Y_AXIS).getMin(),
                        Varray1D.getAxisInfo(AxisInfo.Y_AXIS).getMax(),
                        Varray1D.getAxisInfo(AxisInfo.Y_AXIS).getLabel(),
                        Varray1D.getAxisInfo(AxisInfo.Y_AXIS).getUnits(),
                        AxisInfo.LINEAR);

              note.setXScale( AxisInfo.TRU_LOG );
              note.setYScale( AxisInfo.LINEAR );
              note.setTwoSided(false);
              gjp.setLogScaleX(true);
              gjp.setLogScaleY(false);
              fvc.paintComponents(  );
            }  
            else if( LogBox.getSelectedItem().equals( "Y" ))
            {
              Varray1D.setAxisInfo(AxisInfo.X_AXIS,
                        Varray1D.getAxisInfo(AxisInfo.X_AXIS).getMin(),
                        Varray1D.getAxisInfo(AxisInfo.X_AXIS).getMax(),
                        Varray1D.getAxisInfo(AxisInfo.X_AXIS).getLabel(),
                        Varray1D.getAxisInfo(AxisInfo.X_AXIS).getUnits(),
                        AxisInfo.LINEAR);
              Varray1D.setAxisInfo(AxisInfo.Y_AXIS,
                        Varray1D.getAxisInfo(AxisInfo.Y_AXIS).getMin(),
                        Varray1D.getAxisInfo(AxisInfo.Y_AXIS).getMax(),
                        Varray1D.getAxisInfo(AxisInfo.Y_AXIS).getLabel(),
                        Varray1D.getAxisInfo(AxisInfo.Y_AXIS).getUnits(),
                        AxisInfo.LINEAR);

              note.setYScale( AxisInfo.TRU_LOG );
              note.setXScale( AxisInfo.LINEAR );
              note.setTwoSided(false);
              gjp.setLogScaleY(true);
              gjp.setLogScaleX(false);
              fvc.paintComponents(  );
            }  
            else if( LogBox.getSelectedItem().equals( "X and Y" ))
            {
              Varray1D.setAxisInfo(AxisInfo.X_AXIS,
                        Varray1D.getAxisInfo(AxisInfo.X_AXIS).getMin(),
                        Varray1D.getAxisInfo(AxisInfo.X_AXIS).getMax(),
                        Varray1D.getAxisInfo(AxisInfo.X_AXIS).getLabel(),
                        Varray1D.getAxisInfo(AxisInfo.X_AXIS).getUnits(),
                        AxisInfo.TRU_LOG);
              Varray1D.setAxisInfo(AxisInfo.Y_AXIS,
                        Varray1D.getAxisInfo(AxisInfo.Y_AXIS).getMin(),
                        Varray1D.getAxisInfo(AxisInfo.Y_AXIS).getMax(),
                        Varray1D.getAxisInfo(AxisInfo.Y_AXIS).getLabel(),
                        Varray1D.getAxisInfo(AxisInfo.Y_AXIS).getUnits(),
                        AxisInfo.TRU_LOG);

              note.setXScale( AxisInfo.TRU_LOG );
              note.setYScale( AxisInfo.TRU_LOG );
              note.setTwoSided(false);
              gjp.setLogScaleX(true);
              gjp.setLogScaleY(true);
              fvc.paintComponents( );

            }
	    
	    // Reset range of x and y since log and linear may require different
	    // ranges (log sets min to least positive number in negative)
            AxisInfo xinfo = fvc.getAxisInformation(AxisInfo.X_AXIS);
            AxisInfo yinfo = fvc.getAxisInformation(AxisInfo.Y_AXIS);
	    x_range.setMin(xinfo.getMin());
	    x_range.setMax(xinfo.getMax());
	    y_range.setMin(yinfo.getMin());
	    y_range.setMax(yinfo.getMax());
          }  
      } 
        /* 
          Listens for an overlay change and sets the appropriate overlay.
        */  
      else if( message.equals( ControlCheckboxButton.CHECKBOX_CHANGED ) ) {
        ControlCheckboxButton control = 
                              ( ControlCheckboxButton )ae.getSource(  );
        int bpsize              = big_picture.getComponentCount(  );
        if( control.getTitle(  ).equals( "Annotation Overlay" ) ) {

          AnnotationOverlay note = ( AnnotationOverlay )big_picture.getComponent
                  ( big_picture.getComponentCount(  ) - 3 );

          if( !control.isSelected(  ) ) {
            note.setVisible( false );
          } else {
            note.setVisible( true );
            note.getFocus(  );
          }
        }
      
        else if( control.getTitle(  ).equals( "Legend Overlay" ) ) {
          LegendOverlay legend = ( LegendOverlay )big_picture.getComponent
                  ( big_picture.getComponentCount(  ) - 4 );

          if( !control.isSelected(  ) ) {
            legend.setVisible( false );
          } else {
            legend.setVisible( true );
            legend.getFocus(  );
          }
        }
      
        else if(control.getTitle().equals( "Axis Overlay" ) ) {
          JPanel back = (JPanel)big_picture.getComponent( bpsize - 1);
          if( !control.isSelected() ) {
            big_picture.getComponent(bpsize - 2).setVisible(false);

            back.getComponent(1).setVisible(false);
            back.getComponent(2).setVisible(false);
            back.getComponent(3).setVisible(false);
            back.getComponent(4).setVisible(false);
          }
          else {
           back.getComponent(1).setVisible(true);
           back.getComponent(2).setVisible(true);
           back.getComponent(3).setVisible(true);
           back.getComponent(4).setVisible(true);
           big_picture.getComponent(bpsize - 2).setVisible(true);
          }
        }
       fvc.paintComponents(  );
      }  
       fvc.paintComponents(  );
    }
  }

/*--------------------------- main -----------------------------------------
 *  Main program for testing purposes only.
 */

   public static void main( String[] args ) {
/*
    DataSet[] DSS = ( new IsawGUI.Util(  ) ).loadRunfile( 
           "/IPNShome/serumb/ISAW/SampleRuns/GPPD12358.RUN" );    
   
    int k = DSS.length - 1;

    System.out.println(" DSS " + DSS.length);

    DSS[k].setSelectFlag( 0, true );
    DSS[k].setSelectFlag( 3, true );

    DataSetData ArrayHandler = new DataSetData( DSS[k] );
  
    AxisInfo xaxis = ArrayHandler.getAxisInfo( AxisInfo.X_AXIS );
    AxisInfo yaxis = ArrayHandler.getAxisInfo( AxisInfo.Y_AXIS );
    System.out.println(
      "ArrayHandler info" + xaxis.getMax(  ) + "," + xaxis.getMin(  ) + "," +
      yaxis.getMax(  ) + "," + yaxis.getMin(  ));
  
    if( java.lang.Float.isNaN( xaxis.getMax(  ) ) ) {
      try {
        int c = System.in.read(  );
      } catch( Exception sss ) {}
    }
    FunctionViewComponent fvc = new FunctionViewComponent( ArrayHandler );

    IVirtualArrayList1D Varray1D = fvc.getArray(); 
    GraphJPanel graph_panel = new GraphJPanel();
    JPanel main_panel = new JPanel();
    
    FunctionControls fcontrols = new FunctionControls(Varray1D, graph_panel,
                                                      main_panel, fvc); 
    fcontrols.display_controls();
*/
  }  

}






 


