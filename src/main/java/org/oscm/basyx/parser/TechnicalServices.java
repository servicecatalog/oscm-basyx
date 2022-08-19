/*
 ******************************************************************************

 <p>Copyright FUJITSU LIMITED 2022

 <p>*****************************************************************************
*/
package org.oscm.basyx.parser;

import com.google.gson.Gson;
import org.oscm.basyx.oscmmodel.TechnicalServicesMapper;

import java.util.Optional;

/**
 * Author @goebel
 */
public class TechnicalServices {

    public static Optional<TechnicalServicesMapper> parseJson(String json) {
        try {
            TechnicalServicesMapper rs = new Gson().fromJson(json, TechnicalServicesMapper.class);
            return Optional.ofNullable(rs);
        } catch (Throwable e) {
            throw new IllegalStateException(json, e);
        }
    }
}
