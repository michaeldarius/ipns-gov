/*
 * File:  IParameterGUI.java
 *
 * Copyright (C) 2002, Peter F. Peterson, 2006, Dennis Mikkelson
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
 * Contact :  Dennis Mikkelson<mikkelsond@uwstout.edu>
 *            MSCS Department
 *            HH237H
 *            Menomonie, WI. 54751
 *            (715)-232-2291
 *
 * This work was supported by the National Science Foundation under grant
 * number DMR-0426797, and by the Intense Pulsed Neutron Source Division
 * of Argonne National Laboratory, Argonne, IL 60439-4845, USA.

 * For further information, see <http://www.pns.anl.gov/ISAW/>
 *
 * Modified:
 *
 *  $Log$
 *  Revision 1.1  2006/07/10 16:25:05  dennis
 *  Change to new Parameter GUIs in gov.anl.ipns.Parameters
 *
 *  Revision 1.2  2006/06/25 01:30:25  dennis
 *  Removed updateValueFromGUI() method, since the getValue() method
 *  also will update the stored value from the value in the GUI,
 *  if the GUI is present.
 *
 *  Revision 1.1  2006/06/12 21:52:29  dennis
 *  Initial version of new code for parameter GUIs.  While this is
 *  loosely based on the parameter GUIs developed several years ago
 *  by Peter Peterson and Chris Bouzek, the current system is much
 *  simplified, and should be easier to maintain and extend.
 *
 *
 */
package gov.anl.ipns.Parameters;

import javax.swing.JPanel;
import gov.anl.ipns.Util.Messaging.*;

/**
 * This is an interface to be implemented by all parameters that include
 * a specific GUI component.  The GUI component may, or may not, actually
 * exist.  It is assumed that the GUI component will only be constructed
 * when needed, and will be destroyed when it is no longer needed.
 */

public interface IParameterGUI extends IParameter, IObservable 
{
  /** 
   *  VALUE_CHANGING = "Value Changing" is the message sent to IObservers
   *  when the GUI widget's value is being changed.  This may possibly 
   *  not be used by all PG's, if it is not meaningfully defined for a
   *  specific PG.  However, at least one of VALUE_CHANGED or VALUE_CHANGING
   *  messages MUST be sent by all PGs.
   */
  public static final String VALUE_CHANGING = "Value Changing";

  /** 
   *  VALUE_CHANGED = "Value Changed" is the message sent to IObservers
   *  when the GUI widget's value has been changed.  This may possibly 
   *  not be used by all PG's, if it is not meaningfully defined for a
   *  specific PG.  However, at least one of VALUE_CHANGED or VALUE_CHANGING
   *  messages MUST be sent by all PGs.
   */
  public static final String VALUE_CHANGED  = "Value Changed";


  /**
   * Get a reference to a JPanel containing the GUI component for this
   * ParameterGUI.  If the panel and component don't already exist, they
   * will be constructed.  If they already exist, the existing panel will
   * be returned.
   * The value of the parameter "include_valid_box", will be ignored on 
   * subsequent calls to this method.  If the GUI Panel is to be reconfigured 
   * in a different state, destroyGUIPanel() must be called first, so that an 
   * entirely new Panel will be created on the next call to getGUIPanel().
   * 
   * @param include_valid_box  boolean determining if a "valid check box"
   *                           component is to be included in the GUI Panel.
   *                           This parameter is only used the first time
   *                           a GUI Panel is created after constructing
   *                           the ParameterGUI, and the first time after a
   *                           call to destroyGUIPanel().
   * 
   * @return The GUI panel in which the entry widget is drawn.
   */
  JPanel getGUIPanel( boolean include_valid_box );


  /**
   * Destroys the GUI Panel, Component, and Valid CheckBox(if present).  
   * Calling this method will set the references to all GUI components to 
   * null so that they can be garbage collected.  After calling
   * destroyGUIPanel(), a new GUI Panel will be constructed when getGUIPanel()
   * is called.
   */
  void destroyGUIPanel();


  /**  #############  Temporary BandAid
   * Used to let a parameter validate itself.  This is helpful for any instance
   * where the "valid" checkbox is drawn, as it removes the need to
   * individually validate ParameterGUIs.
   */
  public void validateSelf();


  /**
   * Set the valid flag and valid checkbox, if present.
   *
   * @param  valid   True if this IParameterGUI should be considered valid.
   */
  void setValidFlag( boolean valid );


  /**
   * Get the state of the valid flag.
   *
   * @return  boolean value indicating whether or not the valid flag was
   *          set true for this ParameterGUI object.
   */
  boolean getValidFlag();

 
  /**
   *  Check whether the value stored in the parameter matches the value
   *  from the GUI entry widget, WITHOUT changing either value.  Classes
   *  implementing this method must be careful to implement it in a way
   *  that does not alter the value stored in the parameter, or the value
   *  stored in the GUI entry widget, if it exists.  If the entry widget
   *  does not exist, this method will just return false.
   *   
   *  This method will also set the valid flag to false, if it returns
   *  true.
   *
   *  @return true if there is an entry widget with a meaningful value
   *          that differs from the value stored in the parameter.  This
   *          will return false if there is no entry widget, or if the
   *          value in the entry widget is not a proper value.
   */
  boolean hasChanged();


  /**
   *  Get a string form of the value stored in this Parameter.  This
   *  method calls the getValue() method to get the current value from
   *  the GUI entry widget (if present) and synchronize the Parameter's
   *  value with the GUI entry widget's value.  The resulting value is
   *  returned, or an exception is thrown.
   *
   *  @return  A string form of the value of this Parameter.
   *
   *  @throws IllegalArgumentException if the GUI entry widget exists
   *          but has an invalid value.
   */
  String getStringValue() throws IllegalArgumentException;


  /**
   * Clear out any extra memory used by the IParameterGUI, cleaning up data 
   * structures and releasing Objects that are no longer needed.
   */
  void clear();


  /**
   * Enable or disable the GUI widget for entering values. 
   *
   * @param  on_off  Set true to enable the widget for user input.
   */
  void setEnabled( boolean on_off );


}
