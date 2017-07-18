package org.mytestng.assign4;
import java.util.Map;


public class Version {
    
    private final String version;
    private final String newBoardUrl;
    private final String newBoardMethod;
    private final String newPlayerUrl;
    private final String newPlayerMethod;
    private final String   getParticularBoardUrl;
    private final String getParticularMethod;
    private final String movePlayerUrl;
    private final String movePlayerMethod;
    private final String deletePlayerUrl;
    private final String deletePlayerMethod;
    private final String deleteBoardUrl;
    private final String deleteBoardMethod;
    
    public Version(Map<String, String> versionMap){
 
    this.version   = versionMap.get("version");
    this.newBoardUrl     = versionMap.get("newBoardUrl");
    this.newBoardMethod = versionMap.get("newBoardMethod");
    this.newPlayerUrl = versionMap.get("newPlayerUrl");
    this.newPlayerMethod = versionMap.get("newPlayerMethod");
    this.getParticularBoardUrl = versionMap.get("getParticularBoardUrl");
    this.getParticularMethod = versionMap.get("getParticularMethod");
    this.movePlayerUrl = versionMap.get("movePlayerUrl");
    this.movePlayerMethod = versionMap.get("movePlayerMethod");
    this.deletePlayerUrl = versionMap.get("deletePlayerUrl");
    this.deletePlayerMethod = versionMap.get("deletePlayerMethod");
    this.deleteBoardMethod = versionMap.get("deleteBoardMethod");
    this.deleteBoardUrl =  versionMap.get("deleteBoardUrl");
    
    
    }
    
  
    public String getVersion(){
        return version;
    }

    public String getNewBoardUrl(){
        return newBoardUrl;
    }
    public String getNewPlayerUrl(){
        return newPlayerUrl;
    }
    public String getGetparticularBoardUrl(){
        return getParticularBoardUrl;
    }
    public String getMovePlayerUrl(){
        return movePlayerUrl;
    }
    public String getDeletePlayerUrl(){
        return deletePlayerUrl;
    }
    public String getDeleteBoardUrl(){
        return deleteBoardUrl;
    }

}
