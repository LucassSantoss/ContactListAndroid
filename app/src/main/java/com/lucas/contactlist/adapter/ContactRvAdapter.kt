package com.lucas.contactlist.adapter

import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.recyclerview.widget.RecyclerView
import com.lucas.contactlist.R
import com.lucas.contactlist.databinding.TileContactBinding
import com.lucas.contactlist.model.Contact
import com.lucas.contactlist.ui.OnContactClickListener

class ContactRvAdapter(
    private val contactList: MutableList<Contact>,
    private val onContactClickListener: OnContactClickListener
): RecyclerView.Adapter<ContactRvAdapter.ContactViewHolder>() {
    inner class ContactViewHolder(tcb: TileContactBinding): RecyclerView.ViewHolder(tcb.root) {
        val nameTv: TextView = tcb.nameTv
        val emailTv: TextView = tcb.emailTv

        init {
            tcb.root.setOnCreateContextMenuListener {
                menu, v, menuInfo ->
                (onContactClickListener as AppCompatActivity).menuInflater.inflate(R.menu.context_menu_main, menu)
                menu.findItem(R.id.edit_contact_mi).setOnMenuItemClickListener {
                    onContactClickListener.onEditContactMenuItemClick(adapterPosition)
                    true
                }
                menu.findItem(R.id.remove_contact_mi).setOnMenuItemClickListener {
                    onContactClickListener.onRemoveContactMenuItemClick(adapterPosition)
                    true
                }
            }
            tcb.root.setOnClickListener {
                onContactClickListener.onContactClick(adapterPosition)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder =
        ContactViewHolder(TileContactBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        )

    override fun getItemCount(): Int = contactList.size


    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        contactList[position].let {
            contact -> with(holder) {
                nameTv.text = contact.name
                emailTv.text = contact.email
            }
        }
    }
}