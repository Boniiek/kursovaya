package com.whatislove.mydisk.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.whatislove.mydisk.models.Type;

import java.util.Date;
import java.util.List;

public class ElementOutDTO {
    private String id;

    private String url;

    private Type type;

    private String parentId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private Date date;

    private long size;

    //@JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ElementOutDTO> children;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public List<ElementOutDTO> getChildren() {
        return children;
    }

    public void setChildren(List<ElementOutDTO> children) {
        if (children.isEmpty())
            this.children = null;
        else
            this.children = children;
    }
}
