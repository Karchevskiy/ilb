package ru.inasan.karchevsky.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "matching_criteria")
public class MatchingCriteria implements Serializable {

    /**
     * field for index
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "entity1")
    private Long entity1;

    @Column(name = "entity2")
    private Long entity2;

    @Column(name = "criteria")
    private String criteria;

}
