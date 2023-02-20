package international.tourism.app

import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import international.tourism.app.models.User
import international.tourism.app.services.AuthService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.HttpURLConnection

class RegisterActivity : AppCompatActivity()
{

    private lateinit var authService: AuthService
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val txtName = findViewById<EditText>(R.id.txtUsername)
        val txtMobileNUmber = findViewById<EditText>(R.id.txtMobileNumber)
        val txtEmail = findViewById<EditText>(R.id.txtEmail)
        val txtPassword = findViewById<EditText>(R.id.txtPassword)
        val txtConfirmPassword = findViewById<EditText>(R.id.txtConfirmPassword)
        val btnSignUp = findViewById<Button>(R.id.btnSignUp)

        findViewById<ImageView>(R.id.btnClose).setOnClickListener{
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }

        btnSignUp.setOnClickListener {
            val rName = txtName.text.toString()
            val rMobileNumber = txtMobileNUmber.text.toString().toInt()
            val rEmail = txtEmail.text.toString()
            val rPassword = txtPassword.text.toString()
            val rConfirmPassword = txtConfirmPassword.text.toString()
            if (rPassword == rConfirmPassword)
            {
                user = User(
                    Name = rName,
                    MobileNumber = rMobileNumber,
                    Email = rEmail,
                    PasswordHash = rPassword
                )
                registerDone()
            } else
            {
                Toast.makeText(this, "Password Doesn't Match!", Toast.LENGTH_LONG).show()
            }

        }
    }

    private fun registerDone()
    {

        CoroutineScope(Dispatchers.IO).launch {
            authService = AuthService()
            val response = authService.register(user)
            if (response.code == HttpURLConnection.HTTP_CONFLICT)
            {
                Looper.prepare()
                Toast.makeText(this@RegisterActivity, "User Already Exist!", Toast.LENGTH_LONG)
                    .show()
                Looper.loop()
                sendToLogin()
            }
            Looper.prepare()
            Toast.makeText(
                this@RegisterActivity,
                "User Registered Successfully!",
                Toast.LENGTH_LONG
            ).show()
            Looper.loop()

        }
    }

    private fun sendToLogin()
    {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}