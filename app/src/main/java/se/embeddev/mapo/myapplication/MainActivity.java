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
package se.embeddev.mapo.myapplication;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import se.embeddev.mapo.myapplication.service.Parameters;
import se.embeddev.mapo.myapplication.service.Response_e;
import se.embeddev.mapo.myapplication.service.Result_e;
import se.embeddev.mapo.myapplication.service.serviceA.ServiceA_Req;
import se.embeddev.mapo.myapplication.service.serviceB.ServiceB_Req;


/**
 * MainActivity
 *
 * Application main activity
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, ServiceA_Req.OnServiceAListener, ServiceB_Req.OnServiceBListener
{
  /** Private Members **/
  private ServiceA_Req m_ServiceA;
  private ServiceB_Req m_ServiceB;


  /** Private Methods **/
  /**
   * onStart
   *
   * Called when activity is started
   */
  @Override
  protected void onStart()
  {
    super.onStart();
    m_ServiceA = new ServiceA_Req( this );
    m_ServiceB = new ServiceB_Req( this );
  }


  /**
   * onCreate
   *
   * Called when activity is created
   *
   * @param savedInstanceState reference to Bundle containing activity state
   */
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.activity_main );

    findViewById( R.id.id_st_button_ok ).setOnClickListener( this );

    if (savedInstanceState == null)
    {
      TextView textView = (TextView) findViewById( R.id.id_st_label_log );
      textView.setText( "* [h]elp(?) - Display help text.\n" +
                        "* 1  - Starts Service A\n" +
                        "* 2  - Stops Service A\n" +
                        "* 3  - Sends Request A to Service A\n" +
                        "* 4  - Sends Request B to Service A\n" +
                        "* 5  - Subscribe to Event C from Service A\n" +
                        "* 6  - UnSubscribe to Event C from Service A\n" +
                        "* 7  - Start Service B\n" +
                        "* 8  - Stop Service B\n" +
                        "* 9  - Send Request D to Service B\n" +
                        "* 10 - Send Request E tp Service B\n" +
                        "* 11 - Subscribe to Event F from Service B\n" +
                        "* 12 - UnSubscribe to Event F from Service B\n" );
    }
  }

  /**
   * onClick
   *
   * onClick callback
   *
   * @param view reference to view that was clicked.
   */
  @Override
  public void onClick(View view)
  {
    EditText editText = (EditText) findViewById( R.id.id_st_edit_text_command );
    TextView textView = (TextView) findViewById( R.id.id_st_label_log );

    String commandText = editText.getText().toString();
    String logText     = textView.getText().toString();

    logText += "> " + commandText + "\n";
    String out = "";
    Result_e result = Result_e.Result_Ok_en;

    if (commandText.compareTo( "?" ) == 0 || commandText.compareToIgnoreCase( "help" ) == 0 || commandText.compareToIgnoreCase( "h" ) == 0)
    {
      out = "* [h]elp(?) - Display help text.\n" +
            "* 1  - Starts Service A\n" +
            "* 2  - Stops Service A\n" +
            "* 3  - Sends Request A to Service A\n" +
            "* 4  - Sends Request B to Service A\n" +
            "* 5  - Subscribe to Event C from Service A\n" +
            "* 6  - UnSubscribe to Event C from Service A\n" +
            "* 7  - Start Service B\n" +
            "* 8  - Stop Service B\n" +
            "* 9  - Send Request D to Service B\n" +
            "* 10 - Send Request E tp Service B\n" +
            "* 11 - Subscribe to Event F from Service B\n" +
            "* 12 - UnSubscribe to Event F from Service B\n";
    }
    else if (commandText.compareTo("1") == 0)
    {
      result = m_ServiceA.connect( this );
    }
    else if (commandText.compareTo("2") == 0)
    {
      result = m_ServiceA.disconnect( this );
    }
    else if (commandText.compareTo("3") == 0)
    {
      result = m_ServiceA.request_A();
    }
    else if (commandText.compareTo("4") == 0)
    {
      result = m_ServiceA.request_B( "paramA", "paramB", 42 );
    }
    else if (commandText.compareTo("5") == 0)
    {
      result = m_ServiceA.subscribe_C();
    }
    else if (commandText.compareTo("6") == 0)
    {
      result = m_ServiceA.unSubscribe_C();
    }
    else if (commandText.compareTo("7") == 0)
    {
      result = m_ServiceB.connect( this );
    }
    else if (commandText.compareTo("8") == 0)
    {
      result = m_ServiceB.disconnect( this );
    }
    else if (commandText.compareTo("9") == 0)
    {
      result = m_ServiceB.request_D();
    }
    else if (commandText.compareTo("10") == 0)
    {
      result = m_ServiceB.request_E( "paramA", "paramB", 42 );
    }
    else if (commandText.compareTo("11") == 0)
    {
      result = m_ServiceB.subscribe_F();
    }
    else if (commandText.compareTo("12") == 0)
    {
      result = m_ServiceB.unSubscribe_F();
    }
    else
    {
      out = "* Invalid command.\n";
    }

    if (result != Result_e.Result_Ok_en)
    {
      out = "* Request failed with result=" + result.toString() + "\n";
    }

    textView.setText( logText + out );
    editText.setText( "" );
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
    TextView textView = (TextView)findViewById( R.id.id_st_label_log);

    String log = textView.getText().toString();
    log += "* request=" + signalId.toString() + " response=" + response.toString() + "\n";

    if (data != null)
    {
        log += "* paramA=" + data.getString( Parameters.KKeyParameterA, "" ) + " paramB=" + data.getInt( Parameters.KKeyParameterB, Integer.MIN_VALUE ) + "\n";
    }
    textView.setText( log );
  }


  /**
   * onServiceResponse
   *
   * Called when confirm signal arrives for a request.
   *
   * @param signalId service B signal id
   * @param response service B response code
   * @param data     service B response data (optional)
   */
  @Override
  public void onServiceResponse(se.embeddev.mapo.myapplication.service.serviceB.Request_e signalId, Response_e response, Bundle data)
  {
    TextView textView = (TextView)findViewById( R.id.id_st_label_log);

    String log = textView.getText().toString();
    log += "* request=" + signalId.toString() + " response=" + response.toString() + "\n";

    if (data != null)
    {
      log += "* paramA=" + data.getString( Parameters.KKeyParameterA, "" ) + " paramB=" + data.getInt( Parameters.KKeyParameterB, Integer.MIN_VALUE ) + "\n";
    }
    textView.setText( log );
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
    TextView textView = (TextView)findViewById( R.id.id_st_label_log);

    String log = textView.getText().toString();
    log += "* request=" + signalId.toString() + "\n";

    if (data != null)
    {
      log += "* paramA=" + data.getString( Parameters.KKeyParameterA, "" ) + " paramB=" + data.getString( Parameters.KKeyParameterB, "" ) + " paramC=" + data.getInt(Parameters.KKeyParameterC, Integer.MIN_VALUE) + "\n";
    }
    textView.setText( log );
  }


  /**
   * onServiceIndication
   *
   * Called when indication arrives
   *
   * @param signalId service B signal id
   * @param data     service B indication data (optional)
   */
  @Override
  public void onServiceIndication(se.embeddev.mapo.myapplication.service.serviceB.Request_e signalId, Bundle data)
  {
    TextView textView = (TextView)findViewById( R.id.id_st_label_log);

    String log = textView.getText().toString();
    log += "* request=" + signalId.toString() + "\n";

    if (data != null)
    {
      log += "* paramA=" + data.getString( Parameters.KKeyParameterA, "" ) + " paramB=" + data.getString( Parameters.KKeyParameterB, "" ) + " paramC=" + data.getInt(Parameters.KKeyParameterC, Integer.MIN_VALUE) + "\n";
    }
    textView.setText( log );
  }
}
