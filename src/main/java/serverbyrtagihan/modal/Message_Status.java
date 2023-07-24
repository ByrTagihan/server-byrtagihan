package serverbyrtagihan.modal;


import serverbyrtagihan.auditing.DateConfig;

import javax.persistence.*;

@Entity
@Table(name = "message_status")
public class Message_Status extends DateConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;
}
