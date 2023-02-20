package international.tourism.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import international.tourism.app.models.User
import international.tourism.app.services.AuthService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection

class ForgotPasswordActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgotpassword)

        findViewById<ImageView>(R.id.btnClose).setOnClickListener{
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }

        findViewById<Button>(R.id.btnSendOtp).setOnClickListener {
            verifyEmailAndSendOtp()
        }

    }

    private fun verifyEmailAndSendOtp()
    {
        val email = findViewById<EditText>(R.id.txtEmail)
        val user = User(Email = email.text.toString())
        CoroutineScope(Dispatchers.IO).launch {
            val authService = AuthService()
            val response = authService.verifyEmailAndSendOtp(user)

            if (response.code == HttpURLConnection.HTTP_NOT_FOUND)
            {
                withContext(Dispatchers.Main){
                    Toast.makeText(this@ForgotPasswordActivity, "Wrong Email", Toast.LENGTH_SHORT).show()
                }
                return@launch
            }

            if (response.code == HttpURLConnection.HTTP_INTERNAL_ERROR)
            {
                withContext(Dispatchers.Main){
                    Toast.makeText(this@ForgotPasswordActivity, "Otp Could Not Send By Server", Toast.LENGTH_SHORT).show()
                }
                return@launch
            }
            withContext(Dispatchers.Main) {
                val intent = Intent(this@ForgotPasswordActivity, ResetPasswordActivity::class.java)
                intent.putExtra("email", user.Email)
                startActivity(intent)
            }
        }
    }
}