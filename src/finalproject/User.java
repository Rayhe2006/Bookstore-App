package finalproject;

public class User {

    protected String pwd;
    protected String usr;
    
    public User(String usr, String pwd){
        this.pwd = pwd;
        this.usr = usr;
    }
    
    public String getUsername(){
        return usr;
    }
    
    public String getPassword(){
        return pwd;
    }
}
