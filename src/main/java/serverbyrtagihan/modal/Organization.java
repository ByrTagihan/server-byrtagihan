package serverbyrtagihan.modal;

import serverbyrtagihan.auditing.DateConfig;

import javax.persistence.*;

@Entity
@Table(name = "organization")
public class Organization extends DateConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "customer_id")
    private Long customer_id = 0L;

    @Column(name = "address")
    private String address;

    @Column(name = "hp")
    private String hp;

    @Column(name = "email")
    private String email;

    @Column(name = "city")
    private String city;

    @Column(name = "provinsi")
    private String provinsi;

    @Column(name = "balance")
    private Double balance = 0.0;

    @Column(name = "bank_account_number")
    private int bank_account_number;

    @Column(name = "bank_account_name")
    private String bank_account_name;

    @Column(name = "bank_name")
    private String bank_name;

    @Column(name = "fee_admin")
    private Double fee_admin = 0.0;

    public Organization() {
    }

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

    public Long getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(Long customer_id) {
        this.customer_id = customer_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHp() {
        return hp;
    }

    public void setHp(String hp) {
        this.hp = hp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvinsi() {
        return provinsi;
    }

    public void setProvinsi(String provinsi) {
        this.provinsi = provinsi;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public int getBank_account_number() {
        return bank_account_number;
    }

    public void setBank_account_number(int bank_account_number) {
        this.bank_account_number = bank_account_number;
    }

    public String getBank_account_name() {
        return bank_account_name;
    }

    public void setBank_account_name(String bank_account_name) {
        this.bank_account_name = bank_account_name;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public Double getFee_admin() {
        return fee_admin;
    }

    public void setFee_admin(Double fee_admin) {
        this.fee_admin = fee_admin;
    }
}
