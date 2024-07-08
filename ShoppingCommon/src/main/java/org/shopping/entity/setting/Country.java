package org.shopping.entity.setting;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.shopping.entity.IdBasedEntity;

import java.util.Set;

@Entity
@Table(name = "countries")
public class Country extends IdBasedEntity {

    @Setter
    @Getter
    @Column(nullable = false, length = 45)
    private String name;

    @Setter
    @Getter
    @Column(nullable = false, length = 5)
    private String code;

    @OneToMany(mappedBy = "country")
    private Set<State> states;

    public Country() {

    }

    public Country(Integer id) {
        this.id = id;
    }

    public Country(Integer id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }


    public Country(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public Country(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Country [id=" + id + ", name=" + name + ", code=" + code + "]";
    }

}