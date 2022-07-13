package com.example.lesson12

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.lesson12.ListFragment.Companion.DEFAULT_NAME_FILE
import com.example.lesson12.ListFragment.Companion.KEY_FOLDER_NAME
import com.example.lesson12.ListFragment.Companion.KEY_TRANSFER_NAME
import com.example.lesson12.ListFragment.Companion.SLASH_N
import com.example.lesson12.ListFragment.Companion.TAG_FOR_LIST
import com.example.lesson12.ListFragment.Companion.TXT_EMPTY
import java.io.*
import kotlin.math.max

class DetailFragment : Fragment() {
    private var editTextFile: EditText? = null
    private var btnSave: Button? = null

    private var nameFile: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_detail, container, false)

        findViewsById(view)

        openText()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnSave?.setOnClickListener {
            saveText()

            val fragment = ListFragment()
            parentFragmentManager.beginTransaction()
                .apply { add(R.id.nav_container, fragment, TAG_FOR_LIST) }
                .commit()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        editTextFile = null
        btnSave = null
    }

    private fun findViewsById(view: View) {
        editTextFile = view.findViewById(R.id.editTextFile)
        btnSave = view.findViewById(R.id.btnSave)
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
}