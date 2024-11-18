package com.bmanager.users.model;

import com.bmanager.users.dto.UserPost;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "username", nullable = false)
    private String username;

    @ManyToMany
    @Fetch(FetchMode.JOIN)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleModel> roles;

    public UserModel(Long id, String email, String password, String username) {
        setId(id);
        setEmail(email);
        setPassword(password);
        setUsername(username);
    }

    public UserModel(UserPost user) {
        setUsername(user.getUsername());
        setEmail(user.getEmail());
        setPassword(user.getPassword());
    }
}
