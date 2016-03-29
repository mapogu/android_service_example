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


import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;


/**
 * BaseConfirm_If
 *
 * Base class for confirm service interface
 */
public class BaseConfirm_If
{
  /** Constants **/
  private static final String KLogTag = BaseConfirm_If.class.getSimpleName();


  /** Private Methods **/
  /**
   * Constructor
   */
  protected BaseConfirm_If() {}


  /**
   * sendToClient
   *
   * Sends message to client
   *
   * @param messenger reference to Messenger (client)
   * @param signalId request / indication signal id
   * @param response service response code
   * @param bundle service response / indication data (optionl)
   */
  protected static void sendToClient(Messenger messenger, int signalId, Response_e response, Bundle bundle)
  {
    Message message = Message.obtain( null, signalId );

    message.arg1 = response.valueOf();
    message.setData( bundle );

    try
    {
      messenger.send( message );
      Log.d( KLogTag, "Service -> Client: signalId=" + signalId + " response=" + response.toString() );
    }
    catch (RemoteException e)
    {
      Log.w( KLogTag, "Exception while trying to send signalId=" + signalId );
      e.printStackTrace();
    }
  }
}
