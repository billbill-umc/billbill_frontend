package com.example.billbill_template.ui.message

interface MessageView {
    fun onGetMessagesSuccess(result: GetChattingsData)
    fun onGetMessagesFailure(message: String)
}