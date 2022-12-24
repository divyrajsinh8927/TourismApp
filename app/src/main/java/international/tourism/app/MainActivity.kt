package international.tourism.app

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

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

        showHomeFragment()
    }
    private fun bottomItemNavSelected(item: MenuItem): Boolean
    {
        when(item.itemId)
        {
            R.id.btnHome -> showHomeFragment()
            R.id.btnPlace -> showPlaceFragment()
            R.id.btnHotel -> showHotelFragment()
            R.id.btnBookings -> showBookingFragment()
            else -> return false
        }

        return true
    }

    private fun showHomeFragment()
    {
        val manager = supportFragmentManager.beginTransaction()
        manager.replace(R.id.fragmentContainer,homeFragment)
        manager.commit()
    }

    private fun showPlaceFragment()
    {
        val manager = supportFragmentManager.beginTransaction()
        manager.replace(R.id.fragmentContainer,placeFragment)
        manager.commit()
    }

    private fun showHotelFragment()
    {
        val manager = supportFragmentManager.beginTransaction()
        manager.replace(R.id.fragmentContainer,hotelFragment)
        manager.commit()
    }

    private fun showBookingFragment()
    {
        val manager = supportFragmentManager.beginTransaction()
        manager.replace(R.id.fragmentContainer,homeFragment)
        manager.commit()
    }




}