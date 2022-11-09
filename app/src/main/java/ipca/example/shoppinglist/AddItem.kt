/*
 * AlertConfirm.kt
 * AquaTec
 *
 * Created by Lourenço Gomes on 23/12/21, 12:25.
 * Copyright © 2021 Lourenço Gomes. All rights reserved.
 */

package ipca.example.shoppinglist

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.*
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.FragmentManager

class AddItem : DialogFragment() {

    var okCallback : ((String)-> Unit)? = null

    companion object{
        fun newInstance(): AddItem {
            val frag = AddItem()
            return frag
        }

        fun show(fm: FragmentManager, okCallback: (String)-> Unit) : AddItem {
            val alertDialog: AddItem = AddItem.newInstance()
            alertDialog.show(fm, "fragment_add_item")
            alertDialog.okCallback = { gender ->
                okCallback.invoke(gender)
                alertDialog.dismiss()
            }
            return alertDialog
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialogView: View = layoutInflater.inflate(R.layout.fragment_add_item, null)

        val builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(false)
        builder.setView(dialogView)

        val buttonAdd = dialogView.findViewById<Button>(R.id.buttonAdd)
        val editTextItemDescription  = dialogView.findViewById<EditText>(R.id.editTextItemDescription)

        buttonAdd.setOnClickListener {
            okCallback?.invoke(editTextItemDescription.text.toString())
        }

        return builder.create()
    }

    override fun onResume() {
        val params: WindowManager.LayoutParams? = dialog?.window?.attributes
        params?.width = WindowManager.LayoutParams.WRAP_CONTENT
        params?.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog?.window?.attributes = params as WindowManager.LayoutParams
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        super.onResume()
    }


}