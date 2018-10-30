package ru.inasan.karchevsky.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "star_system")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StarSystemGrouped implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(AccessType.PROPERTY)
    private Long id;

    @Column(name = "system_id")
    private String systemId;
}
