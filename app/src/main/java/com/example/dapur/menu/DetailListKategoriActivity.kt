package com.example.dapur.menu

import android.content.ContentValues
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.dapur.databinding.ActivityDetailListKategoriBinding
import com.example.dapur.db.DatabaseContract
import com.example.dapur.db.PesananHelper
import com.example.dapur.key.Key
import com.example.dapur.model.ItemKategori
import com.example.dapur.pesanan.ListPesananActivity
import com.example.dapur.pesanan.PesananActivity
import java.util.*

class DetailListKategoriActivity: AppCompatActivity() {
    private lateinit var binding: ActivityDetailListKategoriBinding
    private var getNoMejaPesanan: String? = null
    private lateinit var pesananHelper: PesananHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailListKategoriBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pesananHelper = PesananHelper.getInstance(applicationContext)
        pesananHelper.open()

        val getData = intent.getParcelableExtra<ItemKategori>(Key.KEY_DETAIL_KATEGORI)!!
        val getKategori = intent.getStringExtra(Key.KEY_KATEGORI)!!
        getNoMejaPesanan = intent.getStringExtra(Key.KEY_NO_MEJA)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getKategori

        binding.idtvKategori.text = getKategori
        binding.idtvNama.text = getData.nama
        binding.idtvDeskripsi.text = getData.deskripsi
        binding.idtvHarga.text = "HARGA : ${getData.harga}"
        Glide.with(this)
            .load(getData.image)
            .fitCenter()
            .into(binding.idimgKategori)

        binding.idbtnPesan.setOnClickListener {
            showDialog(getData, getNoMejaPesanan)
        }
    }

    fun showDialog(data: ItemKategori, noMeja: String?) {
        val title = if (noMeja != null) "Yakin ingin memesan ini?" else "Anda belum memilih No Meja makanan"
        val messege = if (noMeja != null) "No meja : $noMeja \nMenu : ${data.nama} \nHarga : ${data.harga}" else "Pilih No Meja dulu ?"

        val dialog = AlertDialog.Builder(this)
        dialog.setTitle(title)
            .setMessage(messege)
            .setPositiveButton("Ya") {_, _ ->
                if (noMeja != null) {
                    val values = ContentValues()
                    values.put(DatabaseContract.NoteColumn.NOMER_MEJA, noMeja)
                    values.put(DatabaseContract.NoteColumn.NAMA, data.nama)
                    values.put(DatabaseContract.NoteColumn.HARGA, data.harga)
                    values.put(DatabaseContract.NoteColumn.WAKTU, getCurrentDate())
                    val result = pesananHelper.insert(values)
                    if (result > 0) {
                        val intent = Intent(this, ListPesananActivity::class.java)
                        intent.putExtra(Key.KEY_NO_MEJA, noMeja)
                        startActivity(intent)
                    }
                } else {
                    val intent = Intent(this, PesananActivity::class.java)
                    intent.putExtra(Key.KEY_DASHBOARD, "Pilih No Meja")
                    startActivity(intent)
                }
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.cancel()
            }
            .create()
            .show()
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
        val date = Date()

        return dateFormat.format(date)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}