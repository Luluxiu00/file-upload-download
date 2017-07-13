package com.springmvc.mapper;

import com.springmvc.pojo.Person;

import java.util.List;

/**
 * Created by 326944 on 2017/7/12.
 */
public interface PersonMapper {
    List<Person> findAll();
}
