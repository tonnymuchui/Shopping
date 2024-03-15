package org.shopping.site.admin.category;

import org.junit.jupiter.api.Test;
import org.shopping.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void createRootCategoryTest(){
        Category category = new Category("Electronics");
        Category savedCategory = categoryRepository.save(category);
        assertThat(savedCategory.getId()).isGreaterThan(0);
    }
    @Test
    public void createSubCategoryTest(){
        Category parent = new Category(1);
        Category subCategory = new Category("Desktop",parent);
        Category saveSubCategory = categoryRepository.save(subCategory);
        assertThat(saveSubCategory.getId()).isGreaterThan(0);
    }
    @Test
    public void getCategoryTest(){
        Category category = categoryRepository.findById(1).get();
        Set<Category> children = category.getChildren();
        for (Category subCategory: children){
            System.out.println(subCategory.getName());
        }
        assertThat(children.size()).isGreaterThan(0);
    }
}
