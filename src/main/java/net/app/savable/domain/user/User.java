package net.app.savable.domain.user;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.app.savable.domain.BaseTimeEntity;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "app_user")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column
    private String picture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column
    private String phoneNumber;

    @Builder
    public User(String name, String email, String picture, Role role, String phoneNumber){
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
        this.phoneNumber = phoneNumber;
    }

    public User update(String name, String picture){
        this.name = name;
        this.picture = picture;

        return this;
    }

    public String getRoleKey(){
        return this.role.getKey();
    }
}
