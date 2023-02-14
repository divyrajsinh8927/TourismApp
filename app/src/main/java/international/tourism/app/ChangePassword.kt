package international.tourism.app

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import international.tourism.app.models.User
import international.tourism.app.services.AuthService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.HttpURLConnection

class ChangePassword : AppCompatActivity()
{
    private lateinit var sharedPref: SharedPreferences
    private lateinit var authService: AuthService
    private lateinit var user: User

    private lateinit var txtEmail: EditText
    private lateinit var txtPassword: EditText
    private lateinit var txtNewPassword: EditText
    private lateinit var txtConfirmPassword: EditText
    private lateinit var btnConfirm: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        sharedPref = getSharedPreferences("tourism_pref", MODE_PRIVATE)
        val sharedPrefEmail = sharedPref.getString("email", null)
        val sharedPrefId = sharedPref.getString("id", null)!!.toInt()


        txtEmail = findViewById(R.id.txtEmail)
        txtEmail.isEnabled = false
        txtPassword = findViewById(R.id.txtOldPassword)
        txtNewPassword = findViewById(R.id.txtNewPassword)
        txtConfirmPassword = findViewById(R.id.txtNewConfirmPassword)
        btnConfirm = findViewById(R.id.btnConfirm)

        txtEmail.setText(sharedPrefEmail)
        txtEmail.setSelection(txtEmail.length())

        btnConfirm.setOnClickListener()
        {
            val email = txtEmail.text.toString()
            val password = txtPassword.text.toString()
            val newPassword = txtNewPassword.text.toString()
            val newConfirmPassword = txtConfirmPassword.text.toString()

            user = User(Id = sharedPrefId, Email = email, PasswordHash = password)
            confirmData(newPassword, newConfirmPassword)
        }
    }

    private fun confirmData(newPassword: String, newConfirmPassword: String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            authService = AuthService()
            val response = authService.login(user)
            if (response.code == HttpURLConnection.HTTP_NOT_FOUND)
            {
                Looper.prepare()
                Toast.makeText(this@ChangePassword, "Wrong email or password", Toast.LENGTH_LONG)
                    .show()
                Looper.loop()
                return@launch
            }
            if (newPassword != newConfirmPassword)
            {
                Looper.prepare()
                Toast.makeText(this@ChangePassword, "Password Doesn't Match", Toast.LENGTH_LONG)
                    .show()
                Looper.loop()
            }
            setNewPassword(newPassword)

        }
    }

    private fun setNewPassword(newPassword: String)
    {
        val sharedPrefId = sharedPref.getString("id", null)!!.toInt()
        user = User(Id = sharedPrefId, PasswordHash = newPassword)
        val updateResponse = authService.changePassword(user)
        if (updateResponse.code == HttpURLConnection.HTTP_OK)
        {
            Looper.prepare()
            Toast.makeText(this@ChangePassword, "Password Is Changed", Toast.LENGTH_LONG).show()
            val spEditor = sharedPref.edit()
            spEditor.remove("id")
            spEditor.remove("email")
            spEditor.apply()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            Looper.loop()

        }
        Looper.prepare()
        Toast.makeText(this@ChangePassword, "Password Is Not Updated", Toast.LENGTH_LONG).show()
        Looper.loop()
    }
}