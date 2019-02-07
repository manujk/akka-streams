package th.co.truecorp.campaign.topup.core.utils

import th.co.truecorp.campaign.topup.model.{Parameters, Topup}
import play.api.libs.json._

import scala.util.{Failure, Success, Try}

object ParserJson {

  def jsonParse(input: String): Option[Topup] = {
    implicit val paramReads = Json.reads[Parameters]
    implicit val topupReads = Json.reads[Topup]


    import th.co.truecorp.campaign.topup.core.utils.Helpers._
    Try(Json.fromJson[Topup](Json.parse(input))) match {
      // TODO validate fields
      case Success(JsSuccess(t, _)) => Some(t)
      case Success(ex: JsError) => logException(input, ex)
      case Failure(ex: Throwable) => logException(input, ex)
    }
  }

  def validRecords(in: Option[Topup]): Boolean = in.isDefined

}
