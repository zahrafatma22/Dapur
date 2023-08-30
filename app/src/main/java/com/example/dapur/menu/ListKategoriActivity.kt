package com.example.dapur.menu

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dapur.databinding.ActivityListKategoriBinding
import com.example.dapur.key.Key
import com.example.dapur.model.ItemKategori
import com.example.dapur.model.Kategori

class ListKategoriActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListKategoriBinding
    private var getNoMejaPesanan: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListKategoriBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val getData = intent.getParcelableExtra<Kategori>(Key.KEY_KATEGORI) as Kategori
        supportActionBar?.title = getData.kategori
        source(getData)

        getNoMejaPesanan = intent.getStringExtra(Key.KEY_NO_MEJA)
    }

    fun source(dataKategori: Kategori) {
        val kategori = Kategori(dataKategori.kategori, dataKategori.itemKategori)

        binding.idrvListkategori.apply {
            val adapterRv = ListKategoriAdapter(kategori)
            adapter = adapterRv
            adapterRv.onItemClick(object : ListKategoriAdapter.IOnItemClickCallback {
                override fun onItemClicked(item: ItemKategori) {
                    val intent = Intent(this@ListKategoriActivity, DetailListKategoriActivity::class.java)
                    intent.putExtra(Key.KEY_DETAIL_KATEGORI, item)
                    intent.putExtra(Key.KEY_KATEGORI, dataKategori.kategori)
                    intent.putExtra(Key.KEY_NO_MEJA, getNoMejaPesanan)
                    startActivity(intent)
                }
            })
            layoutManager = GridLayoutManager(this@ListKategoriActivity, 2)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}