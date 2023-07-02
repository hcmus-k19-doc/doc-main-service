package edu.hcmus.doc.mainservice.service.impl;

import edu.hcmus.doc.mainservice.model.entity.ProcessingMethod;
import edu.hcmus.doc.mainservice.model.exception.ProcessingMethodNotFoundException;
import edu.hcmus.doc.mainservice.repository.ProcessingMethodRepository;
import edu.hcmus.doc.mainservice.service.ProcessingMethodService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = Throwable.class)
public class ProcessingMethodServiceImpl implements ProcessingMethodService {

  private final ProcessingMethodRepository processingMethodRepository;

  @Override
  public List<ProcessingMethod> findAll() {
    return processingMethodRepository.findAll();
  }

  @Override
  public ProcessingMethod findByName(String name){
    if(!StringUtils.isBlank(name)) {
      return processingMethodRepository.findProcessingMethodByName(name).orElseThrow(
          ProcessingMethodNotFoundException::new);
    } else return null;
  }


}
