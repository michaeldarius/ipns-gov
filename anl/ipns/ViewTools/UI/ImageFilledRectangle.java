/* 
 * File: ImageFilledRectangle.java
 *
 * Copyright (C) 2010, Ruth Mikkelson
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
 * Contact : Ruth Mikkelson <mikkelsonr@uwstout.edu>
 *           Department of Mathematics, Statistics and Computer Science
 *           University of Wisconsin-Stout
 *           Menomonie, WI 54751, USA
 *
 * This work was supported by the Spallation Neutron Source Division
 * of Oak Ridge National Laboratory, Oak Ridge, TN, USA.
 *
 *  Last Modified:
 * 
 *  $Author:$
 *  $Date:$            
 *  $Rev:$
 */
package gov.anl.ipns.ViewTools.UI;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.*;
import java.util.Arrays;
import javax.swing.*;

import gov.anl.ipns.MathTools.Geometry.Vector3D;
import gov.anl.ipns.Util.Numeric.floatPoint2D;
import gov.anl.ipns.Util.Sys.FinishJFrame;
import gov.anl.ipns.ViewTools.Components.*;
import gov.anl.ipns.ViewTools.Panels.Image.IndexColorMaker;

/**
 * This class creates an image that when drawn in 2D fills the 2D 
 * projection of a rectangle with the given 3D normal. x,y,z are in ipns
 * coordinate system where beam is x and up is z and up, beam and back form a
 * right-handed coordinate system
 * 
 * @author ruth
 *
 */
public class ImageFilledRectangle
{
   IVirtualArray2D image;
   IndexColorModel colModel;
   Dimension rect; 
   Vector3D yvec;
   Vector3D center;
   Vector3D xvec;
   Vector3D yvec_proj;
   Vector3D center_proj;
   Vector3D xvec_proj;
   floatPoint2D[] Edges = null;
   Vector3D normalIn;
   Vector3D normalOut = null;
   float Out2In_x;
   float Out2In_y;
   BufferedImage ResImage = null;
   /**
    * Constructor
    * @param image      2D array of color indexes. Row0,col 0 upper left corner
    * @param colModel   colors for the image data
    * @param rect       Dimensions of the rectangle( pixel coordinates)
    * @param center     Center 3D of Rectangle. In "Pixel" coordinates
    * @param xvec       direction from upper left corner of rectangle to upper right corner
    * @param yvec       direction from  lower left corner of rectangle to upper left corner
    * NOTE: upper left can be any corner. lower left can be any other corner. upper right
    *       must be the other corner adjacent to the upper left corner
    * 
    * NOTE: The image will start(row 1) at top left corner and will fill the whole
    *       rectangle row by row down to the lower left corner.  
    */
   public ImageFilledRectangle(IVirtualArray2D image, 
                               IndexColorModel colModel,
                               Dimension rect, 
                               Vector3D center,
                               Vector3D xvec, 
                               Vector3D yvec)
   {
      this.image= image;
      setRectangle(rect);
      this.colModel = BuildAlphaColModel(colModel);
      this.yvec= yvec;
      this.yvec.normalize( );
      this.center = center;
      this.xvec = xvec;
      this.xvec.normalize( );
      this.yvec_proj= new Vector3D(0,1,0);
      this.center_proj = new Vector3D(0,0,0);
      this.xvec_proj = new Vector3D(1,0,0);
      
      normalIn = new Vector3D(xvec);
      normalIn.cross( yvec );
      normalIn.normalize();

      normalOut = new Vector3D( xvec_proj);
      normalOut.cross( yvec_proj);
      normalOut.normalize();
      
     
      if( image == null)
      {
         
         image = new VirtualArray2D( new float[10][10]);
      }
      
   }
   
   public void setImage( IVirtualArray2D image, IndexColorModel colModel)
   {
      this.image = image;
      this.colModel = BuildAlphaColModel(colModel);
      ResImage = null;
      if( image == null)
      {
         
         this.image = new VirtualArray2D( new float[10][10]);
      }
      
   }
   
   // New colormodel where 0 represents completely transparent. The
   // other indecies map( with offset 1) to the other color model
   private IndexColorModel BuildAlphaColModel(IndexColorModel colModel)
   {
      int size = colModel.getMapSize( );
      byte[] red = new byte[size+1];
      byte[] green = new byte[size+1];
      byte[] blue = new byte[size+1];
      byte[] alpha = new byte[size+1];
      Arrays.fill( alpha , (byte)255 );
      red[0]= green[0]= blue[0]=alpha[0]= 0;
      byte[] buff = new byte[size];
      colModel.getReds( buff );
      System.arraycopy( buff , 0, red , 1 , size );
      colModel.getGreens( buff );
      System.arraycopy( buff , 0, green , 1 , size );
      colModel.getBlues( buff );
      System.arraycopy( buff , 0, blue , 1 , size );
      
      int nbits =(int)(.5+Math.log( size+1 )/Math.log( 2 ));
      if( nbits < 1)
         nbits = 3;
      
      return new IndexColorModel( nbits ,size+1,red,green,blue,alpha);
   }
   
   
   public void setRectangle( Dimension  rect)
   {
      this.rect = rect;
      ResImage = null;
      Edges = null;
   }
   
