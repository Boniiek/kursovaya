package com.whatislove.mydisk.controllers.elementsController.delete;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whatislove.mydisk.dto.ElementDTO;
import com.whatislove.mydisk.dto.ElementListDTO;
import com.whatislove.mydisk.models.Element;
import com.whatislove.mydisk.models.Type;
import com.whatislove.mydisk.services.ElementsService;
import org.junit.jupiter.api.Test;
import org.modelmapper.internal.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ConditionalOnProperty("test.run.integration")
@SpringBootTest
@AutoConfigureMockMvc
public class ElementsDeleteTests {

    /*private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    private final ElementsService elementsService;

    @Autowired
    public ElementsDeleteTests(MockMvc mockMvc, ObjectMapper objectMapper, ElementsService elementsService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.elementsService = elementsService;
    }

    @Test
    public void deleteTest() throws Exception {

        List<ElementDTO> elements = new ArrayList<>();

        ElementDTO firstElement = new ElementDTO();
        firstElement.setId("justForDelete");
        firstElement.setType(Type.FILE);
        firstElement.setSize(15);

        elements.add(firstElement);

        ElementListDTO elementListDTO = new ElementListDTO();
        elementListDTO.setItems(elements);
        elementListDTO.setUpdateDate(new Date());

        mockMvc.perform(post("/imports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(elementListDTO)))
                .andExpect(status().isOk());

        Assert.notNull(elementsService.findById("justForDelete").orElse(null));

        mockMvc.perform(delete("/delete/justForDelete")
                        .param("date", "2022-05-29T21:12:01.000Z"))
                .andExpect(status().isOk());

        Assert.isNull(elementsService.findById("justForDelete").orElse(null));
    }

*/
    /*@Test
    public void deleteChildTest() throws Exception {

        List<ElementDTO> elements = new ArrayList<>();

        ElementDTO firstElement = new ElementDTO();
        firstElement.setId("parentFolder");
        firstElement.setType(Type.FOLDER);
        firstElement.setSize(0);

        ElementDTO firstChild = new ElementDTO();
        firstElement.setId("firstChildToDelete");
        firstElement.setType(Type.FILE);
        firstElement.setSize(14);

        ElementDTO secondChild = new ElementDTO();
        secondChild.setId("secondChildToDelete");
        secondChild.setType(Type.FILE);
        secondChild.setSize(7);

        elements.add(firstElement);
        elements.add(firstChild);
        elements.add(secondChild);

        ElementListDTO elementListDTO = new ElementListDTO();
        elementListDTO.setItems(elements);
        elementListDTO.setUpdateDate(new Date());

        mockMvc.perform(post("/imports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(elementListDTO)))
                .andExpect(status().isOk());

        Element element = elementsService.findById("parentFolder").orElse(null);
        Assert.notNull(element);
        Assert.isNull(element.getSize() == 21);

        mockMvc.perform(delete("/delete/secondChildToDelete")
                        .param("date", "2022-05-29T21:12:01.000Z"))
                .andExpect(status().isOk());

        Assert.isNull(elementsService.findById("secondChildToDelete").orElse(null));
        element = elementsService.findById("parentFolder").orElse(null);
        Assert.notNull(element);
        Assert.isNull(element.getSize() == 14);
    }*/
}
