package hr.fer.progi.dogGO.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class WalkerLogin {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    private Walker walker;

    @Column(unique=true, nullable = false)
    @Size(min=3, max=20)
    @NotNull
    private String username;

    @NotNull
    private String password;

    public WalkerLogin() {

    }

    public WalkerLogin(Walker walker, String username, String password) {
        this.walker = walker;
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Walker getWalker() {
        return walker;
    }

    public void setWalker(Walker walker) {
        this.walker = walker;
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
