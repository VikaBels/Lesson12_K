package com.example.lesson12

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lesson12.databinding.ListItemBinding

class FileItemAdapter(
    private val context: Context,
    private val fileItemList: List<CurrentFile>,
    private val listenerForFragment: OnFragmentSendDataListener?
) : RecyclerView.Adapter<FileItemAdapter.FileItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileItemViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return FileItemViewHolder(binding, listenerForFragment)
    }

    override fun onBindViewHolder(holder: FileItemViewHolder, position: Int) {
        val fileItem = fileItemList[position]
        holder.bind(fileItem)
    }

    override fun getItemCount(): Int {
        return fileItemList.size
    }

    class FileItemViewHolder(
        private val binding: ListItemBinding,
        private val listenerForFragment: OnFragmentSendDataListener?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(fileItem: CurrentFile) {
            binding.name.text = fileItem.getName()
            binding.data.text = fileItem.getData()
            binding.oneItem.setOnClickListener {
                listenerForFragment?.onSendData(fileItem.getName().toString())
            }
        }
    }
}