/*
 * --------- BEGIN COPYRIGHT NOTICE ---------
 * Copyright 2002-2012 Extentech Inc.
 * Copyright 2013 Infoteria America Corp.
 * 
 * This file is part of OpenXLS.
 * 
 * OpenXLS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 * 
 * OpenXLS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with OpenXLS.  If not, see
 * <http://www.gnu.org/licenses/>.
 * ---------- END COPYRIGHT NOTICE ----------
 */
package docs.samples.Images;

import org.openxls.ExtenXLS.CellHandle;
import org.openxls.ExtenXLS.ImageHandle;
import org.openxls.ExtenXLS.WorkBookHandle;
import org.openxls.ExtenXLS.WorkSheetHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/* Test the operation of image insert and extraction
 * 
 * demonstrates how to use the WorkBookHandle thumbnail images

*/
public class TestImages
{
	private static final Logger log = LoggerFactory.getLogger( TestImages.class );
	// change to the folder containing the image files and spreadsheet templates
	String workingdir = System.getProperty( "user.dir" ) + "/docs/samples/Images/";
	int DEBUGLEVEL = 0;
	WorkSheetHandle sheet = null;
	String sheetname = "Sheet1";
	String finpath = "";

	public static void main( String[] args )
	{
		TestImages ti = new TestImages( "Test Images", 0 );
		ti.test();

	}

	public TestImages( String name, int lev )
	{
		DEBUGLEVEL = lev;
	}

	/**
	 * read in an XLS file, output BiffRec vals, write it back to a new file
	 */
	public void test()
	{
		// working files
		String[] testfiles = {
				"testImages.xls", "Sheet1",
		};

		for( int i = 0; i < testfiles.length; i++ )
		{
			finpath = testfiles[i];
			System.out.println( "====================== TEST: " + String.valueOf( i ) + " ======================" );
			sheetname = testfiles[++i];
			System.out.println( "=====================> " + finpath + ":" + sheetname );

			// various image handling tests
			doit( finpath, sheetname );

			// generate a WorkBook thumbnail image
			testWorkBookImage();

			System.out.println( "DONE" );
		}
	}

	void doit( String finpath, String sheetname )
	{
		System.out.println( "Begin parsing: " + workingdir + finpath );
		WorkBookHandle tbo = new WorkBookHandle( workingdir + finpath );

		try
		{
			sheet = tbo.getWorkSheet( sheetname );
			// read images from sheet 1 -- .gif, .png, .jpg
			ImageHandle[] extracted = sheet.getImages();
			// extract and output images
			for( int t = 0; t < extracted.length; t++ )
			{
				System.out
				      .println( "Successfully extracted: " + workingdir + "testImageOut_" + extracted[t].getName() + "." + extracted[t].getType() );
				FileOutputStream outimg = new FileOutputStream( workingdir + extracted[t].getName() + "." + extracted[t].getType() );
				extracted[t].write( outimg );
				outimg.flush();
				outimg.close();
			}

			tbo = new WorkBookHandle();
			sheet = tbo.getWorkSheet( "Sheet1" );
			CellHandle a1 = sheet.add( "This is a new workbook with 3 images: a gif, a jpg, and a png", "A1" );

			// get gif image input stream
			FileInputStream fin = new FileInputStream( workingdir + "testImages.gif" );

			// add to sheet
			ImageHandle giffy = new ImageHandle( fin, sheet );

			// set picture size and location in sheet
			giffy.setCoords( 100, 100, 400, 200 );
			giffy.setName( "giffy" );
			sheet.insertImage( giffy );

			// add to sheet
			for( int x = 0; x < 100; x++ )
			{
				fin = new FileInputStream( workingdir + "testImages.png" );
				ImageHandle jpgy = new ImageHandle( fin, sheet );
				jpgy.setName( "heart" + x );
				// set the random x/y coords of picture
				int ix = Math.round( (float) ((x * (Math.random() * 10))) );
				jpgy.setX( 100 + ix );
				ix = Math.round( (float) ((x * (Math.random() * 10))) );
				jpgy.setY( 100 + ix );
				sheet.insertImage( jpgy );
			}
			// get png image input stream

			fin = new FileInputStream( workingdir + "testImages.jpg" );

			// add to sheet
			ImageHandle pngy = new ImageHandle( fin, sheet );

			// set just the x/y coords of picture
			pngy.setX( 10 );
			pngy.setY( 200 );
			sheet.insertImage( pngy );

		}
		catch( Exception e )
		{
			System.err.println( "testImages failed: " + e.toString() );
		}
		testWrite( tbo, workingdir + "testImagesOut.xls" );
		WorkBookHandle newbook = new WorkBookHandle( workingdir + "testImagesOut.xls" );
		System.out.println( "Successfully read: " + newbook );
	}

	/**
	 * test the creation of the WorkBookHandle thumbnail image
	 */
	public void testWorkBookImage()
	{
		WorkBookHandle book = new WorkBookHandle( workingdir + "testImages.xls" );
		try
		{
//	    	File fout = book.getImage(workingdir + "testWorkBookImage.jpg", 200, 150, 800, 600);
			ImageHandle image = book.getImage( "foo" );
			image.setCoords( 200, 150, 800, 600 );
			File fout = new File( workingdir + "_NBP_testWorkBookImage.jpg" );
			image.write( new FileOutputStream( fout ) );
			log.info( "Successfully generated WorkBook thumbnail image at: " + fout.getCanonicalPath() );

		}
		catch( Exception e )
		{
			System.err.println( "failed generating WorkBook thumbnail image for " + book + ": " + e );
		}
	}

	public static void testWrite( WorkBookHandle b, String fout )
	{
		try
		{
			java.io.File f = new java.io.File( fout );
			FileOutputStream fos = new FileOutputStream( f );
			BufferedOutputStream bbout = new BufferedOutputStream( fos );
			b.write( bbout );
			bbout.flush();
			fos.close();
		}
		catch( java.io.IOException e )
		{
			log.info( "IOException in Tester.  " + e );
		}
	}

}