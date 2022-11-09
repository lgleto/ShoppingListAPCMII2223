package ipca.example.shoppinglist

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ipca.example.shoppinglist.databinding.FragmentFirstBinding
import ipca.example.shoppinglist.databinding.RowItemBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList


class ShoppingListFragment : Fragment() {

    var items = arrayListOf<Item>()

    private var _binding: FragmentFirstBinding? = null

    private val binding get() = _binding!!

    val adapter = ItemsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.recycleViewItems.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recycleViewItems.adapter = adapter
        binding.recycleViewItems.itemAnimator = DefaultItemAnimator()

        binding.fabAdd.setOnClickListener {
            AddItem.show(childFragmentManager) {
                lifecycleScope.launch (Dispatchers.IO){
                    AppDatabase.getDatabase(requireContext()).itemDao().insert(
                        Item(
                            UUID.randomUUID().toString(),
                            it,
                            Date(),
                            1.0
                        )
                    )
                }
            }
        }




    }

    override fun onResume() {
        super.onResume()
        AppDatabase.getDatabase(requireContext())
            .itemDao()
            .getAll().observe(viewLifecycleOwner, Observer {
                items = it as ArrayList<Item>
                adapter.notifyDataSetChanged()
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    inner class ItemsAdapter : RecyclerView.Adapter<ItemsAdapter.ViewHolder>(){

        inner class ViewHolder(binding: RowItemBinding) : RecyclerView.ViewHolder(binding.root){
            val textViewDescription : TextView = binding.textViewDescription
            val textViewQtd : TextView = binding.textViewQtd
            val buttonPlus : Button = binding.buttonPlus
            val buttonMinus : Button = binding.buttonMinus
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                RowItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            var item = items[position]
            holder.apply {
                textViewDescription.text = item.description
                textViewQtd.text = "${item.qtd}"

                buttonPlus.setOnClickListener{
                    item.qtd += 1.0
                    lifecycleScope.launch(Dispatchers.IO) {
                        AppDatabase.getDatabase(requireContext()).itemDao().update(item)
                    }
                }

                buttonMinus.setOnClickListener{
                    item.qtd -= 1.0
                    lifecycleScope.launch(Dispatchers.IO) {
                        AppDatabase.getDatabase(requireContext()).itemDao().update(item)
                    }
                }
            }

        }

        override fun getItemCount(): Int {
            return items.size
        }

    }

}