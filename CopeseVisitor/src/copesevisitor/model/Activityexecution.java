/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package copesevisitor.model;

import java.io.Serializable;
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
    @NamedQuery(name = "Activityexecution.findByHoursworked", query = "SELECT a FROM Activityexecution a WHERE a.hoursworked = :hoursworked"),
    @NamedQuery(name = "Activityexecution.findByStatus", query = "SELECT a FROM Activityexecution a WHERE a.status = :status")})
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
    @Lob
    @Column(name = "campus")
    private String campus;
    @Basic(optional = false)
    @Lob
    @Column(name = "edict")
    private String edict;
    @Basic(optional = false)
    @Column(name = "hoursworked")
    private float hoursworked;
    @Basic(optional = false)
    @Column(name = "status")
    private boolean status;
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Person person;

    public Activityexecution() {
    }

    public Activityexecution(Integer id) {
        this.id = id;
    }

    public Activityexecution(Integer id, String description, String campus, String edict, float hoursworked, boolean status) {
        this.id = id;
        this.description = description;
        this.campus = campus;
        this.edict = edict;
        this.hoursworked = hoursworked;
        this.status = status;
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

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getEdict() {
        return edict;
    }

    public void setEdict(String edict) {
        this.edict = edict;
    }

    public float getHoursworked() {
        return hoursworked;
    }

    public void setHoursworked(float hoursworked) {
        this.hoursworked = hoursworked;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
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
