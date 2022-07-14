package com.example.lesson12

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.example.lesson12.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), OnFragmentSendDataListener {
    private lateinit var bindingMain: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .add(R.id.nav_container, ListFragment(), TAG_FOR_LIST).commit()
    }

    override fun onSendData(data: String?) {
        bindingMain = ActivityMainBinding.inflate(layoutInflater)

        if (bindingMain.navContainerHorizontal != null){
            goToDetailFragment(data,R.id.nav_container_horizontal)
        } else {
            goToDetailFragment(data,R.id.nav_container)
        }
    }

    override fun onFinishDetailFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        val fragment = ListFragment()
        transaction
            .apply { add(R.id.nav_container, fragment, TAG_FOR_LIST) }
            .commit()
        //supportFragmentManager.popBackStack()
    }

    private fun goToDetailFragment(data:String?,containerId: Int){
        val transaction = supportFragmentManager.beginTransaction()
        val fragment = DetailFragment()
        fragment.arguments = bundleOf(KEY_TRANSFER_NAME to data)
        transaction
            .apply { add(containerId, fragment, TAG_FOR_DETAIL) }
            .addToBackStack(null)
            .commit()
    }
}