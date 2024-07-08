package org.shopping.site.admin.setting.state;

import org.shopping.entity.setting.Country;
import org.shopping.entity.setting.State;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StateRepository extends CrudRepository<State, Integer> {

    public List<State> findByCountryOrderByNameAsc(Country country);
}