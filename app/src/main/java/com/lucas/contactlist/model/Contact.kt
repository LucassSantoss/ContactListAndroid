package com.lucas.contactlist.model

import android.os.Parcelable
import com.lucas.contactlist.model.Constant.INVALID_CONTACT_ID
import kotlinx.parcelize.Parcelize

@Parcelize
data class Contact(
    var id: Int? = INVALID_CONTACT_ID,
    var name: String = "",
    var address: String = "",
    var phone: String = "",
    var email: String = ""
): Parcelable