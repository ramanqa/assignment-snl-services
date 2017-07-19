package web_service_test.assignment_snl_services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import Readers.OptionReader;

public class Provide_version_Test {

	static OptionReader read;

	public static void main(String args[]) throws Exception {

		Provide_version_Test test = new Provide_version_Test();

		
		test.writeit("version", args[0]);

	}

	public void writeit(String optionKey, String optionValue) throws IOException {
	
		File f = new File("resources/data.properties");

		String readLine = "", olddata = "";
		BufferedReader b = new BufferedReader(new FileReader(f));
		while ((readLine = b.readLine()) != null) {

			if (olddata == "") {
				olddata = olddata + readLine;
			} else {
				olddata = olddata + "\n" + readLine;

			}
		}

		olddata = olddata + "\n";

		try {
			FileWriter fw = new FileWriter("resources/data.properties");
			fw.append(olddata + optionKey + "=" + optionValue.toString());
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		b.close();

	}

}
