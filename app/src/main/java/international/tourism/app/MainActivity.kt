package international.tourism.app

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.andremion.floatingnavigationview.FloatingNavigationView
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

    private lateinit var mFloatingNavigationView: FloatingNavigationView
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPref = getSharedPreferences("tourism_pref", MODE_PRIVATE)
        val aEmail = sharedPref.getString("email",null)
        if(aEmail == null)
            sendToHome()

        /*  Start Navigation view */

        mFloatingNavigationView =
            findViewById<View>(R.id.floating_navigation_view) as FloatingNavigationView
        mFloatingNavigationView!!.setOnClickListener { mFloatingNavigationView!!.open() }
        mFloatingNavigationView!!.setNavigationItemSelectedListener {item ->

             when (item.itemId) {
                R.id.about -> {
                    startActivity(Intent(this, AboutusActivity::class.java))
                    true
                }
                R.id.share ->{
                    val intent= Intent()
                    intent.action=Intent.ACTION_SEND
                    intent.putExtra(Intent.EXTRA_TEXT,"Hey Check out this Great app:")
                    intent.type="text/plain"
                    startActivity(Intent.createChooser(intent,"Share To:"))
                    true
                }
                 R.id.btnLogout ->{
                     sharedPref.edit().remove("email").apply()
                     sharedPref.edit().remove("id").apply()
                     startActivity(Intent(this,LoginActivity::class.java))
                 }
                 R.id.profile -> {
                     startActivity(Intent(this, ProfileActivity::class.java))
                     true
                 }
                else -> super.onOptionsItemSelected(item)
            }

            mFloatingNavigationView!!.close()
            true
        }
    }

    override fun onBackPressed() {
        if (mFloatingNavigationView!!.isOpened) {
            mFloatingNavigationView!!.close()
        } else {
            super.onBackPressed()
        }
    }

    /* End Navigation view */
    private fun sendToHome()
    {
        val intent = Intent(this,LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}