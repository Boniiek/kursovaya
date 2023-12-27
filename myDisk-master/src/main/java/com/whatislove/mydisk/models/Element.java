package com.whatislove.mydisk.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "element")
public class Element {

    @Id
    @Column(name = "id")
    @NotNull
    private String id;

    @Column(name = "url")
    private String url;

    @Column(name = "parent_id")
    private String parentId;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "date")
    private Date date;

    @Column(name = "size")
    private long size;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "type")
    private Type type;


    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "id", insertable=false, updatable=false)
    private Element parent;

    @OneToMany(mappedBy = "parent")
    private List<Element> children;


    public Element() {
    }

    public Element(String id, String url, String parentId, Date date, long size, Type type) {
        this.id = id;
        this.url = url;
        this.parentId = parentId;
        this.date = date;
        this.size = size;
        this.type = type;
    }

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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<Element> getChildren() {
        return children;
    }

    public void setChildren(List<Element> children) {
        this.children = children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Element element = (Element) o;

        if (!id.equals(element.id)) return false;
        if (!Objects.equals(url, element.url)) return false;
        return Objects.equals(parentId, element.parentId);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (parentId != null ? parentId.hashCode() : 0);
        return result;
    }



}
