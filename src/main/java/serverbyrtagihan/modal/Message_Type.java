package serverbyrtagihan.modal;


import serverbyrtagihan.auditing.DateConfig;

import javax.persistence.*;

@Entity
@Table(name = "message_type")
public class Message_Type extends DateConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
