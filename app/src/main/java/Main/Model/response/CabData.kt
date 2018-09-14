package Main.Model.response

import java.io.Serializable

class CabData : Serializable {

    var iD : Int ? = null
    var cabBannerImage : String ? = null
    var cabScreenShots : ArrayList<String> ? = null
    var cabName : String ? = null
    var cabCompanyName : String ? = null
    var cabModelNumber : String ? = null
    var cabrating : Float ? = null
    var cabPrice : Int ? = null
    var cabDiscount : Int ? = null
    var cabCategory : String ? = null
    var checkEnabled : Boolean = false
}
