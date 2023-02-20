package international.tourism.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import international.tourism.app.models.ForgotPasswordOtp
import international.tourism.app.services.AuthService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection

class ResetPasswordActivity : AppCompatActivity()
{
    private lateinit var txtOtp: EditText
    private lateinit var txtPassword: EditText
    private lateinit var txtConfirmPassword: EditText
    private lateinit var forgotPasswordOtp: ForgotPasswordOtp
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resetpassword)

        val email = intent.getStringExtra("email").toString()
        txtOtp = findViewById(R.id.txtOtp)
        txtPassword = findViewById(R.id.txtPassword)
        txtConfirmPassword = findViewById(R.id.txtConfirmPassword)

        findViewById<ImageView>(R.id.btnClose).setOnClickListener{
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }

        findViewById<Button>(R.id.btnResetPassword).setOnClickListener {
            forgotPasswordOtp = ForgotPasswordOtp(otp = txtOtp.text.toString(), email = email, password = txtPassword.text.toString())
            if(txtPassword.text.toString() != txtConfirmPassword.text.toString())
            {
                Toast.makeText(this@ResetPasswordActivity, "Password Doesn't match!", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            resetPassword()
        }
    }

    private fun resetPassword()
    {
        val authService = AuthService()
        CoroutineScope(Dispatchers.IO).launch {

            val response = authService.resetPassword(forgotPasswordOtp)

            if (response.code == HttpURLConnection.HTTP_FORBIDDEN)
            {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@ResetPasswordActivity, "Your Otp Has Expired!", Toast.LENGTH_SHORT)
                    .show()
            }
            return@launch
            }
            if (response.code == HttpURLConnection.HTTP_INTERNAL_ERROR)
            {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ResetPasswordActivity, "Your Otp is Wrong", Toast.LENGTH_SHORT)
                        .show()
                }
                return@launch
            }

            withContext(Dispatchers.Main) {
                Toast.makeText(this@ResetPasswordActivity, "Password change successfully", Toast.LENGTH_SHORT)
                    .show()
            }
            startActivity(Intent(this@ResetPasswordActivity,LoginActivity::class.java))
        }
    }
}