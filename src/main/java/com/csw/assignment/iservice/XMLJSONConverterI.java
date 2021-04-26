package com.csw.assignment.iservice;

import java.io.File;

public interface XMLJSONConverterI {

	public void convertJSONtoXML(File jsonInput, File xmlOutput) throws Exception;
}
