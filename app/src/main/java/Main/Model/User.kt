package Main.Model

class User {

    var id : String? = null
    var firstName : String ? = null
    var lastName : String ? = null
    var isLoggedIn : Boolean ? =null
    var latitude : Double ? = null
    var longitude : Double ? = null
    var emailAddress : String ? = null
    var travellingCategory : String ? = null


    fun setLatitude(latitude: Double): User {
        this.latitude = latitude
        return this
    }

}