package ru.inasan.karchevsky.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;


@Getter
@Setter
@Entity
@Table(name = "entity_source")
public class EntitySource implements Serializable {
    /**
     * field for index
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "source_id")
    private Long sourceId;

    @Column(name = "entity_id")
    private Long entity_id;

    @Column(name = "row")
    private String row;

}
