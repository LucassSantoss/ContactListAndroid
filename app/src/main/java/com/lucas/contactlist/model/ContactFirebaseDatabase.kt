package com.lucas.contactlist.model

import com.google.firebase.Firebase
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue

class ContactFirebaseDatabase: ContactDao {
    private val databaseReference = Firebase.database.getReference("contactList")
    private val contactList = mutableListOf<Contact>()

    init {
        databaseReference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val contact = snapshot.getValue<Contact>()
                contact?.let { newContact ->
                    if (!contactList.any { it.id == newContact.id }){
                        contactList.add(newContact)
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val contact = snapshot.getValue<Contact>()
                contact?.let { editedContact ->
                    contactList[contactList.indexOfFirst { it.id == editedContact.id }] = editedContact
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val contact = snapshot.getValue<Contact>()
                contact?.let {
                    contactList.remove(it)
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                // NSA
            }

            override fun onCancelled(error: DatabaseError) {
                // NSA
            }
        })

        databaseReference.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val contactMap = snapshot.getValue<Map<String, Contact>>()
                contactList.clear()
                contactMap?.values?.also {
                    contactList.addAll(it)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // NSA
            }

        })
    }

    override fun createContact(contact: Contact): Long {
        databaseReference.child(contact.id.toString()).setValue(contact)
        return 1L
    }

    override fun retrieveContact(id: Int) = contactList[contactList.indexOfFirst { it.id == id }]


    override fun retrieveContacts(): MutableList<Contact> {
        return contactList
    }

    override fun updateContact(contact: Contact): Int {
        databaseReference.child(contact.id.toString()).setValue(contact)
        return 1
    }

    override fun deleteContact(contact: Contact): Int {
        databaseReference.child(contact.id.toString()).removeValue()
        return 1
    }
}