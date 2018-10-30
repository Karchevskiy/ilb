package ru.inasan.karchevsky.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "element")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SystemAbstractElement {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(AccessType.PROPERTY)
    private Long id;

    @Lob
    @Column(name = "value")
    private Serializable value;

    @Column(name = "system_id")
    private String systemId;
}
