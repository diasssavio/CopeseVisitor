package copesevisitor.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author <a href="mailto:diasssavio@gmail.com">Sávio S. Dias</a>
 */
@Entity
@Table(name = "person")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Person.findAll", query = "SELECT p FROM Person p"),
    @NamedQuery(name = "Person.findById", query = "SELECT p FROM Person p WHERE p.id = :id"),
    @NamedQuery(name = "Person.findByCpf", query = "SELECT p FROM Person p WHERE p.cpf = :cpf"),
    @NamedQuery(name = "Person.findByName", query = "SELECT p FROM Person p WHERE p.name = :name"),
    @NamedQuery(name = "Person.findByBirthdate", query = "SELECT p FROM Person p WHERE p.birthdate = :birthdate"),
    @NamedQuery(name = "Person.findByRg", query = "SELECT p FROM Person p WHERE p.rg = :rg"),
    @NamedQuery(name = "Person.findByWichorgan", query = "SELECT p FROM Person p WHERE p.wichorgan = :wichorgan"),
    @NamedQuery(name = "Person.findByGender", query = "SELECT p FROM Person p WHERE p.gender = :gender"),
    @NamedQuery(name = "Person.findByPispasep", query = "SELECT p FROM Person p WHERE p.pispasep = :pispasep"),
    @NamedQuery(name = "Person.findByPhone1", query = "SELECT p FROM Person p WHERE p.phone1 = :phone1"),
    @NamedQuery(name = "Person.findByPhone2", query = "SELECT p FROM Person p WHERE p.phone2 = :phone2"),
    @NamedQuery(name = "Person.findByPhone3", query = "SELECT p FROM Person p WHERE p.phone3 = :phone3")})
public class Person implements Serializable {
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "person")
    private Collection<Activityexecution> activityexecutionCollection;
    @Basic(optional = false)
    @Lob
    @Column(name = "email")
    private String email;
    @Column(name = "siape")
    private String siape;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "cpf")
    private String cpf;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Column(name = "birthdate")
    @Temporal(TemporalType.DATE)
    private Date birthdate;
    @Basic(optional = false)
    @Column(name = "rg")
    private String rg;
    @Basic(optional = false)
    @Column(name = "wichorgan")
    private String wichorgan;
    @Basic(optional = false)
    @Column(name = "gender")
    private String gender;
    @Basic(optional = false)
    @Column(name = "pispasep")
    private String pispasep;
    @Basic(optional = false)
    @Column(name = "phone1")
    private String phone1;
    @Column(name = "phone2")
    private String phone2;
    @Column(name = "phone3")
    private String phone3;
    @Basic(optional = false)
    @Lob
    @Column(name = "uftlink")
    private String uftlink;
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Address address;
    @JoinColumn(name = "bankaccount_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Bankaccount bankaccount;
    @JoinColumn(name = "place_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Place place;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "person")
    private Collection<Activity> activityCollection;

    public Person() {
    }

    public Person(Integer id) {
        this.id = id;
    }

    public Person(Integer id, String cpf, String name, Date birthdate, String rg, String gender, String pispasep, String phone1, String uftlink) {
        this.id = id;
        this.cpf = cpf;
        this.name = name;
        this.birthdate = birthdate;
        this.rg = rg;
        this.gender = gender;
        this.pispasep = pispasep;
        this.phone1 = phone1;
        this.uftlink = uftlink;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getWichorgan() {
        return wichorgan;
    }
    
    public void setWichorgan(String wichorgan) {
        this.wichorgan = wichorgan;
    }
    
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPispasep() {
        return pispasep;
    }

    public void setPispasep(String pispasep) {
        this.pispasep = pispasep;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getPhone3() {
        return phone3;
    }

    public void setPhone3(String phone3) {
        this.phone3 = phone3;
    }

    public String getUftlink() {
        return uftlink;
    }

    public void setUftlink(String uftlink) {
        this.uftlink = uftlink;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Bankaccount getBankaccount() {
        return bankaccount;
    }

    public void setBankaccount(Bankaccount bankaccount) {
        this.bankaccount = bankaccount;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    @XmlTransient
    public Collection<Activity> getActivityCollection() {
        return activityCollection;
    }

    public void setActivityCollection(Collection<Activity> activityCollection) {
        this.activityCollection = activityCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Person)) {
            return false;
        }
        Person other = (Person) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Pictures.Person[ id=" + id + " ]";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSiape() {
        return siape;
    }

    public void setSiape(String siape) {
        this.siape = siape;
    }

    @XmlTransient
    public Collection<Activityexecution> getActivityexecutionCollection() {
        return activityexecutionCollection;
    }

    public void setActivityexecutionCollection(Collection<Activityexecution> activityexecutionCollection) {
        this.activityexecutionCollection = activityexecutionCollection;
    }
    
}
