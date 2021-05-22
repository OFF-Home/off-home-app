package com.offhome.app.ui.profile



import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.offhome.app.R

/**
 * Array which contains the titles of the tabs for all the pages in the pager
 */
private val TAB_TITLES = arrayOf(
    R.string.tab_text_1,
    R.string.tab_text_2,
    R.string.tab_text_3
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    /**
     * Getter of the fragment of a specific item in the pager
     *
     * @param position indicates which item. (0, 1, or 2)
     * @return the fragment of the specified item
     */
    override fun getItem(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return when (position) {
            0 -> ProfileMyActivitiesFragment()
            1 -> ProfileAboutMeFragment()
            else -> ProfileSettingsFragment()
            //else -> ProfilePlaceholderFragment() // Placeholder, change when creating the settings fragment
        }
    }

    /**
     * getter of the title of a specific page
     *
     * @param position indicates which item. (0, 1, or 2)
     * @return the title of the specified page (a CharSequence)
     */
    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    /**
     * gets the number of tabs
     *
     * @return number of tabs
     */
    override fun getCount(): Int {
        return TAB_TITLES.size
    }
}
