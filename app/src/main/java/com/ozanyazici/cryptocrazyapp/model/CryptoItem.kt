package com.ozanyazici.cryptocrazyapp.model

// Çalışan bir API olmadığı için tıklanılan her crypto da aynı bilgileri göstereceğiz.

data class CryptoItem(
    val id: String,
    val logo_url: String,
    val name: String
)