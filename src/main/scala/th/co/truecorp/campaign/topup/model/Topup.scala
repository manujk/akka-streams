package th.co.truecorp.campaign.topup.model

case class Parameters(RequestID: String,
                      BalanceType: String,
                      Amount: String,
                      NewBalance: String,
                      Validity: String,
                      NewValidity: String,
                      SubscriberPricePlanID: String,
                      AccountType: String,
                      IMSI: String,
                      Language: String,
                      ChainID: String)

case class Topup(NotificationID: String,
                 EventBeginTime: String,
                 MSISDN: String,
                 EventType: String,
                 parameters: Parameters)

