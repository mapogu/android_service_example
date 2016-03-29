/**
 * Service Example
 *
 * Copyright (c) 2015 Marcin Pogorzelski, Embeddev AB
 *
 * Permission is hereby granted, free of charge,
 * to any person obtaining a copy of this software
 * and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom
 * the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package se.embeddev.mapo.myapplication.service;


import android.app.Service;
import android.content.Intent;
import android.os.*;
import android.util.Log;
import java.util.Vector;


/**
 * BaseService
 *
 * Base Service Class
 */
public abstract class BaseService extends Service
{
  /** Private Members **/
  private String                m_Tag;
  private Looper                m_Looper;
  private Handler               m_Handler;
  private Messenger             m_Messenger;
  private Vector<Messenger>     m_Subscribers;


  /** Public interface **/
  /**
   * OnSend
   *
   * Strategy pattern for sending indications to subscribers
   */
  public interface OnSend
  {
    /**
     * send
     *
     * Sends indication to subscriber
     *
     * @param messenger Reference to subscribers Messenger
     */
    void send(Messenger messenger);
  }


  /** Public methods **/
  /**
   * Constructor
   *
   * @param tag Log tag to be used
   */
  public BaseService(String tag)
  {
    m_Tag = tag;
    m_Subscribers = new Vector<>();
  }


  /**
   * onBind
   *
   * Implementation of Binder interface required for connecting to service
   *
   * @param intent Intent that started the service
   * @return returns the binder interface for this service
   */
  @Override
  public IBinder onBind(Intent intent)
  {
    Log.i( m_Tag, "onBind" );

    return m_Messenger.getBinder();
  }


  /**
   * onUnbind
   *
   * Called when all clients have disconnected from service
   *
   * @param intent Intent that was used to start the service
   * @return returns always false
   * @see Service
   */
  @Override
  public boolean onUnbind(Intent intent)
  {
    Log.i( m_Tag, "onUnbind" );
    stopSelf();
    return false;
  }


  /**
   * onCreate
   *
   * Called when service is created
   */
  @Override
  public void onCreate()
  {
    Log.i( m_Tag, "onCreate" );

    HandlerThread thread = new HandlerThread(m_Tag + "Thread",
                                             android.os.Process.THREAD_PRIORITY_BACKGROUND);
    thread.start();

    m_Looper    = thread.getLooper();
    m_Handler   = onCreate( m_Looper );
    m_Messenger = new Messenger( m_Handler );
  }


  /**
   * Called when service is destroyed
   */
  @Override
  public void onDestroy()
  {
    Log.i( m_Tag, "onDestroy" );
    m_Looper.quit();
  }


  /** Private Methods **/
  /**
   * Adds subscriber to service
   * @param subscriber Reference to Messenger object, where messages should be sent.
   * @return true if added, false if already subscribed.
   */
  protected boolean addSubscriber(Messenger subscriber)
  {
    for (Messenger item : m_Subscribers)
    {
      if (item.equals( subscriber ))
      {
        return false;
      }
    }
    m_Subscribers.add( subscriber );
    return true;
  }


  /**
   * removeSubscriber
   *
   * Removes subscriber from service
   *
   * @param subscriber subscriber that should be removed
   * @return true if removed, false if not present.
   */
  protected boolean removeSubscriber(Messenger subscriber)
  {
    return m_Subscribers.remove( subscriber );
  }


  /**
   * notifySubscribers
   *
   * Notifies all subscriber throught OnSend interface
   *
   * @param sender reference to OnSend object which is
   *               called for every subscriber
   */
  protected void notifySubscribers(OnSend sender)
  {
    for (Messenger subscriber : m_Subscribers)
    {
      sender.send( subscriber );
    }
  }


  /**
   * numberOfSubscribers
   *
   * @return number of subscribers in service
   */
  protected int numberOfSubscribers()
  {
    return m_Subscribers.size();
  }


  /**
   * onCreate
   *
   * Called when service is created for sub classes to provide
   * handler
   *
   * @param looper Thread in which handler should execute in.
   * @return Reference to Handler
   */
  protected abstract Handler onCreate(Looper looper);
}
