package Main.Model.response

import java.io.Serializable

class TravellerInfoResponse {
    var success : Boolean ? = null
    var data : Data ? = null

    class Data : Serializable {
        var token : String ? = null
        var canSend : Boolean ? = null
    }
}