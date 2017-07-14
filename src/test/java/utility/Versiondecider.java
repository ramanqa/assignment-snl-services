package utility;

import java.io.IOException;


import Readers.OptionReader;


public class Versiondecider {

	public String readit(String Key) throws IOException {
		String data_value = new OptionReader().optionFileReader("version");

		return data_value;

	}

}
