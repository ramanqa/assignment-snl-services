package utility;

import java.io.IOException;


import Readers.OptionReader;


public class Datadecider {

	public String readit(String Key) throws IOException {
		String data_value = new OptionReader().optionFileReader(Key);

		return data_value;

	}
	public void writeit(String Key,String value) throws IOException {
	   new OptionReader().writeit(Key, value);


	}
	
	

}
