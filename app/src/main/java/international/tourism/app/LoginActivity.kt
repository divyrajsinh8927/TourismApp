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
    private var userId: Int = 0

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

        btnClose.setOnClickListener { startActivity(Intent(this, MainActivity::class.java)) }



        signUp.setOnClickListener {
            val myIntent = Intent(this, RegisterActivity::class.java)
            startActivity(myIntent)

            signUp.movementMethod = LinkMovementMethod.getInstance()
        }

        btnLogin.setOnClickListener {
            val email = txtEmail.text.toString()
            val password = txtPassword.text.toString()
            user = User(Email = email, PasswordHash = password)
            loginDone()
            registerToken()
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
            userId = user.Id
            val userIsDelete = user.UserIsDelete
            val status = user.Status
            if (userIsDelete == 1)
            {
                withContext(Dispatchers.Main){
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
            withContext(Dispatchers.Main) {
                val editor = sharedPref.edit()

                editor.putString("email", user.Email)
                editor.putString("id", userId.toString())
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

    private fun registerToken()
    {

        val database = openOrCreateDatabase("ShivTourism_db", MODE_PRIVATE, null)
        val query = "SELECT * FROM userToken"
        val cursor = database.rawQuery(query,null)
        if (cursor.count == 0)
        {
            cursor.close()
            Toast.makeText(this, "Token does not exist!", Toast.LENGTH_LONG).show()
            return
        }

        cursor.moveToFirst()
        val token = cursor.getString(1)

        user = User(FirebaseToken = token)
        CoroutineScope(Dispatchers.IO).launch {
            authService.addToken(user)
        }
        cursor.close()
    }
}


