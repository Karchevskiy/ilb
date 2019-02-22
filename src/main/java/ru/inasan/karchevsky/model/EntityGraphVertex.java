package ru.inasan.karchevsky.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.util.Pair;

import javax.persistence.*;
import javax.persistence.Parameter;
import java.io.Serializable;
import java.util.Collection;


@Getter
@Setter
@Entity
@Table(name = "entity_graph")
public class EntityGraphVertex implements Serializable {

    /**
     * field for index
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "primary_id")
    private Long primaryId;

    @Column(name = "secondary_id")
    private Long secondaryId;

    @Column(name = "pair_id")
    private Long pairId;

    @Column(name = "field_id")
    private Long fieldId;


    @Transient
    public Pair<EntityGraphVertex, EntityGraphVertex> childNode;
    @Transient
    private Collection<Pair<Parameter, ?>> params;
}