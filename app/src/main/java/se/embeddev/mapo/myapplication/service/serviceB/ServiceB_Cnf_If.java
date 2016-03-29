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
import android.os.Messenger;

import se.embeddev.mapo.myapplication.service.BaseConfirm_If;
import se.embeddev.mapo.myapplication.service.Parameters;
import se.embeddev.mapo.myapplication.service.Response_e;


/**
 * ServiceB_Cnf_If
 *
 * Service B Confirm Interface
 */
public class ServiceB_Cnf_If extends BaseConfirm_If
{
  /**
   * ServiceB_Cnf_If__Confirm_D
   *
   * Sends confirm signal for Request_D_en
   *
   * @param messenger reference to Messenger (client)
   * @param response  service response code
   */
  static void ServiceB_Cnf_If__Confirm_D(Messenger messenger, Response_e response)
  {
    sendToClient(messenger, Request_e.Request_D_en.valueOf(), response, null);
  }


  /**
   *  ServiceB_Cnf_If__Confirm_E
   *
   *  Sends confirm signal for Request_E_en
   *
   * @param messenger reference to Messenger (client)
   * @param response service response code
   * @param paramA String containing response data
   * @param paramB Integer containing response data
   */
  static void ServiceB_Cnf_If__Confirm_E(Messenger messenger, Response_e response, String paramA, int paramB)
  {
    Bundle bundle = new Bundle();
    bundle.putString( Parameters.KKeyParameterA, paramA);
    bundle.putInt( Parameters.KKeyParameterB, paramB );
    sendToClient( messenger, Request_e.Request_E_en.valueOf(), response, bundle );
  }


  /**
   * ServiceB_Cnf_If__Confirm_Subscribe_Event_F
   *
   * Sends confirm signal for subscription to event F
   *
   * @param messenger reference to Messenger (client)
   * @param response service response code
   */
  static void ServiceB_Cnf_If__Confirm_Subscribe_Event_F(Messenger messenger, Response_e response)
  {
    sendToClient( messenger, Request_e.Request_Subscribe_Event_F_en.valueOf(), response, null );
  }


  /**
   * ServiceB_Cnf_If__Confirm_UnSubscribe_Event_F
   *
   * Sends confirm signal for UnSubscription to event F
   *
   * @param messenger reference to Messenger (client)
   * @param response service response code
   */
  static void ServiceB_Cnf_If__Confirm_UnSubscribe_Event_F(Messenger messenger, Response_e response)
  {
    sendToClient( messenger, Request_e.Request_UnSubscribe_Event_F_en.valueOf(), response, null );
  }


  /**
   * ServiceB_Cnf_If__Indication_Event_F
   *
   * Sends indication to client for event F
   *
   * @param messenger reference to Messenger (client)
   * @param paramA String indication data
   * @param paramB String indication data
   * @param paramC Integer indication data
   */
  static void ServiceB_Cnf_If__Indication_Event_F(Messenger messenger, String paramA, String paramB, int paramC)
  {
    Bundle bundle = new Bundle();
    bundle.putString(Parameters.KKeyParameterA, paramA);
    bundle.putString(Parameters.KKeyParameterB, paramB );
    bundle.putInt( Parameters.KKeyParameterC, paramC);
    sendToClient( messenger, Request_e.Indication_Event_F_en.valueOf(), Response_e.Response_Ok_en, bundle );
  }
}
