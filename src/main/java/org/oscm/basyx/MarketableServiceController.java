/**
 * *****************************************************************************
 *
 * <p>Copyright FUJITSU LIMITED 2022
 *
 * <p>Creation Date: 12.10.2022
 *
 * <p>*****************************************************************************
 */
package org.oscm.basyx;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/** @author farmaki */
public class MarketableServiceController {

  @PostMapping(
      value = "/service",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<String> createMarketableService(@RequestBody String newServiceAsJson) {

    //    User user = userService.save(newUser);
    //    if (user == null) {
    //      throw new ServerException();
    //    } else {
    //      return new ResponseEntity<>(user, HttpStatus.CREATED);
    //    }

    return ResponseEntity.ok().body("Done.");
  }

  // MediaType.APPLICATION_PROBLEM_JSON_VALUE
  @PostMapping(
      value = "/service/pricemodel",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<String> createPriceModel(@RequestBody String newPriceModelAsJson) {
    return ResponseEntity.ok().body("Done.");
  }
}
