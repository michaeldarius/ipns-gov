/*
 * File:  Display.java
 *
 * Copyright (C) 2004, Mike Miller
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
 * This work was supported by the Intense Pulsed Neutron Source Division
 * of Argonne National Laboratory, Argonne, IL 60439-4845, USA.
 *
 * For further information, see <http://www.pns.anl.gov/ISAW/>
 *
 * Modified:
 *
 * $Log$
 * Revision 1.2  2004/03/13 07:42:05  millermi
 * - Removed unused imports.
 * - Finished factoring out Display from Display2D.
 * - ObjectState now implemented, but needs IViewComponet to
 *   extend IPreserveState before completion.
 * - Wrote meaningful help dialogue.
 *
 * Revision 1.1  2004/03/12 23:22:43  millermi
 * - Initial Version - Factored out common functionality between
 *   into displays this abstract base class.
 *
 */

package gov.anl.ipns.ViewTools.Displays;

import javax.swing.*;
import java.util.Vector;
import java.awt.Container;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.io.Serializable;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;

import gov.anl.ipns.ViewTools.UI.SplitPaneWithState;
import gov.anl.ipns.ViewTools.Components.*;
import gov.anl.ipns.ViewTools.Components.TwoD.*;
import gov.anl.ipns.ViewTools.Components.Menu.MenuItemMaker;
import gov.anl.ipns.ViewTools.Components.Menu.ViewMenuItem;
import gov.anl.ipns.ViewTools.Components.ViewControls.ViewControl;
import gov.anl.ipns.Util.Sys.WindowShower;
import gov.anl.ipns.ViewTools.UI.FontUtil;
import gov.anl.ipns.Util.Sys.PrintComponentActionListener;
import gov.anl.ipns.Util.Sys.SaveImageActionListener;

/**
 * Simple class to display an image, specified by an IVirtualArray2D or a 
 * 2D array of floats, in a frame. This class adds further implementation to
 * the ImageFrame2.java class for thorough testing of the ImageViewComponent.
 */
