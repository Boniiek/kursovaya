package com.whatislove.mydisk.controllers;


import com.whatislove.mydisk.dto.ElementDTO;
import com.whatislove.mydisk.dto.ElementListDTO;
import com.whatislove.mydisk.dto.ElementOutDTO;
import com.whatislove.mydisk.services.ElementsService;
import com.whatislove.mydisk.util.ElementListDTOValidator;
import com.whatislove.mydisk.util.ElementNotFoundException;
import com.whatislove.mydisk.util.ErrorResponse;
import com.whatislove.mydisk.util.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
public class ElementsController {

    private final ElementsService elementsService;


    private final ElementListDTOValidator elementListDTOValidator;


    @Autowired
    public ElementsController(ElementsService elementsService, ElementListDTOValidator elementListDTOValidator) {
        this.elementsService = elementsService;
        this.elementListDTOValidator = elementListDTOValidator;
    }

    @PostMapping("/imports")
    public ResponseEntity<HttpStatus> imports(@RequestBody @Valid ElementListDTO elements,
                                              BindingResult bindingResult) {

        elementListDTOValidator.validate(elements, bindingResult);

        if (bindingResult.hasErrors()) {
            throw new ValidationException("Validation Failed");
        }

        elementsService.imports(elements.getItems(), elements.getUpdateDate());

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") String id,
                                             @RequestParam
                                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME,
                                                     fallbackPatterns = {"yyyy-MM-dd'T'HH:mm:ssXXX"}) Date date) {
        elementsService.delete(id, date);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("nodes/{id}")
    public ResponseEntity<ElementOutDTO> elementInfo(@PathVariable("id") String id) {
        return new ResponseEntity<>(elementsService.getElementOutById(id), HttpStatus.OK);
    }

    @GetMapping("updates")
    public ResponseEntity<List<ElementDTO>> getUpdates(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME,
                    fallbackPatterns = {"yyyy-MM-dd'T'HH:mm:ssXXX"}) Date date) {
        return new ResponseEntity<>(elementsService.getLastUpdatedElements(date), HttpStatus.OK);
    }


    @GetMapping("node/{id}/history")
    public ResponseEntity<List<ElementDTO>> getNodeHistory(@PathVariable("id") String id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME,
                    fallbackPatterns = {"yyyy-MM-dd'T'HH:mm:ssXXX"}) Date dateStart,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME,
                    fallbackPatterns = {"yyyy-MM-dd'T'HH:mm:ssXXX"}) Date dateEnd) {

            return new ResponseEntity<>(elementsService.getElementHistory(id, dateStart, dateEnd), HttpStatus.OK);
    }


    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(ValidationException e) {
        ErrorResponse response = new ErrorResponse(400, e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(ElementNotFoundException e) {
        ErrorResponse response = new ErrorResponse(404, e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(HttpMessageNotReadableException e) {
        ErrorResponse response = new ErrorResponse(400, "Validation Failed");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(ConstraintViolationException e) {
        ErrorResponse response = new ErrorResponse(400, "Validation Failed");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
