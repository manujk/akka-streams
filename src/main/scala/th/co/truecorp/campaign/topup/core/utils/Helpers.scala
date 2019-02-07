package th.co.truecorp.campaign.topup.core.utils

import play.api.libs.json.JsError

object Helpers {

  def logException(input: String, ex: scala.Throwable): Option[Nothing] = {
    //    println(s"[jsonParse] => Invalid Json: $input, Exception: ${ex.getMessage}")
    None
  }

  def logException(input: String, ex: JsError): Option[Nothing] = {
    //    println(s"[jsonParse] => Invalid Json: $input, Exception: ${JsError.toJson(ex).toString()}")
    None
  }

}
