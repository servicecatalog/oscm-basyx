/*
 ******************************************************************************

 <p>Copyright FUJITSU LIMITED 2022

 <p>*****************************************************************************
*/
package org.oscm.basyx.parser;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.basyx.submodel.metamodel.api.ISubmodel;
import org.eclipse.basyx.submodel.metamodel.api.identifier.IdentifierType;
import org.eclipse.basyx.submodel.metamodel.api.qualifier.haskind.ModelingKind;
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

    // when
    Optional<List<ServiceParameterBasyx>> params = NameplateBasyx.parseProperties(nameplate);

    // then
    assertTrue(params.isPresent());
    assertTrue(contains(params.get(), "ManufactName"), "Property not found ");
    assertFalse(
        contains(params.get(), "SerialNumber"),
        "Only Mandatory Properties are added to the list of ServiceParameters.");
  }

  ISubmodel givenNameplate() {
    String smIdShort = "Nameplate";
    Identifier smIdentifier = new Identifier(IdentifierType.IRI, smIdShort);

    Submodel submodel = new Submodel(smIdShort, smIdentifier);

    Property manufactNameProp = new Property();
    manufactNameProp.setIdShort("ManufactName");
    manufactNameProp.setValue("Bob Manufacturer");
    manufactNameProp.setKind(ModelingKind.INSTANCE);
    manufactNameProp.setCategory("PARAMETER");

    Property serialNumberProp = new Property();
    serialNumberProp.setIdShort("SerialNumber");
    serialNumberProp.setValue("123");
    serialNumberProp.setKind(ModelingKind.INSTANCE);
    serialNumberProp.setCategory("PARAMETER");

    submodel.addSubmodelElement(manufactNameProp);
    submodel.addSubmodelElement(serialNumberProp);
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
