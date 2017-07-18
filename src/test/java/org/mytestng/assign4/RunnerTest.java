package org.mytestng.assign4;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class RunnerTest {

	String version;
	String deletePlayerUrl;
	String deleteBoardUrl;
	String particularBoardUrl;
	String newBoardUrl;
	String addPlayerUrl;
	String movePlayerUrl;
	Controller controller;
	
	@Test(dataProvider="getData")
	public void init(Version versionObject) throws Throwable
	{

		version = versionObject.getVersion();
		newBoardUrl = versionObject.getNewBoardUrl();
		movePlayerUrl = versionObject.getMovePlayerUrl();
		addPlayerUrl = versionObject.getNewPlayerUrl();
		deleteBoardUrl = versionObject.getDeleteBoardUrl();
		deletePlayerUrl = versionObject.getDeletePlayerUrl();
		particularBoardUrl = versionObject.getGetparticularBoardUrl();
		controller = new Controller();

	  
	  controller.getBoardTest(newBoardUrl, version);
	  

	 controller.addPlayers(particularBoardUrl, addPlayerUrl, version); 
 
	 controller.updatingPlayers(particularBoardUrl, addPlayerUrl, version); 

	 controller.getList(particularBoardUrl, version);

	 controller.movePlayer(movePlayerUrl, version); 

	 controller.deletePlayer(deletePlayerUrl, version); 

	  System.out.println(deleteBoardUrl);
	 controller.deleteBoard(deleteBoardUrl, version); 
  }
  
  @DataProvider
	public Version[][] getData()
	{
	//Rows - Number of times your test has to be repeated.
	//Columns - Number of parameters in test data.
	  

		Version versionObject;
		JsonReader jsonReader = new  JsonReader();
	
	  
	Version[][] data = new Version[3][1];

	// 1st row
	data[0][0] =jsonReader.getListOfVersions().get(0);
	

	// 2nd row
	data[1][0] =jsonReader.getListOfVersions().get(1);
	
	// 3rd row
	data[2][0] =jsonReader.getListOfVersions().get(2);

	return data;
	}
  
  
}