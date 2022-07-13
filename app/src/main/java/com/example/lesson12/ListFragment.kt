package com.example.lesson12

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lesson12.databinding.FragmentListBinding
import java.io.File
import java.io.IOException
import java.util.*

class ListFragment : Fragment(), OnFragmentSendDataListener {
    companion object {
        const val KEY_FOLDER_NAME = "/documents"
        const val TXT_NULL: String = "NULL"
        const val TXT_EMPTY: String = ""
        const val DEFAULT_NAME_FILE: String = "Document"
        const val SLASH_N: String = "\n"
        const val TAG_FOR_LIST: String = "tagListFragment"
        const val TAG_FOR_DETAIL: String = "tagDetailFragment"
        const val KEY_TRANSFER_NAME: String = "token"
    }

    private var fileList: ListView? = null
    private var btnAdd: Button? = null

    private var listFiles = ArrayList<CurrentFile>()

    private lateinit var adapter: FileItemAdapter
    private lateinit var b: FragmentListBinding

    private var fragmentSendDataListener: OnFragmentSendDataListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentSendDataListener = context as? OnFragmentSendDataListener
            ?: error("$context${resources.getString(R.string.exceptionInterface)}")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        b = FragmentListBinding.inflate(layoutInflater)

        findAllFiles()

        setUpAdapter()

        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.btnAdd).setOnClickListener {
            val newFile = File("${requireContext().filesDir}$KEY_FOLDER_NAME/$DEFAULT_NAME_FILE")
            try {
                newFile.createNewFile()
            } catch (ex: IOException) {
                println(ex)
            }
            fragmentSendDataListener!!.onSendData(TXT_NULL)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fileList = null
        btnAdd = null
    }

    override fun onDetach() {
        super.onDetach()
        fragmentSendDataListener = null
    }

    override fun onSendData(data: String?) {}

    private fun setUpAdapter() {
        adapter = FileItemAdapter(requireContext(), listFiles, fragmentSendDataListener)

        b.filesList.adapter = adapter
        b.filesList.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun findAllFiles() {
        val dir = File("${requireContext().filesDir}$KEY_FOLDER_NAME")
        val path = dir.toString()
        val created = dir.mkdir()
        if (!created) {
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
                            Date(files[i].lastModified()).toString()
                        )
                    )
                }
            }
        }
    }
}