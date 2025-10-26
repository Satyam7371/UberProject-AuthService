package org.example.uberprojectauthservice.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@EntityListeners(AuditingEntityListener.class)                  // automatically handle like created and updated at
@MappedSuperclass                   // this allows to handle inheritance spring data jpa
@Getter
@Setter
public abstract class BaseModel {

    @Id                       // id is used to set the primary key in a table
    @GeneratedValue(strategy = GenerationType.IDENTITY)          // identity means auto_increment
//    @GeneratedValue(strategy = GenerationType.TABLE)          // Here have to change to TABLE to use Table_Per_Class inheritance to create tables
    protected Long id;

    @Column(nullable = false)                  // to make not null
    @Temporal(TemporalType.TIMESTAMP)             // tells spring about the formats of Date object to be stored i.e. Date / Time ? Timestamp
    @CreatedDate                            // only handle for object creation
    protected Date createdAt;


    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate                          // handle the last updation handle it for object update
    protected Date updatedAt;

}

