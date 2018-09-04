package utils

import android.util.Log
import network.ApiEndPoints
import network.NetworkConstants
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class HashUtility {
    companion object {
        fun md5New(output: String): String {
            var output = output
            try {

                val digest = MessageDigest.getInstance("MD5")
                digest.reset()
                digest.update(output.toByteArray())
                val outDigest = digest.digest()
                val outBigInt = BigInteger(1, outDigest)
                output = outBigInt.toString(16)
                while (output.length < 32) {
                    output = "0$output"
                }
            } catch (e: NoSuchAlgorithmException) {

                e.printStackTrace()
            }

            return output.toLowerCase()
        }

        private val TAG: String? = "HashUtility"

        fun getHashOf(userId: Int, userToken: String, apiEndPoints: String) : String {
            val stringForHash = NetworkConstants.API_VERSION +
                    apiEndPoints.replace("{userId}", userId.toString()) +
                    userId +
                    userToken
            Log.d(TAG, "sendQRCode: StringForHash $stringForHash")
            return HashUtility.md5New(stringForHash)
        }

        fun getHashWithPathParam(userId: Int, userToken: String, apiEndPoints: String, pathParamsKey:String, pathParamValue: String ) : String {
            val stringForHash = NetworkConstants.API_VERSION +
                    apiEndPoints.replace("{"+pathParamsKey+"}", pathParamValue) +
                    userId +
                    userToken
            Log.d(TAG, "sendQRCode: StringForHash $stringForHash")
            return HashUtility.md5New(stringForHash)
        }
    }
}