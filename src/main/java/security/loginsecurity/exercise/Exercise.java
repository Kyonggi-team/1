package security.loginsecurity.exercise;

import jakarta.persistence.*;
import lombok.*;
import security.loginsecurity.member.Member;


@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Exercise {

    @Id
    private Long id;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @MapsId
    @JoinColumn(name = "id")
    private Member member;

    @Setter
    private String exercise;

    @Setter
    private int goal;

}
