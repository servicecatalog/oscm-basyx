/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2022                                           
 *                                                                                                                                 
 *  Creation Date: 03.08.2022                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.basyx.oscmmodel;

/**
 * @author goebel
 *
 */
public class ServiceParameter {
	String type;
	String defaultValue;
	String name;

	public ServiceParameter(String type, String defaultValue, String name) {
		this.type = type;
		this.defaultValue = defaultValue;
		this.name = name;
	}
}
