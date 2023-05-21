package edu.hcmus.doc.mainservice.controller;

import edu.hcmus.doc.mainservice.DocURL;
import edu.hcmus.doc.mainservice.model.dto.ExtendRequestDto;
import edu.hcmus.doc.mainservice.model.enums.ExtendRequestStatus;
import edu.hcmus.doc.mainservice.service.ExtendRequestService;
import edu.hcmus.doc.mainservice.util.mapper.ExtensionRequestMapper;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(DocURL.API_V1 + "/extension-requests")
public class ExtendRequestController {

  private final ExtendRequestService extendRequestService;

  private final ExtensionRequestMapper extensionRequestMapper;

  @GetMapping("/{username}")
  public List<ExtendRequestDto> getCurrUserExtensionRequests(@PathVariable String username) {
    return extendRequestService.getExtensionRequestsByUsername(username)
        .stream()
        .map(extensionRequestMapper::toDto)
        .toList();
  }

  @PostMapping
  public Long createExtensionRequest(@RequestBody @Valid ExtendRequestDto extendRequestDto) {
    return extendRequestService.createExtensionRequest(
        extendRequestDto.getProcessingUserId(),
        extensionRequestMapper.toEntity(extendRequestDto));
  }

  @PutMapping("/{id}/{validatorId}/{status}")
  public Long validateExtensionRequest(
      @PathVariable Long id,
      @PathVariable Long validatorId,
      @PathVariable ExtendRequestStatus status) {
    return extendRequestService.validateExtensionRequest(id, validatorId, status);
  }
}