abstract public class Display extends JFrame implements IPreserveState,
                                                       Serializable
{  
  // complete viewer, includes controls and ijp
  protected transient Container pane;
  protected transient IViewComponent ivc;
  protected transient IVirtualArray data;
  protected transient JMenuBar menu_bar;
  protected transient Display this_viewer;
  protected Vector Listeners = new Vector();
  protected int current_view = 0;
  protected boolean add_controls = true;
  
 /**
  * Construct a frame with the specified image and title
  *  
  *  @param  iva Two-dimensional virtual array.
  *  @param  view_code Code for which view component is to be used to
  *                    display the data.
  *  @param  include_ctrls If true, controls to manipulate image will be added.
  */
  protected Display( IVirtualArray iva, int view_code, boolean include_ctrls )
  {
    // make sure data is not null
    if( iva == null )
    {
      System.out.println("Error in Display - Virtual Array is null");
      System.exit(-1);
    }
    this_viewer = this;
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    current_view = view_code;
    add_controls = include_ctrls;
    data = iva;    
    buildMenubar();
    // if DisplayProps.isv exists, load it into the ObjectState automatically.
    // This code will load user settings.
    String props = System.getProperty("user.home") + 
    		    System.getProperty("file.separator") +
    		    "DisplayProps.isv";
    ObjectState temp = getObjectState(IPreserveState.DEFAULT);
    temp.silentFileChooser(props,false);
    setObjectState(temp);
  }
 
 /**
  * This method sets the ObjectState of this viewer to a previously saved
  * state.
  *
  *  @param  new_state The previously saved state that this viewer will be
  *                    set to.
  */ 
  abstract public void setObjectState( ObjectState new_state );
 
 /**
  * This method will get the current values of the state variables for this
  * object. These variables will be wrapped in an ObjectState.
  *
  *  @param  isDefault Should selective state be returned, that used to store
  *                    user preferences common from project to project?
  *  @return if true, the default state containing user preferences,
  *          if false, the entire state, suitable for project specific saves.
  */
  abstract public ObjectState getObjectState( boolean isDefault );
  
 /**
  * This method updates the view component when changes are made to the
  * existing array.
  */ 
  public void dataChanged()
  {
    ivc.dataChanged();
  }
  
 /**
  * Method to add a listener to this component.
  *
  *  @param  act_listener
  */
  public void addActionListener( ActionListener act_listener )
  {	     
    for ( int i = 0; i < Listeners.size(); i++ )    // don't add it if it's
      if ( Listeners.elementAt(i).equals( act_listener ) ) // already there
        return;

    Listeners.add( act_listener ); //Otherwise add act_listener
  }
  
 /**
  * Add the menu items from the ViewComponent.
  */   
  protected void addComponentMenuItems()
  {
    // get menu items from view component and place it in a menu
    ViewMenuItem[] menus = ivc.getMenuItems();
    if( menus == null || menus.length == 0 )
      return;
    for( int i = 0; i < menus.length; i++ )
    {
      if( ViewMenuItem.PUT_IN_FILE.equalsIgnoreCase(
          menus[i].getPath()) )
      {
        menu_bar.getMenu(0).add( menus[i].getItem() ); 
      }
      else if( ViewMenuItem.PUT_IN_OPTIONS.equalsIgnoreCase(
               menus[i].getPath()) )
      {
        menu_bar.getMenu(1).add( menus[i].getItem() );  	 
      }
      else if( ViewMenuItem.PUT_IN_HELP.equalsIgnoreCase(
               menus[i].getPath()) )
      {
        menu_bar.getMenu(2).add( menus[i].getItem() );
      }
    }
  }
 
 /*
  * This private method will (re)build the menubar. This is necessary since
  * the ImageViewComponent could add menu items to the Menubar.
  * If the file being loaded is not found, those menu items
  * must be removed. To do so, rebuild the Menubar.
  */ 
  private void buildMenubar()
  { 
    Vector file              = new Vector();
    Vector options           = new Vector();
    Vector save_results_menu = new Vector();
    Vector view_man  	     = new Vector();
    Vector save_menu 	     = new Vector();
    Vector load_menu 	     = new Vector();
    Vector save_default      = new Vector();
    Vector load_data         = new Vector();
    Vector print             = new Vector();
    Vector save_image        = new Vector();
    Vector exit              = new Vector();
    Vector file_listeners    = new Vector();
    Vector option_listeners  = new Vector();
    
    // build file menu
    file.add("File");
    file_listeners.add( new MenuListener() ); // listener for file
    file.add(load_menu);
      load_menu.add("Open Project");
      file_listeners.add( new MenuListener() ); // listener for load project
    file.add(save_menu);
      save_menu.add("Save Project");
      file_listeners.add( new MenuListener() ); // listener for save project
    file.add(print);
      print.add("Print Image");
      file_listeners.add( new MenuListener() ); // listener for printing IVC
    file.add(save_image);
      save_image.add("Make JPEG Image");
      file_listeners.add( new MenuListener() ); // listener saving IVC as jpg
    file.add(exit);
      exit.add("Exit");
      file_listeners.add( new MenuListener() ); // listener for exiting SWViewer
    
    // build options menu
    options.add("Options");
    option_listeners.add( new MenuListener() ); // listener for options
    options.add(save_default);
      save_default.add("Save User Settings");
      option_listeners.add( new MenuListener() ); // listener for saving results
           
    // add menus to the menu bar.
    setJMenuBar(null);
    menu_bar = new JMenuBar();
    menu_bar.add( MenuItemMaker.makeMenuItem(file,file_listeners) ); 
    menu_bar.add( MenuItemMaker.makeMenuItem(options,option_listeners) );
    
    setJMenuBar(menu_bar);
    /*  old implementation by attempting to us setMnemonics()
    // Add keyboard shortcuts
    JMenu file_menu = menu_bar.getMenu(0);
    file_menu.getItem(0).setMnemonic(KeyEvent.VK_O);   // Open Project
    file_menu.getItem(1).setMnemonic(KeyEvent.VK_S);   // Save Project
    file_menu.getItem(2).setMnemonic(KeyEvent.VK_P);   // Print Image
    file_menu.getItem(3).setMnemonic(KeyEvent.VK_J);   // Make JPEG Image
    file_menu.getItem(4).setMnemonic(KeyEvent.VK_X);   // Exit
    JMenu option_menu = menu_bar.getMenu(1);
    option_menu.getItem(0).setMnemonic(KeyEvent.VK_U); // Save User Settings
    JMenu help_menu = menu_bar.getMenu(2);
    help_menu.getItem(0).setMnemonic(KeyEvent.VK_H);   // Help Menu
    */
    // Add keyboard shortcuts
    KeyStroke binding = 
                  KeyStroke.getKeyStroke(KeyEvent.VK_O,InputEvent.ALT_MASK);
    JMenu file_menu = menu_bar.getMenu(0);
    file_menu.getItem(0).setAccelerator(binding);   // Open Project
    binding = KeyStroke.getKeyStroke(KeyEvent.VK_S,InputEvent.ALT_MASK);
    file_menu.getItem(1).setAccelerator(binding);   // Save Project
    binding = KeyStroke.getKeyStroke(KeyEvent.VK_P,InputEvent.ALT_MASK);
    file_menu.getItem(2).setAccelerator(binding);   // Print Image
    binding = KeyStroke.getKeyStroke(KeyEvent.VK_J,InputEvent.ALT_MASK);
    file_menu.getItem(3).setAccelerator(binding);   // Make JPEG Image
    binding = KeyStroke.getKeyStroke(KeyEvent.VK_X,InputEvent.ALT_MASK);
    file_menu.getItem(4).setAccelerator(binding);   // Exit
    JMenu option_menu = menu_bar.getMenu(1);
    binding = KeyStroke.getKeyStroke(KeyEvent.VK_U,InputEvent.ALT_MASK);
    option_menu.getItem(0).setAccelerator(binding); // Save User Settings
  }
  
 /**
  * Put the controls for view component in a Box and return them as one entity.
  *
  *  @return Box containing all of the controls.
  */ 
  protected Box buildControlPanel()
  {
    // add viewcomponent controls
    Box ivc_controls = new Box(BoxLayout.Y_AXIS);
    TitledBorder ivc_border = 
    		     new TitledBorder(LineBorder.createBlackLineBorder(),
        			      "View Controls");
    ivc_border.setTitleFont( FontUtil.BORDER_FONT ); 
    ivc_controls.setBorder( ivc_border );
    ViewControl[] ivc_ctrl = ivc.getControls();
    // if no controls, return null.
    if( ivc_ctrl.length == 0 )
      return null;
    for( int i = 0; i < ivc_ctrl.length; i++ )
    {
      ivc_controls.add(ivc_ctrl[i]);
    }
    // if resized, adjust container size for the pan view control.
    // THIS WAS COMMENTED OUT BECAUSE IT SLOWED THE VIEWER DOWN.
    //ivc_controls.addComponentListener( new ResizedControlListener() );
    
    // add spacer between ivc controls
    JPanel spacer = new JPanel();
    spacer.setPreferredSize( new Dimension(0, 10000) );
    ivc_controls.add(spacer);
    return ivc_controls;
  }
  
 /*
  * This class is required to handle all messages within the Display.
  */
  private class MenuListener implements ActionListener
  {
    public void actionPerformed( ActionEvent ae )
    {
      if( ae.getActionCommand().equals("Save User Settings") )
      {
        String props = System.getProperty("user.home") + 
	                System.getProperty("file.separator") +
			"DisplayProps.isv";
	getObjectState(IPreserveState.DEFAULT).silentFileChooser(props,true);
      }
      else if( ae.getActionCommand().equals("Save Project") )
      {
	getObjectState(IPreserveState.PROJECT).openFileChooser(true);
      }
      else if( ae.getActionCommand().equals("Open Project") )
      {
        ObjectState state = new ObjectState();
	if( state.openFileChooser(false) )
	  setObjectState(state);
      }
      else if( ae.getActionCommand().equals("Print Image") )
      {
        // Since pane could be one of two things, determine which one
	// it is, then determine the image accordingly.
	Component image;
	if( pane instanceof SplitPaneWithState )
	  image = ((SplitPaneWithState)pane).getLeftComponent();
        else
	  image = pane;
	JMenuItem silent_menu = PrintComponentActionListener.getActiveMenuItem(
	                                "not visible", image );
	silent_menu.doClick();
      }
      else if( ae.getActionCommand().equals("Make JPEG Image") )
      {
        // Since pane could be one of two things, determine which one
	// it is, then determine the image accordingly.
	Component image;
	if( pane instanceof SplitPaneWithState )
	  image = ((SplitPaneWithState)pane).getLeftComponent();
        else
	  image = pane;
        JMenuItem silent_menu = SaveImageActionListener.getActiveMenuItem(
	                                "not visible", image );
	silent_menu.doClick();
      }
      else if( ae.getActionCommand().equals("Exit") )
      {
	this_viewer.dispose();
	System.gc();
	System.exit(0);
      }
    }
  }
  
 /**
  * Tells all listeners about a new action.
  *
  *  @param  message
  */  
  protected void sendMessage( String message )
  {
    for ( int i = 0; i < Listeners.size(); i++ )
    {
      ActionListener listener = (ActionListener)Listeners.elementAt(i);
      listener.actionPerformed( new ActionEvent( this, 0, message ) );
    }
  }
}