   /**
    * Sets indicated vectors if they are not null
    * @param xvec
    * @param yvec
    * @param center
    */
   public void setPlane( Vector3D xvec, Vector3D yvec,  Vector3D center)
   {
      if( yvec != null)
      {
         this.yvec= yvec;
         this.yvec.normalize( );
      }
      
      if( center != null)
      {
         this.center = center;
      }
        
      
      if( xvec != null)
      {
         this.xvec = xvec;
         this.xvec.normalize( );
      }
      
      Edges = null;
      
      normalIn = new Vector3D(xvec);
      normalIn.cross( yvec );
      normalIn.normalize();
      ResImage = null;
   }
   
   
   public void setProjPlane(Vector3D xvec, Vector3D yvec,  Vector3D center)
   {
      
      
         this.yvec_proj= yvec;
      
      
         this.center_proj = center;
     
         this.xvec_proj = xvec;
         
         this.yvec_proj.normalize( );
         this.xvec_proj.normalize( );
         
         Edges = null;
         normalOut = new Vector3D( xvec);
         normalOut.cross( yvec);
         normalOut.normalize();
         
      
   }
   
   // 
   // Returns the points are the 2D projection of the 4 corners of
   // the rectangle onto the projection plane( coordinates rel to xvec and
   // yvec).  The order of the points are (row,col)= (1,1), (nrows,1),(nrows,ncols),
   //   (1,ncols)
   private floatPoint2D[] get2DRectangleProjections()
   {
      if( Edges != null)
         return Edges;
      
      floatPoint2D[] Res = new floatPoint2D[4];

      //Vector3D ProjPlaneNormal = new Vector3D(xvec_proj);
      //ProjPlaneNormal.cross(  yvec_proj );
      //ProjPlaneNormal.normalize( );
      

      Vector3D halfWidth = new Vector3D(xvec);
      halfWidth.multiply( rect.width/2 );
      Vector3D halfHeight = new Vector3D(yvec);
      halfHeight.multiply( rect.height/2 );
      
      Vector3D[] Edge = new Vector3D[4];
      
      Edge[0] = new Vector3D( center);      
      Edge[0].subtract( halfWidth);
      Edge[0].subtract(halfHeight);

      Edge[1] = new Vector3D( center);      
      Edge[1].subtract( halfWidth);
      Edge[1].add(halfHeight);
      
      Edge[2] = new Vector3D( center);      
      Edge[2].add( halfWidth);
      Edge[2].add(halfHeight);

      Edge[3] = new Vector3D( center);      
      Edge[3].add( halfWidth);
      Edge[3].subtract(halfHeight);
      
      for( int i=0; i<4; i++)
      {
         Vector3D V = new Vector3D(Edge[i]);
         V.subtract( center_proj );
         float k = -V.dot(normalOut);
         Vector3D kProjPlaneNormal = new Vector3D(normalOut);
         kProjPlaneNormal.multiply( k );
         Edge[i].add( kProjPlaneNormal );
         Edge[i].subtract( center_proj);
         Res[i]= new floatPoint2D( Edge[i].dot( xvec_proj ), 
                        Edge[i].dot( yvec_proj ));
      }
     
      Edges = Res;
      return Res;
      
   }
   
   public Dimension getRectangle()
   {
      return rect;
   }
   
