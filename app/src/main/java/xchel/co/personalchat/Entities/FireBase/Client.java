package xchel.co.personalchat.Entities.FireBase;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

public class Client {

    private int id;
    private String name;
    private String lastName;
    private String email;
    private String comments;
    private String concept;
    private int type;

    //crear más atrbutos dependiendo el objetivo de la app

    //private Object createTimestamp;

    public Client() {
        //createTimestamp = ServerValue.TIMESTAMP;
    }




    //public Client(int id, String name, String lastName, String email, String comments, String concept, int type, String date) {
    //    this.id = id;
    //    this.name = name;
    //    this.lastName = lastName;
    //    this.email = email;
    //    this.comments = comments;
    //    this.concept = concept;
    //    this.type = type;
    //}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    //getter setter date
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
