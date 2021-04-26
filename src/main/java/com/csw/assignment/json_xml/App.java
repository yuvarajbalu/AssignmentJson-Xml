package com.csw.assignment.json_xml;

import java.io.File;

import com.csw.assignment.iservice.impl.XMLJSONConverterImpl;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
		
		try {
	    	if(args.length!=2) {
				throw new  IllegalArgumentException("No of arguments mismatch ");
			}
			new XMLJSONConverterImpl().convertJSONtoXML(new File(args[0]), new File(args[1]));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
