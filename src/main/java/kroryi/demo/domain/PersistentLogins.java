package kroryi.demo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Getter
@Setter
public class PersistentLogins {
    @Id
    private String username;
    private String series;
    private String token;
    @Column(name="last_used")
    private Date lastUsed;
}
