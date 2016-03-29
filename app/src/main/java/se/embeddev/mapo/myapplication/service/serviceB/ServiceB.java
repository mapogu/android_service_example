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


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import java.util.Vector;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import se.embeddev.mapo.myapplication.service.BaseService;
import se.embeddev.mapo.myapplication.service.Parameters;
import se.embeddev.mapo.myapplication.service.Response_e;
import se.embeddev.mapo.myapplication.service.serviceA.ServiceA_Req_If;


/**
 * ServiceB
 *
 * Handles requests from client and processes them
 */
public class ServiceB extends BaseService implements ServiceA_Req_If.OnServiceAListener
{
  /** Constants **/
  private static final String KLogTag = ServiceB.class.getSimpleName();


  /** Private Members **/
  ScheduledThreadPoolExecutor m_Executor;
  Vector<ScheduledFuture<?>>  m_ScheduledTasks;
  ServiceA_Req_If             m_ServiceA_Interface;


  /** Private Classes **/
  /**
   * SignalHandler
   *
   * Handles request signals from client
   */
  private final class SignalHandler extends Handler
  {
    /**
     * Constructor
     *
     * @param looper Looper in which handler should execute in.
     */
    public SignalHandler(Looper looper)
    {
      super( looper );
    }


    /**
     * handleMessage
     *
     * Handles requests from client
     *
     * @param message message to be handled.
     */
    @Override
    public void handleMessage(Message message)
    {
      Request_e signalId = Request_e.fromInt( message.what );
      Bundle bundle   = message.peekData();

      Log.d( KLogTag, "handleMessage: signalId=" + signalId.toString() );

      switch ( signalId )
      {
        case Request_D_en:_en:
        {
          ServiceB_Cnf_If.ServiceB_Cnf_If__Confirm_D( message.replyTo, Response_e.Response_Ok_en );
          m_ServiceA_Interface.ServiceA_If__Request_Subscribe_Event_C();
          break;
        }

        case Request_E_en:
        {
          ServiceB_Cnf_If.ServiceB_Cnf_If__Confirm_E( message.replyTo, Response_e.Response_Ok_en, "paramA", 42 );

          Log.d( KLogTag, signalId.toString() + " " +
                          bundle.getString( Parameters.KKeyParameterA ) + " " +
                          bundle.getString( Parameters.KKeyParameterB ) + " " +
                          bundle.getInt( Parameters.KKeyParameterC ) );

          m_ServiceA_Interface.ServiceA_If__Request_UnSubscribe_Event_C();
          break;
        }

        case Request_Subscribe_Event_F_en:
        {
          ServiceB_Cnf_If.ServiceB_Cnf_If__Confirm_Subscribe_Event_F( message.replyTo, addSubscriber( message.replyTo ) ? Response_e.Response_Ok_en : Response_e.Response_Error_Already_Exists_en );


          Runnable task = new Runnable()
          {
            @Override
            public void run() {
              BaseService.OnSend sender = new BaseService.OnSend()
              {
                String m_ParamA;
                String m_ParamB;
                int m_ParamC;


                public void send(Messenger messenger)
                {
                  ServiceB_Cnf_If.ServiceB_Cnf_If__Indication_Event_F( messenger, m_ParamA, m_ParamB, m_ParamC );
                }


                public OnSend setParameters(String paramA, String paramB, int paramC)
                {
                  m_ParamA = paramA;
                  m_ParamB = paramB;
                  m_ParamC = paramC;
                  return this;
                }
              }.setParameters( "paramA", "paramB", 42 );


              notifySubscribers( sender );
            }
          };


          m_ScheduledTasks.add( m_Executor.scheduleAtFixedRate( task, 5, 5, TimeUnit.SECONDS ) );
          break;
        }

        case Request_UnSubscribe_Event_F_en:
        {
          Response_e response = Response_e.Response_Error_Not_Found_en;
          if ( removeSubscriber( message.replyTo ) )
          {
            if (numberOfSubscribers() == 0)
            {
              clearAllScheduledTasks();
            }
            response = Response_e.Response_Ok_en;
          }

          ServiceB_Cnf_If.ServiceB_Cnf_If__Confirm_UnSubscribe_Event_F( message.replyTo, response );
          break;
        }

        default:
          Log.w(KLogTag, "Unhandled signalId=" + signalId.toString());
          super.handleMessage( message );
      }
    }
  }


  /** Public Methods **/
  /**
   * Constructor
   */
  public ServiceB()
  {
    super(KLogTag);

    m_Executor       = new ScheduledThreadPoolExecutor(1);
    m_ScheduledTasks = new Vector<>(  );
  }


  /**
   * onDestroy
   *
   * Hook for when service is about to be destroyed.
   */
  @Override
  public void onDestroy()
  {
    m_ServiceA_Interface.ServiceA_If__Destroy( this );

    clearAllScheduledTasks();


    super.onDestroy();
  }


  /**
   * onServiceResponse
   *
   * Called when confirm signal arrives for a request.
   *
   * @param signalId service A signal id
   * @param response service A response code
   * @param data     service A response data (optional)
   */
  @Override
  public void onServiceResponse(se.embeddev.mapo.myapplication.service.serviceA.Request_e signalId, Response_e response, Bundle data)
  {
    Log.d( KLogTag, "onServiceResponse signalId=" + signalId.toString() );
  }


  /**
   * onServiceIndication
   *
   * Called when indication arrives
   *
   * @param signalId service A signal id
   * @param data     service A indication data (optional)
   */
  @Override
  public void onServiceIndication(se.embeddev.mapo.myapplication.service.serviceA.Request_e signalId, Bundle data)
  {
    Log.d( KLogTag, "onServiceIndication signalId=" + signalId.toString());
  }


  /** Private Methods **/
  /**
   * onCreate
   *
   * @param looper Thread in which handler executes in.
   * @return instance to handler which processes messages
   */
  protected Handler onCreate(Looper looper)
  {
    m_ServiceA_Interface = new ServiceA_Req_If( this );
    m_ServiceA_Interface.ServiceA_If__Create( this );

    return new SignalHandler( looper );
  }


  /**
   * clearAllScheduledTasks
   *
   * clears all scheduled tasks, i.e timers.
   */
  private void clearAllScheduledTasks()
  {
    for (ScheduledFuture<?> item : m_ScheduledTasks)
    {
      item.cancel( true );
    }
    m_ScheduledTasks.clear();
  }
}