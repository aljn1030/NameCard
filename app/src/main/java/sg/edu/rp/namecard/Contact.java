package sg.edu.rp.namecard;

import java.io.Serializable;

/**
 * Created by 15017185 on 29/7/2017.
 */

public class Contact implements Serializable{
    public String name;
    public String mobile;
    public String email;
    public String company;
    public int id = -1;

    public Contact(String name, String mobile, String email, String company) {
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.company = company;
    }

    public Contact(int id, String name, String mobile, String email, String company) {
        this.id = id;
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.company = company;
    }
}
