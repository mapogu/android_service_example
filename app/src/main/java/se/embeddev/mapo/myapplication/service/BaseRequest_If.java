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


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;


/**
 * BaseRequest_If
 *
 * Service Request Interface base class
 * Handles service connection and provides
 * signal send interface
 */
public abstract class BaseRequest_If implements ServiceConnection
{
  /** Private Members **/
  private   String    m_Tag;
  protected boolean   m_ServiceRequested;
  protected boolean   m_ServiceBound;
  protected Messenger m_RequestMessenger;


  /** Public Methods **/
  /**
   * Constructor
   *
   * @param tag Log tag to use
   */
  public BaseRequest_If(String tag)
  {
    m_Tag = tag;
    m_ServiceRequested = false;
    m_ServiceBound = false;
    m_RequestMessenger = null;
  }


  /**
   * onServiceConnected
   *
   * Called when service is up and running
   *
   * @param className The concrete component name of the service that has
   * been connected
   * @param service The IBinder of the Service's communication channel,
   * which you can now make calls on
   */
  @Override
  public void onServiceConnected(ComponentName className, IBinder service)
  {
    Log.i( m_Tag, "onServiceConnected" );
    m_RequestMessenger = new Messenger( service );
    m_ServiceBound = true;

    onServiceConnected();
  }


  /**
   * onServiceDisconnected
   *
   * Called when service is disconnected
   *
   * @param className The concrete component name of the service that has
   * been connected.
   */
  @Override
  public void onServiceDisconnected(ComponentName className)
  {
    Log.i( m_Tag, "onServiceDisconnected" );
    m_RequestMessenger = null;
    m_ServiceBound     = false;
    m_ServiceRequested = false;

    onServiceDisconnected();
  }


  /** Private Methods **/
  /**
   * connectToService
   *
   * Connects to service if possible
   *
   * @param context reference to Context
   * @param cls  Class name of service
   * @return Result_Ok_en if successful, error code otherwise
   */
  protected Result_e connectToService(Context context, Class<?> cls)
  {
    if (m_ServiceRequested && m_ServiceBound)
    {
      Log.w( m_Tag, "Service already started" );
      return Result_e.Result_Error_Request_Failed_en;
    }

    if (m_ServiceRequested || m_ServiceBound)
    {
      Log.w( m_Tag, "Service start in progress" );
      return Result_e.Result_Error_Request_Failed_en;
    }

    m_ServiceRequested = true;
    context.bindService( new Intent( context, cls ), this, Context.BIND_AUTO_CREATE );
    return Result_e.Result_Ok_en;
  }


  /**
   * disconnectFromService
   *
   * Disconnects from service
   *
   * @param context reference to Context
   * @return Result_Ok_en of successful, error code otherwise
   */
  protected Result_e disconnectFromService(Context context)
  {
    if (m_ServiceBound && m_ServiceRequested)
    {
      context.unbindService( this );
      m_ServiceBound     = false;
      m_ServiceRequested = false;
      m_RequestMessenger = null;

      onServiceDisconnected();
      return Result_e.Result_Ok_en;
    }

    Log.w( m_Tag, "Service not started" );
    return Result_e.Result_Error_Request_Failed_en;
  }


  /**
   * sendToServiceIfPossible
   *
   * Sends message to service if connected
   *
   * @param signalId integer specifying request signal
   * @param bundle reference to Bundle (optional) containing request data
   * @param responseMessenger reference to Messenger for response
   * @return returns Result_Ok_en if sent, error code otherwise
   */
  protected Result_e sendToServiceIfPossible(int signalId, Bundle bundle, Messenger responseMessenger)
  {
    /** Error Codes **/
    if (!m_ServiceRequested || !m_ServiceBound)
    {
      Log.e( m_Tag, "Service is not started or start is in progress" );
      return Result_e.Result_Error_Request_Failed_en;
    }

    Message message = Message.obtain(null, signalId);
    message.setData( bundle );
    message.replyTo = responseMessenger;

    try
    {
      m_RequestMessenger.send( message );
      Log.d( m_Tag, "Client -> Service: signalId=" + signalId );
    }
    catch ( RemoteException e )
    {
      Log.w( m_Tag, "Exception while trying to send signalId=" + signalId );
      e.printStackTrace();

      return Result_e.Result_Error_Request_Failed_en;
    }

    return Result_e.Result_Ok_en;
  }


  /**
   * onServiceConnected
   *
   * Hook for when service connects
   */
  protected void onServiceConnected()
  {

  }


  /**
   * onServiceDisconnected
   *
   * Hook for when service disconnects
   */
  protected void onServiceDisconnected()
  {

  }
}

