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
package se.embeddev.mapo.myapplication.service.serviceB;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import se.embeddev.mapo.myapplication.service.BaseRequest_If;
import se.embeddev.mapo.myapplication.service.Parameters;
import se.embeddev.mapo.myapplication.service.Response_e;
import se.embeddev.mapo.myapplication.service.Result_e;


/**
 * ServiceB_Req_If
 *
 * Service B Request Interface
 */
public class ServiceB_Req_If extends BaseRequest_If
{
  /** Constants **/
  public static final String KLogTag = ServiceB_Req_If.class.getSimpleName();


  /**
   * OnServiceAListener
   *
   * Service Listener Interface
   */
  public interface OnServiceBListener
  {
    /**
     * onServiceResponse
     *
     * Called when confirm signal arrives for a request.
     *
     * @param signalId service B signal id
     * @param response service B response code
     * @param data     service B response data (optional)
     */
    void onServiceResponse(Request_e signalId, Response_e response, Bundle data);


    /**
     * onServiceIndication
     *
     * Called when indication arrives
     *
     * @param signalId service B signal id
     * @param data     service B indication data (optional)
     */
    void onServiceIndication(Request_e signalId, Bundle data);
  }


  /** Private Members **/
  Messenger          m_Messenger;
  OnServiceBListener m_Listener;


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

        case Request_UnSubscribe_Event_F_en:
        case Request_Subscribe_Event_F_en:
        case Request_D_en:
        case Request_E_en:
        {
          m_Listener.onServiceResponse( messageType, messageResponse, bundle );
          break;
        }

        case Indication_Event_F_en:
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
  public ServiceB_Req_If(OnServiceBListener listener)
  {
    super(KLogTag);
    m_Listener  = listener;
    m_Messenger = new Messenger( new SignalHandler());
  }


  /** Service Request Interface **/
  /**
   * ServiceB_If__Create
   *
   * Connects to service
   *
   * @param context reference to Context
   * @return returns Result_Ok_en if connection attempt successful, error code otherwise
   */
  public Result_e ServiceB_If__Create(Context context)
  {
    return connectToService( context, ServiceB.class );
  }


  /**
   * ServiceB_If__Destroy
   *
   * Disconnects from service
   * Service might live longer if there are more than one client
   *
   * @param context reference to Context
   * @return returns Result_Ok_en if disconnect attempt successful, error code otherwise
   */
  public Result_e ServiceB_If__Destroy(Context context)
  {
    return disconnectFromService( context );
  }


  /**
   * ServiceB_If__Request_D
   *
   * Sends Request_D_en to service
   *
   * @return returns Result_Ok_en if successfully sent, error code otherwise
   */
  public Result_e ServiceB_If__Request_D()
  {
    return sendToServiceIfPossible( Request_e.Request_D_en.valueOf(), null, m_Messenger );
  }


  /**
   * ServiceB_If__Request_E
   *
   * Sends Request_E_en to service
   *
   * @param paramA String parameter A
   * @param paramB String parameter B
   * @param paramC Integer parameter C
   * @return returns Result_Ok_en if successfully sent, error code otherwise
   */
  public Result_e ServiceB_If__Request_E(String paramA, String paramB, int paramC)
  {
    Bundle bundle = new Bundle();
    bundle.putString( Parameters.KKeyParameterA, paramA);
    bundle.putString(Parameters.KKeyParameterB, paramB);
    bundle.putInt( Parameters.KKeyParameterC, paramC );
    return sendToServiceIfPossible( Request_e.Request_E_en.valueOf(), bundle, m_Messenger );
  }


  /**
   * ServiceB_If__Request_Subscribe_Event_F
   *
   * Sends subscribe request for event F
   *
   * @return returns Result_Ok_en if successfully sent, error code otherwise
   */
  public Result_e ServiceB_If__Request_Subscribe_Event_F()
  {
    return sendToServiceIfPossible( Request_e.Request_Subscribe_Event_F_en.valueOf(), null, m_Messenger );
  }


  /**
   * ServiceB_If__Request_UnSubscribe_Event_F
   *
   * Sends UnSubscribe request for event F
   *
   * @return returns Result_Ok_en if successfully sent, error code otherwise
   */
  public Result_e ServiceB_If__Request_UnSubscribe_Event_F()
  {
    return sendToServiceIfPossible( Request_e.Request_UnSubscribe_Event_F_en.valueOf(), null, m_Messenger );
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
