package com.whatislove.mydisk.repositories;

import com.whatislove.mydisk.models.Element;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ElementsRepository extends JpaRepository<Element, String> {

    List<Element> findAllByParentId(String parentId);

    List<Element> findAllByDateBetween(Date begin, Date end);
}
