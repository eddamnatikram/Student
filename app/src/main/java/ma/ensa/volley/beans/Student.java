package ma.ensa.volley.beans;


public class Student extends User {
    private Long id;
    private String name;
    private int phone;
    private String email;
    private Filiere filiere;
    public Student(Long id, String name, int phone, String email, Filiere filiere) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.filiere = filiere;
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

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Filiere getFiliere() {
        return filiere;
    }

    public void setFiliere(Filiere filiere) {
        this.filiere = filiere;
    }
}

