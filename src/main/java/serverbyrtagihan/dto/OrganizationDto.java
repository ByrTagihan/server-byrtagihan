package serverbyrtagihan.dto;

public class OrganizationDto {

    private String name;

    private Long customerId;

    private String addres;

    private String hp;

    private String email;

    private String city;

    private String provinsi;

    private Double balance;

    private int bank_acount_number;

    private String bank_account_name;

    private String bank_name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getAddres() {
        return addres;
    }

    public void setAddres(String addres) {
        this.addres = addres;
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

    public int getBank_acount_number() {
        return bank_acount_number;
    }

    public void setBank_acount_number(int bank_acount_number) {
        this.bank_acount_number = bank_acount_number;
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
}
