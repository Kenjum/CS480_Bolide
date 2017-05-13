package bolide.planner;

public class Note {
    private int id = 0;
    private String title = "";
    private String body = "";

    Note(int newID, String newTitle, String newBody){
        id = newID;
        title = newTitle;
        body = newBody;
    }

    public void setID(int newID){id = newID;}
    public void setTitle(String newTitle){title = newTitle;}
    public void setBody(String newBody){body = newBody;}

    public int getID(){return id;}
    public String getTitle(){return title;}
    public String getBody(){return body;}
}
