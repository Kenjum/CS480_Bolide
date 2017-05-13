package bolide.planner;

public class Note {
    private String id = "";
    private String title = "";
    private String body = "";

    Note(String newID, String newTitle, String newBody){
        id = newID;
        title = newTitle;
        body = newBody;
    }

    public void setID(String newID){id = newID;}
    public void setTitle(String newTitle){title = newTitle;}
    public void setBody(String newBody){body = newBody;}

    public String getID(){return id;}
    public String getTitle(){return title;}
    public String getBody(){return body;}
}
