package international.tourism.app

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.ismaeldivita.chipnavigation.ChipNavigationBar

class MainActivity : AppCompatActivity()
{
    private lateinit var bottomNavigationView: ChipNavigationBar

    private lateinit var placeFragment: PlaceFragment
    private lateinit var hotelFragment: HotelFragment
    private lateinit var homeFragment: HomeFragment
    private lateinit var bookingFragment: BookingFragment

    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottomNavBar)
        bottomNavigationView.setOnItemSelectedListener(::bottomItemNavSelected)

        homeFragment = HomeFragment()
        placeFragment = PlaceFragment()
        hotelFragment = HotelFragment()
        bookingFragment = BookingFragment()

        bottomNavigationView.setItemSelected(R.id.bottomNavHomeBtn, true)
        showHomeFragment()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean
    {
        menuInflater.inflate(R.menu.action_bar, menu)

        val itemLogin = menu.findItem(R.id.menuLogin)
        val itemLogout = menu.findItem(R.id.menuLogout)
        val itemProfile = menu.findItem(R.id.menuProfile)

        sharedPref = getSharedPreferences("tourism_pref", MODE_PRIVATE)
        val aEmail = sharedPref.getString("email", null)
        if (aEmail != null)

            itemLogin.isVisible = false
         else
        {
            itemProfile.isVisible = false
            itemLogout.isVisible = false
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        when (item.itemId)
        {
            R.id.menuProfile -> startActivity(Intent(this, ProfileActivity::class.java))
            R.id.menuAbout -> startActivity(Intent(this,AboutUsActivity::class.java))
            R.id.menuLogin -> startActivity(Intent(this, LoginActivity::class.java))
            R.id.menuLogout ->
            {
                sharedPref = getSharedPreferences("tourism_pref", MODE_PRIVATE)

                val spEditor = sharedPref.edit()
                spEditor.remove("id")
                spEditor.remove("email")
                spEditor.apply()

                startActivity(Intent(this, MainActivity::class.java))
            }
        }
        return true
    }

    private fun bottomItemNavSelected(itemId: Int): Boolean
    {
        when (itemId)
        {
            R.id.bottomNavHomeBtn -> showHomeFragment()
            R.id.bottomNavPlaceBtn -> showPlaceFragment()
            R.id.bottomNavHotelBtn -> showHotelFragment()
            R.id.bottomNavBookingBtn -> showBookingFragment()
            else -> return false
        }

        return true
    }

    private fun showHomeFragment()
    {
        val manager = supportFragmentManager.beginTransaction()
        manager.replace(R.id.fragmentContainer, homeFragment)
        manager.commit()
    }

    fun showPlaceFragment()
    {
        val manager = supportFragmentManager.beginTransaction()
        manager.replace(R.id.fragmentContainer, placeFragment)
        manager.commit()
        bottomNavigationView.setItemSelected(
            R.id.bottomNavPlaceBtn,
            isSelected = true
        )

    }

    private fun showHotelFragment()
    {
        val manager = supportFragmentManager.beginTransaction()
        manager.replace(R.id.fragmentContainer, hotelFragment)
        manager.commit()
    }

    private fun showBookingFragment()
    {
        val manager = supportFragmentManager.beginTransaction()
        manager.replace(R.id.fragmentContainer, bookingFragment)
        manager.commit()
    }
}