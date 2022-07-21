package com.example.cashapp.models

class Transaction {
    var id: Int = 0
    var amount: Float = 0.0f
    var date: String? = null
    var user_id: Int = 0
    var category_id: Int = 0

    constructor(amount: Float, user_id: Int, category_id: Int) {
        this.amount = amount
        this.user_id = user_id
        this.category_id = category_id
    }
}