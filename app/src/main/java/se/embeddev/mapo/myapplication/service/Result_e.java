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
 * Result_e
 *
 * Service Interface Result Values
 */
public enum Result_e
{
  /** Enum Values **/
  Result_Ok_en(0),
  Result_Error_Request_Failed_en(1),
  Result_Error_Invalid_en(2);


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
   * @return enumeration, Result_Error_Invalid_en if not possible to convert.
   */
  public static Result_e fromInt(int value)
  {
    if (Result_Ok_en.valueOf() == value)
    {
      return Result_Ok_en;
    }
    else if (Result_Error_Request_Failed_en.valueOf() == value)
    {
      return Result_Error_Request_Failed_en;
    }

    return Result_Error_Invalid_en;
  }


  /** Private Methods **/
  /**
   * Request_e enum constructor
   * @param value Integer value of enumeration.
   */
  Result_e(int value)
  {
    m_Value = value;
  }
}
