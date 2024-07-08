package org.shopping.site.admin.setting.country;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.shopping.entity.setting.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CountryRestControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired CountryRepository repo;

    @Test
    @WithMockUser(username = "khaganiam@code.edu.az", password = "xaqani123", roles = "ADMIN")
    public void testListCountries() throws Exception {
        String url = "/countries/list";

        MvcResult result = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        Country[] countries = objectMapper.readValue(jsonResponse, Country[].class);

        assertThat(countries).hasSizeGreaterThan(0);
    }

    @Test
    @WithMockUser(username = "khaganiam@code.edu.az", password = "xaqani123", roles = "ADMIN")
    public void testCreateCountry() throws JsonProcessingException, Exception {
        String url = "/countries/save";
        String countryName = "Germany";
        String countryCode = "DE";
        Country country = new Country(countryName, countryCode);

        MvcResult result = mockMvc.perform(post(url).contentType("application/json")
                        .content(objectMapper.writeValueAsString(country))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        Integer countryId = Integer.parseInt(response);

        Optional<Country> findById = repo.findById(countryId);
        assertThat(findById.isPresent());

        Country savedCountry = findById.get();

        assertThat(savedCountry.getName()).isEqualTo(countryName);
    }

    @Test
    @WithMockUser(username = "test@gmail.com", password = "123456789", roles = "ADMIN")
    public void testUpdateCountry() throws JsonProcessingException, Exception {
        String url = "/countries/save";

        Integer countryId = 7;
        String countryName = "Kenya";
        String countryCode = "CN";
        Country country = new Country(countryId, countryName, countryCode);

        mockMvc.perform(post(url).contentType("application/json")
                        .content(objectMapper.writeValueAsString(country))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(countryId)));

        Optional<Country> findById = repo.findById(countryId);
        assertThat(findById.isPresent());

        Country savedCountry = findById.get();

        assertThat(savedCountry.getName()).isEqualTo(countryName);
    }

    @Test
    @WithMockUser(username = "test@gmail.com", password = "1234234567", roles = "ADMIN")
    public void testDeleteCountry() throws Exception {
        Integer countryId = 7;
        String url = "/countries/delete/" + countryId;
        mockMvc.perform(get(url)).andExpect(status().isOk());

        Optional<Country> findById = repo.findById(countryId);

        assertThat(findById).isNotPresent();
    }
}
