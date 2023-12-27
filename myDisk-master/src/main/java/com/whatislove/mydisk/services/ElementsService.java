package com.whatislove.mydisk.services;

import com.whatislove.mydisk.dto.ElementDTO;
import com.whatislove.mydisk.dto.ElementOutDTO;
import com.whatislove.mydisk.models.Element;
import com.whatislove.mydisk.models.Type;
import com.whatislove.mydisk.repositories.ElementsRepository;
import com.whatislove.mydisk.util.ElementNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ElementsService {

    private final ElementsRepository elementsRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ElementsService(ElementsRepository elementsRepository, ModelMapper modelMapper) {
        this.elementsRepository = elementsRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public void imports(List<ElementDTO> elementList, Date date) {
        List resultList = elementList.stream().map(el -> convertToElement(el, date)).collect(Collectors.toList());

        elementsRepository.saveAll(resultList);

        updateFoldersParameters(resultList, false);
    }


    @Async
    public void updateFoldersParameters(List<Element> elements, boolean toRemove) {
        List<Element> files = elements.stream().filter(el -> el.getType().equals(Type.FILE))
                .collect(Collectors.toList());

        for (Element file : files) {
            Element cur = file;
            while (cur.getParentId() != null) {
                cur = elementsRepository.findById(cur.getParentId()).orElse(null);
                if (cur != null) {
                    if (toRemove) cur.setSize(cur.getSize() - file.getSize());
                    else cur.setSize(cur.getSize() + file.getSize());
                    cur.setDate(file.getDate());
                }
            }
        }
    }

    public ElementOutDTO getElementOutById(String id) {
        Element element = elementsRepository.findById(id).orElse(null);

        if (element == null)
            throw new ElementNotFoundException("Item not found");

        return modelMapper.map(element, ElementOutDTO.class);

    }

    public Optional<Element> findById(String id) {
        return elementsRepository.findById(id);
    }

    public List<ElementDTO> getLastUpdatedElements(Date date) {
        Date endDate = new Date(date.getTime() - 24 * 60 * 60 * 1000);
        return elementsRepository.findAllByDateBetween(date, endDate)
                .stream().map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ElementDTO> getElementHistory(String id, Date start, Date end) {
        Element element = elementsRepository.findById(id).orElse(null);

        if (element == null)
            throw new ElementNotFoundException("Item not found");

        start.setTime(start.getTime() - 1);

        List<Element> children = new ArrayList<>();

        getAllChildren(children, element);

        return children.stream().filter(el -> el.getDate().after(start) && el.getDate().before(end))
                .map(el -> convertToDTO(el)).collect(Collectors.toList());
    }

    @Transactional
    public void delete(String id, Date date) {
        Element element = elementsRepository.findById(id).orElse(null);

        if (element == null)
            throw new ElementNotFoundException("Item not found");

        updateFoldersParameters(Collections.singletonList(element), true);

        List<Element> children = new ArrayList<>();
        children.add(element);
        getAllChildren(children, element);

        elementsRepository.deleteAllInBatch(children);
    }

    private void getAllChildren(List<Element> children, Element father) {

        List<Element> curChildren = elementsRepository.findAllByParentId(father.getId());

        children.addAll(curChildren);

        for (Element child : curChildren)
            getAllChildren(children, child);

    }

    private Element convertToElement(ElementDTO elementDTO, Date date) {
        return enrichElement(modelMapper.map(elementDTO, Element.class), date);
    }

    private ElementDTO convertToDTO(Element element) {
        return modelMapper.map(element, ElementDTO.class);
    }

    private Element enrichElement(Element element, Date date) {
        element.setDate(date);
        return element;
    }
}
