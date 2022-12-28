package international.tourism.app

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.text.method.LinkMovementMethod
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import international.tourism.app.models.User
import international.tourism.app.repo.AuthService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection.*

class LoginActivity : AppCompatActivity()
{
    private lateinit var sharedPref: SharedPreferences
    private lateinit var  authService: AuthService
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_TourismApp)
        setContentView(R.layout.activity_login)

        this.supportActionBar!!.hide()

        sharedPref = getSharedPreferences("tourism_pref", MODE_PRIVATE)
        val aEmail = sharedPref.getString("email",null)
        if(aEmail != null)
            sendToHome()

        val txtEmail = findViewById<EditText>(R.id.txtEmail)
        val txtPassword = findViewById<EditText>(R.id.txtPassword)
        val btnLogin = findViewById<Button>(R.id.btnlogin)
        val signUp = findViewById<TextView>(R.id.newUser)
        val btnClose = findViewById<ImageView>(R.id.btnClose)

        btnClose.setOnClickListener{startActivity(Intent(this,MainActivity::class.java))}



        signUp.setOnClickListener{
            val myIntent = Intent(this, RegisterActivity::class.java)
            startActivity(myIntent)

            signUp.movementMethod = LinkMovementMethod.getInstance()
        }

        btnLogin.setOnClickListener {
            val email = txtEmail.text.toString()
            val password = txtPassword.text.toString()
            user = User(Email = email, PasswordHash = password)
            loginDone(email)
        }
    }


    private fun loginDone(email: String)
    {

        CoroutineScope(Dispatchers.IO).launch {
            authService = AuthService()
            val response = authService.login(user)
            if (response.code == HTTP_OK)
            {
                user = Gson().fromJson(response.message, User::class.java)
                val id = user.Id
                val userIsDelete = user.UserIsDelete
                val status = user.Status
                if (userIsDelete != 1)
                {
                    if (status == 1)
                    {
                        withContext(Dispatchers.Main) {
                            val editor = sharedPref.edit()

                            editor.putString("email", email)
                            editor.putString("id", id.toString())
                            editor.apply()
                            sendToHome()
                        }
                    }
                    else
                    {
                        Looper.prepare()
                        Toast.makeText(this@LoginActivity, "User Is Deactivated!", Toast.LENGTH_LONG)
                            .show()
                        Looper.loop()
                    }
                }else
                {
                    Looper.prepare()
                    Toast.makeText(this@LoginActivity, "User Is Deleted!", Toast.LENGTH_LONG)
                        .show()
                    Looper.loop()
                }
            } else if (response.code == HTTP_NOT_FOUND)
            {
                Looper.prepare()
                Toast.makeText(this@LoginActivity, "Wrong email or password", Toast.LENGTH_LONG)
                    .show()
                Looper.loop()
            }

        }

    }
    private fun sendToHome(){
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}