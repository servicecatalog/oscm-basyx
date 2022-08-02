/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2022                                           
 *                                                                                                                                 
 *  Creation Date: 29.07.2022                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.basyx.model;

/**
 * @author goebel
 *
 */
public class SubmodelElement {
    public String idShort;
    public String category;
    public Object value;
    public String valueType;
    public String kind;

    public ModelType modelType;
    public Identification identification;
    public SemanticId semanticId;
}
