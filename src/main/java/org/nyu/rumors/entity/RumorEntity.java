package org.nyu.rumors.entity;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name="rumors")
public class RumorEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name="name")
    private String name;

    @Column(name="rumor")
    private String rumor;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRumor() {
        return rumor;
    }

    public void setRumor(String rumor) {
        this.rumor = rumor;
    }

    public UUID getId() { return id; }

    public void setId(UUID id) { this.id = id; }

}
