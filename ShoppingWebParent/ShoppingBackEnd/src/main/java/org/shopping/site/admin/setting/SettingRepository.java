package org.shopping.site.admin.setting;

import org.shopping.entity.setting.Setting;
import org.shopping.entity.setting.SettingCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SettingRepository extends JpaRepository<Setting, String> {
    public List<Setting> findByCategory(SettingCategory category);
}
