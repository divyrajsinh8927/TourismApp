package international.tourism.app

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity


class SplashActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar!!.hide()

        Handler(Looper.getMainLooper()).postDelayed({
            doSomething()
        }, 2000)
    }

    private fun doSomething()
    {
        startActivity(Intent(this,MainActivity::class.java))
    }


}