package de.tucottbus.kl.csl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Logg {
  private static final Logger LOG = LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[2].getClassName());
  
  private Logg(){
  }
  
  /**
   * 
   * @param src
   * @param message
   */
  public static void msg(String src, String message) {
    LOG.info("["+src+"] " + message);
  }
  
  /**
   * 
   * @param src
   * @param message
   */
  public static void warn(String src, String message) {
    LOG.warn("["+src+"] " + message);
  }
  
  /**
   * 
   * @param src
   * @param message
   */
  public static void err(String src, String message) {
    LOG.error("["+src+"] " + message);    
  }
  
  /**
   * 
   * @param src
   * @param message
   */
  public static void debug(String src, String message) {
    LOG.debug("["+src+"] " + message); 
  }    

}
