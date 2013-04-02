/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package copesevisitor.model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Savio Dias
 */
@Entity
@Table(name = "bankaccount")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Bankaccount.findAll", query = "SELECT b FROM Bankaccount b"),
    @NamedQuery(name = "Bankaccount.findById", query = "SELECT b FROM Bankaccount b WHERE b.id = :id"),
    @NamedQuery(name = "Bankaccount.findByAgency", query = "SELECT b FROM Bankaccount b WHERE b.agency = :agency"),
    @NamedQuery(name = "Bankaccount.findByChecking", query = "SELECT b FROM Bankaccount b WHERE b.checking = :checking")})
public class Bankaccount implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "agency")
    private String agency;
    @Basic(optional = false)
    @Column(name = "checking")
    private String checking;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bankaccount")
    private Collection<Person> personCollection;
    @JoinColumn(name = "bank_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Bank bank;

    public Bankaccount() {
    }

    public Bankaccount(Integer id) {
        this.id = id;
    }

    public Bankaccount(Integer id, String agency, String checking) {
        this.id = id;
        this.agency = agency;
        this.checking = checking;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public String getChecking() {
        return checking;
    }

    public void setChecking(String checking) {
        this.checking = checking;
    }

    @XmlTransient
    public Collection<Person> getPersonCollection() {
        return personCollection;
    }

    public void setPersonCollection(Collection<Person> personCollection) {
        this.personCollection = personCollection;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
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
        if (!(object instanceof Bankaccount)) {
            return false;
        }
        Bankaccount other = (Bankaccount) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Pictures.Bankaccount[ id=" + id + " ]";
    }
    
}
