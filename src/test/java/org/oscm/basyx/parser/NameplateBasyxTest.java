/*
 ******************************************************************************

 <p>Copyright FUJITSU LIMITED 2022

 <p>*****************************************************************************
*/
package org.oscm.basyx.parser;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.basyx.submodel.metamodel.api.ISubmodel;
import org.eclipse.basyx.submodel.metamodel.api.identifier.IdentifierType;
import org.eclipse.basyx.submodel.metamodel.map.Submodel;
import org.eclipse.basyx.submodel.metamodel.map.identifier.Identifier;
import org.eclipse.basyx.submodel.metamodel.map.submodelelement.dataelement.property.Property;
import org.junit.jupiter.api.Test;
import org.oscm.basyx.oscmmodel.ServiceParameterBasyx;

public class NameplateBasyxTest {

  @Test
  public void parseProperties() {
    // given
    ISubmodel nameplate = givenNameplate();
    Optional<List<ServiceParameterBasyx>> params = NameplateBasyx.parseProperties(nameplate);

    assertTrue(params.isPresent());
    assertTrue(contains(params.get(), "ManufactName"), "Property not found ");
  }

  ISubmodel givenNameplate() {
    String smIdShort = "Nameplate";
    Identifier smIdentifier = new Identifier(IdentifierType.IRI, smIdShort);

    Submodel submodel = new Submodel(smIdShort, smIdentifier);

    Property manufactNameProp = new Property();
    manufactNameProp.setIdShort("ManufactName");
    manufactNameProp.setValue("Bob Manufacturer");

    submodel.addSubmodelElement(manufactNameProp);
    return submodel;
  }

  boolean contains(List<ServiceParameterBasyx> l, String name) {
    List<ServiceParameterBasyx> sps =
        l.stream().filter(p -> p.name.equals(name)).collect(Collectors.toList());
    if (!sps.isEmpty()) {
      return "String".equalsIgnoreCase(sps.get(0).type);
    }
    return false;
  }
}
