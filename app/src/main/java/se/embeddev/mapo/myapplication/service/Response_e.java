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


/**
 * Response_e
 *
 * Service Response Values
 */
public enum Response_e
{
  /** Enum Values **/
  Response_Ok_en(0),
  Response_Error_Already_Exists_en(1),
  Response_Error_Not_Found_en(2),
  Response_Error_Request_Failed_en(3),
  Response_Error_Invalid_en(4);


  /** Private members **/
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
   * @return enumeration, Response_Error_Invalid_en if not possible to convert.
   */
  public static Response_e fromInt(int value)
  {
    if (Response_Ok_en.valueOf() == value)
    {
      return Response_Ok_en;
    }
    else if(Response_Error_Already_Exists_en.valueOf() == value)
    {
      return Response_Error_Already_Exists_en;
    }
    else if(Response_Error_Not_Found_en.valueOf() == value)
    {
      return Response_Error_Not_Found_en;
    }
    else if (Response_Error_Request_Failed_en.valueOf() == value)
    {
      return Response_Error_Request_Failed_en;
    }
    return Response_Error_Invalid_en;
  }


  /** Private Methods **/
  /**
   * Request_e enum constructor
   * @param value Integer value of enumeration.
   */
  Response_e(int value)
  {
    m_Value = value;
  }
}