   public BufferedImage getImage()
   {
      if(ResImage != null)
         return ResImage;
      
      
      floatPoint2D[] Edges = get2DRectangleProjections();
      Point[] Span = getTopLeftImagePosition();
      
      int nImageRows = Span[1].y- Span[0].y;//pixels
      int nImageCols =Span[1].x- Span[0].x;//pixels
      
      if( nImageRows <2 || nImageCols < 2)
         return null;
      
      BufferedImage Res = new BufferedImage(nImageCols, nImageRows, 
                      BufferedImage.TYPE_BYTE_INDEXED, colModel );
      
      //Now Setraster to get Data in
      WritableRaster rast = colModel.createCompatibleWritableRaster( 
            nImageCols ,  nImageRows );
      
      int[] xs= new int[4];
      int[]ys= new int[4];
      for( int i=0; i<4;i++)
      {
         xs[i]= (int)Math.floor(.5+Edges[i].x);
         ys[i] =(int)Math.floor(.5+ Edges[i].y);
      }
     
      
      Polygon Pol = new Polygon( xs,ys,4) ;// for contains
      Point[] TopLeft = getTopLeftImagePosition();
      for(int r=0; r < nImageRows ; r++)
         for( int c=0; c < nImageCols ; c++)
         {
            if(!Pol.contains(  new Point(c+TopLeft[0].x,TopLeft[0].y+r) ))
            {
               rast.setPixel( c , r , new int[]{0} );
              
            }
            else
            {
               int[] rc = getInputImageRC(  r , c, Span, nImageRows,nImageCols);
               if( rc == null)
               {   rast.setPixel( c , r , new int[]{0} );
              
               }
               else//TODO  do weighted sum .Nope
               {
                  rast.setPixel( c,r, new int[]
                          {(int)image.getDataValue(rc[0] , rc[1] )+1});
                
                 
                 
               }
            }
         }    
      
      
      
      Res.setData( rast );
      ResImage = Res;
      return Res;
   }
   
   /**
    * Calculates row and col from input image from row and col of output
    * image.  Assume that the output point is interior to the projection
    * of the input rectangle on the display plane.
    * 
    * @param outRow
    * @param outCol
    * @return  int array containing row and col of image 
    */
   public int[] getInputImageRC( int outRow, int outCol, Point[] Span,
                   int nImageRows, int nImageCols)
   {
      

      Vector3D PtIn = Out2D2In3D( outRow, outCol, Span);
      PtIn.subtract( center );
      
      int[] Res = new int[2];
      
      int numCols = image.getNumColumns( );
      int numRows = image.getNumRows( );
      
      Res[1] = (int)Math.floor(.5+PtIn.dot( xvec )*numCols/(float)rect.width+ (1+numCols)/2f);
      Res[0] = (int)Math.floor(.5+PtIn.dot( yvec )*numRows/(float)rect.height+ (1+numRows)/2f);
      
      Res[0] = numRows -Res[0]+1;
     
      if( Res[0] <1 || Res[0]>numRows )
         return null;
      
      if( Res[1] <1 || Res[1]>numCols )
         return null;
      
      return Res;      
      
   }
   
   
   // row, col from top left corner of image, Span[0] is 2D projection
   // of top left corner on projection plane. 
   private Vector3D Out2D2Out3D( float row, float col, Point[] Span)
   {
      if( Math.abs( normalIn.dot( normalOut )) < .01 )
         return null;
      Vector3D Px = new Vector3D( xvec_proj);
      Px.multiply( col);
      Vector3D P = new Vector3D( yvec_proj);
      P.multiply(  row ); //yvec in dir increasing pixels along height
      P.add(  Px );
      
      Vector3D V = new Vector3D( xvec_proj);
      V.multiply(  Span[0].x );
      Vector3D V1 = new Vector3D( yvec_proj);
      V1.multiply(  Span[0].y );
      V1.add( V);
      V1.add( center_proj );//?
      V1.add(P);
      return V1;
      
      
      //now add to top left pixel
   }
   
   // row,col from top left of image. Span[0] top left projection
   //   of top left point of image
   // returns a vector that should be on the original rectangle
   private Vector3D Out2D2In3D( float row, float col, Point[] Span)
   {
      Vector3D V = Out2D2Out3D( row, col, Span);
      
      if( V == null)
         return null;
      
      V.subtract(  center );
      float k = -V.dot( normalIn)/normalIn.dot( normalOut);
      Vector3D U = new Vector3D( normalOut);
      U.multiply(k);
      U.add( V );
      U.add( center );
      return U;
      
   }
   
   /**
    *  Image position of top left corner of image
    * @return  An array of two points.  The first point is the "top left corner"
    *         and the 2nd point is the bottom right corner( Really interchanged but
    *         rest do correspondences correctly). Prop: x and y's increase as go from
    *         Point[0] to Point(1}
    */
   public Point[]  getTopLeftImagePosition()
   {
      floatPoint2D[] Edges = get2DRectangleProjections();
      
      float minx = Edges[0].x,
            maxx = Edges[0].x,
            miny = Edges[0].y,
            maxy = Edges[0].y;
         
      for( int i=1; i< Edges.length ; i++)
      {
         if( Edges[i].x < minx)
            
            minx = Edges[i].x;
         
         else if( Edges[i].x > maxx)
            
            maxx =Edges[i].x;
         
         
         
         if( Edges[i].y < miny)
            
            miny = Edges[i].y;
         
         else if( Edges[i].y > maxy)
            
            maxy =Edges[i].y;
      }
      
      Point[] Res = new Point[2];
      Res[0] = new Point( (int)Math.floor(.5+minx), (int)Math.floor(.5+miny));
      Res[1]= new Point( (int)Math.floor(.5+maxx), (int)Math.floor(.5+maxy) );
     //   Res[1] = new Point( (int)Math.floor(.5+minx), (int)Math.floor(.5+miny));
     //   Res[0]= new Point( (int)Math.floor(.5+maxx), (int)Math.floor(.5+maxy) );
       
      return Res;
   }
   
