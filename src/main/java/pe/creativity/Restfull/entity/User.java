package pe.creativity.Restfull.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Table(name = "security_user")
@Entity
public class User {

    private static final long serialVersionUID = -5258915487222108493L;

    @GeneratedValue
    @Id
    @Column(name = "id_users", unique = true)
    private Long IdUsers;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    @JsonIgnore
    private String passwd;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;


    public User() {
    }

    public User(String username, String passwd, Role role, String firstName, String lastName) {
        this.passwd = passwd;
        this.username = username;
        this.role = Arrays.asList(role);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable
    private List<Role> role;


    public Long getIdUsers() {
        return IdUsers;
    }

    public void setIdUsers(Long idUsers) {
        IdUsers = idUsers;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Role> getRole() {
        return role;
    }

    public void setRole(List<Role> role) {
        this.role = role;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
