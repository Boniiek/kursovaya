package com.whatislove.mydisk.util;

import com.whatislove.mydisk.dto.ElementDTO;
import com.whatislove.mydisk.dto.ElementListDTO;
import com.whatislove.mydisk.models.Element;
import com.whatislove.mydisk.models.Type;
import com.whatislove.mydisk.services.ElementsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ElementListDTOValidator implements Validator {
    private final ElementsService elementsService;

    @Autowired
    public ElementListDTOValidator(ElementsService elementsService) {
        this.elementsService = elementsService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return ElementListDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ElementListDTO elementListDTO = (ElementListDTO) o;

        var elements = elementListDTO.getItems();

        HashSet elementSet = new HashSet(elements.stream().map(el -> el.getId())
                .collect(Collectors.toList()));

        if (elementSet.size() < elements.size())
            errors.rejectValue("items", "duplicate ids");
        List<ElementDTO> folders;
        List<ElementDTO> files;

        try {
             folders = elements.stream().filter(el -> el.getType().equals(Type.FOLDER))
                    .collect(Collectors.toList());

             files = elements.stream().filter(el -> el.getType().equals(Type.FILE))
                    .collect(Collectors.toList());
        }
        catch (NullPointerException e){
            throw new ValidationException("Validation Failed");
        }

        for(ElementDTO folder : folders){
            if (folder.getId() == null)
                throw new ValidationException("Validation Failed");

            Element element = elementsService.findById(folder.getId()).orElse(null);
            if (element != null){
                if (element.getType().equals(Type.FILE))
                    errors.rejectValue("items", "Changing item's type is impossible");
            }

            if (folder.getSize() > 0)
                errors.rejectValue("items", "Folder's size should be zero");

            if (folder.getUrl() != null)
                errors.rejectValue("items", "Folder's url should be null");

            if (folder.getParentId() != null) {
                Element parent = elementsService.findById(folder.getParentId()).orElse(null);

                if (parent != null) {
                    if (parent.getType().equals(Type.FILE))
                        errors.rejectValue("items", "Parent should be folder");
                }
                else{
                    ElementDTO parentEl = folders.stream().filter(el -> el.getId().equals(folder.getParentId()))
                            .findFirst().orElse(null);

                    if (parentEl == null)
                        errors.rejectValue("items", "Parent isn't found");
                    else if (parentEl.getType().equals(Type.FILE))
                        errors.rejectValue("items", "Parent should be folder");
                }

                if (folder.getUrl() != null)
                    errors.rejectValue("items", "Folder url should be null");

                if (folder.getSize() != 0)
                    errors.rejectValue("items", "Folder size should be equals zero");
            }
        }

        for(ElementDTO file : files){
            if (file.getId() == null)
                throw new ValidationException("Validation Failed");

            Element element = elementsService.findById(file.getId()).orElse(null);
            if (element != null){
                if (element.getType().equals(Type.FOLDER))
                    errors.rejectValue("items", "Changing item's type is impossible");
            }

            if (file.getSize() <= 0)
                errors.rejectValue("items", "File's size should be more than zero");

            if (file.getParentId() != null) {
                Element parent = elementsService.findById(file.getParentId()).orElse(null);

                if (parent != null) {
                    if (parent.getType().equals(Type.FILE))
                        errors.rejectValue("items", "Parent should be folder");
                }
                else{
                    ElementDTO parentEl = folders.stream().filter(el -> el.getId().equals(file.getParentId()))
                            .findFirst().orElse(null);
                    if (parentEl == null)
                        errors.rejectValue("items", "Parent isn't found");
                    else if (parentEl.getType().equals(Type.FILE))
                        errors.rejectValue("items", "Parent should be folder");
                }

                if (file.getUrl() != null) {
                    if (file.getUrl().length() > 255)
                        errors.rejectValue("items", "Url size should be less than 255");
                }

                if (file.getSize() == 0)
                    errors.rejectValue("items", "File size should be more than zero");

            }
        }

    }
}
