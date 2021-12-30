package com.miniproject.pokedex.model.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "pokemon")
@DynamicInsert
@DynamicUpdate
data class PokemonEntity (
    @Id
    @Column(name = "id")
    @JsonIgnore
    var id: Long? = null,

    @Column(name = "name")
    var name: String? = null,

    @Column(name = "type")
    var type: String? = null,

    @Column(name = "sprite")
    var sprite: String? = null,

    @Column(name = "weakness")
    var weakness: String? = null,

    @Column(name = "resistance")
    var resistance: String? = null,

    @Column(name = "description")
    var description: String? = null
)