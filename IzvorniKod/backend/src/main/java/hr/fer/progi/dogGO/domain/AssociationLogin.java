package hr.fer.progi.dogGO.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class AssociationLogin {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    private Association association;

    @Column(unique=true, nullable = false)
    @Size(min=3, max=20)
    @NotNull
    private String username;

    @NotNull
    private String password;

    public AssociationLogin(Association association, String userName, String password) {
        this.association = association;
        this.username = userName;
        this.password = password;
    }

    public AssociationLogin() {
    }

    public Long getId() {
        return id;
    }

    public Association getAssociation() {
        return association;
    }

    public void setAssociation(Association association) {
        this.association = association;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
