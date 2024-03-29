package serverbyrtagihan.modal;


import serverbyrtagihan.auditing.DateConfig;

import javax.persistence.*;

@Entity
@Table(name = "message_status")
public class Message_Status extends DateConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

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
}
