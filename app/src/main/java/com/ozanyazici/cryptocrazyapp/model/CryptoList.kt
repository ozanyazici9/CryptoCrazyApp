package com.ozanyazici.cryptocrazyapp.model

// BU model paketindeki iki sınıfı json to kotlin plugin i ile oluşturduk.
// İki tane sınıf olmasının sebebi normalde biz api den gelen veriyi List<CryptoListItem> diyerek de alabilirdik
// burada bunu döndüren ayrıca bir sınıf yaratmış bizde çağrı yaptıktan sonra CryptoList diyebiliriz sadece bu zaten bize o listeyi döndürecek.

class CryptoList : ArrayList<CryptoListItem>()