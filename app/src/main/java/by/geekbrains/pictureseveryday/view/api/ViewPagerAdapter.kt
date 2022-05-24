package by.geekbrains.pictureseveryday.view.api

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import by.geekbrains.pictureseveryday.domain.FragmentsFactory

class ViewPagerAdapter(fm: Fragment, private val fragments: List<FragmentsFactory>) :
    FragmentStateAdapter(fm) {

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int) = fragments[position].factoryMethod()
}