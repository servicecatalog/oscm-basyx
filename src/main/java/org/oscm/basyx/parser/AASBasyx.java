/*
 ******************************************************************************

 <p>Copyright FUJITSU LIMITED 2022

 <p>*****************************************************************************
*/

package org.oscm.basyx.parser;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import org.eclipse.basyx.aas.manager.ConnectedAssetAdministrationShellManager;
import org.eclipse.basyx.aas.metamodel.connected.ConnectedAssetAdministrationShell;
import org.eclipse.basyx.aas.registration.api.IAASRegistry;
import org.eclipse.basyx.aas.registration.proxy.AASRegistryProxy;
import org.eclipse.basyx.submodel.metamodel.api.ISubmodel;
import org.eclipse.basyx.submodel.metamodel.api.identifier.IIdentifier;
import org.eclipse.basyx.submodel.metamodel.api.submodelelement.dataelement.IProperty;
import org.eclipse.basyx.vab.protocol.api.IConnectorFactory;
import org.eclipse.basyx.vab.protocol.http.connector.HTTPConnectorFactory;

/** @author farmaki */
public class AASBasyx {

  public static Optional<ISubmodel> getNameplate(String registryUrl, IIdentifier aasIdentifier)
      throws IOException {

    IConnectorFactory connectorFactory = new HTTPConnectorFactory();

    ISubmodel digitalNameplateSM = null;
    IAASRegistry registry = new AASRegistryProxy(registryUrl);

    ConnectedAssetAdministrationShellManager manager =
        new ConnectedAssetAdministrationShellManager(registry, connectorFactory);

    ConnectedAssetAdministrationShell connectedAAS = manager.retrieveAAS(aasIdentifier);

    Map<String, ISubmodel> submodelsMap = connectedAAS.getSubmodels();

    for (ISubmodel sm : submodelsMap.values()) {
      if ("Nameplate".equals(sm.getIdShort())) {
        digitalNameplateSM = sm;
      }
    }

    return Optional.ofNullable(digitalNameplateSM);
  }

  public static Optional<String> readPropertyFromModel(
      ISubmodel nameplateSubmodel, String propKey) {

    Map<String, IProperty> mapProperties = nameplateSubmodel.getProperties();

    IProperty manufacturerProdNameProp = mapProperties.get(propKey);

    String manufacturerProductName = (String) manufacturerProdNameProp.getValue();
    return Optional.ofNullable(manufacturerProductName);
  }
}
