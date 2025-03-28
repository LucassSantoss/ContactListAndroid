package com.lucas.contactlist

data class Contact(
    var id: Int? = -1,
    var name: String = "",
    var address: String = "",
    var phone: String = "",
    var email: String = ""
)
