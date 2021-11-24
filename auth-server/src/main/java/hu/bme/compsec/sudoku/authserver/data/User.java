package hu.bme.compsec.sudoku.authserver.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hu.bme.compsec.sudoku.authserver.common.UserRole;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.*;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
//    @GeneratedValue(generator = "UUID", strategy = GenerationType.IDENTITY)
//    private UUID id;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    @NonNull
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<UserRole> roles = List.of(UserRole.USER);

    @Builder.Default
    private boolean enabled = true;

    public Collection<GrantedAuthority> getAuthorities() {
        var authorities = new HashSet<GrantedAuthority>();
        roles.forEach(r -> authorities.addAll(r.getGrantedAuthorities()));

        return Collections.unmodifiableCollection(authorities);
    }

}
