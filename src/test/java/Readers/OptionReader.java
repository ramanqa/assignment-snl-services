package Readers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class OptionReader {

	public String optionFileReader(String optionKey) throws IOException
	{
		File f = new File("resources/data.properties");

		BufferedReader b = new BufferedReader(new FileReader(f));

		String readLine = "";
		boolean flag = false;

		String arrsplit[],temp[] = null;
		while ((readLine = b.readLine()) != null) {
			
			arrsplit = readLine.split("=");

			if (arrsplit[0].equals(optionKey)&&!arrsplit[0].startsWith("#")) {
				flag=true;
				temp=readLine.split("=");
			}
	
		}

		
		if(flag==true)
		{
			
			return temp[1].trim().toLowerCase();
		}
		return null;

		
	}
	
	
}
