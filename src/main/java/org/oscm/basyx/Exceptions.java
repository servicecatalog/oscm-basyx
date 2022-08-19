/*
 ******************************************************************************

 <p>Copyright FUJITSU LIMITED 2022

 <p>*****************************************************************************
*/

package org.oscm.basyx;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/** Author */
public class Exceptions {

  static NotFound NotFound(String message) {
    return fail(NotFound.class, message);
  }

  static AccessDenied AccessDenied(String message) {
    return fail(AccessDenied.class, message);
  }

  static InternalError InternalError(Throwable cause) {
    return fail(InternalError.class, cause, "Server Error");
  }

  static <T extends RuntimeException> ResponseEntity<String> asResponseEntity(T error) {
    try {
      Field field = error.getClass().getDeclaredField("HTTP_STATUS");
      field.setAccessible(true);

      return new ResponseEntity<>(
          String.format("{\"errorMsg\":\"%s\"}", error.getMessage()),
          new HttpHeaders(),
          (HttpStatus) field.get(error));
    } catch (IllegalAccessException | NoSuchFieldException e) {
      e.printStackTrace();
    }
    return new ResponseEntity<>(
        error.getMessage(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  static class NotFound extends RuntimeException {
    NotFound(String msg) {
      super(msg);
    }

    HttpStatus HTTP_STATUS = HttpStatus.NOT_FOUND;
  }

  static class AccessDenied extends RuntimeException {
    AccessDenied(String msg) {
      super(msg);
    }

    HttpStatus HTTP_STATUS = HttpStatus.UNAUTHORIZED;
  }

  static class InternalError extends RuntimeException {
    InternalError(String msg, Throwable cause) {
      super(msg, cause);
    }

    HttpStatus HTTP_STATUS = HttpStatus.INTERNAL_SERVER_ERROR;
  }

  static <T extends RuntimeException> T fail(Class<T> clazz, String msg) {
    try {
      return clazz.getDeclaredConstructor(String.class).newInstance(msg);
    } catch (NoSuchMethodException
        | InvocationTargetException
        | InstantiationException
        | IllegalAccessException e) {
      throw new RuntimeException(msg, e);
    }
  }

  static <T extends RuntimeException> T fail(Class<T> clazz, Throwable t, String msg) {
    try {
      return clazz.getDeclaredConstructor(String.class, Throwable.class).newInstance(msg, t);
    } catch (NoSuchMethodException
        | InvocationTargetException
        | InstantiationException
        | IllegalAccessException e) {
      throw new RuntimeException(msg, e);
    }
  }
}
