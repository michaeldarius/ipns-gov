/*
 * File: GreekSimplex.java
 *
 * Copyright (C) 2004, Dennis Mikkelson
 *
 * DO NOT EDIT.  THIS FILE WAS AUTOMATICALLY GENERATED BY
 * THE FileStrokeFont class.
 *
 * This program is free software; you can redistribute it
 * and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software
 * Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330
 * Boston, MA  02111-1307, USA.
 *
 * Contact: 
 * Dennis Mikkelson <mikkelsond@uwstout.edu>
 * Department of Mathematics, Statistics and Computer Science
 * University of Wisconsin-Stout
 * Menomonie, WI 54751, USA
 *
 * This work was supported by the National Science Foundation
 * under grant number DMR-0218882.
 *
 * For further information see <http://www.pns.anl.gov/ISAW/>
 *
 * Modified:
 *
 * $Log$
 * Revision 1.2  2004/06/18 19:46:23  dennis
 * Removed import of GL_ThreeD package, no longer needed since
 * StrokeFont moved to Fonts package.
 *
 * Revision 1.1  2004/06/02 14:11:25  dennis
 * Hershey Font file for use with OpenGL, generated from plain text form
 * of font data, using the FileStrokeFont class.
 *
 */

package gov.anl.ipns.ViewTools.Panels.GL_ThreeD.Fonts;

/**
 * This class stores the data for the font: GreekSimplex
 * that was derived from the public domain Hershey font data,
 * distributed on the USENET network.  The Hershey Fonts were
 * originally digitized by Dr. Allen V. Hershey, a
 * mathematical physicist, while working at the U.S. Naval
 * Weapons Laboratory in Dahlgren, Virginia during the 1960's
 * The USENET distribution was originally created by:
 *
 * James Hurt
 * Cognition, Inc.
 * 900 Technology Park Drive
 * Billerica, MA 01821
 *
 * The data in this class was produced from the *.txf file
 * form of the data produced by Dennis Mikkelson from the 
 * data distributed on USENET.
 */

public class GreekSimplex extends StrokeFont
{
 private static short my_char_start[] = {
 0, 1, 10, 16, 28, 55, 87, 122, 130, 141, 152, 161, 167, 175,
 178, 184, 187, 205, 210, 225, 241, 248, 266, 290, 296, 326, 350,
 362, 376, 380, 386, 390, 411, 467, 476, 500, 506, 515, 527, 548,
 554, 563, 566, 593, 602, 608, 620, 629, 651, 660, 685, 699, 709,
 715, 734, 750, 767, 776, 794, 803, 815, 818, 830, 838, 841, 849,
 873, 904, 918, 942, 961, 982, 999, 1018, 1027, 1048, 1067, 1076, 1097,
 1111, 1129, 1142, 1165, 1184, 1202, 1210, 1226, 1242, 1265, 1294, 1314, 1337,
 1377, 1380, 1420 };

 private static short my_char_width[] = {
 16, 10, 16, 21, 20, 24, 26, 10, 14, 14, 16, 26, 10, 26, 10, 22, 20, 20, 20,
 20, 20, 20, 20, 20, 20, 20, 10, 10, 24, 26, 24, 18, 27, 18, 21, 20, 18,
 19, 20, 17, 22, 8, 21, 21, 18, 24, 22, 22, 22, 22, 21, 18, 16, 18, 18,
 20, 18, 22, 20, 14, 14, 14, 22, 18, 10, 21, 19, 18, 18, 16, 20, 19, 20,
 11, 22, 18, 16, 21, 18, 17, 22, 17, 18, 20, 20, 20, 16, 23, 16, 23, 15,
 14, 8, 14, 24 };

