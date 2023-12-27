package com.whatislove.mydisk.controllers.elementsController.imports;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whatislove.mydisk.dto.ElementDTO;
import com.whatislove.mydisk.dto.ElementListDTO;
import com.whatislove.mydisk.models.Element;
import com.whatislove.mydisk.models.Type;
import com.whatislove.mydisk.services.ElementsService;
import com.whatislove.mydisk.util.ErrorResponse;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ConditionalOnProperty("test.run.integration")
@SpringBootTest
@AutoConfigureMockMvc
public class ElementsImportsTests {

    /*private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    private final ElementsService elementsService;

    @Autowired
    public ElementsImportsTests(MockMvc mockMvc, ObjectMapper objectMapper, ElementsService elementsService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.elementsService = elementsService;
    }

    @Test
    public void importsTest() throws Exception {
        mockMvc.perform(post("/imports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "        \"items\": [" +
                                "            {" +
                                "                \"type\": \"FOLDER\"," +
                                "                \"id\": \"069cb8d7-bbdd-47d3-ad8f-82ef4c269df1\"," +
                                "                \"parentId\": null" +
                                "            }" +
                                "        ]," +
                                "        \"updateDate\": \"2022-02-01T12:00:00Z\"" +
                                "    }"))
                .andExpect(status().isOk());

        Element element = elementsService.findById("069cb8d7-bbdd-47d3-ad8f-82ef4c269df1").orElse(null);
        Assert.notNull(element);
    }

    @Test
    public void importsWithDuplicatesIdsTest() throws Exception {
        List<ElementDTO> elements = new ArrayList<>();

        ElementDTO firstElement = new ElementDTO();
        firstElement.setId("abc");
        firstElement.setType(Type.FILE);
        firstElement.setSize(15);

        ElementDTO secondElement = new ElementDTO();
        secondElement.setId("abc2");
        secondElement.setType(Type.FILE);
        secondElement.setSize(15);

        elements.add(firstElement);
        elements.add(secondElement);

        ElementListDTO elementListDTO = new ElementListDTO();
        elementListDTO.setItems(elements);
        elementListDTO.setUpdateDate(new Date());

        mockMvc.perform(post("/imports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(elementListDTO)))
                .andExpect(status().isOk());

        elements.get(1).setId("abc");

        mockMvc.perform(post("/imports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(elementListDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper
                        .writeValueAsString(new ErrorResponse(400, "Validation Failed"))));
    }

    @Test
    public void importsWithChangingType() throws Exception {
        ElementDTO firstElement = new ElementDTO();
        firstElement.setId("abcd");
        firstElement.setType(Type.FILE);
        firstElement.setSize(15);

        ElementListDTO elementListDTO = new ElementListDTO();
        elementListDTO.setItems(new ArrayList<>());
        elementListDTO.getItems().add(firstElement);
        elementListDTO.setUpdateDate(new Date());

        mockMvc.perform(post("/imports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(elementListDTO)))
                .andExpect(status().isOk());

        elementListDTO.getItems().get(0).setType(Type.FOLDER);

        mockMvc.perform(post("/imports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(elementListDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper
                        .writeValueAsString(new ErrorResponse(400, "Validation Failed"))));
    }

    @Test
    public void importsWithValidParent() throws Exception {
        ElementDTO firstElement = new ElementDTO();
        firstElement.setId("abcde");
        firstElement.setType(Type.FOLDER);
        firstElement.setSize(0);

        ElementDTO secondElement = new ElementDTO();
        secondElement.setId("abcde2");
        secondElement.setType(Type.FILE);
        secondElement.setSize(13);
        secondElement.setParentId("abcde");

        List<ElementDTO> elements = new ArrayList<>();

        elements.add(firstElement);
        elements.add(secondElement);

        ElementListDTO elementListDTO = new ElementListDTO();
        elementListDTO.setItems(elements);
        elementListDTO.setUpdateDate(new Date());

        mockMvc.perform(post("/imports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(elementListDTO)))
                .andExpect(status().isOk());

        Element element = elementsService.findById("abcde2").orElse(null);

        Assert.notNull(element);

        Element parent = elementsService.findById(element.getParentId()).orElse(null);

        Assert.notNull(element);
    }

    @Test
    public void importsWithInvalidParent() throws Exception {
        ElementDTO firstElement = new ElementDTO();
        firstElement.setId("abcdef");
        firstElement.setType(Type.FILE);
        firstElement.setSize(0);

        ElementDTO secondElement = new ElementDTO();
        secondElement.setId("abcdef2");
        secondElement.setType(Type.FILE);
        secondElement.setSize(13);
        secondElement.setParentId("abcdef");

        List<ElementDTO> elements = new ArrayList<>();

        elements.add(firstElement);
        elements.add(secondElement);

        ElementListDTO elementListDTO = new ElementListDTO();
        elementListDTO.setItems(elements);
        elementListDTO.setUpdateDate(new Date());

        mockMvc.perform(post("/imports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(elementListDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper
                        .writeValueAsString(new ErrorResponse(400, "Validation Failed"))));
    }

    @Test
    public void importsFolderWithFiles() throws Exception {
        ElementDTO firstElement = new ElementDTO();
        firstElement.setId("justFolder");
        firstElement.setType(Type.FOLDER);
        firstElement.setSize(0);

        ElementDTO secondElement = new ElementDTO();
        secondElement.setId("fileSize5");
        secondElement.setType(Type.FILE);
        secondElement.setSize(5);
        secondElement.setParentId("justFolder");

        List<ElementDTO> elements = new ArrayList<>();

        elements.add(firstElement);
        elements.add(secondElement);

        ElementListDTO elementListDTO = new ElementListDTO();
        elementListDTO.setItems(elements);
        elementListDTO.setUpdateDate(new Date());

        mockMvc.perform(post("/imports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(elementListDTO)))
                .andExpect(status().isOk());

        Element element = elementsService.findById("justFolder").orElse(null);

        Assert.notNull(element);
        Assert.isTrue(element.getSize() == 5);

        elements.remove(0);
        elements.get(0).setId("fileSize34");
        elements.get(0).setSize(34);

        mockMvc.perform(post("/imports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(elementListDTO)))
                .andExpect(status().isOk());

        element = elementsService.findById("justFolder").orElse(null);

        Assert.notNull(element);
        Assert.isTrue(element.getSize() == 39);
    }
*/
}
