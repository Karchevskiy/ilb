package ru.inasan.karchevsky.model.meta;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "criteria_meta")
public class CriteriaMeta implements Serializable {
    /**
     * field for index
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "index")
    private Long index;

    @Column(name = "description")
    private String description;

    @Column(name = "is_matching")
    private Boolean isMatching;

}
