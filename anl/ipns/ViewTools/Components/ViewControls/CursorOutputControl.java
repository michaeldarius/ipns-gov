
/*
 * File: CursorOutputControl.java
 *
 * Copyright (C) 2003 Brent Serum, Mike Miller
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
 * This work was supported by the National Science Foundation under grant
 * number DMR-0218882, and by the Intense Pulsed Neutron Source Division
 * of Argonne National Laboratory, Argonne, IL 60439-4845, USA.
 *
 * For further information, see <http://www.pns.anl.gov/ISAW/>
 *
 * Modified:
 *
 *  $Log$
 *  Revision 1.8  2005/03/09 22:36:05  millermi
 *  - Added methods get/setControlValue() and messaging of VALUE_CHANGED
 *    to enable controls to be linked.
 *  - Added "cm" as parameter to main() to test control with the
 *    ControlManager.
 *
 *  Revision 1.7  2004/03/12 02:24:53  millermi
 *  - Changed package, fixed imports.
 *
 *  Revision 1.6  2004/02/06 18:02:10  millermi
 *  - Added valid interval to setValue() javadocs.
 *  - setValue() now checks if index is valid, does nothing
 *    if the index is invalid.
 *
 *  Revision 1.5  2004/01/05 18:14:06  millermi
 *  - Replaced show()/setVisible(true) with WindowShower.
 *  - Removed excess imports.
 *
 *  Revision 1.4  2003/12/29 00:38:52  millermi
 *  - Removed setBorderTitle() since it was redundant.
 *
 *  Revision 1.3  2003/10/18 07:15:16  millermi
 *  - Added functionality for 2-D arrays of Strings for
 *    rows and columns of TextFields.
 *
 *  Revision 1.2  2003/10/16 17:06:00  millermi
 *  - Restructured this control to utilize the ViewControl
 *    base class.
 *
 */

package gov.anl.ipns.ViewTools.Components.ViewControls;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.util.Vector;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;

import gov.anl.ipns.ViewTools.UI.TextValueUI;
import gov.anl.ipns.Util.Sys.WindowShower;

/**
  * This class is a ViewControl (ActiveJPanel) with text fields set up for
  * cursor output.
  */
public class CursorOutputControl extends ViewControl
{
  private TextValueUI[][] TextField;
  private Box vert_box = new Box(BoxLayout.X_AXIS);
  private boolean ignore_change = false;
  
 /**
  *  The Constructor initilizes the values for the cursor output,
  *  and sets the headings and the border. Use this constructor for a 1-D
  *  column of textfields.
  *
  * @param name A 1-D array of strings which are the names of the text fields
  *		which display the cursor output in a column.
  */
  public CursorOutputControl( String[] name)
  {
    super("Cursor");
    if (name.length<1) return;

    TextField = new TextValueUI[name.length][1];
    for(int i = 0; i < name.length; i++)
    { 
      TextField[i][0] = new TextValueUI(name[i], Float.NaN);
      TextField[i][0].setEditable(false);
      vert_box.add(TextField[i][0]);
    }
    add(vert_box);
  }  
  
 /**
  *  The Constructor initilizes the values for the cursor output,
  *  and sets the headings and the border. Use this constructor for a 2-D
  *  array of textfields.
  *
  * @param name A 2-D array of strings which are the names of the text fields
  *		which display the cursor output in rows and columns.
  */
  public CursorOutputControl( String[][] name)
  {
    super("Cursor");
    if (name.length<1) return;
    if (name[0].length<1) return;

    TextField = new TextValueUI[name.length][name[0].length];
    Box tempcol;
    for(int col = 0; col < name[0].length; col++)
    { 
      tempcol = new Box(BoxLayout.Y_AXIS);	 
      for(int row = 0; row < name.length; row++)
      { 
	TextField[row][col] = new TextValueUI(name[row][col], Float.NaN);
	TextField[row][col].setEditable(false);
	tempcol.add(TextField[row][col]);
      }
      vert_box.add(tempcol);
    }
    add(vert_box);
  }
  
 /**
  * Set the most recently changed cursor output to the value specified.
  *
  *  @param  value int[] containing combobox index [0] and selected index [1].
  */
  public void setControlValue(Object value)
  {
    if( value == null || !(value instanceof float[][]) )
      return;
    float[][] values = (float[][])value;
    // Make sure that if the values array exceeds the number of TextFields,
    // ignore the extra values.
    int num_rows = values.length;
    if( num_rows > TextField.length )
      num_rows = TextField.length;
    int num_cols = values[0].length;
    if( num_cols > TextField[0].length )
      num_cols = TextField[0].length;
    
    ignore_change = true;
    for( int row = 0; row < num_rows; row++ )
      for( int col = 0; col < num_cols; col++ )
        setValue(row,col,values[row][col]);
    ignore_change = false;
  }
  
 /**
  * Get float[][] containing values for each output control.
  *
  *  @return float[][] of values displayed in output controls.
  */
  public Object getControlValue()
  {
    // Build values array.
    float[][] values = new float[TextField.length][TextField[0].length];
    for( int row = 0; row < values.length; row++ )
      for( int col = 0; col < values[0].length; col++ )
        values[row][col] = TextField[row][col].getValue();
    return values;
  }

