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
package org.openxls.formats.XLS;

import org.openxls.toolkit.ByteTools;

/**
 * Record specifying the bottom margin of the sheet for printing.
 */
public class BottomMargin extends XLSRecord
{
	private static final long serialVersionUID = -3649192673573344145L;

	double margin;

	public BottomMargin()
	{
		setOpcode( BOTTOMMARGIN );
		margin = 1.0;    // default
		setData( ByteTools.doubleToLEByteArray( margin ) );
	}

	@Override
	public void init()
	{
		super.init();

		margin = ByteTools.eightBytetoLEDouble( getBytesAt( 0, 8 ) );
	}

	@Override
	public void setSheet( Sheet sheet )
	{
		super.setSheet( sheet );
		((Boundsheet) sheet).addPrintRec( this );
	}

	public double getMargin()
	{
		return margin;
	}

	public void setMargin( double value )
	{
		margin = value;
		setData( ByteTools.doubleToLEByteArray( value ) );
	}
}
