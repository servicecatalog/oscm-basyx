/*
 ******************************************************************************

 <p>Copyright FUJITSU LIMITED 2022

 <p>*****************************************************************************
*/
package org.oscm.basyx;

/** Author @goebel */
class TokenHolder {
  private static final ThreadLocal<String> store = new ThreadLocal<>();

  static String get() {
    return store.get();
  }

  static void set(String token) {
    store.set(token);
  }
}
