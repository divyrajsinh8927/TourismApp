package international.tourism.app

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import international.tourism.app.models.User
import international.tourism.app.services.AuthService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection.*

class LoginActivity : AppCompatActivity()
{
    private lateinit var sharedPref: SharedPreferences
    private lateinit var authService: AuthService
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_TourismApp)
        setContentView(R.layout.activity_login)

        this.supportActionBar!!.hide()

        sharedPref = getSharedPreferences("tourism_pref", MODE_PRIVATE)
        val aEmail = sharedPref.getString("email", null)
        if (aEmail != null)
            sendToHome()

        val txtEmail = findViewById<EditText>(R.id.txtEmail)
        val txtPassword = findViewById<EditText>(R.id.txtPassword)
        val btnLogin = findViewById<Button>(R.id.btnlogin)
        val signUp = findViewById<TextView>(R.id.newUser)
        val btnClose = findViewById<ImageView>(R.id.btnClose)

        btnClose.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        signUp.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            signUp.movementMethod = LinkMovementMethod.getInstance()
        }

        btnLogin.setOnClickListener {
            val email = txtEmail.text.toString()
            val password = txtPassword.text.toString()
            user = User(Email = email, PasswordHash = password)

            loginDone()
        }

        findViewById<Button>(R.id.btnForgetPassword).setOnClickListener {
            startActivity(Intent(this,ForgotPasswordActivity::class.java))
        }
    }

    private fun loginDone()
    {
        CoroutineScope(Dispatchers.IO).launch {

            authService = AuthService()
            val response = authService.login(user)

            if (response.code == HTTP_NOT_FOUND)
            {
                withContext(Dispatchers.Main)
                {
                    Toast.makeText(this@LoginActivity, "Wrong email or password", Toast.LENGTH_LONG)
                        .show()
                }
                return@launch
            }

            user = Gson().fromJson(response.message, User::class.java)

            val userIsDelete = user.UserIsDelete
            val status = user.Status

            if (userIsDelete == 1)
            {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@LoginActivity, "User Is Deleted!", Toast.LENGTH_LONG)
                        .show()
                }
                return@launch
            }

            if (status == 0)
            {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@LoginActivity,
                        "User Is Deactivated!",
                        Toast.LENGTH_LONG
                    ).show()
                }
                return@launch
            }

            val database = openOrCreateDatabase("ShivTourism_db", MODE_PRIVATE, null)
            val query = "SELECT * FROM userToken"
            database.rawQuery(query, null).use { cursor ->
                if (cursor.count == 0)
                {
                    Toast.makeText(this@LoginActivity, "Token does not exist!", Toast.LENGTH_LONG).show()
                    return@launch
                }

                cursor.moveToFirst()
                val token = cursor.getString(1)

                val userToken = User(Id = user.Id, FirebaseToken = token)
                CoroutineScope(Dispatchers.IO).launch {
                    authService.addToken(userToken)
                }
            }

            withContext(Dispatchers.Main) {
                val editor = sharedPref.edit()

                editor.putString("email", user.Email)
                editor.putString("id", user.Id.toString())
                editor.apply()

                sendToHome()
            }
        }
    }

    private fun sendToHome()
    {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}


