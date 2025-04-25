package com.lucas.contactlist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.lucas.contactlist.R
import com.lucas.contactlist.databinding.TileContactBinding
import com.lucas.contactlist.model.Contact
import com.lucas.contactlist.ui.OnContactClickListener

class ContactRvAdapter(
    private val contactList: MutableList<Contact>,
    private val onContactClickListener: OnContactClickListener
): RecyclerView.Adapter<ContactRvAdapter.ContactViewHolder>() {
    inner class ContactViewHolder(tcb: TileContactBinding): RecyclerView.ViewHolder(tcb.root){
        val nameTv: TextView = tcb.nameTv
        val emailTv: TextView = tcb.emailTv

        init {
            // Criando o menu de contexto para cada célula associada a um novo holder
            tcb.root.setOnCreateContextMenuListener{ menu, v, menuInfo ->
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
            // Setando o listener de clique curto na célula associada a um novo holder
            tcb.root.setOnClickListener { onContactClickListener.onContactClick(adapterPosition) }
        }
    }

    // Chamado somente quando um novo holder precisa ser criado
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ContactViewHolder = ContactViewHolder(
        TileContactBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    // Chamado sempre que os dados de um holder precisam ser preenchidos ou trocados
    override fun onBindViewHolder(
        holder: ContactViewHolder,
        position: Int
    ) {
        contactList[position].let { contact ->
            with(holder) {
                nameTv.text = contact.name
                emailTv.text = contact.email
            }
        }
    }

    // Retorna o número de elementos no data source
    override fun getItemCount(): Int = contactList.size
}