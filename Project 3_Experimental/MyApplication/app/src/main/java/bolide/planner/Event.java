package bolide.planner;

public class Event {
    private String id = "";
    private String title = "";
    private String body = "";
    private String Location = "";


    Event(String newID, String newTitle, String newBody){
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