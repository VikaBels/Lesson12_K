package com.example.lesson12

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.lesson12.databinding.FragmentDetailBinding
import java.io.*
import kotlin.math.max

class DetailFragment : Fragment(), OnFragmentSendDataListener {
    private var editTextFile: EditText? = null

    private var nameFile: String = ""

    private var bindingDetail: FragmentDetailBinding? = null

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
        bindingDetail = FragmentDetailBinding.inflate(layoutInflater)

        editTextFile = bindingDetail?.editTextFile

        openText()

        return bindingDetail?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindingDetail?.btnSave?.setOnClickListener {
            saveText()

            fragmentSendDataListener?.onFinishDetailFragment()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bindingDetail = null
    }

    override fun onDestroy() {
        super.onDestroy()
        editTextFile = null
    }

    override fun onDetach() {
        super.onDetach()
        fragmentSendDataListener = null
    }

    private fun checkingNameForUniqueness(currentFileName: String): String {
        val directory = File("${requireContext().filesDir}$KEY_FOLDER_NAME")
        val files: Array<File> = directory.listFiles() as Array<File>

        var secondPathFirstLine: String
        var arrForPattern: Array<String>

        var newNameFile = currentFileName
        var maxNumber = 0

        for (i in files.indices) {
            val file = files[i]
            if (file.name.contains(currentFileName)) {
                secondPathFirstLine =
                    file.name.substring(currentFileName.length, file.name.length)

                if (secondPathFirstLine.contains(Regex("\\(\\d+\\)$"))) {
                    arrForPattern = secondPathFirstLine.split(Regex("[()]")).toTypedArray()
                    arrForPattern[1].toIntOrNull()?.let { maxNumber = max(maxNumber, it) }
                }

                if (secondPathFirstLine.trim().isEmpty() && maxNumber == 0) {
                    newNameFile = "$currentFileName (1)"
                }
            }
        }

        if (maxNumber != 0) {
            newNameFile = currentFileName + " (${maxNumber + 1})"
        }

        return newNameFile
    }

    private fun saveText() {
        if(nameFile == ""){
            nameFile = TXT_NULL
        }
        val currentFile = File("${requireContext().filesDir}$KEY_FOLDER_NAME/$nameFile")
        var renameFile = currentFile
        val newNameFile: String

        var allFileText = editTextFile?.text.toString()
        var firstLine = allFileText.split(SLASH_N)[0]

        if (firstLine == TXT_EMPTY) {
            firstLine = DEFAULT_NAME_FILE
            editTextFile?.setText(firstLine + editTextFile?.text)
        }

        if (nameFile != firstLine) {
            newNameFile = checkingNameForUniqueness(firstLine)
            renameFile = File("${requireContext().filesDir}$KEY_FOLDER_NAME/$newNameFile")
            try {
                allFileText =
                    newNameFile + SLASH_N + editTextFile?.text.toString().split(SLASH_N)[1]
            } catch (e: Exception) {
                allFileText = newNameFile
            }
        } else {
            allFileText = editTextFile?.text.toString()
        }

        try {
            FileOutputStream(currentFile).use { oStream ->
                oStream.write(allFileText.toByteArray())

                currentFile.renameTo(renameFile)
            }
        } catch (fnfe: FileNotFoundException) {
            fnfe.printStackTrace()
        }
    }

    private fun openText() {
        nameFile = arguments?.getString(KEY_TRANSFER_NAME, TXT_EMPTY).toString()

        val file = File("${requireContext().filesDir}$KEY_FOLDER_NAME/$nameFile")
        try {
            FileInputStream(file).use { iStream ->
                val byte = ByteArray(iStream.available())
                iStream.read(byte)
                val t = String(byte)
                editTextFile?.setText(t, TextView.BufferType.EDITABLE)
                println(t)
            }
        } catch (fnfe: FileNotFoundException) {
            fnfe.printStackTrace()
        }
    }

    override fun onSendData(data: String?) {}

    override fun onFinishDetailFragment() {}
}