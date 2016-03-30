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
package se.embeddev.mapo.myapplication.service.serviceA;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import se.embeddev.mapo.myapplication.service.BaseRequest;
import se.embeddev.mapo.myapplication.service.Parameters;
import se.embeddev.mapo.myapplication.service.Response_e;
import se.embeddev.mapo.myapplication.service.Result_e;


/**
 * ServiceA_Req
 *
 * Service A Request Interface
 */
public class ServiceA_Req extends BaseRequest
{
  /** Constants **/
  public static final String KLogTag = ServiceA_Req.class.getSimpleName();


  /**
   * OnServiceAListener
   *
   * Service Listener Interface
   */
  public interface OnServiceAListener
  {
    /**
     * onServiceResponse
     *
     * Called when confirm signal arrives for a request.
     *
     * @param signalId service A signal id
     * @param response service A response code
     * @param data     service A response data (optional)
     */
    void onServiceResponse(Request_e signalId, Response_e response, Bundle data);


    /**
     * onServiceIndication
     *
     * Called when indication arrives
     *
     * @param signalId service A signal id
     * @param data     service A indication data (optional)
     */
    void onServiceIndication(Request_e signalId, Bundle data);
  }


  /** Private Members **/
  Messenger          m_Messenger;
  OnServiceAListener m_Listener;


  /**
   * SignalHandler
   *
   * Handles confirm signals from service
   **/
  class SignalHandler extends Handler
  {
    /**
     * handleMessage
     *
     * Processes confirm signals from service and notifies listener
     *
     * @param message Message to be handled
     */
    @Override
    public void handleMessage(Message message)
    {
      Request_e  messageType     = Request_e.fromInt( message.what );
      Response_e messageResponse = Response_e.fromInt( message.arg1 );
      Bundle     bundle          = message.peekData();

      Log.d( KLogTag, "handleMessage: signalId=" + messageType.toString() + " result=" + messageResponse.toString() );

      switch ( messageType )
      {
        case Request_UnSubscribe_Event_C_en:
        case Request_Subscribe_Event_C_en:
        case Request_B_en:
        case Request_A_en:
        {
          m_Listener.onServiceResponse( messageType, messageResponse, bundle );
          break;
        }

        case Indication_Event_C_en:
        {
          m_Listener.onServiceIndication( messageType, bundle );
          break;
        }
        default:
        {
          Log.w( KLogTag, "Unhandled signalId=" + messageType.toString() );
          break;
        }
      }
    }
  }


  /** Public Methods **/
  /**
   * Constructor
   *
   * @param listener reference to OnServiceAListener
   */
  public ServiceA_Req(OnServiceAListener listener)
  {
    super(KLogTag);
    m_Listener  = listener;
    m_Messenger = new Messenger( new SignalHandler());
  }


  /** Service Request Interface **/
  /**
   * connect
   *
   * Connects to service
   *
   * @param context reference to Context
   * @return returns Result_Ok_en if connection attempt successful, error code otherwise
   */
  public Result_e connect(Context context)
  {
    return connectToService( context, ServiceA.class );
  }


  /**
   * disconnect
   *
   * Disconnects from service
   * Service might live longer if there are more than one client
   *
   * @param context reference to Context
   * @return returns Result_Ok_en if disconnect attempt successful, error code otherwise
   */
  public Result_e disconnect(Context context)
  {
    return disconnectFromService( context );
  }


  /**
   * request_A
   *
   * Sends Request_A_en to service
   *
   * @return returns Result_Ok_en if successfully sent, error code otherwise
   */
  public Result_e request_A()
  {
    return sendToServiceIfPossible( Request_e.Request_A_en.valueOf(), null, m_Messenger );
  }


  /**
   * request_B
   *
   * Sends Request_B_en to service
   *
   * @param paramA String parameter A
   * @param paramB String parameter B
   * @param paramC Integer parameter C
   * @return returns Result_Ok_en if successfully sent, error code otherwise
   */
  public Result_e request_B(String paramA, String paramB, int paramC)
  {
    Bundle bundle = new Bundle();
    bundle.putString( Parameters.KKeyParameterA, paramA);
    bundle.putString(Parameters.KKeyParameterB, paramB);
    bundle.putInt( Parameters.KKeyParameterC, paramC );
    return sendToServiceIfPossible( Request_e.Request_B_en.valueOf(), bundle, m_Messenger );
  }


  /**
   * subscribe_C
   *
   * Sends subscribe request for event C
   *
   * @return returns Result_Ok_en if successfully sent, error code otherwise
   */
  public Result_e subscribe_C()
  {
    return sendToServiceIfPossible( Request_e.Request_Subscribe_Event_C_en.valueOf(), null, m_Messenger );
  }


  /**
   * unSubscribe_C
   *
   * Sends UnSubscribe request for event C
   *
   * @return returns Result_Ok_en if successfully sent, error code otherwise
   */
  public Result_e unSubscribe_C()
  {
    return sendToServiceIfPossible( Request_e.Request_UnSubscribe_Event_C_en.valueOf(), null, m_Messenger );
  }


  /** Private Methods **/
  /**
   * onServiceConnected
   *
   * Hook for when service is connected
   */
  @Override
  protected void onServiceConnected()
  {
    m_Listener.onServiceResponse( Request_e.Request_Connect_en, Response_e.Response_Ok_en, null );
  }


  /**
   * onServiceDisconnected
   *
   * Hook for when service is disconnected
   */
  @Override
  protected void onServiceDisconnected()
  {
    m_Listener.onServiceResponse( Request_e.Request_Disconnect_en, Response_e.Response_Ok_en, null );
  }
}
