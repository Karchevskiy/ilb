package ru.inasan.karchevsky.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "parameter")
public class Parameter implements Serializable {
    /**
     * field for index
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "entity_id")
    private Long entity_id;

    @Column(name = "parameter_id")
    private Long parameterId;

    @Column(name = "source_id")
    private Long sourceId;

    @Column(name = "value")
    private String value;

}
