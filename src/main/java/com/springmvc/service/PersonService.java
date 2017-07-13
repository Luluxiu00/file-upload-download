package com.springmvc.service;

import com.springmvc.mapper.PersonMapper;
import com.springmvc.pojo.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 326944 on 2017/7/12.
 */
@Service
public class PersonService {

    @Autowired
    PersonMapper personMapper;

    public List<Person> findAll() {

        return personMapper.findAll();
    }
}
