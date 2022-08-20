package com.devsuperio.dscatalog.resources;

import com.devsuperio.dscatalog.dto.ProductDTO;
import com.devsuperio.dscatalog.factory.Factory;
import com.devsuperio.dscatalog.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductResourcesTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private long existingId;
    private long noExistingId;
    private long dependentId;
    private ProductDTO productDTO;
    private PageImpl<ProductDTO> pageDTO;
    private List<ProductDTO> productListDTO;
    private Pageable pageable;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        noExistingId = 999L;
        dependentId = 4L;
        productDTO = Factory.createProductDTO();
        pageable = PageRequest.of(0, 10);
        pageDTO = new PageImpl<>(List.of(productDTO));
        productListDTO = new ArrayList<>(List.of(productDTO));

        Mockito.when(productService.findAll()).thenReturn(productListDTO);
        Mockito.when(productService.findAllPaged(any())).thenReturn(pageDTO);

    }

    @Test
    public void findAllShouldReturnListOfProductDTO() throws Exception {
        ResultActions result =
                mockMvc.perform(get("/products/paged")
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
    }

    @Test
    public void findAllPagedShouldReturnPageOfProductDTO() throws Exception {
        ResultActions result =
                mockMvc.perform(get("/products/paged")
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
    }

}