 private static short my_font_x[] = {
 0, 65, 65, 0, 65, 64, 65, 66, 65, 0, 64, 64, 0, 72, 72, 0, 71, 64, 0,
 77, 70, 0, 64, 78, 0, 63, 77, 0, 68, 68, 0, 72, 72, 0, 77, 75, 72,
 68, 65, 63, 63, 64, 65, 67, 73, 75, 76, 77, 77, 75, 72, 68, 65, 63, 0,
 81, 63, 0, 68, 70, 70, 69, 67, 65, 63, 63, 64, 66, 68, 70, 73, 76, 79,
 81, 0, 77, 75, 74, 74, 76, 78, 80, 81, 81, 79, 77, 0, 83, 83, 82, 81,
 80, 79, 77, 75, 73, 71, 67, 65, 64, 63, 63, 64, 65, 72, 73, 74, 74, 73,
 71, 69, 68, 68, 69, 71, 76, 78, 80, 82, 83, 83, 0, 65, 64, 65, 66, 66,
 65, 64, 0, 71, 69, 67, 65, 64, 64, 65, 67, 69, 71, 0, 63, 65, 67, 69,
 70, 70, 69, 67, 65, 63, 0, 68, 68, 0, 63, 73, 0, 73, 63, 0, 73, 73,
 0, 64, 82, 0, 65, 64, 65, 66, 66, 65, 64, 0, 64, 82, 0, 65, 64, 65,
 66, 65, 0, 80, 62, 0, 69, 66, 64, 63, 63, 64, 66, 69, 71, 74, 76, 77,
 77, 76, 74, 71, 69, 0, 66, 68, 71, 71, 0, 64, 64, 65, 66, 68, 72, 74,
 75, 76, 76, 75, 73, 63, 77, 0, 65, 76, 70, 73, 75, 76, 77, 77, 76, 74,
 71, 68, 65, 64, 63, 0, 73, 63, 78, 0, 73, 73, 0, 75, 65, 64, 65, 68,
 71, 74, 76, 77, 77, 76, 74, 71, 68, 65, 64, 63, 0, 76, 75, 72, 70, 67,
 65, 64, 64, 65, 67, 70, 71, 74, 76, 77, 77, 76, 74, 71, 70, 67, 65, 64,
 0, 77, 67, 0, 63, 77, 0, 68, 65, 64, 64, 65, 67, 71, 74, 76, 77, 77,
 76, 75, 72, 68, 65, 64, 63, 63, 64, 66, 69, 73, 75, 76, 76, 75, 72, 68,
 0, 76, 75, 73, 70, 69, 66, 64, 63, 63, 64, 66, 69, 70, 73, 75, 76, 76,
 75, 73, 70, 68, 65, 64, 0, 65, 64, 65, 66, 65, 0, 65, 64, 65, 66, 65,
 0, 65, 64, 65, 66, 65, 0, 65, 64, 65, 66, 66, 65, 64, 0, 80, 64, 80,
 0, 64, 82, 0, 64, 82, 0, 64, 80, 64, 0, 63, 63, 64, 65, 67, 71, 73,
 74, 75, 75, 74, 73, 69, 69, 0, 69, 68, 69, 70, 69, 0, 78, 77, 75, 72,
 70, 69, 68, 68, 69, 71, 74, 76, 77, 0, 72, 70, 69, 69, 70, 71, 0, 78,
 77, 77, 79, 81, 83, 84, 84, 83, 82, 80, 78, 75, 72, 69, 67, 65, 64, 63,
 63, 64, 65, 67, 69, 72, 75, 78, 80, 81, 0, 79, 78, 78, 79, 0, 69, 61,
 0, 69, 77, 0, 64, 74, 0, 64, 64, 0, 64, 73, 76, 77, 78, 78, 77, 76,
 73, 0, 64, 73, 76, 77, 78, 78, 77, 76, 73, 64, 0, 63, 77, 0, 63, 77,
 0, 69, 61, 0, 69, 77, 0, 61, 77, 0, 64, 64, 0, 64, 77, 0, 64, 72,
 0, 64, 77, 0, 70, 70, 0, 68, 65, 64, 63, 63, 64, 65, 68, 72, 75, 76,
 77, 77, 76, 75, 72, 68, 0, 64, 64, 0, 64, 76, 0, 64, 64, 0, 78, 78,
 0, 64, 78, 0, 64, 64, 0, 61, 62, 64, 66, 67, 67, 66, 66, 67, 68, 70,
 72, 74, 75, 76, 77, 77, 76, 74, 72, 71, 71, 72, 74, 76, 79, 0, 64, 64,
 0, 78, 64, 0, 69, 78, 0, 69, 61, 0, 69, 77, 0, 64, 64, 0, 64, 72,
 0, 80, 72, 0, 80, 80, 0, 64, 64, 0, 64, 78, 0, 78, 78, 0, 69, 67,
 65, 64, 63, 63, 64, 65, 67, 69, 73, 75, 77, 78, 79, 79, 78, 77, 75, 73,
 69, 0, 64, 64, 0, 78, 78, 0, 64, 78, 0, 69, 67, 65, 64, 63, 63, 64,
 65, 67, 69, 73, 75, 77, 78, 79, 79, 78, 77, 75, 73, 69, 0, 68, 74, 0,
 64, 64, 0, 64, 73, 76, 77, 78, 78, 77, 76, 73, 64, 0, 62, 69, 62, 0,
 62, 76, 0, 62, 76, 0, 68, 68, 0, 61, 75, 0, 62, 62, 63, 64, 66, 67,
 68, 69, 69, 0, 76, 76, 75, 74, 72, 71, 70, 69, 0, 75, 74, 71, 68, 65,
 64, 63, 63, 64, 66, 70, 71, 71, 70, 68, 0, 63, 67, 64, 63, 63, 64, 66,
 69, 71, 74, 76, 77, 77, 76, 73, 77, 0, 62, 76, 0, 66, 72, 0, 62, 76,
 0, 71, 71, 0, 62, 63, 64, 65, 66, 67, 70, 72, 75, 76, 77, 78, 79, 80,
 0, 77, 63, 0, 63, 77, 0, 63, 77, 0, 64, 64, 0, 65, 65, 0, 64, 71,
 0, 64, 71, 0, 60, 74, 0, 69, 69, 0, 70, 70, 0, 63, 70, 0, 63, 70,
 0, 63, 71, 79, 0, 63, 71, 79, 0, 60, 78, 0, 65, 66, 65, 64, 64, 65,
 66, 0, 69, 67, 65, 64, 63, 63, 64, 66, 68, 70, 73, 75, 77, 78, 0, 69,
 71, 72, 73, 75, 76, 77, 78, 0, 72, 70, 68, 66, 65, 64, 63, 62, 0, 72,
 74, 76, 76, 75, 74, 72, 69, 0, 69, 71, 73, 74, 74, 73, 72, 70, 68, 66,
 65, 64, 0, 62, 64, 66, 72, 74, 76, 0, 77, 76, 74, 64, 62, 61, 0, 71,
 68, 66, 64, 63, 63, 64, 65, 67, 69, 71, 73, 74, 74, 73, 71, 69, 68, 68,
 69, 71, 73, 75, 0, 73, 72, 70, 67, 65, 65, 66, 69, 0, 69, 65, 63, 63,
 64, 66, 69, 71, 73, 0, 74, 66, 0, 69, 66, 64, 63, 63, 64, 66, 69, 71,
 74, 76, 77, 77, 76, 74, 71, 69, 0, 61, 63, 65, 66, 68, 69, 70, 70, 69,
 0, 77, 76, 75, 69, 67, 66, 0, 61, 62, 64, 66, 67, 67, 66, 64, 0, 66,
 68, 70, 72, 74, 76, 76, 75, 72, 0, 66, 64, 63, 63, 64, 66, 68, 69, 0,
 68, 66, 64, 63, 63, 64, 65, 67, 70, 73, 76, 78, 79, 79, 77, 75, 73, 71,
 69, 66, 0, 66, 62, 0, 76, 75, 74, 72, 68, 66, 65, 0, 65, 67, 68, 70,
 71, 72, 73, 0, 61, 63, 65, 66, 74, 0, 68, 62, 0, 67, 61, 0, 66, 65,
 65, 67, 69, 71, 73, 75, 0, 77, 75, 74, 74, 75, 77, 79, 80, 0, 63, 66,
 65, 64, 63, 0, 76, 75, 74, 72, 69, 66, 63, 0, 68, 66, 64, 63, 63, 64,
 65, 67, 69, 71, 73, 74, 74, 73, 72, 70, 68, 0, 69, 65, 0, 74, 75, 76,
 77, 0, 62, 64, 67, 80, 0, 70, 68, 66, 65, 64, 63, 63, 64, 65, 67, 69,
 71, 72, 73, 74, 74, 73, 72, 70, 0, 64, 73, 0, 64, 64, 65, 66, 68, 70,
 72, 74, 75, 75, 74, 73, 71, 69, 67, 65, 64, 60, 0, 78, 68, 66, 64, 63,
 63, 64, 65, 67, 69, 71, 73, 74, 74, 73, 72, 70, 0, 71, 68, 0, 62, 64,
 67, 78, 0, 61, 62, 64, 66, 67, 67, 65, 65, 67, 69, 72, 74, 76, 77, 77,
 0, 73, 71, 68, 66, 64, 63, 63, 64, 65, 67, 70, 72, 0, 63, 71, 0, 68,
 66, 64, 63, 63, 64, 65, 67, 69, 71, 0, 72, 71, 72, 73, 75, 77, 79, 80,
 80, 79, 78, 0, 70, 68, 67, 67, 68, 71, 74, 0, 71, 68, 66, 65, 65, 67,
 70, 72, 0, 70, 66, 64, 63, 63, 65, 69, 70, 70, 68, 66, 0, 76, 68, 0,
 61, 62, 64, 66, 67, 67, 66, 66, 67, 69, 71, 74, 76, 78, 80, 81, 0, 70,
 68, 67, 67, 68, 71, 74, 0, 74, 70, 67, 64, 63, 63, 64, 66, 69, 70, 70,
 69, 67, 66, 0, 69, 67, 66, 65, 65, 66, 67, 68, 68, 66, 0, 67, 66, 66,
 67, 68, 69, 69, 68, 64, 68, 69, 69, 68, 67, 66, 66, 67, 0, 66, 68, 68,
 67, 66, 65, 65, 66, 67, 69, 0, 64, 64, 0, 65, 67, 68, 69, 69, 68, 67,
 66, 66, 68, 0, 67, 68, 68, 67, 66, 65, 65, 66, 70, 66, 65, 65, 66, 67,
 68, 68, 67, 0, 68, 66, 66, 67, 68, 69, 69, 68, 67, 65, 0, 63, 63, 64,
 66, 68, 70, 74, 76, 78, 80, 81, 0, 63, 64, 66, 68, 70, 74, 76, 78, 80,
 81, 81, 0 };

