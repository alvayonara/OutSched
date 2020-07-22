package com.alvayonara.outsched.ui.onboarding

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IntegerRes
import androidx.annotation.StringRes
import androidx.viewpager.widget.PagerAdapter
import com.alvayonara.outsched.R
import kotlinx.android.synthetic.main.item_on_boarding.view.*

class OnBoardingPagerAdapter(private val context: Context) : PagerAdapter() {

    companion object {
        @IntegerRes
        private val onBoardingImageViews = intArrayOf(
            R.drawable.ic_on_boarding_jogging,
            R.drawable.ic_on_boarding_map,
            R.drawable.ic_on_boarding_date_picker,
            R.drawable.ic_on_boarding_notification
        )

        @StringRes
        private val onBoardingTitles = intArrayOf(
            R.string.on_boarding_name_1,
            R.string.on_boarding_name_2,
            R.string.on_boarding_name_3,
            R.string.on_boarding_name_4
        )

        @StringRes
        private val onBoardingDescriptions = intArrayOf(
            R.string.on_boarding_desc_1,
            R.string.on_boarding_desc_2,
            R.string.on_boarding_desc_3,
            R.string.on_boarding_desc_4
        )
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val view = layoutInflater.inflate(R.layout.item_on_boarding, container, false) as View
        view.iv_on_boarding.setImageResource(onBoardingImageViews[position])
        view.tv_title.setText(onBoardingTitles[position])
        view.tv_description.setText(onBoardingDescriptions[position])

        container.addView(view)

        return view
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun getCount(): Int = 4

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val view = `object` as View

        container.removeView(view)
    }
}