   /**
    * Returns the 2D position on the rectangle from center relative to xvec and yvec
    * 
    * @param x_proj  The x value of the  2D projection of the point in a rectangle 
    * @param y_proj  The y value of the  2D projection of the point in a rectangle 
    * @return  the 2D position on the rectangle from center relative to xvec and yvec
    */
   public floatPoint2D getRectPos( float x_proj, float y_proj)
   {
      Point[] p= getTopLeftImagePosition();
      
      if( p== null || x_proj < p[0].x  || x_proj > p[1].x)
         return null;

      if(  y_proj < p[0].y  || y_proj > p[1].y)
         return null;
      
      x_proj =  x_proj - p[0].x ;
      y_proj = y_proj -  p[0].y ;
      
      Vector3D P = Out2D2In3D( y_proj, x_proj,  p);
      P.subtract( center );
      System.out.println("current rect="+rect);
      return new floatPoint2D( P.dot(xvec), P.dot(yvec) );
      
   }
   public JPanel getTestPanel( BufferedImage image, Point TopLeft )
   {
      return new MyPanel( image, TopLeft);
   }
   
   class MyPanel  extends JPanel implements MouseListener
   {
      /**
       * 
       */
      private static final long serialVersionUID = 1L;
      BufferedImage image;
      Point  TopLeft;
      public MyPanel( BufferedImage image, Point TopLeft)
      {
         super();
         setLayout( new GridLayout(1,1));
         this.image = image;
         this.TopLeft = TopLeft;
         addMouseListener( this);
      }
      
      @Override
      public void mouseClicked(MouseEvent arg0)
      {

        System.out.println("Mouse Clicked at "+ arg0.getX( )+","+arg0.getY());
        System.out.println("    width,height, center="+ rect.width+","+rect.height+","+center);
      }

      @Override
      public void mouseEntered(MouseEvent arg0)
      {

         
         
      }

      @Override
      public void mouseExited(MouseEvent arg0)
      {

         
         
      }

      @Override
      public void mousePressed(MouseEvent arg0)
      {

        
         
      }

      @Override
      public void mouseReleased(MouseEvent arg0)
      {

        
         
      }

      public void paint( Graphics g)
      {
         g.setColor( Color.red );
         g.fillRect( 0,0,800,800);
         g.drawImage(image, TopLeft.x,TopLeft.y,image.getWidth( ),
               image.getHeight( ),this);
         System.out.println("image("+image.getWidth( )+","+
                  image.getHeight());
       
      }
   }
   /**
    * @param args
    */
   public static void main(String[] args)
   {
      
      int N=200;
      int rectWidth =200;
      int rectHeight =400;
      
      float[][]Image = new float[80][80];
      for( int r=0; r<80;r++)
         for( int c=0; c<80;c++)
            Image[r][c] = r*c/(float)(6400)*N;
      
      VirtualArray2D image2D = new VirtualArray2D( Image);
      System.out.println("image rows/cols="+image2D.getNumRows( )+","+
            image2D.getNumColumns( ));
      IndexColorModel colMap = IndexColorMaker.getColorModel( 
            IndexColorMaker.HEATED_OBJECT_SCALE, 200 );
      
      ImageFilledRectangle Rect = new ImageFilledRectangle( image2D,
               colMap, new Dimension(rectWidth,rectHeight), new Vector3D(150,215,-300),
               new Vector3D( 1f,0f,0f), new Vector3D(0,1,0f));
      
      FinishJFrame jf = new FinishJFrame("Test");
      jf.getContentPane( ).setLayout(  new GridLayout(1,1) );
      jf.setSize( 500,500);
      BufferedImage image = Rect.getImage( );
      Point[] P = Rect.getTopLeftImagePosition( );
      JPanel jp = Rect.getTestPanel( image ,P[0]) ;
      jf.getContentPane( ).add( jp);
      jf.setVisible(  true );
      jf.invalidate( );
      jf.getContentPane( ).invalidate();
      
     
     
      
      
      

   }

}
