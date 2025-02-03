package io.aryby.spring_boot_crud.custom_table_attributes;

import io.aryby.spring_boot_crud.custom_table.CustomTable;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Table(name = "CustomTableAttributeses")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class CustomTableAttributes {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nameTypeModifier;

    @Column(nullable = false)
    private String nameAttribute;

    @Column(columnDefinition = "longtext")
    private String sizeJpaAttributes;

    @Column(columnDefinition = "longtext")
    private String customJoins;

    @Column(columnDefinition = "longtext")
    private String customRelations;

    private Long customTable;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

}
