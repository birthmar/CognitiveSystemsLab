package de.tucottbus.kt.csl.core.hardware;

import java.util.Observable;

/**
 * Base class of hardware wrappers. The general contract of a hardware wrapper
 * is to connect to the hardware on instantiation and to maintain this
 * connection until {@link #dispose()} is invoked.
 * 
 * @author Matthias Wolff
 */
public abstract class Hardware extends Observable
{

  // -- Abstract API --
  
  /**
   * Disposes of this hardware item. Derived classes must free all system 
   * resources allocated by the wrapper.
   * 
   * @throws IllegalStateException if the wrapper is disposed.
   */
  public abstract void dispose();

  /**
   * Determines whether this hardware wrapper is disposed, normally if all 
   * communication channels with the hardware have been closed and released.
   */
  public abstract boolean isDisposed();
  
  /**
   * Returns the unique, human readable name of this hardware item.
   */
  public abstract String getName();

  /**
   * Returns <code>true</code> if this hardware item is connected,
   * <code>false</code> otherwise.
   * 
   * @throws IllegalStateException if the wrapper is disposed.
   */
  public abstract boolean isConnected();

  // -- Worker methods --

  /**
   * Checks this hardware wrapper and throws an {@link IllegalStateException} if
   * the wrapper is irrecoverably not functional.
   * 
   * @throws IllegalStateException if the wrapper is disposed. Derived classes
   * may add additional checks. The exception message should contain a 
   * human-readable hint.
   */
  protected void check() throws IllegalStateException
  {
    if (isDisposed())
      throw new IllegalStateException("Wrapper is disposed");
  }
}
