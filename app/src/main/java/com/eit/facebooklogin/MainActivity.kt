package com.eit.facebooklogin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import org.json.JSONObject
import java.lang.Exception


class MainActivity : AppCompatActivity() {
    var callbackManager: CallbackManager? = null
    private val EMAIL:String="email"

    lateinit var loginButtton:LoginButton

    lateinit var name:TextView
    lateinit var userid:TextView
    lateinit var email:TextView
    lateinit var image:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Halper.printHashKey(this)


        callbackManager = CallbackManager.Factory.create()
        loginButtton = findViewById(R.id.login_button)

        name = findViewById(R.id.tvNameid)
        userid = findViewById(R.id.tvUserIdid)
        email = findViewById(R.id.tvEmailid)
        image = findViewById(R.id.tvImageid)
        loginButtton.setReadPermissions(listOf(EMAIL))


        loginButtton.setOnClickListener {
            LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    Log.d("FBLOGIN", loginResult.accessToken.token.toString())
                    Log.d("FBLOGIN", loginResult.recentlyDeniedPermissions.toString())
                    Log.d("FBLOGIN", loginResult.recentlyGrantedPermissions.toString())


                    val request = GraphRequest.newMeRequest(loginResult.accessToken) { obj, response ->
                        try {

                            if (obj.has("id")) {
                                Log.d("FBLOGIN", obj.getString("name"))
                                Log.d("FBLOGIN", obj.getString("email"))
                                Log.d("FBLOGIN", JSONObject(obj.getString("picture")).getJSONObject("data").getString("url"))

                                name.text=obj.getString("name").toString()
                                email.text=obj.getString("email").toString()
                                image.text= JSONObject(obj.getString("picture")).getJSONObject("data").getString("url").toString()

                            }

                        } catch (e: Exception) {
                            e.printStackTrace()

                        }
                    }

                    val parameters = Bundle()
                    parameters.putString("fields", "name,email,id,picture.type(large)")
                    request.parameters = parameters
                    request.executeAsync()

                }

                override fun onCancel() {
                    Log.e("FBLOGIN", "Cancel")
                }

                override fun onError(error: FacebookException) {
                    Log.e("FBLOGIN", "ERROR", error)
                }
            })
        }





    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager!!.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

}