 /**
  * This function sets the value to be displayed in the text field.
  *
  * @param index The int which represents the text field for the value to
  *		 be displayed in, on interval [0,num_textfields - 1].
  * @param value The floating point value to be displayed.
  */
  public void setValue(int index, float value)
  {
    // prevent index out of bounds exception.
    if( index >= TextField.length )
      return;
    TextField[index][0].setValue(value);
    // This if statement will prevent VALUE_CHANGED to be sent out when
    // the setControlValue() method is called.
    if( !ignore_change )
      send_message(VALUE_CHANGED);
  }

 /**
  * This function sets the value to be displayed in the text field for a
  * 2-D group of textfields.
  *
  * @param row The row number of this textfield, on interval [0,#rows-1]
  * @param col The column number of this textfield, on interval [0,#cols-1]
  * @param value The floating point value to be displayed.
  */
  public void setValue(int row, int col, float value)
  {
    // prevent index out of bounds exception.
    if( row >= TextField.length )
      return;
    if( col >= TextField[0].length )
      return;
    
    TextField[row][col].setValue(value);
    // This if statement will prevent VALUE_CHANGED to be sent out when
    // the setControlValue() method is called.
    if( !ignore_change )
      send_message(VALUE_CHANGED);
  }

 /**
  * This function gets the value displayed in the text field.
  *
  * @param  index The int which represents the text field for the value to
  *		  be displayed in, on interval [0,num_textfields - 1].
  * @return Returns a float value displayed in text field. If index is
  *         invalid, Float.NaN is returned.
  */
  public float getValue(int index)
  {
    // prevent index out of bounds exception.
    if( index >= TextField.length )
      return Float.NaN;
    return TextField[index][0].getValue();
  }

 /**
  * This function gets the value displayed in a text field that is part of a
  * 2-D group of textfields.
  *
  * @param  row The row number of this textfield, on interval [0,#rows-1]
  * @param  col The column number of this textfield, on interval [0,#cols-1]
  * @return The floating point value displayed by the text field. If the
  *         row or col is invalid, Float.NaN is returned.
  */
  public float getValue(int row, int col)
  {
    // prevent index out of bounds exception.
    if( row >= TextField.length )
      return Float.NaN;
    if( col >= TextField[0].length )
      return Float.NaN;
    return TextField[row][col].getValue();
  }
  
 /**
  *  Test purposes only... If "cm" is passed as an argument, the
  *  ControlManager will link controls.
  */
  public static void main( String args[] ) 
  {
    // If cm is passed in, test with control manager.
    if( args.length > 0 && args[0].equalsIgnoreCase("cm") )
    {
      int num_controls = 3;
      String[] menu = {"X","Y"};
      final ViewControl[] controls = new ViewControl[num_controls];
      controls[0] = new CursorOutputControl(menu);
      controls[0].setTitle("Cursor1");
      controls[1] = new CursorOutputControl(menu);
      controls[1].setTitle("Cursor2");
      controls[2] = new CursorOutputControl(menu);
      controls[2].setTitle("Cursor3");
      
      String[] keys = new String[num_controls];
      keys[0] = "CursorOutput";
      keys[1] = "CursorOutput";
      keys[2] = "CursorOutput";
      
      JFrame frame = ControlManager.makeManagerTestWindow( controls, keys );
      frame.addMouseMotionListener( new MouseMotionAdapter(){
          public void mouseDragged( MouseEvent me )
	  {
	    int button = me.getModifiers();
	    Point pt = me.getPoint();
	    // Determine the mouse button pressed and alter the associated
	    // cursor output control.
	    if( button == MouseEvent.BUTTON1_MASK )
	    {
	      ((CursorOutputControl)controls[0]).setValue(0,(float)pt.x);
	      ((CursorOutputControl)controls[0]).setValue(1,(float)pt.y);
	    }
	    else if( button == MouseEvent.BUTTON2_MASK )
	    {
	      ((CursorOutputControl)controls[1]).setValue(0,(float)pt.x);
	      ((CursorOutputControl)controls[1]).setValue(1,(float)pt.y);
	    }
	    else if( button == MouseEvent.BUTTON3_MASK )
	    {
	      ((CursorOutputControl)controls[2]).setValue(0,(float)pt.x);
	      ((CursorOutputControl)controls[2]).setValue(1,(float)pt.y);
	    }
	  }
	} );
      WindowShower shower = new WindowShower(frame);
      java.awt.EventQueue.invokeLater(shower);
      shower = null;
      return;
    }
    JFrame tester = new JFrame("CursorOutputControl Test");
    tester.setBounds(0,0,400,200);
    tester.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    //String[] menu = {"menu1","Menu2","Menu3","Menu4","Menu5"};
    String[][] menu = new String[3][4];
    for( int i = 0; i < 3; i++ )
      for( int j = 0; j < 4; j++ )
	menu[i][j] = "Menu" + Integer.toString(i) + Integer.toString(j);
    CursorOutputControl coc = new CursorOutputControl( menu );
    coc.setTitle("Border");
    coc.setValue( 0,2,Float.NaN);
    /*
    float[][] values = new float[1][1];
    values[0][0] = 1.1f;
    coc.setControlValue(values);
    */
    tester.getContentPane().add(coc);
    WindowShower shower = new WindowShower(tester);
    java.awt.EventQueue.invokeLater(shower);
    shower = null;
  }
} 
