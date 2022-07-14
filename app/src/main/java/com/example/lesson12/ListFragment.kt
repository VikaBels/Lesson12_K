package com.example.lesson12

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lesson12.databinding.FragmentListBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ListFragment : Fragment(), OnFragmentSendDataListener {
    private var listFiles = ArrayList<CurrentFile>()

    private lateinit var adapter: FileItemAdapter
    private var bindingList: FragmentListBinding? = null

    private var fragmentSendDataListener: OnFragmentSendDataListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentSendDataListener = context as? OnFragmentSendDataListener
            ?: error("$context${resources.getString(R.string.exceptionInterface)}")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): ConstraintLayout? {
        bindingList = FragmentListBinding.inflate(layoutInflater)

        findAllFiles()

        setUpAdapter()

        return bindingList?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindingList?.btnAdd?.setOnClickListener {
           createFile()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bindingList = null
    }

    override fun onDetach() {
        super.onDetach()
        fragmentSendDataListener = null
    }

    private fun createFile(){
        val newFile = File("${requireContext().filesDir}$KEY_FOLDER_NAME/$DEFAULT_NAME_FILE")
        try {
            newFile.createNewFile()
        } catch (ex: IOException) {
            println(ex)
        }
        fragmentSendDataListener?.onSendData(null)
    }

    private fun setUpAdapter() {
        adapter = FileItemAdapter(requireContext(), listFiles, fragmentSendDataListener)

        bindingList?.filesList?.adapter = adapter
        bindingList?.filesList?.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun findAllFiles() {
        val dir = File("${requireContext().filesDir}$KEY_FOLDER_NAME")
        val path = dir.toString()
        if (!dir.mkdir()) {
            val directory = File(path)
            val files: Array<File> = directory.listFiles()
            if (files.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.noFileInFolder),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                for (i in files.indices) {
                    listFiles.add(
                        CurrentFile(
                            files[i].name,
                            SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date(files[i].lastModified()))
                        )
                    )
                }
            }
        }
    }

    override fun onSendData(data: String?) {}

    override fun onFinishDetailFragment() {}
}