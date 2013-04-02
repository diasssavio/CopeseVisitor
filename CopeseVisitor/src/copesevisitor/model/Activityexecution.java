/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package copesevisitor.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Sávio Dias
 */
@Entity
@Table(name = "activityexecution")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Activityexecution.findAll", query = "SELECT a FROM Activityexecution a"),
    @NamedQuery(name = "Activityexecution.findById", query = "SELECT a FROM Activityexecution a WHERE a.id = :id"),
    @NamedQuery(name = "Activityexecution.findByInstitution", query = "SELECT a FROM Activityexecution a WHERE a.institution = :institution"),
    @NamedQuery(name = "Activityexecution.findByHoursworked", query = "SELECT a FROM Activityexecution a WHERE a.hoursworked = :hoursworked"),
    @NamedQuery(name = "Activityexecution.findByYear", query = "SELECT a FROM Activityexecution a WHERE a.year = :year")})
public class Activityexecution implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Lob
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @Column(name = "institution")
    private String institution;
    @Basic(optional = false)
    @Column(name = "hoursworked")
    private float hoursworked;
    @Basic(optional = false)
    @Column(name = "year")
    @Temporal(TemporalType.DATE)
    private String year;
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Person person;

    public Activityexecution() {
    }

    public Activityexecution(Integer id) {
        this.id = id;
    }

    public Activityexecution(Integer id, String description, String institution, float hoursworked, String year) {
        this.id = id;
        this.description = description;
        this.institution = institution;
        this.hoursworked = hoursworked;
        this.year = year;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public float getHoursworked() {
        return hoursworked;
    }

    public void setHoursworked(float hoursworked) {
        this.hoursworked = hoursworked;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
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
        if (!(object instanceof Activityexecution)) {
            return false;
        }
        Activityexecution other = (Activityexecution) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "copesevisitor.model.Activityexecution[ id=" + id + " ]";
    }
    
}
