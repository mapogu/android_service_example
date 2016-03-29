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


/**
 * Request_e
 *
 * Request Signals
 * **/
public enum Request_e
{
  /** Enum Values **/
  Request_Connect_en(0),
  Request_Disconnect_en(1),
  Request_D_en(2),
  Request_E_en(3),
  Request_Subscribe_Event_F_en(4),
  Request_UnSubscribe_Event_F_en(5),
  Indication_Event_F_en(6),
  Request_Invalid_en( 7 );


  /** Private Members **/
  private int m_Value;


  /** Public Methods **/
  /**
   * valueOf
   *
   * @return Integer value of enumeration.
   */
  public int valueOf()
  {
    return m_Value;
  }


  /**
   * fromInt
   *
   * @param value Integer value to convert to enumeration
   * @return enumeration, Request_Invalid_en if not possible to convert.
   */
  public static Request_e fromInt(final int value)
  {
    if (Request_Connect_en.valueOf() == value)
    {
      return Request_Connect_en;
    }
    else if (Request_Disconnect_en.valueOf() == value)
    {
      return Request_Disconnect_en;
    }
    else if (Request_D_en.valueOf() == value)
    {
      return Request_D_en;
    }
    else if (Request_E_en.valueOf() == value)
    {
      return Request_E_en;
    }
    else if (Request_Subscribe_Event_F_en.valueOf() == value)
    {
      return Request_Subscribe_Event_F_en;
    }
    else if (Request_UnSubscribe_Event_F_en.valueOf() == value)
    {
      return Request_UnSubscribe_Event_F_en;
    }
    else if (Indication_Event_F_en.valueOf() == value)
    {
      return Indication_Event_F_en;
    }
    return Request_Invalid_en;
  }


  /** Private Methods **/
  /**
   * Request_e enum constructor
   * @param value Integer value of enumeration.
   */
  Request_e(int value)
  {
    m_Value = value;
  }
}