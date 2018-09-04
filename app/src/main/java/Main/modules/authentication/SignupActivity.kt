package modules.authentication

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import base.ParentActivity
import com.example.sarwan.final_year_project.R
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import extras.AlertDialog
import kotlinx.android.synthetic.main.signup_layout.*
import kotlinx.android.synthetic.main.third_party_service_layout.*
import modules.main.MainActivity
import utils.ValidationUtility
import java.util.*
import java.util.Arrays.asList



class SignupActivity : ParentActivity() {

    private var auth: FirebaseAuth? = null

    //for Signup
    private var databaseReference: DatabaseReference? = null
    private var firebaseDatabase: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_layout)

        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase?.getReference()?.child("Users")
        alertDialog = AlertDialog()
        setupSpinner()
        generateToken()
        signUp()
    }

    private fun setupSpinner() {
        val dataset = LinkedList(Arrays.asList("Solo", "Group Tour", "Volunteer Travel", "Caravan/RV Road Traveller", "Occasionally"))
        spinner.attachDataSource(dataset)
    }

    private var userId : String ? = null
    private fun generateToken() {
        userId = UUID.randomUUID().toString()
    }


    private fun signUp() {
        signUpButton.setOnClickListener {
            checkFields()
        }
        facebookButton.setOnClickListener {
            showProgress()
            alertDialog?.builder(this,"Can't Connect right now :(")
            hideProgress()
        }

        twitterButton.setOnClickListener {
            showProgress()
            alertDialog?.builder(this,"Can't Connect right now :(")
            hideProgress()
        }

        googlePlusButton.setOnClickListener {
            showProgress()
            alertDialog?.builder(this,"Can't Connect right now :(")
            hideProgress()
        }
    }

    private var alertDialog : AlertDialog ? = null
    private fun checkFields() {


        if ((!TextUtils.isEmpty(firstNameSignUp.text))
                && (!TextUtils.isEmpty(secondNameSignUp.text))
                && (!TextUtils.isEmpty(emailSignUp.text))
                && (!TextUtils.isEmpty(passwordSignUp.text))){
            showProgress()
            signUpProcessing()
        }

        else {
            ValidationUtility.setError(firstNameSignUpLayout,getString(R.string.must_not_be_empty))
            ValidationUtility.setError(secondNameSignUpLayout,getString(R.string.must_not_be_empty))
            ValidationUtility.setError(EmailSignUpLayout,getString(R.string.must_not_be_empty))
            ValidationUtility.setError(passwordSignUpLayout,getString(R.string.must_not_be_empty))
            if(!spinner.isSelected){
                Toast.makeText(this,getString(R.string.must_not_be_empty),Toast.LENGTH_LONG).show()
            }
            ValidationUtility.removeErrors(firstNameSignUpLayout,secondNameSignUpLayout,EmailSignUpLayout,passwordSignUpLayout)
        }
    }

    private fun signUpProcessing() {
        auth?.createUserWithEmailAndPassword(emailSignUp.text.toString(), passwordSignUp.text.toString())
                ?.addOnCompleteListener { task: Task<AuthResult> ->

                    if (auth != null) {

                        val userAuthId = auth?.getCurrentUser()?.uid
                        if(userAuthId!=null){
                            val current_user_db = databaseReference?.child(userAuthId as String)
                            current_user_db?.child("id")?.setValue(userId!!)
                            current_user_db?.child("name")?.setValue(firstNameSignUp.text.toString() + secondNameSignUp.text.toString())
                            current_user_db?.child("email")?.setValue(emailSignUp.text.toString())
                            current_user_db?.child("password")?.setValue(passwordSignUp.text.toString())
                            current_user_db?.child("travellingCategory")?.setValue(spinner.text.toString())
                            hideProgress()
                            saveData()
                            openActivityWithFinish(Intent(this@SignupActivity, MainActivity::class.java))
                        }
                        else {
                            alertDialog?.builder(this,"You can't register with same Email address. Try new one.")
                        }

                    } else {
                        hideProgress()
                        alertDialog?.builder(this,task.exception?.message)
                    }
                }
    }

    private fun saveData() {
        profile?.emailAddress = emailSignUp.text.toString()
        profile?.firstName = firstNameSignUp.text.toString()
        profile?.lastName = secondNameSignUp.text.toString()
        profile?.id = userId
        profile?.travellingCategory = spinner.text.toString()
        profile?.isLoggedIn = true
        saveProfileInSharedPreference()
    }

}

