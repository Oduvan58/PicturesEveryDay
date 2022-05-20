package by.geekbrains.pictureseveryday.view.viewpager

import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

private const val TODAY = 0
private const val YESTERDAY = 1
private const val BEFORE_YESTERDAY = 2

class ViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    private val fragments = arrayOf(
        TodayFragment.newInstance(),
        YesterdayFragment.newInstance(),
        BeforeYesterdayFragment.newInstance()
    )

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int) = when (position) {
        0 -> fragments[TODAY]
        1 -> fragments[YESTERDAY]
        2 -> fragments[BEFORE_YESTERDAY]
        else -> fragments[TODAY]
    }
}