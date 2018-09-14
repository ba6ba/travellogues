package Main.Model.response

import java.io.Serializable

class AllDistrictsResponse : Serializable {
    var message : String ? = null
    var data : ArrayList<AllDistrictsData> ? = null
}