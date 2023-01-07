package international.tourism.app.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class AboutUsSlider(
    activity: FragmentActivity,
    private val fragments: Array<Fragment>
) : FragmentStateAdapter(activity)
{
    override fun getItemCount(): Int = fragments.size
    override fun createFragment(position: Int): Fragment = fragments[position]
}