 private static short my_font_y[] = {
 2, 72, 58, 1, 53, 52, 51, 52, 53, 2, 72, 65, 1, 72, 65, 2, 76, 44, 1,
 76, 44, 1, 63, 63, 1, 57, 57, 2, 76, 47, 1, 76, 47, 1, 69, 71, 72,
 72, 71, 69, 67, 65, 64, 63, 61, 60, 59, 57, 54, 52, 51, 51, 52, 54, 2,
 72, 51, 1, 72, 70, 68, 66, 65, 65, 67, 69, 71, 72, 72, 71, 70, 70, 71,
 72, 1, 58, 57, 55, 53, 51, 51, 52, 54, 56, 58, 58, 2, 63, 64, 65, 65,
 64, 62, 57, 54, 52, 51, 51, 52, 53, 55, 57, 59, 60, 64, 65, 67, 69, 71,
 72, 71, 69, 67, 64, 61, 54, 52, 51, 51, 52, 53, 2, 70, 71, 72, 71, 69,
 67, 66, 2, 76, 74, 71, 67, 62, 58, 53, 49, 46, 44, 2, 76, 74, 71, 67,
 62, 58, 53, 49, 46, 44, 2, 66, 54, 1, 63, 57, 1, 63, 57, 2, 69, 51,
 1, 60, 60, 2, 51, 52, 53, 52, 50, 48, 47, 2, 60, 60, 2, 53, 52, 51,
 52, 53, 2, 76, 44, 2, 72, 71, 68, 63, 60, 55, 52, 51, 51, 52, 55, 60,
 63, 68, 71, 72, 72, 2, 68, 69, 72, 51, 2, 67, 68, 70, 71, 72, 72, 71,
 70, 68, 66, 64, 61, 51, 51, 2, 72, 72, 64, 64, 63, 62, 59, 57, 54, 52,
 51, 51, 52, 53, 55, 2, 72, 58, 58, 1, 72, 51, 2, 72, 72, 63, 64, 65,
 65, 64, 62, 59, 57, 54, 52, 51, 51, 52, 53, 55, 2, 69, 71, 72, 72, 71,
 68, 63, 58, 54, 52, 51, 51, 52, 54, 57, 58, 61, 63, 64, 64, 63, 61, 58,
 2, 72, 51, 1, 72, 72, 2, 72, 71, 69, 67, 65, 64, 63, 62, 60, 58, 55,
 53, 52, 51, 51, 52, 53, 55, 58, 60, 62, 63, 64, 65, 67, 69, 71, 72, 72,
 2, 65, 62, 60, 59, 59, 60, 62, 65, 66, 69, 71, 72, 72, 71, 69, 65, 60,
 55, 52, 51, 51, 52, 54, 2, 65, 64, 63, 64, 65, 1, 53, 52, 51, 52, 53,
 2, 65, 64, 63, 64, 65, 1, 51, 52, 53, 52, 50, 48, 47, 2, 69, 60, 51,
 2, 63, 63, 1, 57, 57, 2, 69, 60, 51, 2, 67, 68, 70, 71, 72, 72, 71,
 70, 68, 66, 64, 63, 61, 58, 1, 53, 52, 51, 52, 53, 2, 64, 66, 67, 67,
 66, 65, 62, 59, 57, 56, 56, 57, 59, 1, 67, 65, 62, 59, 57, 56, 1, 67,
 59, 57, 56, 56, 58, 61, 63, 66, 68, 70, 71, 72, 72, 71, 70, 68, 66, 63,
 60, 57, 55, 53, 52, 51, 51, 52, 53, 54, 1, 67, 59, 57, 56, 2, 72, 51,
 1, 72, 51, 1, 58, 58, 2, 72, 51, 1, 72, 72, 71, 70, 68, 66, 64, 63,
 62, 1, 62, 62, 61, 60, 58, 55, 53, 52, 51, 51, 2, 72, 51, 1, 51, 72,
 2, 72, 51, 1, 72, 51, 1, 51, 51, 2, 72, 51, 1, 72, 72, 1, 62, 62,
 1, 51, 51, 2, 72, 51, 1, 67, 66, 65, 63, 60, 58, 57, 56, 56, 57, 58,
 60, 63, 65, 66, 67, 67, 2, 72, 51, 1, 72, 72, 2, 72, 51, 1, 72, 51,
 1, 62, 62, 2, 72, 51, 2, 61, 63, 65, 65, 64, 62, 57, 54, 52, 51, 51,
 52, 55, 57, 60, 65, 68, 71, 72, 72, 70, 68, 65, 62, 60, 58, 2, 72, 51,
 1, 72, 58, 1, 63, 51, 2, 72, 51, 1, 72, 51, 2, 72, 51, 1, 72, 51,
 1, 72, 51, 1, 72, 51, 2, 72, 51, 1, 72, 51, 1, 72, 51, 2, 72, 71,
 69, 67, 64, 59, 56, 54, 52, 51, 51, 52, 54, 56, 59, 64, 67, 69, 71, 72,
 72, 2, 72, 51, 1, 72, 51, 1, 72, 72, 2, 72, 71, 69, 67, 64, 59, 56,
 54, 52, 51, 51, 52, 54, 56, 59, 64, 67, 69, 71, 72, 72, 1, 62, 62, 2,
 72, 51, 1, 72, 72, 71, 70, 68, 65, 63, 62, 61, 61, 2, 72, 62, 51, 1,
 72, 72, 1, 51, 51, 2, 72, 51, 1, 72, 72, 2, 67, 69, 71, 72, 72, 71,
 69, 65, 51, 1, 67, 69, 71, 72, 72, 71, 69, 65, 2, 63, 64, 65, 65, 64,
 63, 61, 59, 57, 55, 52, 50, 48, 47, 47, 2, 51, 51, 58, 62, 66, 69, 71,
 72, 72, 71, 69, 66, 62, 58, 51, 51, 2, 72, 72, 1, 62, 62, 1, 51, 51,
 2, 72, 51, 1, 66, 66, 65, 61, 59, 58, 57, 57, 58, 59, 61, 65, 66, 66,
 2, 72, 51, 1, 72, 72, 1, 51, 51, 2, 76, 44, 1, 76, 44, 1, 76, 76,
 1, 44, 44, 2, 72, 48, 2, 76, 44, 1, 76, 44, 1, 76, 76, 1, 44, 44,
 2, 67, 72, 67, 1, 67, 71, 67, 2, 44, 44, 2, 70, 71, 72, 71, 69, 67,
 66, 2, 65, 64, 62, 60, 57, 54, 52, 51, 51, 52, 55, 58, 62, 65, 1, 65,
 65, 64, 62, 54, 52, 51, 51, 2, 72, 71, 69, 65, 62, 58, 52, 44, 1, 72,
 72, 70, 67, 65, 64, 63, 63, 1, 63, 62, 60, 58, 55, 53, 52, 51, 51, 52,
 53, 56, 2, 65, 65, 63, 46, 44, 44, 1, 65, 63, 60, 49, 46, 44, 2, 65,
 65, 64, 62, 59, 56, 53, 52, 51, 51, 52, 54, 57, 60, 63, 65, 67, 69, 71,
 72, 72, 71, 69, 2, 63, 64, 65, 65, 64, 62, 60, 59, 1, 59, 58, 56, 54,
 52, 51, 51, 52, 54, 2, 72, 44, 1, 65, 64, 62, 59, 56, 54, 52, 51, 51,
 52, 54, 57, 60, 62, 64, 65, 65, 2, 62, 64, 65, 65, 64, 63, 60, 56, 51,
 1, 65, 62, 60, 51, 47, 44, 2, 61, 63, 65, 65, 64, 62, 58, 51, 1, 58,
 62, 64, 65, 65, 63, 60, 55, 44, 2, 65, 58, 54, 52, 51, 51, 53, 55, 2,
 64, 63, 61, 58, 55, 53, 52, 51, 51, 52, 54, 57, 60, 63, 65, 65, 63, 59,
 54, 44, 2, 65, 51, 1, 64, 65, 65, 64, 60, 59, 59, 1, 59, 58, 57, 52,
 51, 51, 52, 2, 72, 72, 71, 70, 51, 1, 65, 51, 2, 65, 44, 1, 61, 56,
 53, 51, 51, 52, 54, 58, 1, 65, 58, 54, 52, 51, 51, 53, 55, 2, 65, 65,
 59, 54, 51, 1, 65, 62, 60, 57, 54, 52, 51, 2, 65, 64, 62, 59, 56, 53,
 52, 51, 51, 52, 54, 57, 60, 63, 64, 65, 65, 2, 65, 51, 1, 65, 59, 54,
 51, 1, 62, 64, 65, 65, 2, 72, 71, 68, 66, 63, 58, 54, 52, 51, 51, 52,
 55, 57, 60, 65, 69, 71, 72, 72, 1, 62, 62, 2, 59, 56, 53, 52, 51, 51,
 52, 54, 57, 60, 63, 64, 65, 65, 64, 62, 59, 44, 2, 65, 65, 64, 62, 59,
 56, 53, 52, 51, 51, 52, 54, 57, 60, 63, 64, 65, 2, 65, 51, 1, 62, 64,
 65, 65, 2, 61, 63, 65, 65, 64, 62, 56, 53, 51, 51, 52, 54, 58, 62, 65,
 2, 64, 65, 65, 64, 62, 59, 56, 53, 52, 51, 51, 52, 1, 58, 58, 2, 65,
 64, 61, 58, 55, 52, 51, 51, 52, 55, 1, 59, 55, 52, 51, 51, 52, 55, 58,
 61, 64, 65, 2, 72, 71, 70, 69, 68, 67, 67, 1, 67, 66, 65, 63, 61, 59,
 58, 58, 1, 58, 57, 56, 54, 52, 50, 48, 47, 45, 44, 44, 2, 72, 44, 1,
 61, 63, 65, 65, 64, 62, 57, 54, 52, 51, 51, 52, 54, 57, 62, 65, 2, 72,
 71, 70, 69, 68, 67, 67, 1, 67, 65, 63, 60, 57, 55, 53, 51, 49, 47, 45,
 44, 44, 46, 2, 76, 75, 74, 72, 70, 68, 67, 65, 63, 61, 1, 75, 73, 71,
 69, 68, 66, 64, 62, 60, 58, 56, 54, 52, 51, 49, 47, 45, 1, 59, 57, 55,
 53, 52, 50, 48, 46, 45, 44, 2, 76, 44, 2, 76, 75, 74, 72, 70, 68, 67,
 65, 63, 61, 1, 75, 73, 71, 69, 68, 66, 64, 62, 60, 58, 56, 54, 52, 51,
 49, 47, 45, 1, 59, 57, 55, 53, 52, 50, 48, 46, 45, 44, 2, 57, 59, 62,
 63, 63, 62, 59, 58, 58, 59, 61, 1, 59, 61, 62, 62, 61, 58, 57, 57, 58,
 61, 63, 2 };

  /* --------------------- Constructor ------------------- */
  /**
   *  Construct a font object for the font GreekSimplex
   *  to be used with the StrokeText class.  To draw text in
   *  3D, pass an instance of this font along with the
   *  string to the constructor of the StrokeText class, and
   *  then add the StrokeText object to the ThreeD_GL_Panel
   *  where the string should be drawn.
   */
  public GreekSimplex()
  {
    num_chars       = 95;
    first_char_code = 32;
    left_edge       = 60;
    top    = 77;
    cap    = 72;
    half   = 60;
    base   = 51;
    bottom = 43;
    char_start = my_char_start;
    char_width = my_char_width;
    font_x     = my_font_x;
    font_y     = my_font_y;
  }
}
