package uz.drop.htmlparsing.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.drop.htmlparsing.data.Data
import uz.drop.htmlparsing.databinding.ItemBinding

class Adapter(private val list: List<Data>) : RecyclerView.Adapter<Adapter.VH>() {


    inner class VH(private val binding: ItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Data) {
            binding.key.text = data.key
            binding.value.text = data.value
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemBinding.inflate(LayoutInflater.from(parent.context))
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}