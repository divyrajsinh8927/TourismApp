package international.tourism.app

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.gson.Gson
import international.tourism.app.models.User
import international.tourism.app.services.AuthService
import kotlinx.coroutines.*
import java.net.HttpURLConnection

class ProfileActivity : AppCompatActivity()
{
    private lateinit var sharedPref: SharedPreferences
    private lateinit var lblName: TextView
    private lateinit var lblMobile: TextView
    private lateinit var lblEmail: TextView
    private lateinit var btnBack: Button
    private lateinit var  authService: AuthService
    private lateinit var user: User
    private lateinit var btnChangePassword : ConstraintLayout


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        sharedPref = getSharedPreferences("tourism_pref", MODE_PRIVATE)
        val id = sharedPref.getString("id",null)

        lblName = findViewById(R.id.lblName)
        lblMobile = findViewById(R.id.lblMobileNumber)
        lblEmail = findViewById(R.id.lblEmail)
        btnBack = findViewById(R.id.btnBack)
        user = User(Id = id!!.toInt())
        btnChangePassword = findViewById(R.id.btnChangePassword)
        btnChangePassword.setOnClickListener{startActivity(Intent(this,ChangePassword::class.java))}
        populateProfileData()
    }

    private fun populateProfileData()
    {
        CoroutineScope(Dispatchers.IO).launch {
            authService = AuthService()
            val response = authService.getUser(user)
            if (response.code == HttpURLConnection.HTTP_OK)
            {
                user = Gson().fromJson(response.message, User::class.java)

                val name = user.Name
                val email = user.Email
                val mobileNumber = user.MobileNumber

                withContext(Dispatchers.Main){
                    lblName.text = name
                    lblEmail.text = email
                    lblMobile.text = mobileNumber.toString()
                }
            }
        }
    }
}