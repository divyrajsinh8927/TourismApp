package international.tourism.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import international.tourism.app.adapter.AboutUsSlider

class AboutUsActivity : AppCompatActivity()
{
    private lateinit var fragments: Array<Fragment>
    private lateinit var tabIcon: Array<Int>

    private lateinit var tabs: TabLayout
    private lateinit var viewPager: ViewPager2

    private lateinit var tabLayoutMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aboutus)

        supportActionBar!!.hide()

        findViewById<ImageView>(R.id.btnClose).setOnClickListener{startActivity(Intent(this,MainActivity::class.java))}

        setupViewPager()
    }

    private fun setupViewPager()
    {
        fragments = arrayOf(
            AboutUsFragment1(),
            AboutUsFragment2(),
            AboutUsFragment3()
        )


        tabIcon = arrayOf(R.drawable.ic_about_dot,R.drawable.ic_about_dot,R.drawable.ic_about_dot)
        tabs = findViewById(R.id.tabs)
        viewPager = findViewById(R.id.viewPager)
        viewPager.adapter = AboutUsSlider(this, fragments)

        tabLayoutMediator = TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.setIcon(tabIcon[position])
//            tab.text = tabIcon[position]
        }
        tabLayoutMediator.attach()
    }
}