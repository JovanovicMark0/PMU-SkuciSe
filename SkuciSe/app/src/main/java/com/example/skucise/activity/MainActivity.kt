package com.example.skucise

import kotlinx.android.synthetic.main.activity_login.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.skucise.activity.Login
import com.example.skucise.fragments.SignUp

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val adapter = MyViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(Login(), " Prijavi se ")
        adapter.addFragment(SignUp(), " Registruj se ")
        view_pager.adapter = adapter
        tablayout.setupWithViewPager(view_pager)

    }

    class MyViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {

        private val fragmentList: MutableList<Fragment> = ArrayList()
        private val titleList: MutableList<String> = ArrayList()

        override fun getItem(position: Int): Fragment {
            return fragmentList[position]
        }

        override fun getCount(): Int {
            return fragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            fragmentList.add(fragment)
            titleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titleList[position]
        }

    }

}