package com.example.lesson12

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.example.lesson12.ListFragment.Companion.KEY_TRANSFER_NAME
import com.example.lesson12.ListFragment.Companion.TAG_FOR_DETAIL
import com.example.lesson12.ListFragment.Companion.TAG_FOR_LIST

class MainActivity : AppCompatActivity(), OnFragmentSendDataListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .add(R.id.nav_container, ListFragment(), TAG_FOR_LIST).commit()
    }

    override fun onSendData(data: String?) {
        if (findViewById<View>(R.id.nav_container_horizontal) != null){
            goToDifferentFragment(data,R.id.nav_container_horizontal)
        } else {
            goToDifferentFragment(data,R.id.nav_container)
        }
    }

    private fun goToDifferentFragment(data:String?,containerId: Int){
        val transaction = supportFragmentManager.beginTransaction()
        val fragment = DetailFragment()
        fragment.arguments = bundleOf(KEY_TRANSFER_NAME to data)
        transaction.apply { add(containerId, fragment, TAG_FOR_DETAIL) }
            .commit()
    }
}