package com.whatislove.mydisk.dto;


import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

public class ElementListDTO {

    @NotEmpty
    @Valid
    private List<ElementDTO> items;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date updateDate;

    public List<ElementDTO> getItems() {
        return items;
    }

    public void setItems(List<ElementDTO> items) {
        this.items = items;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
