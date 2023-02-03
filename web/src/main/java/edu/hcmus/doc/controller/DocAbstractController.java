package edu.hcmus.doc.controller;

import edu.hcmus.doc.util.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class DocAbstractController {

  @Autowired
  @Qualifier("userMapperDecoratorImpl")
  protected UserMapper userMapper;
}
